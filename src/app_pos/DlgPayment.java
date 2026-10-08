package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import print.RcpPrinter;
import print.StarPrinter;

import com.usaepay.api.jaxws.TransactionResponse;

import model.CCData;
import model.TbGift;
import model.TbOrder;
import model.TbPayment;
import refx.DlgType;
import refx.PayBy;
import refx.PaySrc;
import resrc.ResData;
import resrc.StdFont;

public class DlgPayment extends JDialog implements ActionListener, MSRCCIntf {
	private static final long serialVersionUID = 1L;
	
	private String usrRsp = "NA";
	private JLabel lbSplt, lbTitle, lbTotal, 
		lbPaid, lbRemain, lbChange;
	private LstMdTbPayment mdPay;
	private JList<TbPayment> lstPay;
	private double spltAmt = 0;
	
	private TbOrder pord;
	
	private DlgBox dlgBx;
	
	// -----constructor-----
	
	public DlgPayment(Frame _pr) {
		super(_pr, "Payment :-) Swipe credit card to pay the remaining or split amount.", true);
		dlgBx = new DlgBox(this);
		initComponents();
	}
	
	// -----private-----
	
	private void initComponents() {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		
		crPnTitle();
		crPnPay();
		crPnPayBy();
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
		
		mdPay = new LstMdTbPayment();
		
		lstPay = new JList<>(mdPay);
		lstPay.setFocusable(false);
		lstPay.setFont(StdFont.Fnt18);
		lstPay.setVisibleRowCount(-1);
		lstPay.setCellRenderer(new LstRdrPayment());
		lstPay.setFocusable(false);
		lstPay.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane scp1 = new JScrollPane(lstPay,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		scp1.setAlignmentX(LEFT_ALIGNMENT);
		scp1.setFocusable(false);
		scp1.getVerticalScrollBar().setPreferredSize(
			new Dimension(30, scp1.getVerticalScrollBar().getPreferredSize().height));
		scp1.getHorizontalScrollBar().setPreferredSize(
			new Dimension(scp1.getVerticalScrollBar().getPreferredSize().width, 30));
		scp1.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY));
		
		scp1.setPreferredSize(new Dimension(325, 205));
		scp1.setMaximumSize(new Dimension(325, Short.MAX_VALUE));
		
		JPanel pnLst = new JPanel(new BorderLayout());
		pnLst.add(scp1, BorderLayout.CENTER);
		
		JButton btVoid = new JButton("Void");
		btVoid.setFocusable(false);
		btVoid.setFont(StdFont.Fnt18);
		btVoid.setActionCommand("bt_void");
		btVoid.addActionListener(this);
		btVoid.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY),
			BorderFactory.createEmptyBorder(5, 10, 5, 10)));

		/*
		JButton btReCharge = new JButton("ReCharge");
		btReCharge.setFocusable(false);
		btReCharge.setFont(StdFont.Fnt18);
		btReCharge.setActionCommand("bt_recharge");
		btReCharge.addActionListener(this);
		btReCharge.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 0, 0, Color.GRAY),
				BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		*/
		
		JPanel pnVC = new JPanel();
		pnVC.setLayout(new GridLayout(1, 2));
		pnVC.add(btVoid);
		//pnVC.add(btReCharge);
		pnVC.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
		
		pnLst.add(pnVC, BorderLayout.PAGE_END);
		
		// 
		
		lbTotal = new JLabel("1,230.78");
		lbPaid = new JLabel("0.00");
		
		lbRemain = new JLabel("0.00");
		lbRemain.setForeground(Color.RED);
		
		lbChange = new JLabel("0.00");
		
		JPanel pnPay = new JPanel();
		pnPay.setLayout(new BoxLayout(pnPay, BoxLayout.PAGE_AXIS));

		pnPay.add(pnLst);
		
		pnPay.add(Box.createVerticalStrut(15));
		pnPay.add(crPnLabel(lbTotal, "Total"));
		pnPay.add(crPnLabel(lbPaid, "Paid"));
		pnPay.add(crPnLabel(lbRemain, "Remaining"));
		pnPay.add(crPnLabel(lbChange, "Change"));
		pnPay.add(Box.createVerticalStrut(18));
		
		this.getContentPane().add(pnPay, BorderLayout.LINE_START);
	}
	
	private JPanel crPnLabel(JLabel _lb, String _title) {
		_lb.setFont(StdFont.Fnt18B);
		_lb.setHorizontalAlignment(SwingConstants.RIGHT);
		_lb.setPreferredSize(new Dimension(95, _lb.getPreferredSize().height));
		_lb.setMaximumSize(_lb.getPreferredSize());
		//_lb.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.RED));
		
		JPanel pn1 = new JPanel(new FlowLayout(FlowLayout.TRAILING, 0, 0));
		
		JLabel lbTitle = new JLabel(_title);
		lbTitle.setFont(StdFont.Fnt18B);
		lbTitle.setMaximumSize(lbTitle.getPreferredSize());
		lbTitle.setForeground(_lb.getForeground());
		//lbTitle.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));

		pn1.add(lbTitle);
		pn1.add(_lb);

		pn1.setAlignmentX(LEFT_ALIGNMENT);
		pn1.setMaximumSize(new Dimension(Short.MAX_VALUE, pn1.getPreferredSize().height));
		pn1.setBorder(BorderFactory.createEmptyBorder(0, 0, 7, 15));
		return pn1;
	}
	
	private void crPnCmd() {
		
		Button btSp2 = Button.newButton("Split-2,bt_split_2", this);
		Button btSp3 = Button.newButton("Split-3,bt_split_3", this);
		Button btSp4 = Button.newButton("Split-4,bt_split_4", this);
		Button btSpX= Button.newButton("Split-?,bt_split_x", this);
		Button btSpClr = Button.newButton("Clear,bt_split_clr", this);
		Button btClose = Button.newButton("Close,bt_close", this);

		JPanel pnBt = new JPanel();
		pnBt.setLayout(new BoxLayout(pnBt, BoxLayout.LINE_AXIS));
		pnBt.add(btSp2);
		pnBt.add(btSp3);
		pnBt.add(btSp4);
		pnBt.add(btSpX);
		pnBt.add(btSpClr);
		pnBt.add(Box.createHorizontalGlue());
		pnBt.add(btClose);
		
		lbSplt = new JLabel("Split 3 each payment = $33.22 [Locked]");
		lbSplt.setFont(StdFont.Fnt16);
		lbSplt.setBackground(Color.decode("#006699"));
		lbSplt.setForeground(Color.WHITE);
		lbSplt.setBorder(BorderFactory.createEmptyBorder(10, 13, 10, 10));
		lbSplt.setOpaque(true);
		lbSplt.setVisible(false);
		
		JPanel pnCmd = new JPanel(new BorderLayout());
		pnCmd.add(lbSplt, BorderLayout.PAGE_START);
		pnCmd.add(pnBt, BorderLayout.PAGE_END);
		pnCmd.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

		this.getContentPane().add(pnCmd, BorderLayout.PAGE_END);
	}
	
	private void crPnPayBy() {
		JPanel pn1 = new JPanel();
		pn1.setLayout(new GridLayout(0, 1, 0, 5));
		
		pn1.add(crBtPay("Cash", "pay_cash", this));
		pn1.add(crBtPay("Credit Card", "pay_cc", this));
		pn1.add(crBtPay("Check", "pay_check", this));
		pn1.add(crBtPay("Gift Cert.", "pay_gift", this));
		
		pn1.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 1, 0, 0, Color.GRAY),
			BorderFactory.createEmptyBorder(15, 15, 15, 15)));
		pn1.setBackground(Color.LIGHT_GRAY);
		this.getContentPane().add(pn1, BorderLayout.LINE_END);
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
		try {
			if (paid()) {
				//
				RcpPrinter.printCreditCardSign(mdPay.getItems());
				//
				double amtChange = Double.parseDouble(lbChange.getText());
				pord.paid(amtChange);
				
				if (amtChange > 0) {
					DlgBox dlg1 = new DlgBox(this);
					dlg1.showDialog("Change", 
						String.format("<html>!Change <b>$%.2f</b></html>", amtChange), 
						DlgType.Information);
					StarPrinter.kickDrawer();
				}
				
				this.dispose();
			} else {
				DlgBox msgBx = new DlgBox(this);
				msgBx.showConfirmDialog(
					"Warning, Not Fully Paid", 
					"<html>Order is not fully paid.,<br><b>Close Payment?</b></html>", 
					DlgType.Warning);
				if ("bt_ok".equals(msgBx.getUsrRsp())) {
					pord.unpaid();
					this.dispose();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void validatePaymentInfo() {
		
		double amtNet = pord.getOrdAmtNet();
		double sumPaid = 0;
		double sumRemain = 0;
		double sumChange = 0;
		
		for (TbPayment _pm : mdPay.getItems()) {
			sumPaid += _pm.getPmAmt();
		}

		sumRemain = amtNet - sumPaid;
		if (sumRemain < 0) {
			sumRemain = 0;
		}
		
		sumChange = sumPaid - amtNet;
		if (sumChange < 0) {
			sumChange = 0;
		}
		
		lbPaid.setText(String.format("%.2f", sumPaid));
		lbRemain.setText(String.format("%.2f", sumRemain));
		lbChange.setText(String.format("%.2f", sumChange));
		
		// have to update split amount ... 
		
		if (!lbRemain.getText().equals("0.00")) return;
		
		lbSplt.setVisible(false);
		this.pack();
		this.setLocationRelativeTo(this.getParent());
	}
	
	private void splitAmount(int snum) {
		if (paid()) return;
		//
		double rem1 = Double.parseDouble(lbRemain.getText());
		if (rem1 <= 0) {
			return;
		}
		if (snum == -1) {
			DlgInpNum dlgNum = new DlgInpNum(this);
			dlgNum.showDialog("Total Split?", 5);
			if ("bt_ok".equals(dlgNum.getUsrRsp())) {
				snum = dlgNum.getIntValue();
			} else {
				return;
			}
		}
		spltAmt = rem1/snum;
		lbSplt.setText(String.format("Split %d @= $%.2f [Locked]", snum, spltAmt));
		lbSplt.setVisible(true);
		this.pack();
		this.setLocationRelativeTo(this.getParent());
	}
	
	private void payCash() {
		if (paid()) return;
		//
		TbPayment pm1 = null;
		for (TbPayment _pm : mdPay.getItems()) {
			if (PayBy.Cash == _pm.getPmPayBy()) {
				pm1 = _pm;
				break;
			}
		}
		boolean newFlg = null == pm1;
		if (null == pm1) {
			pm1 = TbPayment.newPayCash(Double.parseDouble(lbRemain.getText()), PaySrc.Order);
		}
		double _pay1 = pm1.getPmAmt();
		if (lbSplt.isVisible()) {
			_pay1 = spltAmt;
		}
		double _remain = Double.parseDouble(lbRemain.getText());
		if (_pay1 > _remain) {
			_pay1 = _remain;
		}
		//
		DlgInpNum dlgInp = new DlgInpNum(this);
		dlgInp.showDialog("Pay Cash?", _pay1);
		if ("bt_ok".equals(dlgInp.getUsrRsp())) {
			try {
				pm1.setRefId(pord.getOrdId());
				pm1.setOrdNo(pord.getOrdNo());
				pm1.setPmAmt(dlgInp.getDoubleValue());
				pm1.save();
				if (newFlg) {
					mdPay.addElement(pm1);
				} else {
					mdPay.validateInfo();
				}
				validatePaymentInfo();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private double getNonCashPayAmt() {
		double _pay1 = Double.parseDouble(lbRemain.getText());
		if (_pay1 <= 0) {
			return 0;
		}
		if (lbSplt.isVisible()) {
			if (_pay1 > spltAmt) {
				_pay1 = spltAmt;
			}
		}
		return _pay1;
	}
	
	private TransactionResponse resp;
	
	private void voidCreditCard(final TbPayment pm1) throws SQLException {
		final DlgThread dlgTrd = new DlgThread(this);
		Thread trd1 = new Thread() {
			public void run() {
				dlgTrd.setExitCode(0);
				dlgTrd.setText("Void Credit Card ... \n");
				dlgTrd.append("Please wait ... \n");
				try {
					boolean prcRes = PaymentGateway.voidTransaction(pm1);
					//dlgTrd.setExitCode(prcRes ? 1 : 2);
					dlgTrd.setExitCode(1);
					dlgTrd.dispose();
				} catch (Exception e) {
					if (e.getMessage().startsWith("24:")) {
						dlgTrd.setExitCode(3);
						dlgTrd.dispose();
					}
					dlgTrd.append(String.format("Error\n%s", e.getMessage()));
					dlgTrd.append("\n");
				}
				dlgTrd.append(String.format("Exit Code [%d]", dlgTrd.getExitCode()));
				dlgTrd.processEnd();
			}
		};
		dlgTrd.showDialog(trd1);
		System.out.printf("[void] done with [%d]\n", dlgTrd.getExitCode());

		if (1 == dlgTrd.getExitCode() || 3 == dlgTrd.getExitCode()) {
			// success
			int pmIdx = lstPay.getSelectedIndex();
			mdPay.removeElementAt(pmIdx);
			pm1.delete();
			validatePaymentInfo();
		} else {
			// error
			dlgBx.showDialog(
				"Void Error", 
				"<html>Void Error, Please try again later.</html>", 
				DlgType.Warning);
		}
	}
	
	private void processCreditCard(final TbPayment pm1) throws SQLException {
		final DlgThread dlgTrd = new DlgThread(this);
		Thread trd1 = new Thread() {
			public void run() {
				dlgTrd.setExitCode(0);
				dlgTrd.setText("Processing Card ... ");
				try {
					resp = PaymentGateway.authenOnly(
						getNonCashPayAmt(), 
						PaySrc.Order.toString(), 
						String.format("ORDER_%d", pord.getOrdNo()), 
						pm1);
					ResData.saveTransactionResponse(pm1.getPmNo(), resp);
					//"A".equals(resp.getResultCode());
					dlgTrd.setExitCode(1);
					dlgTrd.dispose();
				} catch (Exception e) {
					dlgTrd.append(String.format("Error\n%s", e.getMessage()));
					dlgTrd.append("\n");
				}
				dlgTrd.append(String.format("Exit Code [%d]", dlgTrd.getExitCode()));
				dlgTrd.processEnd();
			}
		};
		dlgTrd.showDialog(trd1);
		System.out.printf("done with [%d]\n", dlgTrd.getExitCode());
		
		// Approved
		if (null != resp && "A".equals(resp.getResultCode())) {
			if (resp.getRefNum().longValue() > 0) {
				pm1.setRefId(pord.getOrdId());
				pm1.setOrdNo(pord.getOrdNo());
				pm1.setTxNo(String.format("%d", resp.getRefNum()));
				pm1.setTxResult(resp.getResult());
				pm1.setTxMessage(resp.getAvsResult());
				pm1.save();
				mdPay.addElement(pm1);
			}
			validatePaymentInfo();
		} else { // Not Approved
			if (null != pm1 && pm1.getPmId() > 0) {
				pm1.delete();
			}
			dlgBx.showDialog(
				"Card Process Error", 
				String.format("<html>Result = %s (Code %d)<br><b>%s</b></html>", 
					resp.getResult(), resp.getErrorCode(), resp.getError()), 
				DlgType.Warning);
		}
		// return "A".equals(resp.getResultCode());
	}
	
	private void payNonCash(PayBy _pby) {
		if (paid()) return;
		//
		double _payAmt = getNonCashPayAmt();
		if (_payAmt <= 0) {
			return;
		}
		//
		DlgPayBy dlg1 = new DlgPayBy(this, _pby, _payAmt, PaySrc.Order, true);
		dlg1.showDialog();
		if ("bt_ok".equals(dlg1.getUsrRsp())) {
			try {
				TbPayment pm1 = dlg1.getPayment();
				pm1.setRefId(pord.getOrdId());
				pm1.setOrdNo(pord.getOrdNo());
				pm1.save();

				if (PayBy.CreditCard == pm1.getPmPayBy()) {
					
					processCreditCard(pm1);
					
				} else if (PayBy.Check == pm1.getPmPayBy()) {
					// nothing to do ... pass through
					mdPay.addElement(pm1);
					validatePaymentInfo();
				} else if (PayBy.GiftCard == pm1.getPmPayBy()) {
					// verify gift card balance
					TbGift gft1 = TbGift.findCard(pm1.getPmGiftNo());
					if (null == gft1) {
						DlgBox dlgx = new DlgBox(this);
						dlgx.showDialog(
							"Invalid Card", "<html>Can't find Gift Card<br>Please try again.</html>", 
							DlgType.Information);
						return;
					}
					boolean payValid = gft1.redeem(pm1);
					if (!payValid) {
						DlgBox dlgx = new DlgBox(this);
						dlgx.showDialog(
							"Insufficient Balance", "<html>Gidt Card has insufficient balance.</html>", 
							DlgType.Warning);
						return;
					}
					if (payValid) {
						mdPay.addElement(pm1);
						validatePaymentInfo();
						return;
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void voidPayment() {
		TbPayment selPm = lstPay.getSelectedValue();
		if (null == selPm) return;
		//
		if (selPm.getPmId() > 0) {
			try {
				if (PayBy.CreditCard == selPm.getPmPayBy()) {
					voidCreditCard(selPm);
				} else if (PayBy.GiftCard == selPm.getPmPayBy()) {
					
				} else {
					mdPay.removeElementAt(lstPay.getSelectedIndex());
					selPm.delete();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				dlgBx.showDialog(
					"DB Error", 
					String.format("<html>DB Error [%s]</html>", e.getMessage()), 
					DlgType.Warning);
			}
		}
		//
		validatePaymentInfo();
	}
	
	// -----public-----
	
	public boolean paid() {
		return lbRemain.getText().equals("0.00");
	}
	
	public void showDialog(TbOrder _pord) {
		try {
			pord = _pord;
			
			TbPayment[] oldPms = TbPayment.getPaymentHistById(
				pord.getOrdId(), PaySrc.Order);
			
			lbTitle.setText(pord.getCstHTML());
			lbTotal.setText(String.format("%.2f", pord.getOrdAmtNet()));

			mdPay.setItems(oldPms);
			
			validatePaymentInfo();
			
			this.pack();
			this.setLocationRelativeTo(this.getParent());
			this.setVisible(true);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public String getUsrRsp() {
		return usrRsp;
	}

	@Override
	public void creditCardSwiped(CCData cc1) {
		if (paid()) {
			System.out.println(" ... paid ... ");
			return;
		}
		if (!cc1.isValid()) {
			System.out.println(" ... invalid card ... ");
			dlgBx.showDialog(
				"Invalid Card", 
				"<html>Invalid Card Data ...<br>Please try again or change card.</html>", 
				DlgType.Warning);
			return;
		}
		//
		
		System.out.println("processing ... ");
		try {
			TbPayment pm1 = TbPayment.newPayCreditCard(
				getNonCashPayAmt(), cc1, PaySrc.Order);

			processCreditCard(pm1);
			
			/*
			TransactionResponse resp = 
				PaymentGateway.authenOnly(
					getNonCashPayAmt(), 
					PaySrc.Order.toString(), 
					String.format("ORDER_%d", pord.getOrdNo()), 
					pm1);
			ResData.saveTransactionResponse(pord.getOrdId(), resp);
			*/
			
		} catch (Exception e) {
			//e.printStackTrace();
			dlgBx.showDialog(
				"System Error", 
				e.getMessage(), 
				DlgType.Critical);
		}
		//System.out.println("card swiped ... ");
	}

	@Override
	public void cardReadError() {
		//System.out.println("card read error.");
		dlgBx.showDialog(
			"System Error", 
			"<html>Can't read Card Data<br>Please try again.</html>", 
			DlgType.Critical);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		usrRsp = e.getActionCommand();
		if ("bt_close".equals(usrRsp)) {
			disposeDialog();
		} else if ("bt_split_2".equals(usrRsp)) {
			splitAmount(2);
		} else if ("bt_split_3".equals(usrRsp)) {
			splitAmount(3);
		} else if ("bt_split_4".equals(usrRsp)) {
			splitAmount(4);
		} else if ("bt_split_x".equals(usrRsp)) {
			splitAmount(-1);
		} else if ("bt_split_clr".equals(usrRsp)) {
			lbSplt.setVisible(false);
			this.pack();
			this.setLocationRelativeTo(this.getParent());
		} else if ("pay_cash".equals(usrRsp)) {
			payCash();
		} else if ("pay_cc".equals(usrRsp)) {
			payNonCash(PayBy.CreditCard);
		} else if ("pay_check".equals(usrRsp)) {
			payNonCash(PayBy.Check);
		} else if ("pay_gift".equals(usrRsp)) {
			payNonCash(PayBy.GiftCard);
		} else if ("bt_void".equals(usrRsp)) {
			voidPayment();
		} else if ("bt_recharge".equals(usrRsp)) {
			// re-charge
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

			/*
			PxyTbOrder pord = PxyTbOrder.getPxyTbOrderByOrdNo(598);
			if (null == pord) {
				System.out.println("order not found.");
				System.exit(0);
			}

			DlgPayment dlg1 = new DlgPayment(frm1);
			dlg1.showDialog(pord);
			if ("bt_close".equals(dlg1.getUsrRsp())) {
				
			}
			*/
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		System.exit(0);
	}
}
