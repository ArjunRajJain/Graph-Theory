import java.util.Set;

/*
 * This class will find the closeness centrality of a given node
 */
public class ClosenessCentrality {

  /*
   * Find the closeness centrality for the source, measured by the
   * reciprocal of the sum of the distances between the source and all of the 
   * other nodes in the graph.
   * 
   * @return the closeness centrality of the source
   */
	
	//basically gets the Djisktra of a source...and then does an
	//easy calculation
  public static double Centrality(Set<Friend> people, Friend source) {
	  Dijkstra d = new Dijkstra();
	  DijkstraPair pair = d.dijkstraSP(people, source);
	  double count = 0.0;
	  for(int i: pair.distanceMap.values()) {
		  count+=i;
	  }
	  return 1/count;
  }
}
