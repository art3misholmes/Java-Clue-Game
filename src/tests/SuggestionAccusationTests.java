package tests;

import static org.junit.jupiter.api.Assertions.*;
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
}
