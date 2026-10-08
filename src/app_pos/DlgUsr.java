package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import model.TxUser;
import resrc.StdFont;

public class DlgUsr extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private String usrRsp = "NA";
	private TxUser usrObj = null;
	private JPasswordField pwdFld = null;
	
	// -----------------------------------------
	
	public DlgUsr(Frame prFrm) {
		super(prFrm, "[User] POS Code?", true);
		initComponents();
	}
	
	public DlgUsr(Dialog prDlg) {
		super(prDlg, "[User] POS Code?", true);
		initComponents();
	}
	
	private void initComponents() {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		JPanel pnCt = new JPanel(new BorderLayout());
		
		pwdFld = new JPasswordField();
		pwdFld.setHorizontalAlignment(JTextField.CENTER);
		pwdFld.setFont(StdFont.Fnt22);
		pwdFld.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		pwdFld.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {
	            int kcode = e.getKeyCode();
	            if (KeyEvent.VK_ENTER == kcode) {
	            	if (foundUser()) {
	            		usrRsp = "bt_ok";
	            		disposeDialog();
	            	}
	            }
		    }
		}); 
		
		pnCt.add(pwdFld, BorderLayout.PAGE_START);
		
		// Key
		
		PnKeyNum pnKey = PnKeyNum.newPanelNoBorder(this);
		
		JPanel pk2 = new JPanel();
		pk2.setBackground(Color.decode("#C1D6D6"));
		pk2.add(pnKey);
		pk2.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY), 
			BorderFactory.createEmptyBorder(10, 35, 10, 35)));
		
		pnCt.add(pk2, BorderLayout.CENTER);
		
		// Command
		
		Button btClr = Button.newButton("Clear,bt_clear", this);
		Button btOk = Button.newOk(this);
		Button btCancel = Button.newCancel(this);

		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		pnCmd.add(btClr);
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.add(btOk);
		pnCmd.add(btCancel);

		pnCt.add(pnCmd, BorderLayout.PAGE_END);
		pnCt.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, Color.decode("#CC3300")));
		
		this.getContentPane().add(pnCt, BorderLayout.CENTER);
		
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		this.pack();
	}
	
	private void _clearValue() {
		pwdFld.setText("");
	}
	
	private void _updateValue(String val) {
		String pwd1 = new String(pwdFld.getPassword());
		if ("BkSp".equals(val)) {
			if (pwd1.isEmpty()) {
				return;
			}
			pwd1 = pwd1.substring(0, pwd1.length()-1);
		} else {
			pwd1 += val;
		}
		pwdFld.setText(pwd1);
	}
	
	private void disposeDialog() {
		this.dispose();
	}
	
	private boolean foundUser() {
		String pwd1 = new String(pwdFld.getPassword());
		if (pwd1.isEmpty()) {
			return false;
		}
		usrObj = TxUser.authen(pwd1);
		return null != usrObj;
	}
	
	// ----------------------------------
	
	public void showDialog() {
		usrRsp = "NA";
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}
	
	public TxUser getUser() {
		return usrObj;
	}
	
	public String getUsrRsp() {
		return usrRsp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		usrRsp = e.getActionCommand();
		if ("bt_clear".equals(usrRsp)) {
			_clearValue();
		} else if ("bt_cancel".equals(usrRsp)) {
			disposeDialog();
		} else if ("bt_ok".equals(usrRsp)) {
			if (foundUser()) {
				disposeDialog();
			}
		}
		if (!usrRsp.startsWith("key_")) {
			return;
		}
		_updateValue(usrRsp.substring(4));
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
		
		DlgUsr dlgUsr = new DlgUsr(frm1);
		dlgUsr.showDialog();
		if ("bt_ok".equals(dlgUsr.getUsrRsp())) {
			System.out.printf(">>> [%s]\n", dlgUsr.getUser());
		}
		
		System.exit(0);
	}
}
