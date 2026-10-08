package model;

import java.sql.ResultSet;
import java.sql.SQLException;

import refx.UserGroup;
import resrc.ResData;

public class TxUser {
	
	private int usrId = 0;
	private String usrName = "";
	private String usrPosPWD = "";
	private UserGroup usrGrp = UserGroup.Waiter;
	private boolean isActive = false;
	
	// Constructor
	
	private TxUser() {}
	
	private TxUser(ResultSet rs1) throws SQLException {
		usrId = rs1.getInt("usr_id");
		usrName = rs1.getString("usr_name");
		usrPosPWD = rs1.getString("usr_pos_pwd");
		usrGrp = UserGroup.valueOf(rs1.getString("usr_grp"));
		isActive = rs1.getBoolean("is_active");
	}
	
	// Factory
	
	public static TxUser newInstance() {
		TxUser usr1 = new TxUser();
		usr1.isActive = true;
		return usr1;
	}
	
	public static TxUser fromDb(ResultSet rs1) throws SQLException {
		return new TxUser(rs1);
	}
	
	// Miscellaneous
	
	public static TxUser authen(String pwd) {
		return ResData.findUserByPOSPwd(pwd);
	}
	
	public String toString() {
		return String.format("%s%s", isActive?"":"[x] ", usrName);
	}
	
	// Setter+Getter

	public int getUsrId() {
		return usrId;
	}
	public void setUsrId(int value) {
		this.usrId = value;
	}

	public String getUsrName() {
		return usrName;
	}
	public void setUsrName(String value) {
		this.usrName = value;
	}

	public String getUsrPosPWD() {
		return usrPosPWD;
	}
	public void setUsrPosPWD(String value) {
		this.usrPosPWD = value;
	}

	public UserGroup getUsrGrp() {
		return usrGrp;
	}
	public void setUsrGrp(UserGroup value) {
		this.usrGrp = value;
	}

	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean value) {
		this.isActive = value;
	}

	public static TxUser[] getAllUserWithDelete() throws SQLException {
		return ResData.getAllUser(true);
	}

	public void delete() throws SQLException {
		ResData.deleteTxUser(this.usrId, !isActive);
	}

	public void save() throws SQLException {
		ResData.saveTxUser(this);
	}
}
