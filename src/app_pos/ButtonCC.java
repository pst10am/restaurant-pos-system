package app_pos;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import resrc.StdFont;

public class ButtonCC extends JButton {
	private static final long serialVersionUID = 1L;
	
	private static final Border _normal = BorderFactory.createCompoundBorder(
		BorderFactory.createMatteBorder(2, 2, 2, 2, Color.GRAY),
		BorderFactory.createEmptyBorder(5, 5, 5, 5));
	
	private static final Border _selected = BorderFactory.createCompoundBorder(
		BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED),
		BorderFactory.createEmptyBorder(5, 5, 5, 5));
	
	private boolean selected = false;
	
	private ButtonCC(String title, String cmd, ActionListener lst) {
		super(title);
		
		this.setFont(StdFont.Fnt18);
		this.setFocusable(false);
		this.setText(title);
		this.setActionCommand(cmd);
		if (null != lst) {
			this.addActionListener(lst);
		}
		this.setHorizontalAlignment(SwingConstants.RIGHT);
		this.setForeground(Color.BLACK);
		this.setBackground(Color.WHITE);
		this.setBorder(_normal);
		
		//this.setPreferredSize(new Dimension(235, this.getPreferredSize().height));
	}
	
	public static ButtonCC newButton(String title, String cmd, ActionListener lst) {
		return new ButtonCC(title, cmd, lst);
	}
	
	public static JPanel panelButtonCC(String _title, ButtonCC bt1) {

		bt1.setMaximumSize(new Dimension(Short.MAX_VALUE, bt1.getPreferredSize().height));
		bt1.setAlignmentX(LEFT_ALIGNMENT);
		
		JLabel lb1 = new JLabel(_title);
		lb1.setFont(bt1.getFont());
		lb1.setAlignmentX(LEFT_ALIGNMENT);
		
		JPanel pn1 = new JPanel(); // new FlowLayout(FlowLayout.TRAILING, 0, 0));
		pn1.setLayout(new BoxLayout(pn1, BoxLayout.PAGE_AXIS));
		pn1.add(lb1);
		//pn1.add(Box.createHorizontalStrut(10));
		pn1.add(Box.createVerticalStrut(2));
		pn1.add(bt1);
		pn1.setMaximumSize(new Dimension(Short.MAX_VALUE, pn1.getPreferredSize().height));
		pn1.setAlignmentX(LEFT_ALIGNMENT);
		pn1.setOpaque(false);
		
		return pn1;
	}
	
	public void setSelected(boolean value) {
		selected = value;
		this.setBorder(selected ? _selected : _normal);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
