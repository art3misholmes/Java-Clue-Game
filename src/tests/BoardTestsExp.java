package tests;

import static org.junit.Assert.*;

import org.junit.jupiter.api.*;

import experiment.*;

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
		assertTrue(cellAdj.contains(board.getCell(1, 0)));
		assertTrue(cellAdj.contains(board.getCell(0, 1)));
		assertEquals(2, cellAdj.size());
	}

	@Test
	public void testAdjacencyBottomRight() {
		var cell = board.getCell(3, 3);
		var cellAdj = cell.getAdjList();
		assertTrue(cellAdj.contains(board.getCell(2, 3)));
		assertTrue(cellAdj.contains(board.getCell(3, 2)));
		assertEquals(2, cellAdj.size());
	}

	@Test
	public void testAdjacencyRightEdge() {
		var cell = board.getCell(1, 3);
		var cellAdj = cell.getAdjList();
		assertTrue(cellAdj.contains(board.getCell(0, 3)));
		assertTrue(cellAdj.contains(board.getCell(2, 3)));
		assertTrue(cellAdj.contains(board.getCell(1, 2)));
		assertEquals(3, cellAdj.size());
	}

	@Test
	public void testAdjacencyLeftEdge() {
		var cell = board.getCell(1, 0);
		var cellAdj = cell.getAdjList();
		assertTrue(cellAdj.contains(board.getCell(0, 0)));
		assertTrue(cellAdj.contains(board.getCell(2, 0)));
		assertTrue(cellAdj.contains(board.getCell(1, 1)));
		assertEquals(3, cellAdj.size());
	}

	@Test
	public void testTargetingEmpty() {
		var cell = board.getCell(0, 0);
		board.calcTargets(cell,3);
		var target = board.getTargets();
		assertEquals(6, target.size());
		assertTrue(target.contains(board.getCell(3, 0)));
		assertTrue(target.contains(board.getCell(2, 1)));
		assertTrue(target.contains(board.getCell(0, 1)));
		assertTrue(target.contains(board.getCell(1, 2)));
		assertTrue(target.contains(board.getCell(0, 3)));
		assertTrue(target.contains(board.getCell(1, 0)));
	}

	@Test
	public void testTargetingOccupied() {
		board.getCell(1,1).setOccupied(true);
		var cell = board.getCell(1, 0);
		board.calcTargets(cell,2);
		var target = board.getTargets();
		assertEquals(3, target.size());
		assertTrue(target.contains(board.getCell(0, 1)));
		assertTrue(target.contains(board.getCell(2, 1)));
		assertTrue(target.contains(board.getCell(3, 0)));
	}

	@Test
	public void testTargetingRoom() {

	}

	@Test
	public void testTargetUnused() {

	}

	@Test
	public void testTargetingCannotMove() {

	}
}
