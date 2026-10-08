package model;

import refx.CCCapture;
import refx.PayBy;
import refx.PaySrc;
import refx.RptSaleType;
import resrc.ResUtil;

public class RptData1 {

	private TbShift sFrm, sTo;
	private TbOrder[] orders;
	private TbPayment[] pmnts;

	// table 1
	private java.util.HashMap<RptSaleType, RptSaleInfo> saleInfo;
	
	// table 2
	private java.util.HashMap<RptSaleType, java.util.HashMap<PayBy, RptPaymentInfo>> pmInfoBySaleType;
	
	// table 3
	private java.util.HashMap<PaySrc, java.util.HashMap<PayBy, RptPaymentInfo>> pmInfo;
	
	// canceled items
	private TbOrderItem[] delItems;
	
	// --
	
	private RptData1() {
		saleInfo = new java.util.HashMap<>();
		pmInfoBySaleType = new java.util.HashMap<>();
		pmInfo = new java.util.HashMap<>();
	}
	
	public static RptData1 newInstance() {
		return new RptData1();
	}
	
	// --
	
	private void updateSales(TbOrder ord1) {
		RptSaleType saleType = RptSaleType.getRptSaleType(
			ord1.getOrdType(), ord1.getOrdToGoType());
		if (!saleInfo.containsKey(saleType)) {
			saleInfo.put(saleType, new RptSaleInfo());
		}
		//
		RptSaleInfo s1 = saleInfo.get(saleType);
		if (-1 == s1.frmOrdNo || ord1.getOrdNo() < s1.frmOrdNo) {
			s1.frmOrdNo = ord1.getOrdNo();
		}
		if (-1 == s1.toOrdNo || ord1.getOrdNo() > s1.toOrdNo) {
			s1.toOrdNo = ord1.getOrdNo();
		}
		s1.totalOrder += 1;
		s1.sumAmtBf += ord1.getOrdAmtBf();
		s1.sumAmtTax += ord1.getOrdAmtTax();
		s1.sumAmtDeliFee += ord1.getDeliFee();
		s1.sumAmtDisc += ord1.getOrdAmtDisc();
		s1.sumAmtCharge += ord1.getOrdAmtCharge();
		s1.sumAmtNet += ord1.getOrdAmtNet();
		s1.sumPmChange += ord1.getOrdPmChange();
		//
		RptSaleInfo.gtOrder += 1;
		RptSaleInfo.gtSumAmtBf += ord1.getOrdAmtBf();
		RptSaleInfo.gtSumAmtTax += ord1.getOrdAmtTax();
		RptSaleInfo.gtSumAmtDeliFee += ord1.getDeliFee();
		RptSaleInfo.gtSumAmtDisc += ord1.getOrdAmtDisc();
		RptSaleInfo.gtSumAmtCharge += ord1.getOrdAmtCharge();
		RptSaleInfo.gtSumAmtNet += ord1.getOrdAmtNet();
		RptSaleInfo.gtSumPmChange += ord1.getOrdPmChange();
		//
		for (TbPayment pm1 : ord1.getPayments()) {
			updatePaymentByType(saleType, pm1);
		}
	}
	
	private void updatePaymentByType(RptSaleType saleType, TbPayment pm1) {
		if (!pmInfoBySaleType.containsKey(saleType)) {
			java.util.HashMap<PayBy, RptPaymentInfo> pmPayBy = new java.util.HashMap<>();
			pmPayBy.put(pm1.getPmPayBy(), new RptPaymentInfo());
			pmInfoBySaleType.put(saleType, pmPayBy);
		}
		//
		java.util.HashMap<PayBy, RptPaymentInfo> pmPayBy = pmInfoBySaleType.get(saleType);
		if (!pmPayBy.containsKey(pm1.getPmPayBy())) {
			pmPayBy.put(pm1.getPmPayBy(), new RptPaymentInfo());
		}
		//
		RptPaymentInfo pmInf = pmPayBy.get(pm1.getPmPayBy());
		
		pmInf.totalPayment += 1;
		pmInf.sumPmAmt += pm1.getPmAmt();
		pmInf.sumPmTip += pm1.getPmTip();
		
		RptPaymentInfo.gtTotalPayment += 1;
		RptPaymentInfo.gtSumPmAmt += pm1.getPmAmt();
		RptPaymentInfo.gtSumPmTip += pm1.getPmTip();
	}
	
	private void updatePayment(TbPayment pm1) {
		if (!pmInfo.containsKey(pm1.getRefSrc())) {
			java.util.HashMap<PayBy, RptPaymentInfo> _payBy = new java.util.HashMap<>();
			_payBy.put(pm1.getPmPayBy(), new RptPaymentInfo());
			pmInfo.put(pm1.getRefSrc(), _payBy);
		}
		//
		java.util.HashMap<PayBy, RptPaymentInfo> pmPayBy = pmInfo.get(pm1.getRefSrc());
		if (!pmPayBy.containsKey(pm1.getPmPayBy())) {
			pmPayBy.put(pm1.getPmPayBy(), new RptPaymentInfo());
		}
		//
		RptPaymentInfo pmInf = pmPayBy.get(pm1.getPmPayBy());
		pmInf.totalPayment += 1;
		pmInf.sumPmAmt += pm1.getPmAmt();
		pmInf.sumPmTip += pm1.getPmTip();
	}
	
	// --
	
	public TbOrder[] getOrders() {
		return orders;
	}
	
	public TbPayment[] getPayments() {
		return pmnts;
	}
	
	public TbOrderItem[] getCanceledItems() {
		return delItems;
	}

	public void runData(TbShift _sFrm, TbShift _sTo) throws Exception {
		RptSaleInfo.init();
		RptPaymentInfo.init();
		//
		sFrm = _sFrm;
		sTo = _sTo;
		orders = TbOrder.getPaidOrders(sFrm.getClDt(), sTo.getClDt());
		pmnts = TbPayment.getData(sFrm.getClDt(), sTo.getClDt());
		delItems = TbOrderItem.getCanceledItems(sFrm.getClDt(), sTo.getClDt());
		//
		if (null != orders) {
			for (TbOrder ord1 : orders) {
				updateSales(ord1);
			}
		}
		if (null != pmnts) {
			for (TbPayment pm1 : pmnts) {
				updatePayment(pm1);
			}
		}
	}

	public String getFrmDtStr() {
		return ResUtil.dtoc(sFrm.getClDt(), "M/d/yy hh:mm a").toLowerCase();
	}

	public String getToDtStr() {
		return ResUtil.dtoc(sTo.getClDt(), "M/d/yy hh:mm a").toLowerCase();
	}
	
	// Order By Sale Type

	public boolean hasSaleInfo(RptSaleType rst) {
		return saleInfo.containsKey(rst);
	}
	public RptSaleInfo getSaleInfo(RptSaleType rst) {
		if (!saleInfo.containsKey(rst)) {
			return new RptSaleInfo();
		}
		return saleInfo.get(rst);
	}
	
	// Payment By Sale Type

	public boolean hasPmBySaleType(RptSaleType rst) {
		return pmInfoBySaleType.containsKey(rst);
	}
	public boolean hasPmBySaleType(RptSaleType rst, PayBy pby) {
		if (!hasPmBySaleType(rst)) {
			return false;
		}
		java.util.HashMap<PayBy, RptPaymentInfo> pbyInf = 
			pmInfoBySaleType.get(rst);
		return pbyInf.containsKey(pby);
	}
	public RptPaymentInfo getPmBySaleType(RptSaleType rst, PayBy pby) {
		java.util.HashMap<PayBy, RptPaymentInfo> pbyInf = 
			pmInfoBySaleType.get(rst);
		return pbyInf.get(pby);
	}
	
	// Payment By Source (Order, Gift)

	public boolean hasPmInfo(PaySrc psrc) {
		return pmInfo.containsKey(psrc);
	}
	public boolean hasPmInfo(PaySrc psrc, PayBy pby) {
		if (!pmInfo.containsKey(psrc)) {
			return false;
		}
		java.util.HashMap<PayBy, RptPaymentInfo> pinfs = pmInfo.get(psrc);
		return pinfs.containsKey(pby);
	}

	public RptPaymentInfo getPmInfo(PaySrc psrc, PayBy pby) {
		java.util.HashMap<PayBy, RptPaymentInfo> pinfs = pmInfo.get(psrc);
		return pinfs.get(pby);
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
			if (ord1.getCharges().size() > 0) {
				return true;
			}
		}
		return false;
	}

	public boolean hasDiscounts() {
		if (null == orders) return false;
		for (TbOrder ord1 : orders) {
			if (ord1.getDiscounts().size() > 0) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasCanceledItems() {
		return null != delItems && delItems.length > 0;
	}

	// count transaction.
	
	private int countPayBy(PayBy pby) {
		if (null == pmnts || pmnts.length <= 0) return 0;
		int val = 0;
		for (TbPayment pm1 : pmnts) {
			if (pby == pm1.getPmPayBy()) {
				val += 1;
			}
		}
		return val;
	}
	public int countCash() {
		return countPayBy(PayBy.Cash);
	}
	public int countCredit() {
		return countPayBy(PayBy.CreditCard);
	}
	
	// total by pay type (Cash, Credit, Check, GiftCert)

	private double sumPayBy(PayBy pby) {
		if (null == pmnts || pmnts.length <= 0) return 0;
		double val = 0;
		for (TbPayment pm1 : pmnts) {
			if (pby == pm1.getPmPayBy()) {
				val += pm1.getPmAmt();
			}
		}
		if (PayBy.Cash == pby) {
			double sumChange = 0;
			if (null != orders && orders.length > 0) {
				for (TbOrder ord1 : orders) {
					sumChange += ord1.getOrdPmChange();
				}
			}
			val = val - sumChange;
		}
		return val;
	}
	public double sumCash() {
		return sumPayBy(PayBy.Cash);
	}
	public double sumCredit() {
		return sumPayBy(PayBy.CreditCard);
	}
	
	public double sumTip() {
		if (null == pmnts || pmnts.length <= 0) return 0;
		double val = 0;
		for (TbPayment pm1 : pmnts) {
			val += pm1.getPmTip();
		}
		return val;
	}

	public double sumDeliFee() {
		if (null == orders || orders.length <= 0) return 0;
		double val = 0;
		for (TbOrder ord1 : orders) {
			if (!ord1.hasCharge()) {
				continue;
			}
			for (TbOrderDC dc1 : ord1.getCharges()) {
				if (dc1.isServiceCharge()) {
					val += dc1.getOdcAmt();
				}
			}
		}
		return val;
	}

	public double sumService() {
		// TODO Auto-generated method stub
		return 0;
	}
}
