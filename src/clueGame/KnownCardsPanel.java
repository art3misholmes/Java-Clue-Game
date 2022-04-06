package clueGame;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class KnownCardsPanel extends JPanel {
	private static final long serialVersionUID = 1L; // bluh
	
	private final Player humanPlayer;

	public KnownCardsPanel(HumanPlayer humanPlayer) {
		this.humanPlayer = humanPlayer;
	}
	
	public void update() {
		
	}

	/**
	 * Main to test the panel
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		var humanPlayer = new HumanPlayer("Sample Player", Color.BLUE, 0, 0);
		var panel = new KnownCardsPanel(humanPlayer); // create the panel
		var frame = new JFrame(); // create the frame
		frame.setContentPane(panel); // put the panel in the frame
		frame.setSize(163, 660); // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true); // make it visible

		// test filling in the data
		humanPlayer.updateHand(new Card(Card.Type.PERSON, "hand person"));
		humanPlayer.updateHand(new Card(Card.Type.ROOM, "hand room"));
		humanPlayer.updateHand(new Card(Card.Type.WEAPON, "hand weapon"));
		
		var seenPerson = new Card(Card.Type.PERSON, "seen person");
		humanPlayer.updateSeen(seenPerson);
		humanPlayer.updatePresentedBy(seenPerson, new ComputerPlayer("purple person", Color.MAGENTA, 0, 0));
		
		var seenRoom = new Card(Card.Type.ROOM, "seen room");
		humanPlayer.updateSeen(seenRoom);
		humanPlayer.updatePresentedBy(seenRoom, new ComputerPlayer("blue person", Color.CYAN, 0, 0));
		
		var seenWeapon = new Card(Card.Type.WEAPON, "seen weapon");
		humanPlayer.updateSeen(seenWeapon);
		humanPlayer.updatePresentedBy(seenWeapon, new ComputerPlayer("yellow person", Color.YELLOW, 0, 0));
		
		panel.update();
	}
}
