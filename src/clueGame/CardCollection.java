package clueGame;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Stores cards in a deck, hand, etc.
 * 
 * @author Kai Page
 * @author Kelsi Wood
 */
public class CardCollection {
	private TreeSet<Card> cards = new TreeSet<>();

	public void addCard(Card card) {
		cards.add(card);
	}

	public boolean contains(Card card) {
		return cards.contains(card);
	}

	public void clear() {
		cards.clear();
	}

	public SortedSet<Card> getCards() {
		return cards;
	}

	public SortedSet<Card> getCardsOfType(Card.Type type) {
		return switch (type) {
		case PERSON -> getPersonCards();
		case WEAPON -> getWeaponCards();
		case ROOM -> getRoomCards();
		};
	}

	// order is person < weapon < room

	public SortedSet<Card> getPersonCards() {
		return cards.headSet(
				// from person
				new Card(Card.Type.WEAPON, ""));
	}

	public SortedSet<Card> getWeaponCards() {
		return cards.subSet(new Card(Card.Type.WEAPON, ""), new Card(Card.Type.ROOM, ""));
	}

	public SortedSet<Card> getRoomCards() {
		return cards.tailSet(new Card(Card.Type.ROOM, "")
		// to end
		);
	}
}
