
package airport.client;

import client.DefaultSocketClient;

/*
    This class starts the client side of the application
 */
public class AirportClient {

	private static String IP = "127.0.0.1";
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) 
	{
		// TODO code application logic here
		AirportClient airportClient = new AirportClient();
		airportClient.startClient();
	}

	public void startClient()
	{
		DefaultSocketClient clientSocket;

		try
		{
			clientSocket = new DefaultSocketClient(IP, 4444);
			clientSocket.start();
		}
		catch(Exception e)
		{
			System.err.println("Failed to connect");
			System.exit(1001);
		}


	}
}
