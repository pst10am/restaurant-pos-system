package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
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

import resrc.StdFont;

public class DlgInpNum extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private JLabel lbTitle, lbData;
	private KeyInputType ktype;
	private String usrRsp = "NA";
	
	private int orgIntVal = 0;
	private double orgDblVal = 0;
	private String orgStrVal = "";
	
	private boolean clearFlg = false;
	
	// -----------------------------------------
	
	public DlgInpNum(Frame prFrm) {
		super(prFrm, "Keyboard", true);
		initComponents();
	}
	
	public DlgInpNum(Dialog prDlg) {
		super(prDlg, "Keyboard", true);
		initComponents();
	}
	
	private static final String chkKeys = "0123456789.";
	private void initComponents() {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
	            char c = e.getKeyChar();
	            if (chkKeys.indexOf(c) < 0) {
	            	return;
	            }
            	_updateValue(Character.toString(c));
		    }
			public void keyPressed(KeyEvent e) {
		    }
			public void keyReleased(KeyEvent e) {
	            int kcode = e.getKeyCode();
	            if (kcode == 8) { // backspace
	            	_updateValue("BkSp");
	            }
		    }
		});
		
		JPanel pnTop = new JPanel(new BorderLayout());
		lbTitle = new JLabel("XX");
		lbTitle.setFont(StdFont.Fnt18);
		lbTitle.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));
		pnTop.add(lbTitle, BorderLayout.PAGE_START);
		
		lbData = new JLabel("-");
		lbData.setFont(StdFont.Fnt22);
		lbData.setHorizontalAlignment(SwingConstants.RIGHT);
		lbData.setBackground(Color.WHITE);
		lbData.setForeground(Color.BLACK);
		lbData.setOpaque(true);
		lbData.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 3));
		lbData.setPreferredSize(new Dimension(315, lbData.getPreferredSize().height));
		
		JPanel _pn1 = new JPanel(new FlowLayout(FlowLayout.TRAILING, 0, 0));
		//_pn1.add(Box.createVerticalStrut(lbData.getPreferredSize().height));
		_pn1.add(lbData);
		_pn1.setBackground(Color.WHITE);
		_pn1.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY));
		pnTop.add(_pn1, BorderLayout.CENTER);
		
		this.add(pnTop, BorderLayout.PAGE_START);
		
		// Key
		
		PnKeyNum pnKey = PnKeyNum.newPanelWithBorder(this);
		this.add(pnKey, BorderLayout.CENTER);
		
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

		this.add(pnCmd, BorderLayout.PAGE_END);
		
		this.setResizable(false);
		this.pack();
	}
	
	private void _showDialog(String _title) {
		this.setLocationRelativeTo(this.getParent());
		this.setTitle(_title);
		lbTitle.setText(_title);
		usrRsp = "NA";
		this.setVisible(true);
	}
	
	private void _resetValue() {
		if (KeyInputType.Integer == ktype) {
			lbData.setText(String.format("%d", orgIntVal));
		} else if (KeyInputType.Double == ktype) {
			lbData.setText(String.format("%.2f", orgDblVal));
		} else {
			lbData.setText(orgStrVal);
		}
		clearFlg = true;
		lbData.setBackground(Color.decode("#A3C2FF"));
	}
	
	private boolean _isValidNum(String num) {
		if (KeyInputType.Any == ktype) return true;
		try {
			Double.parseDouble(num);
			return true;
		} catch (Exception e) {}
		return false;
	}
	
	private String _cleanValue(String raw) {
		if (KeyInputType.Any == ktype) return raw;
		int ix1 = -1;
		for (int i=0; i < raw.length(); i++) {
			if ((ix1 == -1) && raw.charAt(i) == '0') {
				continue;
			}
			ix1 = i;
			break;
		}
		if (ix1 == -1) {
			return "0";
		}
		if (raw.charAt(ix1) == '.') {
			return "0"+raw.substring(ix1);
		}
		return raw.substring(ix1);
	}
	
	private void _updateValue(String val) {
		if (KeyInputType.Integer == ktype) {
			if (".".equals(val)) {
				return;
			}
		}
		if (clearFlg) {
			if ("BkSp".equals(val)) {
				lbData.setText("0");
			} else {
				lbData.setText(val);
			}
			clearFlg = false;
			lbData.setBackground(Color.WHITE);
			return;
		}
		if ("BkSp".equals(val)) {
			String dtclr = lbData.getText();
			dtclr = dtclr.substring(0, dtclr.length()-1);
			if (dtclr.isEmpty()) {
				if (KeyInputType.Any == ktype) {
					dtclr = "";
				} else {
					dtclr = "0";
				}
			}
			lbData.setText(_cleanValue(dtclr));
			return;
		}
		String dt0 = lbData.getText() + val;
		if (_isValidNum(dt0)) {
			lbData.setText(_cleanValue(dt0));
		}
	}
	
	// ----------------------------------
	
	public void showDialog(String _title, int _value) {
		orgIntVal = _value;
		ktype = KeyInputType.Integer;
		_resetValue();
		_showDialog(_title);
	}
	
	public void showDialog(String _title, double _value) {
		orgDblVal = _value;
		ktype = KeyInputType.Double;
		_resetValue();
		_showDialog(_title);
	}
	
	public void showDialog(String _title, String _value) {
		orgStrVal = _value;
		ktype = KeyInputType.Any;
		_resetValue();
		_showDialog(_title);
	}
	
	public int getIntValue() {
		return Integer.parseInt(lbData.getText());
	}
	
	public double getDoubleValue() {
		return Double.parseDouble(lbData.getText());
	}
	
	public String getStrValue() {
		return lbData.getText();
	}
	
	public String getUsrRsp() {
		return usrRsp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		usrRsp = e.getActionCommand();
		if ("bt_reset".equals(usrRsp)) {
			_resetValue();
		} else if ("bt_cancel".equals(usrRsp)) {
			this.dispose();
		} else if ("bt_ok".equals(usrRsp)) {
			this.dispose();
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
		
		javax.swing.JFrame frm1 = new javax.swing.JFrame("Test Key Dialog");
		frm1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		javax.swing.JTextArea txt1 = new javax.swing.JTextArea();
		javax.swing.JScrollPane scp1 = new javax.swing.JScrollPane(txt1);
		frm1.getContentPane().add(scp1, BorderLayout.CENTER);
		
		frm1.pack();
		frm1.setSize(1024, 768);
		frm1.setLocationRelativeTo(null);
		frm1.setVisible(true);
		
		DlgInpNum dlgInp = new DlgInpNum(frm1);
		//dlgInp.showDialog("Extra Charge?", 8.95);
		//dlgInp.showDialog("Qty?", 1);
		dlgInp.showDialog("Card No?", "");
		if ("bt_ok".equals(dlgInp.getUsrRsp())) {
			//System.out.printf(">>> [%f]\n", dlgInp.getDoubleValue());
			//System.out.printf(">>> [%d]\n", dlgInp.getIntValue());
			System.out.printf(">>> [%s]\n", dlgInp.getStrValue());
		}
		
		System.exit(0);
	}
}
