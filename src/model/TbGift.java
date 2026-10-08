package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import resrc.ResData;

public class TbGift {

	private int gftId = 0;
	private java.util.Date gftActDt = null;
	private String gftCode = "";
	private double gftBal = 0;
	private int gftStatus = 1;
	
	// Constructor
	
	private TbGift() {}
	
	private TbGift(ResultSet rs1) throws SQLException {
		this.gftId = rs1.getInt("gft_id");
		this.gftActDt = new java.util.Date(
			rs1.getTimestamp("gft_act_dt").getTime());
		this.gftCode = rs1.getString("gft_code");
		this.gftBal = rs1.getDouble("gft_balance");
		this.gftStatus = rs1.getInt("gft_status");
	}
	
	// Factory
	
	public static TbGift newCard(String _code) {
		TbGift gft1 = new TbGift();
		gft1.gftActDt = new java.util.Date();
		gft1.gftCode = _code.replaceAll(";", "").replaceAll(Pattern.quote("?"), "");
		return gft1;
	}

	public static TbGift fromDb(ResultSet rs1) {
		try {
			return new TbGift(rs1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static TbGift findCard(String _code) {
		try {
			String code2 = _code.replaceAll(";", "").replaceAll(Pattern.quote("?"), "");
			return ResData.findGiftCard(code2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Miscellaneous

	public void clear() {
		this.gftId = 0;
		this.gftActDt = new java.util.Date();
		this.gftCode = "";
		this.gftBal = 0;
		this.gftStatus = 1;
	}
	
	// Setter+Getter

	public int getGftId() {
		return gftId;
	}
	public void setGftId(int val) {
		this.gftId = val;
	}

	public java.util.Date getGftActDt() {
		return gftActDt;
	}
	public void setGftActDt(java.util.Date val) {
		this.gftActDt = val;
	}

	public String getGftCode() {
		return gftCode;
	}
	public void setGftCode(String val) {
		this.gftCode = val;
	}

	public double getGftBal() {
		return gftBal;
	}
	public void setGftBal(double val) {
		this.gftBal = val;
	}

	public int getGftStatus() {
		return gftStatus;
	}
	public void setGftStatus(int val) {
		this.gftStatus = val;
	}

	public void save() throws SQLException {
		// TODO Auto-generated method stub
		ResData.saveTbGift(this);
	}

	public void recharge(double netPay) {
		this.gftBal = this.gftBal + netPay;
	}
	
	public boolean redeem(TbPayment pm1) throws SQLException {
		double bal1 = ResData.findGiftBalance(this.getGftId());
		if (bal1 < pm1.getPmAmt()) {
			return false;
		}
		
		TbGiftTrx tx1 = TbGiftTrx.newRedeem(this, pm1.getPmAmt());
		tx1.save();
		
		return ResData.updateGiftBalance(this.getGftId(), bal1-pm1.getPmAmt());
	}
}
