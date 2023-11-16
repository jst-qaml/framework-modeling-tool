# framework-modeling-tool

Astah System Safety plugin to support the framework team's process.

## installation

Drop the .jar file in the releases into the opened Astah System Safety instance to install. Restart the Astah System
Safety for the installation to be implemented.

## metamodel

Access the metamodel folder for accessing the integrated metamodel of M3S.

## case study

Access the case study folder for accessing the Astah System Safety files of the traffic sign recognition case study.

## Dev Environment

### Prerequisites

- [Astah SDK](https://astah.net/support/plugin-dev-tutorial/plugin-development-setup-for-astah-system-safety/)
    - MISSING IN THE DOCS:`M2_HOME` has to be set to the sdk directory (e.g. `C:\dev\safety-plugin-SDK-1.0`)
- [openJDK 8 with HotSpot JVM](https://adoptium.net/en-GB/temurin/releases/?os=windows&arch=x64&package=jdk&version=8)
    - different JDK 8 versions might work
    - Oracle JDK is currently borked (#TODO)

### Running Astah with the plugin

Build the plugin with `astah-build` and launch with `astah-launch` or `astah-debug`. When debugging, connect to
port `44000`. For a slightly nicer way to complete those steps: Create a debug configuration in your IDE that
runs `astah-mvn clean compile astah:debug`.

### Maven

Please make sure to use `astah-mvn` instead of your own maven installation. Set the `MAVEN_HOME` in your IDE according
to the [Plug-in Development Guide](https://astah.net/support/plugin-dev-tutorial/sample-plugin/).