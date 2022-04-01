package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import clueGame.*;
import static tests.Assertions.*;

/**
 * @author Kai Page
 * @author Kelsi Wood
 */
public class CardTests {
	private Board board;

	@BeforeEach
	public void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
	}

	// person +weapon+room
	public static final int ROOM_CARDS = 9, PERSON_CARDS = 6, WEAPON_CARDS = 6,
			TOTAL_CARDS = ROOM_CARDS + PERSON_CARDS + WEAPON_CARDS;

	// deck is created
	@Test
	public void creationTests() {
//		for (var c : board.getDeck().getCards()) {
//			System.out.println(c);
//		}
		assertSetContents(board.getDeck().getCards(), TOTAL_CARDS, new Card(Card.Type.ROOM, "Kitchen Arena"),
				new Card(Card.Type.PERSON, "MessBot7337"), new Card(Card.Type.WEAPON, "Butcher Knife"));
		assertSetContents(board.getDeck().getRoomCards(), ROOM_CARDS, new Card(Card.Type.ROOM, "Rec Room"));
		assertSetContents(board.getDeck().getPersonCards(), PERSON_CARDS, new Card(Card.Type.PERSON, "Professor Norstar"));
		assertSetContents(board.getDeck().getWeaponCards(), WEAPON_CARDS, new Card(Card.Type.WEAPON, "Flambé Torch"));
	}

	// solution is dealt
	@Test
	public void solutionTests() {
		for (int i = 0; i < 50; i++) {
			var isRoom = board.getSolution().room().type();
			assertEquals(Card.Type.ROOM, isRoom);

			var isWeapon = board.getSolution().weapon().type();
			assertEquals(Card.Type.WEAPON, isWeapon);

			var isPerson = board.getSolution().person().type();
			assertEquals(Card.Type.PERSON, isPerson);

			// shuffle
			board.deal();
		}
	}

	// other cards
	@Test
	public void otherDealTests() {
		for (int i = 0; i < 50; i++) {
			int sum = 0;
			int max = Integer.MIN_VALUE;
			int min = Integer.MAX_VALUE;

			for (var p : board.allPlayers()) {
				// if players hands are same size
				var size = p.getHand().getCards().size();
				sum += size;

				if (size > max) {
					max = size;
				}

				if (size < min) {
					min = size;
				}
			}

			// players should have all the cards except the solution
			assertEquals(TOTAL_CARDS - 3, sum);
			// max should be no more than 1 higher than min for ~equal distribution
			assertTrue(max - 1 <= min);
			
			// shuffle
			board.deal();
		}
	}
}
