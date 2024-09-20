### Java Socket messenger server application

This application is the server side of a wider messaging application made of two executable jar and associated packages
- ServerSide
- ClientSide

The current application will create a Socket Server and wait for client connection through Socket Connections. 
Then it will manage incoming connection and broadcast client's messages to other connected clients.

### Launch
To launch the server, simply go to the main directory.
Then execute this command :

````bash
java -jar DevoirSocket.jar
````

The server will be launched on the default `localhost` on the 
`10810` port.
However, it is possible to select the port we want to launch the server on.

To do this, simply add it as a parameter to the launch command

````bash
java -jar DevoirSocket.jar <port>
````

### Exceptions
If a client disconnect by choice by entering the `exit` command.
The server will prompt to its stdout and broadcast to the clients that the user has disconnected.

If a client disconnect improperly (Ctrl-C / Ctrl-Q), the server will catch the connection error and display it, on top of displaying the disconnection to others
