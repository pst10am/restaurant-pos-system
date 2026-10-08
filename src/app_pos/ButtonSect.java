package app_pos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JToggleButton;
import resrc.StdFont;

import model.TxSct;

public class ButtonSect extends JToggleButton {
	private static final long serialVersionUID = 1L;

	private ButtonModel btmd;
	{
		btmd = this.getModel();
		this.setFont(StdFont.Fnt20);
		this.setFocusable(false);
	}
	
	private TxSct sctObj;
	
	private ButtonSect(TxSct _sct, boolean _flg, ActionListener _lst) {
		super();
		sctObj = _sct;
		this.setText(sctObj.getSctName());
		this.setFocusable(false);
		this.setSelected(_flg);
		this.setFont(StdFont.Fnt16);
		this.addActionListener(_lst);
	}
	
	public static ButtonSect newButton(TxSct _sct, boolean _flg, ActionListener _lst) {
		ButtonSect bt1 = new ButtonSect(_sct, _flg, _lst);
		bt1.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY),
			BorderFactory.createEmptyBorder(10, 15, 10, 15)));
		return bt1;
	}
	
	public TxSct getSection() {
		return sctObj;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (btmd.isSelected()) {
			this.setForeground(Color.WHITE);
		} else {
			this.setForeground(Color.BLACK);
		}
	}
}
