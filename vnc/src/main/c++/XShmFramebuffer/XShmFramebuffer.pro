
CONFIG -= qt

TEMPLATE = lib

TARGET = ../XShmFramebuffer

LIBS += -lX11 -lXext

include($$IN_PWD/../common.pri)

HEADERS += \
    fbxshm.h \
    $$COMMON_FOLDER/pixelformat.h \
    $$COMMON_FOLDER/logger.h \
    $$COMMON_FOLDER/mutexlocker.h \
    $$COMMON_FOLDER/mutex.h \
    $$COMMON_FOLDER/framebuffer.h \
    $$COMMON_FOLDER/framebufferapi.h \
    org_javnce_vnc_server_platform_XShmFramebuffer.h \
    $$COMMON_FOLDER/abstractframebuffer.h

SOURCES += \
    fbxshm.cpp \
    jni_api.cpp \
    $$COMMON_FOLDER/pixelformat.cpp \
    $$COMMON_FOLDER/mutex-linux.cpp \
    $$COMMON_FOLDER/framebufferapi.cpp

