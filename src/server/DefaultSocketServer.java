
package server;

import Entities.Customer;
import database.MainDatabase;
import java.io.*;
import java.net.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DefaultSocketServer extends Thread implements SocketClientInterface, SocketClientConstants 
{
	private ObjectInputStream in;
	private ObjectOutputStream out;

	private Socket sock;
	private String strHost;
	private int iPort;

	private final static int PASSED = 1000;
	private final static int FAILED = 0111;
	private final static boolean UPDATE_STACK = true;

	public DefaultSocketServer(String strHost, int iPort) 
	{       
		setPort (iPort);
		setHost (strHost);
	}//constructor

	public DefaultSocketServer(int iPort, Socket s) 
	{   
		setPort (iPort);
		sock = s;
		setHost(sock.getInetAddress().toString());
	}//constructor

	public void run()
	{
		if (openConnection())
		{
			handleSession();

			closeSession();
		}

	}//run

	public boolean openConnection()
	{
		try 
		{
			out = new ObjectOutputStream(sock.getOutputStream());
			in = new ObjectInputStream(sock.getInputStream()); 
		}
		catch (Exception e)
		{
			if (DEBUG) 
				postError("Unable to obtain stream to/from " + strHost);
			return false;
		}
		return true;
	}

	/*
        Method handles server processes and sends information to client.
        Method also recives information from client and acts accordingly to 
        the clients response
	 */
	public void handleSession()
	{
		if (DEBUG) 
			System.out.println ("Handling session with " + strHost + ":" + iPort);

		MainDatabase mb = new MainDatabase();

		int processId = 0;
		do
		{
			processId = getInputStreamCode();

			if(processId > 0 && processId < 10)
			{
				if(processId == 3)
				{
					Object[] o = (Object[])getInputStream();
					if( mb.storeCustomer(new Customer( (long)o[2], (String)o[0], (String)o[1], (boolean) o[3] ), UPDATE_STACK) )
					{
						setOutputStream("Passenger stored in database");
					}
					else
						setOutputStream("Passenger failed to be stored in database");
				}
				else if(processId == 2)
				{
					Object o = getInputStream();
					Object[] obj = (Object[])mb.boardPassenger((int)o);

					if(obj == null)
					{
						setOutputStream(FAILED, obj);
					}
					else
						setOutputStream(PASSED, obj);
				}
				else if(processId == 4)
				{
					long phoneNumber = (long)getInputStream();
					if(mb.removeCustomer( phoneNumber, UPDATE_STACK))
					{
						setOutputStream(PASSED, "Passenger removed from database");
					}
					else
						setOutputStream(FAILED, "Failed to remov passenger from database");
				}
				else if(processId == 5)
				{
					int flightNumber = (int) getInputStream();
					setOutputStream(PASSED, (Object) mb.getFlight(flightNumber) );
				}
				else if(processId == 6)
				{
					//process Ticket
					Object[] obj = (Object[]) getInputStream();
					long phoneNumber = (long)obj[0];
					int flightNumber = (int) obj[1];
					String flightClass = (String) obj[2];

					Customer c = mb.getCustomer(phoneNumber);

					if( mb.processTicket(flightNumber, c, flightClass, UPDATE_STACK) )
					{
						setOutputStream(PASSED, "" + c.getFullName() + " entered in flight #" + flightNumber);
					}
					else
						setOutputStream(FAILED, "Failed to insert " + c.getFullName() + " to flight #" + flightNumber);


				}
				else if(processId == 7)
				{

					String[] s = mb.findAllFlights();

					if(s != null)
					{
						setOutputStream(PASSED, s);
					}
					else 
						setOutputStream(FAILED, s);
				}
				else if(processId == 8)
				{
					String[] s = mb.findAllCustomers();

					if(s != null)
					{
						setOutputStream(PASSED, s);
					}
					else 
						setOutputStream(FAILED, s);
				}
				else if(processId == 9)
				{
					if(mb.undo())
						setOutputStream(PASSED, mb.getUndoMessage());
					else
						setOutputStream(FAILED, mb.getUndoMessage());

				}
			}
			else if(processId >= 10 && processId < 100)
			{
				if(processId == 10)
				{
					try
					{
						long phoneNumber = (long)getInputStream();
						Object obj = (Object)mb.findCustomer(phoneNumber);

						if(obj == null)
						{
							setOutputStream(FAILED, obj);
						}
						else
							setOutputStream(PASSED, obj);
					}
					catch(Exception e)
					{
						setOutputStream(FAILED, "Data currupted");
					}


				}
				else if(processId == 20)
				{
					String fullName = (String) getInputStream();

					Object[] obj = (Object[]) mb.findCustomer(fullName);

					setOutputStream(FAILED, obj);

				}
			}
			else if(processId != 0)
				postMessage("Got processId: " + processId);

		}while(processId != 0 && processId != -1);



	}       

	public void closeSession()
	{
		try 
		{
			out = null;
			in = null;
			postMessage("Closing socket to " + strHost);
			sock.close();
			this.stop();
		}
		catch (IOException e)
		{
			if (DEBUG) 
				postError("Error closing socket to " + strHost);
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
			//Logger.getLogger(ServerSocket.class.getName()).log(Level.SEVERE, null, e);
			return -1;
		}
		catch(ClassNotFoundException e)
		{
			//Logger.getLogger(ServerSocket.class.getName()).log(Level.SEVERE, null, e);
			return -1;
		}
	}

	public void postMessage(String message)
	{
		final String TALKING = "[Server/" + strHost + "]: ";

		System.out.println(TALKING + message);
	}

	public void postError(String message)
	{
		final String TALKING = "[Server/" + strHost + "]: ";

		System.err.println(TALKING + message);
	}

}// class DefaultSocketServer
