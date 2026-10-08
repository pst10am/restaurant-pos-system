package resrc;

import java.awt.Color;
import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import refx.UnitType;
import model.AddrUS;
import model.DecDegree;
import model.TxArea;

public class ResUtil {
	private ResUtil() {
	}

	// -----private-----

	// http://www.movable-type.co.uk/scripts/latlong.html
	private static double haversineDistance(DecDegree p1, DecDegree p2) {
		double R = 6371; // earth's radius in km
		double lat1 = Math.toRadians(p1.lat());
		double lat2 = Math.toRadians(p2.lat());
		double delta_lat = Math.toRadians(p2.lat() - p1.lat());
		double delta_lon = Math.toRadians(p2.lon() - p1.lon());

		double a = Math.sin(delta_lat / 2) * Math.sin(delta_lat / 2)
				+ Math.cos(lat1) * Math.cos(lat2) * Math.sin(delta_lon / 2)
				* Math.sin(delta_lon / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return R * c; // km
	}

	private static boolean inArea(AddrUS tstAddr, TxArea deliArea) {
		boolean oddNodes = false;
		double dstx = tstAddr.getAddrLat();
		double dsty = tstAddr.getAddrLon();
		double[] xpnts = deliArea.x_vertices();
		double[] ypnts = deliArea.y_vertices();
		int j = xpnts.length - 1;
		for (int i = 0; i < xpnts.length; i++) {
			if ((ypnts[i] < dsty && ypnts[j] >= dsty || ypnts[j] < dsty
					&& ypnts[i] >= dsty)
					&& (xpnts[i] <= dstx || xpnts[j] <= dstx)) {
				oddNodes ^= (xpnts[i] + (dsty - ypnts[i])
						/ (ypnts[j] - ypnts[i]) * (xpnts[j] - xpnts[i]) < dstx);
			}
			j = i;
		}
		return oddNodes;
	}

	// -----public-----
	
	public static String dtoc(java.util.Date _date, String _fmt) {
		SimpleDateFormat sdf1 = new SimpleDateFormat(_fmt, Locale.US);
		return sdf1.format(_date);
	}
	
	public static java.util.Date ctod(String _dateStr, String _fmt) {
		SimpleDateFormat sdf1 = new SimpleDateFormat(_fmt, Locale.US);
		java.util.Date val = null;
		try {
			val = sdf1.parse(_dateStr);
		} catch (ParseException e) {
			val = null;
		}
		return val;
	}
	
	public static String formatText(String _fmt, String _raw) {
		StringBuilder bld1 = new StringBuilder();
		int cnt1 = -1;
		for (int i = 0; i < _fmt.length(); i++) {
			if (_fmt.charAt(i) == '-') {
				bld1.append('-');
				continue;
			}
			if (_fmt.charAt(i) == '_') {
				cnt1 += 1;
				if (cnt1 < _raw.length()) {
					bld1.append(_raw.charAt(cnt1));
					continue;
				} else {
					bld1.append('_');
				}
			}
		}
		return bld1.toString();
	}

	public static String formatPhone(String raw) {
		/*
		String fmt1 = "___-___-____";
		StringBuilder bld1 = new StringBuilder();
		int cnt1 = -1;
		for (int i = 0; i < fmt1.length(); i++) {
			if (fmt1.charAt(i) == '-') {
				bld1.append('-');
				continue;
			}
			if (fmt1.charAt(i) == '_') {
				cnt1 += 1;
				if (cnt1 < raw.length()) {
					bld1.append(raw.charAt(cnt1));
					continue;
				} else {
					bld1.append('_');
				}
			}
		}
		return bld1.toString();
		*/
		return formatText("___-___-____", raw);
	}

	public static String formatCCNo(String raw) {
		return formatText("____-____-____-____", raw);
	}

	public static String formatCVV(String raw) {
		return formatText("___", raw);
	}

	public static double getDistance(DecDegree p1, DecDegree p2, UnitType unt) {
		double dstKm = haversineDistance(p1, p2);
		if (UnitType.Metric == unt) {
			return dstKm;
		} else if (UnitType.US == unt) {
			return dstKm * 0.621371;
		}
		throw new IllegalArgumentException("invalid unit type.");
	}
	
	public static double getDistanceFromShop(AddrUS chkAddr) {
		return getDistance(ResCfg.getShopDecDegree(), DecDegree.fromAddr(chkAddr), ResCfg.defaultUnitType());
	}

	public static TxArea getDeliArea(AddrUS chkAddr) {
		if (!chkAddr.isValid() || !chkAddr.hasLatLon()) return null;
		/*
		System.out.printf("%s\n%.2f,%.2f distance = %.2f mi\n", 
			chkAddr.toStringWithUnit(), 
			chkAddr.getAddrLat(), 
			chkAddr.getAddrLon(),
			getDistanceFromShop(chkAddr));
		*/
		int idx = -1;
		TxArea[] _areas = ResData.deliAreas();
		for (int i=0; i < _areas.length; i++) {
			if (inArea(chkAddr, _areas[i])) {
				idx = i;
				break;
			}
		}
		if (idx == -1) return null;
		return _areas[idx];
	}

	public static double round(double value) {
		String val1 = String.format("%.2f", value);
		return Double.parseDouble(val1);
	}
	
	public static String padStr(int size, char padChr) {
		return String.format("%0"+ size +"d", 0).replace('0', padChr);
	}
	
	public static String padc(String str, int size, char padChr) {
		String _padStr = padStr(size, padChr);
		String str2 = String.format("%s%s%s", _padStr, str, _padStr);
		int cutPnt = (str2.length()/2) - (size/2);
		return str2.substring(cutPnt, cutPnt+size);
	}
	
	public static String padl(String str, int size, char padChr) {
		return String.format("%s%s", str, padStr(size, padChr)).substring(0, size);
	}
	
	public static String padr(String str, int size, char padChr) {
		String str2 = String.format("%s%s", padStr(size, padChr), str);
		return str2.substring(str2.length()-size);
	}
	
	public static String padc(String str, int size) {
		return padc(str, size, ' ');
	}
	
	public static String padl(String str, int size) {
		return padl(str, size, ' ');
	}
	
	public static String padr(String str, int size) {
		return padr(str, size, ' ');
	}

	public static URL getResUrl(String resName) {
		return ResUtil.class.getResource(resName);
	}

	public static java.util.Date dtadd(java.util.Date dtin, int i, int addUnit) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dtin);
		cal.add(addUnit, i);
		return cal.getTime();
	}
	
	public static String newLine2Space(String val) {
		String str1 = val.trim().replaceAll("\r", "");
		str1 = str1.replaceAll("\n", " ");
		//
		StringBuilder bld1 = new StringBuilder();
		boolean flg1 = false;
		for (int i=0; i < str1.length(); i++) {
			if (flg1 && (str1.charAt(i) == ' ')) {
				continue;
			}
			bld1.append(str1.charAt(i));
			flg1 = str1.charAt(i) == ' ';
		}
		return bld1.toString();
	}
	
	public static int daysOld(File f1) {
		return _dayDif(f1.lastModified(), new java.util.Date());
	}
	
	private static int _dayDif(long time1, java.util.Date toDt) {
		long time_dif = toDt.getTime() - time1;
		int day_dif = (int)(time_dif / (1000 * 60 * 60 * 24));
		return day_dif;
	}
	
	public static File[] getTmpFile(final int ord_id) {
		File tmpDir = new File("tmp");
		File[] tmp1s = tmpDir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pname) {
				return pname.getName().startsWith(String.format("ordkitc_%d_", ord_id));
			}
		});
		return tmp1s;
	}
	
	public static String getPrnIpFromFileName(File f1) {
		String prn_ip = "";
		int cnt1 = 0;
		int loc1 = -1;
		String fname1 = f1.getName();
		for (int k=0; k < fname1.length(); k++) {
			if ('_' == fname1.charAt(k)) {
				cnt1 += 1;
				if (cnt1 == 3) {
					loc1 = k;
					break;
				}
			}
		}
		if (loc1 != -1) {
			prn_ip = fname1.substring(loc1+1, fname1.lastIndexOf(".tmp"));
		}
		return prn_ip;
	}
	
	public static String toHexStr(Color col1) {
		String rstr = col1.getRed()==0 ? "00" : Integer.toHexString(col1.getRed());
		String gstr = col1.getGreen()==0 ? "00" : Integer.toHexString(col1.getGreen());
		String bstr = col1.getBlue()==0 ? "00" : Integer.toHexString(col1.getBlue());
		return String.format("#%s%s%s\n", rstr, gstr, bstr).toUpperCase();
	}
}
