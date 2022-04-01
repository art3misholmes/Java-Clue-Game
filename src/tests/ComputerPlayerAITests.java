package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import org.junit.jupiter.api.*;
import clueGame.*;

public class ComputerPlayerAITests {

	@Test
	public void testMakingSuggestions() {
		var deck = new CardCollection();
		for (var i = 0; i < 3; i++) {
			deck.addCard(new Card(Card.Type.PERSON, "Person " + i));
			deck.addCard(new Card(Card.Type.ROOM, "Room " + i));
			deck.addCard(new Card(Card.Type.WEAPON, "Weapon " + i));
		}

		var cpu = new ComputerPlayer("Computer Player", Color.BLUE, 0, 0);
		// has Person 0, has seen Weapon 0, is in Room 0
		cpu.updateHand(new Card(Card.Type.PERSON, "Person 0"));
		cpu.updateSeen(new Card(Card.Type.WEAPON, "Weapon 0"));
		var roomCard = new Card(Card.Type.ROOM, "Room 0");

		boolean hasSuggestedPerson1 = false, hasSuggestedPerson2 = false, hasSuggestedWeapon1 = false,
				hasSuggestedWeapon2 = false;
		for (var i = 0; i < 50; i++) {
			var suggestion = cpu.createSuggestion(deck, roomCard);
			
			assertEquals(roomCard, suggestion.room());
			
			assertEquals(suggestion.person().type(), Card.Type.PERSON);
			switch (suggestion.person().name()) {
			case "Person 1" -> hasSuggestedPerson1 = true;
			case "Person 2" -> hasSuggestedPerson2 = true;
			default -> fail("should not suggest person " + suggestion.person().name());
			}

			assertEquals(suggestion.person().type(), Card.Type.WEAPON);
			switch (suggestion.weapon().name()) {
			case "Weapon 1" -> hasSuggestedWeapon1 = true;
			case "Weapon 2" -> hasSuggestedWeapon2 = true;
			default -> fail("should not suggest weapon " + suggestion.weapon().name());
			}
		}
		
		assertTrue(hasSuggestedPerson1);
		assertTrue(hasSuggestedPerson2);
		assertTrue(hasSuggestedWeapon1);
		assertTrue(hasSuggestedWeapon2);
	}
}
