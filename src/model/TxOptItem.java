package model;

import java.sql.ResultSet;
import java.sql.SQLException;

import resrc.ResData;

public class TxOptItem implements Comparable<TxOptItem> {

	private int optId;
	private int optSeq;
	private int opiId;
	private int opiSeq;
	private String opiNamePos;
	private String opiNameWeb;
	private boolean opiDefault;
	private double opiPrice;
	private int opiStatus;
	{
		optId = 0;
		optSeq = 0;
		opiId = 0;
		opiSeq = 0;
		opiNamePos = "";
		opiNameWeb = "";
		opiDefault = false;
		opiPrice = 0;
		opiStatus = 1;
	}
	
	// Constructor
	
	private TxOptItem(){}

	private TxOptItem(ResultSet rs) throws SQLException {
		optId = rs.getInt("opt_id");
		optSeq = rs.getInt("opt_seq");
		opiId = rs.getInt("opi_id");
		opiSeq = rs.getInt("opi_seq");
		opiNamePos = rs.getString("opi_name_pos");
		opiNameWeb = rs.getString("opi_name_web");
		opiDefault = rs.getBoolean("opi_default");
		opiPrice = rs.getDouble("opi_price");
		opiStatus = rs.getInt("opi_status");
	}
	
	// Factory

	public static TxOptItem newInstance() {
		return new TxOptItem();
	}
	
	public static TxOptItem fromDb(ResultSet rs) throws SQLException {
		return new TxOptItem(rs);
	}
	
	// Miscellaneous
	
	public String toString() {
		return String.format(
			"%s %s [%s] %s",
			this.opiDefault ? ">": " ",
			this.opiNamePos,
			this.opiNameWeb,
			this.opiPrice != 0 ? String.format("$%.2f", this.opiPrice) : "");
	}

	@Override
	public int compareTo(TxOptItem obj) {
		if (this.optId != obj.optId) {
			return this.optSeq - obj.optSeq;
		}
		return this.opiSeq - obj.opiSeq;
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) return false;
		if (obj instanceof TxOptItem) {
			TxOptItem opi2 = (TxOptItem)obj;
			return this.getOptId()==opi2.getOptId() && 
				this.getOpiId()==opi2.getOpiId();
		}
		return false;
	}

	// for ticket
	public String getDispStr() {
		if (this.getOpiPrice() > 0) {
			return String.format("%s($%.2f)", 
				this.getOpiNamePos(), this.getOpiPrice());
		}
		return this.getOpiNamePos();
	}
	
	// Setter+Getter

	public int getOptId() {
		return optId;
	}

	public void setOptId(int optId) {
		this.optId = optId;
	}

	public int getOptSeq() {
		return optSeq;
	}

	public void setOptSeq(int optSeq) {
		this.optSeq = optSeq;
	}

	public int getOpiId() {
		return opiId;
	}

	public void setOpiId(int opiId) {
		this.opiId = opiId;
	}

	public int getOpiSeq() {
		return opiSeq;
	}

	public void setOpiSeq(int opiSeq) {
		this.opiSeq = opiSeq;
	}

	public String getOpiNamePos() {
		return opiNamePos;
	}

	public void setOpiNamePos(String value) {
		this.opiNamePos = value;
	}

	public String getOpiNameWeb() {
		return opiNameWeb;
	}

	public void setOpiNameWeb(String value) {
		this.opiNameWeb = value;
	}

	public boolean isOpiDefault() {
		return opiDefault;
	}

	public void setOpiDefault(boolean opiDefault) {
		this.opiDefault = opiDefault;
	}

	public double getOpiPrice() {
		return opiPrice;
	}

	public void setOpiPrice(double opiPrice) {
		this.opiPrice = opiPrice;
	}

	public int getOpiStatus() {
		return opiStatus;
	}

	public void setOpiStatus(int opiStatus) {
		this.opiStatus = opiStatus;
	}

	public void save() throws SQLException {
		ResData.saveTxOptItem(this);
	}
}
