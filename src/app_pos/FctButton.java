package app_pos;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import resrc.StdFont;

public class FctButton {

	private FctButton() {}
	
	public static JButton newBtGiftNo(String txt, String cmd, ActionListener lst) {
		JButton bt1 = new JButton(txt);
		bt1.setFocusable(false);
		bt1.setFont(StdFont.Fnt20B);
		bt1.setHorizontalAlignment(SwingConstants.LEFT);
		bt1.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY),
			BorderFactory.createEmptyBorder(15, 20, 15, 20)));
		bt1.setBackground(Color.decode("#006600"));
		bt1.setForeground(Color.WHITE);
		bt1.setActionCommand(cmd);
		bt1.addActionListener(lst);
		return bt1;
	}
}
