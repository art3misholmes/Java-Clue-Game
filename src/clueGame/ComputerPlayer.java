package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {
	private Solution pendingAccusation;

	public Solution getPendingAccusation() {
		return pendingAccusation;
	}

	public void setPendingAccusation(Solution pendingAccusation) {
		this.pendingAccusation = pendingAccusation;
	}

	public ComputerPlayer(String name, Color color, int row, int column) {
		super(name, color, row, column);
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

		//rand player
		var p = RandomChooser.pickRandom(unseenPlayers);

		//rand weapon
		var w = RandomChooser.pickRandom(unseenWeapons);

		// create new solution composed of p, w, and given room
		var newCombo = new Solution(roomCard, p, w);

		return newCombo;
	}

	public BoardCell selectTarget(Set<BoardCell> validTargets, Map<BoardCell, Room> cellRooms) {
		// get a target from validTargets
		var priorityTargets = new ArrayList<BoardCell>();
		var otherTargets = new ArrayList<BoardCell>();

		for(var t: validTargets) {
			if(t.isRoom() && !this.getSeen().contains(cellRooms.get(t).getCard())) {
				priorityTargets.add(t);
			}else {
				otherTargets.add(t);
			}
		}
		// priority targets
		if(priorityTargets.size() != 0) {
			return RandomChooser.pickRandom(priorityTargets);
		} else {
			return RandomChooser.pickRandom(otherTargets);
		}
	}

}
