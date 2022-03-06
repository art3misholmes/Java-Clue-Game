package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;


public class BoardAdjTargetTest {

	public BoardAdjTargetTest() {
		// TODO Auto-generated constructor stub
	}

	// We make the Board static because we can load it one time and 
	// then do all the tests. 
	private static Board board;

	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout306.csv", "ClueSetup306.txt");		
		// Initialize will load config files 
		board.initialize();
	}

	// Ensure that player does not move around within room
	// These cells are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testAdjacenciesRoomsAndDoors()
	{
		Set<BoardCell> testList = board.getAdjList(10, 1);
		assertSetContents(testList, board.getCell(10, 3));

		testList = board.getAdjList(23, 20);
		assertSetContents(testList, board.getCell(20, 19), board.getCell(2, 20));

		testList = board.getAdjList(19, 3);
		assertSetContents(testList, board.getCell(23, 2), board.getCell(19, 2), board.getCell(18, 3), board.getCell(19, 4));

	}


	// Test a variety of walkway scenarios
	// These tests are Orange on the planning spreadsheet
	@Test
	public void testAdjacencyWalkways()
	{
		Set<BoardCell> testList = board.getAdjList(2, 3);
		assertSetContents(testList, board.getCell(1, 3), board.getCell(2, 4));

		testList = board.getAdjList(2, 18);
		assertSetContents(testList, board.getCell(1, 18), board.getCell(2, 17), board.getCell(3, 18));

		testList = board.getAdjList(9, 7);
		assertSetContents(testList, board.getCell(8, 7), board.getCell(9, 6), board.getCell(9, 8), board.getCell(10, 7));

		testList = board.getAdjList(13, 17);
		assertSetContents(testList, board.getCell(12, 17), board.getCell(13, 18));
	}


	// Tests out of room center, 1, 3 and 4
	// These are LIGHT PINK on the planning spreadsheet
	@Test
	public void testTargetsInTestKitchen() {
		// test a roll of 1
		board.calcTargets(board.getCell(9, 20), 1);
		Set<BoardCell> targets= board.getTargets();
		assertSetContents(targets, board.getCell(10, 18), board.getCell(5, 20));

		// test a roll of 3
		board.calcTargets(board.getCell(9, 20), 3);
		targets= board.getTargets();
		assertSetContents(targets,10, board.getCell(12, 18), board.getCell(5, 22), board.getCell(2, 20));

		// test a roll of 4
		board.calcTargets(board.getCell(9, 20), 4);
		targets= board.getTargets();
		assertSetContents(targets,15, board.getCell(11, 16), board.getCell(4, 20), board.getCell(2, 20));
	}

	@Test
	public void testTargetsInWalkWay() {
		// test a roll of 1
		board.calcTargets(board.getCell(10, 4), 1);
		Set<BoardCell> targets= board.getTargets();
		assertSetContents(targets, board.getCell(9, 4), board.getCell(10, 3), board.getCell(10, 5), board.getCell(11, 4));

		// test a roll of 3
		board.calcTargets(board.getCell(10, 4), 3);
		targets= board.getTargets();
		assertSetContents(targets,11, board.getCell(7, 4), board.getCell(12, 5), board.getCell(10, 1));

		// test a roll of 4
		board.calcTargets(board.getCell(10, 4), 4);
		targets= board.getTargets();
		assertSetContents(targets,13, board.getCell(6, 4), board.getCell(12, 6), board.getCell(10, 1));
	}

	// Tests out of room center, 1, 3 and 4
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsAtDoor() {
		// test a roll of 1, at door
		board.calcTargets(board.getCell(8, 17), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(12, 20)));
		assertTrue(targets.contains(board.getCell(7, 17)));	
		assertTrue(targets.contains(board.getCell(8, 18)));	

		// test a roll of 3
		board.calcTargets(board.getCell(8, 17), 3);
		targets= board.getTargets();
		assertEquals(12, targets.size());
		assertTrue(targets.contains(board.getCell(12, 20)));
		assertTrue(targets.contains(board.getCell(3, 20)));
		assertTrue(targets.contains(board.getCell(7, 17)));	
		assertTrue(targets.contains(board.getCell(7, 19)));
		assertTrue(targets.contains(board.getCell(9, 15)));	

		// test a roll of 4
		board.calcTargets(board.getCell(8, 17), 4);
		targets= board.getTargets();
		assertEquals(15, targets.size());
		assertTrue(targets.contains(board.getCell(12, 20)));
		assertTrue(targets.contains(board.getCell(3, 20)));
		assertTrue(targets.contains(board.getCell(10, 15)));	
		assertTrue(targets.contains(board.getCell(6, 17)));
		assertTrue(targets.contains(board.getCell(5, 16)));	
	}

	@Test
	// test to make sure occupied locations do not cause problems
	public void testTargetsOccupied() {
		// test a roll of 4 blocked 2 down
		board.getCell(15, 7).setOccupied(true);
		board.calcTargets(board.getCell(13, 7), 4);
		board.getCell(15, 7).setOccupied(false);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(13, targets.size());
		assertTrue(targets.contains(board.getCell(14, 2)));
		assertTrue(targets.contains(board.getCell(15, 9)));
		assertTrue(targets.contains(board.getCell(11, 5)));	
		assertFalse( targets.contains( board.getCell(15, 7))) ;
		assertFalse( targets.contains( board.getCell(17, 7))) ;

		// we want to make sure we can get into a room, even if flagged as occupied
		board.getCell(12, 20).setOccupied(true);
		board.getCell(8, 18).setOccupied(true);
		board.calcTargets(board.getCell(8, 17), 1);
		board.getCell(12, 20).setOccupied(false);
		board.getCell(8, 18).setOccupied(false);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(7, 17)));	
		assertTrue(targets.contains(board.getCell(8, 16)));	
		assertTrue(targets.contains(board.getCell(12, 20)));	

		// check leaving a room with a blocked doorway
		board.getCell(12, 15).setOccupied(true);
		board.calcTargets(board.getCell(12, 20), 3);
		board.getCell(12, 15).setOccupied(false);
		targets= board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCell(6, 17)));
		assertTrue(targets.contains(board.getCell(8, 19)));	
		assertTrue(targets.contains(board.getCell(8, 15)));

	}

	private static <T> void assertSetContents(Set<T> set, int size ,T... items) {
		assertEquals(size, set.size());
		for (var item : items) {
			assertTrue(set.contains(item));
		}
	}

	private static <T> void assertSetContents(Set<T> set, T... items) {
		assertSetContents(set, items.length, items);
	}

}
