package clueGame;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClueGame extends JFrame {
	private static final long serialVersionUID = 1L; // bluh

	public ClueGame() {
		var board = Board.getInstance();
		add(board, BorderLayout.CENTER);

		var controlPanel = new GameControlPanel();
		add(controlPanel, BorderLayout.SOUTH);
		board.setControlPanel(controlPanel);
		
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
		
		var cardsPanel = new KnownCardsPanel(board.getHumanPlayer());
		add(cardsPanel, BorderLayout.EAST);
		board.setCardsPanel(cardsPanel);
		cardsPanel.update();
	}

	public static void main(String[] args) {
		var frame = new ClueGame();
		frame.setSize(800, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		//splash screen
		JOptionPane.showMessageDialog(frame, "You are " + Board.getInstance().getHumanPlayer().getName() + ". \n Can you find the solution \n before the Computer players?");
	}
}
