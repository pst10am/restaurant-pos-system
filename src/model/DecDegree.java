package model;

public class DecDegree {
	
	private double lat = 0;
	private double lon = 0;

	private DecDegree() {}
	
	private DecDegree(double _lat, double _lon) {
		lat = _lat;
		lon = _lon;
	}
	
	// text input format = Lat, Lon 
	public static DecDegree fromText(String txt) {
		DecDegree dg1 = new DecDegree();
		String[] values = txt.split(",");
		dg1.lat = Double.parseDouble(values[0]);
		dg1.lon = Double.parseDouble(values[1]);
		return dg1;
	}
	
	public static DecDegree fromAddr(AddrUS addr1) {
		DecDegree dg1 = new DecDegree();
		dg1.lat = addr1.getAddrLat();
		dg1.lon = addr1.getAddrLon();
		return dg1;
	}
	
	public static DecDegree newInstance(double _lat, double _lon) {
		return new DecDegree(_lat, _lon);
	}
	
	public double lat() {
		return lat;
	}
	public void setLat(double val) {
		lat = val;
	}
	
	public double lon() {
		return lon;
	}
	public void setLon(double val) {
		lon = val;
	}
}
