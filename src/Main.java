
/**
 * Just another main class
 */
public class Main  {

    /**
     * Create the server
     */
    public static void main(String[] args) {

        System.out.println("Le serveur se créé");

        // Create the server
        Server server = new Server();

        System.out.println("Le serveur se lance");

        // Starts the server
        server.start();
    }
}