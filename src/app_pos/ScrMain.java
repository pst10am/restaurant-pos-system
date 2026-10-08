package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import print.RcpPrinter;
import print.StarPrinter;
import refx.ClockType;
import refx.DlgType;
import refx.TableService;
import resrc.ClockEmp;
import resrc.ResCfg;
import resrc.ResUtil;
import model.CallInfo;
import model.FindCustInfo;
import model.RptData2;
import model.TbClock;
import model.TbOrder;
import model.TbCust;
import model.TbSettle;
import model.TbShift;
import model.TxSctTable;
import model.TxUser;

public class ScrMain extends JPanel implements ActionListener, PnTxSctIntf {
	private static final long serialVersionUID = 1L;
	
	private ScrIntf mintf;
	private PnTxSct pnSect;
	
	public ScrMain(ScrIntf _smIntf) {
		super(new BorderLayout());
		mintf = _smIntf;
		initComponents();
	}
	
	private void initComponents() {
		pnSect = new PnTxSct(this);
		this.add(pnSect, BorderLayout.CENTER);
		crBtmCmd();
	}
	
	private void crBtmCmd() {
		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		
		Button btPay = Button.newButton("Pay,bt_pay", this);
		Button btGift = Button.newButton("Gift,mng_gift", this);
		Button btClk = Button.newButton("Clock,bt_clock", this);
		Button btTks = Button.newButton("Tickets,bt_tickets", this);
		Button btFn = Button.newButton("Fn*,bt_func", this);
		Button btNewTogo = Button.newButton("+ToGo,new_togo", this);
		Button btWFP = Button.newButton("WFP,bt_pick", this);
		
		pnCmd.add(btPay);
		pnCmd.add(btGift);
		pnCmd.add(btClk);
		pnCmd.add(btTks);
		pnCmd.add(btFn);
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.add(btWFP);
		pnCmd.add(btNewTogo);
		
		pnCmd.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
		this.add(pnCmd, BorderLayout.PAGE_END);
	}
	
	private void newToGoOrder() {
		DlgSelectCaller dlgCid = DlgSelectCaller.getInstance(mintf.getFrame());
		dlgCid.showDialog();
		if ("bt_cancel".equals(dlgCid.getUsrRsp())) {
			return;
		}
		
		TbCust cst1 = null;
		if ("line_selected".equals(dlgCid.getUsrRsp())) {
			CallInfo cif1 = dlgCid.getCallInfo();
			String telNo = cif1.getRawPhoneNo();
			//System.out.printf(" --> [%s]\n", telNo);
			java.util.Vector<FindCustInfo> fndArr = FindCustInfo.searchPhone(telNo);
			if (null != fndArr && fndArr.size() > 0) {
				cst1 = TbCust.getCustById(fndArr.get(0).getCstId());
			} else {
				cst1 = TbCust.newInstance();
				cst1.setCstName(cif1.name);
				cst1.setCstPhone(telNo);
			}
		}
		
		TbOrder[] orders = new TbOrder[1];
		TbOrder ord1 = TbOrder.newToGoOrder();
		if (null != cst1) {
			ord1.updateCustomerInfo(cst1);
		}
		orders[0] = ord1;

		mintf.showOrderScreen(orders);
	}
	
	private void _payOrder(TbOrder pord) {
		DlgPayment dlg1 = new DlgPayment(mintf.getFrame());
		dlg1.showDialog(pord);
		/*
		if ("bt_close".equals(dlg1.getUsrRsp())) {
		}
		*/
		refresh();
	}
	
	private void doPay() {
		DlgInpNum dlgNum = new DlgInpNum(mintf.getFrame());
		dlgNum.showDialog("Order No.?", "");
		if ("bt_ok".equals(dlgNum.getUsrRsp())) {
			try {
				String ordNoStr = dlgNum.getStrValue();
				if (ordNoStr.isEmpty()) {
					return;
				}
				int tmpNo = Integer.parseInt(ordNoStr);
				TbOrder pord = TbOrder.getTbOrderByOrdNo(tmpNo);
				if (null != pord) {
					if (pord.getOrdCntCheck() > 0) {
						_payOrder(pord);
					} else {
						DlgBox dlg1 = new DlgBox(mintf.getFrame());
						dlg1.showDialog("Order Not Check", 
							String.format("<html><center><font size=6>Order No. %d is not checked.,<br>Please check order before pay.</font></center></html>", tmpNo),
							DlgType.Information);
					}
				} else {
					DlgBox dlg1 = new DlgBox(mintf.getFrame());
					dlg1.showDialog("Order Not Found", 
						String.format("<html><center><font size=6>Order No. %d, not found.</font></center></html>", tmpNo),
						DlgType.Information);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	// WFP
	private void waitForPick() {
		DlgWFP dlg1 = new DlgWFP(mintf.getFrame());
		dlg1.showDialog();
	}
	
	private void fnReopen() {
		DlgInpNum dlgNum = new DlgInpNum(mintf.getFrame());
		dlgNum.showDialog("Order No.?", "");
		if ("bt_ok".equals(dlgNum.getUsrRsp())) {
			try {
				String ordNoStr = dlgNum.getStrValue();
				if (ordNoStr.isEmpty()) {
					return;
				}
				int tmpNo = Integer.parseInt(ordNoStr);
				TbOrder pord = TbOrder.getTbOrderByOrdNo(tmpNo);
				if (null != pord) {
					if (pord.isPaid()) {
						DlgBox dlg1 = new DlgBox(mintf.getFrame());
						dlg1.showDialog("Order has been paid.", 
							String.format("<html><center><font size=6>Order# [%d] has been paid.</font></center></html>", tmpNo),
							DlgType.Information);
						return;
					}
					// reopen order
					System.out.printf(" -> Id [%d] No [%d]\n", pord.getOrdId(), pord.getOrdNo());
					TbOrder[] orders = new TbOrder[1];
					orders[0] = TbOrder.findOrderById(pord.getOrdId());
					orders[0].reopen();
					if (pord.getTblId() > 0) {
						TxSctTable.lockTable(pord.getTblId(), true);
					}
					mintf.showOrderScreen(orders);
				} else {
					DlgBox dlg1 = new DlgBox(mintf.getFrame());
					dlg1.showDialog("Order Not Found", 
						String.format("<html><center><font size=6>Order No. %d,<br>Not found.</font></center></html>", tmpNo),
						DlgType.Information);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void fnUnlockTable() {
		try {
			TxSctTable[] tbls = TxSctTable.getLockedTables();
			DlgSelTable dlg1 = new DlgSelTable(mintf.getFrame());
			dlg1.showDialog("Select Table", tbls);
			if ("OK".equals(dlg1.getUsrRsp())) {
				TxSctTable tbl1 = dlg1.getSelectedValue();
				TxSctTable.lockTable(tbl1.getTblId(), false);
				pnSect.tableHasUpdated();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void fnSettlement() {
		DlgBox bx1 = new DlgBox(mintf.getFrame());
		bx1.showConfirmDialog(
			"Settlement", 
			"<html><center>*Please make sure <u>ALL Tips</u> is captured before Settlement*<br>Settlement Now?</center></html>", 
			DlgType.Information);
		if (!"bt_ok".equals(bx1.getUsrRsp())) {
			return;
		}
		
		final DlgThread dlgTrd = new DlgThread(mintf.getFrame());
		Thread trd1 = new Thread() {
			public void run() {
				dlgTrd.setExitCode(0);
				dlgTrd.setText("Settlement ... \n");
				try {
					dlgTrd.setText("Processing ... Please Wait\n");
					PaymentGateway.closeBatch();
					dlgTrd.setExitCode(1);
					//
					TbSettle.saveInstance();
					//
					dlgTrd.append("Done.\n");
					//dlgTrd.dispose();
				} catch (Exception e) {
					dlgTrd.append(String.format("Processing Error\n%s", e.getMessage()));
					dlgTrd.append("\n");
				}
				//dlgTrd.append(String.format("Exit Code [%d]", dlgTrd.getExitCode()));
				dlgTrd.processEnd();
			}
		};
		dlgTrd.showDialog(trd1);
		System.out.printf("settlement done with [%d]\n", dlgTrd.getExitCode());
		//
		/*
		String msg1 = "Batch Status Error: NA";
		BatchStatus bstat = null;
		try {
			bstat = PaymentGateway.getCurrentBatchStatus();
			msg1 = String.format("<html><table>"
				+ "<tr><td>Batch RefNo.</td><td>%d (%s)</tr>"
				+ "<tr><td>Status</td><td><b>%s</b></tr>"
				+ "<tr><td>Credit Amount</td><td>%.2f</tr>"
				+ "<tr><td>Credit Tx Count</td><td>%d</tr>"
				+ "<tr><td>Net Amount</td><td>%.2f</tr>"
				+ "</table></html>", 
				bstat.getBatchRefNum(), 
				bstat.getClosed(),
				bstat.getStatus(),
				bstat.getCreditsAmount(),
				bstat.getCreditsCount(),
				bstat.getNetAmount()
				);
		} catch (Exception e) {
			e.printStackTrace();
			msg1 = String.format("<html>Batch Status Error:<br>%s</html>", e.getMessage());
		}
		DlgBox dlg1 = new DlgBox(mintf.getFrame());
		dlg1.showDialog("Settlement Status", msg1, DlgType.Information);
		if ("bt_close".equals(dlg1.getUsrRsp())) {
		}
		*/
	}
	
	private void fnCloseShift() {
		
		DlgBox box1 = new DlgBox(mintf.getFrame());
		box1.showConfirmDialog(
			"Close Shift"
			,String.format("Close Shift [%s]?", ResUtil.dtoc(new java.util.Date(), "M/d/yy hh:mm a"))
			,DlgType.Warning);
		if (!"bt_ok".equals(box1.getUsrRsp())) {
			return;
		}
		
		DlgBox dlg1 = new DlgBox(mintf.getFrame());
		try {
			TbShift cl1 = TbShift.newInstance();
			cl1.save();
			dlg1.showDialog(
				"Success", 
				"<html><center>New Shift is saved.</center></html>",
				DlgType.Information);
		} catch (SQLException e) {
			e.printStackTrace();
			dlg1.showDialog("Error", e.getMessage(), DlgType.Critical);
		}
	}
	
	private void fnSaleReportByShift() {
		DlgSelShift dlg1 = new DlgSelShift(mintf.getFrame());
		dlg1.showDialog();
		if ("bt_close".equals(dlg1.getUsrRsp())) {
			return;
		}
		TbShift sTo = dlg1.getSelectedValue();
		try {
			TbShift sFrm = TbShift.findPrevShift(sTo);
			System.out.printf("\nSale Report:\n"
					+ "From: %d %s\n"
					+ "  To: %d %s\n\n\n", 
				sFrm.getClId(), ResUtil.dtoc(sFrm.getClDt(), "MM/dd/yy HH:mm:ss"), 
				sTo.getClId(), ResUtil.dtoc(sTo.getClDt(), "MM/dd/yy HH:mm:ss"));
			
			genSaleReport(sFrm.getClDt(), sTo.getClDt(), "Summary Report: Shift", dlg1.getUsrRsp());
			
		} catch (Exception e) {
			e.printStackTrace();
			DlgBox box1 = new DlgBox(mintf.getFrame());
			box1.showDialog(
				"Error", 
				String.format("<html>Error:<br>%s</html>", e.getMessage()), 
				DlgType.Critical);
		}
	}
	
	private void fnSaleReportBySettlement() {
		DlgSelSettle dlg1 = new DlgSelSettle(mintf.getFrame());
		dlg1.showDialog();
		if ("bt_close".equals(dlg1.getUsrRsp())) {
			return;
		}
		TbSettle sTo = dlg1.getSelectedValue();
		try {
			TbSettle sFrm = TbSettle.findPrevSettle(sTo);
			System.out.printf("\nSale Report:\n"
					+ "From: %d %s\n"
					+ "  To: %d %s\n\n\n", 
				sFrm.getStlId(), ResUtil.dtoc(sFrm.getStlDt(), "MM/dd/yy HH:mm:ss"), 
				sTo.getStlId(), ResUtil.dtoc(sTo.getStlDt(), "MM/dd/yy HH:mm:ss"));
			
			genSaleReport(sFrm.getStlDt(), sTo.getStlDt(), "Summary Report: Settlement", dlg1.getUsrRsp());
			
		} catch (Exception e) {
			e.printStackTrace();
			DlgBox box1 = new DlgBox(mintf.getFrame());
			box1.showDialog(
				"Error", 
				String.format("<html>Error:<br>%s</html>", e.getMessage()), 
				DlgType.Critical);
		}
	}
	
	private void genSaleReport(java.util.Date sFrm, java.util.Date sTo, String rptTitle, String rptOpt) {
		try {
			RptData2 rptDt = RptData2.newInstance();
			rptDt.runData(sFrm, sTo);
			
			System.out.println("report option -> "+ rptOpt);
			
			if (rptOpt.startsWith("prn_")) {
				File rptFile = PosReport.genSaleReportB(rptDt, rptTitle,
					"prn_with_detail".equals(rptOpt));
				if (null != rptFile && rptFile.exists()) {
					if (ResCfg.sendToPrinter()) {
						StarPrinter prn1 = StarPrinter.newInstance();
						prn1.addFile(rptFile);
						prn1.startPrint();
					}
				}
			}
			
			// view
			if ("bt_view".equals(rptOpt)){
				File rptFile = PosReport.genSaleReportB(rptDt, rptTitle, true);
				if (null != rptFile && rptFile.exists()) {
					DlgTEXT dlgTxt = new DlgTEXT(mintf.getFrame());
					dlgTxt.showDialog(rptFile, rptTitle);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			DlgBox box1 = new DlgBox(mintf.getFrame());
			box1.showDialog(
				"Error", 
				String.format("<html>Error:<br>%s</html>", e.getMessage()), 
				DlgType.Critical);
		}
	}
	
	private void fnClockReport() {
		DlgSelShift dlg1 = new DlgSelShift(mintf.getFrame());
		dlg1.showDialog();
		if ("bt_close".equals(dlg1.getUsrRsp())) {
			return;
		}
		System.out.println(dlg1.getUsrRsp());
		TbShift clUp = dlg1.getSelectedValue();
	}
	
	private void doClockEmp() {
		DlgUsr dlgUsr = new DlgUsr(mintf.getFrame());
		dlgUsr.showDialog();
		if ("bt_ok".equals(dlgUsr.getUsrRsp())) {
			//System.out.printf(">>> [%s]\n", dlgUsr.getUser());
			TxUser usr1 = dlgUsr.getUser();
			try {
				ClockType clt = ClockEmp.getLastClock(usr1.getUsrId());
				
				ClockType clt2 = ClockType.In;
				if (ClockType.In == clt) {
					clt2 = ClockType.Out;
				}
				
				DlgBox dlgbx1 = new DlgBox(mintf.getFrame());
				dlgbx1.showConfirmDialog(
					"Clock "+ clt2.toString(), 
					String.format("Hi, %s [Clock %s]?", usr1.getUsrName(), clt2.toString()), 
					DlgType.Information);
				if ("bt_ok".equals(dlgbx1.getUsrRsp())) {
					TbClock clk1 = TbClock.newInstance(usr1, clt2);
					ClockEmp.clock(clk1);
					//
					RcpPrinter.printClockSlip(clk1);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void fnTransfer() {
		try {
			TxSctTable[] tbls = TxSctTable.getServiceTables();
			DlgSelTable dlg1 = new DlgSelTable(mintf.getFrame());
			dlg1.showDialog("From Table", tbls);
			if (!"OK".equals(dlg1.getUsrRsp())) {
				return;
			}
			
			TxSctTable tbl1 = dlg1.getSelectedValue();
			
			TxSctTable[] tbls2 = TxSctTable.getAllTables(tbl1.getTblId());
			if (null == tbls2) return;
			
			dlg1.showDialog("To Table", tbls2);
			if (!"OK".equals(dlg1.getUsrRsp())) {
				return;
			}
			
			TxSctTable tbl2 = dlg1.getSelectedValue();
			
			TbOrder[] ordFrm = TbOrder.findOrderByTableId(tbl1.getTblId());
			TbOrder[] ordTo = TbOrder.findOrderByTableId(tbl2.getTblId());
			
			if (null == ordTo) {
				
			}
			
			DlgOrderTransfer dlgTrn = new DlgOrderTransfer(mintf.getFrame(), ordFrm[0], ordTo[1]);

			pnSect.tableHasUpdated();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void fnAdjustTip() {
		DlgTips dlg1 = DlgTips.newInstance(mintf.getFrame());
		dlg1.showDialog();
	}
	
	private void doFunc() {
		DlgFunc dlg1 = new DlgFunc(mintf.getFrame());
		dlg1.showDialog();
		if ("Reopen".equals(dlg1.getUsrRsp())) {
			fnReopen();
		} else if ("Unlock".equals(dlg1.getUsrRsp())) {
			fnUnlockTable();
		} else if ("Close Shift".equals(dlg1.getUsrRsp())) {
			fnCloseShift();
		} else if ("Settlement".equals(dlg1.getUsrRsp())) {
			fnSettlement();
		} else if ("Shift Report".equals(dlg1.getUsrRsp())) {
			fnSaleReportByShift();
		} else if ("Settlement Report".equals(dlg1.getUsrRsp())) {
			fnSaleReportBySettlement();
		} else if ("Clock Report".equals(dlg1.getUsrRsp())) {
			// fnClockReport();
		} else if ("Transfer".equals(dlg1.getUsrRsp())) {
			// fnTransfer();
		} else if ("Adjust Tip".equals(dlg1.getUsrRsp())) {
			fnAdjustTip();
		}
	}
	
	private void doTickets() {
		DlgTickets dlgTks = DlgTickets.newInstance(mintf.getFrame());
		dlgTks.showDialog();
	}
	
	// ----------------------------------------------------
	
	public void refresh() {
		pnSect.refresh();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ("new_togo".equals(cmd)) {
			newToGoOrder();
		} else if ("bt_pay".equals(cmd)) {
			doPay();
		} else if ("mng_gift".equals(cmd)) {
			DlgGift dlgGft = DlgGift.newInstance(mintf.getFrame());
			dlgGft.showDialog();
		} else if ("bt_pick".equals(cmd)) {
			waitForPick();
		} else if ("bt_reopen".equals(cmd)) {
			fnReopen();
		} else if ("bt_clock".equals(cmd)) {
			doClockEmp();
		} else if ("bt_func".equals(cmd)) {
			doFunc();
		} else if ("bt_tickets".equals(cmd)) {
			doTickets();
		} 
	}

	@Override
	public void tableSelected(final TxSctTable tbl1) {
		try {
			if (tbl1.isLocked()) {
				return;
			}
			TxSctTable.lockTable(tbl1.getTblId(), true);
			
			DlgThread dlg1 = new DlgThread(mintf.getFrame());
			ThreadTableInfo tblInf1 = new ThreadTableInfo(dlg1, tbl1);
			dlg1.showDialog(tblInf1);
			if (10 == dlg1.getExitCode()) {
				// paid
				DlgBox dlgBx = new DlgBox(mintf.getFrame());
				dlgBx.showConfirmDialog("Table Ready?", "Table is ready for service?", DlgType.Information);
				if ("bt_ok".equals(dlgBx.getUsrRsp())) {
					TxSctTable.updateServiceStatus(tbl1.getTblId(), TableService.Ready);
				}
				TxSctTable.lockTable(tbl1.getTblId(), false);
				//
				pnSect.tableHasUpdated();
			} else if (20 == dlg1.getExitCode()) {
				// checked
				TbOrder pord = null;
				//
				TbOrder[] cords = TbOrder.getTbOrderByTblId_Checked(tbl1.getTblId());
				if (cords.length > 1) {
					DlgSelOrd dlgs1 = new DlgSelOrd(mintf.getFrame());
					dlgs1.showDialog(tbl1, cords);
					if ("OK".equals(dlgs1.getUsrRsp())) {
						pord = dlgs1.getSelectedValue();
					}
				} else if (cords.length == 1) {
					pord = cords[0];
				}
				if (null != pord) {
					System.out.printf(
						"\n -> ord_id=%d ord_no=%d ord_amt_net=$%.2f\n", 
						pord.getOrdId(), pord.getOrdNo(), pord.getOrdAmtNet());
					_payOrder(pord);
				}
				TxSctTable.lockTable(tbl1.getTblId(), false);
				//
				pnSect.tableHasUpdated();
			} else if (30 == dlg1.getExitCode()) {
				// ready, service
				mintf.showOrderScreen(tblInf1.getOrders());
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
