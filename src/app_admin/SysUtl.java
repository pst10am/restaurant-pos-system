package app_admin;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import resrc.ResUtil;
import resrc.StdFont;

public class SysUtl {
	private SysUtl() {}
	
	static ImageIcon getIcon(String url) {
		return new ImageIcon(ResUtil.getResUrl(url), "-error-image-not-found-");
	}
	
	static JToolBar crToolBar(String[][] menuDef, ActionListener actLst) {
		JToolBar tlb1 = new JToolBar();
		tlb1.setFloatable(false);
		for (String[] mnu1 : menuDef) {
			if ("gap".equals(mnu1[0])) {
				tlb1.add(Box.createHorizontalGlue());
				continue;
			}
			JButton bt1 = new JButton(getIcon(mnu1[0]));
			bt1.setToolTipText(mnu1[1]);
			bt1.setActionCommand(mnu1[2]);
			bt1.addActionListener(actLst);
			bt1.setFocusable(false);
			tlb1.add(bt1);
		}
		
		/*
		tlb1.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY),
			BorderFactory.createEmptyBorder(2, 2, 2, 2)));
		*/
		tlb1.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
		return tlb1;
	}
	
	static JPanel crTitle(String txt) {
		
		JLabel lbTitle = new JLabel(txt);
		lbTitle.setFont(StdFont.Fnt14B);
		lbTitle.setHorizontalAlignment(SwingConstants.LEFT);
		lbTitle.setForeground(Color.WHITE);
		
		JPanel pn1 = new JPanel();
		pn1.setLayout(new BoxLayout(pn1, BoxLayout.LINE_AXIS));
		pn1.setBackground(Color.DARK_GRAY);
		
		pn1.add(lbTitle);
		pn1.add(Box.createHorizontalGlue());
		
		pn1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		return pn1;
	}
}
