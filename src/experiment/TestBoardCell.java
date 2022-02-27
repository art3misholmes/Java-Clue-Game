package experiment;

import java.util.Set;

public class TestBoardCell {
	private int row, column;
	private Set<TestBoardCell> adjList;
	private boolean isRoom, isOccupied;

	public TestBoardCell(int row, int column) {
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

	public Set<TestBoardCell> getAdjList() {
		return adjList;
	}

	public void addAdjacency(TestBoardCell cell) {

	}

}
