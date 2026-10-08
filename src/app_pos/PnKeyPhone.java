package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import resrc.StdFont;

public class PnKeyPhone extends JPanel {
	private static final long serialVersionUID = 1L;

	public PnKeyPhone(ActionListener _lst) {
		super();
		this.setLayout(new BorderLayout());
		initComponents(_lst);
	}
	
	private void initComponents(ActionListener _lst) {
		JPanel pnKey = new JPanel();
		pnKey.setLayout(null);
		String[] keyTxts = {"1","2","3","4","5","6","7","8","9"};
		int x1 = 0;
		int y1 = 0;
		int w1 = 71;
		int h1 = 65;
		int cnt1 = 0;
		for (String kt1 : keyTxts) {
			if (cnt1 == 3) {
				x1 = 0;
				y1 += h1;
				cnt1 = 0;
			}
			pnKey.add(crBtKey(kt1, _lst, x1+1, y1+1, w1, h1));
			x1 += w1;
			cnt1 += 1;
		}
		y1 += h1;
		JButton bt0 = crBtKey("0", _lst, 1, y1+1, w1*2, h1);
		pnKey.add(bt0);
		JButton btBkSp = crBtKey("BkSp", _lst, (w1*2)+1, y1+1, w1, h1);
		pnKey.add(btBkSp);
		pnKey.setPreferredSize(new Dimension((w1*3)+1, (h1*4)+1));
		//pnKey.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.GRAY));
		
		//JPanel _pn = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		//_pn.add(pnKey);
		//_pn.setBorder(BorderFactory.createEmptyBorder(7, 0, 7, 0));
		//_pn.setOpaque(false);
		
		this.add(pnKey, BorderLayout.CENTER);
		//this.add(Box.createHorizontalStrut(35), BorderLayout.LINE_END);
		//this.add(Box.createHorizontalStrut(35), BorderLayout.LINE_START);
	}
	
	private JButton crBtKey(String txt, ActionListener _lst, 
		int _x, int _y, int _w, int _h) {
		JButton bt1 = new JButton(txt);
		if ("BkSp".equals(txt)) {
			bt1.setFont(StdFont.Fnt18);
		} else {
			bt1.setFont(StdFont.Fnt28);
		}
		bt1.setFocusable(false);
		bt1.setActionCommand(String.format("key_%s", txt));
		bt1.addActionListener(_lst);
		bt1.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY));
		bt1.setBounds(_x, _y, _w, _h);
		return bt1;
	}
}
