package print;

import java.util.Calendar;

import model.TbOrder;
import model.TbOrderDC;
import model.TbOrderItem;
import model.TbPayment;
import refx.AmountType;
import refx.OrderType;
import refx.ToGoType;
import resrc.ResUtil;

public class PrnModel {
	private PrnModel() {}
	
	// TbOrder : pretty print
	
	public static String printTbOrder(TbOrder ord1) {
		StringBuilder sbld1 = new StringBuilder();

		String _typeStr = OrderType.DineIn.toString();
		if (OrderType.ToGo == ord1.getOrdType()) {
			_typeStr = ord1.getOrdToGoType().toString();
		}
		sbld1.append(String.format("*%s*\n", _typeStr));
		sbld1.append(ResUtil.padStr(38, '#'));
		sbld1.append("\n");
		if (ord1.isPaid()) {
			sbld1.append("[$PAID$]\n\n");
		} else {
			sbld1.append(">CHECK<\n\n");
		}
		
		sbld1.append(String.format("Order# %d\n\n", ord1.getOrdNo()));
		
		// Customer/Table 
		if (OrderType.DineIn == ord1.getOrdType()) {
			sbld1.append(String.format("Table: %s\n", ord1.getTblName()));
		} else {
			sbld1.append(String.format("Name: %s\n", ord1.getCstName()));
			sbld1.append(String.format("Phone: %s\n", ResUtil.formatPhone(ord1.getCstPhone())));
		}
		
		// Delivery
		if ((OrderType.ToGo == ord1.getOrdType()) && 
			(ToGoType.Delivery == ord1.getOrdToGoType())) {
			sbld1.append(ResUtil.padStr(38, '-'));
			sbld1.append("\n");
			sbld1.append("Address:\n");
			sbld1.append(String.format(" %s", ord1.getDeliAddr1()));
			if (ord1.getDeliAddr2().trim().length() > 0) {
				sbld1.append(String.format(" %s", ord1.getDeliAddr2()));
			}
			if (ord1.getDeliUnitNo().trim().length() > 0) {
				sbld1.append(String.format(" APT. %s", ord1.getDeliUnitNo()));
			}
			sbld1.append(String.format(" %s, %s", ord1.getDeliCity(), ord1.getDeliState()));
			sbld1.append(String.format(" (%.2f miles.)", ord1.getDeliDist()));
			sbld1.append("\n");
		}
		
		if (!ord1.getCstNote().trim().isEmpty()) {
			sbld1.append(ResUtil.padStr(38, '-'));
			sbld1.append("\n");
			sbld1.append("Note:\n");
			sbld1.append(ResUtil.newLine2Space(ord1.getCstNote()));
			sbld1.append("\n");
		}
		
		sbld1.append(ResUtil.padStr(38, '-'));
		sbld1.append("\n");
		sbld1.append(String.format("Order Time: %s\n", ResUtil.dtoc(ord1.getOrdDt(), "M/d/yy hh:mm a")));

		if (OrderType.ToGo == ord1.getOrdType()) {
			// promise date-time.
			sbld1.append("Promise Time: ");
			java.util.Date prmDt = null;
			if (ToGoType.Delivery == ord1.getOrdToGoType()) {
				// delivery: 1 hour.
				prmDt = ResUtil.dtadd(ord1.getOrdDt(), 1, Calendar.HOUR_OF_DAY);
			} else {
				// waiting, pickup: 20 minutes.
				prmDt = ResUtil.dtadd(ord1.getOrdDt(), 20, Calendar.MINUTE);
			}
			sbld1.append(ResUtil.dtoc(prmDt, "M/d/yy hh:mm a"));
			sbld1.append("\n");
		}
		
		sbld1.append(ResUtil.padStr(38, '='));
		sbld1.append("\n");
		
		double tmp_sub_total = 0;
		double tmp_tax = 0;
		
		for (int i=0; i < ord1.getItemCount(); i++) {
			if (i > 0) {
				sbld1.append(ResUtil.padStr(38, '-'));
				sbld1.append("\n");
			}
			TbOrderItem odi1 = ord1.getItems().get(i);
			
			String tmpQty = String.format("[%d]", odi1.getOdiQty());
			String tmpTitle = odi1.getItmNamePos(); 
			String tmpPrice = ResUtil.padr(String.format("$%.2f", odi1.getOdiAmtBf()), 8);
			StringBuilder bld1 = new StringBuilder();
			for (int k=0; k < tmpTitle.length(); k++) {
				if (bld1.length() >= 20) {
					String bld1s = bld1.toString().trim();
					int blnk1 = bld1s.lastIndexOf(" "); 
					if (blnk1 > -1) {
						bld1s = bld1s.substring(0, blnk1);
						bld1.delete(0, blnk1+1);
					} else {
						bld1.delete(0, bld1.length());
					}
					sbld1.append(String.format("%s %s %s\n", tmpQty, ResUtil.padl(bld1s.trim(), 20) , tmpPrice));
					tmpQty = tmpQty.replaceAll(".", " ");
					tmpPrice = tmpPrice.replaceAll(".", " ");
				}
				bld1.append(tmpTitle.charAt(k));
			}
			if (bld1.length() > 0) {
				sbld1.append(String.format("%s %s %s\n", tmpQty, ResUtil.padl(bld1.toString().trim(), 20), tmpPrice));
			}
			
			if (!odi1.getOpiStr().isEmpty()) {
				sbld1.append(" "+odi1.getOpiStr()+"\n");
			}
			if (!odi1.getOdiNote().trim().isEmpty()) {
				sbld1.append(" "+odi1.getOdiNote()+"\n");
			}
			//
			tmp_sub_total += odi1.getOdiAmtBf();
			tmp_tax += odi1.getOdiAmtTax();
		}
		
		double amtDisc = 0;
		if (ord1.getDiscounts().size() > 0) {
			sbld1.append(ResUtil.padStr(38, '-'));
			sbld1.append("\n");
			sbld1.append("Discount:\n");
			for (TbOrderDC dc1 : ord1.getDiscounts()) {
				double val1 = 0;
				if (AmountType.Percent == dc1.getDcAmtType()) {
					val1 = (tmp_sub_total+tmp_tax) * ((dc1.getDcAmt())/100);
				} else if (AmountType.Fixed == dc1.getDcAmtType()) {
					val1 = dc1.getDcAmt();
				}
				if (val1 == 0) {
					sbld1.append(String.format("%s\n",
						dc1.getDcDesc()));
				} else {
					sbld1.append(String.format("%s [$%.2f]\n",
						dc1.getDcDesc(), val1));
				}
				amtDisc += dc1.getOdcAmt();
			}
		}
		
		double amtCharge = 0;
		if (ord1.getCharges().size() > 0) {
			sbld1.append(ResUtil.padStr(38, '-'));
			sbld1.append("\n");
			sbld1.append("Charge:\n");
			for (TbOrderDC dc1 : ord1.getCharges()) {
				double val1 = 0;
				if (AmountType.Percent == dc1.getDcAmtType()) {
					val1 = (tmp_sub_total+tmp_tax) * ((dc1.getDcAmt())/100);
				} else if (AmountType.Fixed == dc1.getDcAmtType()) {
					val1 = dc1.getDcAmt();
				}
				if (val1 == 0) {
					sbld1.append(String.format("%s\n",
						dc1.getDcDesc()));
				} else {
					sbld1.append(String.format("%s [$%.2f]\n",
						dc1.getDcDesc(), val1));
				}
				amtCharge += dc1.getOdcAmt();
			}
		}

		sbld1.append(ResUtil.padStr(38, '='));
		sbld1.append("\n");
		sbld1.append(ResUtil.dtoc(ord1.getOrdDt(), "M/d/yy hh:mm a"));
		sbld1.append("\n\n");
		
		String tmpStr = "";
		
		// sub total
		tmpStr = String.format("Sub Total:  %8.2f\n", tmp_sub_total);
		sbld1.append(ResUtil.padr(tmpStr, 34, ' '));
		
		// tax
		tmpStr = String.format("Tax:  %8.2f\n", tmp_tax);
		sbld1.append(ResUtil.padr(tmpStr, 34, ' '));
		
		// discount
		if (amtDisc > 0) {
			tmpStr = String.format("Discount:  %8.2f\n", amtDisc);
			sbld1.append(ResUtil.padr(tmpStr, 34, ' '));
		}
		
		// charge
		if (amtCharge > 0) {
			tmpStr = String.format("Charge:  %8.2f\n", amtCharge);
			sbld1.append(ResUtil.padr(tmpStr, 34, ' '));
		}
		
		// delivery
		if (ToGoType.Delivery == ord1.getOrdToGoType()) {
			tmpStr = String.format("Delivery:  %8.2f\n", ord1.getDeliFee());
			sbld1.append(ResUtil.padr(tmpStr, 34, ' '));
		}
		
		// total
		tmpStr = String.format("Total:  %8.2f\n", 
			(tmp_sub_total+tmp_tax+ord1.getDeliFee()+amtCharge) - amtDisc);
		sbld1.append(ResUtil.padr(tmpStr, 34, ' '));
		
		sbld1.append("\n");
		
		if (ord1.isPaid()) {
			sbld1.append("Payments:\n");
			if ((null != ord1.getPayments()) && (ord1.getPayments().size() > 0)) {
				for (TbPayment pm1 : ord1.getPayments()) {
					sbld1.append(String.format(
						" -%s [%s] = $%.2f\n"
						,ResUtil.dtoc(pm1.getPmDt(), "M/d/yy HH:mma")
						,pm1.getPmPayBy().abrv()
						,pm1.getPmAmt()));
				}
			} else {
				sbld1.append(" -! Payment not found !-\n");
			}
		}
		
		sbld1.append("\n");
		sbld1.append("Thank You!\n");
		sbld1.append("\n\n\n");
		
		return sbld1.toString();
	}

}
