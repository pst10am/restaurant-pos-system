package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class DlgHTML extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private String usrRsp = "NA";
	private JEditorPane ta1;
	private Button btClose;
	
	// constructor
	
	public DlgHTML(Frame _pr) {
		super(_pr, "XXX", true);
		initComponents();
	}
	
	public DlgHTML(Dialog _pr) {
		super(_pr, "XXX", true);
		initComponents();
	}
	
	// private
	
	private void initComponents() {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		ta1 = new JEditorPane();
		ta1.setEditable(false);
		ta1.setContentType("text/html");
		//ta1.setFont(StdFont.Fnt16);
		ta1.setBorder(null);
		JScrollPane scp1 = new JScrollPane(ta1);
		scp1.setBorder(null);
		
		this.getContentPane().add(scp1, BorderLayout.CENTER);

		//btShift = Button.newButton("Shift,bt_shift", this);
		//btSettle = Button.newButton("Settle,bt_settle", this);
		btClose = Button.newButton("Close,bt_close", this);

		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		//pnCmd.add(btShift);
		//pnCmd.add(btSettle);
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.add(btClose);
		pnCmd.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

		this.add(pnCmd, BorderLayout.PAGE_END);
		
		this.setResizable(true);
		this.pack();
		this.setSize(520, 450);
		this.setLocationRelativeTo(this.getParent());
	}
	
	private void disposeDialog() {
		this.dispose();
	}
	
	// public
	
	public void showDialog(final File _htmlFile, String _title) {
		this.setTitle(_title);
		ta1.setText("Loading ... ");
		Thread trd1 = new Thread() {
			public void run() {
				try {
					ta1.setText("Done");
					ta1.setPage(_htmlFile.toURI().toURL());
				} catch (IOException e) {
					ta1.setText(e.getMessage());
				}
			}
		};
		trd1.start();
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}
	
	public void showDialog(String htmlStr, String _title) {
		this.setTitle(_title);
		ta1.setText(htmlStr);
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}

	public String getUsrRsp() {
		return usrRsp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		usrRsp = e.getActionCommand();
		if ("bt_close".equals(usrRsp)) {
			disposeDialog();
		} else if ("bt_shift".equals(usrRsp)) {
			disposeDialog();
		} else if ("bt_settle".equals(usrRsp)) {
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
		
		DlgHTML dlgPay = new DlgHTML(frm1);
		dlgPay.showDialog(new java.io.File("tmp/sale_rpt_2015413_115835.html"), "test html viewer");
		
		System.exit(0);
	}
}
