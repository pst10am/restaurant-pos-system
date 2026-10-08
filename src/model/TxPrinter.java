package model;

import java.sql.ResultSet;
import java.sql.SQLException;

import refx.OrderItemType;
import resrc.ResCfg;
import resrc.ResData;

public class TxPrinter {

	private int prnId;
	private String prnIp;
	private String prnName;
	private boolean prnHasBuzzer;
	private int prnStatus;
	{
		prnId = 0;
		prnIp = "";
		prnName = "";
		prnHasBuzzer = false;
		prnStatus = 1;
	}
	
	// Constructor
	
	private TxPrinter() {}
	
	private TxPrinter(ResultSet rs1) throws SQLException {
		prnId = rs1.getInt("prn_id");
		prnIp = rs1.getString("prn_ip");
		prnName = rs1.getString("prn_name");
		prnHasBuzzer = rs1.getBoolean("prn_has_buzzer");
		prnStatus = rs1.getInt("prn_status");
	}
	
	// Factory
	
	public static TxPrinter newInstance() {
		return new TxPrinter();
	}

	public static TxPrinter fromDb(ResultSet rs) throws SQLException {
		return new TxPrinter(rs);
	}
	
	// Miscellaneous
	
	public String toString() {
		return String.format("%s%s@%s", prnStatus==1?"":"[x] ", prnName, prnIp);
	}
	
	private static java.util.HashMap<Integer, TxPrinter> printers;
	public static void init() throws SQLException {
		printers = new java.util.HashMap<>();
		TxPrinter[] tmpPrts = ResData.getPrinters(false);
		for (TxPrinter prt : tmpPrts) {
			printers.put(prt.getPrnId(), prt);
		}
	}
	
	public static TxPrinter getPrinterById(int _id) {
		return printers.get(_id);
	}

	public static TxPrinter getPrinterByIp(String chkIp) {
		java.util.Iterator<Integer> keys = 
			printers.keySet().iterator();
		while (keys.hasNext()) {
			TxPrinter prt1 = printers.get(keys.next());
			if (chkIp.equals(prt1.getPrnIp())) {
				return prt1;
			}
		}
		return null;
	}
	
	public static TxPrinter[] findPrinters(TbOrderItem _itm) {
		int[] prnIds = null;
		if (OrderItemType.OpenItem == _itm.getOdiType()) {
			prnIds = ResData.findPrinterByName(ResCfg.getOpenItemPrinterName());
		} else if (OrderItemType.Item == _itm.getOdiType()) {
			prnIds = ResData.findPrinters(_itm.getItmId());
		} else {
			return null;
		}
		//
		if (null == prnIds || prnIds.length <= 0) return null;
		//
		TxPrinter[] values = new TxPrinter[prnIds.length];
		for (int ix=0; ix < prnIds.length; ix++) {
			values[ix] = getPrinterById(prnIds[ix]);
		}
		return values;
	}
	
	public static TxPrinter[] getAllPrintersWithDelete() throws SQLException {
		return ResData.getPrinters(true);
	}
	
	// Setter+Getter

	public int getPrnId() {
		return prnId;
	}

	public void setPrnId(int prnId) {
		this.prnId = prnId;
	}

	public String getPrnIp() {
		return prnIp;
	}

	public void setPrnIp(String prnIp) {
		this.prnIp = prnIp;
	}

	public String getPrnName() {
		return prnName;
	}

	public void setPrnName(String prnName) {
		this.prnName = prnName;
	}

	public boolean hasBuzzer() {
		return prnHasBuzzer;
	}

	public void setBuzzer(boolean prnBuzzer) {
		this.prnHasBuzzer = prnBuzzer;
	}

	public int getPrnStatus() {
		return prnStatus;
	}

	public void setPrnStatus(int prnStatus) {
		this.prnStatus = prnStatus;
	}

	private static final String html1 = "<html><table cellspacing=0 cellpadding=3>"
			+ "<tr><td align=right>IP</td><td>%s</td>"
			+ "<tr><td align=right>Name</td><td><b>%s</b></td>"
			+ "<tr><td align=right>Buzzer</td><td>%s</td>"
			+ "</table></html>";
	public String getHtmlStr() {
		return String.format(html1,
			this.prnIp,
			this.prnName,
			this.prnHasBuzzer?"<font color=red>Yes</font>":"No"
			);
	}

	public void save() throws SQLException {
		ResData.saveTxPrinter(this);
	}

	public void delete() throws SQLException {
		ResData.deleteTxPrinter(this.prnId, this.prnStatus==1?2:1);
	}

	public boolean isRunner() {
		return prnName.toLowerCase().contains("runner");
	}
}
