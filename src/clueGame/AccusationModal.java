package clueGame;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class AccusationModal extends JDialog {
	private static final long serialVersionUID = 1L; // bluh

	private final JComboBox<String> roomCombo, personCombo, weaponCombo;

	public AccusationModal(CardCollection deck) {
		super(JOptionPane.getFrameForComponent(Board.getInstance()), "Accusation!!", true);
		setLocationRelativeTo(Board.getInstance());

		// make graphics box
		setLayout(new GridLayout(4, 2));
		setSize(400, 200);

		/* Room */
		var roomLabel = new JLabel("Room");
		add(roomLabel);

		// room drop down
		add(roomCombo = makeComboBoxForCards(deck.getRoomCards()));

		/* Person */
		var person = new JLabel("Person");
		add(person);

		// person drop down
		add(personCombo = makeComboBoxForCards(deck.getPersonCards()));

		/* Weapon */
		var weaponLabel = new JLabel("Weapon");
		add(weaponLabel);

		// weapon drop down
		add(weaponCombo = makeComboBoxForCards(deck.getWeaponCards()));

		// buttons
		var submit = new JButton("Submit");
		add(submit);
		submit.addActionListener(this::submitClicked);

		var cancel = new JButton("Cancel");
		add(cancel);
		cancel.addActionListener(this::cancelClicked);
	}

	private static JComboBox<String> makeComboBoxForCards(Set<Card> cards) {
		var cardCombo = new JComboBox<String>();
		for (var c : cards) {
			cardCombo.addItem(c.name());
		}
		return cardCombo;
	}

	private void submitClicked(ActionEvent e) {
		var board = Board.getInstance();
		board.handleHumanAccusation(new Solution(new Card(Card.Type.ROOM, (String) roomCombo.getSelectedItem()),
				new Card(Card.Type.PERSON, (String) personCombo.getSelectedItem()),
				new Card(Card.Type.WEAPON, (String) weaponCombo.getSelectedItem())));
		setVisible(false);
	}

	private void cancelClicked(ActionEvent e) {
		setVisible(false);
	}
}
