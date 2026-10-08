package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import refx.OrderItemType;
import resrc.ResData;

public class TxFdItem {

	private int catId;
	private int itmId;
	private int itmSeq;
	private String itmNamePos;
	private String itmNameWeb;
	private String itmDesc;
	private double itmTax;
	private double itmPrice;
	private boolean itmBypassOpt;
	private int itmStatus;
	{
		catId = 0;
		itmId = 0;
		itmSeq = 0;
		itmNamePos = "";
		itmNameWeb = "";
		itmDesc = "";
		itmTax = 0;
		itmPrice = 0;
		itmBypassOpt = false;
		itmStatus = 1;
	}
	private OrderItemType auxType = OrderItemType.Item;
	
	private java.util.Vector<Integer> optIds = null;
	private java.util.Vector<Integer> prnIds = null;
	public void setOptIds(java.util.Vector<Integer> _opts) {
		optIds = _opts;
	}
	public java.util.Vector<Integer> getOptIds() {
		return optIds;
	}
	public void setPrnIds(java.util.Vector<Integer> _prns) {
		prnIds = _prns;
	}
	public java.util.Vector<Integer> getPrnIds() {
		return prnIds;
	}
	
	// Constructor

	private TxFdItem() {
		auxType = OrderItemType.Item;
	}

	private TxFdItem(OrderItemType _type) {
		auxType = _type;
	}

	private TxFdItem(ResultSet rs1) throws SQLException {
		catId = rs1.getInt("cat_id");
		itmId = rs1.getInt("itm_id");
		itmSeq = rs1.getInt("itm_seq");
		itmNamePos = rs1.getString("itm_name_pos");
		itmNameWeb = rs1.getString("itm_name_web");
		itmDesc = rs1.getString("itm_desc");
		itmTax = rs1.getDouble("itm_tax");
		itmPrice = rs1.getDouble("itm_price");
		itmBypassOpt = rs1.getBoolean("itm_bypass_opt");
		itmStatus = rs1.getInt("itm_status");
		auxType = OrderItemType.Item;
	}
	
	// Factory
	
	public static TxFdItem newObj() {
		return new TxFdItem();
	}

	public static TxFdItem fromDb(ResultSet rs) throws SQLException {
		return new TxFdItem(rs);
	}
	
	public static TxFdItem newOpenItem() {
		return new TxFdItem(OrderItemType.OpenItem);
	}

	public static TxFdItem newSeparatorItem() {
		return new TxFdItem(OrderItemType.Line);
	}
	
	@Override
	public String toString() {
		if (itmStatus == 1) {
			return itmNamePos;
		}
		return String.format("[x] %s", itmNamePos);
	}

	// Setter+Getter
	
	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	public int getItmId() {
		return itmId;
	}

	public void setItmId(int itmId) {
		this.itmId = itmId;
	}

	public int getItmSeq() {
		return itmSeq;
	}

	public void setItmSeq(int itmSeq) {
		this.itmSeq = itmSeq;
	}

	public String getItmNamePos() {
		return itmNamePos;
	}

	public void setItmNamePos(String itmName1) {
		this.itmNamePos = itmName1;
	}

	public String getItmNameWeb() {
		return itmNameWeb;
	}

	public void setItmNameWeb(String itmName2) {
		this.itmNameWeb = itmName2;
	}

	public String getItmDesc() {
		return itmDesc;
	}

	public void setItmDesc(String itmDesc) {
		this.itmDesc = itmDesc;
	}

	public double getItmTax() {
		return itmTax;
	}

	public void setItmTax(double itmTax1) {
		this.itmTax = itmTax1;
	}

	public double getItmPrice() {
		return itmPrice;
	}

	public void setItmPrice(double itmPrice1) {
		this.itmPrice = itmPrice1;
	}

	public boolean isBypassOpt() {
		return itmBypassOpt;
	}

	public void setItmBypassOpt(boolean itmBypassOpt) {
		this.itmBypassOpt = itmBypassOpt;
	}

	public int getItmStatus() {
		return itmStatus;
	}

	public void setItmStatus(int itmStatus) {
		this.itmStatus = itmStatus;
	}
	
	public OrderItemType getOrderItemType() {
		return auxType;
	}

	public static Vector<TxFdItem> getItemsInCategory(int _id) 
			throws SQLException {
		return ResData.getItemsInCategory(_id, false, false);
	}

	public static Vector<TxFdItem> getItemsInCatWithDelete(int _id) 
			throws SQLException {
		return ResData.getItemsInCategory(_id, true, true);
	}
	
	public void clearOptions() {
		if (null == optIds) {
			optIds = new java.util.Vector<>();
		}
		optIds.clear();
	}
	
	public void addOptId(int _id) {
		optIds.add(_id);
	}
	
	public void clearPrinters() {
		if (null == prnIds) {
			prnIds = new java.util.Vector<>();
		}
		prnIds.clear();
	}
	
	public void addPrnId(int _id) {
		prnIds.add(_id);
	}
	
	public void save() throws SQLException {
		ResData.saveTxFdItem(this);
		ResData.saveTxFdItemOptPrt(
			this.itmId, this.optIds, this.prnIds);
	}
}
