
#read JAVNCE_INCLUDE_PATHS env variable into TEMP_INCLUDE_PATHS variable
TEMP_INCLUDE_PATHS=$$(JAVNCE_INCLUDE_PATHS)

#if TEMP_INCLUDE_PATHS variable is empty set hardcode for qcreator useage
isEmpty( TEMP_INCLUDE_PATHS ) {
    JDK_ROOT=/home/pauli/jdk1.7.0_09
    INCLUDEPATH += $$JDK_ROOT/include/
    INCLUDEPATH += $$JDK_ROOT/include/linux/
    INCLUDEPATH += $$IN_PWD/../../../target/libs/
} else {
    INCLUDEPATH += $$(JAVNCE_INCLUDE_PATHS)
}

COMMON_FOLDER = $$IN_PWD/common
INCLUDEPATH += $$COMMON_FOLDER

message($$INCLUDEPATH)
message($$(JAVA_INCLUDE_PATHS))

