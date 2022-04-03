package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import org.junit.jupiter.api.*;
import clueGame.*;

public class SuggestionAccusationTests {
	private Board board;

	@BeforeEach
	public void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
	}

	@Test
	public void testCheckAccusation() {
		assertTrue(board.checkAccusation(board.getSolution()));

		var rightRoom = board.getSolution().room();
		var rightPerson = board.getSolution().person();
		var rightWeapon = board.getSolution().weapon();

		var wrongRoom = new Card(Card.Type.ROOM, "Wrong Room");
		assertFalse(board.checkAccusation(new Solution(wrongRoom, rightPerson, rightWeapon)));

		var wrongPerson = new Card(Card.Type.PERSON, "Wrong Person");
		assertFalse(board.checkAccusation(new Solution(rightRoom, wrongPerson, rightWeapon)));

		var wrongWeapon = new Card(Card.Type.WEAPON, "Wrong Weapon");
		assertFalse(board.checkAccusation(new Solution(rightRoom, rightPerson, wrongWeapon)));
	}

	public void disprove() {
		//cp player
		var player_01 = new ComputerPlayer("Computer Player", Color.BLUE, 0, 0);
		//in hand
		var cardPerson = new Card(Card.Type.PERSON, "hand Person");
		var cardWeapon = new Card(Card.Type.WEAPON, "hand Weapon");
		var cardRoom = new Card(Card.Type.ROOM, "hand Room");

		player_01.getHand().addCard(cardPerson);
		player_01.getHand().addCard(cardWeapon);
		player_01.getHand().addCard(cardRoom);

		//not hand
		var cardPersonNotHand = new Card(Card.Type.PERSON, "Not Hand Person");
		var cardWeaponNotHand = new Card(Card.Type.WEAPON, "Not Hand Weapon");
		var cardRoomNotHand = new Card(Card.Type.ROOM, "Not Hand Room");

		//test is in hand
		// create a solution with one of players cards
		var testSolutionInHand = new Solution(cardRoomNotHand, cardPerson,cardWeaponNotHand);
		var disInHand = player_01.disproveSuggestion(testSolutionInHand);
		assertEquals(cardPerson,disInHand);

		//test is not in hand
		var testNotInHand = new Solution(cardRoomNotHand, cardPersonNotHand,cardWeaponNotHand);
		var disNotInHand = player_01.disproveSuggestion(testNotInHand);
		assertNull(disNotInHand);

		//test multiple 
		boolean hasPerson = false, hasWeapon = false;

		for (var i = 0; i < 50; i++) {

			var testSolutionInHandMul = new Solution(cardRoomNotHand, cardPerson,cardWeapon);
			var disInHandMul = player_01.disproveSuggestion(testSolutionInHandMul);

			//person
			if(disInHandMul.equals(cardPerson)) {
				hasPerson = true;
			}else if(disInHandMul.equals(cardWeapon)) {
				//weapon
				hasWeapon =true;
			}else {
				//fail
				fail("Not expected card");
			}
		}

		assertTrue(hasPerson);
		assertTrue(hasWeapon);
		
	}
}
