/*
 * Copyright (C) 2012  Pauli Kauppinen
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses/>.
 */
package org.javnce.vnc.client;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.javnce.eventing.*;
import org.javnce.rfb.messages.*;
import org.javnce.rfb.types.*;
import org.javnce.vnc.common.FbFormatEvent;
import org.javnce.vnc.common.FbRequestEvent;
import org.javnce.vnc.common.KeyEvent;
import org.javnce.vnc.common.MessageDispatcher;
import org.javnce.vnc.common.PointerEvent;
import org.javnce.vnc.common.ReceiveMessageFactory;
import org.javnce.vnc.common.ReceivedMsgEvent;
import org.javnce.vnc.common.SocketClosedEvent;

/**
 * The Class VncClient handles the VNC client protocol.
 */
public class VncClient extends Thread implements EventSubscriber, ReceiveMessageFactory {

    /**
     * The supported VNC protocol version.
     */
    static final private Version version = new Version(3, 8);
    /**
     * The message handler.
     */
    private MessageDispatcher messageHandler;
    /**
     * The socket channel.
     */
    private SocketChannel channel;
    /**
     * The server's address.
     */
    final private InetSocketAddress address;
    /**
     * The event loop.
     */
    final private EventLoop eventLoop;
    /**
     * The client observer.
     */
    final private VncClientObserver observer;
    /**
     * The receive message list.
     */
    final private ArrayList<Message> receiveMessages;
    /**
     * The framebuffer format.
     */
    private PixelFormat format;
    /**
     * The factory mode.
     */
    private boolean factoryMode;

    /**
     * Instantiates a new VNC client.
     *
     * @param address the address of server
     * @param observer the observer
     */
    public VncClient(InetSocketAddress address, VncClientObserver observer) {
        this.eventLoop = new EventLoop();
        this.address = address;
        this.observer = observer;
        setName("ClientProtocol");
        receiveMessages = new ArrayList<>();
        receiveMessages.add(new MsgProtocolVersion());
        receiveMessages.add(new MsgSecurityTypeList());
        receiveMessages.add(new MsgSecurityResult());
        receiveMessages.add(new MsgServerInit());
        factoryMode = false;
    }

    /* (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        try {
            channel = SocketChannel.open();
            channel.connect(address);
            eventLoop.subscribe(ReceivedMsgEvent.eventId(), this);
            eventLoop.subscribe(SocketClosedEvent.eventId(), this);
            messageHandler = new MessageDispatcher(eventLoop, channel, this);
            eventLoop.process();
        } catch (Throwable ex) {
            EventLoop.fatalError(this, ex);
        }
        try {
            channel.close();
        } catch (Throwable ex) {
            // We don't care if we are closing already closed socket
            // or should the socket be open again so that closing is successful ???
        }
        observer.connectionClosed();

    }

    /**
     * Shutdown of client.
     */
    public void shutdown() {
        eventLoop.shutdownAllInTheGroup();
    }

    /* (non-Javadoc)
     * @see org.javnce.eventing.EventSubscriber#event(org.javnce.eventing.Event)
     */
    @Override
    public void event(Event event) {
        //logger.info(event.getClass().getName());
        if (ReceivedMsgEvent.eventId().equals(event.Id())) {
            process(((ReceivedMsgEvent) event).get());
        } else if (SocketClosedEvent.eventId().equals(event.Id())) {
            event((SocketClosedEvent) event);
        } else if (KeyEvent.eventId().equals(event.Id())) {
            event((KeyEvent) event);
        } else if (PointerEvent.eventId().equals(event.Id())) {
            event((PointerEvent) event);
        } else if (FbFormatEvent.eventId().equals(event.Id())) {
            event((FbFormatEvent) event);
        } else if (FbRequestEvent.eventId().equals(event.Id())) {
            event((FbRequestEvent) event);
        } else {
            EventLoop.fatalError(this, new UnsupportedOperationException("Unsubscribed event " + event.getClass().getName()));
        }
    }

    /**
     * Socket closed event handler.
     *
     * @param event the event
     */
    private void event(SocketClosedEvent event) {
        shutdown();
    }

    /**
     * Key event handler.
     *
     * @param event the event
     */
    private void event(KeyEvent event) {
        messageHandler.send(new MsgKeyEvent(event.down(), event.key()));
    }

    /**
     * Pointer event handler.
     *
     * @param event the event
     */
    private void event(PointerEvent event) {
        messageHandler.send(new MsgPointerEvent(event.mask(), event.point()));
    }

    /**
     * Framebuffer format change event handler.
     *
     * @param event the event
     */
    private void event(FbFormatEvent event) {
        setPixelFormat(event.get());
        messageHandler.send(new MsgSetPixelFormat(event.get()));
    }

    /**
     * Framebuffer request event handler.
     *
     * @param event the event
     */
    private void event(FbRequestEvent event) {
        //logger.info("");
        messageHandler.send(
                new MsgFramebufferUpdateRequest(event.incremental(), event.rect()));
    }

    /**
     * The message processing.
     *
     * @param msg the message from server
     */
    private void process(Message msg) {
        switch (msg.getId()) {

            case ProtocolVersion:
                handle((MsgProtocolVersion) msg);
                break;
            case SecurityTypeList:
                handle((MsgSecurityTypeList) msg);
                break;
            case SecurityResult:
                handle((MsgSecurityResult) msg);
                break;
            case ServerInit:
                handle((MsgServerInit) msg);
                break;
            case FramebufferUpdate:
                handle((MsgFramebufferUpdate) msg);
                break;
            case Factory:
                process(((MsgClientFactory) msg).get());
                break;
            case Unknown:
                error("Unknown message " + msg);
                break;
            default:
                error("Unsupported message " + msg);
                break;
        }
    }

    /**
     * Protocol error handler.
     *
     * @param text the text
     */
    private void error(String text) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.warning(text);
        //Note that this call causes SocketClosedEvent event
        messageHandler.close();
    }

    /**
     * ProtocolVersion message handler.
     *
     * @param msg the msg
     */
    private void handle(MsgProtocolVersion msg) {
        if (version.equals(msg.get())) {
            messageHandler.send(new MsgProtocolVersion(version));
        } else {
            error("Protocol version " + msg.get() + " not supported");
        }
    }

    /**
     * SecurityTypes message handler.
     *
     * @param msg the msg
     */
    private void handle(MsgSecurityTypeList msg) {
        if (null != msg.getTypes()) {
            messageHandler.send(new MsgSelectedSecurityType(SecurityType.None));
        } else {
            error("SecurityResult failure : " + msg.getText());
        }
    }

    /**
     * Security result message handler.
     *
     * @param msg the msg
     */
    private void handle(MsgSecurityResult msg) {
        if (msg.getStatus()) {
            messageHandler.send(new MsgClientInit(false));
        } else {
            error("SecurityResult failure : " + msg.getText());
        }
    }

    /**
     * End of handshaking handler.
     */
    private void endHandshake() {
        //After server init following can be handled
        eventLoop.subscribe(KeyEvent.eventId(), this);
        eventLoop.subscribe(PointerEvent.eventId(), this);
        eventLoop.subscribe(FbRequestEvent.eventId(), this);
        eventLoop.subscribe(FbFormatEvent.eventId(), this);
        factoryMode = true;
    }

    /**
     * ServerInit message handler.
     *
     * @param msg the msg
     */
    private void handle(MsgServerInit msg) {
        endHandshake();

        messageHandler.send(new MsgSetEncodings(new int[]{Encoding.JaVNCeRLE}));

        setPixelFormat(msg.getFormat());

        observer.initFramebuffer(msg.getFormat(), msg.getSize());
    }

    /**
     * Framebuffer update message handler.
     *
     * @param msg the msg
     */
    private void handle(MsgFramebufferUpdate msg) {
        observer.framebufferUpdate(msg.get());
    }

    /**
     * Pointer event.
     *
     * @param mask the mask
     * @param x the x
     * @param y the y
     */
    public void pointerEvent(int mask, int x, int y) {
        eventLoop.publish(new PointerEvent(mask, new Point(x, y)));
    }

    /**
     * Key event.
     *
     * @param down the down
     * @param key the key
     */
    public void keyEvent(boolean down, long key) {
        eventLoop.publish(new KeyEvent(down, key));
    }

    /**
     * Framebuffer format request.
     *
     * @param format the new format
     */
    public void setFormat(PixelFormat format) {
        eventLoop.publish(new FbFormatEvent(format));
    }

    /**
     * Request framebuffer.
     *
     * @param incremental the incremental
     * @param rect the rect
     */
    public void requestFramebuffer(boolean incremental, Rect rect) {
        eventLoop.publish(new FbRequestEvent(incremental, rect));
    }

    /**
     * Sets the framebuffer format.
     *
     * @param newformat the new pixel format
     */
    private void setPixelFormat(PixelFormat newformat) {
        format = newformat;
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.common.ReceiveMessageFactory#nextReceiveMessage()
     */
    @Override
    public Message nextReceiveMessage() {

        Message msg = null;
        if (factoryMode) {
            msg = new MsgClientFactory(format);
        } else if (!receiveMessages.isEmpty()) {
            msg = receiveMessages.remove(0);
        }
        return msg;
    }
}
