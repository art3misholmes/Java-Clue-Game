package tests;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.jupiter.api.*;

import experiment.*;

/**
 * @author Kai Page
 * @author Kelsi Wood
 */
public class BoardTestsExp {
	private TestBoard board;

	@BeforeEach
	public void setupTestBoard() {
		board = new TestBoard();
	}

	@Test
	public void testAdjacencyTopLeft() {
		var cell = board.getCell(0, 0);
		var cellAdj = cell.getAdjList();
		assertSetContents(cellAdj, board.getCell(1, 0), board.getCell(0, 1));

	}

	@Test
	public void testAdjacencyBottomRight() {
		var cell = board.getCell(3, 3);
		var cellAdj = cell.getAdjList();
		assertSetContents(cellAdj, board.getCell(2, 3), board.getCell(3, 2));
	}

	@Test
	public void testAdjacencyRightEdge() {
		var cell = board.getCell(1, 3);
		var cellAdj = cell.getAdjList();
		assertSetContents(cellAdj, board.getCell(0, 3), board.getCell(2, 3), board.getCell(1, 2));
	}

	@Test
	public void testAdjacencyLeftEdge() {
		var cell = board.getCell(1, 0);
		var cellAdj = cell.getAdjList();
		assertSetContents(cellAdj, board.getCell(0, 0), board.getCell(2, 0), board.getCell(1, 1));
	}

	@Test
	public void testTargetingEmpty() {
		var cell = board.getCell(0, 0);
		board.calcTargets(cell, 3);
		var target = board.getTargets();
		assertSetContents(target, board.getCell(3, 0), board.getCell(2, 1), board.getCell(0, 1), board.getCell(1, 2),
				board.getCell(0, 3), board.getCell(1, 0));
	}

	@Test
	public void testTargetingOccupied() {
		board.getCell(1, 1).setOccupied(true);
		var cell = board.getCell(1, 0);
		board.calcTargets(cell, 2);
		var target = board.getTargets();
		assertSetContents(target, board.getCell(0, 1), board.getCell(2, 1), board.getCell(3, 0));
	}

	@Test
	public void testTargetingRoom() {
		board.getCell(1, 1).setRoom(true);
		var cell = board.getCell(1, 0);
		board.calcTargets(cell, 2);
		var target = board.getTargets();
		assertSetContents(target, board.getCell(0, 1), board.getCell(2, 1), board.getCell(3, 0));
	}

	@Test
	public void testTargetIreleventConstraints() {
		var cell = board.getCell(0, 0);
		// y,x
		board.getCell(3, 3).setOccupied(true);
		
		board.calcTargets(cell, 1);
		var target = board.getTargets();
		assertSetContents(target, board.getCell(0, 1), board.getCell(1, 0));
	}

	@Test
	public void testTargetingCannotMove() {
		var cell = board.getCell(1, 1);
		// y,x
		board.getCell(0, 1).setOccupied(true);
		board.getCell(1, 0).setOccupied(true);
		board.getCell(1, 2).setOccupied(true);
		board.getCell(2, 1).setOccupied(true);
		
		board.calcTargets(cell, 6);
		var target = board.getTargets();
		assertSetContents(target);
	}

	private static <T> void assertSetContents(Set<T> set, T... items) {
		assertEquals(items.length, set.size());
		for (var item : items) {
			assertTrue(set.contains(item));
		}
	}
}
