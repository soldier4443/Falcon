# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/native-lib.cpp)

add_library( hasher

             SHARED

             src/main/cpp/hasher.c
             src/main/cpp/cryptonight/aesb.c
             src/main/cpp/cryptonight/blake256.c
             src/main/cpp/cryptonight/blake256.h
             src/main/cpp/cryptonight/epee_warnings.h
             src/main/cpp/cryptonight/groestl.c
             src/main/cpp/cryptonight/groestl.h
             src/main/cpp/cryptonight/groestl_tables.h
             src/main/cpp/cryptonight/hash.c
             src/main/cpp/cryptonight/hash-extra-blake.c
             src/main/cpp/cryptonight/hash-extra-groestl.c
             src/main/cpp/cryptonight/hash-extra-jh.c
             src/main/cpp/cryptonight/hash-extra-skein.c
             src/main/cpp/cryptonight/hash-ops.h
             src/main/cpp/cryptonight/initializer.h
             src/main/cpp/cryptonight/int-util.h
             src/main/cpp/cryptonight/jh.c
             src/main/cpp/cryptonight/jh.h
             src/main/cpp/cryptonight/keccak.c
             src/main/cpp/cryptonight/keccak.h
             src/main/cpp/cryptonight/oaes_config.h
             src/main/cpp/cryptonight/oaes_lib.c
             src/main/cpp/cryptonight/oaes_lib.h
             src/main/cpp/cryptonight/skein.c
             src/main/cpp/cryptonight/skein.h
             src/main/cpp/cryptonight/skein_port.h
             src/main/cpp/cryptonight/slow-hash.c)

include_directories(src/main/cpp/cryptonight/)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       native-lib

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )