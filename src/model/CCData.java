package model;

import java.util.regex.Pattern;

public class CCData {
	
	private String trk1 = "";
	private String trk2 = "";
	private String acctNo = "";
	private String acctName = "";
	private String expMonth = "";
	private String expYear = "";
	private String acctCVV = "";
	
	private CCData(String _trk1, String _trk2) {
		trk1 = _trk1;
		trk2 = _trk2;
		
		if (trk1.isEmpty()) return;
		if (trk2.isEmpty()) return;
		
		// Track 1
		
		String[] trk1s = _trk1.substring(1, _trk1.length()-1).split(Pattern.quote("^"));
		for (int i=0; i < trk1s.length; i++) {
			String t1 = trk1s[i].trim();
			if (i == 0) {
				acctNo = t1;
			}
			if (i == 1) {
				acctName = t1;
			}
			if (i == 2) {
				expYear = t1.substring(0, 2);
				expMonth = t1.substring(2, 4);
			}
		}

		if (!trk2.isEmpty()) {
			String[] trk2s = _trk2.substring(1, _trk2.length()-1).split("=");
			for (int i=0; i < trk2s.length; i++) {
				String t2 = trk2s[i].trim();
				if (i == 0) {
					if (!acctNo.endsWith(t2)) {
						acctNo = "";
					}
				}
				if (i == 1) {
					if (!t2.startsWith(String.format("%s%s", expYear, expMonth))) {
						expMonth = "";
						expYear = "";
					}
				}
			}
		}
		
		if ('B' == acctNo.charAt(0)) {
			acctNo = acctNo.substring(1);
		}
		
		if (expYear.length() == 2) {
			expYear = "20"+ expYear;
		}
	}
	
	// Constructor
	
	public static CCData newCard(String _trk1, String _trk2) {
		CCData cdt1 = new CCData(_trk1, _trk2);
		return cdt1;
	}
	
	public static CCData newCard() {
		CCData cdt1 = new CCData("", "");
		return cdt1;
	}
	
	public static CCData fromObject(CCData _obj) {
		CCData cdt1 = new CCData(_obj.getTrk1(), _obj.getTrk2());
		cdt1.setAcctName(_obj.getAcctName());
		cdt1.setAcctNo(_obj.getAcctNo());
		cdt1.setExpMonth(_obj.getExpMonth());
		cdt1.setExpYear(_obj.getExpYear());
		return cdt1;
	}
	
	// Miscellaneous
	
	public void clearData() {
		trk1 = "";
		trk2 = "";
		acctNo = "";
		acctName = "";
		expMonth = "";
		expYear = "";
	}
	
	public boolean isValid() {
		return !acctNo.isEmpty() && 
			!expMonth.isEmpty() && !expYear.isEmpty();
	}

	public String last4digits() {
		return acctNo.substring(acctNo.length()-4);
	}

	// getter
	
	public String getCVV() {
		return acctCVV;
	}
	public void setCVV(String val1) {
		acctCVV = val1;
	}

	public String getTrk1() {
		return trk1;
	}

	public String getTrk2() {
		return trk2;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public String getAcctName() {
		return acctName;
	}

	public String getExpMonth() {
		return expMonth;
	}

	public String getExpYear() {
		return expYear;
	}
	
	// setter

	public void setTrk1(String val) {
		this.trk1 = val.trim();
	}
	
	public void setTrk2(String val) {
		this.trk2 = val.trim();
	}
	
	public void setAcctNo(String val) {
		this.acctNo = val.trim();
	}
	
	public void setAcctName(String val) {
		this.acctName = val.trim();
	}

	public void setExpMonth(String val) {
		this.expMonth = val.trim();
	}

	public void setExpYear(String val) {
		this.expYear = val.trim();
	}

	public boolean isCardSwiped() {
		return !(trk1.isEmpty() && trk2.isEmpty());
	}

	public String getExpMMYY() {
		return String.format("%2s%2s", 
			this.expMonth, this.expYear);
	}
}
