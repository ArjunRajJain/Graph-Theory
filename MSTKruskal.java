
import java.io.*;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class MSTKruskal {

  /**
   * Generate the Minimum Spanning Tree of the graph. The graph is passed in
   * as a set of friends, as each friend knows his/her own friends.  
   *
   * Note that you also need to output a file for MST, though that does not
   * necessarily need to happen in this method.
   */
	private static HashSet<Edge> mst = new HashSet<Edge>();
	
    public static HashSet<Edge> MST(HashSet<Friend> people) {
    	BufferedWriter writeout = null; //to write out to a CSV
    	
    	//this is used to map the Friends hashcode to a number that fits in the unionfind array
    	HashMap<Integer,Integer> mappednums = new HashMap<Integer,Integer>(); 
    	
    	//the count will be used to map the friend to a certain number for union find
    	int count = 0;
    	
    	try {
			writeout = new BufferedWriter(new FileWriter("MST.csv"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	
    	//initializes ths Priority Queue with the proper comparator
        PriorityQueue<Edge> pq = new PriorityQueue<Edge>(1,new KEdge());
        
        //get the edges of friends
        HashSet<Edge> edges = new HashSet<Edge>();
        for(Friend f : people) {
        	for(Friend g : f.friends.keySet()) {
        		Edge edge = new Edge(f,g);
        		if(!edges.contains(edge)) {
        			edges.add(edge);
        			pq.add(edge);
        		}
        	}
        }
        
        UnionFind uf = new UnionFind(people.size());
        
        //createing the MST
        while (!pq.isEmpty() && mst.size() < edges.size()) {
        	//get the edge
        	Edge e = pq.poll();
        	int v = e.A.hashCode();
        	int w = e.B.hashCode();
        	
        	//see if the friends have already been added into mapped nums
        	//if so then set them to their appropriate number
        	//if not then set them to the count
        	if (mappednums.containsKey(v)) v = mappednums.get(v);
        	else {
        		mappednums.put(v, count);
        		v = count;
        		count++;
        	}
        	if (mappednums.containsKey(w)) w = mappednums.get(w);
        	else {
        		mappednums.put(w, count);
        		w = count;
        		count++;
        	}
        	
        	//if they are not connected then add them to the mst!
        	if(!uf.connected(v,w)) {
        		uf.union(v, w);
        		mst.add(e);
    			try {
					writeout.append(e.A.id + ";" + e.B.id);
					System.out.println(e.A.id + ";" + e.B.id);
					writeout.newLine();
				} catch (IOException io) {
					io.printStackTrace();
				}
        	}
        }
        try {
			writeout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return mst;
    }
    
    //this was basically borrowed from the book
    static class UnionFind {
    	private int[] id;
    	private int[] sz;
    	public UnionFind(int N) {
    		id = new int[N];
    		sz = new int[N];
    		for (int i = 0; i < N; i++) {
    			id[i] = i; 
    			sz[i] = i;
    		}
    	}
    	private int root(int i) {
    		while (i != id[i]) {
    			i = id[id[i]];
    			i = id[i];
    		}
    		return i;
    	}
    	public void union(int p, int q) {
    		int i = root(p);
    		int j = root(q);
    		if(sz[i] < sz[j]) { 
    			id[i] = j; 
    			sz[j] += sz[i]; 
    		} 
    		else { 
    			id[j] = i; 
    			sz[i] += sz[j]; 
    		} 
    	}
    	public boolean connected(int p, int q) {
			return root(p) == root(q);
    	}
    }
    
    //comparator for Edges
    static class KEdge implements Comparator<Edge>{
		public int compare(Edge either, Edge other) {
			if(either.equals(other)) return 0;
			return either.A.friends.get(either.B).compareTo(other.A.friends.get(other.B));
		}
    }
    
}
