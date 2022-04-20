package clueGame;

// see Card for notes about record classes. tl;dr: they're great!

/**
 * @author Kai Page
 * @author Kelsi Wood
 */
public record Solution(Card room, Card person, Card weapon) {
	@Override
	public String toString() {
		return String.format("%s in %s with %s", person.name(), room.name(), weapon.name());
	}
}
