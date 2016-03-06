
package Entities;

import java.io.Serializable;
import services.BoardingTicket;


public class Customer implements Serializable
{
	private long phoneNumber;
	private String firstName;
	private String lastName;
	private boolean disabled;
	private BoardingTicket bt = null;


	public Customer(long phoneNumber, String firstName, String lastName)
	{
		this.phoneNumber = phoneNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		disabled = false;
	}

	public Customer(long phoneNumber, String firstName, String lastName, 
			boolean disabled)
	{
		this.phoneNumber = phoneNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.disabled = disabled;
	}
	public long getPhoneNumber() {
		return phoneNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public BoardingTicket getBt() {
		return bt;
	}

	public boolean isDisabled()
	{
		return disabled;
	}

	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setBt(BoardingTicket bt) {
		this.bt = bt;
	}

	public void setDisabled(boolean disabled)
	{
		this.disabled = disabled;
	}

	public String getFlyingClass(int flightNumber)
	{
		if( bt != null && flightNumber == bt.getFlightNumber() )
			return bt.getFlyingClass();
		else
			return null;
	}

	public String getFullName()
	{
		return firstName + " " + lastName;
	}

	@Override
	public String toString()
	{
		String data = "\nCustomer Information\n";
		data += "\tName: " + getFullName() + 
				"\n\tDisabled: " + isDisabled() +
				"\n\tPhone #: " + getPhoneNumber();

		if(bt != null)
			data += "\n" + bt.toString();
		data += "\n";
		return data;
	}
}
