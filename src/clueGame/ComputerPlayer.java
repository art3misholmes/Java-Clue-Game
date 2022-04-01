package clueGame;

import java.awt.Color;
import java.util.Set;

public class ComputerPlayer extends Player {

	public ComputerPlayer(String name, Color color, int row, int column) {
		super(name, color, row, column);
		// TODO Auto-generated constructor stub
	}
	
	public Solution createSuggestion(CardCollection deck, Card roomCard) {
		return null;
	}
	
	public BoardCell selectTarget(Set<BoardCell> validTargets) {
		return null;
	}

}
