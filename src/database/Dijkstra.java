
package database;

import java.util.PriorityQueue;
import java.util.Stack;


public class Dijkstra<AnyType>
{
	public void findShortestPath(Vertex<AnyType> source)
	{
		source.dist = 0;

		PriorityQueue<Vertex<AnyType>> vq = new PriorityQueue<>();
		vq.add(source);

		while(!vq.isEmpty())
		{
			// gets smallest unknown distance vertex
			Vertex<AnyType> v = vq.poll();

			try
			{
				for(Edge<AnyType> e : v.getEdge())
				{
					Vertex<AnyType> w = e.target;

					if( w != null && !w.known)
					{
						//cost between vertex v and w
						double cvw = v.dist + e.weight;

						if(cvw < w.dist)
						{
							//update W
							vq.remove(w);
							w.dist = cvw;
							w.path = v;
							vq.add(w);
						}
					}
				}
			}
			catch(NullPointerException e)
			{

			}
		}

	}

	/*
        getPath returns that path needed to reach the target.

        String[][]- first column represents the departure, second column
        represents the distination. 
	 */
	public String[][] getPath(Vertex<AnyType> target)
	{

		String[][] path = null;
		Stack<Vertex<AnyType>> stack = new Stack<Vertex<AnyType>>();

		while(target != null)
		{
			stack.push(target);
			target = target.path;
		}

		path = new String[stack.size()][2];
		int i = 0, j = 0;
		while(!stack.isEmpty())
		{
			Vertex<AnyType> temp = stack.pop();
			path[i][j] = "" + temp;

			if(!stack.isEmpty())
			{
				if(j == 0)
					j++;
				else
				{
					j--;
					i++;
					path[i][j] = "" + temp;
					j++;
				}

			}


		}

		return path;

	}
}
