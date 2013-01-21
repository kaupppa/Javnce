
CONFIG -= qt

TEMPLATE = lib


TARGET = ../XTestPointerInputDevice

LIBS += -lX11 -lXext -lXtst

include($$IN_PWD/../common.pri)

HEADERS += ../common/logger.h \
    xtestpointerinputdevice.h \
    $$COMMON_FOLDER/mutexlocker.h \
    $$COMMON_FOLDER/mutex.h \
    org_javnce_vnc_server_platform_XTestPointerInputDevice.h

SOURCES += jni_api.cpp \
    xtestpointerinputdevice.cpp \
    $$COMMON_FOLDER/mutex-linux.cpp
