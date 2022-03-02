package experiment;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Kai Page
 * @author Kelsi Wood
 */
public class TestBoard {
	private TestBoardCell[][] grid;
	private Set<TestBoardCell> targets;

	private static final int COLS = 4, ROWS = 4;

	public TestBoard() {
		grid = new TestBoardCell[ROWS][COLS];
		for (var i = 0; i < ROWS; i++) {
			for (var j = 0; j < COLS; j++) {
				grid[i][j] = new TestBoardCell(i, j);
			}
		}
		for (var i = 0; i < ROWS; i++) {
			for (var j = 0; j < COLS; j++) {
				if (i > 0) {
					grid[i][j].addAdjacency(grid[i - 1][j]);
				}
				if (i < ROWS - 1) {
					grid[i][j].addAdjacency(grid[i + 1][j]);
				}
				if (j > 0) {
					grid[i][j].addAdjacency(grid[i][j - 1]);
				}
				if (j < COLS - 1) {
					grid[i][j].addAdjacency(grid[i][j + 1]);
				}
			}
		}
	}

	public void calcTargets(TestBoardCell startCell, int pathLength) {
		targets = new HashSet<>();
		var visited = new HashSet<TestBoardCell>();
		visited.add(startCell);
		calcTargets(startCell, pathLength, visited);
	}

	private void calcTargets(TestBoardCell startCell, int pathLength, Set<TestBoardCell> visited) {
		for (var cell : startCell.getAdjList()) {
			if (!(visited.contains(cell) || cell.isRoom() || cell.isOccupied())) {
				visited.add(cell);
				if (pathLength == 1) {
					targets.add(cell);
				} else {
					calcTargets(cell, pathLength - 1, visited);
				}
				visited.remove(cell);
			}
		}
	}

	public Set<TestBoardCell> getTargets() {
		return targets;
	}

	public TestBoardCell getCell(int row, int col) {
		return grid[row][col];
	}
}
