
package database;

public class Edge<AnyType> 
{
    public Vertex<AnyType> target;
	public double weight;
	
	public Edge(Vertex<AnyType> target, double weight)
	{
		this.target = target;
		this.weight = weight;
	}
}
