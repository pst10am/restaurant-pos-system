package model;

import java.sql.ResultSet;
import java.sql.SQLException;

import resrc.ResData;

public class TxCfgGrp {
	
	private int grpId = 0;
	private String grpName = "";
	private java.util.Vector<TxCfgItem> items = new java.util.Vector<>();
	
	private TxCfgGrp() {
	}
	
	public static TxCfgGrp fromDb(ResultSet rs1) throws SQLException {
		TxCfgGrp obj1 = new TxCfgGrp();
		obj1.grpId = rs1.getInt("grp_id");
		obj1.grpName = rs1.getString("grp_name");
		return obj1;
	}
	
	public static java.util.Vector<TxCfgGrp> getConfigGroups() throws SQLException {
		return ResData.getConfigGroups();
	}
	
	public String toString() {
		return grpName;
	}
	
	//
	
	public java.util.Vector<TxCfgItem> getItems() {
		return items;
	}
	public void setItems(java.util.Vector<TxCfgItem> vals) {
		items = vals;
	}

	public int getGrpId() {
		return grpId;
	}
	public void setGrpId(int val) {
		this.grpId = val;
	}

	public String getGrpName() {
		return grpName;
	}
	public void setGrpName(String val) {
		this.grpName = val;
	}
}
