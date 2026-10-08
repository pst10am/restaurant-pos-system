package model;

import java.sql.ResultSet;
import java.sql.SQLException;

import refx.ConfigType;

public class TxCfgItem {

	private int grpId = 0;
	private int cfgId = 0;
	private String cfgName = "";
	private String cfgKey = "";
	private String cfgValue = "";
	private String cfgComment = "";
	private ConfigType cfgType = ConfigType.FREETEXT;
	private String cfgChoices = "";
	
	private TxCfgItem() {}
	
	public static TxCfgItem fromDb(ResultSet rs1) throws SQLException {
		TxCfgItem obj1 = new TxCfgItem();
		obj1.grpId = rs1.getInt("grp_id");
		obj1.cfgId = rs1.getInt("cfg_id");
		obj1.cfgName = rs1.getString("cfg_name");
		obj1.cfgKey = rs1.getString("cfg_key");
		obj1.cfgValue = rs1.getString("cfg_value");
		obj1.cfgComment = rs1.getString("cfg_comment");
		obj1.cfgType = ConfigType.valueOf(rs1.getString("cfg_type"));
		obj1.cfgChoices = rs1.getString("cfg_choices");
		return obj1;
	}
	
	// --
	
	public int getGrpId() {
		return grpId;
	}
	public void setGrpId(int val) {
		this.grpId = val;
	}

	public int getCfgId() {
		return cfgId;
	}
	public void setCfgId(int val) {
		this.cfgId = val;
	}

	public String getCfgName() {
		return cfgName;
	}
	public void setCfgName(String val) {
		this.cfgName = val;
	}

	public String getCfgKey() {
		return cfgKey;
	}
	public void setCfgKey(String val) {
		this.cfgKey = val;
	}

	public String getCfgValue() {
		return cfgValue;
	}
	public void setCfgValue(String val) {
		this.cfgValue = val;
	}

	public String getCfgComment() {
		return cfgComment;
	}
	public void setCfgComment(String val) {
		this.cfgComment = val;
	}

	public ConfigType getCfgType() {
		return cfgType;
	}
	public void setCfgType(ConfigType val) {
		this.cfgType = val;
	}

	public String getCfgChoices() {
		return cfgChoices;
	}
	public void setCfgChoices(String val) {
		this.cfgChoices = val;
	}
}
