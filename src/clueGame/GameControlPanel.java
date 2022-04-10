package clueGame;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameControlPanel extends JPanel {
	private static final long serialVersionUID = 1L; // bluh

	private final JTextField turnField, rollField, guessField, resultField;

	/**
	 * Constructor for the panel, it does 90% of the work
	 */
	public GameControlPanel() {
		setLayout(new GridLayout(2, 1));

		/* top row */
		var topRow = new JPanel(new GridLayout(1, 4));
		add(topRow);

		// whose turn
		var turnPanel = new JPanel(new GridLayout(2, 1));
		topRow.add(turnPanel);
		var turnLabel = new JLabel("Whose turn?");
		turnPanel.add(turnLabel);
		turnField = new JTextField();
		turnPanel.add(turnField);
		turnField.setEditable(false);

		// rolls panel
		var rollPanel = new JPanel(new GridLayout(1, 1));
		topRow.add(rollPanel);
		var rollLabel = new JLabel("Roll: ");
		rollPanel.add(rollLabel);
		rollLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		rollField = new JTextField();
		rollPanel.add(rollField);
		rollField.setEditable(false);

		// accusation button
		var accusationButton = new JButton("Make Accusation");
		topRow.add(accusationButton);

		// next button
		var nextButton = new JButton("NEXT!");
		topRow.add(nextButton);
		nextButton.addActionListener(this::nextButtonClicked);

		/* bottom row */
		var bottomRow = new JPanel(new GridLayout(1, 2));
		add(bottomRow);

		// guess panel
		var guessPanel = new JPanel(new GridLayout(1, 1));
		bottomRow.add(guessPanel);
		guessPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
		guessField = new JTextField();
		guessPanel.add(guessField);
		guessField.setEditable(false);

		// guess panel
		var resultPanel = new JPanel(new GridLayout(1, 1));
		bottomRow.add(resultPanel);
		resultPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
		resultField = new JTextField();
		resultPanel.add(resultField);
		resultField.setEditable(false);
	}

	public void setTurn(Player p, int roll) {
		rollField.setText("" + roll);
		turnField.setText(p.getName());
		turnField.setBackground(p.getAccentColor());
	}

	public void setGuess(String guess) {
		guessField.setText(guess);

	}

	public void setGuessResult(String result) {
		resultField.setText(result);
	}
	
	private void nextButtonClicked(ActionEvent e) {
		Board.getInstance().nextButtonClicked();
	}

	/**
	 * Main to test the panel
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		var panel = new GameControlPanel(); // create the panel
		var frame = new JFrame(); // create the frame
		frame.setContentPane(panel); // put the panel in the frame
		frame.setSize(750, 180); // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true); // make it visible

		// test filling in the data
		panel.setTurn(new ComputerPlayer("Sample Player", Color.BLUE, 0, 0), 5);
		panel.setGuess("I have no guess!");
		panel.setGuessResult("So you have nothing?");
	}
	

	
	
}
