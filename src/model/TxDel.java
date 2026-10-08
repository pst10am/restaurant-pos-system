package model;

import java.sql.ResultSet;
import java.sql.SQLException;

import resrc.ResData;

public class TxDel {

	private int dlId = 0;
	private String dlDesc = "";
	
	private TxDel() {}
	
	private TxDel(ResultSet rs1) throws SQLException {
		dlId = rs1.getInt("dl_id");
		dlDesc = rs1.getString("dl_desc");
	}
	
	public static TxDel newReason(String _desc) throws SQLException {
		TxDel dl1 = new TxDel();
		dl1.dlDesc = _desc;
		ResData.saveTxDel(dl1);
		return dl1;
	}
	
	public static TxDel[] getDeleteReasons() throws SQLException {
		return ResData.getDeleteReasons();
	}
	
	public void delete() throws SQLException {
		ResData.deleteTxDel(this.getDlId());
	}

	public int getDlId() {
		return dlId;
	}
	public void setDlId(int val) {
		this.dlId = val;
	}

	public String getDlDesc() {
		return dlDesc;
	}
	public void setDlDesc(String val) {
		this.dlDesc = val;
	}

	public static TxDel fromDb(ResultSet rs1) throws SQLException {
		return new TxDel(rs1);
	}
}
