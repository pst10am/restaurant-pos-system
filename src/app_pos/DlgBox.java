package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import refx.DlgType;
import resrc.StdFont;

public class DlgBox extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JLabel lbTxt;
	private JPanel pnMain;
	private String usrRsp = "NA";
	private Button btOk, btCancel, btClose;
	
	public DlgBox(Frame _frame) {
		super(_frame);
		this.setModal(true);
		initComponents();
	}
	
	public DlgBox(Dialog _dlg) {
		super(_dlg);
		this.setModal(true);
		initComponents();
	}
	
	private void initComponents() {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		// Label
		
		lbTxt = new JLabel("-");
		lbTxt.setFont(StdFont.Fnt22);
		lbTxt.setBackground(Color.decode("#FFD699"));
		lbTxt.setOpaque(true);
		this.getContentPane().add(lbTxt, BorderLayout.PAGE_START);
		
		pnMain = new JPanel(new BorderLayout());
		this.getContentPane().add(pnMain, BorderLayout.CENTER);
		
		// Command

		btOk = Button.newOk(this);
		btCancel = Button.newCancel(this);
		btClose = Button.newButton("Close,bt_close", this);

		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.add(btOk);
		pnCmd.add(btCancel);
		pnCmd.add(btClose);
		pnCmd.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

		this.add(pnCmd, BorderLayout.PAGE_END);
		
		this.setResizable(false);
		this.pack();
		this.setLocationRelativeTo(this.getParent());
	}
	
	private void _showDialog(String _title, String _msg, DlgType _type) {
		this.setTitle(_title);
		
		pnMain.setVisible(false);
		lbTxt.setText(_msg);
		lbTxt.setBorder(BorderFactory.createEmptyBorder(35, 50, 35, 50));
		lbTxt.setHorizontalAlignment(SwingConstants.CENTER);
		lbTxt.setBackground(_type.getBackground());
		lbTxt.setForeground(_type.getForeground());
		
		this.pack();
		this.setMinimumSize(new Dimension(390, this.getPreferredSize().height));
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}
	
	// -------------------------------------
	
	public void showConfirmDialog(String _title, String _msg, DlgType _type) {
		btOk.setVisible(true);
		btCancel.setVisible(true);
		btClose.setVisible(false);
		_showDialog(_title, _msg, _type);
	}
	
	public void showDialog(String _title, String _msg, DlgType _type) {
		btOk.setVisible(false);
		btCancel.setVisible(false);
		btClose.setVisible(true);
		_showDialog(_title, _msg, _type);
	}
	
	public String getUsrRsp() {
		return usrRsp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		usrRsp = e.getActionCommand();
		if ("bt_cancel".equals(usrRsp)) {
			this.dispose();
		} else if ("bt_ok".equals(usrRsp)) {
			this.dispose();
		} else if ("bt_close".equals(usrRsp)) {
			this.dispose();
		}
	}

	public static void main(String[] args) {
		try {
			//UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
		} catch (Exception e) {} 
		
		javax.swing.JFrame frm1 = new javax.swing.JFrame("Test Dialog");
		frm1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frm1.pack();
		frm1.setSize(1024, 768);
		frm1.setLocationRelativeTo(null);
		frm1.setVisible(true);
		
		DlgBox dlg1 = new DlgBox(frm1);
		/*
		dlg1.showConfirmDialog("Delete", "Delete [Discount 50%]?");
		if ("bt_ok".equals(dlg1.getUsrRsp())) {
			System.out.println("User okay, Go ahead!");
		}
		*/
		
		dlg1.showDialog(
			"Error", 
			"<html><center><font size=10>Card read error.</font><br>Please try again</center></html>",
			DlgType.Warning);
		if ("bt_close".equals(dlg1.getUsrRsp())) {
			System.out.println("dialog is closed.");
		}
		
		System.exit(0);
	}
}
