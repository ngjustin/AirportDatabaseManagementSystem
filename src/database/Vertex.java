
package database;

public class Vertex<AnyType> implements Comparable<Vertex>
{
    private AnyType data;
	private Edge<AnyType>[] edge;
	public double dist = Double.MAX_VALUE;
	public boolean known = false;
	public boolean visited = false;
	public Vertex<AnyType> path;
	
	public Vertex(AnyType data)
	{
		this.data = data;
		edge = null;
	}
	
	public Vertex(AnyType data, Edge<AnyType>[] edge)
	{
		this.data = data;
		this.setEdge(edge);
	}
	
	public Edge<AnyType>[] getEdge() 
	{
		if(edge == null)
			return null;
		return edge;
	}

	public void setEdge(Edge<AnyType>[] edge) {
		this.edge = edge;
	}
	
	
        public AnyType getData()
        {
            return data;
        }
	@Override
	public int compareTo(Vertex v) 
	{
		// TODO Auto-generated method stub
		return Double.compare(dist, v.dist);
	}

	@Override
	public String toString() 
	{
		// TODO Auto-generated method stub
		return data.toString();
	}
}
