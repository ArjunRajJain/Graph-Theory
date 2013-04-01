import java.util.Map;

/**
 * This class is used to store the two Maps that Dijkstra's must return.
 */
class DijkstraPair{
	
	/*
	 * distanceMap stores the minimum distance from the source to each other
     * friend in the graph. 
	 */
	public Map<Friend, Integer> distanceMap;
	
	/*
	 * parentMap stores the predecessor of each node in the shortest path from 
     * the source to that node. This can be used to recreate the path from the
     * source to any given
	 * node.
	 */
	public Map<Friend, Friend> parentMap;
	
	public DijkstraPair(Map<Friend, Integer> distMap, Map<Friend, Friend> parMap){
		distanceMap = distMap;
		parentMap = parMap;
	}
}