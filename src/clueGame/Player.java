package clueGame;

import java.awt.Color;
import java.util.Set;
import java.util.TreeSet;

public abstract class Player {
	private String name;
	private Color color;
	private int row, column;

	private CardCollection hand = new CardCollection(), seen = new CardCollection();

	public CardCollection getSeen() {
		return seen;
	}

	public Player(String name, Color color, int row, int column) {
		this.name = name;
		this.color = color;
		this.row = row;
		this.column = column;
	}

	public void updateHand(Card card) {
		hand.addCard(card);
		// we see all the cards in our hand
		seen.addCard(card);
	}
	
	public void updateSeen(Card card) {
		seen.addCard(card);
	}
	
	public Card disproveSuggestion(Solution suggestion) {
		return null;
	}

	public String getName() {
		return name;
	}

	public Color getColor() {
		return color;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public CardCollection getHand() {
		return hand;
	}
}
