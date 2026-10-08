package model;

import java.sql.ResultSet;
import java.sql.SQLException;

import refx.AmountType;
import refx.DCType;

public class TxDC {

	private int dcId = 0;
	private String dcDesc = "";
	private DCType dcType;
	private AmountType dcAmtType = AmountType.Percent;
	private double dcAmt = 0;
	private int dcStatus = 1;
	
	// Constructor
	
	protected TxDC() {}

	protected TxDC(DCType _type) {
		dcType = _type;
	}
	
	private TxDC(ResultSet rs1) throws SQLException {
		dcId = rs1.getInt("dc_id");
		dcDesc = rs1.getString("dc_desc");
		dcType = DCType.valueOf(rs1.getString("dc_type"));
		dcAmtType = AmountType.valueOf(rs1.getString("dc_amt_type"));
		dcAmt = rs1.getDouble("dc_amt");
		dcStatus = rs1.getInt("dc_status");
	}
	
	// Factory
	
	public static TxDC newInstance(DCType _type, String _desc, AmountType _amtType, double _amt) {
		TxDC obj1 = new TxDC(_type);
		obj1.dcDesc = _desc;
		obj1.dcAmtType = _amtType;
		obj1.dcAmt = _amt;
		return obj1;
	}
	
	public static TxDC newDiscount() {
		return new TxDC(DCType.Discount);
	}
	
	public static TxDC newCharge() {
		return new TxDC(DCType.Charge);
	}

	public static TxDC fromDb(ResultSet rs) throws SQLException {
		return new TxDC(rs);
	}
	
	// Miscellaneous

	public String dispStr() {
		return this.dcDesc;
	}
	
	public boolean isCharge() {
		return DCType.Charge == this.dcType;
	}
	
	public boolean isServiceCharge() {
		if (!isCharge()) return false;
		return dcDesc.toLowerCase().contains("service");
	}
	
	public boolean isDiscount() {
		return DCType.Discount == this.dcType;
	}
	
	public String toString() {
		String txt = String.format("%d,%s,%s,%s=%.2f", 
			dcId,
			dcType,
			dcDesc,
			dcAmtType,
			dcAmt);
		return txt;
	}
	
	// Setter+Getter

	public int getDcId() {
		return dcId;
	}
	public void setDcId(int value) {
		this.dcId = value;
	}
	
	public DCType getDcType() {
		return this.dcType;
	}

	public String getDcDesc() {
		return dcDesc;
	}
	public void setDcDesc(String value) {
		this.dcDesc = value;
	}

	public AmountType getDcAmtType() {
		return dcAmtType;
	}
	public void setDcAmtType(AmountType value) {
		this.dcAmtType = value;
	}

	public double getDcAmt() {
		return dcAmt;
	}
	public void setDcAmt(double value) {
		this.dcAmt = value;
	}

	public int getDcStatus() {
		return dcStatus;
	}
	public void setDcStatus(int value) {
		this.dcStatus = value;
	}
}
