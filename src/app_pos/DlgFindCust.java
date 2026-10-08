package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import model.FindCustInfo;
import model.TbCust;
import resrc.ResCfg;
import resrc.ResData;
import resrc.ResUtil;
import resrc.StdFont;

public class DlgFindCust extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private JLabel lbData, lbCst;
	private String usrRsp = "NA";
	private LstMdFCI mdCst = null;
	private JList<FindCustInfo> lstCst = null;
	
	// -----------------------------------------
	
	public DlgFindCust(Frame prFrm) {
		super(prFrm, "Phone No.", true);
		initComponents();
	}
	
	public DlgFindCust(Dialog prDlg) {
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
		
			JPanel pnTitle = new JPanel();
			pnTitle.setLayout(new BoxLayout(pnTitle, BoxLayout.LINE_AXIS));
			
			//JLabel lbTitle = new JLabel("Phone No.?");
			//lbTitle.setFont(StdFont.Fnt18);
			//pnTitle.add(lbTitle);
			
			//pnTitle.add(Box.createHorizontalStrut(75));
			
			pnTitle.add(UIFactory.buttonE("Clear", "bt_clear", this));
			String[] areas = ResCfg.getPhoneAreaCodes();
			for (String area1 : areas) {
				pnTitle.add(UIFactory.buttonE(area1, String.format("bt_area_%s", area1), this));
			}
			pnTitle.add(Box.createHorizontalGlue());
			
			pnTitle.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY), 
				BorderFactory.createEmptyBorder(0, 0, 0, 0)));
		
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
		
		JPanel pnCenter = new JPanel(new BorderLayout());
		pnCenter.add(crPnFind(), BorderLayout.LINE_END);
			PnKeyPhone pnKey = new PnKeyPhone(this);
		pnCenter.add(pnKey, BorderLayout.CENTER);
		pnCenter.setBorder(BorderFactory.createMatteBorder(2, 0, 2, 0, Color.DARK_GRAY));
		
		this.getContentPane().add(pnCenter, BorderLayout.CENTER);
		
		// Command
		
		Button btCode = Button.newButton("Code?,bt_find_code", this);
		Button btOk = Button.newOk(this);
		Button btCancel = Button.newCancel(this);

		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		pnCmd.add(btCode);
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.add(btOk);
		pnCmd.add(btCancel);
		//pnCmd.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

		this.getContentPane().add(pnCmd, BorderLayout.PAGE_END);
		
		this.setResizable(false);
		this.pack();
	}
	
	private JPanel crPnFind() {
		
		mdCst = new LstMdFCI();
		lstCst = new JList<>(mdCst);
		lstCst.setFocusable(false);
		lstCst.setFont(StdFont.Fnt18);
		lstCst.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) return;
				//
				ListSelectionModel smd = lstCst.getSelectionModel();
				if (smd.isSelectionEmpty()) return;
				//
				_updateCustDetail();
			}
		});
		
		JScrollPane scp1 = new JScrollPane(lstCst,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scp1.getVerticalScrollBar().setPreferredSize(
			new Dimension(25, 0)); 
		scp1.setPreferredSize(new Dimension(300, 0));
		scp1.setBorder(null);
		
		lbCst = new JLabel("");
		lbCst.setFont(StdFont.Fnt18);
		lbCst.setPreferredSize(new Dimension(225, 0));
		lbCst.setVerticalAlignment(SwingConstants.TOP);
		lbCst.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
		
		JPanel pnFind = new JPanel(new BorderLayout());
		pnFind.add(scp1, BorderLayout.CENTER);
		pnFind.add(lbCst, BorderLayout.LINE_END);

		return pnFind;
	}
	
	private void _showValue(String raw) {
		lbData.setText(ResUtil.formatPhone(raw));
		//
		lstCst.clearSelection();
		lbCst.setText("");
		mdCst.clear();
		if (raw.isEmpty()) {
			return;
		}
		//
		java.util.Vector<FindCustInfo> _finds = FindCustInfo.searchPhone(raw);
		if (null == _finds) {
			return;
		}
		for (FindCustInfo fci1 : _finds) {
			mdCst.addElement(fci1);
		}
	}
	
	private void _showValue(TbCust raw) {
		lbData.setText(ResUtil.formatPhone(raw.getCstPhone()));
		//
		lstCst.clearSelection();
		lbCst.setText("");
		mdCst.clear();
		mdCst.addElement(FindCustInfo.newInstance(raw));
		//
		lstCst.setSelectedIndex(0);
	}
	
	private void _updateValue(String val) {
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
	
	private void _updateCustDetail() {
		FindCustInfo fci = lstCst.getSelectedValue();
		if (fci == null) {
			lstCst.clearSelection();
			return;
		}
		FindCustInfo fci1 = lstCst.getSelectedValue();
		String ptrn1 = "<html>"
				+ "%s" // code
				+ "%s<br>"
				+ "<hr size=1>" // phone
				+ "%s<br>" // name
				+ "%s" // line1
				+ "%s" // line2
				+ "%s</html>"; // city, state, zip

		String code1 = "";
		if (!fci1.getCstCode().isEmpty()) {
			code1 = String.format("<b><i>#%s</i></b><hr>", fci1.getCstCode());
		}
		String line1 = fci1.getCstAddr1();
		String line2 = "";
		String city1 = "";
		if (!line1.isEmpty()) {
			line1 = "<hr size=1>"+ line1 + "<br>";
			if (!fci1.getCstAddr2().isEmpty()) {
				line2 = fci1.getCstAddr2();
			}
			if (!fci1.getCstUnitNo().isEmpty()) {
				line2 += fci1.getCstUnitNo();
			}
			if (!line2.isEmpty()) {
				line2 += "<br>";
			}
			city1 = fci.getCstCity();
			if (!fci.getCstState().isEmpty()) {
				city1 += ", "+ fci.getCstState();
			}
		}
		lbCst.setText(String.format(ptrn1, 
			code1,
			ResUtil.formatPhone(fci.getCstPhone()),
			fci.getCstName(),
			line1,
			line2,
			city1));
	}
	
	private boolean _isOkayToClose() {
		String rawDt = lbData.getText().replaceAll("-", "").replaceAll("_", "");
		return (rawDt.length() == 10) || (lstCst.getSelectedIndex() >= 0);
	}
	
	// ----------------------------------
	
	public void showDialog() {
		usrRsp = "NA";
		_showValue("");
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}
	
	public TbCust getCustomer() {
		if (null != lstCst.getSelectedValue()) {
			return TbCust.getCustById(lstCst.getSelectedValue().getCstId());
		}
		String v1 = lbData.getText().replaceAll("-", "");
		TbCust cst1 = TbCust.newInstance();
		cst1.setCstPhone(v1.replaceAll("_", ""));
		return cst1;
	}
	
	public String getUsrRsp() {
		return usrRsp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		usrRsp = e.getActionCommand();
		if ("bt_clear".equals(usrRsp)) {
			_showValue("");
		} else if ("bt_cancel".equals(usrRsp)) {
			this.dispose();
		} else if ("bt_ok".equals(usrRsp)) {
			if (_isOkayToClose()) {
				this.dispose();
			}
		} else if ("bt_find_code".equals(usrRsp)) {
			DlgCstCode dlg1 = new DlgCstCode(this);
			dlg1.showDialog();
			if ("bt_ok".equals(dlg1.getUsrRsp())) {
				_showValue(dlg1.getCustomer());
			}
			return;
		}
		
		if (usrRsp.startsWith("bt_area_")) {
			_showValue(usrRsp.substring(8));
			return;
		}
		
		if (!usrRsp.startsWith("key_")) {
			return;
		}
		_updateValue(usrRsp.substring(4));
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
		
		DlgFindCust dlg1 = new DlgFindCust(frm1);
		dlg1.showDialog();
		if ("bt_ok".equals(dlg1.getUsrRsp())) {
			TbCust cst1 = dlg1.getCustomer();
			System.out.printf("id[%d] name[%s] phone[%s]\n",
				cst1.getCstId(),
				cst1.getCstName(),
				cst1.getCstPhone());
		}
		
		System.exit(0);
	}
}
