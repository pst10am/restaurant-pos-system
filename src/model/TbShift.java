package model;

import java.sql.ResultSet;
import java.sql.SQLException;

import resrc.ResData;
import resrc.ResUtil;

public class TbShift {

	private int clId = 0;
	private java.util.Date clDt = null;
	private int clStatus = 1;
	
	// Constructor
	
	private TbShift() {
		clDt = new java.util.Date();
	}
	
	private TbShift(ResultSet rs1) throws SQLException {
		clId = rs1.getInt("cl_id");
		clDt = new java.util.Date(rs1.getTimestamp("cl_dt").getTime());
		clStatus = rs1.getInt("cl_status");
	}
	
	// Getter+Setter
	
	public int getClId() { return clId; }
	public void setClId(int val) { this.clId = val; }

	public java.util.Date getClDt() { return clDt; }
	public void setClDt(java.util.Date val) { this.clDt = val; }

	public int getClStatus() { return clStatus; }
	public void setClStatus(int val) { this.clStatus = val; }
	
	// ------------------------------------------------
	// Miscellaneous
	// ------------------------------------------------
	
	public void save() throws SQLException {
		ResData.saveTbShift(this);
	}
	
	public String toString() {
		return ResUtil.dtoc(clDt, "MM/dd/yyyy HH:mm:ss");
	}
	
	// static

	public static TbShift[] findAll() throws SQLException {
		return ResData.findAllShift();
	}
	
	public static TbShift newInstance() {
		return new TbShift();
	}
	
	public static TbShift fromDb(ResultSet rs1) throws SQLException {
		return new TbShift(rs1);
	}
	
	public static TbShift firstShift() {
		TbShift cl1 = new TbShift();
		cl1.clDt = ResUtil.ctod("2010-11-12", "yyyy-MM-dd");
		return cl1;
	}
	
	public static TbShift findLastShift() 
			throws SQLException {
		TbShift value = ResData.findLastShift();
		if (null != value) {
			return value;
		}
		return TbShift.firstShift();
	}

	public static TbShift findPrevShift(TbShift clUp) 
			throws SQLException {
		TbShift val = ResData.findPrevShift(clUp);
		if (null != val) {
			return val;
		}
		return TbShift.firstShift();
	}
}
