package game;

import java.awt.event.KeyEvent;
import java.util.Random;

class Util {

	private static final Random rand = new Random();
	
	private static final int[] moveKeys = {
		KeyEvent.VK_UP, KeyEvent.VK_DOWN, 
		KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT
	};
	
	static boolean isMoveKey(int kev) {
		for (int x : moveKeys) {
			if (kev == x) return true;
		}
		return false;
	}
	
	public static int nextInt(int min, int max) {
	    return rand.nextInt((max - min) + 1) + min;
	}	
}
