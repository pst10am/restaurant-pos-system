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
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import model.TbCust;
import resrc.StdFont;

public class DlgCstCode extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private String usrRsp = "NA";
	private TbCust usrObj = null;
	private JTextField fldCode = null;
	
	// -----------------------------------------
	
	public DlgCstCode(Frame prFrm) {
		super(prFrm, "Customer Code?", true);
		initComponents();
	}
	
	public DlgCstCode(Dialog prDlg) {
		super(prDlg, "Customer Code?", true);
		initComponents();
	}
	
	private void initComponents() {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		JPanel pnCt = new JPanel(new BorderLayout());
		
		fldCode = new JTextField();
		fldCode.setHorizontalAlignment(JTextField.CENTER);
		fldCode.setFont(StdFont.Fnt22);
		fldCode.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		fldCode.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {
	            int kcode = e.getKeyCode();
	            if (KeyEvent.VK_ENTER == kcode) {
	            	if (foundCustomer()) {
	            		usrRsp = "bt_ok";
	            		disposeDialog();
	            	}
	            }
		    }
		}); 
		
		pnCt.add(fldCode, BorderLayout.PAGE_START);
		
		// Key
		
		PnKeyNum pnKey = PnKeyNum.newPanelWithBorder(this);
		pnCt.add(pnKey, BorderLayout.CENTER);
		
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
		pnCt.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, Color.decode("#0099CC")));
		
		this.getContentPane().add(pnCt, BorderLayout.CENTER);
		
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		this.pack();
	}
	
	private void _clearValue() {
		fldCode.setText("");
	}
	
	private void _updateValue(String val) {
		String code1 = fldCode.getText();
		if ("BkSp".equals(val)) {
			if (code1.isEmpty()) {
				return;
			}
			code1 = code1.substring(0, code1.length()-1);
		} else {
			code1 += val;
		}
		fldCode.setText(code1);
	}
	
	private void disposeDialog() {
		this.dispose();
	}
	
	private boolean foundCustomer() {
		String code1 = fldCode.getText().trim();
		if (code1.isEmpty()) {
			return false;
		}
		usrObj = TbCust.findCustomerByCode(code1);
		return null != usrObj;
	}
	
	// ----------------------------------
	
	public void showDialog() {
		usrRsp = "NA";
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}
	
	public TbCust getCustomer() {
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
			if (foundCustomer()) {
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
		
		DlgCstCode dlg1 = new DlgCstCode(frm1);
		dlg1.showDialog();
		if ("bt_ok".equals(dlg1.getUsrRsp())) {
			System.out.printf(">>> [%s]\n", dlg1.getCustomer().getCstName());
		}
		
		System.exit(0);
	}
}
