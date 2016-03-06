
package database;

import java.util.Comparator;


/*
    class holds and maintains the graph. 
    AVL tree is used to store the vertex so that they can be easily searched
    to retrieve information about a particular location. 
 */
public class Map 
{
	private Dijkstra<String> dij;
	private  AvlTree<Vertex<String>> location = new AvlTree<>(new Comparator<Vertex<String>>()
	{
		@Override
		public int compare(Vertex<String> v1, Vertex<String> v2)
		{

			int result = v1.getData().compareTo(v2.getData());
			return result;
		}
	});


	public Map()
	{
		dij = new Dijkstra<>();
		loadTestData();
	}

	private void loadTestData()
	{
		Vertex<String> sanJose = new Vertex<>("San Jose, CA");
		Vertex<String> chicago = new Vertex<>("Chicago, IL");
		Vertex<String> newYork = new Vertex<>("New York City, NY");
		Vertex<String> london = new Vertex<>("London, UK");
		Vertex<String> seattle = new Vertex<>("Seattle, WA");
		Vertex<String> miami = new Vertex<>("Miami, FL");
		Vertex<String> hongKong = new Vertex<>("Hong Kong");
		Vertex<String> india = new Vertex<>("Mumbai, India");
		Vertex<String> dubai = new Vertex<>("Dubia, UAE");
		Vertex<String> libya = new Vertex<>("Libya, Africa");
		Vertex<String> moskau = new Vertex<>("Moskau, Russia");
		Vertex<String> venezuela = new Vertex<>("Venezuela");

		//initializing edges
		sanJose.setEdge(new Edge[] {new Edge<String>(seattle, 1143.34),
				new Edge<String>(chicago, 2968.54),
				new Edge<String>(miami, 4128.99),
				new Edge<String>(newYork, 4117.74),
				new Edge<String>(hongKong, 11198.05)});

		chicago.setEdge(new Edge[] {new Edge<String>(newYork, 1148.84),
				new Edge<String>(miami, 1918.71)});


		newYork.setEdge(new Edge[] {new Edge<String>(london, 5593.25),
				new Edge<String>(libya, 8260.86)});


		london.setEdge(new Edge[] {new Edge<String>(moskau, 2512.36),
				new Edge<String>(libya, 3163.37),
				new Edge<String>(dubai, 5488.68)
		});

		miami.setEdge(new Edge[] {new Edge<String>(venezuela, 2589.02),
		});

		hongKong.setEdge(new Edge[] {new Edge<String>(india, 3644.58),
				new Edge<String>(moskau, 7150.68)});

		india.setEdge(new Edge[] {new Edge<String>(moskau, 5200.71)});

		dubai.setEdge(new Edge[] {new Edge<String>(moskau, 3693),
				new Edge<String>(india, 2482.78)});

		libya.setEdge(new Edge[] {new Edge<String>(dubai, 3810.21),
				new Edge<String>(india, 2482.78)});

		moskau.setEdge(new Edge[] {new Edge<String>(moskau, 2512.36)});

		location.insert(sanJose);
		location.insert(chicago);
		location.insert(newYork);
		location.insert(london);
		location.insert(seattle);
		location.insert(miami);
		location.insert(hongKong);
		location.insert(india);
		location.insert(dubai);
		location.insert(libya);
		location.insert(moskau);
		location.insert(venezuela);
		//Source set to San Jose
		dij.findShortestPath(sanJose);
	}

	public Vertex<String> getVertex(Vertex<String> target)
	{
		return location.get(target);
	}
	public String[][] getPath(Vertex<String> target)
	{
		return dij.getPath(target);
	}
}
