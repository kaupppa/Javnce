

!isEmpty( $$JAVA_INCLUDE_PATHS ) {
    INCLUDEPATH += $$JAVA_INCLUDE_PATHS

} else {
    JDK_ROOT=/home/pauli/jdk1.7.0_09
    INCLUDEPATH += $$JDK_ROOT/include/
    INCLUDEPATH += $$JDK_ROOT/include/linux/
}
!isEmpty( $$JAVNCE_INCLUDE_PATHS ) {
    INCLUDEPATH += $$JAVNCE_INCLUDE_PATHS

} else {
    INCLUDEPATH += $$IN_PWD/../../../target/libs/
}



COMMON_FOLDER = $$IN_PWD/common
INCLUDEPATH += $$COMMON_FOLDER
