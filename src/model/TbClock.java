package model;

import java.sql.ResultSet;
import java.sql.SQLException;

import refx.ClockType;

public class TbClock {
	
	private int clkId = 0;
	private int usrId = 0;
	private String usrName = "";
	private java.util.Date clkDt = null;
	private ClockType clkType = ClockType.NA;

	private TbClock() {}
	
	private TbClock(ResultSet rs1) 
			throws SQLException {
		clkId = rs1.getInt("clk_id");
		usrId = rs1.getInt("usr_id");
		clkDt = new java.util.Date(rs1.getTimestamp("clk_dt").getTime());
		clkType = ClockType.valueOf(rs1.getString("clk_type"));
	}
	
	//
	
	public static TbClock newInstance(TxUser usr1, ClockType clt) {
		TbClock clk1 = new TbClock();
		clk1.setUsrId(usr1.getUsrId());
		clk1.setUsrName(usr1.getUsrName());
		clk1.setClkDt(new java.util.Date());
		clk1.setClkType(clt);
		return clk1;
	}
	
	public static TbClock fromDb(ResultSet rs1) 
			throws SQLException {
		return new TbClock(rs1);
	}
	
	//

	public int getClkId() {
		return clkId;
	}
	public void setClkId(int val) {
		this.clkId = val;
	}

	public int getUsrId() {
		return usrId;
	}
	public void setUsrId(int val) {
		this.usrId = val;
	}

	public String getUsrName() {
		return usrName;
	}
	public void setUsrName(String val) {
		this.usrName = val;
	}

	public java.util.Date getClkDt() {
		return clkDt;
	}
	public void setClkDt(java.util.Date val) {
		this.clkDt = val;
	}

	public ClockType getClkType() {
		return clkType;
	}
	public void setClkType(ClockType val) {
		this.clkType = val;
	}
}
