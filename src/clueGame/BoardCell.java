package clueGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

public class BoardCell {
	private int row, column;
	private Set<BoardCell> adjList = new HashSet<>();
	private boolean isRoom, isOccupied, isLabel, isRoomCenter;
	private DoorDirection doorDirection;
	private char secretPassage;

	public BoardCell(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public void addAdjacency(BoardCell cell) {
		adjList.add(cell);
	}

	private static final int DOOR_THICKNESS = 4;
	
	private static final Color WALKWAY_COLOR = new Color(186, 243, 192);
	private static final Color ROOM_COLOR = Color.DARK_GRAY;
	private static final Color DOOR_COLOR = Color.WHITE;
	private static final Color TARGET_COLOR = Color.CYAN;

	public void draw(Graphics g, CellMetrics m, boolean isTarget) {
		var board = Board.getInstance();
		var room = board.getRoom(this);

		if (room.isNormalSpace()) {
			switch (room.getName()) {
			case "Unused" -> {
				// don't need to draw over the background
			}
			case "Walkway" -> {
				g.setColor(isTarget ? TARGET_COLOR : WALKWAY_COLOR);
				g.fillRect(m.xOffset() + m.cellWidth() * column + 1, m.yOffset() + m.cellHeight() * row + 1, m.cellWidth() - 2,
						m.cellHeight() - 2);
			}
			}
		} else {
			// cell is part of a room
			g.setColor(isTarget ? TARGET_COLOR : ROOM_COLOR);
			g.fillRect(m.xOffset() + m.cellWidth() * column, m.yOffset() + m.cellHeight() * row, m.cellWidth(), m.cellHeight());

			// is there a door into this space?
			g.setColor(DOOR_COLOR);
			if (row > 0 && board.getCell(row - 1, column).doorDirection == DoorDirection.DOWN) {
				g.fillRect(m.xOffset() + m.cellWidth() * column, m.yOffset() + m.cellHeight() * row, m.cellWidth(), DOOR_THICKNESS);
			}
			if (row < board.getNumRows() - 1 && board.getCell(row + 1, column).doorDirection == DoorDirection.UP) {
				g.fillRect(m.xOffset() + m.cellWidth() * column, m.yOffset() + m.cellHeight() * row + m.cellHeight() - DOOR_THICKNESS,
						m.cellWidth(), DOOR_THICKNESS);
			}
			if (column > 0 && board.getCell(row, column - 1).doorDirection == DoorDirection.RIGHT) {
				g.fillRect(m.xOffset() + m.cellWidth() * column, m.yOffset() + m.cellHeight() * row, DOOR_THICKNESS, m.cellHeight());
			}
			if (column < board.getNumColumns() - 1
					&& board.getCell(row, column + 1).doorDirection == DoorDirection.LEFT) {
				g.fillRect(m.xOffset() + m.cellWidth() * column + m.cellWidth() - DOOR_THICKNESS, m.yOffset() + m.cellHeight() * row,
						DOOR_THICKNESS, m.cellHeight());
			}
		}
	}
	
	@Override
	public String toString() {
		return String.format("BoardCell[%d, %d]", row, column);
	}

	// getters
	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public Set<BoardCell> getAdjList() {
		return adjList;
	}

	public char getSecretPassage() {
		return secretPassage;
	}

	public DoorDirection getDoorDirection() {
		return doorDirection;
	}

	public boolean isRoom() {
		return isRoom;
	}

	public boolean isOccupied() {
		return isOccupied;
	}

	public boolean isDoorway() {
		return doorDirection != null && doorDirection != DoorDirection.NONE;
	}

	public boolean isLabel() {
		return isLabel;
	}

	public boolean isRoomCenter() {
		return isRoomCenter;
	}

	/* Setters */

	public void setRoom(boolean isRoom) {
		this.isRoom = isRoom;
	}

	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}

	public void setLabel(boolean isLabel) {
		this.isLabel = isLabel;
	}

	public void setRoomCenter(boolean isRoomCenter) {
		this.isRoomCenter = isRoomCenter;
	}

	public void setDoorDirection(DoorDirection doorDirection) {
		this.doorDirection = doorDirection;
	}

	public void setSecretPassage(char secretPassage) {
		this.secretPassage = secretPassage;
	}
}
