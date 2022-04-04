package clueGame;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GameControlPanel extends JPanel {
	private static final long serialVersionUID = 1L; // bluh
	
	private final JTextField turnField;

	/**
	 * Constructor for the panel, it does 90% of the work
	 */
	public GameControlPanel()  {
		setLayout(new GridLayout(2, 1));
		
		var topRow = new JPanel(new GridLayout(1, 4));
		add(topRow);
		var turnPanel = new JPanel(new GridLayout(2, 1));
		topRow.add(turnPanel);
		var turnLabel = new JLabel("Whose turn?");
		turnPanel.add(turnLabel);
		turnField = new JTextField();
		turnPanel.add(turnField);
		turnField.setEditable(false);
	}

	public void setTurn(Player p, int roll) {
		// TODO Auto-generated method stub
		
	}

	public void setGuess(String guess) {
		// TODO Auto-generated method stub
		
	}

	public void setGuessResult(String result) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Main to test the panel
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		var panel = new GameControlPanel();  // create the panel
		var frame = new JFrame();  // create the frame 
		frame.setContentPane(panel); // put the panel in the frame
		frame.setSize(750, 180);  // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true); // make it visible
		
		// test filling in the data
		panel.setTurn(new ComputerPlayer("Sample Player", Color.BLUE, 0, 0), 5);
		panel.setGuess( "I have no guess!");
		panel.setGuessResult( "So you have nothing?");
	}
}
