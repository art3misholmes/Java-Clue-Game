package clueGame;

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

	public boolean isRoom() {
		return isRoom;
	}

	public void setRoom(boolean isRoom) {
		this.isRoom = isRoom;
	}

	public boolean isOccupied() {
		return isOccupied;
	}

	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}

	public Set<BoardCell> getAdjList() {
		return adjList;
	}

	public void addAdjacency(BoardCell cell) {
		adjList.add(cell);
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

	public boolean isDoorway() {
		return doorDirection != null && doorDirection != DoorDirection.NONE;
	}

	public DoorDirection getDoorDirection() {
		return doorDirection;
	}

	public boolean isLabel() {
		return isLabel;
	}

	public boolean isRoomCenter() {
		return isRoomCenter;
	}

	public char getSecretPassage() {
		return secretPassage;
	}

}
