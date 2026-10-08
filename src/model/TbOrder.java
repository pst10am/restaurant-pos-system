package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import refx.AmountType;
import refx.OrderType;
import refx.TableService;
import refx.ToGoType;
import resrc.ResData;
import resrc.ResUtil;

public class TbOrder {

	private int ordId = 0;
	private int ordNo= 0;
	
	private java.util.Date ordDt = null;
	private OrderType ordType;
	private ToGoType ordToGoType = ToGoType.NA;
	
	private int tblId = 0;
	private String tblName = "";
	private int tblPartySize = 0;
	
	private int cstId = 0;
	private String cstCode = "";
	private String cstName = "";
	private String cstPhone = "";
	private String cstEmail = "";
	private String cstNote = "";
	
	private String deliAddr1 = "";
	private String deliAddr2 = "";
	private String deliUnitNo = "";
	private String deliCity = "";
	private String deliState = "";
	private double deliLat = 0;
	private double deliLon = 0;
	private double deliDist = 0;
	private double deliFee = 0;
	
	private int ordCntCheck = 0;
	private int ordCntReceipt = 0;
	
	private double ordAmtBf = 0;
	private double ordAmtTax = 0;
	private double ordAmtDisc = 0;
	private double ordAmtCharge = 0;
	private double ordAmtNet = 0;
	
	private boolean ordPaid = false;
	private boolean ordPick = false;
	private int ordStatus = 1;
	private double ordPmChange = 0;

	private java.util.Vector<TbOrderItem> items = new java.util.Vector<>();
	private java.util.Vector<TbOrderDC> discounts = new java.util.Vector<>();
	private java.util.Vector<TbOrderDC> charges = new java.util.Vector<>();
	private java.util.Vector<TbPayment> payments = new java.util.Vector<>();
	
	// Constructor
	
	private TbOrder() {}
	
	private TbOrder(OrderType _type) {
		ordNo = ResData.getNewOrdNo();
		ordDt = new java.util.Date();
		ordType = _type;
	}
	
	private TbOrder(ResultSet rs1) throws SQLException {
		this.setOrdId(rs1.getInt("ord_id"));
		this.setOrdNo(rs1.getInt("ord_no"));

		this.setOrdDt(new java.util.Date(rs1.getTimestamp("ord_dt").getTime()));
		this.setOrdType(OrderType.valueOf(rs1.getString("ord_type")));
		this.setOrdToGoType(ToGoType.valueOf(rs1.getString("ord_togo_type")));

		this.setTblId(rs1.getInt("tbl_id"));
		this.setTblName(rs1.getString("tbl_name"));
		this.setPartySize(rs1.getInt("tbl_party_size"));

		this.setCstId(rs1.getInt("cst_id"));
		this.setCstCode(rs1.getString("cst_code"));
		this.setCstName(rs1.getString("cst_name"));
		this.setCstPhone(rs1.getString("cst_phone"));
		this.setCstEmail(rs1.getString("cst_email"));
		this.setCstNote(rs1.getString("cst_note"));

		this.setDeliAddr1(rs1.getString("deli_addr_1"));
		this.setDeliAddr2(rs1.getString("deli_addr_2"));
		this.setDeliUnitNo(rs1.getString("deli_unit_no"));
		this.setDeliCity(rs1.getString("deli_city"));
		this.setDeliState(rs1.getString("deli_state"));
		this.setDeliLat(rs1.getDouble("deli_lat"));
		this.setDeliLon(rs1.getDouble("deli_lon"));
		this.setDeliDist(rs1.getDouble("deli_dist"));
		this.setDeliFee(rs1.getDouble("deli_fee"));

		this.setOrdCntCheck(rs1.getInt("ord_cnt_check"));
		this.setOrdCntReceipt(rs1.getInt("ord_cnt_receipt"));

		this.ordAmtBf = rs1.getDouble("ord_amt_bf");
		this.ordAmtTax = rs1.getDouble("ord_amt_tax");
		this.ordAmtDisc = rs1.getDouble("ord_amt_disc");
		this.ordAmtCharge = rs1.getDouble("ord_amt_charge");
		this.ordAmtNet = rs1.getDouble("ord_amt_net");

		this.setOrdPaid(rs1.getBoolean("ord_paid"));
		this.setOrdPick(rs1.getBoolean("ord_pick"));
		this.setOrdStatus(rs1.getInt("ord_status"));
		
		this.setOrdPmChange(rs1.getDouble("ord_pm_change"));
		
		ResData.updateOrder_Items(this.getOrdId(), this.items);
		ResData.updateOrder_DC(this.getOrdId(), this.discounts, this.charges);
		ResData.updateOrder_Payment(this.getOrdId(), this.payments);
	}
	
	// Factory
	
	public static TbOrder newDineInOrder(TbOrder ord0) {
		TbOrder ord1 = new TbOrder(ord0.getOrdType());
		ord1.setTblId(ord0.getTblId());
		ord1.setTblName(ord0.getTblName());
		ord1.setCstId(ord0.getCstId());
		ord1.setCstName(ord0.getCstName());
		ord1.setCstPhone(ord0.getCstPhone());
		ord1.setCstEmail(ord0.getCstEmail());
		ord1.setCstNote(ord0.getCstNote());
		return ord1;
	}
	
	public static TbOrder newDineInOrder(TxSctTable tbl1) {
		TbOrder ord1 = new TbOrder(OrderType.DineIn);
		ord1.setTblId(tbl1.getTblId());
		ord1.setTblName(tbl1.getTblName());
		return ord1;
	}
	
	public static TbOrder newToGoOrder() {
		return new TbOrder(OrderType.ToGo);
	}

	public static TbOrder fromDb(ResultSet rs1) throws SQLException {
		return new TbOrder(rs1);
	}
	
	public boolean hasCharge() {
		return null != charges && charges.size() > 0;
	}
	
	public boolean hasDiscount() {
		return null != discounts && discounts.size() > 0;
	}
	
	// Miscellaneous
	
	private static double _sumDC(double amt1, java.util.Vector<TbOrderDC> dcs) {
		double value = 0;
		for (TbOrderDC odc1 : dcs) {
			if (AmountType.Percent == odc1.getDcAmtType()) {
				odc1.setOdcAmt((amt1 * odc1.getDcAmt())/100);
				value += odc1.getOdcAmt();
			} else if (AmountType.Fixed == odc1.getDcAmtType()) {
				odc1.setOdcAmt(odc1.getDcAmt());
				value += odc1.getOdcAmt();
			}
		}
		return value;
	}
	
	public void updateAmount() {
		
		this.ordAmtBf = 0;
		this.ordAmtTax = 0;
		for (TbOrderItem odi1 : items) {
			this.ordAmtBf += odi1.getOdiAmtBf();
			this.ordAmtTax += odi1.getOdiAmtTax();
		}
		double tmpBf = this.ordAmtBf + this.ordAmtTax;
		
		this.ordAmtDisc = _sumDC(tmpBf, discounts);
		this.ordAmtCharge = _sumDC(tmpBf, charges);
		
		this.ordAmtNet = 
			(this.getOrdAmtBf() 
			+ this.getOrdAmtTax() 
			+ this.getDeliFee() 
			+ this.getOrdAmtCharge()
			) - this.getOrdAmtDisc();
	}

	// customer information text
	public String getCstTxt() {
		if (OrderType.DineIn == this.getOrdType()) {
			return String.format("Table: %s%s", 
				this.getTblName(), 
				this.getCstName().isEmpty() ? "": String.format(" (%s)", this.getCstName())
			);
		} else if (OrderType.ToGo == this.getOrdType()) {
			if (ToGoType.Delivery == this.getOrdToGoType()) {
				return String.format("[%s] %s/%s Address: %s", 
					this.getOrdToGoType(),
					this.getCstName(),
					ResUtil.formatPhone(this.getCstPhone()),
					this.getAddrTxt()
				);
			} else {
				return String.format("[%s] %s/%s", 
					this.getOrdToGoType(),
					this.getCstName(),
					ResUtil.formatPhone(this.getCstPhone())
				);
			}
		}
		return "error: invalid order-type";
	}

	// customer information text (short)
	public String getCstTxtShort() {
		if (OrderType.DineIn == this.getOrdType()) {
			return String.format("Table: %s",this.getTblName());
		}
		return this.getCstName();
	}

	public String getCstHTML() {
		if (OrderType.DineIn == this.ordType) {
			String txt1 = String.format("DineIn #%d Table %s", 
				this.ordNo, this.tblName);
			if (!this.cstName.isEmpty()) {
				return String.format("<html>%s<br>%s</html>", txt1, this.cstName);
			}
			return txt1;
		}
		// ToGo
		return String.format("<html>ToGo:%s #%d<br>%s %s</html>", 
			this.ordToGoType.toString(),
			this.ordNo, 
			this.cstName, 
			(this.cstPhone.isEmpty() ? "" : this.cstPhone)
		);
	}
	
	public String getAddrTxt() {
		if (this.getDeliAddr1().isEmpty()) {
			return "-";
		}
		String addrTxt = String.format("%s%s%s, %s %s",
			this.getDeliAddr1(),
			this.getDeliUnitNo().isEmpty() ? "" : String.format(" APT. %s", this.getDeliUnitNo()),
			this.getDeliAddr2().isEmpty() ? "" : String.format(", %s", this.getDeliAddr2()),
			this.getDeliCity(),
			this.getDeliState()
			);
		return addrTxt;
	}
	
	// item
	public void addItem(TbOrderItem _itm) {
		items.addElement(_itm);
	}
	public java.util.Vector<TbOrderItem> getItems() {
		return items;
	}
	
	// discount
	public void addDiscount(TxDC dc1) {
		if (null == discounts) {
			discounts = new java.util.Vector<>();
		}
		discounts.add(TbOrderDC.newInstance(dc1));
	}
	public java.util.Vector<TbOrderDC> getDiscounts() {
		return discounts;
	}
	
	// charge
	public void addCharge(TxDC dc1) {
		if (null == charges) {
			charges = new java.util.Vector<>();
		}
		charges.add(TbOrderDC.newInstance(dc1));
	}
	public java.util.Vector<TbOrderDC> getCharges() {
		return charges;
	}
	
	public java.util.Vector<TbPayment> getPayments() {
		return payments;
	}

	// customer part
	public void updateCustomerInfo(TbCust cst1) {
		this.setCstId(cst1.getCstId());
		this.setCstCode(cst1.getCstCode());
		this.setCstName(cst1.getCstName());
		this.setCstEmail(cst1.getCstEmail());
		this.setCstPhone(cst1.getCstPhone());
		this.setDeliAddr1(cst1.getCstAddr1());
		this.setDeliAddr2(cst1.getCstAddr2());
		this.setDeliUnitNo(cst1.getCstUnitNo());
		this.setDeliCity(cst1.getCstCity());
		this.setDeliState(cst1.getCstState());
		this.setDeliLat(cst1.getCstLat());
		this.setDeliLon(cst1.getCstLon());
		this.setCstNote(cst1.getCstNote());
	}

	public void updateDeliInfo(AddrUS chkAddr) {
		this.setDeliAddr1(chkAddr.getAddrLine1());
		this.setDeliAddr2(chkAddr.getAddrLine2());
		this.setDeliUnitNo(chkAddr.getAddrUnitNo());
		this.setDeliCity(chkAddr.getAddrCity());
		this.setDeliState(chkAddr.getAddrState());
		this.setDeliLat(chkAddr.getAddrLat());
		this.setDeliLon(chkAddr.getAddrLon());
		
		this.setDeliDist(ResUtil.getDistanceFromShop(chkAddr));
		System.out.printf("distance ... [%.2f]\n", this.getDeliDist());
		
		TxArea chkArea = ResUtil.getDeliArea(chkAddr);
		if (null == chkArea) {
			this.setDeliFee(0);
			return;
		}
		this.setDeliFee(chkArea.getAreaDeliFee());
	}
	
	public void save() throws SQLException {
		TbCust cst1 = TbCust.fromOrder(this);
		cst1.save();
		this.setCstId(cst1.getCstId());
		//
		boolean noItems = true;
		for (int i=0; i < items.size(); i++) {
			TbOrderItem odi1 = items.get(i);
			if (odi1.isActive()) {
				noItems = false;
				break;
			}
		}
		if (noItems) {
			// delete
			this.setOrdStatus(2);
		}
		//
		ResData.saveTbOrder(this);
		for (int i=0; i < items.size(); i++) {
			TbOrderItem odi1 = items.get(i);
			odi1.setOrdId(this.getOrdId());
			odi1.setOdiSeq(i+1);
			odi1.save();
		}
		ResData.clearTbOrderDC(this.getOrdId());
		for (TbOrderDC odc1 : discounts) {
			odc1.setOrdId(this.getOrdId());
			ResData.saveTbOrderDC(odc1);
		}
		for (TbOrderDC odc1 : charges) {
			odc1.setOrdId(this.getOrdId());
			ResData.saveTbOrderDC(odc1);
		}
	}
	
	// Setter+Getter
	
	public int getOrdId() {
		return ordId;
	}
	public void setOrdId(int value) {
		this.ordId = value;
	}
	
	public int getOrdNo() {
		return ordNo;
	}
	public void setOrdNo(int value) {
		this.ordNo = value;
	}
	
	public java.util.Date getOrdDt() {
		return ordDt;
	}
	public void setOrdDt(java.util.Date value) {
		this.ordDt = value;
	}
	
	public OrderType getOrdType() {
		return ordType;
	}
	private void setOrdType(OrderType value) {
		this.ordType = value;
	}
	
	public ToGoType getOrdToGoType() {
		return ordToGoType;
	}
	public void setOrdToGoType(ToGoType value) {
		this.ordToGoType = value;
	}
	
	// Table
	
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
	public int getPartySize() {
		return tblPartySize;
	}
	public void setPartySize(int value) {
		this.tblPartySize = value;
	}
	
	// Customer
	
	public int getCstId() {
		return cstId;
	}
	public void setCstId(int value) {
		this.cstId = value;
	}
	
	public String getCstCode() {
		return cstCode;
	}
	public void setCstCode(String value) {
		this.cstCode = value;
	}
	
	public String getCstName() {
		return cstName;
	}
	public void setCstName(String value) {
		this.cstName = value;
	}
	
	public String getCstPhone() {
		return cstPhone;
	}
	public void setCstPhone(String value) {
		this.cstPhone = value;
	}
	
	public String getCstEmail() {
		return cstEmail;
	}
	public void setCstEmail(String value) {
		this.cstEmail = value;
	}
	
	public String getCstNote() {
		return cstNote;
	}
	public void setCstNote(String value) {
		this.cstNote = value;
	}
	
	public String getDeliAddr1() {
		return deliAddr1;
	}
	public void setDeliAddr1(String value) {
		this.deliAddr1 = value;
	}
	
	public String getDeliAddr2() {
		return deliAddr2;
	}
	public void setDeliAddr2(String value) {
		this.deliAddr2 = value;
	}
	
	public String getDeliUnitNo() {
		return deliUnitNo;
	}
	public void setDeliUnitNo(String value) {
		this.deliUnitNo = value;
	}
	
	public String getDeliCity() {
		return deliCity;
	}
	public void setDeliCity(String value) {
		this.deliCity = value;
	}
	
	public String getDeliState() {
		return deliState;
	}
	public void setDeliState(String value) {
		this.deliState = value;
	}
	
	public double getDeliLat() {
		return deliLat;
	}
	public void setDeliLat(double value) {
		this.deliLat = value;
	}
	
	public double getDeliLon() {
		return deliLon;
	}
	public void setDeliLon(double value) {
		this.deliLon = value;
	}
	
	public double getDeliDist() {
		return deliDist;
	}
	public void setDeliDist(double value) {
		this.deliDist = value;
	}
	
	public double getDeliFee() {
		if (OrderType.ToGo != this.getOrdType()) {
			return 0;
		}
		if (ToGoType.Delivery != this.getOrdToGoType()) {
			return 0;
		}
		return this.deliFee;
	}
	public void setDeliFee(double deliFee) {
		this.deliFee = deliFee;
	}
	
	public int getOrdCntCheck() {
		return ordCntCheck;
	}
	public void setOrdCntCheck(int value) {
		this.ordCntCheck = value;
	}
	
	public int getOrdCntReceipt() {
		return ordCntReceipt;
	}
	public void setOrdCntReceipt(int value) {
		this.ordCntReceipt = value;
	}

	
	public double getOrdAmtBf() {
		return ordAmtBf;
	}
	public double getOrdAmtTax() {
		return ordAmtTax;
	}
	public double getOrdAmtDisc() {
		return ordAmtDisc;
	}
	public double getOrdAmtCharge() {
		return ordAmtCharge;
	}
	public double getOrdAmtNet() {
		return ordAmtNet;
	}
	
	
	public boolean isPaid() {
		return ordPaid;
	}
	public void setOrdPaid(boolean value) {
		this.ordPaid = value;
	}
	
	public boolean isPick() {
		return ordPick;
	}
	public void setOrdPick(boolean value) {
		this.ordPick = value;
	}
	
	public int getOrdStatus() {
		return ordStatus;
	}
	public void setOrdStatus(int value) {
		this.ordStatus = value;
	}
	
	public double getOrdPmChange() {
		return this.ordPmChange;
	}
	public void setOrdPmChange(double value) {
		this.ordPmChange = value;
	}
	
	// *********************************

	public int getItemCount() {
		return items.size();
	}

	public String checkOrderInfo() {
		if (OrderType.DineIn == this.ordType) {
			return this.tblId > 0 && !this.tblName.isEmpty() ? 
				"Ok" : "Missing table information";
		}
		
		if (ToGoType.NA == this.ordToGoType) {
			return "Customer Information";
		}
		
		String errStr = "Ok";
		if (ToGoType.Delivery == this.ordToGoType) {
			if (this.deliAddr1.isEmpty() 
				|| (this.getDeliFee() <= 0)
				|| this.cstName.isEmpty()
				|| this.cstPhone.isEmpty()
			) {
				errStr = "Check Fee, Address, Name or Phone";
			}
		}
		if (ToGoType.PickUp == this.ordToGoType) {
			if (this.cstName.isEmpty()
				|| this.cstPhone.isEmpty()
			) {
				errStr = "Name, Phone";
			}
		}
		if (ToGoType.Waiting == this.ordToGoType) {
		}
		
		return errStr.toString();
	}

	public static TbOrder findOrderById(int _id) throws SQLException {
		return ResData.findOrderByOrdId(_id);
	}

	public static TbOrder findOrderByNo(int _id) throws SQLException {
		return ResData.findOrderByOrdNo(_id);
	}
	
	public static TbOrder[] findOrderByTableId(int _id) throws SQLException {
		return ResData.findOrderByTableId(_id);
	}
	
	public static TbOrder[] findOrderByPayAndType(
		boolean isPaid
		,OrderType _ordType
		,ToGoType _ToGoType
		,java.util.Date _frmDt
		,java.util.Date _toDt) 
			throws SQLException {
		return ResData.findOrderByPayAndType(isPaid, _ordType, _ToGoType, _frmDt, _toDt);
	}

	public void kitchenPrinted() throws SQLException {
		ResData.kitchenPrinted(this.ordId);
	}

	public void reopen() throws SQLException {
		this.ordCntCheck = 0;
		ResData.reopen(this.ordId);
	}

	public void check() throws SQLException {
		this.ordCntCheck = 1;
		ResData.orderCheck(this.ordId);
	}

	public void paid(double amtChange) throws SQLException {
		this.ordPaid = true;
		this.ordPmChange = amtChange;
		ResData.updateOrderIsPaid(this.ordId, this.ordPaid, amtChange);
		if (this.tblId > 0) {
			if (TxSctTable.tableIsPaid(this.tblId)) {
				TxSctTable.updateServiceStatus(this.tblId, TableService.Paid);
			}
		}
	}

	public void unpaid() throws SQLException {
		this.ordPaid = false;
		this.ordPmChange = 0;
		ResData.updateOrderIsPaid(this.ordId, this.ordPaid, 0);
		if (this.tblId > 0) {
			TxSctTable.lockTable(this.tblId, false);
			TxSctTable.updateServiceStatus(this.tblId, TableService.Checked);
		}
	}
	
	public static TbOrder getTbOrderByOrdNo(int ordNo) throws SQLException {
		return ResData.getTbOrderByOrdNo(ordNo);
	}
	
	public static TbOrder[] getTbOrderByTblId_Checked(int tblId) throws SQLException {
		return ResData.getTbOrderByTblIdChecked(tblId);
	}

	public static TbOrder[] getTbOrderWFP() throws SQLException {
		return ResData.getTbOrderWFP();
	}

	public static TbOrder[] getPaidOrders(java.util.Date dt1, java.util.Date dt2) 
			throws SQLException {
		return ResData.getPaidOrders(dt1, dt2);
	}

	public void picked() throws SQLException {
		ResData.updateOrderIsPicked(this.ordId, true);
	}

	public boolean isActive() {
		return this.ordStatus == 1;
	}
	
	public boolean isToGo() {
		return OrderType.ToGo == this.getOrdType();
	}

	public void delete() throws SQLException {
		ResData.deleteTbOrder(this.getOrdId());
	}

	public boolean isChecked() {
		return this.getOrdCntCheck() > 0;
	}
}
