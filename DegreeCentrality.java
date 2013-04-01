import java.util.HashSet;
import java.util.Set;

public class DegreeCentrality {

  /**
   * Finds the degree centrality of a given node
   * If people is null or source cannot be found in the graph, throw and
   * IllegalArgumentException
   * @param source is the ID of the source node
   *
   */
	
	//this is fairly straight forward...
	public static int getDegree(Set<Friend> people, String source) {
		//check for exceptions
		if (people == null) throw new IllegalArgumentException();
		HashSet<Edge> edges = MSTKruskal.MST((HashSet<Friend>) people);
		if (edges.size() == 0) throw new IllegalArgumentException();
		
		//calculates the centrality by keeping a count
		int count = 0;
		for (Edge e: edges) {
			if(e.A.id.equals(source)||e.B.id.equals(source)) count++;
		}
		return count;
    }
    public static void main(String[] args) {
    	FakebookReader f = new FakebookReader("5.json");
    	HashSet<Friend> friends =  new HashSet<Friend>();
    	friends.addAll(f.Users.values());
    	System.out.println(getDegree(friends, "531553417"));
    }
	
}
