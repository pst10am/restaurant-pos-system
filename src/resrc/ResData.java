package resrc;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import com.usaepay.api.jaxws.TransactionResponse;

import refx.ClockType;
import refx.DCType;
import refx.OrderType;
import refx.PayBy;
import refx.PaySrc;
import refx.TableService;
import refx.ToGoType;
import model.FindCustInfo;
import model.TbClock;
import model.TbGift;
import model.TbGiftTrx;
import model.TbOrder;
import model.TbOrderDC;
import model.TbOrderItem;
import model.TbPayment;
import model.TbSettle;
import model.TbShift;
import model.TxArea;
import model.TxAreaVT;
import model.TxCfgGrp;
import model.TxCfgItem;
import model.TxDC;
import model.TxDel;
import model.TxStreet;
import model.TbCust;
import model.TxFdCat;
import model.TxFdItem;
import model.TxOpt;
import model.TxOptIdMand;
import model.TxOptItem;
import model.TxPrinter;
import model.TxSct;
import model.TxSctTable;
import model.TxUser;

public class ResData {
	private ResData() {}
	
	private static int getSeqNo(Connection conn, String _name) {
		int val = 0;
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(
				String.format("SELECT SeqNo('%s')", _name));
		) {
			if (rs1.next()) {
				val = rs1.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return val;
	}
	
	private static Connection conn = null;
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			conn = DriverManager.getConnection(
				ResCfg.getDbConnStr(),  
				ResCfg.getDbUser(), 
				ResCfg.getDbPwd());
			
			conn.setAutoCommit(true);
			
			ResCfg.init(conn);
			TxPrinter.init();
			
			clean();
			
		} catch (Exception excp) {
			excp.printStackTrace();
		}
	}
	
	private static void clean() {
		cleanDir("tmp");
		cleanDir("log");
	}
	
	private static int maxDaysOld = 5;
	private static void cleanDir(String cdir) {
		try {
			File tmpDir = new File(cdir);
			File[] tmpFiles = tmpDir.listFiles();
			for (File f1 : tmpFiles) {
				int daysOld = ResUtil.daysOld(f1);
				if (daysOld >= maxDaysOld) {
					f1.delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static java.util.Vector<TxOpt> getOptions(boolean allFlg) throws SQLException {
		final String sqlStr1 = "SELECT * FROM tx_opt %sORDER BY opt_seq";
		final String sqlStr2 = "SELECT a.*, b.opt_seq "
				+ "FROM tx_opt_item a "
				+ "LEFT JOIN tx_opt b ON a.opt_id=b.opt_id "
				+ "WHERE "
				+ "%sa.opt_id=%d ORDER BY a.opi_seq";
		final String ws1 = "WHERE opt_status=1 ";
		final String ws2 = "a.opi_status=1 AND ";
		java.util.Vector<TxOpt> result = new java.util.Vector<TxOpt>();
		try (
			Statement stmt1 = conn.createStatement();
			Statement stmt2 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(String.format(sqlStr1, allFlg?"":ws1));
		) {
			if (rs1.next()) {
				do {
					TxOpt opt1 = TxOpt.fromDb(rs1);
					try (
						ResultSet rs2 = stmt2.executeQuery(
							String.format(sqlStr2, allFlg?"":ws2, opt1.getOptId()));
					) {
						if (rs2.next()) {
							java.util.Vector<TxOptItem> _items = 
								new java.util.Vector<>();
							do {
								_items.add(TxOptItem.fromDb(rs2));
							} while (rs2.next());
							opt1.setOptItems(_items);
						}
					}
					result.add(opt1);
				} while (rs1.next());
			}
		}
		return result;
	}

	private static TxOptIdMand[] getOptIdsByItmId(int itmId) throws SQLException {
		final String sqlStr1 = "SELECT DISTINCT "
			+ "a.opt_id, b.opt_mand "
			+ "FROM "
			+ "tx_fd_item_opt a "
			+ "LEFT JOIN tx_opt b ON a.opt_id=b.opt_id "
			+ "WHERE a.itm_id=%d "
			+ "ORDER BY b.opt_seq";
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(String.format(sqlStr1, itmId));
		) {
			if (rs1.next()) {
				java.util.Vector<TxOptIdMand> res1 = new java.util.Vector<>();
				do {
					int _optId = rs1.getInt("opt_id");
					if (!res1.contains(_optId)) {
						res1.add(new TxOptIdMand(_optId, rs1.getBoolean("opt_mand")));
					}
				} while (rs1.next());
				TxOptIdMand[] result = new TxOptIdMand[res1.size()];
				int idx1 = 0;
				for (TxOptIdMand v1 : res1) {
					result[idx1++] = v1;
				}
				return result;
			}
		}
		return null;
	}

	// --------------------------------------------------------------
	// Public 
	// --------------------------------------------------------------
	
	private static final String[] _months = {
		"1-Jan", "2-Feb", "3-Mar",
		"4-Apr", "5-May", "6-Jun",
		"7-Jul", "8-Aug", "9-Sep",
		"10-Oct", "11-Nov", "12-Dec"
	};
	
	public static String[] months() {
		return _months;
	}
	
	public static int currentYear() {
		String yr1 = ResUtil.dtoc(new java.util.Date(), "YYYY");
		return Integer.parseInt(yr1);
	}
	
	public static void status() {
		System.out.println("> connection status.");
		try {
			System.out.printf("> [%s]\n", !conn.isClosed() ? "connected" : "disconnected");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.printf("> error:[%s]\n", e.getMessage());
		}
	}
	
	public static void close() {
		System.out.println("> closing database connection ... ");
		try {
			if (null == conn || conn.isClosed()) {
				return;
			}
			conn.close();
			System.out.println("> [done]. ");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.printf("> error:[%s]\n", e.getMessage());
		}
	}
	
	public static boolean isClose() {
		boolean flg1 = false;
		try {
			flg1 = (null == conn) || conn.isClosed();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.printf("> error:[%s]\n", e.getMessage());
		}
		return flg1;
	}

	public static int getNewOrdNo() {
		return getSeqNo(conn, "ord_no");
	}
	
	public static int getNewPmNo() {
		return getSeqNo(conn, "pm_no");
	}

	public static java.util.Vector<TxFdCat> getFoodCats(boolean allFlg) throws SQLException {
		final String sqlStr1 = "SELECT * FROM tx_fd_cat %sORDER BY cat_seq";
		final String wstr = "WHERE cat_status=1 ";
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(String.format(sqlStr1, allFlg?"":wstr));
		) {
			if (rs1.next()) {
				java.util.Vector<TxFdCat> result = new java.util.Vector<TxFdCat>();
				do {
					result.add(TxFdCat.fromDb(rs1));
				} while (rs1.next());
				return result;
			}
		}
		return null;
	}
	
	private static java.util.Vector<Integer> _getItmOptPrtId(int _type, int _id) throws SQLException {
		final String sql1 = "SELECT a.opt_id FROM tx_fd_item_opt a "
			+ "LEFT OUTER JOIN tx_opt b ON a.opt_id=b.opt_id "
			+ "WHERE a.itm_id=%d ORDER BY b.opt_seq";
		final String sql2 = "SELECT prn_id FROM tx_fd_item_printer WHERE itm_id=%d";
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(
				String.format(_type==1?sql1:sql2, _id));
		) {
			if (rs1.next()) {
				java.util.Vector<Integer> result = new java.util.Vector<>();
				do {
					result.add(rs1.getInt(1));
				} while (rs1.next());
				return result;
			}
		}
		return null;
	}

	public static java.util.Vector<TxFdItem> getItemsInCategory(
			int catId, boolean allFlg, boolean withOptPrn)
			throws SQLException {
		final String sqlStr1 = "SELECT * FROM tx_fd_item "
			+ "WHERE cat_id=%d %s"
			+ "ORDER BY itm_seq";
		final String ws1 = "AND itm_status=1 ";
		java.util.Vector<TxFdItem> result = new java.util.Vector<TxFdItem>();
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(String.format(sqlStr1, catId, allFlg?"":ws1));
		) {
			if (rs1.next()) {
				do {
					TxFdItem itm1 = TxFdItem.fromDb(rs1);
					if (withOptPrn) {
						itm1.setOptIds(_getItmOptPrtId(1, itm1.getItmId()));
						itm1.setPrnIds(_getItmOptPrtId(2, itm1.getItmId()));
					}
					result.add(itm1);
				} while (rs1.next());
			}
		}
		return result;
	}

	public static TxPrinter[] getPrinters(boolean allFlg) throws SQLException {
		final String sqlStr1 = "SELECT * FROM tx_printer %sORDER BY prn_name";
		final String ws1 = "WHERE prn_status=1 ";
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(String.format(sqlStr1, allFlg?"":ws1));
		) {
			if (rs1.next()) {
				java.util.Vector<TxPrinter> result = new java.util.Vector<TxPrinter>();
				do {
					TxPrinter prn1 = TxPrinter.fromDb(rs1);
					result.add(prn1);
				} while (rs1.next());
				TxPrinter[] values = new TxPrinter[result.size()];
				result.toArray(values);
				return values;
			}
		}
		return null;
	}

	public static java.util.Vector<TxSct> getSections(boolean allFlg) throws SQLException {
		final String sqlStr1 = "SELECT * FROM tx_sct %sORDER BY sct_seq";
		final String wstr = "WHERE sct_status=1 ";
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(
				String.format(sqlStr1, allFlg?"":wstr, sqlStr1));
		) {
			if (rs1.next()) {
				java.util.Vector<TxSct> result = new java.util.Vector<TxSct>();
				do {
					TxSct sct1 = TxSct.fromDb(rs1);
					result.add(sct1);
				} while (rs1.next());
				return result;
			}
		}
		return null;
	}

	public static java.util.Vector<TxSctTable> getTablesInSection(TxSct sct1) {
		final String sqlStr1 = "SELECT * FROM tx_sct_table WHERE tbl_status=1 AND sct_id=%d";
		java.util.Vector<TxSctTable> result = new java.util.Vector<TxSctTable>();
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(
				String.format(sqlStr1, sct1.getSctId()));
		) {
			if (rs1.next()) {
				do {
					TxSctTable tbl1 = TxSctTable.fromDb(rs1);
					result.add(tbl1);
				} while (rs1.next());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static TbCust getCustById(int _cstId) {
		final String sqlStr1 = "SELECT * FROM tb_cust WHERE cst_id=%d";
		TbCust result = null;
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(
				String.format(sqlStr1, _cstId));
		) {
			if (rs1.next()) {
				result = TbCust.fromDb(rs1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private static java.util.HashMap<Integer, TxOpt> mapOpt;
	private static TxOpt getOptByOptId(int optId) 
			throws SQLException {
		if (null == mapOpt) {
			mapOpt = new java.util.HashMap<>();
			java.util.Vector<TxOpt> opts = getOptions(false);
			for (TxOpt opt1 : opts) {
				mapOpt.put(opt1.getOptId(), opt1);
			}
		}
		return mapOpt.get(optId);
	}
	
	private static TxOptItem getOptItemByOpiId(int optId, int opiId) 
			throws SQLException {
		TxOpt opt1 = getOptByOptId(optId);
		for (TxOptItem opi1 : opt1.getOptItems()) {
			if (opiId == opi1.getOpiId()) {
				return opi1;
			}
		}
		return null;
	}

	private static java.util.HashMap<Integer, TxOptIdMand[]> mapItmOpt;
	public static java.util.Vector<TxOpt> getOptsByItmId(int itmId) throws SQLException {
		if (null == mapItmOpt) {
			mapItmOpt = new java.util.HashMap<>();
		}
		if (!mapItmOpt.containsKey(itmId)) {
			mapItmOpt.put(itmId, getOptIdsByItmId(itmId));
		}
		TxOptIdMand[] opts = mapItmOpt.get(itmId);
		if (null == opts || opts.length <= 0) {
			return null;
		}
		java.util.Vector<TxOpt> result = new java.util.Vector<>();
		for (TxOptIdMand odm : opts) {
			result.add(getOptByOptId(odm.optId));
		}
		return result;
	}
	
	public static TxOptIdMand[] getOptIdMandByItmId(int itmId) throws SQLException {
		if (itmId == 0) return null;
		if (!mapItmOpt.containsKey(itmId)) {
			mapItmOpt.put(itmId, getOptIdsByItmId(itmId));
		}
		return mapItmOpt.get(itmId);
	}
	
	public static TxStreet[] getStreets() {
		final String sqlStr1 = "SELECT * FROM tx_street";
		java.util.Vector<TxStreet> res1 = new java.util.Vector<>();
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(sqlStr1);
		) {
			if (rs1.next()) {
				do {
					res1.add(TxStreet.fromDb(rs1));
				} while (rs1.next());
			}
			TxStreet[] result = new TxStreet[res1.size()];
			res1.toArray(result);
			Arrays.sort(result);
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static TxDC[] getTxDC(DCType _type) {
		final String sqlStr1 = "SELECT * FROM tx_dc WHERE dc_type='%s' AND dc_status=1 ORDER BY dc_id";
		TxDC[] result = null;
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(
				String.format(sqlStr1, _type.toString()));
		) {
			if (rs1.next()) {
				java.util.Vector<TxDC> _tmpres = new java.util.Vector<>();
				do {
					_tmpres.add(TxDC.fromDb(rs1));
				} while (rs1.next());
				result = new TxDC[_tmpres.size()];
				_tmpres.toArray(result);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static java.util.Vector<FindCustInfo> searchCustByPhone(String _phone) {
		final String sqlStr1 = "SELECT "+
			"cst_id, "+
			"cst_code, "+
			"cst_phone, "+
			"cst_name, "+
			"IFNULL(cst_addr_1,'') cst_addr_1, "+
			"IFNULL(cst_addr_2,'') cst_addr_2, "+
			"IFNULL(cst_unit_no,'') cst_unit_no, "+
			"IFNULL(cst_city,'') cst_city, "+
			"IFNULL(cst_state,'') cst_state "+
			"FROM tb_cust "+
			"WHERE "+
			"cst_status = 1 "+
			"AND cst_name > ' ' "+
			"AND cst_phone LIKE '%s%%' "+
			"ORDER BY cst_name LIMIT 15";
		java.util.Vector<FindCustInfo> result = null;
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(
				String.format(sqlStr1, _phone));
		) {
			if (rs1.next()) {
				result = new java.util.Vector<>();
				do {
					result.add(FindCustInfo.fromDb(rs1));
				} while (rs1.next());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static TbCust findCustomerByCode(String _code) {
		final String sqlStr1 = "SELECT * FROM tb_cust WHERE cst_code > '' AND cst_code = '%s'";
		TbCust cst1 = null;
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(
				String.format(sqlStr1, _code));
		) {
			if (rs1.next()) {
				cst1 = TbCust.fromDb(rs1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cst1;
	}

	public static TxUser findUserByPOSPwd(String pwd) {
		final String sqlStr1 = "SELECT * FROM tx_user WHERE usr_pos_pwd='%s'";
		TxUser usr1 = null;
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(
				String.format(sqlStr1, pwd));
		) {
			if (rs1.next()) {
				usr1 = TxUser.fromDb(rs1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return usr1;
	}
	
	private static TxAreaVT[] areaVertices(int _id) {
		TxAreaVT[] vts = null;
		final String sqlStr1 = "SELECT * FROM tx_area_vt "
			+ "WHERE area_id=%d ORDER BY vseq";
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(String.format(sqlStr1, _id));
		) {
			if (rs1.next()) {
				java.util.Vector<TxAreaVT> values = new java.util.Vector<>();
				do {
					values.add(TxAreaVT.fromDb(rs1));
				} while (rs1.next());
				vts = new TxAreaVT[values.size()];
				values.toArray(vts);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (null == vts) {
			throw new IllegalArgumentException("invalid area vertices.");
		}
		return vts;
	}

	private static TxArea[] deliAreas = null;
	public static TxArea[] deliAreas() {
		if (null == deliAreas) {
			final String sqlStr1 = "SELECT * FROM tx_area "
				+ "WHERE area_status = 1 ORDER BY area_id";
			try (
				Statement stmt1 = conn.createStatement();
				ResultSet rs1 = stmt1.executeQuery(sqlStr1);
			) {
				if (rs1.next()) {
					java.util.Vector<TxArea> values = new java.util.Vector<>();
					do {
						values.add(TxArea.fromDb(rs1));
					} while (rs1.next());
					deliAreas = new TxArea[values.size()];
					values.toArray(deliAreas);
					//
					for (TxArea ar1 : deliAreas) {
						ar1.setVertices(areaVertices(ar1.getAreaId()));
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return deliAreas;
	}

	// #######################
	// ----- data update -----
	// #######################

	public static void saveTbOrder(TbOrder ord1) throws SQLException {
		ResDB.saveTbOrder(conn, ord1);
	}

	public static void saveTbOrderItem(TbOrderItem odi1) throws SQLException {
		ResDB.saveTbOrderItem(conn, odi1);
	}

	public static void saveTbGiftTrx(TbGiftTrx tx1) throws SQLException {
		ResDB.saveTbGiftTrx(conn, tx1);
	}
	
	public static void clearTbOrderDC(int ordId) throws SQLException {
		final String sqlStr = 
			"UPDATE tb_order_dc SET odc_status=0 WHERE ord_id=%d";
		try (
			Statement stmt1 = conn.createStatement();
		) {
			stmt1.executeUpdate(String.format(sqlStr, ordId));
		}
	}
	
	public static void saveTbOrderDC(TbOrderDC odc1) throws SQLException {
		try (
			PreparedStatement pstmt1 = conn.prepareStatement(
				SqlStr.sqlTbOrderDCInsert);
		) {
			pstmt1.setInt(1, odc1.getOrdId());
			pstmt1.setInt(2, odc1.getDcId());
			pstmt1.setString(3, odc1.getDcDesc());
			pstmt1.setString(4, odc1.getDcType().toString());
			pstmt1.setString(5, odc1.getDcAmtType().toString());
			pstmt1.setDouble(6, ResUtil.round(odc1.getDcAmt()));
			pstmt1.setDouble(7, ResUtil.round(odc1.getOdcAmt()));
			pstmt1.executeUpdate();
		}
	}

	public static void clearTbOrderItemOpi(int odiId) throws SQLException {
		final String sqlStr = 
			"UPDATE tb_order_item_opi SET odp_status=0 WHERE odi_id=%d";
		try (Statement stmt1 = conn.createStatement()) 
		{
			stmt1.executeUpdate(String.format(sqlStr, odiId));
		}
	}
	
	public static void saveTbOrderItemOpi(int odiId, TxOptItem opi1) throws SQLException {
		try (
			PreparedStatement pstmt1 = conn.prepareStatement(
				SqlStr.sqlTbOrderItemOpiInsert);
		) {
			pstmt1.setInt(1, odiId);
			pstmt1.setInt(2, opi1.getOptId());
			pstmt1.setInt(3, opi1.getOpiId());
			pstmt1.setInt(4, opi1.getOpiSeq());
			pstmt1.setString(5, opi1.getOpiNamePos());
			pstmt1.setDouble(6, ResUtil.round(opi1.getOpiPrice()));
			pstmt1.executeUpdate();
		}
	}
	
	public static void updateTableServiceById(int tblId, TableService tblSrv) throws SQLException {
		final String sqlStr = "UPDATE tx_sct_table SET tbl_service='%s' WHERE tbl_id=%d";
		try (Statement stmt1 = conn.createStatement()) {
			stmt1.executeUpdate(String.format(sqlStr, tblSrv.toString(), tblId));
		}
	}
	
	public static void lockTableById(int tblId, boolean lockFlg) throws SQLException {
		final String sqlStr = "UPDATE tx_sct_table SET tbl_is_locked=%d WHERE tbl_id=%d";
		try (Statement stmt1 = conn.createStatement()) {
			stmt1.executeUpdate(String.format(sqlStr, lockFlg?1:0, tblId));
		}
	}

	public static void refreshTableService(TxSctTable tbl1) throws SQLException {
		final String sqlStr = "SELECT ord_id, ord_cnt_check "
			+ "FROM tb_order WHERE "
			+ "ord_status=1 "
			+ "AND ord_paid=0 "
			+ "AND ord_type='DineIn' AND tbl_id=%d";
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(String.format(sqlStr, tbl1.getTblId()));
		) {
			if (rs1.next()) {
				// found unpaid orders
				int totChk = 0;
				int totOrd = 0;
				do {
					int ord_cnt_check = rs1.getInt("ord_cnt_check");
					totChk += ord_cnt_check > 0 ? 1 : 0;
					totOrd += 1;
				} while (rs1.next());
				if (totOrd == totChk) {
					tbl1.setTblService(TableService.Checked);
				} else {
					tbl1.setTblService(TableService.Service);
				}
			} else {
				// not found unpaid orders
				final String sqlStr2 = "SELECT tbl_service FROM tx_sct_table WHERE tbl_id=%d";
				try (
					ResultSet rs2 = 
						stmt1.executeQuery(
							String.format(sqlStr2, tbl1.getTblId()));
				) {
					if (rs2.next()) {
						tbl1.setTblService(TableService.valueOf(rs2.getString("tbl_service")));
					}
				}
			}
		}
	}

	public static boolean isTableLocked(int tblId) throws SQLException {
		final String sqlStr = "SELECT tbl_is_locked FROM "
			+ "tx_sct_table WHERE tbl_id=%d";
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(String.format(sqlStr, tblId));
		) {
			if (rs1.next()) {
				return rs1.getBoolean("tbl_is_locked");
			}
		}
		throw new SQLException(String.format("Table with Id=[%d] not found.", tblId));
	}

	public static TbOrder[] findOrderByTableId(int _id) throws SQLException {
		TbOrder[] result = null;
		final String sqlStr = "SELECT * FROM tb_order "
			+ "WHERE ord_status=1 AND ord_paid=0 AND ord_cnt_check=0 AND tbl_id=%d";
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(String.format(sqlStr, _id));
		) {
			if (rs1.next()) {
				java.util.Vector<TbOrder> _values = new java.util.Vector<>();
				do {
					_values.add(TbOrder.fromDb(rs1));
				} while (rs1.next());
				result = new TbOrder[_values.size()];
				_values.toArray(result);
			}
		}
		return result;
	}

	public static TbOrder[] findOrderByPayAndType(
			boolean _isPaid, OrderType _ordType, ToGoType _toGoType
			, java.util.Date _frm, java.util.Date _to) 
			throws SQLException {
		
		TbOrder[] result = null;
		
		final String sqlStr = "SELECT * FROM tb_order "
			+ "WHERE ord_status=1 "
			+ "AND ord_paid=%d "
			+ "%s"
			+ "%s"
			+ "ORDER BY ord_no DESC";
		
		String sqlOrdDt = ""; // "AND (ord_dt BETWEEN '%s' AND '%s') ";
		if (_isPaid) {
			final String _sqlOrdDt = "AND (ord_id IN (SELECT DISTINCT ref_id FROM tb_payment "
				+ "WHERE pm_status=1 "
				+ "AND ref_src='Order' "
				+ "AND (pm_dt BETWEEN '%s' AND '%s'))) ";
			sqlOrdDt = String.format(_sqlOrdDt, 
				ResUtil.dtoc(_frm, "yyyy-MM-dd HH:mm:ss"),
				ResUtil.dtoc(_to, "yyyy-MM-dd HH:mm:ss")
				);
		}
		
		String sqlOrdType = "AND ord_type='DineIn' ";
		if (OrderType.ToGo == _ordType) {
			sqlOrdType = String.format(
				"AND ord_type='ToGo' AND ord_togo_type='%s' ", 
				_toGoType);
		}
		
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(
				String.format(sqlStr, (_isPaid?1:0), 
					sqlOrdDt, sqlOrdType));
		) {
			if (rs1.next()) {
				java.util.Vector<TbOrder> _values = new java.util.Vector<>();
				do {
					_values.add(TbOrder.fromDb(rs1));
				} while (rs1.next());
				result = new TbOrder[_values.size()];
				_values.toArray(result);
			}
		}
		return result;
	}

	public static void updateOrder_Items(int ordId, java.util.Vector<TbOrderItem> items) throws SQLException {
		final String sqlStr = "SELECT * FROM tb_order_item "
			+ "WHERE "
			+ "ord_id=%d AND odi_status=1 "
			+ "ORDER BY odi_seq";
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(String.format(sqlStr, ordId));
		) {
			items.clear();
			if (rs1.next()) {
				do {
					items.add(TbOrderItem.fromDb(rs1));
				} while (rs1.next());
			}
		}
	}

	public static void updateOrder_Payment(int ordId, java.util.Vector<TbPayment> payments) throws SQLException {
		final String sqlStr = "SELECT * FROM tb_payment "
			+ "WHERE "
			+ "ref_src='Order' AND pm_status=1 AND "
			+ "ref_id=%d "
			+ "ORDER BY ref_id";
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(String.format(sqlStr, ordId));
		) {
			payments.clear();
			if (rs1.next()) {
				do {
					payments.add(TbPayment.fromDb(rs1));
				} while (rs1.next());
			}
		}
	}
	
	public static void updateOrder_DC(int ordId, 
		java.util.Vector<TbOrderDC> discounts, 
		java.util.Vector<TbOrderDC> charges) throws SQLException {
		
		final String sqlStr = "SELECT *, 1 dc_status FROM tb_order_dc "
			+ "WHERE "
			+ "ord_id=%d AND odc_status=1";
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(String.format(sqlStr, ordId));
		) {
			discounts.clear();
			charges.clear();
			if (rs1.next()) {
				do {
					TbOrderDC odc1 = TbOrderDC.fromDb(rs1);
					if (DCType.Discount == odc1.getDcType()) {
						discounts.add(odc1);
					} else if (DCType.Charge == odc1.getDcType()) {
						charges.add(odc1);
					}
				} while (rs1.next());
			}
		}
	}

	public static void updateOrderItem_Options(int odiId, java.util.Vector<TxOptItem> options) 
		throws SQLException {
		
		final String sqlStr = "SELECT opt_id, opi_id FROM tb_order_item_opi "
			+ "WHERE "
			+ "odi_id=%d AND odp_status=1";
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(String.format(sqlStr, odiId));
		) {
			if (rs1.next()) {
				do {
					int optId = rs1.getInt("opt_id");
					int opiId = rs1.getInt("opi_id");
					TxOptItem opi1 = getOptItemByOpiId(optId, opiId);
					if (null != opi1) {
						options.add(opi1);
					}
				} while (rs1.next());
			}
		}
	}

	private static TbOrder[] getTbOrder(String sqlStr) throws SQLException {
		TbOrder[] values = null;
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(sqlStr);
		) {
			if (rs1.next()) {
				java.util.Vector<TbOrder> _values = new java.util.Vector<>();
				do {
					_values.add(TbOrder.fromDb(rs1));
				} while (rs1.next());
				values = new TbOrder[_values.size()];
				_values.toArray(values);
			}
		}
		return values;
	}
	private static final String tmpTbOrderSql = "SELECT "
			+ " * FROM tb_order";
	public static TbOrder[] getTbOrderByTblIdChecked(int tblId) throws SQLException {
		String sqlStr = "%s WHERE "
				+ "ord_status=1 "
				+ "AND tbl_id=%d "
				+ "AND ord_paid=0 "
				+ "AND ord_cnt_check > 0 "
			+ "ORDER BY ord_no";
		return getTbOrder(String.format(sqlStr, tmpTbOrderSql, tblId));
	}
	public static TbOrder getTbOrderByOrdNo(int ordNo) throws SQLException {
		String sqlStr = "%s WHERE ord_status=1 AND ord_no=%d ";
		TbOrder[] _values = getTbOrder(String.format(sqlStr, tmpTbOrderSql, ordNo));
		return null != _values ? _values[0] : null;
	}
	
	public static TbOrder[] getTbOrderWFP() throws SQLException {
		String sqlStr = "SELECT * FROM tb_order "
			+ "WHERE "
			+ "ord_status=1 AND "
			+ "ord_cnt_check > 0 AND "
			+ "ord_type='ToGo' AND "
			+ "ord_pick=0 "
			+ "ORDER BY ord_dt";
		return getTbOrder(sqlStr);
	}

	public static TxSctTable findTableById(int _id) throws SQLException {
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(
				String.format("SELECT * FROM tx_sct_table WHERE tbl_id=%d", _id));
		) {
			if (rs1.next()) {
				return TxSctTable.fromDb(rs1);
			}
		}
		return null;
	}

	public static TbGift findGiftCard(String _code) throws SQLException {
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(
				String.format("SELECT * FROM tb_gift WHERE gft_code='%s' AND gft_status=1", _code));
		) {
			if (rs1.next()) {
				return TbGift.fromDb(rs1);
			}
		}
		return null;
	}

	public static void saveTbPayment(TbPayment pm1) throws SQLException {
		ResDB.saveTbPayment(conn, pm1);
	}

	public static void updateOrderIsPaid(int ordId, boolean isPaid, double amtChange) throws SQLException {
		final String sqlStr = "UPDATE tb_order SET "
			+ "ord_pm_change=%.2f, "
			+ "ord_paid=%d "
			+ "WHERE ord_id=%d";
		try (Statement stmt1 = conn.createStatement()) {
			stmt1.executeUpdate(String.format(sqlStr, 
				amtChange, 
				(isPaid?1:0),
				ordId));
		}
	}

	public static boolean tableIsPaidById(int tblId) throws SQLException {
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(
				String.format("SELECT count(*) cnt1 FROM tb_order "
					+ "WHERE ord_status=1 AND ord_paid=0 AND tbl_id=%d", tblId));
		) {
			if (rs1.next()) {
				return 0 == rs1.getInt("cnt1");
			}
		}
		return false;
	}

	public static void deleteOrderItem(int delId, TxDel delObj) throws SQLException {
		try (
			Statement stmt1 = conn.createStatement();
		) {
			stmt1.executeUpdate(
				String.format("UPDATE tb_order_item SET odi_status=2, odi_remark='%s' WHERE odi_id=%d", 
					delObj.getDlDesc(), delId));
		}
	}

	public static void saveTxDel(TxDel dl1) throws SQLException {
		ResDB.saveTxDel(conn, dl1);
	}

	public static void deleteTxDel(int dlId) throws SQLException {
		try (
			Statement stmt1 = conn.createStatement();
		) {
			stmt1.executeUpdate(
				String.format("DELETE FROM tx_del WHERE dl_id=%d", dlId));
		}
	}

	public static TxDel[] getDeleteReasons() throws SQLException {
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery("SELECT * FROM tx_del ORDER BY dl_desc");
		) {
			if (rs1.next()) {
				java.util.Vector<TxDel> values = new java.util.Vector<>();
				do {
					values.add(TxDel.fromDb(rs1));
				} while (rs1.next());
				TxDel[] resArr = new TxDel[values.size()];
				values.copyInto(resArr);
				return resArr;
			}
		}
		return null;
	}

	public static TbPayment[] getPaymentHistById(int _id, PaySrc _src) throws SQLException {
		String sqlStr = String.format(
			"SELECT * FROM tb_payment WHERE "
			+ "pm_status=1 "
			+ "AND ref_src='%s' "
			+ "AND ref_id=%d "
			+ "AND tx_no > 0 "
			+ "AND tx_result='Approved' "
			+ "ORDER BY pm_id", 
			_src.toString(), _id);
		return _getPaymentHist(sqlStr);
	}
	public static TbPayment[] getPaymentHistByOrdNo(int _ordNo) throws SQLException {
		String sqlStr =  String.format(
			"SELECT * FROM tb_payment "
			+ "WHERE "
			+ "pm_status=1 "
			+ "AND ref_src='Order' "
			+ "AND ord_no=%d "
			+ "AND tx_no > 0 "
			+ "AND tx_result='Approved' "
			+ "ORDER BY pm_id", 
			_ordNo);
		return _getPaymentHist(sqlStr);
	}
	private static TbPayment[] _getPaymentHist(String sqlStr) throws SQLException {
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(sqlStr);
		) {
			if (rs1.next()) {
				java.util.Vector<TbPayment> values = new java.util.Vector<>();
				do {
					values.add(TbPayment.fromDb(rs1));
				} while (rs1.next());
				TbPayment[] resArr = new TbPayment[values.size()];
				values.copyInto(resArr);
				return resArr;
			}
		}
		return null;
	}

	public static TbGiftTrx[] getGiftTrans(int _id) throws SQLException {
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(
				String.format("SELECT * FROM tb_gift_trx WHERE gft_id=%d ORDER BY tx_dt", _id));
		) {
			if (rs1.next()) {
				java.util.Vector<TbGiftTrx> values = new java.util.Vector<>();
				do {
					values.add(TbGiftTrx.fromDb(rs1));
				} while (rs1.next());
				TbGiftTrx[] resArr = new TbGiftTrx[values.size()];
				values.copyInto(resArr);
				return resArr;
			}
		}
		return null;
	}

	public static void saveTbGift(TbGift gft1) throws SQLException {
		ResDB.saveTbGift(conn, gft1);
	}

	public static double findGiftBalance(int gftId) throws SQLException {
		double _balance = 0;
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(
				String.format("SELECT gft_balance FROM tb_gift WHERE gft_id=%d", gftId));
		) {
			if (rs1.next()) {
				_balance = rs1.getDouble("gft_balance");
			}
		}
		return _balance;
	}

	public static boolean updateGiftBalance(int _id, double _bal) throws SQLException {
		try (
			PreparedStatement pstmt1 = 
				conn.prepareStatement(
					String.format("UPDATE tb_gift SET gft_balance=? WHERE gft_id=%d", _id));
		) {
			pstmt1.setDouble(1, _bal);
			pstmt1.executeUpdate();
		}
		return true;
	}

	public static void orderCheck(int _id) throws SQLException {
		try (
			Statement stmt1 = conn.createStatement(); 
		) {
			stmt1.executeUpdate(String.format("UPDATE tb_order SET ord_cnt_check=1 WHERE ord_id=%d", _id));
		}
	}
	
	public static TbOrder findOrderByOrdId(int _id) throws SQLException {
		final String sqlStr = "SELECT * FROM tb_order WHERE ord_id=%d";
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(String.format(sqlStr, _id));
		) {
			if (rs1.next()) {
				return TbOrder.fromDb(rs1);
			}
		}
		return null;
	}

	public static TbOrder findOrderByOrdNo(int _id) throws SQLException {
		final String sqlStr = "SELECT * FROM tb_order WHERE ord_no=%d";
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(String.format(sqlStr, _id));
		) {
			if (rs1.next()) {
				return TbOrder.fromDb(rs1);
			}
		}
		return null;
	}

	// 2015/1/31
	public static int[] findPrinters(int _itmId) {
		final String sqlStr1 = "SELECT DISTINCT prn_id FROM tx_fd_item_printer WHERE itm_id=%d";
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(String.format(sqlStr1, _itmId));
		) {
			if (rs1.next()) {
				java.util.Vector<Integer> tmparr1 = 
					new java.util.Vector<Integer>();
				do {
					tmparr1.add(rs1.getInt("prn_id"));
				} while (rs1.next());
				//
				int[] values = new int[tmparr1.size()];
				for (int idx1=0; idx1 < tmparr1.size(); idx1++) {
					values[idx1] = tmparr1.get(idx1);
				}
				return values;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void deleteTxSct(int uid, boolean delFlg) throws SQLException {
		try (
			Statement stmt1 = conn.createStatement(); 
		) {
			stmt1.executeUpdate(String.format(
				"UPDATE tx_sct SET sct_status=%d WHERE sct_id=%d", delFlg?2:1, uid));
		}
	}

	public static byte[] findImage(String pictName) throws Exception {
		final String sqlStr = "SELECT * FROM tx_pict WHERE pct_file_name='%s'";
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(String.format(sqlStr, pictName));
		) {
			if (rs1.next()) {
				return rs1.getBytes("pct_file");
			}
		}
		return null;
	}

	public static void saveTxSct(TxSct sct1) throws SQLException {
		ResDB.saveTxSct(conn, sct1);
	}

	public static void saveTxPict(String _name, byte[] _img) throws SQLException {
		ResDB.saveTxPict(conn, _name, _img);
	}

	public static void saveTxSctTable(TxSctTable tbl1) throws SQLException {
		ResDB.saveTxSctTable(conn, tbl1);
	}

	public static void deleteTxSctTable(int uid) throws SQLException {
		try (
			Statement stmt1 = conn.createStatement(); 
		) {
			stmt1.executeUpdate(String.format(
				"UPDATE tx_sct_table SET tbl_status=2 WHERE tbl_id=%d", uid));
		}
	}

	public static TxUser[] getAllUser(boolean allFlg) throws SQLException {
		final String sqlStr = "SELECT * FROM tx_user %sORDER BY usr_name";
		final String ws1 = "WHERE is_active=1 ";
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(String.format(sqlStr, allFlg?"":ws1));
		) {
			if (rs1.next()) {
				java.util.Vector<TxUser> values = new java.util.Vector<>();
				do {
					values.add(TxUser.fromDb(rs1));
				} while (rs1.next());
				TxUser[] result = new TxUser[values.size()];
				values.toArray(result);
				return result;
			}
		}
		return null;
	}

	public static void saveTxPrinter(TxPrinter prn1) throws SQLException {
		ResDB.saveTxPrinter(conn, prn1);
	}

	public static void deleteTxPrinter(int uid, int flg) throws SQLException {
		try (
			Statement stmt1 = conn.createStatement(); 
		) {
			stmt1.executeUpdate(String.format(
				"UPDATE tx_printer SET prn_status=%d WHERE prn_id=%d", flg, uid));
		}
	}

	public static void saveTxOpt(TxOpt opt1) throws SQLException {
		ResDB.saveTxOpt(conn, opt1);
	}

	public static void saveTxOptItem(TxOptItem opi1) throws SQLException {
		ResDB.saveTxOptItem(conn, opi1);
	}

	public static void deleteTxOpt(int uid, int flg) throws SQLException {
		try (
			Statement stmt1 = conn.createStatement(); 
		) {
			stmt1.executeUpdate(String.format(
				"UPDATE tx_opt SET opt_status=%d WHERE opt_id=%d", flg, uid));
		}
	}

	public static void saveTxCat(TxFdCat cat1) throws SQLException {
		ResDB.saveTxCat(conn, cat1);
	}

	public static void saveTxFdItem(TxFdItem itm1) throws SQLException {
		ResDB.saveTxFdItem(conn, itm1);
	}

	public static void saveTxFdItemOptPrt(int _id, Vector<Integer> optIds,
			Vector<Integer> prnIds) throws SQLException {
		try (
			Statement stmt1 = conn.createStatement(); 
		) {
			stmt1.addBatch(String.format("DELETE FROM tx_fd_item_opt WHERE itm_id=%d", _id));
			if (null != optIds && optIds.size() > 0) {
				for (int _optId : optIds) {
					stmt1.addBatch(String.format(
						"INSERT INTO tx_fd_item_opt (itm_id, opt_id) VALUES (%d, %d)", _id, _optId));
				}
			}
			stmt1.addBatch(String.format("DELETE FROM tx_fd_item_printer WHERE itm_id=%d", _id));
			if (null != prnIds && prnIds.size() > 0) {
				for (int _prnId : prnIds) {
					stmt1.addBatch(String.format(
						"INSERT INTO tx_fd_item_printer (itm_id, prn_id) VALUES (%d, %d)", _id, _prnId));
				}
			}
			stmt1.executeBatch();
		}
	}

	public static void kitchenPrinted(int ordId) throws SQLException {
		try (
			Statement stmt1 = conn.createStatement(); 
		) {
			stmt1.executeUpdate(String.format(
				"UPDATE tb_order_item SET odi_printed=1 WHERE ord_id=%d AND odi_hold=0", ordId));
		}
	}

	public static void reopen(int ordId) throws SQLException {
		try (
			Statement stmt1 = conn.createStatement(); 
		) {
			stmt1.executeUpdate(String.format(
				"UPDATE tb_order SET ord_cnt_check=0 WHERE ord_id=%d", ordId));
		}
	}

	public static void deleteTxFdCat(int catId, int delflg) throws SQLException {
		try (
			Statement stmt1 = conn.createStatement(); 
		) {
			stmt1.executeUpdate(String.format(
				"UPDATE tx_fd_cat SET cat_status=%d WHERE cat_id=%d", delflg, catId));
		}
	}

	public static void deleteTxUser(int usrId, boolean b) throws SQLException {
		try (
			Statement stmt1 = conn.createStatement(); 
		) {
			stmt1.executeUpdate(String.format(
				"UPDATE tx_user SET is_active=%d WHERE usr_id=%d", b?1:0, usrId));
		}
	}

	public static void deleteTbPayment(TbPayment pm1) throws SQLException {
		try (
			Statement stmt1 = conn.createStatement(); 
		) {
			stmt1.executeUpdate(String.format(
				"UPDATE tb_payment SET pm_status=0 WHERE pm_id=%d", pm1.getPmId()));
		}
	}

	public static void saveTxUser(TxUser usr1) throws SQLException {
		ResDB.saveTxUser(conn, usr1);
	}
	
	
	public static TxSctTable[] _getTxSctTable(String sqlStr) throws SQLException {
		TxSctTable[] values = null;
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(sqlStr);
		) {
			if (rs1.next()) {
				java.util.Vector<TxSctTable> _res = new java.util.Vector<TxSctTable>();
				do {
					TxSctTable tbl1 = TxSctTable.fromDb(rs1);
					_res.add(tbl1);
				} while (rs1.next());
				values = new TxSctTable[_res.size()];
				_res.toArray(values);
			}
		}
		return values;
	}

	public static TxSctTable[] getLockedTables() throws SQLException {
		final String sqlStr1 = "SELECT * FROM tx_sct_table "
			+ "WHERE tbl_status=1 AND tbl_is_locked=1 ORDER BY tbl_name";
		return _getTxSctTable(sqlStr1);
	}

	public static TxSctTable[] getServiceTables() throws SQLException {
		final String sqlStr1 = "SELECT * FROM tx_sct_table WHERE "
			+ "tbl_status=1 "
			+ "AND tbl_id IN ("
			  + "SELECT tbl_id FROM tb_order "
			  + "WHERE ord_type='DineIn' "
			  + "AND ord_status=1 "
			  + "AND ord_paid=0 "
			  + "AND ord_cnt_check=0 "
			+ ") ORDER BY tbl_name";
		return _getTxSctTable(sqlStr1);
	}

	public static void saveTbShift(TbShift sft1) throws SQLException {
		ResDB.saveTbShift(conn, sft1);
	}

	public static TbShift[] findAllShift() throws SQLException {
		final String sqlStr1 = "SELECT * FROM tb_shift "
			+ "WHERE "
			+ "cl_status=1 "
			+ "ORDER BY cl_dt DESC";
		TbShift[] values = null;
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(sqlStr1);
		) {
			if (rs1.next()) {
				java.util.Vector<TbShift> _res = new java.util.Vector<TbShift>();
				do {
					TbShift tbl1 = TbShift.fromDb(rs1);
					_res.add(tbl1);
				} while (rs1.next());
				values = new TbShift[_res.size()];
				_res.toArray(values);
			}
		}
		return values;
	}

	public static TbShift findLastShift() throws SQLException {
		final String sqlStr1 = "SELECT * FROM tb_shift "
			+ "WHERE "
			+ "cl_status=1 "
			+ "ORDER BY cl_dt DESC "
			+ "LIMIT 1";
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(sqlStr1);
		) {
			if (rs1.next()) {
				return TbShift.fromDb(rs1);
			}
		}
		return null;
	}

	public static TbSettle findLastSettle() throws SQLException {
		final String sqlStr1 = "SELECT * FROM tb_settle "
			+ "WHERE "
			+ "stl_status=1 "
			+ "ORDER BY stl_dt DESC "
			+ "LIMIT 1";
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(sqlStr1);
		) {
			if (rs1.next()) {
				return TbSettle.fromDb(rs1);
			}
		}
		return null;
	}

	public static ClockType getLastClock(java.util.Date fromDt, int usrId) throws SQLException {
		final String sqlStr1 = "SELECT * FROM tb_clock "
			+ "WHERE "
			+ "usr_id=%d "
			+ "AND clk_dt > '%s' "
			+ "ORDER BY clk_dt DESC "
			+ "LIMIT 1";
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(
				String.format(sqlStr1, usrId, ResUtil.dtoc(fromDt, "yyyy-MM-dd")));
		) {
			if (rs1.next()) {
				return ClockType.valueOf(rs1.getString("clk_type"));
			}
		}
		return ClockType.Out;
	}

	public static void clockEmp(TbClock clk1) throws SQLException {
		final String sqlStr1 = "INSERT INTO tb_clock "
			+ "(usr_id, usr_name, clk_dt, clk_type) "
			+ "VALUES (?, ?, ?, ?)";
		try (
			PreparedStatement pstmt1 = conn.prepareStatement(sqlStr1,
				Statement.RETURN_GENERATED_KEYS);
		) {
			pstmt1.setInt(1, clk1.getUsrId());
			pstmt1.setString(2, clk1.getUsrName());
			pstmt1.setTimestamp(3, new java.sql.Timestamp(clk1.getClkDt().getTime()));
			pstmt1.setString(4, clk1.getClkType().toString());
			pstmt1.executeUpdate();
			//
			ResultSet rs1 = pstmt1.getGeneratedKeys();
			if (rs1.next()) {
				clk1.setClkId(rs1.getInt(1));
			}
			rs1.close();rs1=null;
		}
	}

	public static TbOrder[] getPaidOrders(java.util.Date clLw, java.util.Date clUp) throws SQLException {
		final String sqlStr1 = 
			"SELECT "
			+"* "
			+"FROM tb_order "
			+"WHERE "
			+"ord_status=1 AND ord_paid "
			+"AND ord_id IN ( "
			+"SELECT DISTINCT ref_id "
			+"FROM tb_payment "
			+"WHERE pm_status=1 "
			+"AND ref_src='Order' "
			+"AND (pm_dt BETWEEN ? AND ?)) "
			+"ORDER BY ord_no";
		try (
			PreparedStatement pstmt1 = conn.prepareStatement(sqlStr1);
		) {
			pstmt1.setTimestamp(1, new java.sql.Timestamp(clLw.getTime()));
			pstmt1.setTimestamp(2, new java.sql.Timestamp(clUp.getTime()));
			ResultSet rs1 = pstmt1.executeQuery();
			if (rs1.next()) {
				java.util.Vector<TbOrder> orders = 
					new java.util.Vector<TbOrder>();
				do {
					orders.addElement(TbOrder.fromDb(rs1));
				} while (rs1.next());
				TbOrder[] values = new TbOrder[orders.size()];
				orders.toArray(values);
				return values;
			}
		}
		return null;
	}

	public static TbPayment[] getTbPaymentData(java.util.Date dt1, java.util.Date dt2) throws SQLException {
		final String sqlStr1 = "SELECT * FROM tb_payment "
			+ "WHERE "
			+ "pm_status = 1 "
			+ "AND (pm_dt BETWEEN ? AND ?) "
			+ "ORDER BY pm_dt";
		try (
			PreparedStatement pstmt1 = conn.prepareStatement(sqlStr1);
		) {
			pstmt1.setTimestamp(1, new java.sql.Timestamp(dt1.getTime()));
			pstmt1.setTimestamp(2, new java.sql.Timestamp(dt2.getTime()));
			ResultSet rs1 = pstmt1.executeQuery();
			if (rs1.next()) {
				java.util.Vector<TbPayment> orders = 
					new java.util.Vector<TbPayment>();
				do {
					orders.addElement(TbPayment.fromDb(rs1));
				} while (rs1.next());
				TbPayment[] values = new TbPayment[orders.size()];
				orders.toArray(values);
				return values;
			}
		}
		return null;
	}

	public static void updateOrderIsPicked(int ordId, boolean b) throws SQLException {
		final String sqlStr = "UPDATE tb_order SET "
			+ "ord_pick=%d "
			+ "WHERE ord_id=%d";
		try (Statement stmt1 = conn.createStatement()) {
			stmt1.executeUpdate(String.format(sqlStr, b?1:0, ordId)); 
		}
	}

	public static void saveTbCust(TbCust cst1) throws SQLException {
		ResDB.saveTbCust(conn, cst1);
	}

	public static Vector<TxCfgGrp> getConfigGroups() throws SQLException {
		java.util.Vector<TxCfgGrp> values = null;
		final String sqlStr = "SELECT * FROM tx_cfg_grp";
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(sqlStr);
		) {
			if (rs1.next()) {
				values = new java.util.Vector<>();
				do {
					TxCfgGrp grp1 = TxCfgGrp.fromDb(rs1);
					grp1.setItems(ResData.getConfigItems(grp1.getGrpId()));
					values.add(grp1);
				} while (rs1.next());
			}
		}
		return values;
	}
	public static Vector<TxCfgItem> getConfigItems(int grpId) throws SQLException {
		java.util.Vector<TxCfgItem> values = null;
		final String sqlStr = "SELECT * FROM tx_cfg_item WHERE grp_id=%d";
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(String.format(sqlStr, grpId));
		) {
			if (rs1.next()) {
				values = new java.util.Vector<>();
				do {
					values.add(TxCfgItem.fromDb(rs1));
				} while (rs1.next());
			}
		}
		return values;
	}

	public static void saveConfig(Properties prop1) throws SQLException {
		final String sqlCfgUpd = "UPDATE tx_cfg_item SET cfg_value=? WHERE cfg_key=?";
		try (PreparedStatement pstmt1 = conn.prepareStatement(sqlCfgUpd)) {
			Iterator<Object> keys = prop1.keySet().iterator();
			while (keys.hasNext()) {
				String key1 = (String)keys.next();
				String val1 = prop1.getProperty(key1);
				pstmt1.setString(1, val1);
				pstmt1.setString(2, key1);
				pstmt1.executeUpdate();
			}
		}
	}

	public static void saveTransactionResponse(int pmRefNo, TransactionResponse resp) 
			throws SQLException {
		ResDB.saveTransactionResponse(conn, pmRefNo, resp);
	}
	
	private static int countOrderForTableId(String sqlStr) throws SQLException {
		int val = 0;
		try (Statement stmt1 = conn.createStatement();) {
			ResultSet rs1 = stmt1.executeQuery(sqlStr);
			if (rs1.next()) {
				val = rs1.getInt("cnt_order");
			}
		}
		return val;
	}

	public static int countUnPaidService(int tblId) throws SQLException {
		String sqlStr = "SELECT COUNT(DISTINCT ord_id) cnt_order "
			+ "FROM tb_order WHERE "
			+ "ord_paid=0 "
			+ "AND tbl_id=%d "
			+ "AND ord_status=1 "
			+ "AND ord_cnt_check = 0";
		return countOrderForTableId(String.format(sqlStr, tblId));
	}

	public static int countUnPaidCheck(int tblId) throws SQLException {
		String sqlStr = "SELECT COUNT(DISTINCT ord_id) cnt_order "
			+ "FROM tb_order WHERE "
			+ "ord_paid=0 "
			+ "AND tbl_id=%d "
			+ "AND ord_status=1 "
			+ "AND ord_cnt_check > 0";
		return countOrderForTableId(String.format(sqlStr, tblId));
	}

	public static TableService getTblServiceById(int tblId) throws SQLException {
		String sqlStr = "SELECT tbl_service FROM tx_sct_table WHERE tbl_id=%d";
		try (Statement stmt1 = conn.createStatement();) {
			ResultSet rs1 = stmt1.executeQuery(String.format(sqlStr, tblId));
			if (rs1.next()) {
				return TableService.valueOf(rs1.getString("tbl_service"));
			}
		}
		return TableService.Ready;
	}

	public static int[] findPrinterByName(String prnName) {
		final String sqlStr1 = "SELECT DISTINCT prn_id FROM tx_printer WHERE prn_name IN (%s)";
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(String.format(sqlStr1, prnName));
		) {
			if (rs1.next()) {
				java.util.Vector<Integer> values = new java.util.Vector<>();
				do {
					values.add(rs1.getInt("prn_id"));
				} while (rs1.next());
				int[] result = new int[values.size()];
				for (int x=0; x < values.size(); x++) {
					result[x] = values.get(x);
				}
				return result;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static TbOrderItem[] getCanceledItems(
				java.util.Date clDtFrom, java.util.Date clDtTo) 
			throws SQLException {
		final String sqlStr1 = 
			"SELECT * "
			+"FROM tb_order_item "
			+"WHERE "
			+"(odi_status <> 1) "
			+ "AND (odi_dt BETWEEN ? AND ?) "
			+"ORDER BY odi_dt";
		try (
			PreparedStatement pstmt1 = conn.prepareStatement(sqlStr1);
		) {
			pstmt1.setTimestamp(1, new java.sql.Timestamp(clDtFrom.getTime()));
			pstmt1.setTimestamp(2, new java.sql.Timestamp(clDtTo.getTime()));
			ResultSet rs1 = pstmt1.executeQuery();
			if (rs1.next()) {
				java.util.Vector<TbOrderItem> odis = 
					new java.util.Vector<TbOrderItem>();
				do {
					odis.addElement(TbOrderItem.fromDb(rs1));
				} while (rs1.next());
				TbOrderItem[] values = new TbOrderItem[odis.size()];
				odis.toArray(values);
				return values;
			}
		}
		return null;
	}

	public static TxSctTable[] getAllTablesExcept(int id) throws SQLException {
		String sqlStr1 = String.format(
			"SELECT a.* "
			+ "FROM tx_sct_table a "
			+   "LEFT OUTER JOIN tx_sct b ON a.sct_id=b.sct_id "
			+ "WHERE "
			+   "b.sct_status=1 "
			+   "AND a.tbl_service IN ('Ready','Service') "
			+   "AND a.tbl_id<>%d "
			+   "AND a.tbl_status=1 "
			+ "ORDER BY a.tbl_name", id);
		return _getTxSctTable(sqlStr1);
	}

	public static void saveTbSettle(TbSettle stl1) throws SQLException {
		ResDB.saveTbSettle(conn, stl1);
	}

	public static TbSettle[] findAllSettlement() 
			throws SQLException {
		final String sqlStr1 = "SELECT * FROM tb_settle "
				+ "WHERE stl_status=1 "
				+ "ORDER BY stl_dt DESC";
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(sqlStr1);
		) {
			if (rs1.next()) {
				java.util.Vector<TbSettle> _vals = new java.util.Vector<>();
				do {
					_vals.add(TbSettle.fromDb(rs1));
				} while (rs1.next());
				TbSettle[] values = new TbSettle[_vals.size()];
				_vals.toArray(values);
				return values;
			}
		}
		return null;
	}

	public static TbShift findPrevShift(TbShift clUp) throws SQLException {
		final String sqlStr1 = "SELECT * FROM tb_shift "
			+ "WHERE  cl_status=1 "
			+ "AND cl_dt < '%s' "
			+ "ORDER BY cl_dt DESC LIMIT 1";
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(
				String.format(sqlStr1, ResUtil.dtoc(clUp.getClDt(), "yyyy-MM-dd HH:mm:ss")));
		) {
			if (rs1.next()) {
				return TbShift.fromDb(rs1);
			}
		}
		return null;
	}
	
	public static TbSettle findPrevSettle(TbSettle stl1) 
			throws SQLException {
		final String sqlStr1 = "SELECT * FROM tb_settle "
				+ "WHERE stl_status=1 "
				+ "AND stl_dt < '%s' "
				+ "ORDER BY stl_dt DESC LIMIT 1";
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(
				String.format(sqlStr1, ResUtil.dtoc(stl1.getStlDt(), "yyyy-MM-dd HH:mm:ss")));
		) {
			if (rs1.next()) {
				return TbSettle.fromDb(rs1);
			}
		}
		return null;
	}

	public static void deleteTbOrder(int _id) throws SQLException {
		final String sqlStr1 = "UPDATE tb_order SET ord_status=2 WHERE ord_id=%d";
		try (
			Statement stmt1 = conn.createStatement();
		) {
			stmt1.executeUpdate(String.format(sqlStr1, _id));
		}
	}

	public static TbPayment[] findPaymentTrans(PaySrc psrc, PayBy pby, Date frmDt, Date toDt) 
			throws SQLException {
		final String sqlStr1 = "SELECT * FROM tb_payment "
			+ "WHERE pm_status=1 "
			+ "AND ref_src='%s' "
			+ "AND pm_pay_by='%s' "
			+ "AND (pm_dt BETWEEN '%s' AND '%s') "
			+ "ORDER BY ref_id, pm_dt";
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(String.format(sqlStr1
				,psrc.toString()
				,pby.toString()
				,ResUtil.dtoc(frmDt, "yyyy-MM-dd HH:mm:ss")
				,ResUtil.dtoc(toDt, "yyyy-MM-dd HH:mm:ss")
				));
		) {
			if (rs1.next()) {
				java.util.Vector<TbPayment> pms = new java.util.Vector<>();
				do {
					pms.add(TbPayment.fromDb(rs1));
				} while (rs1.next());
				TbPayment[] values = new TbPayment[pms.size()];
				pms.toArray(values);
				return values;
			}
		}
		return null;
	}
}


