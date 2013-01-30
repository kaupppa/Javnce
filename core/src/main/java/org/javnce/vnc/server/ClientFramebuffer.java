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
package org.javnce.vnc.server;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.javnce.eventing.Event;
import org.javnce.eventing.EventLoop;
import org.javnce.eventing.EventSubscriber;
import org.javnce.rfb.types.Encoding;
import org.javnce.rfb.types.Framebuffer;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Rect;
import org.javnce.rfb.types.Size;
import org.javnce.vnc.common.FbChangeEvent;
import org.javnce.vnc.common.FbEncodingsEvent;
import org.javnce.vnc.common.FbFormatEvent;
import org.javnce.vnc.common.FbRequestEvent;
import org.javnce.vnc.common.FbUpdateEvent;
import org.javnce.vnc.server.platform.FramebufferDevice;
import org.javnce.vnc.server.platform.PlatformController;

/**
 * The Class ClientFramebuffer handles framebuffer preparing for one client.
 */
class ClientFramebuffer extends Thread implements EventSubscriber {

    /**
     * The nb.
     */
    final private FramebufferDevice nb;
    /**
     * The size.
     */
    final private Size size;
    /**
     * The format.
     */
    final private PixelFormat format;
    /**
     * The supported encodings.
     */
    private int[] supportedEncodings;
    /**
     * The used encoding.
     */
    private int usedEncoding;
    /**
     * The requested area.
     */
    private Rect requestedArea;
    /**
     * The dirty areas.
     */
    final private RectContainer dirtyAreas;
    /**
     * The force full.
     */
    private boolean forceFull;
    /**
     * The event loop.
     */
    final private EventLoop eventLoop;

    /**
     * Instantiates a new client framebuffer.
     *
     * @param parent the parent
     */
    ClientFramebuffer(EventLoop parent) {
        this.eventLoop = new EventLoop(parent);
        nb = PlatformController.instance().getPlatformManager().getFramebufferDevice();
        size = nb.size();
        format = nb.format();
        dirtyAreas = new RectContainer();
        forceFull = true;
        usedEncoding = Encoding.RAW;
        setName("Javne-ClientFramebuffer");
    }

    /* (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        eventLoop.subscribe(FbRequestEvent.eventId(), this);
        eventLoop.subscribe(FbFormatEvent.eventId(), this);
        eventLoop.subscribe(FbEncodingsEvent.eventId(), this);
        eventLoop.subscribe(FbChangeEvent.eventId(), this);

        eventLoop.process();
    }

    /* (non-Javadoc)
     * @see org.javnce.eventing.EventSubscriber#event(org.javnce.eventing.Event)
     */
    @Override
    public void event(Event event) {
        if (FbFormatEvent.eventId().equals(event.Id())) {
            event((FbFormatEvent) event);
        } else if (FbEncodingsEvent.eventId().equals(event.Id())) {
            event((FbEncodingsEvent) event);
        } else if (FbRequestEvent.eventId().equals(event.Id())) {
            event((FbRequestEvent) event);
        } else if (FbChangeEvent.eventId().equals(event.Id())) {
            event((FbChangeEvent) event);
        } else {
            EventLoop.fatalError(this, new UnsupportedOperationException("Unsubscribed event " + event.getClass().getName()));
        }
    }

    /**
     * Framebuffer format change event handler.
     *
     * @param event the event
     */
    private void event(FbFormatEvent event) {
        if (!event.get().equals(this.nb.format())) {
            EventLoop.fatalError(this, new UnsupportedOperationException("Not implemented event : FbFormatEvent"));
        }
        forceFull = true;
    }

    /**
     * Client supported encodings event handler.
     *
     * @param event the event
     */
    private void event(FbEncodingsEvent event) {
        supportedEncodings = event.get();
        usedEncoding = Encoding.RAW;

        for (int encoding : supportedEncodings) {
            if (encoding == Encoding.JaVNCeRLE) {
                usedEncoding = encoding;
                break;
            }
        }
    }

    /**
     * Framebuffer request event handler.
     *
     * @param event the event
     */
    private void event(FbRequestEvent event) {
        this.requestedArea = event.rect();

        if (!event.incremental() || forceFull) {
            publishFrameBuffer(new Rect[]{new Rect(0, 0, size.width(), size.height())});
        } else if (!dirtyAreas.isEmpty()) {
            publishFrameBuffer(dirtyAreas.get());
        }
    }

    /**
     * Framebuffer change event handler.
     *
     * @param event the event
     */
    private void event(FbChangeEvent event) {

        dirtyAreas.add(event.get());

        if (null != requestedArea) {
            publishFrameBuffer(dirtyAreas.get());
        }
    }

    /**
     * Preparer framebuffers for client.
     *
     * @param rect the area
     * @param buffers the buffers
     * @return the framebuffer
     */
    private Framebuffer createFB(Rect rect, ByteBuffer buffers[]) {
        Framebuffer result;

        if (usedEncoding == Encoding.JaVNCeRLE) {

            List<ByteBuffer> list = new ArrayList<>();
            int bytesPerPixel = format.bytesPerPixel();

            for (int i = 0; i < buffers.length; i++) {
                buffers[i].clear();
                List<ByteBuffer> rleBuffers = RunLengthEncoder.encode(buffers[i], bytesPerPixel);
                list.addAll(rleBuffers);
            }

            result = new Framebuffer(rect, usedEncoding, list.toArray(new ByteBuffer[list.size()]));
        } else {
            result = new Framebuffer(rect, usedEncoding, buffers);
        }

        return result;
    }

    /**
     * Publish framebuffer for sending to client.
     *
     * @param areas the areas
     */
    private void publishFrameBuffer(Rect[] areas) {
        List<Framebuffer> list = new ArrayList<>();

        for (int i = 0; i < areas.length; i++) {
            if (requestedArea.overlaps(areas[i])) {
                Rect rect = requestedArea.overlapping(areas[i]);
                ByteBuffer[] buffers = nb.buffer(rect.x(), rect.y(), rect.width(), rect.height());
                list.add(createFB(rect, buffers));

            }
        }

        if (0 != list.size()) {
            eventLoop.publish(new FbUpdateEvent(list.toArray(new Framebuffer[list.size()])));
            forceFull = false;
            requestedArea = null;
            dirtyAreas.clear();
        }
    }
}
