package model;

import java.sql.ResultSet;
import java.sql.SQLException;

import refx.GiftTrxType;
import resrc.ResData;
import resrc.ResUtil;

public class TbGiftTrx {
	
	private int txId = 0;
	private int gftId = 0;
	private java.util.Date txDt = null;
	private GiftTrxType txType = GiftTrxType.NA;
	private double txAmt = 0;
	private int voidTxId = 0;
	
	// Constructor

	private TbGiftTrx(int _gftId, GiftTrxType _type, double _amt) {
		gftId = _gftId;
		txDt = new java.util.Date();
		txType = _type;
		txAmt = _amt;
	}
	
	private TbGiftTrx(ResultSet rs1) throws SQLException {
		gftId = rs1.getInt("gft_id");
		txDt = new java.util.Date(rs1.getTimestamp("tx_dt").getTime());
		txType =GiftTrxType.valueOf(rs1.getString("tx_type"));
		txAmt = rs1.getDouble("tx_amt");
	}
	
	// Factory
	
	public static TbGiftTrx fromDb(ResultSet rs1) throws SQLException {
		return new TbGiftTrx(rs1);
	}
	
	public static TbGiftTrx newRefill(TbGift _gft, double _amt) {
		TbGiftTrx tx1 = new TbGiftTrx(
			_gft.getGftId(), 
			GiftTrxType.Refill, 
			_amt);
		return tx1;
	}
	
	public static TbGiftTrx newRedeem(TbGift _gft, double _amt) {
		TbGiftTrx tx1 = new TbGiftTrx(
			_gft.getGftId(), 
			GiftTrxType.Redeem, 
			_amt);
		return tx1;
	}

	// Miscellaneous
	
	public void save() throws SQLException {
		ResData.saveTbGiftTrx(this);
	}
	
	// Setter+Getter
	
	public int getTxId() {
		return txId;
	}
	public void setTxId(int val) {
		this.txId = val;
	}

	public int getGftId() {
		return gftId;
	}
	public void setGftId(int val) {
		this.gftId = val;
	}

	public java.util.Date getTxDt() {
		return txDt;
	}
	public void setTxDt(java.util.Date val) {
		this.txDt = val;
	}

	public GiftTrxType getTxType() {
		return txType;
	}
	public void setTxType(GiftTrxType val) {
		this.txType = val;
	}

	public double getTxAmt() {
		return txAmt;
	}
	public void setTxAmt(double val) {
		this.txAmt = val;
	}

	public int getVoidTxId() {
		return voidTxId;
	}
	public void setVoidTxId(int val) {
		this.voidTxId = val;
	}

	public static TbGiftTrx[] getTransactions(TbGift gft1) throws SQLException {
		return ResData.getGiftTrans(gft1.getGftId());
	}

	public String toLstString() {
		return String.format("<html>%s _ %s _ <b>$%.2f</b></html>",
			this.getTxType().toString(), 
			ResUtil.dtoc(this.getTxDt(), "MM/dd/YY hh:mm:ss"),
			this.getTxAmt());
	}
}
