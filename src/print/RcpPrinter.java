package print;

import java.io.File;
import refx.OrderType;
import refx.PayBy;
import refx.PrintTo;
import refx.SlipType;
import resrc.ResCfg;
import model.TbClock;
import model.TbOrder;
import model.TbPayment;

public class RcpPrinter {
	
	private static SlipGen slipGen;
	static {
		try {
			Class<?> cls1 = Class.forName(ResCfg.getSlipGenClassName());
			slipGen = (SlipGen)cls1.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private RcpPrinter() {}
	
	private static void printOrderTo(StarPrinter prn1, TbOrder _ord1, boolean _allFlg, boolean _runnerFlg, PrintTo ... _prt) throws Exception {
		for (PrintTo prto : _prt) {
			if (PrintTo.Kitchen == prto) {
				File[] pfiles = slipGen.genKitchenSlip(_ord1, _allFlg, _runnerFlg);
				if ((null != pfiles) && (pfiles.length > 0)) {
					for (File pf1 : pfiles) {
						prn1.addFile(pf1);
					}
				}
				_ord1.kitchenPrinted();
			}
			if (PrintTo.Packing == prto) {
				if (OrderType.ToGo == _ord1.getOrdType()) {
					//File pfile = slipGen.genPackingSlip(_ord1);
					File pfile = slipGen.genSlip(_ord1, SlipType.Packing);
					// System.out.printf("packing -> [%s]\n", pfile.getName());
					prn1.addFile(pfile);
				}
			}
			if (PrintTo.Cashier == prto) {
				if (_ord1.getOrdCntCheck() == 1) {
					File pfile = slipGen.genSlip(_ord1, SlipType.Cashier);
					// System.out.printf("packing -> [%s]\n", pfile.getName());
					prn1.addFile(pfile);
				}
			}
		}
	}
	
	// miscellaneous
	
	public static void printOrderTo(java.util.Vector<TbOrder> orders, PrintTo ... _prt) throws Exception {
		StarPrinter prn1 = StarPrinter.newInstance();
		for (TbOrder ord1 :  orders) {
			printOrderTo(prn1, ord1, false, false, _prt);
		}
		if (ResCfg.sendToPrinter()) {
			prn1.startPrint();
		}
	}
	
	public static void printOrderTo(TbOrder _ord1, PrintTo ... _prt) throws Exception {
		StarPrinter prn1 = StarPrinter.newInstance();
		printOrderTo(prn1, _ord1, false, false, _prt);
		if (ResCfg.sendToPrinter()) {
			prn1.startPrint();
		}
	}
	
	public static void printKitchen(TbOrder _ord1, boolean _allFlg) throws Exception {
		StarPrinter prn1 = StarPrinter.newInstance();
		printOrderTo(prn1, _ord1, _allFlg, false, PrintTo.Kitchen);
		if (ResCfg.sendToPrinter()) {
			prn1.startPrint();
		}
	}
	
	public static void printRunner(TbOrder _ord1, boolean _allFlg) throws Exception {
		StarPrinter prn1 = StarPrinter.newInstance();
		printOrderTo(prn1, _ord1, _allFlg, true, PrintTo.Kitchen);
		if (ResCfg.sendToPrinter()) {
			prn1.startPrint();
		}
	}
	
	public static void printCreditCardSign(java.util.Vector<TbPayment> pms) throws Exception {
		boolean hasCC = false;
		for (TbPayment pm1 : pms) {
			hasCC = PayBy.CreditCard == pm1.getPmPayBy();
			if (hasCC) {
				break;
			}
		}
		if (!hasCC) {
			// nothing to print, no credit card used.
			return;
		}
		//
		StarPrinter prn1 = StarPrinter.newInstance();
		for (TbPayment pm1 : pms) {
			if (PayBy.CreditCard != pm1.getPmPayBy()) {
				continue;
			}
			prn1.addFile(slipGen.genCreditCardSign(pm1));
		}
		if (ResCfg.sendToPrinter()) {
			prn1.startPrint();
		}
	}
	
	public static void printClockSlip(TbClock clk1) throws Exception {
		StarPrinter prn1 = StarPrinter.newInstance();
		prn1.addFile(slipGen.genClockSlip(clk1));
		if (ResCfg.sendToPrinter()) {
			prn1.startPrint();
		}
	}
}
