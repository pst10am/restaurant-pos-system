package app_pos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.border.Border;

import resrc.StdFont;
import model.TxSctTable;

public class ButtonTable extends JButton {
	private static final long serialVersionUID = 1L;
	
	private static final Border _pressed = BorderFactory.createLineBorder(Color.WHITE);
	private static final Border _normal = BorderFactory.createLineBorder(Color.DARK_GRAY);
	
	private static final Font fntSH = new Font("", Font.PLAIN, 9);
	
	private ButtonModel btmd;
	private TxSctTable obj;
	{
		btmd = this.getModel();
		obj = null;
		this.setFont(StdFont.Fnt20);
		this.setFocusable(false);
	}
	
	private ButtonTable(TxSctTable tbl1, ActionListener lst) {
		super();
		obj = tbl1;
		this.setText(obj.getTblName());
		this.setBounds(obj.getTblX(), obj.getTblY(), tbl1.getTblW(), tbl1.getTblH());
		this.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		this.setBackground(obj.getTableColor());
		this.setActionCommand("select_table");
		this.addActionListener(lst);
	}
	
	public static ButtonTable newTable(TxSctTable tbl1, ActionListener lst) {
		return new ButtonTable(tbl1, lst);
	}
	
	public TxSctTable getTable() {
		return obj;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (btmd.isPressed()) {
			this.setBorder(_pressed);
		} else {
			this.setBorder(_normal);
		}
		//g.setFont(fntSH);
		//g.setColor(Color.BLACK);
		//g.drawString("SH", 2, obj.getTblH()-3);
	}
}
