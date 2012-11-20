
CONFIG -= qt

TEMPLATE = lib

COMMON_FOLDER = $$IN_PWD/../common
INCLUDEPATH += $$COMMON_FOLDER

LIBS += -lX11 -lXext

HEADERS += \
    fbxshm.h \
    $$COMMON_FOLDER/pixelformat.h \
    $$COMMON_FOLDER/logger.h \
    $$COMMON_FOLDER/mutexlocker.h \
    $$COMMON_FOLDER/mutex.h \
    $$COMMON_FOLDER/framebuffer.h \
    $$COMMON_FOLDER/framebufferapi.h \
    org_javnce_vnc_server_platform_XShmFramebuffer.h

SOURCES += \
    fbxshm.cpp \
    jni_api.cpp \
    $$COMMON_FOLDER/pixelformat.cpp \
    $$COMMON_FOLDER/mutex.cpp \
    $$COMMON_FOLDER/framebufferapi.cpp

