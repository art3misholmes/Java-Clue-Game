package clueGame;

import java.util.Set;
import java.util.TreeSet;

/**
 * Stores cards in a deck, hand, memory, etc.
 * 
 * @author Kai Page
 * @author Kelsi Wood
 */
public class CardCollection {
	private TreeSet<Card> cards = new TreeSet<>();

	public void addCard(Card card) {
		cards.add(card);
	}

	public Set<Card> getCards() {
		return cards;
	}
	
	// order is person < weapon < room
	
	public Set<Card> getPersonCards() {
		return cards.headSet(
				// from person
				new Card(Card.Type.WEAPON, "")
		);
	}
	
	public Set<Card> getWeaponCards() {
		return cards.subSet(
				new Card(Card.Type.WEAPON, ""), 
				new Card(Card.Type.ROOM, "")
		);
	}
	
	public Set<Card> getRoomCards() {
		return cards.tailSet(
				new Card(Card.Type.ROOM, "")
				// to end
		);
	}
}
