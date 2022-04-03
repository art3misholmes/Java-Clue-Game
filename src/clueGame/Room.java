package clueGame;

public class Room {
	private String name;
	private boolean isNormalSpace;
	private BoardCell labelCell, centerCell;
	private Card card;

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

	public Card getCard() {
		return card;
	}

	public void setLabelCell(BoardCell labelCell) {
		this.labelCell = labelCell;
	}

	public void setCenterCell(BoardCell centerCell) {
		this.centerCell = centerCell;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public BoardCell getLabelCell() {
		return labelCell;
	}

	public BoardCell getCenterCell() {
		return centerCell;
	}

}
