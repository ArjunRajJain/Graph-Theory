import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

public class ConnectedComponentFinder {

	public ConnectedComponentFinder() {
	}

	/*
	 * Find the connected component of a given node
	 * 
	 * @param srcNode - the node whose connected component is to be found.
	 */
	
	//this is all basically the BFS from Fakebook Reader...not much else to say
	public Set<Friend> findCC(Friend srcNode) {
		if (srcNode != null) {
			if (srcNode.friends.size() > 0) {
				Iterator<Friend> iter = new BreadthOrderIterator(srcNode);
				Set<Friend> friends = new HashSet<Friend>();
				while (iter.hasNext()) {
					friends.add(iter.next());
				}
				return friends;
			}
		}
		throw new IllegalArgumentException();
	}

	class BreadthOrderIterator implements Iterator<Friend> {
		Queue<Friend> BreadthOrderQueue;
		HashSet<String> seen = new HashSet<String>();

		public BreadthOrderIterator(Friend friend) {
			BreadthOrderQueue = new LinkedList<Friend>();
			BreadthOrderQueue.add(friend);
			seen.add(friend.id);
		}

		@Override
		public boolean hasNext() {
			return !BreadthOrderQueue.isEmpty();
		}

		@Override
		public Friend next() {
			if (!hasNext())
				throw new NoSuchElementException();
			Friend temp = BreadthOrderQueue.poll();
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

		public LinkedHashMap sortHashMapByValuesD(HashMap passedMap) {
			TreeMap<Friend, Integer> changed = new TreeMap<Friend, Integer>();
			changed.putAll(passedMap);
			ArrayList mapKeys = new ArrayList(changed.keySet());
			ArrayList mapValues = new ArrayList(changed.values());
			Collections.sort(mapValues);
			Collections.reverse(mapValues);
			Collections.sort(mapKeys);
			LinkedHashMap sortedMap = new LinkedHashMap();
			Iterator<Integer> valueIt = mapValues.iterator();
			while (valueIt.hasNext()) {
				int val = valueIt.next();
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
}
