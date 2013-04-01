import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FakebookReader implements Iterable<Friend> {

	/**
	 * Construct a friend graph from the JSON document at filename. If the file
	 * does not exist or is otherwise improperly formatted, create an empty
	 * friend graph.
	 * 
	 * @param filename
	 *            The JSON file to be read
	 **/
	//This is a graph (map) with a key as a string to quickly find friends
	//its necessary for adding friends to the hash map
	HashMap<String, Friend> Users = new HashMap<String, Friend>();
	HashSet<Friend> JustFriends = new HashSet<Friend>(); //This is just the Graph of Friends
	Friend root; //this is the root 

	public FakebookReader(String filename) {
		// Using the Jsonparse was just really easy compared to the
		// JsonReader
		JsonParser parser = new JsonParser();
		String text = "";
		try {
			text = readFile(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		JsonObject JsonFriends = parser.parse(text).getAsJsonObject();
		for (Entry<String, JsonElement> e : JsonFriends.entrySet()) {
			JsonObject user = e.getValue().getAsJsonObject();
			// creating the user
			Friend current = new Friend();
			current.id = e.getKey();

			// if this friend already exists...make it link to that one
			if (Users.containsKey(current.id)) {
				current = Users.get(current.id);
			} else
				current.name = user.get("name").getAsString();

			for (JsonElement friends : user.get("friends").getAsJsonArray()) {
				JsonObject thisFriend = friends.getAsJsonObject();

				// Creating the inner friends
				Friend inner = new Friend();

				inner.id = thisFriend.get("id").getAsString();

				// if this friend already exists...make it link to that one
				if (Users.containsKey(inner.id)) {
					inner = Users.get(inner.id);
				} else
					inner.name = thisFriend.get("name").getAsString();
				// making sure they are friends with each other
				current.friends.put(inner, 0);
				inner.friends.put(current, 0);
				Users.put(inner.id, inner);
			}
			Users.put(current.id, current);
		}

		// Entering in mutual funds
		for (Friend e : Users.values()) {
			for (Friend inner : e.friends.keySet()) {
				int mutual = 0;
				for (Friend inner2 : Users.get(inner.id).friends.keySet()) {
					if (e.friends.containsKey(inner2))
						mutual++;
				}
				inner.friends.put(e, (int) Math.floor(1000 / (mutual + 1)));
			}
		}
		
		//Make the Friend Graph
		JustFriends = new HashSet<Friend>();
		JustFriends.addAll(Users.values());
	}

	// used to convert the json file to a massive string
	private static String readFile(String path) throws IOException {
		FileInputStream stream = new FileInputStream(new File(path));
		try {
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
					fc.size());
			return Charset.defaultCharset().decode(bb).toString();
		} finally {
			stream.close();
		}
	}

	public void setRoot(Friend root) {
		this.root = root;
	}

	/**
	 * Get the root of the friend graph built from the file we were given in the
	 * constructor.
	 * 
	 * @returns the root of the friend graph
	 **/
	public Friend getRoot() {
		return root;
	}

	/**
	 * Returns an iterator that traverses the graph in BFS order from the root
	 * When adding nodes to the queue, the ordering should be based on edge
	 * weight in ascending order, and then ID in ascending order if tied. You
	 * can assume all nodes will be connected through the root node.
	 * 
	 */
	public Iterator<Friend> iterator() {
		return new BreadthOrderIterator();
	}

	class BreadthOrderIterator implements Iterator<Friend> {
		Queue<Friend> BreadthOrderQueue; 
		
		//this stores all friends who have been seen
		HashSet<String> seen = new HashSet<String>();

		//initialize the iterator with Shannon
		public BreadthOrderIterator() {
			BreadthOrderQueue = new LinkedList<Friend>();
			setRoot(Users.get("732100026"));
			BreadthOrderQueue.add(getRoot());
			seen.add(getRoot().id);
		}

		@Override
		public boolean hasNext() {
			return !BreadthOrderQueue.isEmpty();
		}

		@Override
		public Friend next() {
			if (!hasNext()) throw new NoSuchElementException();
			Friend temp = BreadthOrderQueue.poll();
			
			//sort friends by their weights
			HashMap<Friend, Integer> friends = temp.friends;
			HashMap<Friend, Integer> sorted = sortHashMapByValuesD(friends);
			for (Friend e : sorted.keySet()) {
				if (!seen.contains(e.id)) {
					seen.add(e.id);
					BreadthOrderQueue.add(e);
				}
			}
			return temp;
		}

		@Override
		public void remove() {
		}

		//sorting algorithm
		public LinkedHashMap sortHashMapByValuesD(HashMap passedMap) {
			//tree map sorts teh friends by their ids
			TreeMap<Friend, Integer> changed = new TreeMap<Friend, Integer>();
			changed.putAll(passedMap);
			
			//we split the weights and friends and sort both
			ArrayList mapKeys = new ArrayList(changed.keySet());
			ArrayList mapValues = new ArrayList(changed.values());
			Collections.sort(mapValues);
			Collections.reverse(mapValues);
			Collections.sort(mapKeys);
			
			//the final sorted hashmap
			LinkedHashMap sortedMap = new LinkedHashMap();
			//make an iterator for the values so we put 
			Iterator<Integer> valueIt = mapValues.iterator();
			
			
			//put the values back into the sorted map
			while (valueIt.hasNext()) {
				int val = valueIt.next();
				
				//this while loops find the appropriate friend
				//whose weight is the val
				//it then puts the friend in sortedmap and 
				//removes it from changed. 
				//since the friends are in order of id
				//the iterator will show them in that order
				//and therefore we will get sortings by
				//id and weight
				Iterator<Friend> keyIt = mapKeys.iterator();
				while (keyIt.hasNext()) {
					Friend key = keyIt.next();
					int comp = changed.get(key);
					if (comp==val) {
						changed.remove(key);
						mapKeys.remove(key);
						sortedMap.put(key, val);
						break;
					}
				}
			}
			return sortedMap;
		}
	}

	// ****************************************
	// NOTE: next two methods to be implemented in section 2.3, not 2.2
	// ****************************************

	/**
	 * Uses ConnectedComponentFinder to generate the entire set of connected
	 * components in the graph. Each CC is a Set of Friends, so the set of CCs
	 * is a Set of Set of friends. Your implementation here should not be
	 * inefficient - that is, it should not call BFS from every single node.
	 */
	public Set<Set<Friend>> getConnectedComponents() {
		return getCCHelper(JustFriends);
	}

	//helper method for getConnectedComponents(WithoutRoot)
	public Set<Set<Friend>> getCCHelper(Collection<Friend> friends) {
		Set<Set<Friend>> megaSet = new HashSet<Set<Friend>>();
		ConnectedComponentFinder getCC = new ConnectedComponentFinder();
		for (Friend e : friends) {
			Iterator<Set<Friend>> iter = megaSet.iterator();
			boolean add = true;
			while (iter.hasNext()) {
				if (iter.next().contains(e))
					add = false;
			}
			if (add) megaSet.add(getCC.findCC(e));
		}
		return megaSet;
	}

	/**
	 * Uses ConnectedComponentFinder to generate the entire set of connected
	 * components in the graph when the root has been removed (ie, the root
	 * should not be in any of the connected components). Each CC is a Set of
	 * Friends, so the set of CCs is a Set of Set of friends.
	 * 
	 * Your implementation here should not be inefficient - that is, it should
	 * not call BFS from every single node.
	 */
	public Set<Set<Friend>> getConnectedComponentsWithoutRoot() {
		//removes the root from the graph and gets the connected components
		HashSet<Friend> WRUsers = new HashSet<Friend>(JustFriends);
		WRUsers.remove(getRoot().id);
		for (Friend e : WRUsers) {
			e.friends.remove(getRoot());
		}
		return getCCHelper(WRUsers);
	}
}