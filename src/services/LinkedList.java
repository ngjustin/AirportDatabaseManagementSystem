
package services;

import java.util.Comparator;


public class LinkedList<T> 
{
	public Node<T> head;
	protected Comparator<? super T> cmp;
	private int count;

	public LinkedList(Comparator<? super T> cmp)
	{
		head = null;
		this.cmp = cmp;
		count = 0;
	}

	public boolean isEmpty()
	{
		if(head != null)
		{
			return false;
		}
		return true;
	}

	public void insert(T x)
	{
		Node<T> newNode = new Node<T>(x);
		Node<T> previous = null;
		Node<T> current = head;

		while (current != null) 
		{
			int compareResult = myCompare(x, current.Data);
			if(compareResult < 0)
				break;
			previous = current;
			current = current.next;
		}
		if (previous == null) {
			newNode.next = head;
			head = newNode;
		}

		else 
		{
			previous.next = newNode;
			newNode.next = current;
		}
		count++;
	}

	@SuppressWarnings("unchecked")
	private int myCompare(T n1,T n2)
	{
		if(cmp != null)
			return cmp.compare(n1, n2);
		else
			return ( (Comparable<T>) n1).compareTo(n2);
	}

	public T remove() 
	{
		Node<T> current = head;
		Node<T> temp = current;
		head = current.next;
		count--;
		return temp.Data;
	}

	public T removeTarget(T x, Comparator<? super T> cmp)
	{
		if(head == null)
			return null;

		Node<T> previous = null;
		Node<T> current = head;
		Node<T> deletedNode;

		while (current != null)
		{

			int compareResult = cmp.compare(x, current.Data);
			if (compareResult == 0)
			{
				deletedNode = current;

				if (previous == null)
				{
					head = current.next;
					count--;
					return deletedNode.Data;

				}
				else
				{
					previous.next = current.next;

					count--;
					return deletedNode.Data;
				}
			}
			else
			{
				previous = current;
				current = current.next;
			}
		}

		return null;
	}

	public void printList()
	{
		Node<T> current = head;
		while(current != null)
		{
			System.out.println(current.Data);
			current = current.next;
		}
	}

	public int getSize()
	{
		return count;
	}
	public class Node<T>
	{

		public T Data;
		public Node<T> next;

		public Node(T x) {
			Data = x;
		}

		public void printNode() {
			System.out.println(Data + " ");
		}

	}	
}