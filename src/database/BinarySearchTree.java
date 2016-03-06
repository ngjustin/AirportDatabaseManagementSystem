
package database;

import java.util.*;


class UnderflowException extends Exception { };


public class BinarySearchTree<AnyType> implements treeInterface<AnyType>
{
	public BinarySearchTree(Comparator<? super AnyType> c)
	{
		root = null;
		cmp = c;
	}
	protected static class BinaryNode<AnyType>
	{
		BinaryNode( AnyType theElement )
		{ this( theElement, null, null); }

		BinaryNode( AnyType theElement, 
				BinaryNode<AnyType> lt, BinaryNode<AnyType> rt )
		{ element  =  theElement; left  =  lt; right  =  rt; }

		AnyType element;
		BinaryNode<AnyType> left;
		BinaryNode<AnyType> right;
		int height;
	}

	public BinarySearchTree()
	{ root = null; }

	public void makeEmpty()
	{ 
		modCount ++;
		root  =  null; 
	}

	public boolean isEmpty()
	{ return root  ==  null; }

	public boolean contains( AnyType x )
	{ return contains( x, root ); }

	public AnyType get(AnyType x)
	{
		return get(x, root);
	}
	public AnyType findMin() throws UnderflowException
	{
		if ( isEmpty() )
			throw new UnderflowException();
		else
			return findMin( root ).element;
	}

	public AnyType findMax() throws UnderflowException
	{
		if ( isEmpty() )
			throw new UnderflowException();
		else
			return findMax( root ).element;
	}

	public void insert( AnyType x )
	{ root  =  insert( x, root ); }

	public void remove( AnyType x )
	{ root  =  remove( x, root ); }

	public void remove(AnyType x, Comparator<? super AnyType> cmp)
	{
		root = remove(x, root, cmp);
	}
	public void printTree()
	{ 
		if ( isEmpty() )
			System.out.println( "Empty tree" );
		else
			printTree( root );
	}
	public ArrayList<AnyType> inOrder()
	{
		inOrder(root);
		return ordered;

	}



	protected void printTree( BinaryNode<AnyType> t )
	{
		if(t != null)
		{
			printTree(t.left);
			System.out.print(t.element + " ");
			printTree(t.right);
		}
	}

	private int myCompare(AnyType lhs, AnyType rhs)
	{
		if(cmp != null)
			return cmp.compare(lhs, rhs);
		else
			return ( (Comparable) lhs).compareTo(rhs);
	}

	protected boolean contains( AnyType x, BinaryNode<AnyType> t )
	{
		if(t == null)
			return false;

		int compareResult = myCompare(x, t.element);

		if( compareResult < 0)
			return contains(x, t.left);
		else if( compareResult > 0)
			return contains(x, t.right);
		else
			return true; //match
	}

	private AnyType get(AnyType x, BinaryNode<AnyType> t)
	{
		while(t != null)
		{
			int compareResult = myCompare(x, t.element);

			if(compareResult < 0)
				t = t.left;
			else if(compareResult > 0)
				t = t.right;
			else if(compareResult == 0)
				return t.element;
		}

		return null;
	}
	public ArrayList<AnyType> findMultipleOccurrences (AnyType target)
	{
		return findMultipleOccurrences(target, root);
	}

	protected ArrayList<AnyType> findMultipleOccurrences(AnyType target, BinaryNode<AnyType> root)
	{
		boolean foundTarget = false;
		ArrayList<AnyType> matched = new ArrayList<>();

		while (root != null)
		{
			AnyType temp = root.element;

			int compareResult = myCompare(target, temp);

			if (compareResult == 0)
			{
				matched.add(temp);
				//System.out.println(temp);

				root = root.left;
			}
			else if (compareResult > 0) 
			{
				root = root.right;
			}
			else if(compareResult < 0) 
			{
				root = root.left;
			}
			else
				root = null;
		}


		return matched;
	}


	protected BinaryNode<AnyType> findMin( BinaryNode<AnyType> t )
	{
		BinaryNode<AnyType> currentNode = t;

		while(currentNode.left != null)
		{
			currentNode = currentNode.left;
		}

		return currentNode;

	}

	protected BinaryNode<AnyType> findMax( BinaryNode<AnyType> t )
	{
		BinaryNode<AnyType> currentNode = t;

		while(currentNode.right != null)
		{
			currentNode = currentNode.right;
		}

		return currentNode;

	}

	protected synchronized BinaryNode<AnyType> insert( AnyType x, BinaryNode<AnyType> t)
	{
		if ( t == null )
		{
			modCount++ ;
			return new BinaryNode<AnyType>( x, null, null);
		}

		int compareResult  =  myCompare(x, t.element);

		if ( compareResult < 0)
			t.left  =  insert( x, t.left);
		else if ( compareResult > 0)
			t.right  =  insert( x, t.right);
		else
			t.left  =  insert( x, t.left); // duplicate

		return t;
	}

	protected synchronized BinaryNode<AnyType> remove( AnyType x, BinaryNode<AnyType> t )
	{
		if ( t == null )
			return t; // not found

		int compareResult  =  cmp.compare(x, t.element);

		if ( compareResult < 0)
			t.left  =  remove( x, t.left );
		else if ( compareResult > 0)
			t.right  =  remove( x, t.right );
		else if ( t.left != null && t.right != null ) // two children
		{
			t.element  =  findMin( t.right ).element;
			t.right  =  remove( t.element, t.right );
		}
		else
		{  
			t = ( t.left != null) ? t.left : t.right;
		}

		return t;
	}

	private synchronized BinaryNode<AnyType> remove(AnyType x, BinaryNode<AnyType> t, Comparator<? super AnyType> cmp)
	{
		if ( t == null )
			return t; // not found

		int compareResult  =  cmp.compare(x, t.element);

		if ( compareResult < 0)
			t.left  =  remove( x, t.left, cmp );
		else if ( compareResult > 0)
			t.right  =  remove( x, t.right, cmp );
		else if ( t.left != null && t.right != null ) // two children
		{
			t.element  =  findMin( t.right ).element;
			t.right  =  remove( t.element, t.right, cmp );
		}
		else
		{  
			t = ( t.left != null) ? t.left : t.right;
		}

		return t;
	}

	protected void inOrder(BinaryNode<AnyType> t)
	{
		if(t != null)
		{
			inOrder(t.left);
			ordered.add(t.element);
			inOrder(t.right); 
		}
	}
	protected BinaryNode<AnyType> root;
	protected Comparator<? super AnyType> cmp;
	protected ArrayList<AnyType> ordered = new ArrayList<AnyType>();
	int modCount  =  0;
}
