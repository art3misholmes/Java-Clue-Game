package clueGame;

/**
 * Information about the sizing and position of cells
 * @author Kai Page
 * @author Kelsi Wood
 */
public record CellMetrics(int xOffset, int yOffset, int cellWidth, int cellHeight) {
	public static CellMetrics calculateMetrics(int width, int height, int rows, int cols) {
		// get a square to paint in
		int dim, xOffset = 0, yOffset = 0;
		if (width < height) {
			dim = width;
			yOffset = (height - width) / 2;
		} else {
			dim = height;
			xOffset = (width - height) / 2;
		}

		// shrink to the game board's aspect ratio
		int frameWidth, frameHeight;
		if (cols < rows) {
			frameHeight = dim;
			frameWidth = dim * cols / rows;
			xOffset += (frameHeight - frameWidth) / 2;
		} else {
			frameWidth = dim;
			frameHeight = dim * rows / cols;
			yOffset += (frameWidth - frameHeight) / 2;
		}

		int cellWidth = frameWidth / cols, cellHeight = frameHeight / rows;

		return new CellMetrics(xOffset, yOffset, cellWidth, cellHeight);
	}
}
