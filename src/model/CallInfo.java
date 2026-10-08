package model;

import resrc.CIDRecord;

public class CallInfo {
	
	public int line = 0;
	public String phone = "";
	public String name = "";
	
	public CallInfo(CIDRecord cid1) {
		line = cid1.line;
		phone = cid1.phone;
		name = cid1.name;
	}

	public void update(CIDRecord cid1) {
		line = cid1.line;
		phone = cid1.phone;
		name = cid1.name;
	}

	public String getRawPhoneNo() {
		return phone.replaceAll("-", "");
	}
}
