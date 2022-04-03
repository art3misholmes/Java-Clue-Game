package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;

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

			assertEquals(suggestion.weapon().type(), Card.Type.WEAPON);
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
	
	@Test
	public void testMoveTargeting() {
		// setup cells
		var cell_0_0 = new BoardCell(0, 0);
		var cell_0_1 = new BoardCell(0, 1);
		var cell_1_0 = new BoardCell(1, 0);
		cell_1_0.setRoom(true);
		cell_1_0.setRoomCenter(true);
		var cell_1_1 = new BoardCell(1, 1);
		cell_1_1.setRoom(true);
		cell_1_1.setRoomCenter(true);
		
		// setup rooms - (1, 0) is room 1, (1, 1) is room 2
		var room1 = new Room("Room 1", false);
		room1.setCenterCell(cell_1_0);
		room1.setCard(new Card(Card.Type.ROOM, "Room 1"));
		var room2 = new Room("Room 2", false);
		room2.setCenterCell(cell_1_1);
		room2.setCard(new Card(Card.Type.ROOM, "Room 2"));
		
		var cellRooms = new HashMap<BoardCell, Room>();
		cellRooms.put(cell_1_0, room1);
		cellRooms.put(cell_1_1, room2);
		
		var cpu = new ComputerPlayer("Computer Player", Color.BLACK, 2, 2);
		
		// test random decisions between two normal spaces
		var targets = new HashSet<BoardCell>();
		targets.add(cell_0_0);
		targets.add(cell_0_1);
		boolean picked_0_0 = false, picked_0_1 = false;
		for (var i = 0; i < 50; i++) {
			var picked = cpu.selectTarget(targets, cellRooms);
			if (picked == cell_0_0) {
				picked_0_0 = true;
			} else if (picked == cell_0_1) {
				picked_0_1 = true;
			} else {
				fail("Picked an unexpected cell");
			}
		}
		assertTrue(picked_0_0, "Never picked (0, 0)");
		assertTrue(picked_0_1, "Never picked (0, 1)");
		
		// test random decisions between two equivalent rooms
		targets.clear();
		targets.add(cell_1_0);
		targets.add(cell_1_1);
		boolean picked_1_0 = false, picked_1_1 = false;
		for (var i = 0; i < 50; i++) {
			var picked = cpu.selectTarget(targets, cellRooms);
			if (picked == cell_1_0) {
				picked_1_0 = true;
			} else if (picked == cell_1_1) {
				picked_1_1 = true;
			} else {
				fail("Picked an unexpected cell");
			}
		}
		assertTrue(picked_1_0, "Never picked (1, 0)");
		assertTrue(picked_1_1, "Never picked (1, 1)");
		
		// test preference for room over normal space
		targets.clear();
		targets.add(cell_0_0);
		targets.add(cell_1_0);
		for (var i = 0; i < 50; i++) {
			var picked = cpu.selectTarget(targets, cellRooms);
			assertEquals(cell_1_0, picked);
		}
		
		// test no preference for seen room over normal space
		cpu.updateSeen(room1.getCard());
		picked_0_0 = false; picked_1_0 = false;
		for (var i = 0; i < 50; i++) {
			var picked = cpu.selectTarget(targets, cellRooms);
			if (picked == cell_0_0) {
				picked_0_0 = true;
			} else if (picked == cell_1_0) {
				picked_1_0 = true;
			} else {
				fail("Picked an unexpected cell");
			}
		}
		assertTrue(picked_0_0, "Never picked (0, 0)");
		assertTrue(picked_1_0, "Never picked (1, 0)");
		
		// test preference for seen room over unseen room
		targets.clear();
		targets.add(cell_1_0);
		targets.add(cell_1_1);
		for (var i = 0; i < 50; i++) {
			var picked = cpu.selectTarget(targets, cellRooms);
			assertEquals(cell_1_1, picked);
		}
	}
}
