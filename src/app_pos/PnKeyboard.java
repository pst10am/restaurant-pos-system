package app_pos;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import resrc.StdFont;

public class PnKeyboard extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private static final int def_w = 60;
	private static final int def_h = 55;
	
	private BtKeyTxt btCaps;
	private BtKeyTxt btShift1;
	private BtKeyTxt btShift2;
	private boolean capFlg = false;
	private boolean shiftFlg = false;
	
	private Robot rbt;
	
	private static String[][] keyDefs = {
		{"`~","1!","2@","3#","4$","5%","6^","7&","8*","9(","0)","-_","=+","BkSp"},
		{"Tab","Q","W","E","R","T","Y","U","I","O","P","[{","]}","\\|"},
		{"Caps","A","S","D","F","G","H","J","K","L",";:","'\"","Enter"},
		{"Shift","Z","X","C","V","B","N","M",",<",".>","/?","Shift"},
		{"Space"}
	};
	
	private static PnKeyboard pnKb = null;

	private PnKeyboard() {
		super();
		try {
			rbt = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		this.setLayout(null);
		this.setFont(StdFont.Fnt24);
		this.setBackground(Color.decode("#787878"));
		initComponents();
	}
	
	public static PnKeyboard getInstance() {
		if (null == pnKb) {
			pnKb = new PnKeyboard();
		}
		return pnKb;
	}
	
	private void initComponents() {
		int max_w = 0;
		int y = 0;
		int shiftCnt = 0;
		for (int rw=0; rw < keyDefs.length; rw++) {
			int x = 0;
			int tmp_w = 0;
			for (int cl=0; cl < keyDefs[rw].length; cl++) {
				int x2 = x;
				int w2 = def_w;
				int lightFlg = 0;
				if ("BkSp".equals(keyDefs[rw][cl])) {
					w2 = 75;
				} else if ("Tab".equals(keyDefs[rw][cl])) {
					w2 = 75;
				} else if ("Caps".equals(keyDefs[rw][cl])) {
					w2 = 95;
					lightFlg = 1;
				} else if ("Enter".equals(keyDefs[rw][cl])) {
					w2 = 100;
				} else if ("Shift".equals(keyDefs[rw][cl])) {
					if (shiftCnt == 0) {
						w2 = 127;
					} else {
						w2 = 128;
					}
					lightFlg = 1;
					shiftCnt += 1;
				}  else if ("Space".equals(keyDefs[rw][cl])) {
					w2 = 550;
					x2 = 150;
				}
				BtKeyTxt bt1 = new BtKeyTxt(keyDefs[rw][cl], x2, y, w2+1, def_h+1, lightFlg);
				if ("Caps".equals(keyDefs[rw][cl])) {
					btCaps = bt1;
				} else if ("Shift".equals(keyDefs[rw][cl])) {
					if (btShift1 == null) {
						btShift1 = bt1;
					} else {
						btShift2 = bt1;
					}
				}
				
				bt1.addActionListener(this);
				this.add(bt1);
				x = x + w2;
				tmp_w += w2;
			}
			y = y + def_h;
			if (tmp_w > max_w) {
				max_w = tmp_w;
			}
		}
		this.setPreferredSize(new Dimension(max_w+1, (5*def_h)+1));
	}
	
	private void updateShift(boolean sflg) {
		shiftFlg = sflg;
		btShift1.flipLight(shiftFlg);
		btShift2.flipLight(shiftFlg);
		if (shiftFlg) {
			rbt.keyPress(KeyEvent.VK_SHIFT);
		} else {
			rbt.keyRelease(KeyEvent.VK_SHIFT);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		BtKeyTxt bt1 = (BtKeyTxt)e.getSource();
		if (bt1 == btCaps) {
			capFlg = !capFlg;
			btCaps.flipLight(capFlg);
			if (capFlg) {
				rbt.keyPress(KeyEvent.VK_CAPS_LOCK);
				rbt.keyRelease(KeyEvent.VK_CAPS_LOCK);
			} else {
				System.out.println("clear caps.");
				rbt.keyPress(KeyEvent.VK_CAPS_LOCK);
				rbt.keyRelease(KeyEvent.VK_CAPS_LOCK);
				updateShift(false);
			}
		} else if (bt1 == btShift1 || bt1 == btShift2) {
			updateShift(!shiftFlg);
		} else {
			rbt.keyPress(bt1.getKeyNorm());
			rbt.keyRelease(bt1.getKeyNorm());
			//
			if (shiftFlg) {
				updateShift(false);
			}
		}
	}
	
	private class BtKeyTxt extends javax.swing.JButton {
		private static final long serialVersionUID = 1L;
		
		private String btTxt = "";
		private int lightFlg = 0;
		private int keyNorm = -1;

		BtKeyTxt(String _btTxt, int bx, int by, int bw, int bh, int _lightFlg) {
			super();
			setBorder(BorderFactory.createLineBorder(Color.GRAY));
			setBackground(Color.WHITE);
			setFont(StdFont.Fnt16B);
			setFocusable(false);
			setOpaque(true);
			setBounds(bx, by, bw, bh);
			btTxt = _btTxt;
			lightFlg = _lightFlg;
			if ("BkSp".equals(btTxt)) {
				keyNorm = KeyEvent.VK_BACK_SPACE;
			} else if ("Tab".equals(btTxt)) {
				keyNorm = KeyEvent.VK_TAB;
			} else if ("Caps".equals(btTxt)) {
				keyNorm = KeyEvent.VK_CAPS_LOCK;
			} else if ("Enter".equals(btTxt)) {
				keyNorm = KeyEvent.VK_ENTER;
			} else if ("Shift".equals(btTxt)) {
				keyNorm = KeyEvent.VK_SHIFT;
			} else if ("Space".equals(btTxt)) {
				keyNorm = KeyEvent.VK_SPACE;
			} else if ("`~".equals(btTxt)) {
				keyNorm = KeyEvent.VK_BACK_QUOTE;
			} else if ("1!".equals(btTxt)) {
				keyNorm = KeyEvent.VK_1;
			} else if ("2@".equals(btTxt)) {
				keyNorm = KeyEvent.VK_2;
			} else if ("3#".equals(btTxt)) {
				keyNorm = KeyEvent.VK_3;
			} else if ("4$".equals(btTxt)) {
				keyNorm = KeyEvent.VK_4;
			} else if ("5%".equals(btTxt)) {
				keyNorm = KeyEvent.VK_5;
			} else if ("6^".equals(btTxt)) {
				keyNorm = KeyEvent.VK_6;
			} else if ("7&".equals(btTxt)) {
				keyNorm = KeyEvent.VK_7;
			} else if ("8*".equals(btTxt)) {
				keyNorm = KeyEvent.VK_8;
			} else if ("9(".equals(btTxt)) {
				keyNorm = KeyEvent.VK_9;
			} else if ("0)".equals(btTxt)) {
				keyNorm = KeyEvent.VK_0;
			} else if ("-_".equals(btTxt)) {
				keyNorm = KeyEvent.VK_MINUS;
			} else if ("=+".equals(btTxt)) {
				keyNorm = KeyEvent.VK_EQUALS;
			} else if ("Q".equals(btTxt)) {
				keyNorm = KeyEvent.VK_Q;
			} else if ("W".equals(btTxt)) {
				keyNorm = KeyEvent.VK_W;
			} else if ("E".equals(btTxt)) {
				keyNorm = KeyEvent.VK_E;
			} else if ("R".equals(btTxt)) {
				keyNorm = KeyEvent.VK_R;
			} else if ("T".equals(btTxt)) {
				keyNorm = KeyEvent.VK_T;
			} else if ("Y".equals(btTxt)) {
				keyNorm = KeyEvent.VK_Y;
			} else if ("U".equals(btTxt)) {
				keyNorm = KeyEvent.VK_U;
			} else if ("I".equals(btTxt)) {
				keyNorm = KeyEvent.VK_I;
			} else if ("O".equals(btTxt)) {
				keyNorm = KeyEvent.VK_O;
			} else if ("P".equals(btTxt)) {
				keyNorm = KeyEvent.VK_P;
			} else if ("[{".equals(btTxt)) {
				keyNorm = KeyEvent.VK_OPEN_BRACKET;
			} else if ("]}".equals(btTxt)) {
				keyNorm = KeyEvent.VK_CLOSE_BRACKET;
			} else if ("\\|".equals(btTxt)) {
				keyNorm = KeyEvent.VK_BACK_SLASH;
			} else if ("A".equals(btTxt)) {
				keyNorm = KeyEvent.VK_A;
			} else if ("S".equals(btTxt)) {
				keyNorm = KeyEvent.VK_S;
			} else if ("D".equals(btTxt)) {
				keyNorm = KeyEvent.VK_D;
			} else if ("F".equals(btTxt)) {
				keyNorm = KeyEvent.VK_F;
			} else if ("G".equals(btTxt)) {
				keyNorm = KeyEvent.VK_G;
			} else if ("H".equals(btTxt)) {
				keyNorm = KeyEvent.VK_H;
			} else if ("J".equals(btTxt)) {
				keyNorm = KeyEvent.VK_J;
			} else if ("K".equals(btTxt)) {
				keyNorm = KeyEvent.VK_K;
			} else if ("L".equals(btTxt)) {
				keyNorm = KeyEvent.VK_L;
			} else if (";:".equals(btTxt)) {
				keyNorm = KeyEvent.VK_SEMICOLON;
			} else if ("'\"".equals(btTxt)) {
				keyNorm = KeyEvent.VK_QUOTE;
			} else if ("Z".equals(btTxt)) {
				keyNorm = KeyEvent.VK_Z;
			} else if ("X".equals(btTxt)) {
				keyNorm = KeyEvent.VK_X;
			} else if ("C".equals(btTxt)) {
				keyNorm = KeyEvent.VK_C;
			} else if ("V".equals(btTxt)) {
				keyNorm = KeyEvent.VK_V;
			} else if ("B".equals(btTxt)) {
				keyNorm = KeyEvent.VK_B;
			} else if ("N".equals(btTxt)) {
				keyNorm = KeyEvent.VK_N;
			} else if ("M".equals(btTxt)) {
				keyNorm = KeyEvent.VK_M;
			} else if (",<".equals(btTxt)) {
				keyNorm = KeyEvent.VK_COMMA;
			} else if (".>".equals(btTxt)) {
				keyNorm = KeyEvent.VK_PERIOD;
			} else if ("/?".equals(btTxt)) {
				keyNorm = KeyEvent.VK_SLASH;
			}
		}
		
		int getKeyNorm() {
			return keyNorm;
		}
		
		void flipLight(boolean lightStatus) {
			if (lightFlg <= 0) return;
			lightFlg = lightStatus ? 2 : 1;
			repaint();
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (btTxt.length() == 2) {
				g.drawString(btTxt.substring(1), 7, 22);
				g.drawString(btTxt.substring(0, 1), 7, 47);
			} else {
				g.drawString(btTxt, 5, 22);
			}
			if (lightFlg <= 0) return;
			
			g.setColor(Color.GRAY);
			if (lightFlg == 2) {
				g.setColor(Color.GREEN);
			}
			g.fillRect(this.getSize().width - 11, 3, 7, 7);
			g.setColor(Color.GRAY);
			g.drawRect(this.getSize().width - 11, 3, 7, 7);
		}
	}
	
}
