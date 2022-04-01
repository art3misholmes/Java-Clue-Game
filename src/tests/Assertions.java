package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

public class Assertions {

	public Assertions() {
		// TODO Auto-generated constructor stub
	}

	public static <T> void assertSetContents(Set<T> set, int size, T... items) {
		assertEquals(size, set.size());
		for (var item : items) {
			assertTrue(set.contains(item));
		}
	}

	public static <T> void assertSetContents(Set<T> set, T... items) {
		assertSetContents(set, items.length, items);
	}
}
