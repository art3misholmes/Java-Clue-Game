package experiment;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Kai Page
 * @author Kelsi Wood
 */
public class TestBoardCell {
	private int row, column;
	private Set<TestBoardCell> adjList = new HashSet<>();
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
		adjList.add(cell);
	}

}
