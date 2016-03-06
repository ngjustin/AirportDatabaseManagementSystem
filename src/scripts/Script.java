
package scripts;

import java.util.Scanner;
import util.Variable;


public class Script 
{
	public Script()
	{
		greeting();
	}

	private void greeting()
	{
		System.out.println("Thank you for logging in.");

		System.out.print("\n\n");
	}

	private void integerMenuInstruction()
	{
		System.out.println("Enter the integer correspoding to the action you want");
	}

	private void lineInputIndicator()
	{
		System.out.print("-> ");
	}

	private void notValidResponceMessage(String responce)
	{
		System.out.print("\n");
		System.out.println( responce + " is not a valid response!");
	}

	public int mainMenu()
	{
		Scanner scan = new Scanner(System.in);
		int response = -1;

		while(response < 0 || response > 9 )
		{
			integerMenuInstruction();
			displayMessage("1. Search customer");
			displayMessage("2. Board passengers");
			displayMessage("3. Create customer");
			displayMessage("4. Delete customer");
			displayMessage("5. Search flight");
			displayMessage("6. Give ticket to customer");
			displayMessage("7. Display all flights");
			displayMessage("8. Display all customers");
			displayMessage("9. Undo");
			displayMessage("0. Exit");
			lineInputIndicator();
			String input = scan.nextLine();
			try
			{
				response = Integer.parseInt(input);

				if(response < 0 || response > 9)
					notValidResponceMessage("" + input);
				else if(response == 1)
					return searchPassengerMenu();
			}
			catch(NumberFormatException e)
			{
				notValidResponceMessage("" + input);
				response = -1;

			}
		}

		return response;
	}

	/*
        response is times 10 of the option picked
	 */
	public int searchPassengerMenu()
	{
		Scanner scan = new Scanner(System.in);
		int response = -1;

		while(response < 0 || response > 3 )
		{
			integerMenuInstruction();
			System.out.println("1. Search by phone number");
			System.out.println("2. Search by name");
			System.out.println("3. back");
			lineInputIndicator();
			String input = scan.nextLine();

			try
			{
				response = Integer.parseInt(input);

				if(response < 0 || response > 3)
				{
					notValidResponceMessage("" + input);
					response = -1;
				}
			}
			catch(Exception e)
			{
				notValidResponceMessage("" + input);
				response = -1;

			}
		}

		return response * 10;
	}

	public long searchPassengerByNumber()
	{
		long phoneNumber = -1;

		Scanner scan = new Scanner(System.in);
		while(phoneNumber < 0)
		{
			displayMessage("Enter the customer's phone number");
			lineInputIndicator();

			String input = scan.nextLine();

			try
			{
				phoneNumber = Long.parseLong(input);

				if(phoneNumber < 0)
				{
					notValidResponceMessage(input);
					phoneNumber = -1;
				}
			}
			catch(NumberFormatException e)
			{
				notValidResponceMessage(input);
				phoneNumber = -1;
			}
		}
		return phoneNumber;
	}


	public String searchCustomerByName()
	{
		Scanner scan = new Scanner(System.in);
		String input = null;


		displayMessage("Enter the name of the customer");
		lineInputIndicator();

		input = scan.nextLine();

		return input;
	}

	public int searchFlights()
	{
		Scanner scan = new Scanner(System.in);
		int flightNumber = -1;

		while(flightNumber < 0)
		{
			displayMessage("Enter the flight number");
			lineInputIndicator();
			String input = scan.nextLine();

			try
			{
				flightNumber = Integer.parseInt(input);

				if(flightNumber < 0)
					notValidResponceMessage(input);
			}
			catch(NumberFormatException e)
			{
				notValidResponceMessage(input);
				flightNumber = -1;
			}
		}

		return flightNumber;


	}

	/*
        Obtains the flight number to perform boarding on
	 */
	public int board()
	{
		Scanner scan = new Scanner(System.in);
		int response = -1;

		while(response < 0)
		{
			displayMessage("Enter the flight number to board");
			lineInputIndicator();

			String input = scan.nextLine();
			try
			{
				response = Integer.parseInt(input);

				if(response < 0)
					notValidResponceMessage(input);
			}
			catch(NumberFormatException e)
			{
				notValidResponceMessage(input);
				response = -1;
			}

		}

		return response;
	}

	/*
        returns 
        object[] - 
            [0] - phoneNumber of the customer wanting to be inserted
            [1] - flightNumber to be inserted too
            [2] - desired class
	 */
	public Object[] giveTicket()
	{

		long phoneNumber = this.searchPassengerByNumber();
		int flightNumber = this.searchFlights();

		int flightClass = -1;
		Scanner scan = new Scanner(System.in);
		while(flightClass < 0)
		{
			displayMessage("Enter the integer corresponding to the desired class");
			displayMessage("1. " + Variable.FIRST);
			displayMessage("2. " + Variable.BUSINESS);
			displayMessage("3. " + Variable.ECONOMY);
			lineInputIndicator();

			String input = scan.nextLine();

			try
			{
				flightClass = Integer.parseInt(input);

				if(flightClass < 0 && flightClass > 3)
				{
					notValidResponceMessage(input);
					flightClass = -1;
				}

			}
			catch(NumberFormatException e)
			{
				notValidResponceMessage(input);
				flightClass = -1;
			}
		}

		String _flightClass = null;
		if(flightClass == 1)
			_flightClass = Variable.FIRST;
		else if(flightClass == 2)
			_flightClass = Variable.BUSINESS;
		else 
			_flightClass = Variable.ECONOMY;

		Object[] obj = new Object[3];
		obj[0] = phoneNumber;
		obj[1] = flightNumber;
		obj[2] = _flightClass;
		return obj;

	}

	/*
        displays the passenger boarded onto a flight
	 */
	public void board(String[] boardedPassenger)
	{
		displayMessage("\n\n");


		if(boardedPassenger == null)
		{
			displayMessage("Flight number was not found");
			return;
		}

		displayMessage("*boarding passengers*");
		for(String s:boardedPassenger)
			displayMessage(s);
		displayMessage("*Passengers boarded*");
		displayMessage("\n");
	}

	/*
        returns 
        Object[]
              [0] - first name
              [1] - last name
              [2] - phone number
              [3] - disability status
	 */
	public Object[] createPassenger()
	{
		Scanner scan = new Scanner(System.in);

		displayMessage("\n\n");
		displayMessage("Enter customer first name");
		lineInputIndicator();
		String firstName = scan.nextLine();
		displayMessage("Enter customer last name");
		lineInputIndicator();
		String lastName = scan.nextLine();

		String strDisabled = null;
		boolean isDisabled = false;
		while(strDisabled == null)
		{
			displayMessage("Is customer disabled? (Y/N)");
			lineInputIndicator();
			strDisabled = scan.nextLine();

			if(strDisabled.equalsIgnoreCase("y") || strDisabled.equalsIgnoreCase("yes"))
				isDisabled = true;
			else if(strDisabled.equalsIgnoreCase("n") || strDisabled.equalsIgnoreCase("no"))
				isDisabled = false;
			else
				strDisabled = null; 
		}
		boolean validNumber = false;
		long phoneNumber = 0;
		while(!validNumber)
		{
			displayMessage("Enter customer's phone number");
			lineInputIndicator();
			String stringNumber = scan.nextLine();

			try
			{
				phoneNumber = Long.parseLong(stringNumber);
				validNumber = true;
			}
			catch(NumberFormatException e)
			{
				notValidResponceMessage(stringNumber);
			}
		}

		Object[] o = new Object[4];
		o[0] = firstName;
		o[1] = lastName;
		o[2] = phoneNumber;
		o[3] = isDisabled;

		return o;

	}

	public void displayMessage(String message)
	{
		System.out.println(message);
	}

	public void displayMessage(String[] message)
	{
		for(String m : message)
		{
			System.out.println(m);
		}
	}
}
