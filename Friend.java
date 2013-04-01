import java.io.Serializable;
import java.util.HashMap;

public class Friend implements Comparable, Serializable {

	// Basic Elements
	public String name;
	public String id;
	// The value in each k-v pair here is the weight of the edge between
	// this user and that friend.
	// Weight is defined as Math.floor(1000 / (number of mutual friends + 1))
	// where mutual friends = number of friends two users have in common in
	// the graph.
	public HashMap<Friend, Integer> friends;

	public Friend() {
		friends = new HashMap<Friend, Integer>();
	}

	@Override
	public String toString() {
		return null;
	}

	// Suitable as facebook IDs are unique
	@Override
	public int hashCode() {
		return (Long.decode(id).hashCode());
	}

	@Override
	public int compareTo(Object o) {
		Friend friend = (Friend) o;
		return this.id.compareTo(friend.id);
	}
}
