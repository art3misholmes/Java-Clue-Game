package clueGame;

import java.util.List;
import java.util.Random;

public class RandomChooser {
	private RandomChooser() {}

	private static Random rand = new Random();
	
	public static <T> T pickRandom(List<T> list) {
		return list.get(rand.nextInt(list.size()));
	}
}
