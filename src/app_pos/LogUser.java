package app_pos;

import model.TxUser;

public class LogUser {
	private LogUser() {}
	
	private static TxUser user;
	
	void setUser(TxUser _user) {
		user = _user;
	}
	
	TxUser getUser() {
		return user;
	}
}
