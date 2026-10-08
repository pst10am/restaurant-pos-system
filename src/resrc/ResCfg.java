package resrc;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import refx.UnitType;
import model.DecDegree;

public class ResCfg {
	private ResCfg() {}
	
	private static Properties prop1;
	private static int maxShopLine = 5;
	
	static {
		prop1 = new Properties();
		try {
			prop1.load(new FileInputStream(new File("cfg/config.ini")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void init(Connection conn) {
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery("SELECT * FROM tx_cfg_item");
		) {
			if (rs1.next()) {
				do {
					prop1.setProperty(rs1.getString("cfg_key"), rs1.getString("cfg_value"));
				} while (rs1.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery("SELECT * FROM tx_cfg_trm");
		) {
			if (rs1.next()) {
				String trmName = getTrmName();
				do {
					String _trmName = rs1.getString("trm_name");
					if (!trmName.equals(_trmName)) {
						continue;
					}
					prop1.setProperty(rs1.getString("cfg_key"), rs1.getString("cfg_value"));
				} while (rs1.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// From File: config.ini
	
	public static String getDbConnStr() {
		return prop1.getProperty("db_conn_str");
	}
	
	public static String getDbUser() {
		return prop1.getProperty("db_user");
	}
	
	public static String getDbPwd() {
		return prop1.getProperty("db_pwd");
	}
	
	/** Google Maps API key (config.ini: maps_api_key). */
	public static String getMapsApiKey() {
		String key = prop1.getProperty("maps_api_key");
		return null == key ? "" : key;
	}

	public static String getTrmName() {
		return prop1.getProperty("trm_name");
	}
	
	// From DB
	
	public static int scrWidth() {
		return Integer.parseInt(prop1.getProperty("SCR_WIDTH"));
	}
	
	public static int scrHeight() {
		return Integer.parseInt(prop1.getProperty("SCR_HEIGHT"));
	}

	public static boolean scrFullScreen() {
		return prop1.getProperty("FULL_SCREEN").toLowerCase().equals("yes");
	}

	public static boolean scrNoBorder() {
		return prop1.getProperty("NO_FRAME").toLowerCase().equals("yes");
	}
	
	public static String theme() {
		return prop1.getProperty("THEME");
	}

	// ex: 192.168.1.255
	public static String getBroadCastIp() {
		return prop1.getProperty("BC_IP");
	}
	
	public static String getDeliCheckBy() {
		return prop1.getProperty("DELI_CHECK_BY").toUpperCase();
	}
	
	public static double getOpenItemTax() {
		return Double.parseDouble(prop1.getProperty("OPEN_ITEM_TAX"));
	}
	
	public static boolean printLogo() {
		return prop1.getProperty("PRINT_LOGO").toLowerCase().equals("yes");
	}
	
	public static boolean printBarcode() {
		return prop1.getProperty("PRINT_BARCODE").toLowerCase().equals("yes");
	}

	public static String[] getCityList() {
		String[] values = null;
		String _values = prop1.getProperty("CITY_LIST");
		if (null != _values && !_values.trim().isEmpty()) {
			values = _values.trim().split("[,]");
		}
		return values;
	}
	
	static String[] ADDR_STATE = {"IL"};
	public static String[] getStateList() {
		return ADDR_STATE;
	}
	
	public static int getTotalShopLine() {
		return maxShopLine;
	}
	
	public static String getShopAddr() {
		return prop1.getProperty("SHOP_ADDR");
	}
	
	public static int getOptionColumn() {
		int val = 3;
		try {
			val = Integer.parseInt(prop1.getProperty("OPTION_COLUMN"));
		} catch (Exception e) {
			val = 3;
		}
		return val;
		 
	}
	
	public static String[] getPhoneAreaCodes() {
		return prop1.getProperty("PHONE_AREA").split(",");
	}
	
	public static DecDegree getShopDecDegree() {
		// text input format = Lat, Lon 
		String txt1 = prop1.getProperty("SHOP_LAT_LON");
		return DecDegree.fromText(txt1);
	}
	
	public static UnitType defaultUnitType() {
		return UnitType.valueOf(prop1.getProperty("DEFAULT_UNIT_TYPE"));
	}
	
	private static final String googleGeoCodeUrl = 
		"http://maps.googleapis.com/maps/api/geocode/json?address=%s&sensor=false";
	public static String googleGeoCodeURL() {
		return googleGeoCodeUrl;
	}
	
	public static String getPackingPrnIp() {
		return prop1.getProperty("PACKING_PRN_IP");
	}
	
	public static String getTrmPrnIP() {
		return prop1.getProperty("TRM_PRN_IP");
	}
	
	public static String getTrmCashDrawer() {
		return prop1.getProperty("TRM_CASH_DRAWER");
	}
	
	public static boolean sendToPrinter() {
		return prop1.getProperty("SEND_TO_PRINTER").toLowerCase().equals("yes");
	}
	
	public static String getShopLine(int i) {
		return prop1.getProperty(String.format("SHOP_LINE_%d", i));
	}
	
	public static String getSlipGenClassName() {
		return prop1.getProperty("SLIP_GEN_CLASS");
	}

	public static String getShopName() {
		return prop1.getProperty("SHOP_NAME");
	}

	public static Properties getProperties() {
		return prop1;
	}
	
	// Payment Gateway : USAePay
	
	public static String getUEP_EndPoint() {
		return prop1.getProperty("UEP_END_POINT");
	}
	
	public static String getUEP_SourceKey() {
		return prop1.getProperty("UEP_SOURCE_KEY");
	}
	
	public static String getUEP_SourcePin() {
		return prop1.getProperty("UEP_SOURCE_PIN");
	}
	
	public static String getUEP_ClientIP() {
		return prop1.getProperty("UEP_CLIENT_IP");
	}

	public static String getOpenItemPrinterName() {
		String[] values = prop1.getProperty("OPEN_PRN_NAME").split(",");
		StringBuilder bld1 = new StringBuilder();
		for (String str1 : values) {
			if (bld1.length() > 0) {
				bld1.append(",");
			}
			bld1.append(String.format("'%s'", str1));
		}
		return bld1.toString();
	}
	
	// Runner
	
	public static boolean printRunner() {
		return prop1.getProperty("PRINT_RUNNER").equalsIgnoreCase("YES");
	}
}
