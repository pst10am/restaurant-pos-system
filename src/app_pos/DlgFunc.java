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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import print.RcpPrinter;
import print.StarPrinter;

import com.usaepay.api.jaxws.BatchStatus;
import com.usaepay.api.jaxws.TransactionResponse;

import model.TbOrder;
import model.TbPayment;
import model.TbShift;
import refx.CCCapture;
import refx.DlgType;
import refx.PayBy;
import refx.PrintTo;
import resrc.ResData;
import resrc.StdFont;

public class DlgFunc extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private String usrRsp = "NA";
	
	public DlgFunc(Frame _pr) {
		super(_pr, "Function", true);
		initComponents();
	}
	
	// private
	
	private void initComponents() {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		String[] fnGrp = {
			"Order", "Table", "Batch", "Report", "Miscellaneous"
		};
		
		String[][] fnGrpDt = {
			{"Reopen", "RePrint"},
			{"Unlock", "Transfer"},
			{"Adjust Tip", "Close Shift", "Settlement"},
			{"Shift Report", "Settlement Report", "Clock Report"},
			{"Open Cash Drawer"}
		};
		
		JPanel pnFn = new JPanel();
		pnFn.setOpaque(false);
		pnFn.setLayout(new BoxLayout(pnFn, BoxLayout.PAGE_AXIS));
		for (int i=0; i < fnGrp.length; i++) {
			String grp1 = fnGrp[i];
			
			JLabel lbGrp = new JLabel(grp1);
			lbGrp.setFont(StdFont.Fnt16);
			lbGrp.setAlignmentX(LEFT_ALIGNMENT);
			lbGrp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
			lbGrp.setForeground(Color.WHITE);
			lbGrp.setBackground(Color.decode("#006699"));
			lbGrp.setOpaque(true);
			lbGrp.setMaximumSize(
				new Dimension(Short.MAX_VALUE, lbGrp.getPreferredSize().height));
			
			pnFn.add(lbGrp);
			
			JPanel pnItm = new JPanel();
			pnItm.setOpaque(false);
			pnItm.setLayout(new GridLayout(0, 3, 10, 10));
			for (int j=0; j < fnGrpDt[i].length; j++) {
				pnItm.add(crMnuBt(fnGrpDt[i][j], this));
			}
			pnItm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 105));
			pnItm.setAlignmentX(LEFT_ALIGNMENT);
			
			pnFn.add(pnItm);
		}
		
		pnFn.add(Box.createVerticalStrut(25));
		JPanel _pnFn = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
		_pnFn.setBackground(Color.decode("#E9E9E9"));
		_pnFn.add(pnFn);
		JScrollPane scpFn = new JScrollPane(_pnFn, 
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scpFn.getVerticalScrollBar().setPreferredSize(
			new Dimension(30, scpFn.getVerticalScrollBar().getPreferredSize().height));
		scpFn.setBorder(null);
		
		this.getContentPane().add(scpFn, BorderLayout.CENTER);
		
		// Command

		Button btClose = Button.newButton("Close,bt_close", this);

		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.add(btClose);
		pnCmd.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

		this.getContentPane().add(pnCmd, BorderLayout.PAGE_END);
		
		this.pack();
		this.setSize(600, 560);
		this.setResizable(false);
		this.setLocationRelativeTo(this.getParent());
	}
	
	private static JButton crMnuBt(String cmdStr, ActionListener lst) {
		JButton bt1 = new JButton(cmdStr);
		bt1.setFont(StdFont.Fnt14);
		bt1.setFocusable(false);
		bt1.setActionCommand(cmdStr);
		bt1.addActionListener(lst);
		bt1.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY), 
			BorderFactory.createEmptyBorder(10, 15, 10, 15)));
		return bt1;
	}
	
	private void disposeDialog() {
		this.dispose();
	}

	private void doAdjustTip() {
		DlgBox dlg1 = new DlgBox(this);
		String msg1 = _doAdjustTip(dlg1);
		if ("Tip Updated".equals(msg1)) {
			return;
		}
		if ("Cancel".equals(msg1)) {
			return;
		}
		if ("No Update".equals(msg1)) {
			return;
		}
		
		dlg1.showDialog("Error", msg1, DlgType.Warning);
		if ("bt_close".equals(dlg1.getUsrRsp())) {
		}
	}
	
	private TransactionResponse resp;
	private String _doAdjustTip(DlgBox dlg1) {
		
		TbShift stl1 = null;
		try {
			stl1 = TbShift.findLastShift();
		} catch (SQLException e1) {
			e1.printStackTrace();
			return e1.getMessage();
		}
		if (null == stl1) {
			return "Error: Last shift not found..";
		}
		
		DlgInpNum dlgNum = new DlgInpNum(this);
		dlgNum.showDialog("Order No.?", "");
		if (!"bt_ok".equals(dlgNum.getUsrRsp())) {
			return "Cancel";
		}
		
		int ordNo = dlgNum.getIntValue();
		try {
			TbPayment[] pm1s = TbPayment.getPaymentHistByOrdNo(ordNo);
			if (null == pm1s || pm1s.length <= 0) {
				return String.format("Error: Order No. [%d] No Payment Found.", ordNo);
			}
			int ccCnt = 0;
			for (TbPayment pm1 : pm1s) {
				if (pm1.isSettle()) {
					return "Payment was settled.";
				}
				if (PayBy.CreditCard != pm1.getPmPayBy()) {
					continue;
				}
				ccCnt += 1;
			}
			if (ccCnt <= 0) {
				return String.format("Order No. [%d], No Credit Card Payment.", ordNo);
			}
			TbPayment pmnt = null;
			if (ccCnt > 1) {
				dlgNum = new DlgInpNum(this);
				dlgNum.showDialog("Last 4 digit CC No.?", "");
				if ("bt_ok".equals(dlgNum.getUsrRsp())) {
					String cc4dg = dlgNum.getStrValue();
					if (cc4dg.isEmpty()) {
						return "Error: Invalid Last 4 digits.";
					}
					for (TbPayment pm1 : pm1s) {
						if (PayBy.CreditCard != pm1.getPmPayBy()) continue;
						if (cc4dg.equals(pm1.getCCLast4Digits())) {
							pmnt = pm1;
							break;
						}
					}
					if (null == pmnt) {
						return "Error: Payment Not Found.";
					}
					if (CCCapture.Success == pmnt.getPmCapture()) {
						// return String.format("TIP is captured (Tip=$%.2f).", pmnt.getPmTip());
					}
				} else {
					return "Cancel";
				}
			} else { // 1 credit card payment
				for (TbPayment pm1 : pm1s) {
					if (PayBy.CreditCard != pm1.getPmPayBy()) continue;
					pmnt = pm1;
					break;
				}
			}
			
			dlgNum = new DlgInpNum(this);
			dlgNum.showDialog("Tip?", pmnt.getPmTip());
			if ("bt_ok".equals(dlgNum.getUsrRsp())) {
				double tipAmt = dlgNum.getDoubleValue();
				System.out.printf("tip -> [%.2f]\n", tipAmt);
				boolean tipOk = true;
				if (tipAmt > (pmnt.getPmAmt() * 0.20)) {
					dlg1.showConfirmDialog("Tip > 20%", 
						String.format("<html>Warning!<br>Tip [$%.2f] is over 20%% of [$%.2f],<br>Continue?</html>", tipAmt, pmnt.getPmAmt()), 
						DlgType.Warning);
					tipOk = "bt_ok".equals(dlg1.getUsrRsp());
				}
				if (!tipOk) {
					return "Cancel";
				}
				// update tip
				
				// capture tx+tip
				final TbPayment pmx = pmnt;
				final DlgThread dlgTrd = new DlgThread(this);
				Thread trd1 = new Thread() {
					public void run() {
						dlgTrd.setExitCode(0);
						dlgTrd.setText("Processing Tip ... ");
						try {
							resp = PaymentGateway.captureTrans(pmx);
							ResData.saveTransactionResponse(pmx.getPmNo(), resp);
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
				System.out.printf("(Adjust Tip) done with [%d]\n", dlgTrd.getExitCode());
				// Approved
				if ("A".equals(resp.getResultCode())) {
					pmnt.setPmCapture(CCCapture.Success);
					pmnt.setPmTip(tipAmt);
					pmnt.save();
					return "Tip Updated";
				} else { // Not Approved
					return String.format("<html>Error = %s (Code %d)<br><b>%s</b></html>", 
						resp.getResult(), resp.getErrorCode(), resp.getError());
				}
				// capture tx+tip
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		
		return "No Update";
	}
	
	private void showBatchStatus() {
		String msg1 = "Batch Status Error: NA";
		BatchStatus bstat = null;
		try {
			bstat = PaymentGateway.getCurrentBatchStatus();
			msg1 = String.format("<html><table cellspacing=0 cellpadding=3>"
				+ "<tr><td>Batch RefNo</td><td><b>%d</b></tr>"
				+ "<tr><td>Status</td><td><b>%s</b></tr>"
				+ "<tr><td>Credit Amount</td><td>%.2f</tr>"
				+ "<tr><td>Tx Count</td><td>%d</tr>"
				+ "<tr><td>Net Amount</td><td>%.2f</tr>"
				+ "</table></html>", 
				bstat.getBatchRefNum(), 
				bstat.getStatus(),
				bstat.getCreditsAmount(),
				bstat.getCreditsCount(),
				bstat.getNetAmount()
				);
		} catch (Exception e) {
			//e.printStackTrace();
			msg1 = String.format("<html>Batch Status Error:<br>%s</html>", e.getMessage());
		}
		DlgBox dlg1 = new DlgBox(this);
		dlg1.showDialog("Settlement Status", msg1, DlgType.Information);
		/*
		if ("bt_close".equals(dlg1.getUsrRsp())) {
		}
		*/
	}
	
	private void viewOrderDetail() {
		DlgInpNum dlgNum = new DlgInpNum(this);
		dlgNum.showDialog("Order No.?", "");
		if (!"bt_ok".equals(dlgNum.getUsrRsp())) {
			return;
		}
		String ordNoStr = dlgNum.getStrValue();
		if (ordNoStr.isEmpty()) {
			return;
		}
		try {
			int tmpNo = Integer.parseInt(ordNoStr);
			TbOrder pord = TbOrder.getTbOrderByOrdNo(tmpNo);
			if (null == pord) {
				_showError(String.format(
					"<html><center><font size=6>Order# [%d] is not found.</font></center></html>", 
					tmpNo));
				return;
			}

			System.out.printf(" -> Id [%d] No [%d]\n", pord.getOrdId(), pord.getOrdNo());

			String ordTxt = PosReport.genOrderDetail(pord);
			DlgTEXT dlgWeb = new DlgTEXT(this);
			dlgWeb.showDialog(ordTxt, String.format("Order# %d", pord.getOrdNo()));
			
		} catch (Exception e) {
			_showError(e.getMessage());
		}
	}
	
	private void rePrintOrder() {
		DlgInpNum dlgNum = new DlgInpNum(this);
		dlgNum.showDialog("Order No.?", "");
		if (!"bt_ok".equals(dlgNum.getUsrRsp())) {
			return;
		}
		String ordNoStr = dlgNum.getStrValue();
		if (ordNoStr.isEmpty()) {
			return;
		}
		try {
			int tmpNo = Integer.parseInt(ordNoStr);
			final TbOrder pord = TbOrder.getTbOrderByOrdNo(tmpNo);
			if (null == pord) {
				_showError(String.format(
					"<html><center><font size=6>Order# [%d] is not found.</font></center></html>", 
					tmpNo));
				return;
			}
			
			final String[] prnOpts = {
				"Kitchen", 
				pord.getOrdType().equals("DineIn") ? "Runner" : "Packing", 
				"Check/Receipt"};
			
			DlgSelOpt dlgOpt = DlgSelOpt.newInstance(this, "Select Print Option", prnOpts);
			dlgOpt.showDialog();
			System.out.println(dlgOpt.getUsrRsp());
			if (!"bt_ok".equals(dlgOpt.getUsrRsp())) {
				return;
			}
			
			String cmd1 = dlgOpt.getUsrSelOpt();
			
			if (!pord.isActive()) {
				_showError(String.format("Order# [%d] has been deleted.", pord.getOrdNo()));
				return;
			}
			if ("Kitchen".equals(cmd1)) {
				Thread trd1 = new Thread() {
					public void run() {
						try {
							RcpPrinter.printKitchen(pord, true);
						} catch (Exception e) {
							_showError(e.getMessage());
						}
					}
				};
				trd1.start();
			} else if ("Runner".equals(cmd1)) {
				Thread trd1 = new Thread() {
					public void run() {
						try {
							RcpPrinter.printRunner(pord, true);
						} catch (Exception e) {
							_showError(e.getMessage());
						}
					}
				};
				trd1.start();
			} else if ("Packing".equals(cmd1)) {
				Thread trd1 = new Thread() {
					public void run() {
						try {
							RcpPrinter.printOrderTo(pord, PrintTo.Packing);
						} catch (Exception e) {
							_showError(e.getMessage());
						}
					}
				};
				trd1.start();
			} else if ("Check/Receipt".equals(cmd1)) {
				if (!pord.isChecked()) {
					_showError(String.format("Order# [%d] is not checked.", pord.getOrdNo()));
					return;
				}
				Thread trd1 = new Thread() {
					public void run() {
						try {
							RcpPrinter.printOrderTo(pord, PrintTo.Cashier);
						} catch (Exception e) {
							_showError(e.getMessage());
						}
					}
				};
				trd1.start();
			} 
			
		} catch (Exception e) {
			_showError(e.getMessage());
		}
	}
	
	private void _showError(String msg) {
		DlgBox dlgx = new DlgBox(this);
		dlgx.showDialog("Error", msg, DlgType.Critical);
	}
	
	// public
	
	public void showDialog() {
		this.setVisible(true);
	}
	
	public String getUsrRsp() {
		return usrRsp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		usrRsp = e.getActionCommand();
		if ("bt_close".equals(usrRsp)) {
			disposeDialog();
		} else if ("Reopen".equals(usrRsp)) {
			disposeDialog();
		} else if ("RePrint".equals(usrRsp)) {
			rePrintOrder();
		} else if ("Unlock".equals(usrRsp)) {
			disposeDialog();
		} else if ("Close Shift".equals(usrRsp)) {
			disposeDialog();
		} else if ("Shift Report".equals(usrRsp)) {
			disposeDialog();
		} else if ("Settlement Report".equals(usrRsp)) {
			disposeDialog();
		} else if ("Clock Report".equals(usrRsp)) {
			disposeDialog();
		} else if ("Adjust Tip".equals(usrRsp)) {
			//doAdjustTip();
			disposeDialog();
		} else if ("Settlement".equals(usrRsp)) {
			disposeDialog();
		} else if ("Batch Status".equals(usrRsp)) {
			showBatchStatus();
		} else if ("Order Detail".equals(usrRsp)) {
			viewOrderDetail();
		} else if ("Transfer".equals(usrRsp)) {
			disposeDialog();
		} else if ("Open Cash Drawer".equals(usrRsp)) {
			StarPrinter.kickDrawer();
		} else if ("Update Zero Tips".equals(usrRsp)) {
			disposeDialog();
		}
	}

	// main
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
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

		DlgFunc dlg1 = new DlgFunc(frm1);
		dlg1.showDialog();
		
		System.exit(0);
	}
}
