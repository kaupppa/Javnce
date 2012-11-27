Javnce - desktop sharing client and server.
===========================================

The Javnce is simple JavaFX application to share and to connect 
to shared desktop. 

You can share your desktop in two modes, view only and full access 
mode. In view only mode, connected clients does not have access to 
mouse or keyboard. In full access mode client have remote access to 
mouse or keyboard. Note that when sharing your desktop, client gets 
the access only if you accept the connection.

When connecting to remote desktop, the Javnce shows desktops that 
are shared. All you need to do is pick one and wait that it is accepted.

Build requirements.
===================

Linux:
- Java with JavaFX support, that is latest oracle jdk.
- qt4-dev-tools package
- build-essential package
- libxtst-dev package

Known issues.
=============
JavaFX Native Installers for linux has a bug. The libjvm.so is missing 
from debian package

Workaround is the manually fix it after installing javnce-application-1.0.deb.
Just create link as following:
- mkdir -p /opt/javnce-application/runtime/jre/lib/amd64/client/
- cd /opt/javnce-application/runtime/jre/lib/amd64/client/
- ln -s /opt/javnce-application/runtime/jre/lib/amd64/server/libjvm.so
