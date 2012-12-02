
CONFIG -= qt

TEMPLATE = lib

COMMON_FOLDER = $$IN_PWD/../common
INCLUDEPATH += $$COMMON_FOLDER
TARGET = ../XTestKeyBoard
LIBS += -lX11 -lXext -lXtst

SOURCES += xtestkeyboard.cpp \
    modifiermap.cpp \
    keymap.cpp \
    jni_api.cpp \
    $$COMMON_FOLDER/mutex-linux.cpp

HEADERS += xtestkeyboard.h \
    modifiermap.h \
    keymap.h \
    constants.h \
    $$COMMON_FOLDER/mutexlocker.h \
    $$COMMON_FOLDER/mutex.h \
    $$COMMON_FOLDER/logger.h \
    org_javnce_vnc_server_platform_XTestKeyBoard.h

