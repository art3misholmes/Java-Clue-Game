package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {

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
		// rand num from 0-arrarylist size
		Random rand = new Random();
		int randPosition = rand.nextInt(unseenPlayers.size());

		// arraylist at rand num
		var p = unseenPlayers.get(randPosition);

		//rand weapon
		// rand num from 0-arrarylist size
		Random rand2 = new Random();
		int randPositionW = rand.nextInt(unseenWeapons.size());

		// arraylist at rand num
		var w = unseenWeapons.get(randPositionW);

		// create new solution composed of p, w, and given room
		var newCombo = new Solution(roomCard, p, w);

		return newCombo;
	}

	public BoardCell selectTarget(Set<BoardCell> validTargets, Map<BoardCell, Room> cellRooms) {
		// get a target from validTargets
		var targetPrioity = new ArrayList<BoardCell>();
		var targetNon = new ArrayList<BoardCell>();

		for(var t: validTargets) {
			if(t.isRoom() && !this.getSeen().contains(cellRooms.get(t).getCard())) {
				targetPrioity.add(t);
			}else {
				targetNon.add(t);
			}
		}
		// priority targets
		Random rand = new Random();
		if(targetPrioity.size() != 0) {
			//pick rand and return it
			// rand num from 0-arrarylist size
			int randPosition = rand.nextInt(targetPrioity.size());
			return targetPrioity.get(randPosition);
		}else {
			//non
			int randPosition = rand.nextInt(targetNon.size());
			return targetNon.get(randPosition);
		}
	}

}