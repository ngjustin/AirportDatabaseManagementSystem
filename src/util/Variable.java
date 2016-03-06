
package util;

public class Variable 
{
	//Flight class status variables
	public final static String BUSINESS = "BUSINESS";
	public final static String FIRST = "FIRST";
	public final static String ECONOMY = "ECONOMY";

	//action variables
	public final static String INSERT = "INSERT";
	public final static String DELETE = "DELETED";
	public final static String BOARD = "BOARD";



	public static String getBusiness()
	{
		return BUSINESS;
	}

	public static String getFirst()
	{
		return FIRST;
	}

	public static String getEconomy()
	{
		return ECONOMY;
	}
}
