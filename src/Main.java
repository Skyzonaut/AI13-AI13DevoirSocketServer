
/**
 * Just another main class
 */
public class Main  {

    /**
     * Create the server
     */
    public static void main(String[] args) {

        System.out.println("Le serveur se créé");

        Server server;

        // Create the server
        try {
            // If there is a given port
            if(args.length == 1) {
                server = new Server(Integer.parseInt((String) args[0]));
                System.out.println("Serveur créé sur le port " + args[0]);
            } 
            // Else we create the ServerSocket on the 10810 default port
            else {
                server = new Server();
                System.out.println("Serveur créé sur le port 10810");
            }
        }
        // If the port parameter cannot be parsed we create also the default port server
        catch (NumberFormatException e) 
        {
            System.out.println("Port incompréhensible. Port 10810 par défaut");
            server = new Server();
        }

        System.out.println("Le serveur se lance");

        // Starts the server
        server.start();
    }
}