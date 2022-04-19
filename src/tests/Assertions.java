package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

public class Assertions {

	public Assertions() {
		// TODO Auto-generated constructor stub
	}

	public static <T> void assertSetContents(Set<T> set, int size, T... items) {
		assertEquals(size, set.size());
		for (var item : items) {
			assertTrue(set.contains(item), "Expected " + item + " to be in the set but it was not");
		}
	}

	public static <T> void assertSetContents(Set<T> set, T... items) {
		assertSetContents(set, items.length, items);
	}
}
