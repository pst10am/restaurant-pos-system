package print;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Calendar;

import model.TbClock;
import model.TbOrder;
import model.TbOrderDC;
import model.TbOrderItem;
import model.TbPayment;
import model.TxPrinter;
import refx.AmountType;
import refx.ClockType;
import refx.OrderItemType;
import refx.OrderType;
import refx.PayBy;
import refx.PaySrc;
import refx.SlipType;
import refx.ToGoType;
import resrc.ResCfg;
import resrc.ResUtil;
import static print.PrnConst.*;

class SlipGenDefault implements SlipGen {

	@Override
	public File[] genKitchenSlip(TbOrder ord1, boolean allFlg, boolean runnerOnly) throws Exception {
		java.util.HashMap<Integer, PrtInfo> prifs = new java.util.HashMap<>();
		//
		int _setNo = 1;
		for (int i=0; i < ord1.getItemCount(); i++) {
			TbOrderItem odi1 = ord1.getItems().get(i);
			if (OrderItemType.Line == odi1.getOdiType()) {
				_setNo += 1;
			}
			odi1.setSetNo(_setNo);
			//
			if (odi1.isPrinted() && !allFlg) continue;
			if (odi1.isHold()) continue;
			//
			TxPrinter[] prts = TxPrinter.findPrinters(odi1);
			if (null == prts) {
				continue;
			}
			for (TxPrinter prt1 : prts) {
				if (runnerOnly) {
					if (!prt1.isRunner()) {
						continue;
					}
				}
				if (prt1.isRunner()) {
					if (!ResCfg.printRunner()) {
						continue;
					}
					if (OrderType.DineIn != ord1.getOrdType()) {
						continue;
					}
				}
				if (!prifs.containsKey(prt1.getPrnId())) {
					prifs.put(prt1.getPrnId(), PrtInfo.newInstance(prt1));
				}
				PrtInfo prif1 = prifs.get(prt1.getPrnId());
				prif1.addItem(odi1);
			}
		}
		//
		File[] pfiles = new File[prifs.size()];
		int px = 0;
		java.util.Iterator<Integer> iter1 = prifs.keySet().iterator();
		while (iter1.hasNext()) {
			int prnId = iter1.next();
			
			PrtInfo prif1 = prifs.get(prnId);
			
			File pfile = new File(prif1.getFileName(ord1.getOrdId()));
			
			_printKitchenSlipToFile(ord1, prif1, pfile);
			
			pfiles[px] = pfile;
			px += 1;
		}
		return pfiles;
	}
	
	private void _printKitchenSlipToFile(TbOrder ord1, PrtInfo k1, File pfile) throws Exception {
		try (
			PrintWriter pw1 = new PrintWriter(pfile)
		) {
			pw1.println(pESC + "|5lF");
			pw1.println(pF4+pBOLD+ String.format("** %s **", k1.getPrnName()));

			// Order Type/No
			String _typeStr = OrderType.DineIn.toString();
			if (OrderType.ToGo == ord1.getOrdType()) {
				_typeStr = ord1.getOrdToGoType().toString();
			}

			// ToGo -> Reverse
			if (ord1.isToGo()) {
				pw1.println(pF4+pREVERSE+ ResUtil.padl("*"+_typeStr+"*", 24, ' '));
			} else {
				pw1.println(pF1+ ResUtil.padStr(48, ':'));
				pw1.println(pF4+ ResUtil.padl("*"+_typeStr+"*", 24, ' '));
				pw1.println(pF1+ ResUtil.padStr(48, ':'));
			}
			
			pw1.println(pF2+ String.format("Order: %d", ord1.getOrdNo()));
			
			// Customer/Table 
			if (OrderType.DineIn == ord1.getOrdType()) {
				pw1.println(pF2+ String.format("Table: %s", ord1.getTblName()));
			} else {
				pw1.println(pF2+ String.format("Name: %s", ord1.getCstName()));
			}
			
			int _setNo = 1;
			
			pw1.println(pF1+ ResUtil.padStr(48, '='));
			for (int i=0; i < k1.getSize(); i++) {
				if (i > 0) {
					pw1.println(pF1+ ResUtil.padStr(48, '-'));
				}
				TbOrderItem odi1 = k1.getItemAt(i);
				
				if (_setNo != odi1.getSetNo()) {
					_setNo = odi1.getSetNo();
					pw1.println(pF2+pCENTER+ String.format("####### %d #######", _setNo));
				}
				
				if (odi1.getOdiQty() > 1) {
					pw1.println(pF2+ String.format("[%d] %s", odi1.getOdiQty(), odi1.getItmNamePos()));
				} else {
					pw1.println(pF2+ odi1.getItmNamePos());
				}
				if (!odi1.getOpiStr().isEmpty()) {
					String[] opis = odi1.getOpiStr().split(",");
					for (String opi1 : opis) {
						pw1.println(pF2+ String.format(" %s", opi1));
					}
				}
				if (!odi1.getOdiNote().trim().isEmpty()) {
					pw1.println(pF2+ odi1.getOdiNote().trim());
				}
			}

			pw1.println(pF1+ String.format("%s", ResUtil.padStr(48, '=')));
			pw1.println(pF3+pRIGHT+ String.format("%s", ResUtil.dtoc(ord1.getOrdDt(), "MM/dd/yyyy hh:mm:ss aaa")));
			pw1.println(pESC + "|3lF");
			
			if (k1.hasBuzzer()) {
				pw1.println(pBUZZER);
			}
		}
	}

	@Override
	public File genSlip(TbOrder ord1, SlipType slpType) throws Exception {
		
		String slpStr = "cash";
		TxPrinter prt1 = TxPrinter.getPrinterByIp(ResCfg.getTrmPrnIP());
		if (SlipType.Packing == slpType) {
			slpStr = "pack";
			prt1 = TxPrinter.getPrinterByIp(ResCfg.getPackingPrnIp());
		}
		
		File pfile = new File(String.format("tmp/%d_%d_%s.tmp", 
			prt1.getPrnId(), ord1.getOrdId(), slpStr));
		
		try (
			PrintWriter pw1 = new PrintWriter(pfile);
		) {
			pw1.println(pESC + "|2lF");
			
			pw1.println("[LOGO]");
			
			for (int i=1; i <= ResCfg.getTotalShopLine(); i++) {
				if (ResCfg.getShopLine(i).trim().isEmpty()) {
					continue;
				}
				pw1.println(pF1+pBOLD+pCENTER+ ResCfg.getShopLine(i));
			}
			
			pw1.println();

			String _typeStr = OrderType.DineIn.toString();
			if (OrderType.ToGo == ord1.getOrdType()) {
				_typeStr = ord1.getOrdToGoType().toString();
			}
			// pw1.println(pF4+pREVERSE+ ResUtil.padl("*"+_typeStr+"*", 24, ' '));
			pw1.println(pF1+ ResUtil.padStr(48, '#'));
			pw1.println(pF4+ ResUtil.padl("*"+_typeStr+"*", 24, ' '));
			pw1.println(pF1+ ResUtil.padStr(48, '#'));
			
			if (ord1.isPaid()) {
				pw1.println(pF4+pREVERSE+ "[$PAID$]");
			} else {
				pw1.println(pF4+pBOLD+ ">CHECK<");
			}
			if (SlipType.Packing == slpType) {
				pw1.println(pF4+pBOLD+ ">PACKING LIST<");
			}
			
			pw1.println(pF2+ String.format("Order: %d", ord1.getOrdNo()));
			
			// Customer/Table 
			if (OrderType.DineIn == ord1.getOrdType()) {
				pw1.println(pF2+ String.format("Table: %s", ord1.getTblName()));
			} else {
				pw1.println(pF2+ String.format("Name: %s", ord1.getCstName()));
				pw1.println(pF2+ String.format("Phone: %s", ResUtil.formatPhone(ord1.getCstPhone())));
			}
			
			// Delivery
			if ((OrderType.ToGo == ord1.getOrdType()) && 
				(ToGoType.Delivery == ord1.getOrdToGoType())) {
				pw1.println(pF1+ ResUtil.padStr(48, '-'));
				pw1.println(pF2+pBOLD+ "Address:");
				pw1.println(pF2+pBOLD+ String.format(" %s", ord1.getDeliAddr1()));
				if (ord1.getDeliAddr2().trim().length() > 0) {
					pw1.println(pF2+pBOLD+ String.format(" %s", ord1.getDeliAddr2()));
				}
				if (ord1.getDeliUnitNo().trim().length() > 0) {
					pw1.println(pF2+pBOLD+ String.format(" APT. %s", ord1.getDeliUnitNo()));
				}
				pw1.println(pF2+pBOLD+ String.format(" %s, %s", ord1.getDeliCity(), ord1.getDeliState()));
				pw1.println(pF2+ String.format(" (%.2f miles.)", ord1.getDeliDist()));
			}
			
			if (!ord1.getCstNote().trim().isEmpty()) {
				pw1.println(pF1+ ResUtil.padStr(48, '-'));
				pw1.println(pF2+pBOLD+ "Note:");
				pw1.println(pF2+pBOLD+ ResUtil.newLine2Space(ord1.getCstNote()));
			}
			
			pw1.println(pF1+ ResUtil.padStr(48, '-'));
			pw1.println(pF1+ String.format("Order Time: %s", ResUtil.dtoc(ord1.getOrdDt(), "MM/dd/yyyy hh:mm:ss aaa")));

			if (OrderType.ToGo == ord1.getOrdType()) {
				// promise date-time.
				/*
				pw1.println(pF4+pBOLD+ "Promise Time:");
				java.util.Date prmDt = null;
				if (ToGoType.Delivery == ord1.getOrdToGoType()) {
					// delivery: 1 hour.
					prmDt = ResUtil.dtadd(ord1.getOrdDt(), 1, Calendar.HOUR_OF_DAY);
				} else {
					// waiting, pickup: 20 minutes.
					prmDt = ResUtil.dtadd(ord1.getOrdDt(), 20, Calendar.MINUTE);
				}
				pw1.println(pF2+pBOLD+ ResUtil.dtoc(prmDt, "MM/dd/yyyy hh:mm:ss aaa"));
				*/
			}
			
			pw1.println(pF1+ ResUtil.padStr(48, '='));
			
			double tmp_sub_total = 0;
			double tmp_tax = 0;
			
			for (int i=0; i < ord1.getItemCount(); i++) {
				if (i > 0) {
					pw1.println(pF1+ ResUtil.padStr(48, '-'));
				}
				TbOrderItem odi1 = ord1.getItems().get(i);
				
				String tmpQty = String.format("[%d]", odi1.getOdiQty());
				String tmpTitle = odi1.getItmNamePos(); 
				String tmpPrice = ResUtil.padr(String.format("$%.2f", odi1.getOdiAmtBf()), 8);
				StringBuilder bld1 = new StringBuilder();
				for (int k=0; k < tmpTitle.length(); k++) {
					if (bld1.length() >= 11) {
						String bld1s = bld1.toString().trim();
						int blnk1 = bld1s.lastIndexOf(" "); 
						if (blnk1 > -1) {
							bld1s = bld1s.substring(0, blnk1);
							bld1.delete(0, blnk1+1);
						} else {
							bld1.delete(0, bld1.length());
						}
						pw1.println(pF2+ String.format("%s %s %s", tmpQty, ResUtil.padl(bld1s.trim(), 11) , tmpPrice));
						tmpQty = tmpQty.replaceAll(".", " ");
						tmpPrice = tmpPrice.replaceAll(".", " ");
					}
					bld1.append(tmpTitle.charAt(k));
				}
				if (bld1.length() > 0) {
					pw1.println(pF2+ String.format("%s %s %s", tmpQty, ResUtil.padl(bld1.toString().trim(), 11), tmpPrice));
				}
				
				if (!odi1.getOpiStr().isEmpty()) {
					pw1.println(pF3+ " "+odi1.getOpiStr());
				}
				if (!odi1.getOdiNote().trim().isEmpty()) {
					pw1.println(pF3+ " "+odi1.getOdiNote());
				}
				//
				tmp_sub_total += odi1.getOdiAmtBf();
				tmp_tax += odi1.getOdiAmtTax();
			}
			
			double amtDisc = 0;
			if (ord1.getDiscounts().size() > 0) {
				pw1.println(pF1+ ResUtil.padStr(48, '-'));
				pw1.println(pF1+pBOLD+ "Discount:");
				for (TbOrderDC dc1 : ord1.getDiscounts()) {
					double val1 = 0;
					if (AmountType.Percent == dc1.getDcAmtType()) {
						val1 = (tmp_sub_total+tmp_tax) * ((dc1.getDcAmt())/100);
					} else if (AmountType.Fixed == dc1.getDcAmtType()) {
						val1 = dc1.getDcAmt();
					}
					if (val1 == 0) {
						pw1.println(pF1+ String.format("%s",
							dc1.getDcDesc()));
					} else {
						pw1.println(pF1+ String.format("%s [$%.2f]",
							dc1.getDcDesc(), val1));
					}
					amtDisc += dc1.getOdcAmt();
				}
			}
			
			double amtCharge = 0;
			if (ord1.getCharges().size() > 0) {
				pw1.println(pF1+ ResUtil.padStr(48, '-'));
				pw1.println(pF1+pBOLD+ "Charge:");
				for (TbOrderDC dc1 : ord1.getCharges()) {
					double val1 = 0;
					if (AmountType.Percent == dc1.getDcAmtType()) {
						val1 = (tmp_sub_total+tmp_tax) * ((dc1.getDcAmt())/100);
					} else if (AmountType.Fixed == dc1.getDcAmtType()) {
						val1 = dc1.getDcAmt();
					}
					if (val1 == 0) {
						pw1.println(pF1+ String.format("%s",
							dc1.getDcDesc()));
					} else {
						pw1.println(pF1+ String.format("%s [$%.2f]",
							dc1.getDcDesc(), val1));
					}
					amtCharge += dc1.getOdcAmt();
				}
			}

			pw1.println(pF1+ ResUtil.padStr(48, '='));
			pw1.println(pF3+pRIGHT+ ResUtil.dtoc(ord1.getOrdDt(), "MM/dd/yyyy hh:mm:ss aaa"));
			pw1.println();
			
			String tmpStr = "";
			
			// sub total
			tmpStr = String.format("Sub Total: $%.2f", tmp_sub_total);
			pw1.println(pF2+pBOLD+ ResUtil.padr(tmpStr, 24, ' '));
			
			// tax
			tmpStr = String.format("Tax: $%.2f", tmp_tax);
			pw1.println(pF2+pBOLD+ ResUtil.padr(tmpStr, 24, ' '));
			
			// discount
			if (amtDisc > 0) {
				tmpStr = String.format("Discount: $%.2f", amtDisc);
				pw1.println(pF2+pBOLD+ ResUtil.padr(tmpStr, 24, ' '));
			}
			
			// charge
			if (amtCharge > 0) {
				tmpStr = String.format("Charge: $%.2f", amtCharge);
				pw1.println(pF2+pBOLD+ ResUtil.padr(tmpStr, 24, ' '));
			}
			
			// delivery
			if (ToGoType.Delivery == ord1.getOrdToGoType()) {
				tmpStr = String.format("Delivery: $%.2f", ord1.getDeliFee());
				pw1.println(pF2+pBOLD+ ResUtil.padr(tmpStr, 24, ' '));
			}
			
			// total
			tmpStr = String.format("Total: $%.2f", 
				(tmp_sub_total+tmp_tax+ord1.getDeliFee()+amtCharge) - amtDisc);
			pw1.println(pF2+pBOLD+ ResUtil.padr(tmpStr, 24, ' '));
			
			pw1.println();
			pw1.println(pF3+pBOLD+pCENTER+ "Thank You!");
			
			pw1.println(pESC + "|3lF");
		}
		
		return pfile;
	}

	@Override
	public File genCreditCardSign(TbPayment pm1) throws Exception {
		if (PayBy.CreditCard != pm1.getPmPayBy()) return null;

		TxPrinter prt1 = TxPrinter.getPrinterByIp(ResCfg.getTrmPrnIP());
		File pfile = new File(String.format("tmp/%d_%d_ccsign.tmp", 
			prt1.getPrnId(), pm1.getPmId()));
		try (
			PrintWriter pw1 = new PrintWriter(pfile);
		) {
			pw1.println(pESC + "|3lF");
			for (int k=0; k < 2; k++) {
				for (int i=1; i <= ResCfg.getTotalShopLine(); i++) {
					if (ResCfg.getShopLine(i).trim().isEmpty()) {
						continue;
					}
					pw1.println(pF1+pBOLD+pCENTER+ ResCfg.getShopLine(i));
				}
				pw1.println();
				if (PaySrc.Order == pm1.getRefSrc()) {
					pw1.println(pBOLD+pF3+ String.format("Order No. %d", pm1.getOrdNo()));
				} else {
					pw1.println(pBOLD+pF3+ String.format("Gift No. %d", pm1.getRefId()));
				}
				pw1.println(pBOLD+ "Date-Time: "+ ResUtil.dtoc(pm1.getPmDt(), "MM/dd/YYYY HH:mm:ss"));
				pw1.println( "================================================");
				pw1.println( pm1.getCCAcctName() +" ..."+ pm1.getCCLast4Digits());
				pw1.println( ResUtil.padl("Amount",38,' ')+String.format("%10.2f", pm1.getPmAmt()));
				if (pm1.getPmTip() > 0) {
					pw1.println( ResUtil.padl("Tip",38,' ')+String.format("%10.2f", pm1.getPmTip()));
					pw1.println( ResUtil.padl("Total",38,' ')+String.format("%10.2f", pm1.getPmAmt() + pm1.getPmTip()));
				} else {
					pw1.println();
					pw1.println( "Tip                            _________________");
					pw1.println();
					pw1.println( "Total                          _________________");
				}
				pw1.println();
				pw1.println( "Signature  _____________________________________");
				pw1.println( "================================================");
				
				// customer copy guide tip
				if (1==k) { 
					double tip15 = pm1.getPmAmt() * 0.15;
					double tip18 = pm1.getPmAmt() * 0.18;
					double tip20 = pm1.getPmAmt() * 0.20;
					pw1.println(String.format("15%%=$%.2f, 18%%=$%.2f, 20%%=$%.2f", tip15, tip18, tip20));
					pw1.println("Thank You");
				}
				
				pw1.println( (0==k ? "Restaurant Copy" : "Customer Copy"));
				pw1.println(pESC + "|3lF");
				if (k==0) {
					pw1.println(pESC + "|100fP");
				}
				pw1.println(pESC + "|2lF");				
			}
		}

		return pfile;
	}

	@Override
	public File genClockSlip(TbClock clk1) throws Exception {
		TxPrinter prt1 = TxPrinter.getPrinterByIp(ResCfg.getTrmPrnIP());
		
		File pfile = new File(String.format("tmp/%d_%d_clock.tmp", 
			prt1.getPrnId(), clk1.getClkId()));
		
		try (
			PrintWriter prn1 = new PrintWriter(pfile);
		) {
			prn1.println();
			prn1.println(String.format("Hi, [%s]", clk1.getUsrName()));
			prn1.println("--------------------------------");
			if (ClockType.In == clk1.getClkType()) {
				prn1.println(String.format(" > Clock-In [%s]", ResUtil.dtoc(clk1.getClkDt(), "MM/dd/yyyy HH:mm:ss")));
				prn1.println("--------------------------------");
				prn1.println("Have a Nice Day!");
			} else {
				//prn1.println(String.format("   Clock-In [%s]", SysUtl.dtoc(clk1.getClkDt(), "MM/dd/yyyy HH:mm:ss")));
				prn1.println(String.format(" > Clock-Out [%s]", ResUtil.dtoc(clk1.getClkDt(), "MM/dd/yyyy HH:mm:ss")));
				/*
				long diff = clk1.clkDt.getTime() - clk1.lastClkIn.clkDt.getTime();
				long dMin = diff / (60 * 1000) % 60;
				long dHrs = diff / (60 * 60 * 1000) % 24;
				prn1.println(String.format(" = Hrs:Mins [%d:%d]", dHrs, dMin));
				*/
				prn1.println("----------------------------------");
				prn1.println("Have a good one!");
			}
			prn1.println(pESC + "|3lF");
			//prn1.println(pESC + "|100fP");
		}
		
		return pfile;
	}
}
