
CONFIG -= qt

TEMPLATE = lib

COMMON_FOLDER = $$IN_PWD/../common
INCLUDEPATH += $$COMMON_FOLDER

LIBS += -lX11 -lXext -lXtst

HEADERS += ../common/logger.h \
    xtestpointerinputdevice.h \
    $$COMMON_FOLDER/mutexlocker.h \
    $$COMMON_FOLDER/mutex.h \
    org_javnce_vnc_server_platform_XTestPointerInputDevice.h

SOURCES += jni_api.cpp \
    xtestpointerinputdevice.cpp \
    $$COMMON_FOLDER/mutex.cpp
