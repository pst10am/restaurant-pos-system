package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

class Snake {
	
	private int go_dir = KeyEvent.VK_RIGHT;
	private SnakeBody head = null;
	
	Snake() {
		head = new SnakeBody(72, 90);
	}

	int x() {
		return head.x;
	}
	
	int y() {
		return head.y;
	}
	
	void grow() {
		SnakeBody crnt = head;
		while (null != crnt.next) {
			crnt = crnt.next;
		}
		crnt.next = new SnakeBody(crnt);
	}
	
	void move(int kevt) {
		if (!Util.isMoveKey(kevt)) return;
		go_dir = kevt;
	}

	void draw(Graphics g) {
		int tmp_x = 0; int tmp_y = 0;
		int prv_x = head.x; int prv_y = head.y;
		if (KeyEvent.VK_UP == go_dir) head.y -= 6;
		if (KeyEvent.VK_DOWN == go_dir) head.y += 6;
		if (KeyEvent.VK_LEFT == go_dir) head.x -= 6;
		if (KeyEvent.VK_RIGHT == go_dir) head.x += 6;
		SnakeBody crnt = head;
		do {
			if (crnt == head) {
				g.setColor(Color.RED);
				crnt.draw(g);
				g.setColor(Color.BLUE);
			} else {
				crnt.draw(g);
			}
			crnt = crnt.next;
			if (null == crnt) break;
			tmp_x = crnt.x; tmp_y = crnt.y;
			crnt.x = prv_x; crnt.y = prv_y;
			prv_x = tmp_x; prv_y = tmp_y;
		} while (true);
	}

	boolean canEat(Food fd1) {
		return head.intersects(fd1);
	}
}
