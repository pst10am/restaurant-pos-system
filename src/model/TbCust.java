package model;

import java.sql.ResultSet;
import java.sql.SQLException;

import resrc.ResData;

public class TbCust {

	private int cstId = 0;
	private String cstCode = "";
	private String cstName = "";
	private String cstEmail = "";
	private String cstPhone = "";
	private String cstAddr1 = "";
	private String cstAddr2 = "";
	private String cstUnitNo = "";
	private String cstCity = "";
	private String cstState = "";
	private double cstLat = 0;
	private double cstLon = 0;
	private String cstNote = "";
	private int cstStatus = 1;
	
	// Constructor
	
	private TbCust() {}
	
	private TbCust(ResultSet rs1) throws SQLException {
		cstId = rs1.getInt("cst_id");
		cstCode = rs1.getString("cst_code");
		cstName = rs1.getString("cst_name");
		cstEmail = rs1.getString("cst_email");
		cstPhone = rs1.getString("cst_phone");
		cstAddr1 = rs1.getString("cst_addr_1");
		cstAddr2 = rs1.getString("cst_addr_2");
		cstUnitNo = rs1.getString("cst_unit_no");
		cstCity = rs1.getString("cst_city");
		cstState = rs1.getString("cst_state");
		cstLat = rs1.getDouble("cst_lat");
		cstLon = rs1.getDouble("cst_lon");
		cstNote = rs1.getString("cst_note");
		cstStatus = rs1.getInt("cst_status");
	}
	
	// Factory
	
	public static TbCust newInstance() {
		return new TbCust();
	}
	
	public static TbCust fromDb(ResultSet rs1) throws SQLException {
		return new TbCust(rs1);
	}

	public static TbCust fromOrder(TbOrder ord1) {
		TbCust cst1 = new TbCust();
		cst1.cstId = ord1.getCstId();
		cst1.cstCode = ord1.getCstCode();
		cst1.cstName = ord1.getCstName();
		cst1.cstEmail = ord1.getCstEmail();
		cst1.cstPhone = ord1.getCstPhone();
		cst1.cstAddr1 = ord1.getDeliAddr1();
		cst1.cstAddr2 = ord1.getDeliAddr2();
		cst1.cstUnitNo = ord1.getDeliUnitNo();
		cst1.cstCity = ord1.getDeliCity();
		cst1.cstState = ord1.getDeliState();
		cst1.cstLat = ord1.getDeliLat();
		cst1.cstLon = ord1.getDeliLon();
		cst1.cstNote = ord1.getCstNote();
		cst1.cstStatus = 1;
		return cst1;
	}
	
	// Miscellaneous

	public static TbCust findCustomerByCode(String _code) {
		return ResData.findCustomerByCode(_code);
	}
	
	// Setter+Getter

	// cst_id
	public int getCstId() {
		return cstId;
	}
	public void setCstId(int value) {
		this.cstId = value;
	}

	// cst_code
	public String getCstCode() {
		return cstCode;
	}
	public void setCstCode(String value) {
		this.cstCode = value;
	}

	// cst_name
	public String getCstName() {
		return cstName;
	}
	public void setCstName(String value) {
		this.cstName = value;
	}

	// cst_email
	public String getCstEmail() {
		return cstEmail;
	}
	public void setCstEmail(String value) {
		this.cstEmail = value;
	}

	// cst_phone
	public String getCstPhone() {
		return cstPhone;
	}
	public void setCstPhone(String value) {
		this.cstPhone = value;
	}

	// cst_addr_1
	public String getCstAddr1() {
		return cstAddr1;
	}
	public void setCstAddr1(String value) {
		this.cstAddr1 = value;
	}

	// cst_addr_2
	public String getCstAddr2() {
		return cstAddr2;
	}
	public void setCstAddr2(String value) {
		this.cstAddr2 = value;
	}

	// cst_unit_no
	public String getCstUnitNo() {
		return cstUnitNo;
	}
	public void setCstUnitNo(String value) {
		this.cstUnitNo = value;
	}

	// cst_city
	public String getCstCity() {
		return cstCity;
	}
	public void setCstCity(String value) {
		this.cstCity = value;
	}

	// cst_state
	public String getCstState() {
		return cstState;
	}
	public void setCstState(String value) {
		this.cstState = value;
	}

	// cst_lat
	public double getCstLat() {
		return cstLat;
	}
	public void setCstLat(double value) {
		this.cstLat = value;
	}

	// cst_lon
	public double getCstLon() {
		return cstLon;
	}
	public void setCstLon(double value) {
		this.cstLon = value;
	}

	// cst_note
	public String getCstNote() {
		return cstNote;
	}
	public void setCstNote(String value) {
		this.cstNote = value;
	}

	// cst_status
	public int getCstStatus() {
		return cstStatus;
	}
	public void setCstStatus(int value) {
		this.cstStatus = value;
	}

	public static TbCust getCustById(int _id) {
		return ResData.getCustById(_id);	
	}

	public void save() throws SQLException {
		if (this.getCstPhone().isEmpty()) {
			return;
		}
		if (this.getCstName().isEmpty()) {
			return;
		}
		ResData.saveTbCust(this);
	}
}
