import java.util.HashMap;

public class Betweeness {
    /**
     * Construct an object to compute betweeness centrality
     * for the given friend graph
     **/
	HashMap<Friend,Integer> betweens = new HashMap<Friend,Integer>();
    public Betweeness(HashMap<String, Friend> people) {
    	
    }


    /**
     * Compute betweeness centrality for a node in the friend graph using
     * Brandes' algorithm: 
     * http://www.inf.uni-konstanz.de/algo/publications/b-fabc-01.pdf
     * Note this algorithm gives betweeness centrality for all nodes.
     * You may want to consider computing this only once and storing
     * it in a lookup table that this method refers to.
     *
     * @returns the betweeness centrality of root
     **/
    public double calculate(Friend root) {
        return 0.0;
    }
}
