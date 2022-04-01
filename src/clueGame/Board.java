package clueGame;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This is the actual board for our clue game.
 * 
 * @author Kelsi Wood
 * @author Kai Page
 */

public class Board {
	private ArrayList<ArrayList<BoardCell>> grid;
	private String layoutFile, setupFile;
	private Map<Character, Room> rooms; // list of corresponding char to each room
	private Map<BoardCell, Room> cellRooms; // the cells that make up a room
	private int rows, cols;
	@Deprecated()
	private Set<BoardCell> targets;

	private CardCollection deck;
	private Solution solution;

	private HumanPlayer humanPlayer;
	private ArrayList<ComputerPlayer> computerPlayers;

	private static Board instance = new Board(); // singleton

	// part of singleton
	private Board() {
	}

	// sets up the board
	public void initialize() {
		try {
			loadSetupConfig();
			loadLayoutConfig();
			deal();
		} catch (BadConfigFormatException e) {
			throw new RuntimeException(e);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Randomly distribute the cards between the players and solution
	 */
	public void deal() {
		// the 306 configs do not have players, abort if that is the case
		if (humanPlayer == null) {
			return;
		}
		
		var players = allPlayers();
		var cardStack = deck.getCards().stream().collect(Collectors.toList());
		Collections.shuffle(cardStack);
		Card room = null, person = null, weapon = null;
		var dealToNext = 0;

		for (int i = 0; i < cardStack.size(); i++) {
			var card = cardStack.get(i);

			// switch expression: yield true if the card should be dealt to a player
			if (switch (card.type()) {
			case ROOM -> {
				if (room == null) {
					room = card;
					yield false;
				}
				yield true;
			}
			case PERSON -> {
				if (person == null) {
					person = card;
					yield false;
				}
				yield true;
			}
			case WEAPON -> {
				if (weapon == null) {
					weapon = card;
					yield false;
				}
				yield true;
			}
			}) {
				// card was not part of the solution, deal it to a player
				players.get(dealToNext).updateHand(card);
				dealToNext = (dealToNext + 1) % players.size();
			}
		}
		
		solution = new Solution(room, person, weapon);
	}

	/**
	 * @see {@link getTargets(BoardCell, pathLength)}
	 */
	@Deprecated()
	public void calcTargets(BoardCell startCell, int pathLength) {
		targets = new HashSet<>();
		var visited = new HashSet<BoardCell>();
		calcTargets(startCell, pathLength, targets, visited);
	}

	/**
	 * Compute and return the accessible cells with a given roll
	 * 
	 * @param startCell  the starting position
	 * @param pathLength the number rolled
	 * @return a set of cells the player can land on
	 */
	public Set<BoardCell> getTargets(BoardCell startCell, int pathLength) {
		var targets = new HashSet<BoardCell>();
		var visited = new HashSet<BoardCell>();
		calcTargets(startCell, pathLength, targets, visited);
		return targets;
	}

	private void calcTargets(BoardCell startCell, int pathLength, Set<BoardCell> targets, Set<BoardCell> visited) {
		visited.add(startCell);
		for (var cell : startCell.getAdjList()) {
			if (!(visited.contains(cell) || (cell.isOccupied() && !cell.isRoomCenter()))) {
				if (pathLength == 1 || cell.isRoom()) {
					targets.add(cell);
				} else {
					calcTargets(cell, pathLength - 1, targets, visited);
				}
			}
		}
		visited.remove(startCell);
	}

	// loads the configuration of the board
	public void loadSetupConfig() throws BadConfigFormatException, FileNotFoundException {
		rooms = new HashMap<>();
		deck = new CardCollection();
		humanPlayer = null;
		computerPlayers = new ArrayList<>();

		var lineNumber = 0;
		try (var scanner = new Scanner(new FileInputStream(setupFile))) {
			while (scanner.hasNextLine()) {
				var line = scanner.nextLine();
				lineNumber++;
				if (line.startsWith("//") || line.isBlank()) {
					continue;
				}
				var split = line.split(", ");

				switch (split[0]) {
				case "Room", "Space" -> {
					// start by checking for exceptional input
					if (split.length != 3) {
						throw new BadConfigFormatException(
								String.format("line %d of %s should have 3 entries", lineNumber, setupFile));
					}
					if (split[2].length() != 1) {
						throw new BadConfigFormatException(String.format("line %d of %s has bad room character %s",
								lineNumber, setupFile, split[2]));
					}
					if (rooms.containsKey(split[2].charAt(0))) {
						throw new BadConfigFormatException(String.format(
								"line %d of %s contains duplicate room character %s", lineNumber, setupFile, split[2]));
					}

					var room = new Room(split[1], split[0].equals("Space"));
					rooms.put(split[2].charAt(0), room);
					deck.addCard(new Card(Card.Type.ROOM, split[1]));
				}
				case "Player" -> {
					var name = split[1];
					var color = new Color(Integer.parseInt(split[2].substring(0, 2), 16),
							Integer.parseInt(split[2].substring(2, 4), 16),
							Integer.parseInt(split[2].substring(4, 6), 16));
					var row = Integer.parseInt(split[3]);
					var column = Integer.parseInt(split[4]);

					deck.addCard(new Card(Card.Type.PERSON, name));
					if (humanPlayer == null) {
						humanPlayer = new HumanPlayer(name, color, row, column);
					} else {
						computerPlayers.add(new ComputerPlayer(name, color, row, column));
					}
				}
				case "Weapon" -> {
					deck.addCard(new Card(Card.Type.WEAPON, split[1]));
				}
				default -> {
					throw new BadConfigFormatException(String.format("unexpected config entry type %s", split[0]));
				}
				}

			}
		}
	}

	// loads the layout of the board
	public void loadLayoutConfig() throws BadConfigFormatException, FileNotFoundException {

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
					cell.setRoom(!room.isNormalSpace());

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
		}

		rows = grid.size();
		cols = grid.get(0).size();

		for (var i = 0; i < rows; i++) {
			for (var j = 0; j < cols; j++) {
				var cell = grid.get(i).get(j);
				// only process walkway tiles directly
				if (cellIsWalkway(cell)) {
					// adjacencies to other walkway tiles
					if (i > 0) {
						addAdjacencyIfWalkway(cell, grid.get(i - 1).get(j));
					}
					if (i < rows - 1) {
						addAdjacencyIfWalkway(cell, grid.get(i + 1).get(j));
					}
					if (j > 0) {
						addAdjacencyIfWalkway(cell, grid.get(i).get(j - 1));
					}
					if (j < cols - 1) {
						addAdjacencyIfWalkway(cell, grid.get(i).get(j + 1));
					}

					// adjacencies into/out of rooms
					if (cell.getDoorDirection() != null) {
						switch (cell.getDoorDirection()) {
						case UP -> {
							var room = getRoom(grid.get(i - 1).get(j));
							cell.addAdjacency(room.getCenterCell());
							room.getCenterCell().addAdjacency(cell);
						}
						case DOWN -> {
							var room = getRoom(grid.get(i + 1).get(j));
							cell.addAdjacency(room.getCenterCell());
							room.getCenterCell().addAdjacency(cell);
						}
						case LEFT -> {
							var room = getRoom(grid.get(i).get(j - 1));
							cell.addAdjacency(room.getCenterCell());
							room.getCenterCell().addAdjacency(cell);
						}
						case RIGHT -> {
							var room = getRoom(grid.get(i - 1).get(j + 1));
							cell.addAdjacency(room.getCenterCell());
							room.getCenterCell().addAdjacency(cell);
						}
						default -> {
						}
						}
					}
				}

				// secret door
				if (cell.getSecretPassage() != 0) {
					var from = cellRooms.get(cell);

					var to = rooms.get(cell.getSecretPassage());

					from.getCenterCell().addAdjacency(to.getCenterCell());
				}

			}
		}
	}

	private boolean cellIsWalkway(BoardCell cell) {
		return getRoom(cell).getName().equals("Walkway");
	}

	private void addAdjacencyIfWalkway(BoardCell cell, BoardCell maybeAdjacent) {
		if (cellIsWalkway(maybeAdjacent)) {
			cell.addAdjacency(maybeAdjacent);
		}
	}

	// gets adjacent cells to current cell
	public Set<BoardCell> getAdjList(int row, int column) {
		// call cell's adjacency
		return grid.get(row).get(column).getAdjList();
	}

	/**
	 * @return all the players
	 */
	public ArrayList<Player> allPlayers() {
		var ret = new ArrayList<Player>(computerPlayers.size() + 1);
		ret.add(humanPlayer);
		for (var cpu : computerPlayers) {
			ret.add(cpu);
		}
		return ret;
	}

	// getters

	/**
	 * @see {@link getTargets(BoardCell, pathLength)}
	 */
	@Deprecated()
	public Set<BoardCell> getTargets() {
		return targets;
	}

	public BoardCell getCell(int row, int col) {
		return grid.get(row).get(col);
	}

	public static Board getInstance() {
		return instance;
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

	public HumanPlayer getHumanPlayer() {
		return humanPlayer;
	}

	public ArrayList<ComputerPlayer> getComputerPlayers() {
		return computerPlayers;
	}

	public Solution getSolution() {
		return solution;
	}

	// setters
	public void setConfigFiles(String layout, String setup) {
		layoutFile = "data/" + layout;
		setupFile = "data/" + setup;
	}

}
