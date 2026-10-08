package model;

import java.util.HashMap;

import refx.CCCapture;
import refx.PayBy;
import refx.RptSaleType;
import refx.ToGoType;
import resrc.ResUtil;

public class RptData2 {

	private java.util.Date sFrm, sTo;
	private TbOrder[] orders;
	private TbPayment[] pmnts;
	private TbOrderItem[] delItems;
	
	private java.util.HashMap<PayBy, java.util.HashMap<RptSaleType, RptFld>> dtSales;
	private java.util.HashMap<PayBy, RptFld> dtDeliFee;
	private java.util.HashMap<RptSaleType, java.util.HashMap<PayBy, RptFld>> dtSP;
	
	// **********************************************
	
	private RptData2() {}
	
	public static RptData2 newInstance() {
		return new RptData2();
	}
	
	// **********************************************
	
	public TbOrder[] getOrders() {
		return orders;
	}
	
	public TbPayment[] getPayments() {
		return pmnts;
	}
	
	public TbOrderItem[] getCanceledItems() {
		return delItems;
	}

	public String getFrmDtStr() {
		return ResUtil.dtoc(sFrm, "M/d/yy hh:mm a").toLowerCase();
	}

	public String getToDtStr() {
		return ResUtil.dtoc(sTo, "M/d/yy hh:mm a").toLowerCase();
	}
	
	// **********************************************
	// report fields
	// **********************************************
	
	public boolean hasSales() {
		return null != dtSales && dtSales.size() > 0;
	}

	public boolean hasUnCapturedTips() {
		if (null == pmnts) return false;
		for (TbPayment pmnt1 : pmnts) {
			if (PayBy.CreditCard != pmnt1.getPmPayBy()) continue;
			if (CCCapture.Success != pmnt1.getPmCapture()) {
				return true;
			}
		}
		return false;
	}

	public boolean hasCharges() {
		if (null == orders) return false;
		for (TbOrder ord1 : orders) {
			if (null != ord1.getCharges() && 
				ord1.getCharges().size() > 0) {
				return true;
			}
		}
		return false;
	}

	public boolean hasDiscounts() {
		if (null == orders) return false;
		for (TbOrder ord1 : orders) {
			if (null != ord1.getDiscounts() && 
				ord1.getDiscounts().size() > 0) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasCanceledItems() {
		return null != delItems && delItems.length > 0;
	}

	public HashMap<PayBy, HashMap<RptSaleType, RptFld>> getDataSales() {
		return dtSales;
	}
	
	public HashMap<RptSaleType, java.util.HashMap<PayBy, RptFld>> getOrderTypesPayments() {
		return dtSP;
	}
	
	public RptFld getDeliFee(PayBy pby1) {
		if (!dtDeliFee.containsKey(pby1)) {
			return new RptFld();
		}
		return dtDeliFee.get(pby1);
	}
	
	private double _getSumPayBy(PayBy pby1) {
		if (null == pmnts || null == orders) return 0;
		double val = 0;
		for (int x=0; x < pmnts.length; x++) {
			TbPayment pm1 = pmnts[x];
			if (pby1 == pm1.getPmPayBy()) {
				val += pm1.getPmAmt();
			}
		}
		if (PayBy.Cash == pby1) {
			double change = 0;
			for (int x=0; x < orders.length; x++) {
				TbOrder ord1 = orders[x];
				change += ord1.getOrdPmChange();
			}
			val = val - change;
		}
		return val;
	}
	
	public double getSumCash() {
		return _getSumPayBy(PayBy.Cash);
	}
	
	public double getSumCredit() {
		return _getSumPayBy(PayBy.CreditCard);
	}
	
	public double getSumCheck() {
		return _getSumPayBy(PayBy.Check);
	}
	
	public double getSumGift() {
		return _getSumPayBy(PayBy.GiftCard);
	}
	
	public double getSumOther() {
		return _getSumPayBy(PayBy.NA);
	}
	
	public double getSumDeliFee() {
		if (null == orders) return 0;
		double val = 0;
		for (int x=0; x < orders.length; x++) {
			TbOrder ord1 = orders[x];
			if (ToGoType.Delivery == ord1.getOrdToGoType()) {
				val += ord1.getDeliFee();
			}
		}
		return val;
	}
	
	public double getSumServiceCharge() {
		if (null == orders) return 0;
		double val = 0;
		for (int x=0; x < orders.length; x++) {
			TbOrder ord1 = orders[x];
			if (ord1.hasCharge()) {
				for (TbOrderDC odc1 : ord1.getCharges()) {
					if (odc1.isServiceCharge()) {
						val += odc1.getOdcAmt();
					}
				}
			}
		}
		return val;
	}
	
	public double getSumTips() {
		if (null == pmnts) return 0;
		double val = 0;
		for (int x=0; x < pmnts.length; x++) {
			TbPayment pm1 = pmnts[x];
			if (PayBy.CreditCard != pm1.getPmPayBy()) {
				continue;
			}
			val += pm1.getPmTip();
		}
		return val;
	}
	
	public int getOrdCount() {
		return null != orders ? orders.length : 0;
	}
	
	public double getOrdSumAmtBf() {
		if (null == orders) return 0;
		double val = 0;
		for (TbOrder ord1 : orders) {
			val += ord1.getOrdAmtBf();
		}
		return val;
	}
	
	public double getOrdSumAmtDisc() {
		if (null == orders) return 0;
		double val = 0;
		for (TbOrder ord1 : orders) {
			val += ord1.getOrdAmtDisc();
		}
		return val;
	}
	
	public double getOrdSumAmtCharge() {
		if (null == orders) return 0;
		double val = 0;
		for (TbOrder ord1 : orders) {
			val += ord1.getOrdAmtCharge();
		}
		return val;
	}
	
	public double getOrdSumAmtTax() {
		if (null == orders) return 0;
		double val = 0;
		for (TbOrder ord1 : orders) {
			val += ord1.getOrdAmtTax();
		}
		return val;
	}
	
	public boolean hasOrderTypesPayments() {
		return null != dtSP && dtSP.size() > 0;
	}
	
	// **********************************************
	// data preparation
	// **********************************************
	
	private void updateSales(TbOrder ord1) {
		
		if (null == ord1.getPayments() || ord1.getPayments().size() <= 0) {
			return;
		}
		
		RptSaleType saleType = RptSaleType.getRptSaleType(
			ord1.getOrdType(), ord1.getOrdToGoType());
		
		// sale info
		for (TbPayment pm1 : ord1.getPayments()) {
			
			PayBy pby1 = pm1.getPmPayBy();

			// [A]
			if (!dtSales.containsKey(pby1)) {
				dtSales.put(pby1, 
					new java.util.HashMap<RptSaleType, RptFld>());
			}
			java.util.HashMap<RptSaleType, RptFld> sdt1 = dtSales.get(pby1);
			if (!sdt1.containsKey(saleType)) {
				sdt1.put(saleType, new RptFld());
			}
			RptFld fld1 = sdt1.get(saleType);
			fld1.count += 1;
			fld1.amount += pm1.getPmAmt();
			if (PayBy.Cash == pm1.getPmPayBy()) {
				fld1.amount -= ord1.getOrdPmChange();
			}
			
			// [D]
			if (!dtSP.containsKey(saleType)) {
				dtSP.put(saleType, new java.util.HashMap<PayBy, RptFld>());
			}
			java.util.HashMap<PayBy, RptFld> sp1 = dtSP.get(saleType);
			if (!sp1.containsKey(pby1)) {
				sp1.put(pby1, new RptFld());
			}
			RptFld spfld1 = sp1.get(pby1);
			spfld1.count += 1;
			spfld1.amount += pm1.getPmAmt();
			if (PayBy.Cash == pm1.getPmPayBy()) {
				spfld1.amount -= ord1.getOrdPmChange();
			}
		}
		
		// delivery fee info
		if (RptSaleType.Delivery == saleType) {
			TbPayment pm1 = ord1.getPayments().get(0);
			if (!dtDeliFee.containsKey(pm1.getPmPayBy())) {
				dtDeliFee.put(pm1.getPmPayBy(), new RptFld());
			}
			RptFld fld1 = dtDeliFee.get(pm1.getPmPayBy());
			fld1.count += 1;
			fld1.amount += ord1.getDeliFee();
		}
	}

	/*
	private void updatePayment(TbPayment pm1) {
	}
	*/

	public void runData(java.util.Date _sFrm, java.util.Date _sTo) throws Exception {
		
		sFrm = _sFrm;
		sTo = _sTo;
		
		dtSales = new java.util.HashMap<>();
		dtDeliFee = new java.util.HashMap<>();
		dtSP = new java.util.HashMap<>();
		
		orders = TbOrder.getPaidOrders(sFrm, sTo);
		pmnts = TbPayment.getData(sFrm, sTo);
		delItems = TbOrderItem.getCanceledItems(sFrm, sTo);
		
		if (null != orders) {
			for (TbOrder ord1 : orders) {
				updateSales(ord1);
			}
		}
		/*
		if (null != pmnts) {
			for (TbPayment pm1 : pmnts) {
				updatePayment(pm1);
			}
		}
		*/
	}

	public boolean hasOrders() {
		return (null != orders) && (orders.length > 0);
	}
	
	// **********************************************
}
