package app_pos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JToggleButton;
import javax.swing.border.Border;

import resrc.StdFont;

public class ButtonToGo extends JToggleButton {
	private static final long serialVersionUID = 1L;
	
	private static final Border _normal = BorderFactory.createCompoundBorder(
		BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY),
		BorderFactory.createEmptyBorder(10, 15, 10, 15));
			
	private ButtonModel btmd;
	{
		btmd = this.getModel();
		this.setFont(StdFont.Fnt20);
		this.setFocusable(false);
	}
	
	private ButtonToGo(String title, String cmd, ActionListener lst) {
		super();
		this.setText(title);
		this.setBorder(_normal);
		this.setActionCommand(cmd);
		this.addActionListener(lst);
	}
	
	public static ButtonToGo newButton(String title, String cmd, ActionListener lst) {
		ButtonToGo bt1 = new ButtonToGo(title, cmd, lst);
		return bt1;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (btmd.isSelected()) {
			this.setForeground(Color.WHITE);
		} else {
			this.setForeground(Color.BLACK);
			this.setBorder(_normal);
		}
	}
}
