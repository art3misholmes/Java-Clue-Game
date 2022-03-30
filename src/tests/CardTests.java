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
	
	//person +weapon+room
	static final int totalNumCard = 9+6+6;

	//solution is dealt
	@Test
	public void solutionTests() {
		for(int i=0; i<50; i++) {
			var isRoom = board.getSolution().room().type();
			assertEquals(Card.Type.ROOM, isRoom);
			
			var isWeapon = board.getSolution().weapon().type();
			assertEquals(Card.Type.WEAPON, isWeapon);
			
			var isPerson = board.getSolution().person().type();
			assertEquals(Card.Type.PERSON, isPerson);	
			
			//shuffle
			board.deal();
		}
	}
	
	//other cards
	public void other() {
		int sum =0;
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		
		for(var p : board.allPlayers()) {
			//if players hands are same size
			var size = p.getHand().size();
			sum += size;
			
			if(size > max) {
				max = size;
			}
			
			if(size < min) {
				min = size;
			}
		}
		
		assertEquals(totalNumCard-3, sum);
		assertTrue(max-1 <= min);
	}
}
