package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import refx.OrderItemType;
import resrc.ResCfg;
import resrc.ResData;
import resrc.ResUtil;

public class TbOrderItem {

	private int ordId = 0;
	private int odiId = 0;
	private java.util.Date odiDt = null;
	private OrderItemType odiType = OrderItemType.NA;
	private int catId = 0;
	private String catNamePos = "";
	private int itmId = 0;
	private String itmNamePos = "";
	private String itmNameWeb = "";
	private boolean itmBypassOpt = false;
	private double itmPrice = 0;
	private double itmTax = 0;
	private int odiQty = 0;
	private double odiAmtBf = 0;
	private double odiAmtTax = 0;
	private double odiAmtOpt = 0;
	private double odiAmtNet = 0;
	private String odiNote = "";
	private double odiExtra = 0;
	private boolean odiHold = false;
	private boolean odiPrinted = false;
	private int odiStatus = 1;
	private int odiSeq = 0;
	
	private String odiRemark = "";
	
	private int setNo = 1;

	private java.util.Vector<TxOptItem> options = null;
	{
		options = new java.util.Vector<>();
	}
	
	// Constructor

	private TbOrderItem() {}
	
	private TbOrderItem(ResultSet rs1) throws SQLException {
		ordId = rs1.getInt("ord_id");
		odiId = rs1.getInt("odi_id");
		odiDt = new java.util.Date(rs1.getTimestamp("odi_dt").getTime());
		odiType = OrderItemType.valueOf(rs1.getString("odi_type"));
		catId = rs1.getInt("cat_id");
		catNamePos = ""; // rs1.getString("cat_name_pos");
		itmId = rs1.getInt("itm_id");
		itmNamePos = rs1.getString("itm_name_pos");
		itmNameWeb = ""; // rs1.getString("itm_name_web");
		itmBypassOpt = rs1.getBoolean("itm_bypass_opt");
		itmPrice = rs1.getDouble("itm_price");
		itmTax = rs1.getDouble("itm_tax");
		odiQty = rs1.getInt("odi_qty");
		odiAmtBf = rs1.getDouble("odi_amt_bf");
		odiAmtTax = rs1.getDouble("odi_amt_tax");
		odiAmtOpt = rs1.getDouble("odi_amt_opt");
		odiAmtNet = rs1.getInt("odi_amt_net");
		odiNote = rs1.getString("odi_note");
		odiExtra = rs1.getInt("odi_extra");
		odiHold = rs1.getBoolean("odi_hold");
		odiPrinted = rs1.getBoolean("odi_printed");
		odiStatus = rs1.getInt("odi_status");
		odiSeq = rs1.getInt("odi_seq");
		odiRemark = rs1.getString("odi_remark");
		
		ResData.updateOrderItem_Options(odiId, options);
		if (null != options && options.size() > 0) {
			java.util.Collections.sort(options);
		}
	}
	
	// Factory
	
	public static TbOrderItem newItem(TxFdItem fdItm) {
		TbOrderItem odi1 = new TbOrderItem();
		odi1.odiType = OrderItemType.Item;
		odi1.odiDt = new java.util.Date();
		odi1.catId = fdItm.getCatId();
		odi1.itmId = fdItm.getItmId();
		odi1.itmNamePos = fdItm.getItmNamePos();
		odi1.itmBypassOpt = fdItm.isBypassOpt();
		odi1.itmPrice = fdItm.getItmPrice();
		odi1.itmTax = fdItm.getItmTax();
		odi1.odiQty = 1;
		odi1.updateOdiAmt();
		return odi1;
	}
	
	public static TbOrderItem newOpenItem() {
		TbOrderItem odi1 = new TbOrderItem();
		odi1.odiType = OrderItemType.OpenItem;
		odi1.odiDt = new java.util.Date();
		odi1.itmNamePos = "";
		odi1.itmBypassOpt = false;
		odi1.itmTax = ResCfg.getOpenItemTax();
		odi1.odiQty = 1;
		odi1.updateOdiAmt();
		return odi1;
	}
	
	public static TbOrderItem newLineItem() {
		TbOrderItem odi1 = new TbOrderItem();
		odi1.odiType = OrderItemType.Line;
		odi1.odiDt = new java.util.Date();
		odi1.itmNamePos = "";
		odi1.itmBypassOpt = true;
		odi1.itmTax = 0;
		odi1.odiQty = 0;
		return odi1;
	}
	
	public static TbOrderItem fromDb(ResultSet rs) throws SQLException {
		return new TbOrderItem(rs);
	}
	
	// Miscellaneous

	public static TbOrderItem[] getCanceledItems(Date clDtFrom, Date clDtTo) 
			throws SQLException {
		return ResData.getCanceledItems(clDtFrom, clDtTo);
	}
	
	private void updateOdiAmt() {
		this.odiAmtOpt = 0;
		for (TxOptItem opi1 : options) {
			this.odiAmtOpt += opi1.getOpiPrice();
		}
		this.odiAmtBf = this.odiQty * (this.itmPrice + this.odiAmtOpt + this.odiExtra);
		this.odiAmtTax = (this.itmTax * this.odiAmtBf)/100;
		this.odiAmtNet = this.odiAmtBf + this.odiAmtTax;
	}
	
	public void updateOptionItem(boolean _addNew, TxOpt _opt, TxOptItem _opi) {
		if (_addNew) {
			if (_opt.isMandatory()) {
				for (TxOptItem opi1 : options) {
					if (opi1.getOptId() == _opi.getOptId()) {
						options.remove(opi1);
						break;
					}
				}
			}
			options.add(_opi);
			java.util.Collections.sort(options);
		} else {
			for (TxOptItem opi1 : options) {
				if (opi1.getOpiId() == _opi.getOpiId()) {
					options.remove(opi1);
					break;
				}
			}
		}
		updateOdiAmt();
	}
	
	public boolean hasValidOption() throws SQLException {
		if (OrderItemType.Item != this.getOdiType()) {
			if (OrderItemType.OpenItem == this.getOdiType()) {
				return !this.getItmNamePos().trim().isEmpty();
			}
			return true;
		}
		TxOptIdMand[] optInfo = ResData.getOptIdMandByItmId(this.getItmId());
		if (null == optInfo) {
			options.clear();
			return true;
		}
		for (TxOptIdMand odm : optInfo) {
			if (!odm.optMand) {
				continue;
			}
			boolean fndFlg = false;
			for (TxOptItem opi1 : options) {
				if (odm.optId == opi1.getOptId()) {
					fndFlg = true;
					break;
				}
			}
			if (!fndFlg) {
				return false;
			}
		}
		return true;
	}

	public String getOpiStr() {
		if (null == options || options.size() <= 0) {
			return "";
		}
		StringBuilder bld1 = new StringBuilder();
		for (TxOptItem opi1 : options) {
			if (bld1.length() > 0) {
				bld1.append(',');
			}
			bld1.append(opi1.getDispStr());
		}
		return bld1.toString();
	}
	
	private static final String html1 = "<html>"
		+ "<table border=0 cellspacing=0 cellpadding=0>"
		+ "%s%s"
		+ "</table></html>";
	public String getInfoHtml() {
        if (OrderItemType.Line == this.getOdiType()) {
        	return "- SEPARATOR -";
        }
        // option
        String optTxt = this.getOpiStr();
        if (!optTxt.isEmpty()) {
        	optTxt = String.format(
        			"<tr><td><b>Option</b></td></tr>"
        			+ "<tr><td>%s</td></tr>", optTxt);
        }
        // note
        String optNote = "";
        if (!this.getOdiNote().isEmpty()) {
        	optNote = this.getOdiNote().trim();
        }
        if (this.getOdiExtra() > 0) {
        	optNote = String.format("%s(+$%.2f)", optNote, this.getOdiExtra());
        }
        if (!optNote.isEmpty()) {
        	optNote = String.format(
    			"<tr><td height=5></td></tr>"
    			+ "<tr><td><b>*Note/Special*</b></td></tr>"
    			+ "<tr><td>%s</td></tr>", optNote);
        }
        return String.format(html1, optTxt, optNote);
	}

	public boolean hasOpi(TxOptItem _opi) {
		if (options.size() <= 0) return false;
		for (TxOptItem opi1 : options) {
			if (opi1.getOpiId() == _opi.getOpiId()) {
				return true;
			}
		}
		return false;
	}
	
	public void save() throws SQLException {
		ResData.saveTbOrderItem(this);
		//
		ResData.clearTbOrderItemOpi(this.getOdiId());
		for (TxOptItem opi1 : options) {
			ResData.saveTbOrderItemOpi(this.getOdiId(), opi1);
		}
	}

	// Setter+Getter

	public int getOrdId() {
		return ordId;
	}

	public void setOrdId(int ordId) {
		this.ordId = ordId;
	}

	public int getOdiId() {
		return odiId;
	}

	public void setOdiId(int odiId) {
		this.odiId = odiId;
	}

	public java.util.Date getOdiDt() {
		return odiDt;
	}

	public void setOdiDt(java.util.Date odiDt) {
		this.odiDt = odiDt;
	}

	public OrderItemType getOdiType() {
		return odiType;
	}

	public void setOdiType(OrderItemType odiType) {
		this.odiType = odiType;
	}

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	public String getCatNamePos() {
		return catNamePos;
	}

	public void setCatNamePos(String catNamePos) {
		this.catNamePos = catNamePos;
	}

	public int getItmId() {
		return itmId;
	}

	public void setItmId(int itmId) {
		this.itmId = itmId;
	}

	public String getItmNamePos() {
		return itmNamePos;
	}

	public void setItmNamePos(String itmNamePos) {
		this.itmNamePos = itmNamePos;
	}

	public String getItmNameWeb() {
		return itmNameWeb;
	}

	public void setItmNameWeb(String itmNameWeb) {
		this.itmNameWeb = itmNameWeb;
	}

	public boolean isBypassOpt() {
		return itmBypassOpt;
	}

	public void setItmBypassOpt(boolean itmBypassOpt) {
		this.itmBypassOpt = itmBypassOpt;
	}

	public double getItmPrice() {
		return itmPrice;
	}

	public void setItmPrice(double itmPrice) {
		this.itmPrice = itmPrice;
		updateOdiAmt();
	}

	public double getItmTax() {
		return itmTax;
	}

	public void setItmTax(double itmTax) {
		this.itmTax = itmTax;
	}

	public int getOdiQty() {
		return odiQty;
	}

	public void setOdiQty(int odiQty) {
		this.odiQty = odiQty;
		updateOdiAmt();
	}

	public String getOdiNote() {
		return odiNote;
	}

	public void setOdiNote(String odiNote) {
		this.odiNote = odiNote;
	}

	public double getOdiExtra() {
		return odiExtra;
	}

	public void setOdiExtra(double value) {
		this.odiExtra = value;
		updateOdiAmt();
	}
	
	public double getOdiAmtBf() {
		return this.odiAmtBf;
	}
	
	public double getOdiAmtTax() {
		return this.odiAmtTax;
	}
	
	public double getOdiAmtOpt() {
		return this.odiAmtOpt;
	}
	
	public double getOdiAmtNet() {
		return this.odiAmtNet;
	}

	public boolean isHold() {
		return odiHold;
	}

	public void setOdiHold(boolean odiHold) {
		this.odiHold = odiHold;
	}

	public int getOdiStatus() {
		return odiStatus;
	}

	public void setOdiStatus(int odiStatus) {
		this.odiStatus = odiStatus;
	}
	
	public boolean isPrinted() {
		return this.odiPrinted;
	}
	
	public void setOdiPrinted(boolean val) {
		this.odiPrinted = val;
	}

	public static void delete(int delId, TxDel delObj) throws SQLException {
		ResData.deleteOrderItem(delId, delObj);
	}
	
	public int getOdiSeq() {
		return this.odiSeq;
	}
	public void setOdiSeq(int val) {
		this.odiSeq = val;
	}
	
	public void setSetNo(int val) {
		this.setNo = val;
	}
	public int getSetNo() {
		return this.setNo;
	}
	
	public String getOdiRemark() {
		return this.odiRemark;
	}

	public boolean isActive() {
		// TODO Auto-generated method stub
		return this.odiStatus == 1;
	}
}
