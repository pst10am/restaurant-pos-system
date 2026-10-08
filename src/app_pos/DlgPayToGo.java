package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class DlgPayToGo extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private String usrRsp = "NA";
	
	// constructor
	
	public DlgPayToGo(Frame _pr) {
		super(_pr, "Pay ... ", true);
		initComponents();
	}
	
	// private
	
	private void initComponents() {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		JButton btNow = UIFactory.crBtPayToGo("Pay Now", "pay_now", this);
		JButton btLater = UIFactory.crBtPayToGo("Pay Later", "pay_later", this);

		JPanel pnPay = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		pnPay.add(btNow);
		pnPay.add(Box.createHorizontalStrut(15));
		pnPay.add(btLater);
		
		pnPay.setBackground(Color.decode("#F2F2F2"));
		
		pnPay.setBorder(BorderFactory.createEmptyBorder(20, 35, 20, 35));
		
		this.getContentPane().add(pnPay, BorderLayout.CENTER);
		
		crPnCmd();
		
		this.setResizable(false);
		this.pack();
	}
	
	private void crPnCmd() {
		// Command
		Button btCancel = Button.newCancel(this);
		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.add(btCancel);
		pnCmd.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
		this.getContentPane().add(pnCmd, BorderLayout.PAGE_END);
	}
	
	private void disposeDialog() {
		this.dispose();
	}
	
	// public
	
	public void showDialog() {
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}

	public String getUsrRsp() {
		return usrRsp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		usrRsp = e.getActionCommand();
		if ("bt_cancel".equals(usrRsp)) {
			disposeDialog();
		} else if ("pay_later".equals(usrRsp)) {
			disposeDialog();
		} else if ("pay_now".equals(usrRsp)) {
			disposeDialog();
		}
	}
	
	// main

	public static void main(String[] args) {
		
		MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
		
		javax.swing.JFrame frm1 = new javax.swing.JFrame("Test Dialog");
		frm1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		frm1.pack();
		
		frm1.setSize(1024, 768);
		frm1.setLocationRelativeTo(null);
		frm1.setVisible(true);
		
		DlgPayToGo dlgPay = new DlgPayToGo(frm1);
		dlgPay.showDialog();
		
		System.exit(0);
	}
}
