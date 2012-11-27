# Javnce - desktop sharing client and server.
The Javnce is simple JavaFX application to share and to connect 
to shared desktop. 

## Sharing desktop.
You can share your desktop in two modes, view only and full access 
mode. In view only mode, connected clients does not have access to 
mouse or keyboard. In full access mode client have remote access to 
mouse or keyboard. Note that when sharing your desktop, client gets 
the access only if you accept the connection.

## Connecting to shared desktop.
When connecting to remote desktop, the Javnce shows desktops that 
are shared. All you need to do is pick one and wait that it is accepted.

# The dirty details
## Build requirements.
### Linux
- Java jdk with JavaFX support, that is latest oracle jdk.
- qt4-dev-tools package for jni part
- build-essential package for jni part
- libxtst-dev package for jni part

## Known issues.
1. JavaFX Native Installers for linux has a bug with Java runtime libraries
   Workaround is the manually fix it after installing javnce-application-1.0.deb.
   Just create symolic link as following:
   - mkdir -p /opt/javnce-application/runtime/jre/lib/amd64/client/
   - cd /opt/javnce-application/runtime/jre/lib/amd64/client/
   - ln -s /opt/javnce-application/runtime/jre/lib/amd64/server/libjvm.so

2. JavaFX ImageView and WritableImage
   The new JavaFX image is missing lost of features that Swing image have. 
   Run the example.swingclient.SwingClient to see the difference.

