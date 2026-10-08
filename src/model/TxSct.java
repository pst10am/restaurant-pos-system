package model;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import resrc.ResData;
import resrc.ResUtil;

public class TxSct {

	private int sctId;
	private int sctSeq;
	private String sctName;
	private String sctBgName;
	private Color sctBgColor;
	private int sctWidth;
	private int sctHeight;
	private int sctStatus;
	{
		sctId = 0;
		sctSeq = 0;
		sctName = "";
		sctBgName = "";
		sctBgColor = Color.GRAY;
		sctWidth = 1024;
		sctHeight = 768;
		sctStatus = 1;
	}
	
	// Constructor
	
	private TxSct() {}
	
	private TxSct(ResultSet rs1) throws SQLException {
		sctId = rs1.getInt("sct_id");
		sctSeq = rs1.getInt("sct_seq");
		sctName = rs1.getString("sct_name");
		sctBgName = rs1.getString("sct_bg_name");
		sctBgColor = Color.decode(rs1.getString("sct_bg_color"));
		sctWidth= rs1.getInt("sct_width");
		sctHeight = rs1.getInt("sct_height");
		sctStatus= rs1.getInt("sct_status");
	}
	
	// Factory
	
	public static TxSct newInstance() {
		return new TxSct();
	}
	
	public static TxSct fromDb(ResultSet rs1) throws SQLException {
		return new TxSct(rs1);
	}
	
	// Miscellaneous
	
	@Override
	public String toString() {
		String delTxt = "";
		if (sctStatus != 1) {
			delTxt = "[x] ";
		}
		return String.format("%s%s", delTxt, sctName);
	}
	
	// Setter+Getter

	public int getSctId() {
		return sctId;
	}

	public void setSctId(int sctId) {
		this.sctId = sctId;
	}

	public int getSctSeq() {
		return sctSeq;
	}

	public void setSctSeq(int sctSeq) {
		this.sctSeq = sctSeq;
	}

	public String getSctName() {
		return sctName;
	}

	public void setSctName(String sctName) {
		this.sctName = sctName;
	}

	public String getSctBgName() {
		return sctBgName;
	}
	
	public void setSctBgColor(Color color1) {
		this.sctBgColor = color1;
	}
	
	public Color getSctBgColor() {
		return this.sctBgColor;
	}

	public void setSctBgName(String sctBgName) {
		this.sctBgName = sctBgName;
	}

	public int getSctWidth() {
		return sctWidth;
	}

	public void setSctWidth(int sctWidth) {
		this.sctWidth = sctWidth;
	}

	public int getSctHeight() {
		return sctHeight;
	}

	public void setSctHeight(int sctHeight) {
		this.sctHeight = sctHeight;
	}

	public int getSctStatus() {
		return sctStatus;
	}

	public void setSctStatus(int sctStatus) {
		this.sctStatus = sctStatus;
	}
	
	//

	public static Vector<TxSct> getSections() 
			throws SQLException {
		return ResData.getSections(false);
	}

	public static Vector<TxSct> getSectionsWithDelete() 
			throws SQLException {
		return ResData.getSections(true);
	}

	public void delete() throws SQLException {
		ResData.deleteTxSct(this.getSctId(), sctStatus==1);
	}

	public void save() throws SQLException {
		ResData.saveTxSct(this);
	}

	public String getSctBgColorHexStr() {
		return ResUtil.toHexStr(this.sctBgColor);
	}
}
