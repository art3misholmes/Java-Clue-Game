package clueGame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Board {
	private ArrayList<ArrayList<BoardCell>> grid;
	private Set<BoardCell> targets;
	private String layoutFile, setupFile;
	private Map<Character, Room> rooms;
	private int rows, cols;

	private static Board instance = new Board();

	private Board() {
	}

	public void initialize() {
		try {
			loadSetupConfig();
			loadLayoutConfig();
		} catch (BadConfigFormatException e) {
			throw new RuntimeException(e);
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

	public void setConfigFiles(String layout, String setup) {
		layoutFile = layout;
		setupFile = setup;
	}

	public Room getRoom(char c) {
		return rooms.get(c);
	}

	public int getNumRows() {
		return rows;
	}

	public int getNumColumns() {
		return cols;
	}

	public Room getRoom(BoardCell cell) {
		return null;
	}

	public void loadSetupConfig() throws BadConfigFormatException {
		rooms = new HashMap<>();
		var lineNumber = 0;
		try (var scanner = new Scanner(new FileInputStream(setupFile))) {
			while (scanner.hasNextLine()) {
				var line = scanner.nextLine();
				lineNumber++;
				if (line.startsWith("//") || line.isBlank()) {
					continue;
				}
				var split = line.split(", ");
				
				// check for exceptional input
				if (split.length != 3) {
					throw new BadConfigFormatException(String.format("line %d of %s should have 3 entries", lineNumber, setupFile));
				}
				if (!split[0].equals("Room") && !split[0].equals("Space")) {
					throw new BadConfigFormatException(String.format("line %d of %s has bad room type %s", lineNumber, setupFile, split[0]));
				}
				if (split[2].length() != 1) {
					throw new BadConfigFormatException(String.format("line %d of %s has bad room character %s", lineNumber, setupFile, split[2]));
				}
				if (rooms.containsKey(split[2].charAt(0))) {
					throw new BadConfigFormatException(String.format("line %d of %s contains duplicate room character %s", lineNumber, setupFile, split[2]));
				}
				
				var room = new Room(split[1], split[0] == "Space");
				rooms.put(split[2].charAt(0), room);
			}
		} catch (FileNotFoundException e) {
			throw new BadConfigFormatException(e.toString());
		}
	}

	public void loadLayoutConfig() throws BadConfigFormatException {
		
		String line = "";
		var rowNumber = 0;
		 grid = new ArrayList<>();
		
		try (var scanner = new Scanner(new FileInputStream(layoutFile))) {
			while (scanner.hasNextLine()) {
				line = scanner.nextLine();
				
				var split = line.split(", ");
				
				ArrayList<BoardCell> row = new ArrayList<>();
				
				if(!grid.isEmpty()){
					if(split.length != grid.get(0).size()) {
						throw new BadConfigFormatException("Not the right length.");
					}
				}
				
				for(int i=0; i<split.length; i++) {
					BoardCell temp = new BoardCell(rowNumber, i);
					
					row.add(temp);
				}
				
						
				rowNumber++;
			}
			
			
			
		}catch(FileNotFoundException e) {
			throw new BadConfigFormatException(e.toString());
		}
		
		
//		grid = new BoardCell[ROWS][COLS];
//		for (var i = 0; i < ROWS; i++) {
//			for (var j = 0; j < COLS; j++) {
//				grid[i][j] = new BoardCell(i, j);
//			}
//		}
//		for (var i = 0; i < ROWS; i++) {
//			for (var j = 0; j < COLS; j++) {
//				if (i > 0) {
//					grid[i][j].addAdjacency(grid[i - 1][j]);
//				}
//				if (i < ROWS - 1) {
//					grid[i][j].addAdjacency(grid[i + 1][j]);
//				}
//				if (j > 0) {
//					grid[i][j].addAdjacency(grid[i][j - 1]);
//				}
//				if (j < COLS - 1) {
//					grid[i][j].addAdjacency(grid[i][j + 1]);
//				}
//			}
//		}
	}

}
