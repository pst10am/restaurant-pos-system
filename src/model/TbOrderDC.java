package model;

import java.sql.ResultSet;
import java.sql.SQLException;

import refx.AmountType;
import refx.DCType;

public class TbOrderDC {
	
	private int ordId = 0;
	private TxDC dcObj = null;
	private double odcAmt = 0;
	
	// Constructor
	
	private TbOrderDC(TxDC dc1) {
		dcObj = dc1;
	}
	
	private TbOrderDC(ResultSet rs1) throws SQLException {
		ordId = rs1.getInt("ord_id");
		dcObj = TxDC.fromDb(rs1);
		odcAmt = rs1.getDouble("odc_amt");
	}
	
	// Factory
	
	public static TbOrderDC newInstance(TxDC dc1) {
		return new TbOrderDC(dc1);
	}
	
	public static TbOrderDC fromDb(ResultSet rs1) throws SQLException {
		return new TbOrderDC(rs1);
	}

	// tb_order
	public int getOrdId() {
		return ordId;
	}
	public void setOrdId(int value) {
		ordId = value;
	}

	// tb_order_dc
	public double getOdcAmt() {
		return odcAmt;
	}
	public void setOdcAmt(double value) {
		odcAmt = value;
	}

	// tx_dc
	public int getDcId() {
		return dcObj.getDcId();
	}
	public DCType getDcType() {
		return dcObj.getDcType();
	}
	public AmountType getDcAmtType() {
		return dcObj.getDcAmtType();
	}
	public String getDcDesc() {
		return dcObj.getDcDesc();
	}
	public double getDcAmt() {
		return dcObj.getDcAmt();
	}

	public String dispStr() {
		return dcObj.dispStr();
	}

	public boolean isServiceCharge() {
		return dcObj.isServiceCharge();
	}
}
