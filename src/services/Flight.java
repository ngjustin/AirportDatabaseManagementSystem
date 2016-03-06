
package services;

import Entities.Customer;
import java.util.Comparator;
import util.Variable;


public class Flight 
{
	private int flightNumber;
	private static final int MAX_CAPACITY = 150;
	private static final int MAX_ECONOMY = 80;
	private static final int MAX_BUSINESS = 40;
	private static final int MAX_FIRST = 30;

	//counters
	private int seatsPurchased;
	private int econSeatsPurchased;
	private int bussinessSeatsPurchased;
	private int firstClassSeatsPurchased;

	private String[][] path;
	private String departure;
	private String distination;


	//declare priority queue
	PriorityQueue<Customer> pq = new PriorityQueue<>(
			new Comparator<Customer>()
			{
				@Override
				public int compare(Customer one, Customer two)
				{
					if(two.isDisabled())
						return 1;
					if(one.isDisabled())
						return -1;

					String oneClass = one.getFlyingClass(flightNumber);
					String twoClass = two.getFlyingClass(flightNumber);

					if(oneClass.equals(twoClass))
						return 0;

					int onePri = getPri(oneClass);
					int twoPri = getPri(twoClass);

					if(onePri < twoPri)
						return -1;
					else
						return 1;
				}

				private int getPri(String flyingClass)
				{
					if(flyingClass.equals(Variable.FIRST))
						return 1;
					else if(flyingClass.equals(Variable.BUSINESS))
						return 2;
					else 
						return 3;
				}
			});


	public Flight(int flightNumber)
	{
		this.flightNumber = flightNumber;

		seatsPurchased = 0;
		econSeatsPurchased = 0;
		bussinessSeatsPurchased = 0;
		firstClassSeatsPurchased = 0;
	}

	public Flight(int flightNumber, String[][] path, String source, String dist)
	{
		this.flightNumber = flightNumber;
		this.path = path;
		this.departure = source;
		this.distination = dist;

		seatsPurchased = 0;
		econSeatsPurchased = 0;
		bussinessSeatsPurchased = 0;
		firstClassSeatsPurchased = 0;
	}
	public boolean insertQueue(Customer p)
	{
		if (p == null)
			return false;

		synchronized(pq)
		{
			pq.insert(p);
		}

		seatsPurchased++;

		if (p.getFlyingClass(flightNumber).equals(Variable.ECONOMY))
			econSeatsPurchased++;
		else if (p.getFlyingClass(flightNumber).equals(Variable.BUSINESS))
			bussinessSeatsPurchased++;
		else if (p.getFlyingClass(flightNumber).equals(Variable.FIRST))
			firstClassSeatsPurchased++;

		return true;

	}

	/*
        method deletes a specific target, if found in priority queue
	 */
	public boolean deleteQueue(Customer target)
	{
		synchronized(pq)
		{
			Customer c = pq.removeTarget(target, new Comparator<Customer>()
			{
				@Override
				public int compare(Customer one, Customer two)
				{
					int result = Long.compare(one.getPhoneNumber(), two.getPhoneNumber());
					return result;
				}
			});

		}

		seatsPurchased--;

		if (target.getFlyingClass(flightNumber).equals(Variable.ECONOMY))
			econSeatsPurchased--;
		else if (target.getFlyingClass(flightNumber).equals(Variable.BUSINESS))
			bussinessSeatsPurchased--;
		else if (target.getFlyingClass(flightNumber).equals(Variable.FIRST))
			firstClassSeatsPurchased--;

		return true;

	}


	/*
        Method boards all passengers in the queue.
	 */
	public String[] board()
	{
		String[] passengerBoarding = null;
		synchronized(pq)
		{
			int size = pq.getSize();

			passengerBoarding = new String[size];

			for(int i = 0; i < size && !pq.isEmpty(); i++)
			{
				Customer c = pq.remove();

				if (c.getFlyingClass(flightNumber).equals(Variable.ECONOMY))
					econSeatsPurchased--;
				else if (c.getFlyingClass(flightNumber).equals(Variable.BUSINESS))
					bussinessSeatsPurchased--;
				else if (c.getFlyingClass(flightNumber).equals(Variable.FIRST))
					firstClassSeatsPurchased--;

				String info = c.getFullName() + " " + c.getFlyingClass(flightNumber) + " " + c.isDisabled();
				c.setBt(null);
				passengerBoarding[i] = info;
			}
		}

		return passengerBoarding;
	}

	public void setFlightNumber(int flightNumber)
	{
		this.flightNumber = flightNumber;
	}
	public int getFlightNumber()
	{
		return flightNumber;
	}

	public int getSeatsLeft() { return MAX_CAPACITY - seatsPurchased; }

	public int getEconSeatsLeft() { return MAX_ECONOMY - econSeatsPurchased; }

	public int getBusinessSeatsLeft() { return MAX_BUSINESS - bussinessSeatsPurchased; }

	public int getFirstClassSeatsLeft() { return MAX_FIRST - firstClassSeatsPurchased; }

	public boolean checkFlightFull()
	{
		if (getSeatsLeft() > 0)
			return false;
		else
			return true;
	}

	public boolean checkEconFull()
	{
		if (getEconSeatsLeft() > 0)
			return false;
		else
			return true;
	}

	public boolean checkBusinessFull()
	{
		if (getBusinessSeatsLeft() > 0)
			return false;
		else
			return true;
	}

	public boolean checkFirstFull()
	{
		if (getFirstClassSeatsLeft() > 0)
			return false;
		else
			return true;
	}

	public void setPath(String[][] path)
	{
		this.path = path;
	}

	public void setDist(String dist)
	{
		this.distination = dist;      
	}

	@Override
	public String toString()
	{
		String data;
		String full = "\n\t\tfull";
		String aval = "\n\t\t# of avaliable seats: ";

		data = "\nFlight #" + flightNumber;

		data += "\n\tFirst Class:";
		if(checkFirstFull())
			data += full;
		else
			data += aval + this.getFirstClassSeatsLeft();

		data += "\n\tBussiness Class:";
		if(this.checkBusinessFull())
			data += full;
		else
			data += aval + this.getBusinessSeatsLeft();

		data += "\n\tEconomy Class:";
		if(this.checkEconFull())
			data += full;
		else
			data += aval + this.getEconSeatsLeft();

		if(path != null)
		{
			data += "\n\tFrom " + departure + " to " + distination;
			data += "\n\t\tFlight path:";

			int sizeX = path.length;
			int sizeY = path[0].length;

			for(int i = 0, j = 0; i < sizeX; i++)
			{
				if(path[i][1] != null)
					data += "\n\t\tDeparture: " + path[i][0] + " Distination: " + path[i][1];

			}
		}

		return data;
	}


}
