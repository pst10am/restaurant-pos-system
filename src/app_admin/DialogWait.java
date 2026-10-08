package app_admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.text.DefaultCaret;

public class DialogWait extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private JTextArea ta1;
	private JButton btClose;
	
	DialogWait(Frame _pr, String _title) {
		super(_pr, _title, true);
		initComponents();
	}
	
	private void initComponents() {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		ta1 = new JTextArea();
		ta1.setFont(new Font("monospaced", Font.PLAIN, 14));
		ta1.setEditable(false);
		ta1.setMargin(new Insets(5, 5, 5, 5));
		DefaultCaret caret = (DefaultCaret)ta1.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		JScrollPane scp1 = new JScrollPane(ta1);
		scp1.setPreferredSize(new Dimension(320, 140));
		scp1.setBorder(null);
		this.getContentPane().add(scp1, BorderLayout.CENTER);
		//
		JPanel pnCmd = new JPanel(new FlowLayout(FlowLayout.TRAILING, 0, 0));
		btClose = new JButton("Close");
		btClose.setActionCommand("bt_close");
		btClose.addActionListener(this);
		pnCmd.add(btClose);
		pnCmd.setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY), 
				BorderFactory.createEmptyBorder(5,5,5,5)));
		this.getContentPane().add(pnCmd, BorderLayout.PAGE_END);
		//
		this.pack();
	}
	
	//
	
	void setText(String txt) {
		ta1.setText(txt);
	}
	
	void append(String txt) {
		ta1.append(txt);
	}
	
	void done() {
		btClose.setEnabled(true);
	}
	
	void showDialog(Thread trd1) {
		btClose.setEnabled(false);
		trd1.start();
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}
	
	void disposeDialog() {
		this.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ("bt_close".equals(cmd)) {
			disposeDialog();
		}
	}
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		javax.swing.JFrame frm1 = new javax.swing.JFrame("Test");
		frm1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frm1.pack();
		frm1.setSize(1024, 768);
		frm1.setLocation(100, 100);
		frm1.setVisible(true);
		
		final DialogWait dlgw = new DialogWait(frm1, "Test Wait Dialog ... ");
		Thread trd1 = new Thread() {
			public void run() {
				dlgw.setText("start ... ");
				try {
					for (int i=0; i < 50; i++) {
						dlgw.append(String.format(">> %d\n", i));
							Thread.sleep(200);
					}
				} catch (InterruptedException e) {
					dlgw.append("error..\n");
					dlgw.append(e.getMessage());
				}
				dlgw.append("done\n");
				dlgw.done();
			}
		};
		dlgw.showDialog(trd1);
		
		System.exit(0);
	}
}
