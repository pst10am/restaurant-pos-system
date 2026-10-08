package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import resrc.ResData;
import resrc.ResUtil;

public class FindCustInfo {

	private int cstId = 0;
	private String cstCode = "";
	private String cstName = "";
	private String cstPhone = "";
	private String cstAddr1 = "";
	private String cstAddr2 = "";
	private String cstUnitNo = "";
	private String cstCity = "";
	private String cstState = "";
	
	
	// Constructor
	
	private FindCustInfo() {}
	
	private FindCustInfo(ResultSet rs1) throws SQLException {
		cstId = rs1.getInt("cst_id");
		cstCode = rs1.getString("cst_code");
		cstName = rs1.getString("cst_name");
		cstPhone = rs1.getString("cst_phone");
		cstAddr1 = rs1.getString("cst_addr_1");
		cstAddr2 = rs1.getString("cst_addr_2");
		cstUnitNo = rs1.getString("cst_unit_no");
		cstCity = rs1.getString("cst_city");
		cstState = rs1.getString("cst_state");
	}
	
	// Factory
	
	public static FindCustInfo fromDb(ResultSet rs1) throws SQLException {
		return new FindCustInfo(rs1);
	}

	public static FindCustInfo newInstance(TbCust cst1) {
		FindCustInfo fci1 = new FindCustInfo();
		fci1.cstId = cst1.getCstId();
		fci1.cstCode = cst1.getCstCode();
		fci1.cstName = cst1.getCstName();
		fci1.cstPhone = cst1.getCstPhone();
		fci1.cstAddr1 = cst1.getCstAddr1();
		fci1.cstAddr2 = cst1.getCstAddr2();
		fci1.cstUnitNo = cst1.getCstUnitNo();
		fci1.cstCity = cst1.getCstCity();
		fci1.cstState = cst1.getCstState();
		return fci1;
	}
	
	// Miscellaneous
	
	public String toString() {
		String addr1 = "";
		if (!cstAddr1.isEmpty()) {
			addr1 = String.format(" / %s", cstAddr1);
		}
		return String.format("%s / %s%s", 
			ResUtil.formatPhone(cstPhone), 
			cstName.toUpperCase(), 
			addr1);
	}

	// Setter+Getter
	
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
	
	public String getCstAddr1() {
		return cstAddr1;
	}
	public void setCstAddr1(String value) {
		this.cstAddr1 = value;
	}
	
	public String getCstAddr2() {
		return cstAddr2;
	}
	public void setCstAddr2(String value) {
		this.cstAddr2 = value;
	}
	
	public String getCstUnitNo() {
		return cstUnitNo;
	}
	public void setCstUnitNo(String value) {
		this.cstUnitNo = value;
	}
	
	public String getCstCity() {
		return cstCity;
	}
	public void setCstCity(String value) {
		this.cstCity = value;
	}
	
	public String getCstState() {
		return cstState;
	}
	public void setCstState(String value) {
		this.cstState = value;
	}

	public static Vector<FindCustInfo> searchPhone(String raw) {
		return ResData.searchCustByPhone(raw);
	}
}
