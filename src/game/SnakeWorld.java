package game;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class SnakeWorld extends JFrame {
	private static final long serialVersionUID = 1L;

	private String ctrlMsg = "";
	private PnStage pnStage;
	private int keyEvt = -1;

	public SnakeWorld() {
		super("!!!SnakeWorld!!!");
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		initComponents();
		
		this.pack();
		this.setSize(640, 480);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		play();
	}
	
	private void initComponents() {
		pnStage = new PnStage();
		this.getContentPane().add(pnStage, BorderLayout.CENTER);
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				ctrlMsg = "";
				keyEvt = e.getKeyCode();
			}
		});
	}
	
	private void gameOver() {
		JOptionPane.showMessageDialog(this, "GAME OVER");
		System.exit(0);
	}
	
	private void play() {
		JOptionPane.showMessageDialog(this, "PRESS ENTER TO START!");
		Thread trd1 = new Thread() {
			public void run() {
				try {
					while (!"GAME OVER".equals(ctrlMsg)) {
						if (KeyEvent.VK_ESCAPE == keyEvt) {
							ctrlMsg = "GAME OVER";
						}
						if (KeyEvent.VK_SPACE == keyEvt) {
							ctrlMsg = "PAUSE";
						}
						pnStage.updateStage(keyEvt, ctrlMsg);
						keyEvt = -1;
						Thread.sleep(100);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				gameOver();
			}
		};
		trd1.start();
	}

	public static void main(String[] args) {
		MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
		new SnakeWorld();
	}
}
