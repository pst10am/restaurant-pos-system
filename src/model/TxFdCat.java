package model;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import resrc.ResData;

public class TxFdCat {
	
	private int catId;
	private int catSeq;
	private String catNamePos;
	private String catNameWeb;
	private String catDesc;
	private Color catBgColor;
	private int catStatus;
	{
		catId = 0;
		catSeq = 0;
		catNamePos = "";
		catNameWeb = "";
		catDesc = "";
		catBgColor = Color.decode("#C2E0E0");
		catStatus = 1;
	}
	
	// Constructor

	private TxFdCat() {}
	
	private TxFdCat(ResultSet rs1) throws SQLException {
		catId = rs1.getInt("cat_id");
		catSeq = rs1.getInt("cat_seq");
		catNamePos = rs1.getString("cat_name_pos");
		catNameWeb = rs1.getString("cat_name_web");
		catDesc = rs1.getString("cat_desc");
		catBgColor = Color.decode(rs1.getString("cat_bg_color").trim());
		catStatus = rs1.getInt("cat_status");
	}
	
	// Factory
	
	public TxFdCat newObj() {
		return new TxFdCat();
	}
	
	public static TxFdCat fromDb(ResultSet rs) throws SQLException {
		return new TxFdCat(rs);
	}
	
	@Override
	public String toString() {
		return String.format("%s%s", this.catStatus==1 ? "":"[x] ", catNamePos);
	}
	
	// Setter+Getter

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	public int getCatSeq() {
		return catSeq;
	}

	public void setCatSeq(int catSeq) {
		this.catSeq = catSeq;
	}

	public String getCatNamePos() {
		return catNamePos;
	}

	public void setCatNamePos(String catName1) {
		this.catNamePos = catName1;
	}

	public String getCatNameWeb() {
		return catNameWeb;
	}

	public void setCatNameWeb(String catName2) {
		this.catNameWeb = catName2;
	}

	public String getCatDesc() {
		return catDesc;
	}

	public void setCatDesc(String catDesc) {
		this.catDesc = catDesc;
	}

	public Color getCatBgColor() {
		return catBgColor;
	}

	public void setCatBgColor(Color catBgColor) {
		this.catBgColor = catBgColor;
	}

	public int getCatStatus() {
		return catStatus;
	}

	public void setCatStatus(int catStatus) {
		this.catStatus = catStatus;
	}
	
	// -- 2/3/2015 --

	public static Vector<TxFdCat> getFoodCats() 
			throws SQLException {
		return ResData.getFoodCats(false);
	}

	public static Vector<TxFdCat> getFoodCatsWithDelete() 
			throws SQLException {
		return ResData.getFoodCats(true);
	}

	public void save() throws SQLException {
		ResData.saveTxCat(this);
	}

	public static TxFdCat newInstance() {
		return new TxFdCat();
	}

	public void delete() throws SQLException {
		ResData.deleteTxFdCat(this.catId, this.catStatus==1?2:1);
	}
}
