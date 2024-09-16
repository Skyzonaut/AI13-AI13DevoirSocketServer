import java.io.*;
import java.net.Socket;
import java.net.SocketException;

/**
 * Class extending the {@link Thread} class in order to encapsulate a connection to a given
 * {@link Socket}, and listen to its incoming data through a {@link BufferedReader} as much as sending
 * some to it.
 * @see Thread
 */
public class ClientSocket extends Thread {

    /**
     * Pseudo of the user that connected to the other side of the socket
     */
    public String pseudo;

    /**
     * Socket containing a client's connection to the server through a {@link Socket}
     */
    public Socket socket;

    /**
     * <b>BufferedReader</b> listening to the given socket incoming stream
     */
    private BufferedReader in;

    /**
     * PrintWriter retrieved from the {@link ClientSocket#socket} that will be used to send to the client
     * data coming from the server or other clients
     */
    private PrintWriter out;

    /**
     * Reference to the main server so the ClientSocket can delegate it the communication with other ClientSocket
     */
    private Server server;

    /**
     * Constructor of the ClientSocket class. Instantiate and return a ClientSocket
     * containing the socket connection to the client, and a reference of the Server.
     * Also retrieve the pseudo from the client and stores it.
     * @param socket The SocketConnection to the client
     * @param server The Server reference that will be used to delegate communication to other ClientSocket
     */
    public ClientSocket(Socket socket, Server server) {
        try {
            // Common constructor instantiation
            this.socket = socket;
            this.server = server;
            
            // Get the BufferedReader from the socket
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

            // Get the PrintWriter from the socket
            this.out = new PrintWriter(this.socket.getOutputStream(), true);

            // Send to the client the list of all pseudo already used by connected users
            // We could also pass it in parameters tho
            this.out.println(this.server.getClientsPseudo());

            // Read the client pseudo from the incoming client stream.
            // The pseudo is the first thing requested by the client application. And the first inputs will
            // always be its pseudo. So we retrieve it
            this.pseudo = this.in.readLine();
            System.out.println(this.pseudo);
        } 
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Starts the handling of the client IO communication
     * This method is run by the thread when it executes.
     * This method is not intended to be invoked directly.
     * This method will loop indefinitely waiting for incoming data, read it and ask the server to send it to other
     * {@link ClientSocket}. This loop stops when the incoming data is the termination request "exit". Then close
     * all opened stream
     * @see Thread#run()
     */
    @Override
    public void run() {
        try {
            // As long as the socket is on. We let the Server kill the child Thread to avoid zombie process
            while (!this.socket.isClosed()) {

                // We read the incoming data
                String inputString = this.in.readLine();

                // If the incoming data is "exit" we must terminate the socket and ask the server to terminate the
                // child thread
                if(inputString.equalsIgnoreCase("exit")) {
                    // Call the method killing all threads and streams
                    this.exit();
                }
                // For any other incoming data, ask the server to send it to all other ClientSockets
                else {
                    this.server.broadcast(this.pseudo, inputString);
                }
            }
        }
        catch (Exception e) {
            if(e instanceof SocketException) {
                System.out.println("%% Erreur connection : " + this.pseudo);
                this.exit();
            } else {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Method closing all streams and asking to the server to kill the current ClientSocket Thread
     */
    private void exit() {
        try {
            // Close IO Stream
            this.in.close();
            this.out.close();
    
            // Close the socket thus stoping the loop
            this.socket.close();
    
            // Ask the server to terminate this
            this.server.killThread(this);
        } 
        catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method sending a message to the client through this end of the connection {@link ClientSocket#socket}
     * @param msg Message to send to the client
     */
    public void msg(String msg) {
        this.out.println(msg);
    }

    /**
     * Pseudo getter
     * @return {@link ClientSocket#pseudo}
     */
    public String getPseudo() {
        return this.pseudo;
    }
    
}
