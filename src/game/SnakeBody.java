package game;

import java.awt.Graphics;

class SnakeBody extends java.awt.Rectangle {
	private static final long serialVersionUID = 1L;
	
	SnakeBody next = null;
	
	SnakeBody(int _x, int _y) {
		super(_x, _y, 6, 6);
	}
	
	SnakeBody(SnakeBody obj) {
		this(obj.x, obj.y);
	}

	void draw(Graphics g) {
		g.fillRect(x, y, width, height);
	}
}
