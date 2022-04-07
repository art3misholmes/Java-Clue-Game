package clueGame;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class KnownCardsPanel extends JPanel {
	private static final long serialVersionUID = 1L; // bluh

	private final HumanPlayer humanPlayer;
	private final CardDisplayPanel people, rooms, weapons;

	public KnownCardsPanel(HumanPlayer humanPlayer) {
		this.humanPlayer = humanPlayer;
		
		setLayout(new GridLayout(3, 1));
		setBorder(new TitledBorder(new EtchedBorder(), "Known Cards"));
		
		add(people = new CardDisplayPanel("People", humanPlayer.getHand().getPersonCards(), humanPlayer.getSeen().getPersonCards()));
		add(rooms = new CardDisplayPanel("Rooms", humanPlayer.getHand().getRoomCards(), humanPlayer.getSeen().getRoomCards()));
		add(weapons = new CardDisplayPanel("Weapons", humanPlayer.getHand().getWeaponCards(), humanPlayer.getSeen().getWeaponCards()));
	}

	public void update() {
		people.update();
		rooms.update();
		weapons.update();
		revalidate();
	}

	private class CardDisplayPanel extends JPanel {
		private static final long serialVersionUID = 1L; // bluh

		private final Set<Card> hand, seen;

		public CardDisplayPanel(String title, Set<Card> hand, Set<Card> seen) {
			this.hand = hand;
			this.seen = seen;

			setLayout(new GridLayout(0, 1));
			setBorder(new TitledBorder(new EtchedBorder(), title));
		}

		public void update() {
			removeAll();

			var handLabel = new JLabel("Hand:");
			add(handLabel);

			addCards(hand);

			var seenLabel = new JLabel("Seen:");
			add(seenLabel);

			var seenButNotInHand = new TreeSet<Card>();
			seenButNotInHand.addAll(seen);
			seenButNotInHand.removeAll(hand);
			addCards(seenButNotInHand);
		}

		private void addCards(Set<Card> cards) {
			if (cards.isEmpty()) {
				var field = new JTextField("None");
				field.setEditable(false);
				field.setBackground(Color.WHITE);
				add(field);
			} else {
				for (var c : cards) {
					var field = new JTextField(c.name());
					field.setEditable(false);

					if (humanPlayer.getPresentedBy().containsKey(c)) {
						field.setBackground(humanPlayer.getPresentedBy().get(c));
					} else {
						field.setBackground(humanPlayer.getAccentColor());
					}
					
					add(field);
				}
			}
		}
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
		frame.setSize(180, 700); // size the frame
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
