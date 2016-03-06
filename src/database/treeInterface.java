
package database;

public interface treeInterface <AnyType>
{
	class UnderflowException extends Exception { };

	public void makeEmpty();
	public boolean isEmpty();
	public boolean contains(AnyType x);
	public AnyType findMin() throws UnderflowException; 
	public AnyType findMax() throws UnderflowException;
	public void insert(AnyType x);
	public void remove(AnyType x);
	public void printTree();

}
