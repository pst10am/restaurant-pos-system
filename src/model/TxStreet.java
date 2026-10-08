package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import resrc.ResData;

public class TxStreet implements Comparable<TxStreet> {
	
	private static TxStreet[] streets = null;
	public static void init() {
		if (null != streets) return;
		ResData.status();
		try {
			streets = ResData.getStreets();
			values = new TxStreet[20];
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static TxStreet[] values= null;

	public final String stFullName;
	public final String stDir;
	public final String stName;
	public final String stSuffix;
	public final String stSuffixDir;
	public final int stMinAddr;
	public final int stMaxAddr;
	
	private TxStreet(ResultSet rs1) throws SQLException {
		stFullName = rs1.getString("st_full_name");
		stDir = rs1.getString("st_dir");
		stName = rs1.getString("st_name");
		stSuffix = rs1.getString("st_suffix");
		stSuffixDir = rs1.getString("st_suffix_dir");
		stMinAddr = rs1.getInt("st_min_addr");
		stMaxAddr = rs1.getInt("st_max_addr");
	}
	
	public static TxStreet fromDb(ResultSet rs1) throws SQLException {
		return new TxStreet(rs1);
	}

	public int compareTo(TxStreet o2) {
		int lv1 = this.stFullName.compareTo(o2.stFullName);
		return lv1 != 0 ? lv1 : this.stName.compareTo(o2.stName);
	}
	
	public String toString() {
		return String.format("%d-%d %s", stMinAddr, stMaxAddr, stFullName);
	}
	
	public static boolean partMatched(String _input) {
		if (null == streets || 0 >= streets.length) return false;
		String _chkStr = "";
		if (_input.indexOf(",") > -1) {
			_chkStr = _input.substring(0, _input.indexOf(","));
		} else {
			_chkStr = _input;
		}
		if (_chkStr.isEmpty()) return false;
		
		for (int i=0; i < values.length; i++) {
			values[i] = null;
		}
		
		int _addrNo = 0;
		String _street = "";
		
		int loc1 = _chkStr.indexOf(" ");
		if (loc1 > 0) {
			try {
				_addrNo = Integer.parseInt(_chkStr.substring(0, loc1));
				_street = _chkStr.substring(loc1+1);
			} catch (Exception e) {
				_addrNo = 0;
				_street = _chkStr;
			}
		} else {
			try {
				_addrNo = Integer.parseInt(_chkStr);
			} catch (Exception e) {
				_addrNo = 0;
				_street = _chkStr;
			}
		}
		_street = _street.toUpperCase();
		int idx = -1;
		for (TxStreet st1 : streets) {
			if (_addrNo > 0 && st1.stMaxAddr < _addrNo) {
				continue;
			}
			if (st1.stFullName.startsWith(_street)) {
				idx += 1;
				if (idx < values.length) {
					values[idx] = st1;
				} else {
					break;
				}
			}
		}
		return idx >= 0;
	}
}
