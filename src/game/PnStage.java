package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

class PnStage extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private String ctrlMsg = "";
	private Snake snake;
	private java.util.Vector<Food> foods;
	
	PnStage() {
		super();
		snake = new Snake();
		foods = new java.util.Vector<>();
		this.setBackground(Color.decode("#ACFA58"));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (ctrlMsg.isEmpty()) {
			
			snake.draw(g);
			
			for (Food fd1 : foods)
				fd1.draw(g);
			
			g.setColor(Color.BLACK);
			g.drawString("SPACE - Pause", 15, 25);
			g.drawString("ESC - Quit", 15, 45);
			g.drawString(String.format("Snake: %d,%d", snake.x(), snake.y()), 15, 65);
			
			return;
		}
		g.setColor(Color.BLACK);
		g.drawString(ctrlMsg, 15, 25);
	}
	
	private void checkSnakeEatFood() {
		for (int x=0; x < foods.size(); x++) {
			Food fd1 = foods.get(x);
			if (snake.canEat(fd1)) {
				snake.grow();
				foods.remove(x);
				return;
			}
		}
	}
	
	private void randomFood() {
		if (foods.size() >= 5) return; 
		int tmpNum = Util.nextInt(10, 20);
		if (tmpNum != 15) return;
		foods.add(Food.random());
	}
	
	void updateStage(int kev, String _msg) {
		ctrlMsg = _msg;
		if (ctrlMsg.isEmpty()) {
			if (KeyEvent.VK_HOME == kev) {
				snake.grow();
			}
			snake.move(kev);
			checkSnakeEatFood();
			randomFood();
		}
		this.updateUI();
	}
}
