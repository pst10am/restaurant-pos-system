package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import refx.CCCapture;
import refx.PayBy;
import refx.PaySrc;
import resrc.ResData;

public class TbPayment {
	
	private int refId = 0;
	private PaySrc refSrc = PaySrc.NA;
	
	private int ordNo = 0;
	
	private int pmId = 0;
	private java.util.Date pmDt = new java.util.Date();
	private PayBy pmPayBy;
	private double pmAmt = 0;
	private double pmTip = 0;
	private int pmStatus = 1;

	// pay with: credit card
	private String ccTrk1 = "";
	private String ccTrk2 = "";
	private String ccNo = "";
	private String ccAcctName = "";
	private String ccExpMonth = "";
	private String ccExpYear = "";
	private String ccCVV = "";
	private String txNo = "";
	private String txResult = "";
	private String txMessage = "";
	
	// pay with: check
	private String pmChkNo = "";

	// pay with: gift card
	private String pmGiftNo = "";
	
	private boolean pmSettle = false;
	private CCCapture pmCapture = CCCapture.None;
	private int pmNo = 0;
	
	// Constructor
	{
		this.pmNo = ResData.getNewPmNo();
	}

	private TbPayment(PayBy pby, PaySrc psrc) {
		this.pmPayBy = pby;
		this.refSrc = psrc;
	}
	
	private TbPayment(ResultSet rs1) throws SQLException {
		refId = rs1.getInt("ref_id");
		refSrc = PaySrc.valueOf(rs1.getString("ref_src"));
		ordNo = rs1.getInt("ord_no");
		pmId = rs1.getInt("pm_id");
		pmDt = new java.util.Date(rs1.getTimestamp("pm_dt").getTime());
		pmPayBy = PayBy.valueOf(rs1.getString("pm_pay_by"));
		pmAmt = rs1.getDouble("pm_amt");
		pmTip = rs1.getDouble("pm_tip");
		pmStatus = rs1.getInt("pm_status");
		ccTrk1 = rs1.getString("cc_trk1");
		ccTrk2 = rs1.getString("cc_trk2");
		ccNo = rs1.getString("cc_no");
		ccAcctName = rs1.getString("cc_acct_name");
		ccExpMonth = rs1.getString("cc_exp_month");
		ccExpYear = rs1.getString("cc_exp_year");
		ccCVV = rs1.getString("cc_cvv");
		txNo = rs1.getString("tx_no");
		txResult = rs1.getString("tx_result");
		txMessage = rs1.getString("tx_message");
		pmChkNo = rs1.getString("pm_chk_no");
		pmGiftNo = rs1.getString("pm_gift_no");
		pmSettle = rs1.getBoolean("pm_settle");
		pmCapture = CCCapture.valueOf(rs1.getString("pm_capture"));
	}
	
	// Factory
	
	public static TbPayment newPayCash(double _amt, PaySrc _src) {
		TbPayment pm1 = new TbPayment(PayBy.Cash, _src);
		pm1.pmAmt = _amt;
		return pm1;
	}
	
	public static TbPayment newPayCreditCard(double _amt, CCData cdt1, PaySrc _src) {
		TbPayment pm1 = new TbPayment(PayBy.CreditCard, _src);
		pm1.setPmAmt(_amt);
		pm1.setCCData(cdt1);
		return pm1;
	}
	
	public static TbPayment newPayCheck(double _amt, String _chkNo, PaySrc _src) {
		TbPayment pm1 = new TbPayment(PayBy.Check, _src);
		pm1.setPmAmt(_amt);
		pm1.setPmChkNo(_chkNo);
		return pm1;
	}
	
	public static TbPayment newPayGiftCard(double _amt, String _gftNo, PaySrc _src) {
		TbPayment pm1 = new TbPayment(PayBy.GiftCard, _src);
		pm1.setPmAmt(_amt);
		pm1.setPmGiftNo(_gftNo);
		return pm1;
	}
	
	// Miscellaneous
	
	public void save() throws SQLException {
		ResData.saveTbPayment(this);
	}

	public void delete() throws SQLException {
		ResData.deleteTbPayment(this);
	}
	
	public String dispStr() {
		if (PayBy.CreditCard == this.pmPayBy) {
			return String.format("%s = %.2f (x%s)", this.pmPayBy.toString(), this.pmAmt, this.getCCLast4Digits());
		}
		return String.format("%s = %.2f", this.pmPayBy.toString(), this.pmAmt);
	}
	
	public String getCCLast4Digits() {
		return this.ccNo.substring(ccNo.length()-4);
	}

	private void setCCData(CCData cc1) {
		this.ccTrk1 = cc1.getTrk1();
		this.ccTrk2 = cc1.getTrk2();
		this.ccNo = cc1.getAcctNo();
		this.ccAcctName = cc1.getAcctName();
		this.ccExpMonth = cc1.getExpMonth();
		this.ccExpYear = cc1.getExpYear();
		this.ccCVV = cc1.getCVV();
	}
	
	// Setter+Getter

	public int getRefId() {
		return refId;
	}
	public void setRefId(int val) {
		this.refId = val;
	}
	
	public int getOrdNo() {
		return ordNo;
	}
	public void setOrdNo(int val) {
		this.ordNo = val;
	}
	
	public PaySrc getRefSrc() {
		return refSrc;
	}
	public void setRefSrc(PaySrc psrc) {
		this.refSrc = psrc;
	}

	public int getPmId() {
		return pmId;
	}
	public void setPmId(int val) {
		this.pmId = val;
	}

	public java.util.Date getPmDt() {
		return pmDt;
	}
	public void setPmDt(java.util.Date val) {
		this.pmDt = val;
	}

	public PayBy getPmPayBy() {
		return pmPayBy;
	}
	public void setPmPayBy(PayBy val) {
		this.pmPayBy = val;
	}

	public double getPmAmt() {
		return pmAmt;
	}
	public void setPmAmt(double val) {
		this.pmAmt = val;
	}

	public double getPmTip() {
		return pmTip;
	}
	public void setPmTip(double val) {
		this.pmTip = val;
	}

	public int getPmStatus() {
		return pmStatus;
	}
	public void setPmStatus(int val) {
		this.pmStatus = val;
	}
	
	// credit card

	public String getCCTrk1() {
		return ccTrk1;
	}
	public void setCCTrk1(String val) {
		this.ccTrk1 = val;
	}

	public String getCCTrk2() {
		return ccTrk2;
	}
	public void setCCTrk2(String val) {
		this.ccTrk2 = val;
	}

	public String getCCNo() {
		return ccNo;
	}
	public void setCCNo(String val) {
		this.ccNo = val;
	}

	public String getCCAcctName() {
		return ccAcctName;
	}
	public void setCCAcctName(String val) {
		this.ccAcctName = val;
	}

	public String getCCExpMonth() {
		return ccExpMonth;
	}
	public void setCCExpMonth(String val) {
		this.ccExpMonth = val;
	}

	public String getCCExpYear() {
		return ccExpYear;
	}
	public void setCCExpYear(String val) {
		this.ccExpYear = val;
	}

	public String getCCCVV() {
		return ccCVV;
	}
	public void setCCCVV(String val) {
		this.ccCVV = val;
	}

	public String getTxNo() {
		return txNo;
	}
	public void setTxNo(String val) {
		this.txNo = val;
	}

	public String getTxResult() {
		return txResult;
	}
	public void setTxResult(String val) {
		this.txResult = val;
	}

	public String getTxMessage() {
		return txMessage;
	}
	public void setTxMessage(String val) {
		this.txMessage = val;
	}
	
	// check

	public String getPmChkNo() {
		return pmChkNo;
	}
	public void setPmChkNo(String val) {
		this.pmChkNo = val;
	}
	
	// gift

	public String getPmGiftNo() {
		return pmGiftNo;
	}
	public void setPmGiftNo(String val) {
		this.pmGiftNo = val;
	}

	public static TbPayment[] getPaymentHistById(int _id, PaySrc _src) throws SQLException {
		return ResData.getPaymentHistById(_id, _src);
	}

	public static TbPayment[] getPaymentHistByOrdNo(int _ordNo) throws SQLException {
		return ResData.getPaymentHistByOrdNo(_ordNo);
	}

	public static TbPayment fromDb(ResultSet rs1) throws SQLException {
		return new TbPayment(rs1);
	}

	public boolean ccChargeOk() {
		return txResult.equals("Ok");
	}

	public static TbPayment[] getData(java.util.Date dt1, java.util.Date dt2) 
			throws SQLException {
		return ResData.getTbPaymentData(dt1, dt2);
	}

	public boolean isCardSwiped() {
		if (PayBy.CreditCard != this.pmPayBy) {
			return false;
		}
		return !(this.ccTrk1.isEmpty() && this.ccTrk2.isEmpty());
	}

	public String getExpMMYY() {
		return String.format("%2s%2s", 
			this.ccExpMonth, this.ccExpYear);
	}
	
	public boolean isSettle() {
		return pmSettle;
	}

	public CCCapture getPmCapture() {
		return pmCapture;
	}
	public void setPmCapture(CCCapture value) {
		pmCapture = value;
	}
	
	public int getPmNo() {
		return this.pmNo;
	}
	public void setPmNo(int val) {
		this.pmNo = val;
	}

	public static TbPayment[] findTrans(PaySrc psrc, PayBy pby, Date frmDt, Date toDt) 
			throws SQLException {
		return ResData.findPaymentTrans(psrc, pby, frmDt, toDt);
	}
}
