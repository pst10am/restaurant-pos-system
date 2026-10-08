package model;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import refx.TableService;
import resrc.ResData;

public class TxSctTable {

	private int sctId;
	private int tblId;
	private String tblName;
	private int tblW = 55;
	private int tblH = 55;
	private int tblX;
	private int tblY;
	private boolean tblIsLocked;
	private TableService tblService;
	private int tblStatus;
	{
		sctId = 0;
		tblId = 0;
		tblName = "";
		tblW = 65;
		tblH = 65;
		tblX = 0;
		tblY = 0;
		tblIsLocked = false;
		tblService = TableService.Ready;
		tblStatus = 1;
	}
	
	// Constructor
	
	private TxSctTable() {}
	
	private TxSctTable(ResultSet rs1) throws SQLException {
		sctId = rs1.getInt("sct_id");
		tblId = rs1.getInt("tbl_id");
		tblName = rs1.getString("tbl_name");
		tblW = rs1.getInt("tbl_w");
		tblH = rs1.getInt("tbl_h");
		tblX = rs1.getInt("tbl_x");
		tblY = rs1.getInt("tbl_y");
		tblIsLocked = rs1.getBoolean("tbl_is_locked");
		tblService = TableService.valueOf(rs1.getString("tbl_service"));
		tblStatus = rs1.getInt("tbl_status");
	}

	// Factory
	
	public static TxSctTable newObj() {
		return new TxSctTable();
	}
	
	public static TxSctTable fromDb(ResultSet rs1) throws SQLException {
		return new TxSctTable(rs1);
	}

	public static TxSctTable findById(int _id) throws SQLException {
		return ResData.findTableById(_id);
	}
	
	// Miscellaneous
	
	public Color getTableColor() {
		if (tblIsLocked) return Color.WHITE;
		if (TableService.Ready == tblService) {
			return Color.GREEN;
		}
		if (TableService.Service == tblService) {
			return Color.RED;
		}
		if (TableService.Checked == tblService) {
			return Color.decode("#FFFF00");
		}
		if (TableService.Paid == tblService) {
			return Color.decode("#FE2EF7");
		}
		return Color.GRAY;
	}

	public void refreshTableService() throws SQLException {
		ResData.refreshTableService(this);
	}
	
	// Setter+Getter

	public int getSctId() {
		return sctId;
	}

	public void setSctId(int value) {
		this.sctId = value;
	}

	public int getTblId() {
		return tblId;
	}
	public void setTblId(int value) {
		this.tblId = value;
	}

	public String getTblName() {
		return tblName;
	}
	public void setTblName(String value) {
		this.tblName = value;
	}

	public int getTblW() {
		return tblW;
	}
	public void setTblW(int value) {
		this.tblW = value;
	}

	public int getTblH() {
		return tblH;
	}
	public void setTblH(int value) {
		this.tblH = value;
	}

	public int getTblX() {
		return tblX;
	}
	public void setTblX(int value) {
		this.tblX = value;
	}

	public int getTblY() {
		return tblY;
	}
	public void setTblY(int value) {
		this.tblY = value;
	}

	public boolean isLocked() {
		try {
			tblIsLocked = ResData.isTableLocked(this.tblId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tblIsLocked;
	}
	public boolean getIsLocked() {
		return tblIsLocked;
	}
	public void setIsLocked(boolean value) {
		this.tblIsLocked = value;
	}
	
	public TableService getTblService() {
		return this.tblService;
	}
	public void setTblService(TableService value) {
		this.tblService = value;
	}

	public int getTblStatus() {
		return tblStatus;
	}
	public void setTblStatus(int value) {
		this.tblStatus = value;
	}

	public static void lockTable(int _Id, boolean _val) 
			throws SQLException {
		ResData.lockTableById(_Id, _val);
	}

	public static void updateServiceStatus(int _Id, TableService tblSrv) throws SQLException {
		ResData.updateTableServiceById(_Id, tblSrv);
	}

	public static boolean tableIsPaid(int _id) throws SQLException {
		return ResData.tableIsPaidById(_id);
	}

	public static Vector<TxSctTable> getTablesInSection(TxSct sct1) {
		return ResData.getTablesInSection(sct1);
	}

	public void save() throws SQLException {
		ResData.saveTxSctTable(this);
	}

	public void delete() throws SQLException {
		ResData.deleteTxSctTable(this.tblId);
	}

	public static TxSctTable fromTable(TxSctTable table) {
		TxSctTable tbl1 = new TxSctTable();
		tbl1.setTblName(table.getTblName());
		tbl1.setTblW(table.getTblW());
		tbl1.setTblH(table.getTblH());
		return tbl1;
	}

	public static TxSctTable[] getLockedTables() throws SQLException {
		return ResData.getLockedTables();
	}

	public static TxSctTable[] getServiceTables() throws SQLException {
		return ResData.getServiceTables();
	}

	public static TxSctTable[] getAllTables(int id) throws SQLException {
		return ResData.getAllTablesExcept(id);
	}

	public static TableService updateServiceStatus(int id1) throws SQLException {
		TableService srv1 = TableService.Ready;
		if (ResData.countUnPaidService(id1) > 0) {
			srv1 = TableService.Service;
		} else {
			if (ResData.countUnPaidCheck(id1) > 0) {
				srv1 = TableService.Checked;
			} else {
				// only "paid" or "ready" are correct.
				srv1 = ResData.getTblServiceById(id1);
				if (TableService.Service == srv1 || TableService.Checked == srv1) {
					// "service" or "checked" is wrong, change to "ready"
					srv1 = TableService.Ready;
				} // else is either "paid" or "ready", which is correct.
			}
		}
		TxSctTable.updateServiceStatus(id1, srv1);
		return srv1;
	}
}
