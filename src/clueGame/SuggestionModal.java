package clueGame;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class SuggestionModal extends JDialog {

	public SuggestionModal (Card currentRoom, CardCollection deck) {
		
		//make graphics box
		setLayout(new GridLayout(4,2));
		
		//Labels
		var currentRoomLabel = new JLabel("Current room");
		add(currentRoomLabel);
		
		//room box
		var roomBox = new JTextField();
		roomBox.setEditable(false);
		add(roomBox);
		
		//label
		var person = new JLabel("Person");
		add(person);
				
		//person drop down
		JComboBox<String> personCombo = new JComboBox<String>();
		for(var personCard: deck.getPersonCards()) {
			personCombo.addItem(personCard.name());
		}
		
		add(personCombo);
		
		//label
		var weaponLabel = new JLabel("Weapon");
		add(weaponLabel); 
		
		//drop down
		JComboBox<String> weaponCombo = new JComboBox<String>();
		for(var weaponCard: deck.getWeaponCards()) {
			weaponCombo.addItem(weaponCard.name());
		}
		
		add(weaponCombo);
		
		//buttons
		var submit = new JButton("Submit");
		add(submit);
		
		var cancel =new JButton("Cancel");
		add(cancel);
		
	}


}
