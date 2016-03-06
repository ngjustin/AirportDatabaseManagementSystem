
package services;

import java.io.Serializable;


public class BoardingTicket implements Serializable
{
	private int flightNumber;
	private String flyingClass;

	public BoardingTicket(int flightNumber, String flyingClass)
	{
		this.flyingClass = flyingClass;
		this.flightNumber = flightNumber;
	}

	public String getFlyingClass() 
	{
		return flyingClass;
	}

	public int getFlightNumber() {
		return flightNumber;
	}


	public void setFlyingClass(String flyingClass) {
		this.flyingClass = flyingClass;
	}

	public void setFlightNumber(int flightNumber) {
		this.flightNumber = flightNumber;
	}

	@Override
	public String toString()
	{
		String data = "Boarding Ticket information\n\t" + "Flight #" + flightNumber + " Class: " + flyingClass;
		return data;
	}

}
