package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import resrc.StdFont;

public class UIFactory {

	private UIFactory() {}
	
	// button
	
	public static JButton buttonA(String title, String cmd, ActionListener lst) {
		JButton bt1 = new JButton(title);
		bt1.setFocusable(false);
		bt1.setActionCommand(cmd);
		bt1.addActionListener(lst);
		bt1.setFont(StdFont.Fnt18);
		return bt1;
	}
	
	public static JButton buttonB(String title, String cmd, ActionListener lst) {
		JButton bt1 = buttonA(title, cmd, lst);
		bt1.setHorizontalAlignment(SwingConstants.RIGHT);
		bt1.setPreferredSize(new Dimension(135, bt1.getPreferredSize().height));
		bt1.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY), 
			BorderFactory.createEmptyBorder(0, 0, 0, 10)));
		return bt1;
	}
	
	public static JButton buttonC(String title, String cmd, ActionListener lst) {
		JButton bt1 = buttonA(title, cmd, lst);
		bt1.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY), 
			BorderFactory.createEmptyBorder(10, 15, 10, 15)));
		return bt1;
	}
	
	public static JButton buttonD(String _txt, String _cmd, ActionListener _lst) {
		JButton bt1 = new JButton(_txt);
		bt1.setFocusable(false);
		bt1.setActionCommand(_cmd);
		bt1.addActionListener(_lst);
		bt1.setFont(StdFont.Fnt22);
		bt1.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 1, 0, 0, Color.GRAY), 
			BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		return bt1;
	}
	
	public static JButton buttonE(String _code, String _cmd, ActionListener _lst) {
		JButton bt1 = new JButton(_code);
		bt1.setFocusable(false);
		bt1.setActionCommand(_cmd);
		bt1.addActionListener(_lst);
		bt1.setFont(StdFont.Fnt22);
		bt1.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY), 
			BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		return bt1;
	}	
	
	public static JButton buttonG(String _code, String _cmd, ActionListener _lst) {
		JButton bt1 = new JButton(_code);
		bt1.setFocusable(false);
		bt1.setActionCommand(_cmd);
		bt1.addActionListener(_lst);
		bt1.setFont(StdFont.Fnt22);
		bt1.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY), 
			BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		return bt1;
	}	

	public static JButton buttonH(String _title, String _cmd, ActionListener _lst) {
		JButton btTk = new JButton(_title);
		btTk.setFocusable(false);
		btTk.setFont(StdFont.Fnt14B);
		btTk.setHorizontalAlignment(SwingConstants.LEFT);
		btTk.setActionCommand(_cmd);
		btTk.addActionListener(_lst);
		btTk.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY),
			BorderFactory.createEmptyBorder(10, 10, 10, 10)
			));
		return btTk;
	}

	// panel
	
	public static JPanel panelA(JButton bt1, JLabel lb1) {
		JPanel pn1 = new JPanel();
		pn1.setLayout(new BorderLayout());
		pn1.setOpaque(true);
		pn1.setBackground(Color.WHITE);

		pn1.add(bt1, BorderLayout.LINE_START);
		
		lb1.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		pn1.add(lb1, BorderLayout.CENTER);
		
		pn1.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
		pn1.setPreferredSize(new Dimension(590, pn1.getPreferredSize().height));
		pn1.setMinimumSize(pn1.getPreferredSize());
		pn1.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		return pn1;
	}
	
	static JButton crBtPayToGo(String _txt, String _cmd, ActionListener _lst) {
		JButton bt1 = new JButton(_txt);
		bt1.setFocusable(false);
		bt1.setFont(StdFont.Fnt28);
		bt1.setActionCommand(_cmd);
		bt1.addActionListener(_lst);
		
		bt1.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createEtchedBorder(), 
			BorderFactory.createEmptyBorder(15, 25, 15, 25)));
		
		return bt1;
	}
}
