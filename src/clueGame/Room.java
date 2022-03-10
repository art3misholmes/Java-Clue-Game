package clueGame;

public class Room {
	private String name;
	private boolean isNormalSpace;
	private BoardCell labelCell, centerCell;

	public Room(String name, boolean isNormalSpace) {
		this.name = name;
		this.isNormalSpace = isNormalSpace;
	}

	public boolean isNormalSpace() {
		return isNormalSpace;
	}

	public String getName() {
		return name;
	}

	public void setLabelCell(BoardCell labelCell) {
		this.labelCell = labelCell;
	}

	public void setCenterCell(BoardCell centerCell) {
		this.centerCell = centerCell;
	}

	public BoardCell getLabelCell() {
		return labelCell;
	}

	public BoardCell getCenterCell() {
		return centerCell;
	}

}
