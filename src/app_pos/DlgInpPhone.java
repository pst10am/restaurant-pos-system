package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import resrc.ResCfg;
import resrc.ResData;
import resrc.ResUtil;
import resrc.StdFont;

public class DlgInpPhone extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private JLabel lbData;
	private String usrRsp = "NA";
	private String orgVal = "";
	
	// -----------------------------------------
	
	public DlgInpPhone(Frame prFrm) {
		super(prFrm, "Phone No.", true);
		initComponents();
	}
	
	public DlgInpPhone(Dialog prDlg) {
		super(prDlg, "Phone No.", true);
		initComponents();
	}
	
	private static final String chkKeys = "0123456789";
	private void initComponents() {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
	            char c = e.getKeyChar();
	            if (chkKeys.indexOf(c) < 0) {
	            	return;
	            }
            	updateValue(Character.toString(c));
		    }
			public void keyPressed(KeyEvent e) {
		    }
			public void keyReleased(KeyEvent e) {
	            int kcode = e.getKeyCode();
	            if (kcode == 8) { // backspace
	            	updateValue("BkSp");
	            }
		    }
		});
		
		JPanel pnTop = new JPanel(new BorderLayout());
		
			JPanel pnTitle = new JPanel();
			pnTitle.setLayout(new BoxLayout(pnTitle, BoxLayout.LINE_AXIS));
			
				JLabel lbTitle = new JLabel("Phone No.?");
				lbTitle.setFont(StdFont.Fnt18);
				lbTitle.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 65));
			pnTitle.add(lbTitle);
			
			pnTitle.add(Box.createHorizontalGlue());
			
			pnTitle.add(UIFactory.buttonD("Clear", "bt_clear", this));
			String[] areas = ResCfg.getPhoneAreaCodes();
			for (String area1 : areas) {
				pnTitle.add(UIFactory.buttonD(area1, String.format("bt_area_%s", area1), this));
			}
			
			pnTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
		
		pnTop.add(pnTitle, BorderLayout.PAGE_START);
		
			lbData = new JLabel("-");
			lbData.setFont(StdFont.Fnt24);
			lbData.setHorizontalAlignment(SwingConstants.CENTER);
			lbData.setBackground(Color.WHITE);
			lbData.setForeground(Color.BLACK);
			lbData.setOpaque(true);
			lbData.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
		
		pnTop.add(lbData, BorderLayout.CENTER);
		
		this.getContentPane().add(pnTop, BorderLayout.PAGE_START);
		
		// Key + Find
		
		JPanel pnCt = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		pnCt.setBackground(Color.decode("#669999"));
		
			PnKeyPhone pnKey = new PnKeyPhone(this);
			pnKey.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createEmptyBorder(10, 0, 10, 0), 
					BorderFactory.createMatteBorder(1, 1, 0, 0, Color.GRAY)));
			pnKey.setOpaque(false);
		
		pnCt.add(Box.createHorizontalStrut(55), BorderLayout.LINE_END);
		pnCt.add(pnKey, BorderLayout.CENTER);
		pnCt.add(Box.createHorizontalStrut(55), BorderLayout.LINE_START);
		pnCt.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.decode("#476B6B")));
		
		this.getContentPane().add(pnCt, BorderLayout.CENTER);
		
		// Command
		
		Button btReset = Button.newButton("Reset,bt_reset", this);
		Button btOk = Button.newOk(this);
		Button btCancel = Button.newCancel(this);

		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		pnCmd.add(btReset);
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.add(btOk);
		pnCmd.add(btCancel);

		this.getContentPane().add(pnCmd, BorderLayout.PAGE_END);
		
		this.setResizable(false);
		this.pack();
	}
	
	private void _showDialog(Component _parent) {
		this.setLocationRelativeTo(_parent);
		usrRsp = "NA";
		this.setVisible(true);
	}
	
	private void _resetValue() {
		_showValue(orgVal);
	}
	
	private void _showValue(String raw) {
		lbData.setText(ResUtil.formatPhone(raw));
	}
	
	private void updateValue(String val) {
		String rawDt = lbData.getText().replaceAll("-", "").replaceAll("_", "");
		if ("BkSp".equals(val)) {
			if (rawDt.length() > 0) {
				rawDt = rawDt.substring(0, rawDt.length()-1);
			}
			if (rawDt.isEmpty()) {
				rawDt = "";
			}
			_showValue(rawDt);
			return;
		}
		if (rawDt.length() >= 10) {
			return;
		}
		String val1 = rawDt+val;
		_showValue(val1);
	}
	
	private boolean isOkayToClose() {
		String rawDt = lbData.getText().replaceAll("-", "").replaceAll("_", "");
		return rawDt.length() == 10;
	}
	
	// ----------------------------------
	
	public void showDialog(Component _parent, String _value) {
		orgVal = _value;
		_resetValue();
		_showDialog(_parent);
	}
	
	public String getPhoneValue() {
		String v1 = lbData.getText().replaceAll("-", "");
		return v1.replaceAll("_", "");
	}
	
	public String getUsrRsp() {
		return usrRsp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		usrRsp = e.getActionCommand();
		if ("bt_reset".equals(usrRsp)) {
			_resetValue();
		} else if ("bt_clear".equals(usrRsp)) {
			_showValue("");
		} else if ("bt_cancel".equals(usrRsp)) {
			this.dispose();
		} else if ("bt_ok".equals(usrRsp)) {
			if (isOkayToClose()) {
				this.dispose();
			}
		}
		
		if (usrRsp.startsWith("bt_area_")) {
			_showValue(usrRsp.substring(8));
			return;
		}
		
		if (!usrRsp.startsWith("key_")) {
			return;
		}
		updateValue(usrRsp.substring(4));
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ResData.status();
		try {
			//UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
		} catch (Exception e) {} 
		
		javax.swing.JFrame frm1 = new javax.swing.JFrame("Test Dialog");
		frm1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		javax.swing.JTextArea txt1 = new javax.swing.JTextArea();
		javax.swing.JScrollPane scp1 = new javax.swing.JScrollPane(txt1);
		frm1.getContentPane().add(scp1, BorderLayout.CENTER);
		
		frm1.pack();
		frm1.setSize(1024, 768);
		frm1.setLocationRelativeTo(null);
		frm1.setVisible(true);
		
		DlgInpPhone dlg1 = new DlgInpPhone(frm1);
		dlg1.showDialog(frm1, "5555550100");
		if ("bt_ok".equals(dlg1.getUsrRsp())) {
			System.out.printf(">>> phone no. [%s]\n", dlg1.getPhoneValue());
		}
		
		System.exit(0);
	}
}
