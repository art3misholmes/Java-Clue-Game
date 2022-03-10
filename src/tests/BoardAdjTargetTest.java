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

	// We make the Board static because we can load it one time and
	// then do all the tests.
	private static Board board;

	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load config files
		board.initialize();
	}

	// Ensure that player does not move around within room
	// These cells are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testAdjacenciesRoomsAndDoors() {
		// from a room to a door
		Set<BoardCell> testList = board.getAdjList(10, 1);
		assertSetContents(testList, board.getCell(10, 3));

		// from a room to a door where there is also a secret passage
		testList = board.getAdjList(23, 20);
		assertSetContents(testList, board.getCell(23, 17), board.getCell(20, 19), board.getCell(2, 20));

		// from a room to a door
		testList = board.getAdjList(19, 3);
		assertSetContents(testList, board.getCell(23, 2), board.getCell(19, 2), board.getCell(18, 3),
				board.getCell(19, 4));

		// within a room
		testList = board.getAdjList(22, 3);
		assertTrue(testList.isEmpty());
	}

	// Test a variety of walkway scenarios
	// These tests are Orange on the planning spreadsheet
	@Test
	public void testAdjacencyWalkways() {
		Set<BoardCell> testList = board.getAdjList(2, 3);
		assertSetContents(testList, board.getCell(1, 3), board.getCell(2, 4));

		testList = board.getAdjList(2, 18);
		assertSetContents(testList, board.getCell(1, 18), board.getCell(2, 17), board.getCell(3, 18));

		testList = board.getAdjList(9, 7);
		assertSetContents(testList, board.getCell(8, 7), board.getCell(9, 6), board.getCell(9, 8),
				board.getCell(10, 7));

		testList = board.getAdjList(13, 17);
		assertSetContents(testList, board.getCell(12, 17), board.getCell(13, 18));
	}

	// Tests out of room center, 1, 3 and 4
	// These are LIGHT PINK on the planning spreadsheet
	@Test
	public void testTargetsInTestKitchen() {
		// test a roll of 1
		board.calcTargets(board.getCell(9, 20), 1);
		Set<BoardCell> targets = board.getTargets();
		assertSetContents(targets, board.getCell(10, 18), board.getCell(5, 20));

		// test a roll of 3
		board.calcTargets(board.getCell(9, 20), 3);
		targets = board.getTargets();
		assertSetContents(targets, 10, board.getCell(12, 18), board.getCell(5, 22), board.getCell(2, 20));

		// test a roll of 4
		board.calcTargets(board.getCell(9, 20), 4);
		targets = board.getTargets();
		assertSetContents(targets, 18, board.getCell(11, 16), board.getCell(4, 20), board.getCell(2, 20));
	}

	// Tests using or not using the secret passage when rolling a 1 or 2
	// These are LIGHT PINK on the planning spreadsheet
	@Test
	public void testTargetsInRecRoom() {
		// test a roll of 1
		board.calcTargets(board.getCell(2, 20), 1);
		Set<BoardCell> targets = board.getTargets();
		assertSetContents(targets, board.getCell(4, 20), board.getCell(23, 20));

		// test a roll of 2
		board.calcTargets(board.getCell(2, 20), 2);
		targets = board.getTargets();
		assertSetContents(targets, board.getCell(4, 19), board.getCell(5, 20), board.getCell(4, 21),
				board.getCell(23, 20));
	}

	@Test
	public void testTargetsInWalkWay() {
		// test a roll of 1
		board.calcTargets(board.getCell(10, 4), 1);
		Set<BoardCell> targets = board.getTargets();
		assertSetContents(targets, board.getCell(9, 4), board.getCell(10, 3), board.getCell(10, 5),
				board.getCell(11, 4));

		// test a roll of 3
		board.calcTargets(board.getCell(10, 4), 3);
		targets = board.getTargets();
		assertSetContents(targets, 14, board.getCell(7, 4), board.getCell(12, 5), board.getCell(10, 1));

		// test a roll of 4
		board.calcTargets(board.getCell(10, 4), 4);
		targets = board.getTargets();
		assertSetContents(targets, 18, board.getCell(6, 4), board.getCell(12, 6), board.getCell(10, 1));
	}

	// Tests rolling a 1 at the board edge
	// These are LIGHT PINK on the planning spreadsheet
	@Test
	public void testTargetsAtBoardEdge() {
		// top edge
		board.calcTargets(board.getCell(0, 3), 1);
		Set<BoardCell> targets = board.getTargets();
		assertSetContents(targets, board.getCell(0, 4), board.getCell(1, 3));

		// left edge
		board.calcTargets(board.getCell(13, 0), 1);
		targets = board.getTargets();
		assertSetContents(targets, board.getCell(13, 1), board.getCell(14, 0));

		// right edge
		board.calcTargets(board.getCell(17, 22), 1);
		targets = board.getTargets();
		assertSetContents(targets, board.getCell(16, 22), board.getCell(17, 21));

		// bottom edge
		board.calcTargets(board.getCell(25, 12), 1);
		targets = board.getTargets();
		assertSetContents(targets, board.getCell(24, 12), board.getCell(25, 13));
	}

	@Test
	// test to make sure occupied locations do not cause problems
	public void testTargetsOccupied() {
		// test a roll of 1 blocked adjacently
		board.getCell(11, 3).setOccupied(true);
		board.calcTargets(board.getCell(12, 3), 1);
		board.getCell(11, 3).setOccupied(false);
		Set<BoardCell> targets = board.getTargets();
		assertSetContents(targets, board.getCell(13, 3), board.getCell(12, 4));

		board.getCell(9, 17).setOccupied(true);
		board.calcTargets(board.getCell(9, 16), 1);
		board.getCell(9, 17).setOccupied(false);
		targets = board.getTargets();
		assertSetContents(targets, board.getCell(9, 15), board.getCell(8, 16), board.getCell(10, 16));

		// we want to make sure we can get into a room, even if flagged as occupied
		board.getCell(2, 1).setOccupied(true);
		board.calcTargets(board.getCell(6, 2), 1);
		board.getCell(2, 1).setOccupied(false);
		targets = board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(2, 1)));

		// check leaving a room with a blocked doorway
		board.getCell(23, 17).setOccupied(true);
		board.calcTargets(board.getCell(23, 20), 1);
		board.getCell(23, 17).setOccupied(false);
		targets = board.getTargets();
		assertFalse(targets.contains(board.getCell(23, 17)));
	}

	private static <T> void assertSetContents(Set<T> set, int size, T... items) {
		assertEquals(size, set.size());
		for (var item : items) {
			assertTrue(set.contains(item));
		}
	}

	private static <T> void assertSetContents(Set<T> set, T... items) {
		assertSetContents(set, items.length, items);
	}

}
