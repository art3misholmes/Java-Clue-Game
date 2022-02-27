package experiment;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Kai Page
 * @author Kelsi Wood
 */
public class TestBoard {
	public TestBoard() {
		
	}
	
	public void calcTargets(TestBoardCell startCell, int pathLength) {
		
	}
	
	public Set<TestBoardCell> getTargets() {
		return new HashSet<>();
	}
	
	public TestBoardCell getCell(int row, int col) {
		return new TestBoardCell(row, col);
	}
}
