
package database;

import java.util.Comparator;


public class AvlTree<AnyType> extends BinarySearchTree<AnyType>
{
	public AvlTree(Comparator<? super AnyType> c)
	{
		super(c);
	}

	/*
	 * Returns the height of the node. If the node is null
	 * It will return -1
	 */
	private int height(BinaryNode<AnyType> t)
	{
		return t == null ? -1: t.height;
	}

	/*
	 * (non-Javadoc)
	 * @see BinarySearchTree#insert(java.lang.Object, BinarySearchTree.BinaryNode)
	 * uses the insertion method in BinarySearchTree and then balances. 
	 */
	@Override
	protected synchronized BinarySearchTree.BinaryNode<AnyType> insert(AnyType x, BinarySearchTree.BinaryNode<AnyType> t) 
	{
		// TODO Auto-generated method stub
		return balance( super.insert(x, t) );
	}


	/*
	 * (non-Javadoc)
	 * @see BinarySearchTree#remove(java.lang.Object, BinarySearchTree.BinaryNode)
	 * Uses the remove method found in BinarySearchTree and then balances.
	 */
	@Override
	protected synchronized BinaryNode<AnyType> remove(AnyType x, BinaryNode<AnyType> t) {
		// TODO Auto-generated method stub
		return balance ( super.remove(x, t) );
	}

	/*
	 * Method balances the tree by comparing the height of the trees.
	 * If the height difference is greater then the range allowed then
	 * thre tree is not balance and rotations will take place to make
	 * the tree balanced. 
	 */
	private BinarySearchTree.BinaryNode<AnyType> balance(BinarySearchTree.BinaryNode<AnyType> t)
	{
		int IMBALANCE = 1;

		if(t == null)
			return t;

		if( height(t.left) - height(t.right) > IMBALANCE)
		{
			if( height(t.left.left) >= height(t.left.right) )
				t = rotateWithLeftChild( t );
			else
				t = doubleWithLeftChild( t );
		}
		else
		{
			if( height( t.right ) - height(t.left) > IMBALANCE)
				if( height(t.right.right ) >= height(t.right.left) )
					t = rotateWithRightChild( t );
				else
					t = doubleWithRightChild( t );

		}
		t.height = Math.max( height( t.left ), height( t.right ) ) + 1;
		return t;
	}

	private BinarySearchTree.BinaryNode<AnyType> rotateWithLeftChild( BinarySearchTree.BinaryNode<AnyType> t)
	{
		BinarySearchTree.BinaryNode<AnyType> temp = t.left;
		t.left = temp.right;
		temp.right = t;

		t.height = Math.max( height( t.left), height( t.right ) ) + 1;
		temp.height = Math.max(height( temp.left), height( temp.right ) ) + 1;

		return temp;
	}

	private BinarySearchTree.BinaryNode<AnyType> rotateWithRightChild( BinarySearchTree.BinaryNode<AnyType> t)
	{
		BinarySearchTree.BinaryNode<AnyType> temp = t.right;
		t.right = temp.left;
		temp.left = t;

		t.height = Math.max( height( t.left), height( t.right ) ) + 1;
		temp.height = Math.max(height( temp.left), height( temp.right ) ) + 1;

		return temp;
	}


	private BinarySearchTree.BinaryNode<AnyType> doubleWithLeftChild( BinarySearchTree.BinaryNode<AnyType> t)
	{
		t.left = rotateWithRightChild( t.left );
		return rotateWithLeftChild( t );
	}

	private BinarySearchTree.BinaryNode<AnyType> doubleWithRightChild( BinarySearchTree.BinaryNode<AnyType> t)
	{
		t.right = rotateWithLeftChild( t.right );
		return rotateWithRightChild( t );
	}

	@Override
	protected void printTree(BinarySearchTree.BinaryNode<AnyType> t)
	{
		/*
		 * Tree prints in pre-order
		 */
		if(t != null)
		{
			System.out.print( t.element + " " );
			printTree(t.left);
			printTree( t.right );
		}
	}

}
