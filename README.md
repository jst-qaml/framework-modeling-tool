# eAI Framework Modeling Plugin
This plugin is made to facilitate the Multi-view Modeling Framework for ML System (M3S) inside Astah* System Safety.

For more information on the process: [https://link.springer.com/article/10.1007/s11219-024-09687-z]

## Astah* System Safety

This plugin is made for Astah* System Safety version 8.0.0 ([https://astah.net/release-notes/system-safety-8-0/]). Compatibility with newer version is not guaranteed.

## Preparation

To setup network connection with the backend side, do the following:

1. Access the [SSHFramework.java](src/main/java/ai/engineering/pipeline/SSHConnector.java).
2. Set the IP address, username, password, and port at the following section:
   ```
   //Set the host IP, username, password, and port for training server
    static String host = "";
    static String user = "";
    static String password = "";
    static String port = ""
   ```
3. Set the local directory for temporary files to be stored. The default is D drive root folder.
   ```
   //Set the local folder to hold temporary files
   static String local = "D:";
   ```
4. Compile the plugin.

## Compiling
To create your own version of the binary, follow the Astah* System Safety plugin development tutorial: [https://astah.net/support/plugin-dev-tutorial/plugin-development-setup-for-astah-system-safety/]

## Installation
Intallation can be done in two ways:

- Drop the .jar file into the opened Astah System Safety instance to install.
- Restart the Astah System Safety for the installation to be implemented.

OR

- From the **Plugin** menu, open **Installed Plugins...**
- Click **Install**
- Select the .jar file
- Restart the Astah System Safety for the installation to be implemented.

## Metamodel
Access the metamodel folder to access the integrated metamodel of M3S.

## Manual
More complete instruction on how to operate the plugin is available on the manual below.
[docs/manual.md](docs/manual.md)
