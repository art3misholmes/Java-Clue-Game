package clueGame;

import java.util.HashSet;
import java.util.Set;

public class Board {
	private BoardCell[][] grid;
	private Set<BoardCell> targets;

	private static final int COLS = 4, ROWS = 4;
	private static Board instance = new Board();
	
	private Board() {}
	
	public void initialize() {
		grid = new BoardCell[ROWS][COLS];
		for (var i = 0; i < ROWS; i++) {
			for (var j = 0; j < COLS; j++) {
				grid[i][j] = new BoardCell(i, j);
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

	public void calcTargets(BoardCell startCell, int pathLength) {
		targets = new HashSet<>();
		var visited = new HashSet<BoardCell>();
		visited.add(startCell);
		calcTargets(startCell, pathLength, visited);
	}

	private void calcTargets(BoardCell startCell, int pathLength, Set<BoardCell> visited) {
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

	public Set<BoardCell> getTargets() {
		return targets;
	}

	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}
	
	public static Board getInstance() {
		return instance;
	}

	public void setConfigFiles(String string, String string2) {
		// TODO Auto-generated method stub
		
	}

	public Room getRoom(char c) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getNumRows() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getNumColumns() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Room getRoom(BoardCell cell) {
		// TODO Auto-generated method stub
		return null;
	}

	public void loadSetupConfig() throws BadConfigFormatException {
		// TODO Auto-generated method stub
		
	}

	public void loadLayoutConfig() throws BadConfigFormatException {
		// TODO Auto-generated method stub
		
	}
	
	

}
