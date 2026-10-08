package model;

public class AddrUS {
	
	private String addrLine1 = "";
	private String addrLine2 = "";
	private String addrUnitNo = "";
	private String addrCity = "";
	private String addrState = "";
	private double addrLat = 0;
	private double addrLon = 0;
	
	// Constructor
	
	private AddrUS() {}
		
	// Factory
	
	public static AddrUS newInstance() {
		AddrUS addr1 = new AddrUS();
		return addr1;
	}
	
	public static AddrUS newInstance(
		String _line1, String _unitNo, 
		String _line2, 
		String _city, String _state) {
		
		AddrUS addr1 = new AddrUS();
		addr1.setAddrLine1(_line1);
		addr1.setAddrUnitNo(_unitNo);
		addr1.setAddrLine2(_line2);
		addr1.setAddrCity(_city);
		addr1.setAddrState(_state);
		
		return addr1;
	}
	
	public static AddrUS fromOrder(TbOrder ord1) {
		AddrUS addr1 = new AddrUS();
		addr1.addrLine1 = ord1.getDeliAddr1();
		addr1.addrLine2 = ord1.getDeliAddr2();
		addr1.addrUnitNo = ord1.getDeliUnitNo();
		addr1.addrCity = ord1.getDeliCity();
		addr1.addrState = ord1.getDeliState();
		addr1.addrLat = ord1.getDeliLat();
		addr1.addrLon = ord1.getDeliLon();
		return addr1;
	}
	
	// Miscellaneous
	
	private static String toStr(AddrUS addr1, boolean withUnitNo) {
		if (addr1.addrLine1.trim().isEmpty()) return "";
		//
		StringBuilder bld1 = new StringBuilder();
		bld1.append(addr1.addrLine1.trim());
		if (withUnitNo && !addr1.addrUnitNo.isEmpty()) {
			bld1.append(String.format(" APT. %s", addr1.addrUnitNo));
		}
		if (!addr1.addrLine2.isEmpty()) {
			bld1.append(String.format(", %s", addr1.addrLine2.trim()));
		}
		bld1.append(String.format(", %s, %s", addr1.addrCity, addr1.addrState));
		//
		return bld1.toString();
	}
	
	public String toStringWithUnit() {
		return AddrUS.toStr(this, true);
	}
	
	public String toStringNoUnit() {
		return AddrUS.toStr(this, false);
	}

	public String toGoogleAddrString() {
		if (addrLine1.isEmpty()) return "";
		String _addr1 = String.format("%s, %s %s", addrLine1, addrCity, addrState);
		return _addr1.replace(' ', '+').replace("#", "%23");
	}	

	public boolean isValid() {
		return !addrLine1.isEmpty();
	}
	
	public boolean hasLatLon() {
		return addrLat != 0 && addrLon != 0;
	}
	
	// Setter+Getter

	public String getAddrLine1() {
		return addrLine1;
	}

	public void setAddrLine1(String value) {
		this.addrLine1 = value;
	}

	public String getAddrLine2() {
		return addrLine2;
	}

	public void setAddrLine2(String value) {
		this.addrLine2 = value;
	}

	public String getAddrUnitNo() {
		return addrUnitNo;
	}

	public void setAddrUnitNo(String value) {
		this.addrUnitNo = value;
	}

	public String getAddrCity() {
		return addrCity;
	}

	public void setAddrCity(String value) {
		this.addrCity = value;
	}

	public String getAddrState() {
		return addrState;
	}

	public void setAddrState(String value) {
		this.addrState = value;
	}

	public double getAddrLat() {
		return addrLat;
	}

	public void setAddrLat(double addrLat) {
		this.addrLat = addrLat;
	}

	public double getAddrLon() {
		return addrLon;
	}

	public void setAddrLon(double addrLon) {
		this.addrLon = addrLon;
	}

	public boolean isEmpty() {
		return this.addrLine1.isEmpty() && this.addrLine2.isEmpty();
	}
}
