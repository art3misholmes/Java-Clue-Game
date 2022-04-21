package clueGame;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class SuggestionModal extends JDialog {

	private final Card currentRoom;
	private final JComboBox<String> personCombo, weaponCombo;

	public SuggestionModal(Card currentRoom, CardCollection deck) {
		super(JOptionPane.getFrameForComponent(Board.getInstance()), "Suggestion", true);
		setLocationRelativeTo(Board.getInstance());

		this.currentRoom = currentRoom;

		// make graphics box
		setLayout(new GridLayout(4, 2));
		setSize(400, 200);

		// Labels
		var currentRoomLabel = new JLabel("Current room");
		add(currentRoomLabel);

		// room box
		var roomBox = new JTextField(currentRoom.name());
		roomBox.setEditable(false);
		add(roomBox);

		// label
		var person = new JLabel("Person");
		add(person);

		// person drop down
		personCombo = new JComboBox<>();
		for (var personCard : deck.getPersonCards()) {
			personCombo.addItem(personCard.name());
		}

		add(personCombo);

		// label
		var weaponLabel = new JLabel("Weapon");
		add(weaponLabel);

		// drop down
		weaponCombo = new JComboBox<>();
		for (var weaponCard : deck.getWeaponCards()) {
			weaponCombo.addItem(weaponCard.name());
		}

		add(weaponCombo);

		// buttons
		var submit = new JButton("Submit");
		add(submit);
		submit.addActionListener(this::submitClicked);

		var cancel = new JButton("Cancel");
		add(cancel);
		cancel.addActionListener(this::cancelClicked);
	}

	private void submitClicked(ActionEvent e) {
		var board = Board.getInstance();
		board.handleSuggestion(
				new Solution(currentRoom, new Card(Card.Type.PERSON, (String) personCombo.getSelectedItem()),
						new Card(Card.Type.WEAPON, (String) weaponCombo.getSelectedItem())),
				board.getHumanPlayer());
		setVisible(false);
	}

	private void cancelClicked(ActionEvent e) {
		setVisible(false);
	}
}
