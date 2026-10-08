package app_pos;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.border.Border;

import refx.ButtonType;
import resrc.StdFont;

public class Button extends JButton {
	private static final long serialVersionUID = 1L;
	
	private static final Border _NormalPressed = BorderFactory.createCompoundBorder(
		BorderFactory.createMatteBorder(3, 3, 3, 3, Color.decode("#0066CC")),
		BorderFactory.createEmptyBorder(5, 7, 3, 5));
	
	private static final Border _NormalNeutral = BorderFactory.createCompoundBorder(
		BorderFactory.createRaisedBevelBorder(),
		BorderFactory.createEmptyBorder(5, 7, 5, 5));
	
	private static final Border _OkPressed = BorderFactory.createCompoundBorder(
		BorderFactory.createMatteBorder(3, 3, 3, 3, Color.GREEN),
		BorderFactory.createEmptyBorder(5, 7, 3, 5));
	
	private static final Border _OkNeutral = BorderFactory.createCompoundBorder(
		BorderFactory.createRaisedBevelBorder(),
		BorderFactory.createEmptyBorder(5, 7, 5, 5));
	
	private static final Border _CancelPressed = BorderFactory.createCompoundBorder(
		BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED),
		BorderFactory.createEmptyBorder(5, 7, 3, 5));
	
	private static final Border _CancelNeutral = BorderFactory.createCompoundBorder(
		BorderFactory.createRaisedBevelBorder(),
		BorderFactory.createEmptyBorder(5, 7, 5, 5));

	private static int defw = 65;
	private static int defh = 55;
	
	private ButtonType btType = ButtonType.Normal;
	private ButtonModel btmd;
	{
		btType = ButtonType.Normal;
		btmd = this.getModel();
		setFont(StdFont.Fnt14);
		setFocusable(false);
		setVerticalAlignment(BOTTOM);
		setHorizontalAlignment(LEFT);
	}

	private Button() {}
	
	public static Button newButton(String _cfg, ActionListener _lst) {
		return _newButton(_cfg, _lst, defw, defh);
	}
	
	public static Button newButtonSizeToText(String _cfg, ActionListener _lst) {
		return _newButton(_cfg, _lst);
	}
	
	public static Button newOk(ActionListener _lst) {
		Button bt1 = _newButton("Ok,bt_ok", _lst, defw, defh);
		bt1.setFont(StdFont.Fnt14B);
		bt1.setForeground(Color.decode("#009900"));
		bt1.btType = ButtonType.Ok;
		return bt1;
	}
	
	public static Button newCancel(ActionListener _lst) {
		Button bt1 = _newButton("Cancel,bt_cancel", _lst, defw+15, defh);
		bt1.setFont(StdFont.Fnt14B);
		bt1.setForeground(Color.RED);
		bt1.btType = ButtonType.Cancel;
		return bt1;
	}
	
	private static Button _newButton(String _cfg, ActionListener _lst, int w, int h) {
		String[] cfg1s = _cfg.split("[,]");
		Button bt1 = new Button();
		//
		bt1.setPreferredSize(new Dimension(w, h));
		bt1.setMinimumSize(bt1.getPreferredSize());
		bt1.setMaximumSize(bt1.getPreferredSize());
		bt1.setText(cfg1s[0]);
		bt1.setActionCommand(cfg1s[1]);
		bt1.addActionListener(_lst);
		//
		return bt1;
	}
	
	private static Button _newButton(String _cfg, ActionListener _lst) {
		String[] cfg1s = _cfg.split("[,]");
		Button bt1 = new Button();
		//
		bt1.setText(cfg1s[0]);
		bt1.setPreferredSize(new Dimension(bt1.getPreferredSize().width, defh));
		bt1.setMinimumSize(bt1.getPreferredSize());
		bt1.setMaximumSize(bt1.getPreferredSize());
		bt1.setActionCommand(cfg1s[1]);
		bt1.addActionListener(_lst);
		//
		return bt1;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (ButtonType.Ok == btType) {
			if (btmd.isPressed()) {
				this.setBorder(_OkPressed);
			} else {
				this.setBorder(_OkNeutral);
			}
			return;
		}
		if (ButtonType.Cancel == btType) {
			if (btmd.isPressed()) {
				this.setBorder(_CancelPressed);
			} else {
				this.setBorder(_CancelNeutral);
			}
			return;
		}
		// Normal
		if (btmd.isPressed()) {
			this.setBorder(_NormalPressed);
		} else {
			this.setBorder(_NormalNeutral);
		}
	}
}
