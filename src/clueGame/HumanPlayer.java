package clueGame;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class HumanPlayer extends Player {
	private final Map<Card, Color> presentedBy = new HashMap<>();

	public HumanPlayer(String name, Color color, int row, int column) {
		super(name, color, row, column);
		// TODO Auto-generated constructor stub
	}

	public Map<Card, Color> getPresentedBy() {
		return presentedBy;
	}

	public void updatePresentedBy(Card c, Player p) {
		presentedBy.put(c, p.getAccentColor());
	}
}
