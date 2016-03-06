
package util;

import database.MainDatabase;
import Entities.Customer;
import java.util.Stack;
import services.BoardingTicket;

/*
    stack - holds contents
    message - message describing the undo action last performed
 */
public class UndoStack 
{
	private Stack<UndoStackInfo<Customer>> stack;
	private String message;

	private static final String UNDO = "[UNDO] ";
	/*
    Contains information about the action performed
    and object it was performed on.

    data - object that action was performed on
    ACTION - describes type of action performed
	 */
	private class UndoStackInfo<AnyType>
	{
		private final AnyType data;

		private final String ACTION;


		public UndoStackInfo(AnyType data, String action)
		{
			this.data = data;
			ACTION = action;
		}


		public String getAction()
		{
			return ACTION;
		}

		public AnyType getData()
		{
			return data;
		}
	}


	public UndoStack()
	{
		stack = new Stack<>();
	}

	public void push(Customer data, String action)
	{
		stack.push(new UndoStackInfo<>(data, action));
	}

	/*
        Before popping, the method checks first to see if the undo operation
        can be performed. If it the stack will pop.
        Returns
        true: item undo
        false: not able to undo item.
	 */
	public boolean pop()
	{
		if(stack.isEmpty())
		{
			message = UNDO + "No action to undo";
			return false;
		}
		UndoStackInfo<Customer> usi = stack.peek();

		if(usi == null)
			return false;
		String action = usi.getAction();
		Customer c = usi.getData();

		if ( performUndo(c, action))
		{
			stack.pop();
			return true;
		}

		return false;

	}

	/*
        The method takes a Customer and String
        Customer c: Customer action was performed on
        String action: action performed on customer.

        Method performs the opposite action that was performed on 
        Customer object. 

        returns:
        true: action was undone.
        false: failed to undo action.  
	 */
	private boolean performUndo(Customer c, String action)
	{
		if(action.equalsIgnoreCase(Variable.INSERT))
		{
			MainDatabase mb = new MainDatabase();

			if(!mb.removeCustomer(c, false))
			{
				message = UNDO + "Error removing passenger " + c.getFullName();
				System.out.println(message);
				return false;
			}

			message = UNDO + "Passenger " + c.getFullName() + " removed from database";
			return true;
		}
		else if(action.equalsIgnoreCase(Variable.DELETE))
		{
			MainDatabase mb = new MainDatabase();

			if(!mb.storeCustomer(c, false))
			{
				message = UNDO + "Error inserting passenger: " + c.getFullName();
				System.out.println(message);
				return false;
			}

			message = UNDO + "Passenger " + c.getFullName() + " restored in database";
			return true;
		}
		else if(action.equalsIgnoreCase(Variable.BOARD))
		{
			MainDatabase mb = new MainDatabase();


			BoardingTicket bt = c.getBt();

			if(bt == null)
			{
				message = UNDO + "Passenger " + c.getFullName() + " has no boarding Ticket";
				return true;
			}
			int flightNumber = bt.getFlightNumber();
			long phoneNumber = c.getPhoneNumber();

			if(!mb.removePassengerFromFlight(flightNumber, c))
			{
				message = UNDO + "Error removing : " + c.getFullName() +" from flight #" + flightNumber;
			}


			message = UNDO + "Passenger " + c.getFullName() + " removed from flight #" + flightNumber;

			return true;
		}

		return false;
	}

	public String getMessage()
	{
		return message;
	}
}
