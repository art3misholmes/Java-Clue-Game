package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Player {
	private String name;
	private Color color;
	private int row, column;
	private boolean wasDragged;

	// this is the method name Eclipse came up with! 🤷
	public boolean isWasDragged() {
		return wasDragged;
	}

	public void setWasDragged(boolean wasDragged) {
		this.wasDragged = wasDragged;
	}

	private CardCollection hand = new CardCollection(), seen = new CardCollection(); //Collection of cards that the player has

	//getter
	public CardCollection getSeen() {
		return seen;
	}

	//sets player attributes
	public Player(String name, Color color, int row, int column) {
		this.name = name;
		this.color = color;
		this.row = row;
		this.column = column;
	}

	//adds to seen cards and deals the cards to the player
	public void updateHand(Card card) {
		hand.addCard(card);
		// we see all the cards in our hand
		seen.addCard(card);
	}

	//sets/updates seen cards
	public void updateSeen(Card card) {
		seen.addCard(card);
	}
	
	//disproves a suggestion
	public Card disproveSuggestion(Solution suggestion) {
		var listToDis = Stream.of(suggestion.room(), suggestion.person(), suggestion.weapon())
                .filter(hand::contains)
                .collect(Collectors.toList());
		
		if(listToDis.isEmpty()) {
			return null;
		}else {
			return RandomChooser.pickRandom(listToDis);
		}

	}
	
	//getters
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
	
	public Color getAccentColor() {
		var hsbComponents = Color.RGBtoHSB(getColor().getRed(), getColor().getGreen(), getColor().getBlue(),
				null);
		return Color.getHSBColor(hsbComponents[0], hsbComponents[1] * 0.4f, 0.6f + hsbComponents[2] * 0.4f
				);
		
	}
	
	//create graphics for player
	public void draw(Graphics g, CellMetrics m, int offset) {
		g.setColor(color);
		g.fillOval(m.xOffset() + m.cellWidth() * column + offset, m.yOffset() + m.cellHeight() * row, m.cellWidth(), m.cellHeight());
	}
	
	//setters
	public void setRow(int row) {
		this.row = row;
	}

	public void setColumn(int column) {
		this.column = column;
	}
}
