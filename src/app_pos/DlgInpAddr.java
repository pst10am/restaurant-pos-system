package app_pos;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import model.AddrUS;
import model.TxStreet;
import resrc.ResCfg;
import resrc.ResData;
import resrc.StdFont;

public class DlgInpAddr extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private static final int dfw1 = 115;
	
	private String usrRsp = "NA";
	private JTextField fldLine1, fldLine2, fldUnit;
	private JComboBox<String> cbxCity;
	private JComboBox<String> cbxState;
	private DefaultListModel<TxStreet> mdSt;
	private JList<TxStreet> lstSt;
	
	private AddrUS addrObj;
	
	// -----constructor-----
	
	public DlgInpAddr(Frame _pr) {
		super(_pr, "Address", true);
		initComponents();
	}
	
	public DlgInpAddr(Dialog _pr) {
		super(_pr, "Address", true);
		initComponents();
	}
	
	// -----private-----
	
	private void initComponents() {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		
		// label + data
		
		JLabel lbLine1 = new JLabel("Line 1");
		JLabel lbLine2 = new JLabel("Line 2");
		JLabel lbUnit = new JLabel("Unit/Apt");
		
		fldLine1 = new JTextField();
		fldLine1.addKeyListener(new StLst());
		
		fldLine2 = new JTextField();
		fldUnit = new JTextField();
		
		cbxCity = new JComboBox<>(ResCfg.getCityList());
		cbxCity.setFocusable(false);
		cbxCity.setFont(StdFont.Fnt20);
		
		cbxState = new JComboBox<>(ResCfg.getStateList());
		cbxState.setFocusable(false);
		cbxState.setFont(StdFont.Fnt20);
		
		JPanel pnZip = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
		pnZip.add(Box.createHorizontalStrut(dfw1));
		pnZip.add(cbxCity);
		pnZip.add(Box.createHorizontalStrut(15));
		pnZip.add(cbxState);
		
		// address fields

		JPanel pnFld = new JPanel();
		pnFld.setLayout(new BoxLayout(pnFld, BoxLayout.PAGE_AXIS));
		
		pnFld.add(crPanel(lbLine1, fldLine1));
		pnFld.add(Box.createVerticalStrut(10));
		pnFld.add(crPanel(lbUnit, fldUnit));
		pnFld.add(Box.createVerticalStrut(10));
		pnFld.add(crPanel(lbLine2, fldLine2));
		pnFld.add(Box.createVerticalStrut(10));
		pnFld.add(pnZip);
		
		JPanel pnFld2 = new JPanel(new BorderLayout());
		pnFld2.add(pnFld, BorderLayout.PAGE_START);
		pnFld2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		// street field
		
		mdSt = new DefaultListModel<>();
		lstSt = new JList<>(mdSt);
		lstSt.setFont(StdFont.Fnt20);
		lstSt.setFocusable(false);
		
		JScrollPane scpSt = new JScrollPane(lstSt, 
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scpSt.getVerticalScrollBar().setPreferredSize(
			new Dimension(25, 0)); 
		scpSt.setPreferredSize(new Dimension(335, 195));
		
		JPanel pnSt = new JPanel(new BorderLayout());
		pnSt.add(scpSt, BorderLayout.CENTER);
		
		JButton btSel = new JButton("Select");
		btSel.setFocusable(false);
		btSel.setFont(StdFont.Fnt22);
		btSel.setActionCommand("addr_select");
		btSel.addActionListener(this);
		pnSt.add(btSel, BorderLayout.PAGE_END);
		
		// keyboard
		
		PnKeyboard pnTxtPad = PnKeyboard.getInstance();
		this.add(pnTxtPad, BorderLayout.CENTER);
		
		// put everything together
		
		JPanel pnAll = new JPanel(new BorderLayout());
		pnAll.add(pnFld2, BorderLayout.CENTER);
		pnAll.add(pnSt, BorderLayout.LINE_END);
		pnAll.add(pnTxtPad, BorderLayout.PAGE_END);
		
		this.getContentPane().add(pnAll, BorderLayout.CENTER);
		
		// command
		
		Button btReset = Button.newButton("Reset,bt_reset", this);
		Button btClear = Button.newButton("Clear,bt_clear", this);
		Button btOk = Button.newOk(this);
		Button btCancel = Button.newCancel(this);

		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		pnCmd.add(btReset);
		pnCmd.add(btClear);
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.add(btOk);
		pnCmd.add(btCancel);

		this.add(pnCmd, BorderLayout.PAGE_END);
		this.pack();
		
		TxStreet.init();
	}
	
	private static JPanel crPanel(JLabel lb1, JTextField fld1) {
		JPanel pn1 = new JPanel(new BorderLayout());
		
		lb1.setPreferredSize(new Dimension(dfw1, lb1.getPreferredSize().height));
		lb1.setHorizontalAlignment(SwingConstants.RIGHT);
		lb1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		lb1.setFont(StdFont.Fnt20);
		
		fld1.setFont(StdFont.Fnt20);
		
		pn1.add(lb1, BorderLayout.LINE_START);
		pn1.add(fld1, BorderLayout.CENTER);
		return pn1;
	}
	
	private void _updateLine1() {
		TxStreet sif = lstSt.getSelectedValue();
		String curTxt = fldLine1.getText();
		// address no.
		String addrNo = "";
		int locNo = curTxt.indexOf(" ");
		if (locNo > -1) {
			try {
				Integer.parseInt(curTxt.substring(0, locNo));
				addrNo = String.format("%s ",curTxt.substring(0, locNo));
			} catch (Exception e) {
				addrNo = "";
			}
		}
		// apt no, other.
		String othInfo = "";
		int loc1 = curTxt.lastIndexOf(","); 
		if (loc1 > 0) {
			othInfo = curTxt.substring(loc1);
		}
		lstSt.clearSelection();
		fldLine1.setText(String.format("%s%s%s", addrNo, sif.stFullName, othInfo));
	}
	
	private void _resetAddress() {
		fldLine1.setText(addrObj.getAddrLine1());
		fldLine2.setText(addrObj.getAddrLine2());
		fldUnit.setText(addrObj.getAddrUnitNo());
		_updateComboBox(cbxCity, addrObj.getAddrCity());
		_updateComboBox(cbxState, addrObj.getAddrState());
	}
	
	private void _clearAddress() {
		fldLine1.setText("");
		fldLine2.setText("");
		fldUnit.setText("");
		cbxCity.setSelectedIndex(0);
		cbxState.setSelectedIndex(0);
	}
	
	// -----private class-----

	private class StLst implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {}
		@Override
		public void keyPressed(KeyEvent e) {}
		@Override
		public void keyReleased(KeyEvent e) {
			String txt1 = fldLine1.getText();
			if (TxStreet.partMatched(txt1)) {
				lstSt.clearSelection();
				mdSt.clear();
				for (TxStreet sif : TxStreet.values) {
					if (null == sif) break;
					mdSt.addElement(sif);
				}
			}
		}
	}
	
	private static void _updateComboBox(JComboBox<String> cbx1, String selVal) {
		int selIdx = 0;
		for (int i=0; i < cbx1.getItemCount(); i++) {
			String val1 = cbx1.getItemAt(i);
			if (val1.equals(selVal)) {
				selIdx = i;
				break;
			}
		}
		cbx1.setSelectedIndex(selIdx);
	}
	
	private AddrUS chkAddr = null;
	//private TxArea chkArea = null;
	
	private boolean _isOkayToClose() {
		if (fldLine1.getText().trim().isEmpty() && 
			fldLine2.getText().trim().isEmpty() && 
			fldUnit.getText().trim().isEmpty()) {
			return true;
		}
		if (fldLine1.getText().isEmpty()) return false;
		
		chkAddr = AddrUS.newInstance(
			fldLine1.getText().trim(), fldUnit.getText().trim(), 
			fldLine2.getText().trim(),
			(String)cbxCity.getSelectedItem(),
			(String)cbxState.getSelectedItem());
		
		//chkArea = null;
		
		DlgChkAddr dlgChk = new DlgChkAddr(this);
		dlgChk.showDialog(chkAddr);
		if ("bt_ok".equals(dlgChk.getUsrRsp())) {
			//chkArea = dlgChk.getArea();
			return true;
		}
		return false;
	}
	
	// -----public-----
	
	public void showDialog(AddrUS addr1) {
		this.setLocationRelativeTo(this.getParent());
		addrObj = addr1;
		_resetAddress();
		this.setVisible(true);
	}
	
	public String getUsrRsp() {
		return usrRsp;
	}
	
	public AddrUS getAddress() {
		return chkAddr;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		usrRsp = e.getActionCommand();
		if ("bt_cancel".equals(usrRsp)) {
			this.dispose();
		} else if ("bt_ok".equals(usrRsp)) {
			if (_isOkayToClose()) {
				this.dispose();
			}
		} else if ("bt_reset".equals(usrRsp)) {
			_resetAddress();
		} else if ("bt_clear".equals(usrRsp)) {
			_clearAddress();
		} else if ("addr_select".equals(usrRsp)) {
			TxStreet sif = lstSt.getSelectedValue();
			if (null == sif) return;
			_updateLine1();
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			//UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
		} catch (Exception e) {} 
		
		{
			ResData.status();
		}
		
		javax.swing.JFrame frm1 = new javax.swing.JFrame("Test Dialog");
		frm1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		javax.swing.JTextArea txt1 = new javax.swing.JTextArea();
		javax.swing.JScrollPane scp1 = new javax.swing.JScrollPane(txt1);
		frm1.getContentPane().add(scp1, BorderLayout.CENTER);
		
		frm1.pack();
		frm1.setSize(1024, 768);
		frm1.setLocationRelativeTo(null);
		frm1.setVisible(true);
		
		AddrUS addr1 = AddrUS.newInstance();
		addr1.setAddrLine1("123 Main St");
		addr1.setAddrUnitNo("203");
		addr1.setAddrCity("Chicago");
		addr1.setAddrState("IL");
		
		DlgInpAddr dlg1 = new DlgInpAddr(frm1);
		dlg1.showDialog(addr1);
		
		System.exit(0);
	}
}
