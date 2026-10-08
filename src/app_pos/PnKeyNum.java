package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import resrc.StdFont;

public class PnKeyNum extends JPanel {
	private static final long serialVersionUID = 1L;
	
	// -----constructor-----

	private PnKeyNum(ActionListener _lst, boolean _withBorder) {
		super();
		this.setLayout(new BorderLayout());
		this.setBackground(Color.decode("#787878"));
		initComponents(_lst, _withBorder);
	}
	
	// -----factory-----

	public static PnKeyNum newPanelWithBorder(ActionListener _lst) {
		return new PnKeyNum(_lst, true);
	}

	public static PnKeyNum newPanelNoBorder(ActionListener _lst) {
		return new PnKeyNum(_lst, false);
	}
	
	// -----private-----
	
	private void initComponents(ActionListener _lst, boolean _withBorder) {
		JPanel pnKey = new JPanel();
		pnKey.setLayout(new GridLayout(0, 3));
		String[] keyTxts = {"7","8","9","4","5","6","1","2","3","0",".","BkSp"};
		for (String kt1 : keyTxts) {
			pnKey.add(crBtKey(kt1, _lst));
		}
		pnKey.setOpaque(false);
		if (_withBorder) {
			pnKey.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(7, 0, 7, 0),
				BorderFactory.createMatteBorder(1, 1, 0, 0, Color.GRAY)));
		} else {
			pnKey.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.GRAY));
		}
		JPanel _pn = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		_pn.setOpaque(false);
		_pn.add(pnKey);
		this.add(_pn, BorderLayout.CENTER);
		//this.add(Box.createHorizontalStrut(35), BorderLayout.LINE_END);
		//this.add(Box.createHorizontalStrut(35), BorderLayout.LINE_START);
		//this.setPreferredSize(this.getPreferredSize());
		//this.setMaximumSize(this.getPreferredSize());
		//this.setMinimumSize(this.getPreferredSize());
	}
	
	private JButton crBtKey(String txt, ActionListener _lst) {
		JButton bt1 = new JButton(txt);
		if ("BkSp".equals(txt)) {
			bt1.setFont(StdFont.Fnt18);
		} else {
			bt1.setFont(StdFont.Fnt28);
		}
		bt1.setFocusable(false);
		bt1.setActionCommand(String.format("key_%s", txt));
		bt1.addActionListener(_lst);
		bt1.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY), 
				BorderFactory.createEmptyBorder(15, 15, 15, 15)));
		return bt1;
	}
}
