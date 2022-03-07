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
	private Map<BoardCell, Room> cellRooms;
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
		return grid.get(row).get(col);
	}

	public static Board getInstance() {
		return instance;
	}

	public void setConfigFiles(String layout, String setup) {
		layoutFile = "data/" + layout;
		setupFile = "data/" + setup;
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
		return cellRooms.get(cell);
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
					throw new BadConfigFormatException(
							String.format("line %d of %s should have 3 entries", lineNumber, setupFile));
				}
				if (!split[0].equals("Room") && !split[0].equals("Space")) {
					throw new BadConfigFormatException(
							String.format("line %d of %s has bad room type %s", lineNumber, setupFile, split[0]));
				}
				if (split[2].length() != 1) {
					throw new BadConfigFormatException(
							String.format("line %d of %s has bad room character %s", lineNumber, setupFile, split[2]));
				}
				if (rooms.containsKey(split[2].charAt(0))) {
					throw new BadConfigFormatException(String.format(
							"line %d of %s contains duplicate room character %s", lineNumber, setupFile, split[2]));
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
		cellRooms = new HashMap<>();

		try (var scanner = new Scanner(new FileInputStream(layoutFile))) {
			while (scanner.hasNextLine()) {
				line = scanner.nextLine();

				var split = line.split(",");

				ArrayList<BoardCell> row = new ArrayList<>();

				if (!grid.isEmpty()) {
					if (split.length != grid.get(0).size()) {
						throw new BadConfigFormatException("Not the right length.");
					}
				}

				for (int i = 0; i < split.length; i++) {
					BoardCell cell = new BoardCell(rowNumber, i);

					// check for exceptional cells
					if (split[i].length() < 1 || split[i].length() > 2 || !rooms.containsKey(split[i].charAt(0))) {
						throw new BadConfigFormatException(String.format("Bad cell %s", split[i]));
					}

					var room = rooms.get(split[i].charAt(0));
					cellRooms.put(cell, room);
					if (split[i].length() == 2) {
						// with this fancy new syntax from java 17,
						// you don't have to write break at the end of each case
						switch (split[i].charAt(1)) {
						case '^' -> cell.setDoorDirection(DoorDirection.UP);
						case '<' -> cell.setDoorDirection(DoorDirection.LEFT);
						case '>' -> cell.setDoorDirection(DoorDirection.RIGHT);
						case 'v' -> cell.setDoorDirection(DoorDirection.DOWN);
						case '#' -> {
							if (room.getLabelCell() != null) {
								throw new BadConfigFormatException(
										String.format("Room %s has multiple labels", room.getName()));
							}
							cell.setLabel(true);
							room.setLabelCell(cell);
						}
						case '*' -> {
							if (room.getCenterCell() != null) {
								throw new BadConfigFormatException(
										String.format("Room %s has multiple centers", room.getName()));
							}
							cell.setRoomCenter(true);
							room.setCenterCell(cell);
						}
						default -> {
							if (rooms.containsKey(split[i].charAt(1))) {
								cell.setSecretPassage(split[i].charAt(1));
							} else {
								throw new BadConfigFormatException(String.format("Bad cell %s", split[i]));
							}
						}
						}
					}
					row.add(cell);
				}
				grid.add(row);
				rowNumber++;
			}

		} catch (FileNotFoundException e) {
			throw new BadConfigFormatException(e.toString());
		}
		
		rows = grid.size();
		cols = grid.get(0).size();

		for (var i = 0; i < rows; i++) {
			for (var j = 0; j < cols; j++) {
				if (i > 0) {
					grid.get(i).get(j).addAdjacency(grid.get(i - 1).get(j));
				}
				if (i < rows - 1) {
					grid.get(i).get(j).addAdjacency(grid.get(i + 1).get(j));
				}
				if (j > 0) {
					grid.get(i).get(j).addAdjacency(grid.get(i).get(j - 1));
				}
				if (j < cols - 1) {
					grid.get(i).get(j).addAdjacency(grid.get(i).get(j + 1));
				}
			}
		}
	}

}
