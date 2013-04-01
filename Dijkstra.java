import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 *
 * @author Shaanan Cohney
 */
public class Dijkstra {
	private PriorityQueue<Friend> pq;
	private HashMap<Friend,Integer> distanceMap;
	private HashMap<Friend, Friend> parentMap;
	private HashSet<Edge> seen;
	
    /*
     * Runs Dijkstra's algorithm to find the shortest path between the given 
     * source and all nodes in people. 
     * To return both the distances and the actual paths that Dijkstra's gives,
     * you will store those in a DijkstraPair. All Friends in people should be
     * included in both Maps of the pair. 
     * dijkstraPair.parentMap.get(source) should be null.
     */
	
	//the comparator for friends used in djikstra!
	class ExtFriend extends Friend implements Comparator<Friend> {
		public int compare(Friend f1, Friend f2){
			return distanceMap.get(f1).compareTo(distanceMap.get(f2));
		}
	}
	
	
    public DijkstraPair dijkstraSP(Set<Friend> people, Friend source) {
    	//all edges that have been seen --saves time
    	seen = new HashSet<Edge>();
    	
    	//pq that is off the appropriate size and uses the proper comparator
    	pq = new PriorityQueue<Friend>(people.size(),new ExtFriend());
    	
    	//get the distance from this friend to the root
    	distanceMap = new HashMap<Friend,Integer>();
    	
    	//who is the direct parent of any node 
    	parentMap = new HashMap<Friend,Friend>();
    	
    	//make all distances a max
        for(Friend f : people) {
        	distanceMap.put(f,Integer.MAX_VALUE);
        }
        
        //add the root
    	pq.add(source);
    	
    	//the roots distance to itself is 0
    	distanceMap.put(source, 0);
    	
    	//calculate dijsktra by relaxing edges of friends
    	//the item extracted from the pq is bound to be in the dijkstra graph
    	while(!pq.isEmpty()) {
    		Friend parent = pq.remove();
    		for(Friend child : parent.friends.keySet()) {
    			Edge edge = new Edge(parent,child);
    			if(!seen.contains(edge)) {
    				seen.add(edge);
    				relax(edge);
    			}
    		}
    	}
    	
    	//source has no parent
    	parentMap.put(source, null);
    	
    	
    	return new DijkstraPair(distanceMap, parentMap);
    }
    
    //to populat the priority queue by adding the edges iff 
    //the edge weight is smaller from the root than what it
    //already is
    //the algorithm is basically from the book
    private void relax(Edge e) {
    	Friend parent = e.A;
    	Friend child = e.B;
    	if(distanceMap.get(child)> distanceMap.get(parent)+parent.friends.get(child)) {
    		distanceMap.put(child,distanceMap.get(parent)+parent.friends.get(child));
    		parentMap.put(child,parent);
    		if(pq.contains(child)) {
    			pq.remove(child);
    		}
    		pq.add(child);
    	}
    }
}
