package clueGame;

public class Room {
	private String name;
	private BoardCell labelCell, centerCell;

	public Room(String name) {
		this.name = name;
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
