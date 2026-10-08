package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import model.CallInfo;
import resrc.CallerID;
import resrc.StdFont;

public class DlgSelectCaller extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;

	private static DlgSelectCaller dlgCid = null;
	private int lineNum = 2;
	private int selLine = 0;
	private String usrRsp = "NA";
	private JButton[] btTels;
	
	private DlgSelectCaller(Frame _pr) {
		super(_pr, "Select Phone Line or Manual Input Number :-)", true);
		initComponents();
	}
	
	public static DlgSelectCaller getInstance(Frame _pr) {
		if (null == dlgCid) {
			dlgCid = new DlgSelectCaller(_pr);
		}
		return dlgCid;
	}
	
	private void initComponents() {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		JPanel pnLine = new JPanel();
		pnLine.setLayout(new BoxLayout(pnLine, BoxLayout.PAGE_AXIS));
		
		btTels = new JButton[lineNum];
		
		Icon phoneIcon = new ImageIcon("img/Talk1.png");
		
		for (int k=0; k < lineNum; k++) {
			btTels[k] = new JButton(String.format("Line %d : -No Calls-", k+1), phoneIcon);
			btTels[k].setHorizontalAlignment(SwingConstants.LEFT);
			btTels[k].setIconTextGap(10);
			btTels[k].setFont(StdFont.Fnt18);
			btTels[k].setFocusable(false);
			btTels[k].setActionCommand(String.format("select_line_%d", k+1));
			btTels[k].addActionListener(this);
			btTels[k].setAlignmentX(LEFT_ALIGNMENT);
			btTels[k].setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#336699")), 
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
			btTels[k].setMaximumSize(new Dimension(Short.MAX_VALUE, btTels[k].getPreferredSize().height));
			pnLine.add(btTels[k]);
		}
		
		JButton btNo = new JButton("Manual Input Number");
		btNo.setHorizontalAlignment(SwingConstants.LEFT);
		btNo.setFont(StdFont.Fnt18B);
		btNo.setFocusable(false);
		btNo.setActionCommand("select_no_line");
		btNo.addActionListener(this);
		btNo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		btNo.setMaximumSize(new Dimension(Short.MAX_VALUE, btNo.getPreferredSize().height));
		pnLine.add(btNo);
		
		this.getContentPane().add(pnLine, BorderLayout.CENTER);

		Button btCancel = Button.newCancel(this);

		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.add(btCancel);
		pnCmd.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.decode("#336699")));

		this.add(pnCmd, BorderLayout.PAGE_END);
		
		this.setResizable(false);
		this.pack();
		this.setSize(450, this.getPreferredSize().height);
		this.setLocationRelativeTo(this.getParent());
	}
	
	// public
	
	public void showDialog() {
		for (int k=0; k < lineNum; k++) {
			CallInfo cr1 = CallerID.getCIDRecord(k+1);
			btTels[k].setEnabled(null != cr1);
			if (null == cr1) {
				btTels[k].setText(String.format("Line %d : -No Calls-", k+1));
				continue;
			}
			btTels[k].setText(
				String.format("Line %d : %s%s", 
					k+1,
					cr1.phone,
					cr1.name.isEmpty() ? "": String.format(" (%s)", cr1.name)));
		}
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}
	
	public CallInfo getCallInfo() {
		if (!"line_selected".equals(usrRsp) || selLine <= 0) {
			return null;
		}
		return CallerID.getCIDRecord(selLine);
	}
	
	
	public String getUsrRsp() {
		return usrRsp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		usrRsp = e.getActionCommand();
		if ("bt_cancel".equals(usrRsp)) {
			this.dispose();
		}
		if ("select_no_line".equals(usrRsp)) {
			this.dispose();
		}
		if (usrRsp.startsWith("select_line_")) {
			selLine = Integer.parseInt(usrRsp.substring(12));
			usrRsp = "line_selected";
			this.dispose();
		}
	}

}
