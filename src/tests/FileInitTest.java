package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import clueGame.*;

public class FileInitTest {
	public static final int LEGEND_SIZE = 11, NUM_ROWS = 26, NUM_COLUMNS = 23;

	private static Board board;

	@BeforeAll
	public static void setup() {
		// initialize board with our layout and configuration
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
	}

	@Test
	public void testRoomLabels() {
		assertEquals("Kitchen Arena", board.getRoom('K').getName());
		assertEquals("Green Room", board.getRoom('G').getName());
		assertEquals("Warehouse", board.getRoom('H').getName());
		assertEquals("Rec Room", board.getRoom('R').getName());
		assertEquals("Battle Zone", board.getRoom('B').getName());
	}

	@Test
	public void testBoardDimensions() {
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());
	}

	@Test
	public void fourDoorDirections() {
		// some cells that are doors
		var cell = board.getCell(6, 2);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.UP, cell.getDoorDirection());
		cell = board.getCell(10, 3);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.LEFT, cell.getDoorDirection());
		cell = board.getCell(12, 7);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.DOWN, cell.getDoorDirection());
		cell = board.getCell(10, 18);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.RIGHT, cell.getDoorDirection());

		// some cells that are not doors
		cell = board.getCell(8, 15);
		assertFalse(cell.isDoorway());
		cell = board.getCell(20, 6);
		assertFalse(cell.isDoorway());
	}

	@Test
	public void testNumberOfDoorways() {
		var doorsCount = 0;
		for (var i = 0; i < NUM_ROWS; i++) {
			for (var j = 0; j < NUM_COLUMNS; j++) {
				if (board.getCell(i, j).isDoorway()) {
					doorsCount++;
				}
			}
		}
		assertEquals(16, doorsCount);
	}

	@Test
	public void testRooms() {
		// standard room
		var cell = board.getCell(15, 5);
		var room = assertNotNull(board.getRoom(cell));
		assertEquals("Kitchen Arena", room.getName());
		assertFalse(cell.isLabel());
		assertFalse(cell.isRoomCenter());
		assertFalse(cell.isDoorway());

		// room label
		cell = board.getCell(10, 0);
		room = assertNotNull(board.getRoom(cell));
		assertEquals("VIP Lounge", room.getName());
		assertTrue(cell.isLabel());
		assertEquals(cell, room.getLabelCell());

		// room center
		cell = board.getCell(2, 12);
		room = assertNotNull(board.getRoom(cell));
		assertEquals("Battle Zone", room.getName());
		assertTrue(cell.isRoomCenter());
		assertEquals(cell, room.getCenterCell());

		// secret passage
		cell = board.getCell(5, 2);
		room = assertNotNull(board.getRoom(cell));
		assertEquals("Studio", room.getName());
		assertEquals('G', cell.getSecretPassage());

		// walkway
		cell = board.getCell(6, 6);
		room = assertNotNull(board.getRoom(cell));
		assertEquals("Walkway", room.getName());
		assertFalse(cell.isLabel());
		assertFalse(cell.isRoomCenter());

		// unused
		cell = board.getCell(0, 8);
		room = assertNotNull(board.getRoom(cell));
		assertEquals("Unused", room.getName());
		assertFalse(cell.isLabel());
		assertFalse(cell.isRoomCenter());
	}

	public static <T> T assertNotNull(T value) {
		assertNotEquals(null, value);
		return value;
	}
}
