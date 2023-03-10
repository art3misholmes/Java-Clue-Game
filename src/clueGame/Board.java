package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * This is the actual board for our clue game.
 * 
 * @author Kelsi Wood
 * @author Kai Page
 */

public class Board extends JPanel {
	private static final long serialVersionUID = 1L; // bluh

	private ArrayList<ArrayList<BoardCell>> grid; // all cells on board
	private String layoutFile, setupFile;
	private Map<Character, Room> rooms; // list of corresponding char to each room
	private Map<BoardCell, Room> cellRooms; // the cells that make up a room
	private int rows, cols;
	@Deprecated()
	private Set<BoardCell> targets; // list of potential targets

	/**
	 * All the cards that exist, even after they have been dealt
	 */
	private CardCollection deck;
	private Solution solution;
	/**
	 * Maps the player cards to the players they represent
	 */
	private Map<Card, Player> cardPlayers;

	private HumanPlayer humanPlayer;
	private ArrayList<ComputerPlayer> computerPlayers;

	// 0 is always the human player
	private static final int HUMAN_PLAYER_TURN_INDEX = 0;
	private int currentTurnIndex = 0;

	/**
	 * targets for the human player to move if it is not null
	 */
	private Set<BoardCell> movementTargets;

	private GameControlPanel controlPanel; // displays current turn info
	private KnownCardsPanel cardsPanel; // displays known cards

	// getters
	public GameControlPanel getControlPanel() {
		return controlPanel;
	}

	// setter
	public void setControlPanel(GameControlPanel controlPanel) {
		this.controlPanel = controlPanel;
	}

	// getter
	public KnownCardsPanel getCardsPanel() {
		return cardsPanel;
	}

	// setter
	public void setCardsPanel(KnownCardsPanel cardsPanel) {
		this.cardsPanel = cardsPanel;
	}

	private static Board instance = new Board(); // singleton

	private Image grass;
	// part of singleton
	private Board() {
		try {
			grass = ImageIO.read(getClass().getResource("data/space.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		addMouseListener(new BoardMouseListener());
	}

	// mouse events handled here
	private class BoardMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// move when movementTargets isn't null

			if (movementTargets != null) {

				// subtra y off from click/hight
				var cellM = CellMetrics.calculateMetrics(getWidth(), getHeight(), rows, cols);

				var clickRow = (e.getY() - cellM.yOffset()) / cellM.cellHeight();
				// same for x
				var clickColum = (e.getX() - cellM.xOffset()) / cellM.cellWidth();

				var targetCell = getCell(clickRow, clickColum);

				if (targetCell.isRoom()) {
					targetCell = cellRooms.get(targetCell).getCenterCell();
				}

				// if spot at clickRow x clickColum is contained within movment targets
				if (movementTargets.contains(targetCell)) {
					// move player
					movePlayer(humanPlayer, targetCell.getRow(), targetCell.getColumn());
					movementTargets = null;
					repaint();

					// is new spot room?
					if (targetCell.isRoom()) {
						new SuggestionModal(cellRooms.get(targetCell).getCard(), deck).setVisible(true);
					}
					// hand sugestion
					// update result
				} else {
					JOptionPane.showMessageDialog(Board.getInstance(), "Not a valid target.");
				}

			}

		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

	}

	// sets up the board
	public void initialize() {
		try {
			loadSetupConfig();
			loadLayoutConfig();
		} catch (BadConfigFormatException e) {
			throw new RuntimeException(e);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

		// the 306 configs do not have players, abort if that is the case
		if (humanPlayer == null) {
			return;
		}

		for (var player : allPlayers()) {
			getCell(player.getRow(), player.getColumn()).setOccupied(true);
		}

		deal();

		// the tests do not set a control panel, don't bother with turns in tests
		if (controlPanel != null) {
			startHumanTurn();
		}
	}
	
	// for testing accusations
	private static final boolean DEBUG_EVERYONE_SEES_EVERYONES_HAND = false;

	/**
	 * Randomly distribute the cards between the players and solution
	 */
	public void deal() {
		// the 306 configs do not have players, abort if that is the case
		if (humanPlayer == null) {
			return;
		}

		var players = allPlayers();
		for (var p : players) {
			p.getHand().clear();
		}

		var cardStack = deck.getCards().stream().collect(Collectors.toList()); // cards to be dealt
		Collections.shuffle(cardStack); // randomizes cards

		var tentativeSolution = new CardCollection();
		var dealToNext = 0;

		for (int i = 0; i < cardStack.size(); i++) {
			var card = cardStack.get(i);

			if (tentativeSolution.getCardsOfType(card.type()).isEmpty()) {
				tentativeSolution.addCard(card);
			} else {
				// card was not part of the solution, deal it to a player
				players.get(dealToNext).updateHand(card);
				if (DEBUG_EVERYONE_SEES_EVERYONES_HAND) {
					for (var p : players) {
						p.updateSeen(card);
					}
					humanPlayer.updatePresentedBy(card, players.get(dealToNext));
				}
				dealToNext = (dealToNext + 1) % players.size();
			}
		}

		solution = new Solution(tentativeSolution.getRoomCards().first(), tentativeSolution.getPersonCards().first(),
				tentativeSolution.getWeaponCards().first());
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

	// implements calculating targets
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

	// checks to see if accusation is correct
	public boolean checkAccusation(Solution accusation) {
		return accusation.equals(solution);
	}

	// checks for refutation
	public Card handleSuggestion(Solution suggestion, Player suggestingPlayer) {
		// drag the person into the room - person may not exist in a testing environment
		if (cardPlayers.containsKey(suggestion.person())) {
			var draggee = cardPlayers.get(suggestion.person());
			if (draggee != suggestingPlayer) { // the instructors' version doesn't seem to have this check. you can keep dragging yourself into a room and sit there forever.
				movePlayer(draggee, suggestingPlayer.getRow(), suggestingPlayer.getColumn());
				draggee.setWasDragged(true);
			}
		}

		// update UI - controlPanel is null in a testing environment
		if (controlPanel != null) {
			controlPanel.setGuess(String.format("%s in %s with %s", suggestion.person().name(),
					suggestion.room().name(), suggestion.weapon().name()));
		}

		// figure out who can disprove it
		var players = allPlayers();
		var suggestingIndex = players.indexOf(suggestingPlayer);
		var numPlayers = players.size();
		for (var i = (suggestingIndex + 1) % numPlayers; i != suggestingIndex; i = (i + 1) % numPlayers) {
			var cardUsedToDis = players.get(i).disproveSuggestion(suggestion);
			if (cardUsedToDis != null) {
				suggestingPlayer.updateSeen(cardUsedToDis);
				if (controlPanel != null) {
					if (suggestingPlayer == humanPlayer) {
						controlPanel.setGuessResult(cardUsedToDis.name(), players.get(i));
						humanPlayer.updatePresentedBy(cardUsedToDis, players.get(i));
						cardsPanel.update();
					} else {
						controlPanel.setGuessResult("Disproven", players.get(i));
					}
				}
				return cardUsedToDis;
			}
		}

		if (controlPanel != null) {
			controlPanel.setGuessResult("Not disproven", null);
		}
		return null;
	}

	// loads the configuration of the board
	public void loadSetupConfig() throws BadConfigFormatException, FileNotFoundException {
		rooms = new HashMap<>();
		deck = new CardCollection();
		cardPlayers = new HashMap<>();
		humanPlayer = null;
		computerPlayers = new ArrayList<>();

		var lineNumber = 0;
		try (var scanner = new Scanner(/*new FileInputStream(setupFile)*/ getClass().getResourceAsStream(setupFile))) {
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
					if (split.length != 3 && split.length != 4) {
						throw new BadConfigFormatException(
								String.format("line %d of %s should have 3 or 4 entries", lineNumber, setupFile));
					}
					if (split[2].length() != 1) {
						throw new BadConfigFormatException(String.format("line %d of %s has bad room character %s",
								lineNumber, setupFile, split[2]));
					}
					if (rooms.containsKey(split[2].charAt(0))) {
						throw new BadConfigFormatException(String.format(
								"line %d of %s contains duplicate room character %s", lineNumber, setupFile, split[2]));
					}

					var isNormalSpace = split[0].equals("Space");
					var room = new Room(split[1], isNormalSpace);
					rooms.put(split[2].charAt(0), room);
					if (!isNormalSpace) {
						var card = new Card(Card.Type.ROOM, split[1]);
						deck.addCard(card);
						room.setCard(card);
					}

					// fourth item in our setup is width of the label
					if (split.length > 3) {
						room.setLabelWidth(Integer.parseInt(split[3]));
					}
				}
				case "Player" -> {
					var name = split[1];
					var color = new Color(Integer.parseInt(split[2].substring(0, 2), 16),
							Integer.parseInt(split[2].substring(2, 4), 16),
							Integer.parseInt(split[2].substring(4, 6), 16));
					var row = Integer.parseInt(split[3]);
					var column = Integer.parseInt(split[4]);

					var card = new Card(Card.Type.PERSON, name);
					deck.addCard(card);
					if (humanPlayer == null) {
						var me = new HumanPlayer(name, color, row, column);
						humanPlayer = me;
						cardPlayers.put(card, me);
					} else {
						var me = new ComputerPlayer(name, color, row, column);
						computerPlayers.add(me);
						cardPlayers.put(card, me);
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

		try (var scanner = new Scanner(/*new FileInputStream(layoutFile)*/ getClass().getResourceAsStream(layoutFile))) {
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

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// clear the screen
		if(grass != null) {
			g.drawImage(grass, 0, 0, getWidth(), getHeight(), null);
			
		}else {
			g.setColor(new Color(10, 172, 58));
			g.fillRect(0, 0, getWidth(), getHeight());
		}

		// pack up everything we've calculated to pass into drawing methods
		var metrics = CellMetrics.calculateMetrics(getWidth(), getHeight(), rows, cols);

		for (var row : grid) {
			for (var cell : row) {
				cell.draw(g, metrics, movementTargets != null && (movementTargets.contains(cell)
						|| cell.isRoom() && movementTargets.contains(cellRooms.get(cell).getCenterCell())));
			}
		}

		for (var room : rooms.values()) {
			if (!room.isNormalSpace()) {
				room.drawLabel(g, metrics);
			}
		}

		// figure out if any players are overlapping
		var players = allPlayers();
		var playerLocations = new HashMap<BoardCell, ArrayList<Player>>();
		for (var player : players) {
			var cell = getCell(player.getRow(), player.getColumn());
			if (playerLocations.containsKey(cell)) {
				playerLocations.get(cell).add(player);
			} else {
				var list = new ArrayList<Player>();
				list.add(player);
				playerLocations.put(cell, list);
			}
		}

		for (var player : players) {
			var offset = metrics.cellWidth()
					* playerLocations.get(getCell(player.getRow(), player.getColumn())).indexOf(player) / 4;
			player.draw(g, metrics, offset);
		}
	}

	private Random rand = new Random();

	public void nextButtonClicked() {
		if (movementTargets != null) {
			JOptionPane.showMessageDialog(this, "You need to move!");
		} else {
			currentTurnIndex = (currentTurnIndex + 1) % (computerPlayers.size() + 1);
			if (currentTurnIndex == HUMAN_PLAYER_TURN_INDEX) {
				startHumanTurn();
			} else {
				var roll = rand.nextInt(6) + 1;
				var currentPlayer = computerPlayers.get(currentTurnIndex - 1);

				// TODO maybe make an accusation
				var targets = getTargets(getCell(currentPlayer.getRow(), currentPlayer.getColumn()), roll);
				if (currentPlayer.isWasDragged()) {
					currentPlayer.setWasDragged(false);
					targets.add(getCell(currentPlayer.getRow(), currentPlayer.getColumn()));
				}
				var targetCell = currentPlayer.selectTarget(targets, cellRooms);
				movePlayer(currentPlayer, targetCell.getRow(), targetCell.getColumn());

				// TODO maybe make a suggestion after moving
				var compRoom = getCell(currentPlayer.getRow(), currentPlayer.getColumn()).isRoom();

				if (compRoom) {
					// make suggestion
					var compSugest = currentPlayer.createSuggestion(deck,
							cellRooms.get(getCell(currentPlayer.getRow(), currentPlayer.getColumn())).getCard());
					var compHand = handleSuggestion(compSugest, currentPlayer);

				}
				controlPanel.setTurn(currentPlayer, roll);
			}
			repaint();
		}
	}

	/**
	 * Rolls the die and assigns movement targets to the human player
	 */
	private void startHumanTurn() {
		var roll = rand.nextInt(6) + 1;
		movementTargets = getTargets(getCell(humanPlayer.getRow(), humanPlayer.getColumn()), roll);
		if (humanPlayer.isWasDragged()) {
			humanPlayer.setWasDragged(false);
			movementTargets.add(getCell(humanPlayer.getRow(), humanPlayer.getColumn()));
		}
		if (movementTargets.isEmpty()) {
			movementTargets = null;
		}
		controlPanel.setTurn(humanPlayer, roll);
	}

	private void movePlayer(Player p, int newRow, int newColumn) {
		getCell(p.getRow(), p.getColumn()).setOccupied(false);
		p.setRow(newRow);
		p.setColumn(newColumn);
		getCell(newRow, newColumn).setOccupied(true);
		repaint();
	}

	// getters

	/**
	 * @see {@link getTargets(BoardCell, pathLength)}
	 */
	@Deprecated()
	public Set<BoardCell> getTargets() {
		return targets;
	}

	public CardCollection getDeck() {
		return deck;
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

	public void handleHumanAccusation(Solution accusation) {
		if (checkAccusation(accusation)) {
			JOptionPane.showMessageDialog(this, "You did it! Congratulations");
		} else {
			JOptionPane.showMessageDialog(this, "Nope! It was " + solution);
		}
		System.exit(0);
	}

	public void accusationButtonClicked() {
		if (movementTargets != null) {
			new AccusationModal(deck).setVisible(true);
		} else {
			JOptionPane.showMessageDialog(this, "Wait for your turn");
		}
	}
}
