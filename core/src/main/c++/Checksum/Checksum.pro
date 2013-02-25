
CONFIG -= qt

TEMPLATE = lib


TARGET = ../checksum

include($$IN_PWD/../common.pri)

HEADERS += ../common/logger.h \
    jni_api.h \
    checksum.h

SOURCES += jni_api.cpp \
    checksum.cpp
