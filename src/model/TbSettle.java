package model;

import java.sql.ResultSet;
import java.sql.SQLException;

import resrc.ResData;
import resrc.ResUtil;

public class TbSettle {
	
	private int stlId = 0;
	private java.util.Date stlDt = null;
	private int stlStatus = 0;
	
	// Constructors
	
	private TbSettle() {
		stlId = 0;
		stlDt = new java.util.Date();
		stlStatus = 1;
	}
	
	private TbSettle(java.util.Date _dt) {
		stlId = 0;
		stlDt = _dt;
		stlStatus = 1;
	}
	
	private TbSettle(ResultSet rs1) throws SQLException {
		stlId = rs1.getInt("stl_id");
		stlDt = new java.util.Date(rs1.getTimestamp("stl_dt").getTime());
		stlStatus = rs1.getInt("stl_status");
	}
	
	// Getter+Setter
	
	public int getStlId() { return stlId;}
	public void setStlId(int stlId) { this.stlId = stlId; }

	public java.util.Date getStlDt() { return stlDt; }
	//public void setStlDt(java.util.Date stlDt) { this.stlDt = stlDt; }

	public int getStlStatus() { return stlStatus; }
	//public void setStlStatus(int stlStatus) { this.stlStatus = stlStatus; }
	
	// ------------------------------------------------
	// Miscellaneous
	// ------------------------------------------------
	
	public void save() throws SQLException {
		ResData.saveTbSettle(this);
	}
	
	// static
	
	public static TbSettle newInstance() {
		return new TbSettle();
	}
	
	public static void saveInstance() throws SQLException {
		TbSettle stl1 = new TbSettle();
		stl1.save();
	}
	
	public static TbSettle firstSettle() {
		return new TbSettle(ResUtil.ctod("2010-11-12", "yyyy-MM-dd"));
	}
	
	public static TbSettle findLastSettle() 
			throws SQLException {
		TbSettle stl1 = ResData.findLastSettle();
		if (null != stl1) {
			return stl1;
		}
		return TbSettle.firstSettle();
	}
	
	public static TbSettle fromDb(ResultSet rs1) 
			throws SQLException {
		return new TbSettle(rs1);
	}
	
	public static TbSettle[] findAll() throws SQLException {
		return ResData.findAllSettlement();
	}
	
	public static TbSettle findPrevSettle(TbSettle stl1) throws SQLException {
		TbSettle val = ResData.findPrevSettle(stl1);
		if (null != val) {
			return val;
		}
		return TbSettle.firstSettle();
	}
}
