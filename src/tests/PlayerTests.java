package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import org.junit.jupiter.api.*;

import clueGame.*;

public class PlayerTests {
	private Board board;

	@BeforeEach
	public void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
	}

	@Test
	public void testPlayerLoading() {
		// cocoa is the human player
		var cocoa = board.getHumanPlayer();
		assertEquals("Cocoa Ramsey", cocoa.getName());
		assertEquals(31, cocoa.getColor().getRed());
		assertEquals(77, cocoa.getColor().getGreen());
		assertEquals(111, cocoa.getColor().getBlue());
		assertEquals(4, cocoa.getRow());
		assertEquals(2, cocoa.getColumn());

		// marcon is an NPC
		var marcon = board.getComputerPlayers().get(0);
		assertEquals("Marcon", marcon.getName());
		assertEquals(20, marcon.getColor().getRed());
		assertEquals(20, marcon.getColor().getGreen());
		assertEquals(54, marcon.getColor().getBlue());
		assertEquals(3, marcon.getRow());
		assertEquals(5, marcon.getColumn());
	}
}
