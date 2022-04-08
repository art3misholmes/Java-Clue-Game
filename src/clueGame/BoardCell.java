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

	public void draw(Graphics g, CellMetrics m) {
		var board = Board.getInstance();
		var room = board.getRoom(this);

		if (room.isNormalSpace()) {
			switch (room.getName()) {
			case "Unused" -> {
				// don't need to draw over the background
			}
			case "Walkway" -> {
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(m.xOffset() + m.cellWidth() * column + 1, m.yOffset() + m.cellHeight() * row + 1, m.cellWidth() - 2,
						m.cellHeight() - 2);
			}
			}
		} else {
			// cell is part of a room
			g.setColor(Color.DARK_GRAY);
			g.fillRect(m.xOffset() + m.cellWidth() * column, m.yOffset() + m.cellHeight() * row, m.cellWidth(), m.cellHeight());

			// is there a door into this space?
			g.setColor(Color.WHITE);
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
