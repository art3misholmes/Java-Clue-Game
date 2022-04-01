package clueGame;

// record classes are cool and new! (https://docs.oracle.com/en/java/javase/17/language/records.html)
// from the specified fields, they automatically get:
// - private final member variables
// - a "canonical constructor" that takes and sets them
// - getters (called e.g. name() rather than getName())
// - sensible implementations of equals(), hashCode(), and toString()
// which is basically everything Card needs!

/**
 * @author Kai Page
 * @author Kelsi Wood
 */
public record Card(Type type, String name) implements Comparable<Card> {
	public static enum Type {
		PERSON, WEAPON, ROOM
	}

	/**
	 * Sort by type, then by name
	 */
	@Override
	public int compareTo(Card o) {
		int cmp = type().compareTo(o.type());
		if (cmp == 0) {
			cmp = name().compareTo(o.name());
		}
		return cmp;
	}
}
