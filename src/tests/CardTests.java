package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import clueGame.*;

public class CardTests {
	private Board board;

	@BeforeEach
	public void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
	}

	//deck is created

	//solution is dealt
	@Test
	public void solutionTests() {
		for(int i=0; i<50; i++) {
			var isRoom = board.getSolution().room().type();
			assertEquals(Card.Type.ROOM, isRoom);
		}
	}

	//other cards

}
