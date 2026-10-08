package test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import resrc.CallerID;
import app_pos.DlgSelectCaller;

public class TestCallerID2 extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private JTextArea ta1;

	TestCallerID2() {
		super("Test Caller ID");
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		initComponents();
		
		this.pack();
		this.setSize(1024, 768);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	private void initComponents() {
		ta1 = new JTextArea();
		JScrollPane scp1 = new JScrollPane(ta1);
		this.getContentPane().add(scp1, BorderLayout.CENTER);
		//
		JPanel pnCmd = new JPanel();
		JButton btToGo = new JButton("+ToGo");
		btToGo.setActionCommand("bt_new_togo");
		btToGo.addActionListener(this);
		pnCmd.add(btToGo);
		
		this.getContentPane().add(pnCmd, BorderLayout.PAGE_END);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ("bt_new_togo".equals(cmd)) {
			DlgSelectCaller dlgCid = DlgSelectCaller.getInstance(this);
			dlgCid.showDialog();
		}
	}

	public static void main(String[] args) {
		MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
		CallerID cid = CallerID.getInstance();
		cid.start();
		new TestCallerID2();
	}
}
