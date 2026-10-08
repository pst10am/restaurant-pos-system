package game;

import java.awt.Color;
import java.awt.Graphics;

class Food extends java.awt.Rectangle {
	private static final long serialVersionUID = 1L;
	
	private Food(int _x, int _y) {
		super(_x, _y, 6, 6);
	}
	
	void draw(Graphics g) {
		g.setColor(Color.MAGENTA);
		g.fillRect(x, y, width, height);
		g.setColor(Color.WHITE);
		g.drawRect(x-1, y-1, width+1, height+1);
		//g.drawString(String.format("%d,%d", x, y), x, y+18);
	}

	static Food random() {
		int _x = Util.nextInt(150, 490);
		int _y = Util.nextInt(150, 330);
		int _x2 = (int)(Math.floor(_x/6) * 6);
		int _y2 = (int)(Math.floor(_y/6) * 6);
		return new Food(_x2, _y2);
	}
}
