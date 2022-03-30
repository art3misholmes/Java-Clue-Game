package clueGame;

import java.awt.Color;
import java.util.Set;
import java.util.TreeSet;

public abstract class Player {
	private String name;
	private Color color;
	private int row, column;
	
	private Set<Card> hand = new TreeSet<>();
	
	public Player(String name, Color color, int row, int column) {
		this.name = name;
		this.color = color;
		this.row = row;
		this.column = column;
	}
	
	public void updateHand(Card card) {
		
	}
}
