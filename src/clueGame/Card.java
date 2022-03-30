package clueGame;

// record types are cool and new! (java 16+)
// specify stuff in the header and get:
// - private final fields
// - a public constructor with parameters to set those fields
// - automatically generated getters (called e.g. type() instead of getType())
// - sensible implementations of equals(), hashCode(), and toString()
// which is basically everything Card needs!

/**
 * @author Kai Page
 * @author Kelsi Wood
 */
public record Card(Type type, String name) {
	
	public static enum Type {
		PERSON,
		WEAPON,
		ROOM
	}
}
