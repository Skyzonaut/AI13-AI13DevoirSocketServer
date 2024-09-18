import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Class handling the new client socket connection.
 * The server will create the {@link ClientSocket} associated with the client connection socket,
 * and handle the communication between all of them.
 * @see Thread
 * @see Socket
 */
public class Server {

    /**
     * {@link HashMap} that will store the client connection thread represented by the {@link ClientSocket}
     * class. Associated with their client's pseudo has the key
     */
    private final HashMap<String, ClientSocket> clients = new HashMap<>();

    /**
     * {@link ServerSocket} the client socket will connect to on port 10810
     */
    private ServerSocket server;

    /**
     * Constructor of the Server class. It will create a new {@link ServerSocket} on port 10810 and
     * store it in the attribute {@link Server#server}.
     * The port is 10810 by default
     */
    public Server() {
        try {
            this.server = new ServerSocket(10810);
        } catch (IOException ioError) {
            System.out.println(ioError.getMessage());
            System.out.println("%% Erreur dans la création du serveur");
        }
    }

    /**
     * Constructor of the Server class on the given port. It will create a new {@link ServerSocket} on port 10810 and
     * store it in the attribute {@link Server#server}
     * @param port Port to create the ServerSocket on
     */
    public Server(int port) {
        try {
            this.server = new ServerSocket(port);
        } catch (IOException ioError) {
            System.out.println(ioError.getMessage());
            System.out.println("%% Erreur dans la création du serveur");
        }
    }

    /**
     * This method is run by the thread when it executes.
     * This method is not intended to be invoked directly.
     * This method will loop indefinitely to catch incoming client socket connection, store it
     * in the {@link Server#clients}. Then it will create a new {@link ClientSocket} to handle the
     * IO communication between the client and itself.
     * @see Thread#run()
     */
    public void start() {
        try {
            // While the serverSocket is open
            while(!this.server.isClosed()) {

                // Accept all incoming connection sockets from the clients
                Socket socket = this.server.accept();

                // Create a new ClientSocket to handle the io interactions and passes it the connection socket
                ClientSocket newClientSocket = new ClientSocket(socket, this);

                // Store the ClientSocket thread in the client map
                String pseudo = newClientSocket.getPseudo();
                clients.put(pseudo, newClientSocket);

                // Start the new threads
                newClientSocket.start();

                this.broadcast(String.format("== %s a rejoint le chat ==", pseudo));
            }
        } 
        catch (IllegalThreadStateException illThreadException) 
        {
            System.out.println("%% Erreur dans la création des threads");
            System.out.println("%% " + illThreadException.getMessage());
        } 
        catch (IOException ioError) 
        {
            System.out.println("%% Erreur dans la création du serveur");
            System.out.println("%% " + ioError.getMessage());
        }
    }

    /**
     * Method that will send the incoming message from a ClientSocket to all others
     * @param originPseudo ClientSocket which sent the message
     * @param msg The message to send
     */
    public void broadcast(String originPseudo, String msg) {
        // Also print the message to the Server stdout to see all exchanges
        System.out.println(originPseudo + " > " + msg);

        // Loop through all ClientSocket and send the message to all others than the original sender
        for(Map.Entry<String, ClientSocket> entry : this.clients.entrySet()) {
            if(entry.getKey().equals(originPseudo)) continue;
            entry.getValue().msg("<" + originPseudo + "> " + msg);
        }
    }
    /**
     * Method that will send a message to all ClientSockets
     * @param msg The message to send
     */
    public void broadcast(String msg) {
        System.out.println(msg);
        for(Map.Entry<String, ClientSocket> entry : this.clients.entrySet()) {
            entry.getValue().msg(msg);
        }
    }

    /**
     * Method handling the termination request of a ClientSocket
     * @param clientSocket ClientSocket which sent the termination request
     */
    public void killThread(ClientSocket clientSocket) {
        // Interruption of the thread which asked for termination
        this.clients.get(clientSocket.pseudo).interrupt();

        // Removes it from the list of active ClientSocker
        this.clients.remove(clientSocket.pseudo);

        // Display to all other ClientSocket that one disconnected
        this.broadcast(String.format("== %s a quitté le chat ==", clientSocket.pseudo));
    }

    /**
     * Getter retrieving the pseudo of all connected users.
     * @return Concatenated list of all users pseudo
     */
    public String getClientsPseudo() {
        return String.join(";;", clients.keySet());
    }
}
