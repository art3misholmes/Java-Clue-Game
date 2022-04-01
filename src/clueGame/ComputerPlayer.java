package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ComputerPlayer extends Player {

	public ComputerPlayer(String name, Color color, int row, int column) {
		super(name, color, row, column);
		// TODO Auto-generated constructor stub
	}
	
	public Solution createSuggestion(CardCollection deck, Card roomCard) {
		var personCards = deck.getPersonCards();
		var weaponCards = deck.getWeaponCards();
		
		var seenPlayers = this.getSeen().getPersonCards();
		var seenWeapons = this.getSeen().getWeaponCards();
		
		//players
		var unseenPlayers = new ArrayList<Card>();
		unseenPlayers.addAll(personCards);
		unseenPlayers.removeAll(seenPlayers);
		
		//weapons
		var unseenWeapons = new ArrayList<Card>();
		unseenWeapons.addAll(weaponCards);
		unseenWeapons.removeAll(seenWeapons);
		
		//make rand - get rand
		
		
		
		return null;
	}
	
	public BoardCell selectTarget(Set<BoardCell> validTargets) {
		return null;
	}

}
