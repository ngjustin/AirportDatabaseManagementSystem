
package airport.server;

import java.io.IOException;
import java.net.ServerSocket;
import server.DefaultSocketServer;

/*
    This class starts the server side of the application
*/
public class AirportServer 
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        // TODO code application logic here
        AirportServer airportServer = new AirportServer();
        airportServer.startServer();
        
    }
    
    public void startServer()
    {
        ServerSocket serverSocket= null;
        
        try
        {
            serverSocket = new ServerSocket(4444);
        }
        catch(IOException e)
        {
            System.err.println("Could not listen on port: 4444.");
	    System.exit(1);
        }
        
        //Starts the database so it can load data
        new database.MainDatabase();
        
        try
        {
            while(true)
            {
                DefaultSocketServer server = new DefaultSocketServer(4444, serverSocket.accept());
                server.start();
            }
        }
        catch(IOException e)
        {
            System.err.println("Accept failed.");
	    System.exit(2);
        }
    }
    
}
