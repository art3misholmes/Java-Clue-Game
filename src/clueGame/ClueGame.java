package clueGame;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class ClueGame extends JFrame {
	private static final long serialVersionUID = 1L; // bluh

	public ClueGame() {
		var board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
		add(board, BorderLayout.CENTER);

		var controlPanel = new GameControlPanel();
		// controlPanel.setSize(750, 180);
		add(controlPanel, BorderLayout.SOUTH);

		var cardsPanel = new KnownCardsPanel(board.getHumanPlayer());
		// cardsPanel.setSize(180, 700);
		cardsPanel.update();
		add(cardsPanel, BorderLayout.EAST);
	}

	public static void main(String[] args) {
		var frame = new ClueGame();
		frame.setSize(800, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
