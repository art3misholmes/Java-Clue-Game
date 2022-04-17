package clueGame;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;

public class Room {
	private String name;
	private boolean isNormalSpace;
	private BoardCell labelCell, centerCell;
	private Card card;
	private int labelWidth;

	//getter
	public int getLabelWidth() {
		return labelWidth;
	}

	//creates room name graphic
	public void drawLabel(Graphics g, CellMetrics m) {
		g.setColor(Color.WHITE);
		var fontSize = 30;
		//Shrink font until it fits
		do {
			g.setFont(new Font(Font.SERIF, Font.BOLD, fontSize));
			fontSize--;
		} while (g.getFontMetrics().stringWidth(name) > m.cellWidth() * labelWidth);
		g.drawString(name, m.xOffset() + m.cellWidth() * labelCell.getColumn() + m.cellWidth() / 2,
				m.yOffset() + m.cellWidth() * labelCell.getRow());
	}

	//setter
	public void setLabelWidth(int labelWidth) {
		this.labelWidth = labelWidth;
	}

	//setter
	public Room(String name, boolean isNormalSpace) {
		this.name = name;
		this.isNormalSpace = isNormalSpace;
	}

	//getters
	public boolean isNormalSpace() {
		return isNormalSpace;
	}

	public String getName() {
		return name;
	}

	public Card getCard() {
		return card;
	}

	//setters
	public void setLabelCell(BoardCell labelCell) {
		this.labelCell = labelCell;
	}

	public void setCenterCell(BoardCell centerCell) {
		this.centerCell = centerCell;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	//getters
	public BoardCell getLabelCell() {
		return labelCell;
	}

	public BoardCell getCenterCell() {
		return centerCell;
	}

}
