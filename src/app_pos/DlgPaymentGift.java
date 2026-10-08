package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import model.CCData;
import model.TbGift;
import model.TbGiftTrx;
import model.TbPayment;
import refx.DlgType;
import refx.PayBy;
import refx.PaySrc;
import resrc.StdFont;

public class DlgPaymentGift extends JDialog implements ActionListener, MSRCCIntf {
	private static final long serialVersionUID = 1L;
	
	private String usrRsp = "NA";
	private JLabel lbTitle, lbAmt;
	private TbGift gftObj;
	private PnKeyNum pnKey;
	private InpNumHandler nhAmt;
	private double netPay = 0;
	
	// -----constructor-----
	
	public DlgPaymentGift(Frame _pr) {
		super(_pr, "Add Money", true);
		initComponents();
	}
	
	public DlgPaymentGift(Dialog _pr) {
		super(_pr, "Add Money", true);
		initComponents();
	}
	
	// -----private-----
	
	private void initComponents() {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		
		crPnTitle();
		crPnPay();
		crPnCmd();
		
		this.addKeyListener(new MSRCCLst(this));
		this.pack();
	}
	
	private void crPnTitle() {
		
		lbTitle = new JLabel("-");
		lbTitle.setFont(StdFont.Fnt20);
		lbTitle.setBackground(Color.decode("#003399"));
		lbTitle.setForeground(Color.WHITE);
		lbTitle.setBorder(BorderFactory.createEmptyBorder(13, 13, 13, 13));
		lbTitle.setOpaque(true);
		
		this.getContentPane().add(lbTitle, BorderLayout.PAGE_START);
	}
	
	private void crPnPay() {
		JPanel pn1 = new JPanel(new BorderLayout());
		
		JPanel pnPre = new JPanel();
		pnPre.setLayout(new BoxLayout(pnPre, BoxLayout.PAGE_AXIS));
		
		String[] preGft = {"$25", "$50", "$100", "$200"};
		for (int m=0; m < preGft.length; m++) {
			if (m > 0) {
				pnPre.add(Box.createVerticalStrut(5));
			}
			String str1 = preGft[m];
			pnPre.add(crBtPreGft(str1));
		}
		
		pnPre.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 0));
		
		pn1.add(pnPre, BorderLayout.LINE_START);
		
		JPanel pnAmtKey = new JPanel(new BorderLayout());
		
			lbAmt = new JLabel("0");
			lbAmt.setFont(StdFont.Fnt22);
			lbAmt.setBackground(Color.WHITE);
			lbAmt.setOpaque(true);
			lbAmt.setHorizontalAlignment(SwingConstants.RIGHT);
			lbAmt.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createMatteBorder(1, 1, 0, 1, Color.GRAY),
					BorderFactory.createEmptyBorder(10, 10, 10, 10)
				));
			
			nhAmt = new InpNumHandler(lbAmt);
			nhAmt.setDoubleValue(0);
			
		pnAmtKey.add(lbAmt, BorderLayout.PAGE_START);
		
			pnKey = PnKeyNum.newPanelNoBorder(this);
			pnKey.setOpaque(false);
			pnKey.setAlignmentX(LEFT_ALIGNMENT);
		pnAmtKey.add(pnKey, BorderLayout.CENTER);
		
		pnAmtKey.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(20, 20, 20, 20),
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY)
			));
		
		pn1.add(pnAmtKey, BorderLayout.CENTER);
		
			JPanel pnBy = new JPanel();
			pnBy.setLayout(new GridLayout(0, 1, 0, 5));
			
			pnBy.add(crBtPay("Cash", "pay_cash", this));
			pnBy.add(crBtPay("Credit Card", "pay_cc", this));
			pnBy.add(crBtPay("Check", "pay_check", this));
			
			pnBy.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 1, 0, 0, Color.GRAY),
				BorderFactory.createEmptyBorder(20, 20, 20, 25)));
			pnBy.setBackground(Color.LIGHT_GRAY);
			
		pn1.add(pnBy, BorderLayout.LINE_END);
		
		this.add(pn1, BorderLayout.CENTER);
	}
	
	private JButton crBtPreGft(String txt) {
		JButton bt1 = new JButton(txt);
		bt1.setFont(StdFont.Fnt20);
		bt1.setHorizontalAlignment(SwingConstants.LEFT);
		bt1.setFocusable(false);
		bt1.setActionCommand(String.format("bt_preset_%s", txt.substring(1)));
		bt1.addActionListener(this);
		bt1.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createEtchedBorder(),
			BorderFactory.createEmptyBorder(10, 10, 10, 35)));
		bt1.setMaximumSize(new Dimension(Short.MAX_VALUE, bt1.getPreferredSize().height));
		bt1.setAlignmentX(LEFT_ALIGNMENT);
		bt1.setAlignmentY(TOP_ALIGNMENT);
		return bt1;
	}
	
	private void crPnCmd() {
		
		Button btClose = Button.newButton("Close,bt_close", this);

		JPanel pnBt = new JPanel();
		pnBt.setLayout(new BoxLayout(pnBt, BoxLayout.LINE_AXIS));
		pnBt.add(Box.createHorizontalGlue());
		pnBt.add(btClose);

		pnBt.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

		this.getContentPane().add(pnBt, BorderLayout.PAGE_END);
	}
	
	private static JButton crBtPay(String _txt, String _cmd, ActionListener _lst) {
		JButton bt1 = new JButton(_txt);
		bt1.setActionCommand(_cmd);
		bt1.addActionListener(_lst);
		bt1.setFocusable(false);
		bt1.setHorizontalAlignment(SwingConstants.LEFT);
		bt1.setVerticalAlignment(SwingConstants.TOP);
		bt1.setFont(StdFont.Fnt22);
		bt1.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createEtchedBorder(),
			BorderFactory.createEmptyBorder(10, 10, 10, 35)));
		return bt1;
	}
	
	private void disposeDialog() {
		this.dispose();
	}
	
	private boolean isPayValid(double _payAmt, double _chkAmt) {
		if (_payAmt > _chkAmt) {
			DlgBox dlgBx = new DlgBox(this);
			dlgBx.showDialog(
				"Invalid Amount", 
				String.format("<html>Invalid Pay Amount<br><b>Pay value must be equals/greater [%.2f].</b></html>", _payAmt),
				DlgType.Warning);
			return false;
		}
		return true;
	}
	
	private void _payCash() {
		double payAmt = Double.parseDouble(lbAmt.getText());
		if (payAmt <= 0) {
			return;
		}
		DlgInpNum dlgInp = new DlgInpNum(this);
		dlgInp.showDialog("Pay Cash?", payAmt);
		if ("bt_ok".equals(dlgInp.getUsrRsp())) {
			double chkAmt = dlgInp.getDoubleValue();
			System.out.printf("pay [%.2f] -> [%.2f]\n", payAmt, chkAmt);
			if (!isPayValid(payAmt, chkAmt)) {
				return;
			}
			try {
				TbGiftTrx tr1 = TbGiftTrx.newRefill(gftObj, payAmt);
				tr1.save();
				
				TbPayment pm1 = TbPayment.newPayCash(payAmt, PaySrc.Gift);
				pm1.setRefId(tr1.getTxId());
				pm1.save();
				
				if (chkAmt > payAmt) {
					DlgBox dlgBx = new DlgBox(this);
					dlgBx.showDialog(
						"Change", 
						String.format("<html>Change = <b>$%.2f</b></html>", chkAmt-payAmt),
						DlgType.Information);
				}
				
				netPay = payAmt;
				usrRsp = "paid";
				disposeDialog();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void _payNonCash(PayBy _pby) {
		double payAmt = Double.parseDouble(lbAmt.getText());
		if (payAmt <= 0) {
			return;
		}
		//
		DlgPayBy dlg1 = new DlgPayBy(this, _pby, payAmt, PaySrc.Gift, false);
		dlg1.showDialog();
		if ("bt_ok".equals(dlg1.getUsrRsp())) {
			try {
				TbPayment pm1 = dlg1.getPayment();
				if (!isPayValid(payAmt, pm1.getPmAmt())) {
					return;
				}
				
				TbGiftTrx tr1 = TbGiftTrx.newRefill(gftObj, payAmt);
				tr1.save();

				pm1.setRefId(tr1.getTxId());
				pm1.save();
				
				if (PayBy.CreditCard == pm1.getPmPayBy()) {
					// verify credit card
				}
				
				netPay = payAmt;
				usrRsp = "paid";
				disposeDialog();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	// -----public-----
	
	public void showDialog(TbGift _gft) {
		gftObj = _gft;
		lbTitle.setText(String.format("Gift No. %s", gftObj.getGftCode()));
		this.pack();
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}
	
	public String getUsrRsp() {
		return usrRsp;
	}
	
	public double getNetPay() {
		return netPay;
	}

	@Override
	public void creditCardSwiped(CCData cc1) {
		double payAmt = Double.parseDouble(lbAmt.getText());
		if (payAmt <= 0) {
			return;
		}
		if (!cc1.isValid()) return;
		try {
			TbGiftTrx tr1 = TbGiftTrx.newRefill(gftObj, payAmt);
			tr1.save();

			TbPayment pm1 = TbPayment.newPayCreditCard(payAmt, cc1, PaySrc.Gift);
			pm1.setRefId(tr1.getTxId());
			pm1.save();
			
			// verify credit card
			
			netPay = payAmt;
			usrRsp = "paid";
			disposeDialog();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("card swiped ... ");
	}

	@Override
	public void cardReadError() {
		System.out.println("card read error.");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		usrRsp = e.getActionCommand();
		if ("bt_close".equals(usrRsp)) {
			disposeDialog();
			return;
		}
		if ("pay_cash".equals(usrRsp)) {
			_payCash();
			return;
		}
		if ("pay_cc".equals(usrRsp)) {
			_payNonCash(PayBy.CreditCard);
			return;
		}
		if ("pay_check".equals(usrRsp)) {
			_payNonCash(PayBy.Check);
			return;
		}
		if (usrRsp.startsWith("key_")) {
			nhAmt.updateValue(usrRsp);
			return;
		}
		if (usrRsp.startsWith("bt_preset_")) {
			nhAmt.setDoubleValue(
				Double.parseDouble(usrRsp.substring(10)));
			return;
		}
	}
	
	// -----main-----

	public static void main(String[] args) {
		try {
			//UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
			
			javax.swing.JFrame frm1 = new javax.swing.JFrame("Test Dialog");
			frm1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			
			javax.swing.JTextArea txt1 = new javax.swing.JTextArea();
			javax.swing.JScrollPane scp1 = new javax.swing.JScrollPane(txt1);
			frm1.getContentPane().add(scp1, BorderLayout.CENTER);
			
			frm1.pack();
			frm1.setSize(1024, 768);
			frm1.setLocationRelativeTo(null);
			frm1.setVisible(true);
			
			TbGift gft1 = TbGift.findCard("112233");

			DlgPaymentGift dlg1 = new DlgPaymentGift(frm1);
			dlg1.showDialog(gft1);
			if ("bt_close".equals(dlg1.getUsrRsp())) {
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		System.exit(0);
	}
}
