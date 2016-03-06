
package services;

import java.util.Comparator;

public class PriorityQueue<T> 
{

	private LinkedList<T> list;

	public PriorityQueue(Comparator<? super T> cmp) {
		list = new LinkedList<T>(cmp);
	}

	public void insert(T x) {
		list.insert(x);
	}

	public T remove()   
	{
		return list.remove();

	}

	public T removeTarget(T x, Comparator<? super T> cmp)
	{
		return list.removeTarget(x, cmp);
	}


	public boolean isEmpty()
	{
		return list.isEmpty();
	}

	public int getSize()
	{
		return list.getSize();
	}

}
