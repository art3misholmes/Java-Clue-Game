package clueGame;

import java.util.HashSet;
import java.util.Set;

public class BoardCell {
	private int row, column;
	private Set<BoardCell> adjList = new HashSet<>();
	private boolean isRoom, isOccupied;

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

	public boolean isDoorway() {
		// TODO Auto-generated method stub
		return false;
	}

	public DoorDirection getDoorDirection() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isLabel() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isRoomCenter() {
		// TODO Auto-generated method stub
		return false;
	}

	public char getSecretPassage() {
		// TODO Auto-generated method stub
		return 0;
	}

}
