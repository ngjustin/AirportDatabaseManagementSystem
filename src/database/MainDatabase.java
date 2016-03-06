
package database;

import Entities.Customer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import services.BoardingTicket;
import services.Flight;
import util.UndoStack;
import util.Variable;


public class MainDatabase
{
	//shared session data structures
	private static final HashMap<Long, Customer> hm = new HashMap();

	private static final BinarySearchTree<Customer> bst = new BinarySearchTree<>(
			new Comparator<Customer>()
			{
				@Override
				public int compare(Customer one, Customer two)
				{
					String oneName = one.getFullName();
					String twoName = two.getFullName();

					int result = oneName.compareTo(twoName);
					return result;
				}
			});

	private static final AvlTree<Flight> flights = new AvlTree<>(
			new Comparator<Flight>()
			{

				@Override
				public int compare(Flight f1, Flight f2)
				{
					int result = Integer.compare(f1.getFlightNumber(), f2.getFlightNumber());
					return result;
				}
			});

	private static final Map map = new Map();


	//Session variables
	private UndoStack us;

	//Shared session constants
	private static boolean isRunning;
	private final static String CUSTOMER_NOT_FOUND_MESSAGE = "Customer not found";
	private final static String FLIGHT_NOT_FOUND_MESSAGE = "Flight not found";
	private static final String TALKING = "[MainDatabase]: ";


	public MainDatabase()
	{
		us = new UndoStack();

		if(!isRunning)
		{ 
			loadTestData();
			isRunning = true;
		}
	}

	public static void main(String args[])
	{
		MainDatabase md = new MainDatabase();
		md.postMessage("Running in main");

	}

	/*
    Methods loads test data for
	 */
	private void loadTestData()
	{

		Customer[] c = new Customer[10];
		int i = 0;
		int flightNumber = 1;
		String[][] path;
		String dist;
		String source = "San Jose, CA";

		dist = randomDist();
		path = getPath(dist);
		flights.insert(new Flight(1, path, source, dist));

		dist = randomDist();
		path = getPath(dist);
		flights.insert(new Flight(2, path, source, dist));

		dist = randomDist();
		path = getPath(dist);
		flights.insert(new Flight(3, path, source, dist));

		dist = randomDist();
		path = getPath(dist);
		flights.insert(new Flight(4, path, source, dist));

		dist = randomDist();
		path = getPath(dist);
		flights.insert(new Flight(5, path, source, dist));

		dist = randomDist();
		path = getPath(dist);
		flights.insert(new Flight(6, path, source, dist));

		dist = randomDist();
		path = getPath(dist);
		flights.insert(new Flight(7, path, source, dist));

		c[i++] = new Customer(0000000, "Justin", "Ng");

		c[i++] = new Customer(1111111, "Potato", "Salad");

		c[i++] = new Customer(2222222, "John", "Cena");

		c[i++] = new Customer(3333333, "Barack", "Obama");

		c[i++] = new Customer(4444444, "John", "Cena");

		c[i++] = new Customer(5555555, "South", "East");

		c[i++] = new Customer(6666667, "Eynak", "Tsae");

		c[i++] = new Customer(7777777, "Turkey", "Sandwich");

		c[i++] = new Customer(8888888, "Ham", "Sandwich");

		c[i++] = new Customer(9999999, "Chicken", "Sandwich");


		for (Customer ct : c)
		{
			this.storeCustomer(ct, false);
			this.processTicket(flightNumber, ct, randomClass(), false);

		}

	}
	/*
        Chooses a random string in a array which contains a destination
        returns
            A string containing the name of the destination
	 */
	private static String randomDist()
	{
		final String[] arr = {"Chicago, IL", "New York City, NY", "London, UK", "Seattle, WA",
				"Miami, FL", "Hong Kong", "Mumbai, India", "Dubia, UAE", "Libya, Africa", "Moskau, Russia", "Venezuela"};

		int pos = (int)(Math.random() * (arr.length) );

		return arr[pos];

	}

	/*
        Takes in a string containing the name of the destination.
        method then retrieves the Vertex associated with the dist. string
        to help find the shortest path from the source. 

	 */
	private static String[][] getPath(String dist)
	{
		Vertex<String> target = map.getVertex(new Vertex<String>(dist));

		if(target == null)
		{
			return null;
		}

		return map.getPath(target);
	}

	/*
        Method returns a random class picked from a String array.  

	 */
	private String randomClass()
	{
		final String[] arr = {Variable.getBusiness(), Variable.getFirst(), Variable.getEconomy()};

		int pos = (int)(Math.random() * (arr.length) );

		return arr[pos];
	}

	/*
        Method accepts a customer object and boolean
        Customer p - the object to be inserted in the database
        updateStack - if true data will be stored in undo stack
                        if false data will not be store in undo stack

        Method inserts the customer into the BST and HashMap

        Returns 
            true: object inserted
            false: Object fails to be inserted. 
	 */
	public boolean storeCustomer(Customer p, boolean updateStack)
	{
		synchronized (hm)
		{
			long phoneNumber = p.getPhoneNumber();

			if(hm.containsKey( phoneNumber) )
				return false;
			hm.put(phoneNumber, p);
		}
		synchronized(bst)
		{
			bst.insert(p);
		}

		if(updateStack)
			us.push(p, Variable.INSERT);
		postMessage("Customer " + p.getFirstName() + " " + p.getLastName() + " stored.");
		return true;
	}

	/*
        Method accepts a long phoneNumber
        long phoneNumber: the phone number of a customer, used as
        search key.

        method searches and finds a customer using the phone number 
        as a target.

        returns
        Customer: The found customer associated with the phoneNumber
        null: customer not found 
	 */
	public Customer getCustomer(long phoneNumber)
	{
		Customer c;
		synchronized(hm)
		{
			c = hm.get(phoneNumber);

			String customerData = null;
			if(c != null)
			{
				customerData = c.toString();

			}

		}
		return c;
	}

	/*
	 *Similar to the getCustomer method*
        Returns:
        customer.toString(): Returns the toString() of the customer
        CUSTOMER_NOT_FOUND_MESSAGE: when customer is not found
	 */
	public String findCustomer(long phoneNumber)
	{
		String customerData = null;
		synchronized (hm)
		{
			Customer c = hm.get(phoneNumber);


			if(c != null)
			{
				customerData = c.toString();
			}
			else
				customerData = CUSTOMER_NOT_FOUND_MESSAGE;
		}
		return customerData;
	}

	/*
        Searches the BST to find all occurrences of a
        customer associated with the target name

        returns
            null if name not found 
            String[] containing all customer data of
                the target name. Each index contains
                 customer data 
	 */
	public String[] findCustomer(String fullName)
	{

		String data[] = fullName.split(" ");

		if(data.length < 2)
		{
			String[] cd = {CUSTOMER_NOT_FOUND_MESSAGE};
			return cd;
		}

		ArrayList<Customer> c = null;
		synchronized(bst)
		{
			c =  (bst.findMultipleOccurrences(new Customer(0, data[0], data[1])));
		}
		int size;
		if(c == null || (size = c.size()) <= 0)
		{
			String[] cd = {CUSTOMER_NOT_FOUND_MESSAGE};
			return cd;
		}


		String[] customerData = new String[size];

		int counter = 0;
		for(Customer cus : c)
		{
			customerData[counter++] = cus.toString();
		}
		return customerData; 

	}

	/*
        method returns all customers in the database by 
        using the inOrder traversal found in BST.

        returns:
        String[]: Returns an array of String contain customer data
                    each index contains customer data to a specific
                     customer. 
        null: no customers are in the database. 
	 */
	public String[] findAllCustomers()
	{
		ArrayList<Customer> temp;
		synchronized(bst)
		{
			temp = bst.inOrder();
		}
		if(temp == null)
			return null;
		String[] allCustomers = new String[temp.size()];
		for(int i = 0; i < temp.size(); i++)
			allCustomers[i] = temp.get(i).toString();
		return allCustomers;
	}

	/*
        Method takes in a int.
        int flightNumber: to desired flightNumber to be boarded.

        Boards all passengers in a flight. Once boarded the flight
        will then be set to a new dist and recalculate the path. 

        returns 
        String[]: returns a string contianing all passengers 
        inserted into the flight.
        null: flight cannot be found 

	 */
	public String[] boardPassenger(int flightNumber)
	{

		Flight f = flights.get(new Flight(flightNumber));

		if(f == null)
			return null;
		postMessage("Boarding flight: " + f.getFlightNumber());

		String[] boarded = f.board();

		String dist = randomDist();
		String[][] path = getPath(dist);

		f.setDist(dist);
		f.setPath(path);
		return boarded;
	}

	/*
        Method takes in a int
        int flightNumber: number of the flight to search

        method searches for the target flight through the AVL
        tree.

        returns:
        String: returns flight.toString()
        null: Flight cannot be found
	 */
	public String getFlight(int flightNumber)
	{
		//int flightNumber = 1;

		Flight f = flights.get(new Flight(flightNumber));

		if(f == null)
			return FLIGHT_NOT_FOUND_MESSAGE;

		return f.toString();
	}

	/*
	 *similar to the findAllCustomer() method*
	 */
	public String[] findAllFlights()
	{
		ArrayList<Flight> temp = flights.inOrder();

		if(temp == null)
			return null;
		String[] allFlights = new String[temp.size()];
		for(int i = 0; i < temp.size(); i++)
			allFlights[i] = temp.get(i).toString();
		return allFlights;
	}

	/*
        Method takes in a int, Customer, String, boolean
        int flightNumber: The flightNumber the desired passenger wants
        to be in.
        Customer c: The customer that wants to inserted in the flight
        String flightClass: The customers desired class
        boolean: boolean to update undo stack or no
            true: UndoStack will update
            false: UndoStack will not update 

        Method first checks if the flightNumber exists by searching the AVL
        tree. It will then check if the flight has space to allocate for the Customer.
        If so the customer will be inserted into the flight's boarding queue. 

        Returns:
        false: When conditions are not met to insert customer
        true: customer inserted into the flight


	 */
	public boolean processTicket(int flightNumber, Customer c, String flightClass, boolean updateStack)
	{
		if(c.getBt() != null)
			return false;
		Flight f = flights.get(new Flight(flightNumber));

		if(f == null)
			return false;

		else
		{
			//String flightClass = c.getFlyingClass(flightNumber);
			if (f.checkFlightFull() == false)
			{
				if (flightClass.equals(Variable.ECONOMY) && f.checkEconFull() == false)
				{
					c.setBt(new BoardingTicket(flightNumber, Variable.ECONOMY));
					f.insertQueue(c);
				}
				else if (flightClass.equals(Variable.BUSINESS) && f.checkBusinessFull() == false)
				{
					c.setBt(new BoardingTicket(flightNumber, Variable.BUSINESS));
					f.insertQueue(c);
				}
				else if (flightClass.equals(Variable.FIRST) && f.checkFirstFull() == false)
				{
					c.setBt(new BoardingTicket(flightNumber, Variable.FIRST));
					f.insertQueue(c);
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}

		if(updateStack)
			us.push(c, Variable.BOARD);
		return true;
	}

	/*
        method takes in a int and customer
        int flightNumber: The flight number that contains the customer to be
        removed.
        Customer c: The customer to remove.

        Method checks weather flight exists. If it exists it will then check if
        the customer is in the database by searching its phoneNumber. If customer
        exists it will then remove the passenger from the flight. 

        Returns:
        false; could not remove passenger from flight due to wrong flightNumber
        or non-existing passenger.
        true: Passenger removed from flight
	 */
	public boolean removePassengerFromFlight(int flightNumber, Customer c)
	{
		Flight f = flights.get(new Flight(flightNumber));

		if(f == null)
			return false;

		if(c == null)
			return false;
		if(!f.deleteQueue(c))
			return false;
		c.setBt(null);


		return true;
	}

	public boolean removeCustomer(Customer c, boolean updateStack)
	{
		Long phoneNumber = c.getPhoneNumber();
		return removeCustomer(phoneNumber, updateStack);
	}

	/*
        method takes in long and boolean
        long phoneNumber: The phoneNumber associated with the desired customer
        to remove from the database. 
        boolean updateStack: boolean to update undo stack or no
            true: UndoStack will update
            false: UndoStack will not update 

        Method removes a passenger from the database. 

        returns
        false: Customer does not exist
        true: Customer removed from database
	 */
	public boolean removeCustomer(long phoneNumber, boolean updateStack)
	{
		Customer c;
		synchronized(hm)
		{
			if(!hm.containsKey(phoneNumber))
				return false;


			c = hm.remove(phoneNumber);
		}

		synchronized(bst)
		{
			bst.remove(c, new Comparator<Customer>()
			{
				@Override
				public int compare(Customer one, Customer two)
				{
					int result = Long.compare(one.getPhoneNumber(), two.getPhoneNumber());

					if(result == 0)
						return result;
					else
					{
						result = one.getFullName().compareTo(two.getFullName());
						if(result == 0)
							result = -1;
						return result;
					}
				}
			});
		}


		BoardingTicket bt = c.getBt();
		if(bt != null)
		{
			removePassengerFromFlight(bt.getFlightNumber(), c);
			bt = null;
			c.setBt(bt);
		}

		if(updateStack)
			us.push(c, Variable.DELETE);
		postMessage(c.getFullName() + " removed from database");
		return true;
	}

	/*
        Method performs the undo operation in the database.
        Returns:
        true: managed to undo an action
        false: failed to undo an action
	 */
	public boolean undo()
	{
		return us.pop();
	}

	/*
        method gets the message associated with the undo() operation. 
	 */
	public String getUndoMessage()
	{
		return us.getMessage();
	}

	public void postMessage(String message)
	{
		System.out.println(TALKING + message);
	}

}
