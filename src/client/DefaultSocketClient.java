
package client;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import scripts.Script;

public class DefaultSocketClient extends Thread implements SocketClientInterface, SocketClientConstants 
{

	private ObjectInputStream in;
	private ObjectOutputStream out;

	private Socket sock;
	private String strHost;
	private int iPort;

	private static int PASSED = 1000;
	private static int FAILED = 0111;
	public DefaultSocketClient(String strHost, int iPort) 
	{       
		setPort (iPort);
		setHost (strHost);
	}//constructor

	public DefaultSocketClient(int iPort, Socket s) 
	{   
		setPort (iPort);
		setHost (s.toString());
		sock = s;
	}//constructor

	public void run()
	{
		//establishes connection
		if (openConnection())
		{
			handleSession();
			closeSession();
		}

	}//run

	@Override
	public boolean openConnection()
	{
		try 
		{
			sock = new Socket(strHost, iPort);   
		}
		catch(IOException socketError)
		{
			if (DEBUG) 
				System.err.println("Unable to connect to " + strHost);
			return false;
		}
		try 
		{
			out = new ObjectOutputStream(sock.getOutputStream());
			in = new ObjectInputStream(sock.getInputStream()); 
		}
		catch (Exception e)
		{
			if (DEBUG) 
				System.err.println("Unable to obtain stream to/from " + strHost);
			return false;
		}
		return true;
	}

	/*
        Method handles client processes and sends information to server.
        Method also recives information from server
	 */
	@Override
	public void handleSession()
	{
		if (DEBUG) 
			System.out.println ("Handling session with " + strHost + ":" + iPort);


		Script script = new Script();

		int processId;

		do
		{
			processId = script.mainMenu();

			if(processId > 0 && processId <= 9)
			{
				if(processId == 3)
				{
					this.setOutputStream(processId, script.createPassenger());
					script.displayMessage((String) this.getInputStream());
				}
				else if(processId == 2)
				{
					this.setOutputStream(processId, script.board());
					int code = getInputStreamCode();
					script.board((String[])getInputStream());
				}
				else if(processId == 4)
				{
					//Delete
					this.setOutputStream(processId, (Object)script.searchPassengerByNumber());
					int code = getInputStreamCode();
					script.displayMessage((String) this.getInputStream());
				}
				else if(processId == 5)
				{
					int flightNumber = script.searchFlights();
					this.setOutputStream(processId, flightNumber);
					int code = this.getInputStreamCode();
					script.displayMessage((String) this.getInputStream());
				}
				else if(processId == 6)
				{
					Object[] obj = script.giveTicket();
					this.setOutputStream(processId, obj);
					int code = this.getInputStreamCode();
					script.displayMessage((String) this.getInputStream());
				}
				else if(processId == 7)
				{
					setOutputStream(processId);
					int code = this.getInputStreamCode();
					script.displayMessage((String[]) this.getInputStream());
				}
				else if(processId == 8)
				{   
					setOutputStream(processId);
					int code = this.getInputStreamCode();
					script.displayMessage((String[]) this.getInputStream());

				}
				else if(processId == 9)
				{
					//Undo
					this.setOutputStream(processId);
					int code = this.getInputStreamCode();
					script.displayMessage((String) this.getInputStream());
				}
			}
			else if(processId >= 10 && processId <= 90)
			{
				if(processId == 10)
				{
					this.setOutputStream(processId, (Object)script.searchPassengerByNumber());
					int code = getInputStreamCode();
					script.displayMessage((String) this.getInputStream());
				}
				else if(processId == 20)
				{
					String fullName = script.searchCustomerByName();
					this.setOutputStream(processId, fullName);
					int code = getInputStreamCode();
					script.displayMessage((String[]) getInputStream());
				}
				else if(processId == 30)
				{
					//ignore
				}

			}


		}while(processId != 0);

	}       

	public void closeSession()
	{
		try 
		{
			out = null;
			in = null;
			sock.close();
			this.stop();
		}
		catch (IOException e)
		{
			if (DEBUG) 
				System.err.println("Error closing socket to " + strHost);
		}       
	}

	private void setHost(String strHost)
	{
		this.strHost = strHost;
	}

	private void setPort(int iPort)
	{
		this.iPort = iPort;
	}

	public void setOutputStream(int code, Object object)
	{
		try 
		{
			out.writeObject(code);
			out.writeObject(object);
		} 
		catch (IOException ex) 
		{
			Logger.getLogger(ServerSocket.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void setOutputStream(Object object)
	{
		try 
		{
			out.writeObject(object);
		} 
		catch (IOException ex) 
		{
			Logger.getLogger(ServerSocket.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public Object getInputStream() 
	{
		try
		{
			return in.readObject();
		}
		catch(IOException e)
		{
			Logger.getLogger(ServerSocket.class.getName()).log(Level.SEVERE, null, e);
			return null;
		}
		catch(ClassNotFoundException e)
		{
			Logger.getLogger(ServerSocket.class.getName()).log(Level.SEVERE, null, e);
			return null;
		}
	}

	public int getInputStreamCode()
	{
		try
		{
			return (int)in.readObject();
		}
		catch(IOException e)
		{
			Logger.getLogger(ServerSocket.class.getName()).log(Level.SEVERE, null, e);
			return -1;
		}
		catch(ClassNotFoundException e)
		{
			Logger.getLogger(ServerSocket.class.getName()).log(Level.SEVERE, null, e);
			return -1;
		}
	}



}// class DefaultSocketClient
