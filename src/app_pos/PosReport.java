package app_pos;

import static print.PrnConst.pESC;

import java.io.File;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Scanner;

import refx.AmountType;
import refx.CCCapture;
import refx.OrderType;
import refx.PayBy;
import refx.PaySrc;
import refx.RptSaleType;
import refx.ToGoType;
import resrc.ResCfg;
import resrc.ResUtil;
import model.RptData2;
import model.RptPaymentInfo;
import model.RptSaleInfo;
import model.TbOrder;
import model.TbOrderDC;
import model.TbOrderItem;
import model.TbPayment;
import model.TxPrinter;
import model.RptFld;

public class PosReport {
	private PosReport() {}

	public static String genOrderDetail(TbOrder ord1) {
		
		StringBuilder taDt = new StringBuilder();
		
		String _typeStr = OrderType.DineIn.toString();
		if (OrderType.ToGo == ord1.getOrdType()) {
			_typeStr = ord1.getOrdToGoType().toString();
		}
		taDt.append(ResUtil.padl("*"+_typeStr+"*", 24, ' ')+"\n");
		
		if (ord1.isPaid()) {
			taDt.append("[$PAID$]\n");
		} else {
			taDt.append(">CHECK<\n");
		}
		
		taDt.append(String.format("Order: %d\n", ord1.getOrdNo()));
		
		// Customer/Table 
		if (OrderType.DineIn == ord1.getOrdType()) {
			taDt.append(String.format("Table: %s\n", ord1.getTblName()));
		} else {
			taDt.append(String.format("Name: %s\n", ord1.getCstName()));
			taDt.append(String.format("Phone: %s\n", ResUtil.formatPhone(ord1.getCstPhone())));
		}

		// Delivery
		if ((OrderType.ToGo == ord1.getOrdType()) && 
			(ToGoType.Delivery == ord1.getOrdToGoType())) {
			taDt.append(ResUtil.padStr(40, '-') +"\n");
			taDt.append("Address:\n");
			taDt.append(String.format("%s", ord1.getDeliAddr1()));
			if (ord1.getDeliAddr2().trim().length() > 0) {
				taDt.append(String.format(" %s", ord1.getDeliAddr2()));
			}
			if (ord1.getDeliUnitNo().trim().length() > 0) {
				taDt.append(String.format(" APT.  %s", ord1.getDeliUnitNo()));
			}
			taDt.append(String.format(" %s, %s", ord1.getDeliCity(), ord1.getDeliState()));
			taDt.append(String.format(" (%.2f miles.)\n", ord1.getDeliDist()));
		}
		
		if (!ord1.getCstNote().trim().isEmpty()) {
			taDt.append(ResUtil.padStr(40, '-')+"\n");
			taDt.append("Note:\n");
			taDt.append(ord1.getCstNote()+"\n");
		}
		
		taDt.append(ResUtil.padStr(40, '-')+"\n");
		taDt.append(String.format("Date/Time:%s\n", ResUtil.dtoc(ord1.getOrdDt(), "MM/dd/yyyy hh:mm:ss aaa")));

		if (OrderType.ToGo == ord1.getOrdType()) {
			// promise date-time.
			taDt.append("Promise Time:");
			java.util.Date prmDt = null;
			if (ToGoType.Delivery == ord1.getOrdToGoType()) {
				// delivery: 1 hour.
				prmDt = ResUtil.dtadd(ord1.getOrdDt(), 1, Calendar.HOUR_OF_DAY);
			} else {
				// waiting, pickup: 20 minutes.
				prmDt = ResUtil.dtadd(ord1.getOrdDt(), 20, Calendar.MINUTE);
			}
			taDt.append(ResUtil.dtoc(prmDt, "MM/dd/yyyy hh:mm:ss aaa")+"\n");
		}
		
		taDt.append(ResUtil.padStr(40, '=')+"\n");
		
		double tmp_sub_total = 0;
		double tmp_tax = 0;
		
		for (int i=0; i < ord1.getItemCount(); i++) {
			if (i > 0) {
				taDt.append(ResUtil.padStr(40, '-')+"\n");
			}
			TbOrderItem odi1 = ord1.getItems().get(i);

			taDt.append(String.format("[%s] $[%.2f] %s\n", 
				odi1.getOdiQty(), 
				odi1.getOdiAmtBf(), 
				odi1.getItmNamePos()));
			
			if (!odi1.getOpiStr().isEmpty()) {
				taDt.append(odi1.getOpiStr()+"\n");
			}
			if (!odi1.getOdiNote().trim().isEmpty()) {
				taDt.append(String.format("Note:%s\n", odi1.getOdiNote()));
			}
			//
			tmp_sub_total += odi1.getOdiAmtBf();
			tmp_tax += odi1.getOdiAmtTax();
		}
		
		double amtDisc = 0;
		if (ord1.getDiscounts().size() > 0) {
			taDt.append(ResUtil.padStr(40, '-')+"\n");
			taDt.append("Discount:\n");
			for (TbOrderDC dc1 : ord1.getDiscounts()) {
				double val1 = 0;
				if (AmountType.Percent == dc1.getDcAmtType()) {
					val1 = (tmp_sub_total+tmp_tax) * ((dc1.getDcAmt())/100);
				} else if (AmountType.Fixed == dc1.getDcAmtType()) {
					val1 = dc1.getDcAmt();
				}
				taDt.append(ResUtil.padStr(40, '-')+"\n");
				taDt.append(String.format("$%.2f (%s)%s\n",
					val1,
					dc1.getDcAmtType(),
					dc1.getDcDesc()));
				amtDisc += dc1.getOdcAmt();
			}
		}
		
		double amtCharge = 0;
		if (ord1.getCharges().size() > 0) {
			taDt.append(ResUtil.padStr(40, '-')+"\n");
			taDt.append("Charge:\n");
			for (TbOrderDC dc1 : ord1.getCharges()) {
				double val1 = 0;
				if (AmountType.Percent == dc1.getDcAmtType()) {
					val1 = (tmp_sub_total+tmp_tax) * ((dc1.getDcAmt())/100);
				} else if (AmountType.Fixed == dc1.getDcAmtType()) {
					val1 = dc1.getDcAmt();
				}
				taDt.append(ResUtil.padStr(40, '-')+"\n");
				taDt.append(String.format("$%.2f (%s)%s\n",
					val1,
					dc1.getDcAmtType(),
					dc1.getDcDesc()));
				amtCharge += dc1.getOdcAmt();
			}
		}
		
		taDt.append(ResUtil.padStr(40, '=')+"\n");
		
		// sub total
		taDt.append(String.format("Sub Total: $%.2f\n", tmp_sub_total));
		
		// tax
		taDt.append(String.format("Tax: $%.2f\n", tmp_tax));
		
		if (amtDisc > 0) {
			taDt.append(String.format("Discount: $%.2f\n", amtDisc));
		}
		
		if (amtCharge > 0) {
			taDt.append(String.format("Charge: $%.2f\n", amtCharge));
		}
		
		// delivery
		if (ToGoType.Delivery == ord1.getOrdToGoType()) {
			taDt.append(String.format("Delivery: $%.2f\n", ord1.getDeliFee()));
		}
		
		// total
		double netTotal = (tmp_sub_total+tmp_tax+ord1.getDeliFee()) - amtDisc;
		taDt.append(String.format("Total: $%.2f\n", netTotal));
		
		taDt.append("\n");

		return taDt.toString();
	}
	
	public static File genSaleReportB(RptData2 rptDt, String rptTitle, boolean withDetail) throws Exception {
		TxPrinter prt1 = TxPrinter.getPrinterByIp(ResCfg.getTrmPrnIP());
		File rptFile = new File(String.format("tmp/%d_srpt_%s.txt", 
			prt1.getPrnId(),
			ResUtil.dtoc(new java.util.Date(), "yyMMdd_HHmmss")));
		try (
			PrintWriter pw1 = new PrintWriter(rptFile);
		) {
			// start report
			// **********************************************

			double totSales = 0;
			
			pw1.println();
			pw1.println(String.format(
				"%s%n%s - %s"
				,rptTitle
				,rptDt.getFrmDtStr()
				,rptDt.getToDtStr())
			);

			pw1.println(String.format("Print Date-Time: %s", 
				ResUtil.dtoc(new java.util.Date(), "MM/dd/YYYY HH:mm")));
			pw1.println();
			
			pw1.println("[A] SALES");
			if (rptDt.hasSales()) {
				java.util.HashMap<PayBy, java.util.HashMap<RptSaleType, RptFld>> 
					dtSales = rptDt.getDataSales();
				
				pw1.println("Type                       Amount($)");
				pw1.println("------------------------------------");
				for (int x=0; x < PayBy.size; x++) {
					PayBy pby1 = PayBy.values()[x];
					if (!dtSales.containsKey(pby1)) {
						continue;
					}
					pw1.printf("%s%n", pby1.toString());
					
					java.util.HashMap<RptSaleType, RptFld> sdt1 = dtSales.get(pby1);
					double sumAmt = 0;
					for (int y=0; y < RptSaleType.size; y++) {
						RptSaleType rst1 = RptSaleType.values()[y];
						if (!sdt1.containsKey(rst1)) {
							continue;
						}
						RptFld fld1 = sdt1.get(rst1);
						pw1.printf("  %s     %8.2f%n",
							ResUtil.padl(rst1.toString(), 20),
							fld1.amount);
						sumAmt += fld1.amount;
					}
					
					RptFld deli1 = rptDt.getDeliFee(pby1);
					if (deli1.count > 0) {
						pw1.printf("  %s     %8.2f%n",
							ResUtil.padl("Delivery Fee", 20),
							deli1.amount);
					}
					
					pw1.printf("  %s     %8.2f%n",
						ResUtil.padr("Total", 20),
						(sumAmt - deli1.amount));
					
					totSales += (sumAmt - deli1.amount);
				}
				pw1.println("------------------------------------");
				pw1.printf("  %s     %8.2f%n",
						ResUtil.padr("Total Sales", 20),
						totSales);
			} else {
				pw1.println("------------------------------------");
				pw1.println("-No Data-");
			}
			pw1.println("------------------------------------");
			
			double sumCash = rptDt.getSumCash();
			double sumDeli = rptDt.getSumDeliFee();
			double sumServ = rptDt.getSumServiceCharge();
			double sumTips = rptDt.getSumTips();
			
			pw1.println();
			pw1.println("[B] CASH ON HAND");
			pw1.println("------------------------------------");
			pw1.println(String.format("Cash Collected             %8.2f", sumCash));
			pw1.println(String.format("- Delivery Charge          %8.2f", sumDeli));
			pw1.println(String.format("- Service Charge           %8.2f", sumServ));
			pw1.println(String.format("- Credit Tips              %8.2f", sumTips));
			pw1.println("------------------------------------");
			pw1.println(String.format("Net Cash Collected         %8.2f", sumCash - (sumDeli+sumServ+sumTips)));
			pw1.println("------------------------------------");
			
			int ordCnt = rptDt.getOrdCount();
			double ordAmtBf = rptDt.getOrdSumAmtBf();
			double ordAmtDisc = rptDt.getOrdSumAmtDisc();
			double ordAmtCharge = rptDt.getOrdSumAmtCharge();
			double ordAmtTax = rptDt.getOrdSumAmtTax();
			
			pw1.println();
			pw1.println("[C] ORDERS SUMMARY");
			pw1.println("------------------------------------");
			pw1.printf(" # Orders                      %4d%n", ordCnt);
			pw1.printf(" Sub Total                $%8.2f%n", ordAmtBf);
			pw1.printf(" - Discounts              $%8.2f%n", ordAmtDisc);
			pw1.printf(" + Service Charge         $%8.2f%n", sumServ);
			pw1.printf(" + Other Charges          $%8.2f%n", (ordAmtCharge - sumServ));
			pw1.printf(" + Tax                    $%8.2f%n", ordAmtTax);
			pw1.printf(" = Sales Total            $%8.2f%n", ((ordAmtBf-ordAmtDisc) + ordAmtTax + ordAmtCharge));
			pw1.printf(" + Deli Charge Total      $%8.2f%n", sumDeli);
			pw1.println("------------------------------------");
			pw1.printf(" = Gross Sales Total      $%8.2f%n", ((ordAmtBf-ordAmtDisc) + ordAmtTax + ordAmtCharge + sumDeli));
			pw1.println("------------------------------------");
			
			pw1.println();
			pw1.println("[D] ORDER TYPES / PAYMENTS");
			if (rptDt.hasOrderTypesPayments()) {
				
				java.util.HashMap<RptSaleType, java.util.HashMap<PayBy, RptFld>> 
					dtSP = rptDt.getOrderTypesPayments();
				int grnCnt = 0;
				double grnAmt = 0;
				
				pw1.println("------------------------------------");
				
				for (int x=0; x < RptSaleType.size; x++) {
					
					RptSaleType rst1 = RptSaleType.values()[x];
					if (!dtSP.containsKey(rst1)) {
						continue;
					}
					
					pw1.printf("%s%n", rst1.toString());
					
					java.util.HashMap<PayBy, RptFld> dtsp1 = dtSP.get(rst1);
					int totCnt = 0;
					double totAmt = 0;
					
					for (int y=0; y < PayBy.size; y++) {
						PayBy pby1 = PayBy.values()[y];
						if (!dtsp1.containsKey(pby1)) {
							continue;
						}
						RptFld fld1 = dtsp1.get(pby1);
						pw1.printf("  %s  %4d       %8.2f%n", 
							ResUtil.padl(pby1.toString(), 12),
							fld1.count,
							fld1.amount);
						
						totCnt += fld1.count;
						totAmt += fld1.amount;
						
						grnCnt += fld1.count;
						grnAmt += fld1.amount;
					}
					pw1.printf("  %s  %4d       %8.2f%n", 
						ResUtil.padr("Total", 12),
						totCnt,
						totAmt);
				}
				pw1.println("------------------------------------");
				pw1.printf("  %s  %4d       %8.2f%n", 
					ResUtil.padr("Total", 12),
					grnCnt,
					grnAmt);
			} else {
				pw1.println("------------------------------------");
				pw1.println("-No Data-");
			}
			pw1.println("------------------------------------");
			pw1.println();
			
			pw1.println("[E] DISCOUNTS");
			pw1.println("------------------------------------");
			if (rptDt.hasDiscounts()) {
				TbOrder[] orders = rptDt.getOrders();
				for (TbOrder ord1 : orders) {
					if (!ord1.hasDiscount()) {
						continue;
					}
					double totAmt = 0;
					for (TbOrderDC odc1 : ord1.getDiscounts()) {
						pw1.printf(" %s $ %8.2f%n"
							,ResUtil.padl(String.format("#%d %s", 
								ord1.getOrdNo(), 
								odc1.dispStr()), 23)
							,odc1.getOdcAmt());
						totAmt += odc1.getOdcAmt();
					}
					pw1.printf(" %s $ %8.2f%n"
						,ResUtil.padr("Total", 23)
						,totAmt);
				}
			} else {
				pw1.println("-No Discount-");
			}
			pw1.println("------------------------------------");
			pw1.println();
			
			pw1.println("[F] CHARGES");
			pw1.println("------------------------------------");
			if (rptDt.hasCharges()) {
				TbOrder[] orders = rptDt.getOrders();
				for (TbOrder ord1 : orders) {
					if (!ord1.hasCharge()) {
						continue;
					}
					double totAmt = 0;
					for (TbOrderDC odc1 : ord1.getCharges()) {
						pw1.printf(" %s $ %8.2f%n"
							,ResUtil.padl(String.format("#%d %s", 
								ord1.getOrdNo(), 
								odc1.dispStr()), 23)
							,odc1.getOdcAmt());
						totAmt += odc1.getOdcAmt();
					}
					pw1.printf(" %s $ %8.2f%n"
						,ResUtil.padr("Total", 23)
						,totAmt);
				}
			} else {
				pw1.println("-No Charge-");
			}
			pw1.println("------------------------------------");
			pw1.println();

			pw1.println("[G] CANCELLED ITEMS");
			if (rptDt.hasCanceledItems()) {
				pw1.println("------------------------------------");
				TbOrderItem[] delOdis = rptDt.getCanceledItems();
				for (TbOrderItem odi1 : delOdis) {
					pw1.printf("%s  Qty=%d  $%8.2f%n  [%s]=%s%n",
						ResUtil.dtoc(odi1.getOdiDt(), "M/d/yy HH:mm"),
						odi1.getOdiQty(),
						odi1.getOdiAmtNet(),
						odi1.getOdiRemark(),
						odi1.getItmNamePos() 
						);
				}
			} else {
				pw1.println("------------------------------------");
				pw1.println("-No Canceled Item-");
			}
			pw1.println("------------------------------------");
			pw1.println();
			
			if (withDetail) {
				if (rptDt.hasOrders()) {
					_printTickets(rptDt, pw1);
				} else {
					pw1.println("-No Orders-");
				}
			}
			
			// **********************************************
			// end report
			
			pw1.println(pESC + "|3lF");
		}
		return rptFile;
	}
	
	private static void _printTickets(RptData2 rptDt, PrintWriter pw1) {
		if (!rptDt.hasOrders()) {
			return;
		}
		TbOrder[] orders = rptDt.getOrders();
		pw1.println("[H] TICKETS");
		pw1.println("------------------------------------");
		pw1.printf("Total Ticktes = [%d]%n", orders.length);
		pw1.println("------------------------------------");
		for (int x=0; x < RptSaleType.size; x++) {
			RptSaleType rst1 = RptSaleType.values()[x];
			String str1 = rst1.toString();
			for (TbOrder ord1 : orders) {
				RptSaleType rst2 = RptSaleType.getRptSaleType(
					ord1.getOrdType(), ord1.getOrdToGoType());
				if (rst1 != rst2) {
					continue;
				}
				if (!str1.isEmpty()) {
					pw1.printf("*** [%s] ***%n", str1);
					str1 = "";
				}
				pw1.printf("#%d [%s] AMT$=%.2f"
					,ord1.getOrdNo()
					,ResUtil.dtoc(ord1.getOrdDt(), "M/d/yy HH:mm")
					,ord1.getOrdAmtNet());
				if (ord1.getDeliFee() > 0) {
					pw1.printf(" DELI$=%.2f", ord1.getDeliFee());
				}
				if (ord1.getOrdAmtDisc() > 0) {
					pw1.printf(" DSC$=%.2f", ord1.getOrdAmtDisc());
				}
				if (ord1.getOrdAmtCharge() > 0) {
					pw1.printf(" CHRG$=%.2f", ord1.getOrdAmtCharge());
				}
				pw1.println();
				if (null == ord1.getPayments() || ord1.getPayments().size() <= 0) {
					pw1.println("  -No Payment-");
				} else {
					for (TbPayment pm1 : ord1.getPayments()) {
						double payAmt = pm1.getPmAmt();
						if (PayBy.Cash == pm1.getPmPayBy()) {
							payAmt -= ord1.getOrdPmChange();
						}
						pw1.printf("  %s %s $=%.2f"
							,pm1.getPmPayBy().abrv()
							,ResUtil.dtoc(pm1.getPmDt(), "HH:mm")
							,payAmt);
						if (PayBy.CreditCard == pm1.getPmPayBy()) {
							pw1.printf(" T=%.2f xx%s", 
								pm1.getPmTip(),
								pm1.getCCLast4Digits());
						}
						pw1.println();
					}
				}
				pw1.println();
			}
		}
		pw1.println("------------------------------------");
		pw1.println();
	}
	
	
}
