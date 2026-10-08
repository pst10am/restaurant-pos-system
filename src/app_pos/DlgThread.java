package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.text.DefaultCaret;

import resrc.StdFont;

public class DlgThread extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private JTextArea ta1;
	private JButton btClose;
	private int exitCode = -3;
	
	// Constructor

	public DlgThread(Frame _pr) {
		super(_pr, "Processing ... Please wait", true);
		initComponents();
	}

	public DlgThread(Dialog _pr) {
		super(_pr, "Processing ... Please wait", true);
		initComponents();
	}

	// -----private-----
	
	private void initComponents() {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		ta1 = new JTextArea();
		ta1.setFont(StdFont.Fnt16);
		ta1.setEditable(false);
		ta1.setFocusable(false);
		ta1.setMargin(new Insets(5, 5, 5, 5));
		DefaultCaret caret = (DefaultCaret)ta1.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		JScrollPane scp1 = new JScrollPane(ta1);
		scp1.setFocusable(false);
		scp1.setPreferredSize(new Dimension(365, 225));
		scp1.setBorder(null);
		
		this.getContentPane().add(scp1, BorderLayout.CENTER);
		
		// Command
		
		btClose = Button.newButton("Close,bt_close", this);

		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.add(btClose);
		pnCmd.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

		this.add(pnCmd, BorderLayout.PAGE_END);
		
		this.pack();
	}
	
	// -----public-----
	
	public void showDialog(Thread thrd1) {
		btClose.setEnabled(false);
		//
		thrd1.start();
		//
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}
	
	public void append(String txt) {
		ta1.append(txt);
	}
	public void setText(String txt) {
		ta1.setText(txt);
	}

	public void processEnd() {
		this.setTitle("Processing ... Done");
		btClose.setEnabled(true);
	}
	
	public int getExitCode() {
		return exitCode;
	}
	public void setExitCode(int value) {
		exitCode = value;
	}

	public void setTextFont(Font fnt1) {
		ta1.setFont(fnt1);
	}
	
	public void setDlgsize(int _width, int _height) {
		this.setSize(_width, _height);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ("bt_close".equals(cmd)) {
			this.dispose();
		}
	}

	// -----main-----
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
		
		javax.swing.JFrame frm1 = new javax.swing.JFrame();
		frm1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frm1.pack();
		frm1.setSize(1024, 768);
		frm1.setLocationRelativeTo(null);
		frm1.setVisible(true);
		
		final DlgThread dlg1 = new DlgThread(frm1);
		Thread trd1 = new Thread() {
			public void run() {
				dlg1.setExitCode(0);
				try {
					for (int i=1; i <= 15; i++) {
						dlg1.append(String.format("run! [%d]\n", i));
						Thread.sleep(355);
					}
					int rnd1 = (int)(Math.random() * 100);
					if (rnd1 > 55) {
						throw new Exception(String.format("random exception [%d]", rnd1));
					}
					dlg1.setExitCode(1);
					dlg1.dispose();
				} catch (Exception e) {
					dlg1.append("Error\n");
					dlg1.append(e.getMessage());
					dlg1.append("\n");
				}
				dlg1.append(String.format("Exit Code [%d]", dlg1.getExitCode()));
				dlg1.processEnd();
			}
		};
		dlg1.showDialog(trd1);
		
		System.out.printf("done with [%d]\n", dlg1.getExitCode());
		System.exit(0);
	}
}
