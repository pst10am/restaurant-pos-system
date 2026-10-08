package resrc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import com.usaepay.api.jaxws.TransactionResponse;

import model.TbCust;
import model.TbGift;
import model.TbGiftTrx;
import model.TbOrder;
import model.TbOrderItem;
import model.TbPayment;
import model.TbSettle;
import model.TbShift;
import model.TxDel;
import model.TxFdCat;
import model.TxFdItem;
import model.TxOpt;
import model.TxOptItem;
import model.TxPrinter;
import model.TxSct;
import model.TxSctTable;
import model.TxUser;

class ResDB {
	private ResDB() {}

	static void saveTbOrder(Connection conn, TbOrder ord1) throws SQLException {
		String sqlStr = "";
		int stmtType = -1;
		if (ord1.getOrdId() == 0) {
			stmtType = Statement.RETURN_GENERATED_KEYS;
			sqlStr = SqlStr.sqlTbOrderInsert;
		} else {
			stmtType = Statement.NO_GENERATED_KEYS;
			sqlStr = String.format(SqlStr.sqlTbOrderUpdate, ord1.getOrdId());
		}
		try (
			PreparedStatement pstmt1 = conn.prepareStatement(sqlStr, stmtType);
		) {
			pstmt1.setInt(1, ord1.getOrdNo());
			pstmt1.setTimestamp(2, new java.sql.Timestamp(ord1.getOrdDt().getTime()));
			
			pstmt1.setString(3, ord1.getOrdType().toString());
			pstmt1.setString(4, ord1.getOrdToGoType().toString());
			
			pstmt1.setInt(5, ord1.getTblId());
			pstmt1.setString(6, ord1.getTblName());
			pstmt1.setInt(7, ord1.getPartySize());
			
			pstmt1.setInt(8, ord1.getCstId());
			pstmt1.setString(9, ord1.getCstCode());
			pstmt1.setString(10, ord1.getCstName());
			pstmt1.setString(11, ord1.getCstPhone());
			pstmt1.setString(12, ord1.getCstEmail());
			pstmt1.setString(13, ord1.getCstNote());
			
			pstmt1.setString(14, ord1.getDeliAddr1());
			pstmt1.setString(15, ord1.getDeliAddr2());
			pstmt1.setString(16, ord1.getDeliUnitNo());
			pstmt1.setString(17, ord1.getDeliCity());
			pstmt1.setString(18, ord1.getDeliState());
			pstmt1.setDouble(19, ord1.getDeliLat());
			pstmt1.setDouble(20, ord1.getDeliLon());
			
			pstmt1.setDouble(21, ResUtil.round(ord1.getDeliDist()));
			pstmt1.setDouble(22, ResUtil.round(ord1.getDeliFee()));
			pstmt1.setInt(23, ord1.getOrdCntCheck());
			pstmt1.setInt(24, ord1.getOrdCntReceipt());
			pstmt1.setDouble(25, ResUtil.round(ord1.getOrdAmtBf()));
			pstmt1.setDouble(26, ResUtil.round(ord1.getOrdAmtTax()));
			pstmt1.setDouble(27, ResUtil.round(ord1.getOrdAmtDisc()));
			pstmt1.setDouble(28, ResUtil.round(ord1.getOrdAmtCharge()));
			pstmt1.setDouble(29, ResUtil.round(ord1.getOrdAmtNet()));
			pstmt1.setBoolean(30, ord1.isPaid());
			pstmt1.setBoolean(31, ord1.isPick());
			pstmt1.setInt(32, ord1.getOrdStatus());
			pstmt1.setDouble(33, ResUtil.round(ord1.getOrdPmChange()));
			
			pstmt1.executeUpdate();
			
			if (Statement.RETURN_GENERATED_KEYS == stmtType) {
				ResultSet rs1 = pstmt1.getGeneratedKeys();
				if (rs1.next()) {
					ord1.setOrdId(rs1.getInt(1));
				}
				rs1.close();rs1=null;
			}
		}
	}

	static void saveTbOrderItem(Connection conn, TbOrderItem odi1) throws SQLException {
		String sqlStr = "";
		int stmtType = -1;
		if (odi1.getOdiId() == 0) {
			stmtType = Statement.RETURN_GENERATED_KEYS;
			sqlStr = SqlStr.sqlTbOrderItemInsert;
		} else {
			stmtType = Statement.NO_GENERATED_KEYS;
			sqlStr = String.format(SqlStr.sqlTbOrderItemUpdate, odi1.getOdiId());
		}
		try (
			PreparedStatement pstmt1 = conn.prepareStatement(sqlStr, stmtType);
		) {
			pstmt1.setInt(1, odi1.getOrdId());
			pstmt1.setTimestamp(2, new java.sql.Timestamp(odi1.getOdiDt().getTime()));
			pstmt1.setString(3, odi1.getOdiType().toString());
			pstmt1.setInt(4, odi1.getCatId());
			pstmt1.setString(5, odi1.getCatNamePos());
			pstmt1.setInt(6, odi1.getItmId());
			pstmt1.setString(7, odi1.getItmNamePos());
			pstmt1.setString(8, odi1.getItmNameWeb());
			pstmt1.setBoolean(9, odi1.isBypassOpt());
			pstmt1.setDouble(10, ResUtil.round(odi1.getItmPrice()));
			pstmt1.setDouble(11, ResUtil.round(odi1.getItmTax()));
			pstmt1.setInt(12, odi1.getOdiQty());
			pstmt1.setDouble(13, ResUtil.round(odi1.getOdiAmtBf()));
			pstmt1.setDouble(14, ResUtil.round(odi1.getOdiAmtTax()));
			pstmt1.setDouble(15, ResUtil.round(odi1.getOdiAmtOpt()));
			pstmt1.setDouble(16, ResUtil.round(odi1.getOdiAmtNet()));
			pstmt1.setString(17, odi1.getOdiNote());
			pstmt1.setDouble(18, ResUtil.round(odi1.getOdiExtra()));
			pstmt1.setBoolean(19, odi1.isHold());
			pstmt1.setBoolean(20, odi1.isPrinted());
			pstmt1.setInt(21, odi1.getOdiStatus());
			pstmt1.setInt(22, odi1.getOdiSeq());
			
			pstmt1.executeUpdate();
			
			if (Statement.RETURN_GENERATED_KEYS == stmtType) {
				ResultSet rs1 = pstmt1.getGeneratedKeys();
				if (rs1.next()) {
					odi1.setOdiId(rs1.getInt(1));
				}
				rs1.close();rs1=null;
			}
		}
	}

	static void saveTbGiftTrx(Connection conn, TbGiftTrx tx1) throws SQLException {
		try (
			PreparedStatement pstmt1 = conn.prepareStatement(
				SqlStr.sqlTbGiftTrxInsert, 
				Statement.RETURN_GENERATED_KEYS);
		) {
			pstmt1.setInt(1, tx1.getGftId());
			pstmt1.setTimestamp(2, new java.sql.Timestamp(tx1.getTxDt().getTime()));
			pstmt1.setString(3, tx1.getTxType().toString());
			pstmt1.setDouble(4, tx1.getTxAmt());
			pstmt1.setInt(5, tx1.getVoidTxId());
			
			pstmt1.executeUpdate();
			
			ResultSet rs1 = pstmt1.getGeneratedKeys();
			if (rs1.next()) {
				tx1.setTxId(rs1.getInt(1));
			}
			rs1.close();rs1=null;
		}
	}

	static void saveTbPayment(Connection conn, TbPayment pm1) throws SQLException {
		String sqlStr = "";
		int stmtType = -1;
		if (pm1.getPmId() == 0) {
			stmtType = Statement.RETURN_GENERATED_KEYS;
			sqlStr = SqlStr.sqlTbPaymentInsert;
		} else {
			stmtType = Statement.NO_GENERATED_KEYS;
			sqlStr = String.format(SqlStr.sqlTbPaymentUpdate, pm1.getPmId());
		}
		try (
			PreparedStatement pstmt1 = conn.prepareStatement(sqlStr, stmtType);
		) {
			//System.out.printf("Exp Month [%s], Year [%s]\n", pm1.getCCExpMonth(), pm1.getCCExpYear());
			pstmt1.setInt(1, pm1.getRefId());
			pstmt1.setTimestamp(2, new java.sql.Timestamp(pm1.getPmDt().getTime()));
			pstmt1.setString(3, pm1.getPmPayBy().toString());
			pstmt1.setString(4, pm1.getPmChkNo());
			pstmt1.setString(5, pm1.getPmGiftNo());
			pstmt1.setDouble(6, pm1.getPmAmt());
			pstmt1.setDouble(7, pm1.getPmTip());
			pstmt1.setDouble(8, pm1.getPmStatus());
			// Credit Card
			pstmt1.setString(9, pm1.getCCTrk1());
			pstmt1.setString(10, pm1.getCCTrk2());
			pstmt1.setString(11, pm1.getCCNo());
			pstmt1.setString(12, pm1.getCCAcctName());
			pstmt1.setString(13, pm1.getCCExpMonth());
			pstmt1.setString(14, pm1.getCCExpYear());
			pstmt1.setString(15, pm1.getCCCVV());
			pstmt1.setString(16, pm1.getTxNo());
			pstmt1.setString(17, pm1.getTxResult());
			pstmt1.setString(18, pm1.getTxMessage());
			pstmt1.setString(19, pm1.getRefSrc().toString());
			pstmt1.setInt(20, pm1.getOrdNo());
			pstmt1.setBoolean(21, pm1.isSettle());
			pstmt1.setString(22, pm1.getPmCapture().toString());
			pstmt1.setInt(23, pm1.getPmNo());
			
			pstmt1.executeUpdate();
			
			if (Statement.RETURN_GENERATED_KEYS == stmtType) {
				ResultSet rs1 = pstmt1.getGeneratedKeys();
				if (rs1.next()) {
					pm1.setPmId(rs1.getInt(1));
				}
				rs1.close();rs1=null;
			}
		}
	}

	public static void saveTxDel(Connection conn, TxDel dl1) throws SQLException {
		String sqlStr = "";
		int stmtType = -1;
		if (dl1.getDlId() > 0) {
			stmtType = Statement.NO_GENERATED_KEYS;
			sqlStr = String.format(SqlStr.sqlTxDelUpdate, dl1.getDlId());
		} else {
			stmtType = Statement.RETURN_GENERATED_KEYS;
			sqlStr = SqlStr.sqlTxDelInsert;
		}
		try (
			PreparedStatement pstmt1 = conn.prepareStatement(sqlStr, stmtType);
		) {
			pstmt1.setString(1, dl1.getDlDesc());
			pstmt1.executeUpdate();
			if (Statement.RETURN_GENERATED_KEYS == stmtType) {
				try (ResultSet rs1 = pstmt1.getGeneratedKeys()) {
					if (rs1.next()) {
						dl1.setDlId(rs1.getInt(1));
					}
				}
			}
		}
	}

	public static void saveTbGift(Connection conn, TbGift gft1) throws SQLException {
		String sqlStr = "";
		int stmtType = -1;
		if (gft1.getGftId() > 0) {
			stmtType = Statement.NO_GENERATED_KEYS;
			sqlStr = String.format(SqlStr.sqlTbGiftUpdate, gft1.getGftId());
		} else {
			stmtType = Statement.RETURN_GENERATED_KEYS;
			sqlStr = SqlStr.sqlTbGiftInsert;
		}
		try (
			PreparedStatement pstmt1 = conn.prepareStatement(sqlStr, stmtType);
		) {
			pstmt1.setTimestamp(1, new java.sql.Timestamp(gft1.getGftActDt().getTime()));
			pstmt1.setString(2, gft1.getGftCode());
			pstmt1.setDouble(3, gft1.getGftBal());
			pstmt1.setInt(4, gft1.getGftStatus());
			
			pstmt1.executeUpdate();
			if (Statement.RETURN_GENERATED_KEYS == stmtType) {
				try (ResultSet rs1 = pstmt1.getGeneratedKeys()) {
					if (rs1.next()) {
						gft1.setGftId(rs1.getInt(1));
					}
				}
			}
		}
	}

	public static void saveTxSct(Connection conn, TxSct sct1) throws SQLException {
		String sqlStr = "";
		int stmtType = -1;
		if (sct1.getSctId() > 0) {
			stmtType = Statement.NO_GENERATED_KEYS;
			sqlStr = String.format(SqlStr.sqlTxSctUpdate, sct1.getSctId());
		} else {
			stmtType = Statement.RETURN_GENERATED_KEYS;
			sqlStr = SqlStr.sqlTxSctInsert;
		}
		try (
			PreparedStatement pstmt1 = conn.prepareStatement(sqlStr, stmtType);
		) {
			pstmt1.setInt(1, sct1.getSctSeq());
			pstmt1.setString(2, sct1.getSctName());
			pstmt1.setString(3, sct1.getSctBgName());
			pstmt1.setString(4, sct1.getSctBgColorHexStr());
			pstmt1.setInt(5, sct1.getSctWidth());
			pstmt1.setInt(6, sct1.getSctHeight());
			pstmt1.setInt(7, sct1.getSctStatus());
			
			pstmt1.executeUpdate();
			if (Statement.RETURN_GENERATED_KEYS == stmtType) {
				try (ResultSet rs1 = pstmt1.getGeneratedKeys()) {
					if (rs1.next()) {
						sct1.setSctId(rs1.getInt(1));
					}
				}
			}
		}
	}

	public static void saveTxPict(Connection conn, String _name, byte[] _img) throws SQLException {
		final String sqlStr = "INSERT INTO tx_pict ("
				+ "pct_file_name, pct_file"
				+ ") VALUES (?, ?) "
				+ "ON DUPLICATE KEY UPDATE pct_file=VALUES(pct_file)";
		try (
			PreparedStatement pstmt1 = conn.prepareStatement(sqlStr, 
				Statement.NO_GENERATED_KEYS);
		) {
			pstmt1.setString(1, _name);
			pstmt1.setBytes(2, _img);
			pstmt1.executeUpdate();
		}
	}

	public static void saveTxSctTable(Connection conn, TxSctTable tbl1) throws SQLException {
		String sqlStr = "";
		int stmtType = -1;
		if (tbl1.getTblId() > 0) {
			stmtType = Statement.NO_GENERATED_KEYS;
			sqlStr = String.format(SqlStr.sqlTxSctTableUpdate, tbl1.getTblId());
		} else {
			stmtType = Statement.RETURN_GENERATED_KEYS;
			sqlStr = SqlStr.sqlTxSctTableInsert;
		}
		try (
			PreparedStatement pstmt1 = conn.prepareStatement(sqlStr, stmtType);
		) {
			pstmt1.setInt(1, tbl1.getSctId());
			pstmt1.setString(2, tbl1.getTblName());
			pstmt1.setInt(3, tbl1.getTblX());
			pstmt1.setInt(4, tbl1.getTblY());
			pstmt1.setInt(5, tbl1.getTblW());
			pstmt1.setInt(6, tbl1.getTblH());
			pstmt1.setBoolean(7, tbl1.getIsLocked());
			pstmt1.setString(8, tbl1.getTblService().toString());
			pstmt1.setInt(9, tbl1.getTblStatus());
			pstmt1.executeUpdate();
			if (Statement.RETURN_GENERATED_KEYS == stmtType) {
				try (ResultSet rs1 = pstmt1.getGeneratedKeys()) {
					if (rs1.next()) {
						tbl1.setTblId(rs1.getInt(1));
					}
				}
			}
		}
	}

	public static void saveTxPrinter(Connection conn, TxPrinter prn1) throws SQLException {
		String sqlStr = "";
		int stmtType = -1;
		if (prn1.getPrnId() > 0) {
			stmtType = Statement.NO_GENERATED_KEYS;
			sqlStr = String.format(SqlStr.sqlTxPrinterUpdate, prn1.getPrnId());
		} else {
			stmtType = Statement.RETURN_GENERATED_KEYS;
			sqlStr = SqlStr.sqlTxPrinterInsert;
		}
		try (
			PreparedStatement pstmt1 = conn.prepareStatement(sqlStr, stmtType);
		) {
			pstmt1.setString(1, prn1.getPrnIp());
			pstmt1.setString(2, prn1.getPrnName());
			pstmt1.setBoolean(3, prn1.hasBuzzer());
			pstmt1.setInt(4, prn1.getPrnStatus());
			pstmt1.executeUpdate();
			if (Statement.RETURN_GENERATED_KEYS == stmtType) {
				try (ResultSet rs1 = pstmt1.getGeneratedKeys()) {
					if (rs1.next()) {
						prn1.setPrnId(rs1.getInt(1));
					}
				}
			}
		}
	}

	public static void saveTxOpt(Connection conn, TxOpt opt1) throws SQLException {
		String sqlStr = "";
		int stmtType = -1;
		if (opt1.getOptId() > 0) {
			stmtType = Statement.NO_GENERATED_KEYS;
			sqlStr = String.format(SqlStr.sqlTxOptUpdate, opt1.getOptId());
		} else {
			stmtType = Statement.RETURN_GENERATED_KEYS;
			sqlStr = SqlStr.sqlTxOptInsert;
		}
		try (
			PreparedStatement pstmt1 = conn.prepareStatement(sqlStr, stmtType);
		) {
			
			pstmt1.setInt(1, opt1.getOptSeq());
			pstmt1.setString(2, opt1.getOptNamePos());
			pstmt1.setString(3, opt1.getOptNameWeb());
			pstmt1.setString(4, opt1.getOptType().toString());
			pstmt1.setBoolean(5, opt1.isMandatory());
			pstmt1.setString(6, opt1.getOptComment());
			pstmt1.setInt(7, opt1.getOptStatus());
			
			pstmt1.executeUpdate();
			if (Statement.RETURN_GENERATED_KEYS == stmtType) {
				try (ResultSet rs1 = pstmt1.getGeneratedKeys()) {
					if (rs1.next()) {
						opt1.setOptId(rs1.getInt(1));
					}
				}
			}
		}
	}

	public static void saveTxOptItem(Connection conn, TxOptItem opi1) throws SQLException {
		String sqlStr = "";
		int stmtType = -1;
		if (opi1.getOpiId() > 0) {
			stmtType = Statement.NO_GENERATED_KEYS;
			sqlStr = String.format(SqlStr.sqlTxOptItemUpdate, opi1.getOpiId());
		} else {
			stmtType = Statement.RETURN_GENERATED_KEYS;
			sqlStr = SqlStr.sqlTxOptItemInsert;
		}
		try (
			PreparedStatement pstmt1 = conn.prepareStatement(sqlStr, stmtType);
		) {
			
			pstmt1.setInt(1, opi1.getOptId());
			pstmt1.setInt(2, opi1.getOpiSeq());
			pstmt1.setString(3, opi1.getOpiNamePos());
			pstmt1.setString(4, opi1.getOpiNameWeb());
			pstmt1.setDouble(5, opi1.getOpiPrice());
			pstmt1.setBoolean(6, opi1.isOpiDefault());
			pstmt1.setInt(7, opi1.getOpiStatus());
			
			pstmt1.executeUpdate();
			if (Statement.RETURN_GENERATED_KEYS == stmtType) {
				try (ResultSet rs1 = pstmt1.getGeneratedKeys()) {
					if (rs1.next()) {
						opi1.setOpiId(rs1.getInt(1));
					}
				}
			}
		}
	}

	public static void saveTxCat(Connection conn, TxFdCat cat1) throws SQLException {
		String sqlStr = "";
		int stmtType = -1;
		if (cat1.getCatId() > 0) {
			stmtType = Statement.NO_GENERATED_KEYS;
			sqlStr = String.format(SqlStr.sqlTxFdCatUpdate, cat1.getCatId());
		} else {
			stmtType = Statement.RETURN_GENERATED_KEYS;
			sqlStr = SqlStr.sqlTxFdCatInsert;
		}
		try (
			PreparedStatement pstmt1 = conn.prepareStatement(sqlStr, stmtType);
		) {
			
			pstmt1.setInt(1, cat1.getCatSeq());
			pstmt1.setString(2, cat1.getCatNamePos());
			pstmt1.setString(3, cat1.getCatNameWeb());
			pstmt1.setString(4, cat1.getCatDesc());
			pstmt1.setString(5, ResUtil.toHexStr(cat1.getCatBgColor()));
			pstmt1.setInt(6, cat1.getCatStatus());
			
			pstmt1.executeUpdate();
			if (Statement.RETURN_GENERATED_KEYS == stmtType) {
				try (ResultSet rs1 = pstmt1.getGeneratedKeys()) {
					if (rs1.next()) {
						cat1.setCatId(rs1.getInt(1));
					}
				}
			}
		}
	}

	public static void saveTxFdItem(Connection conn, TxFdItem itm1) throws SQLException {
		String sqlStr = "";
		int stmtType = -1;
		if (itm1.getItmId() > 0) {
			stmtType = Statement.NO_GENERATED_KEYS;
			sqlStr = String.format(SqlStr.sqlTxFdItemUpdate, itm1.getItmId());
		} else {
			stmtType = Statement.RETURN_GENERATED_KEYS;
			sqlStr = SqlStr.sqlTxFdItemInsert;
		} 
		try (
			PreparedStatement pstmt1 = conn.prepareStatement(sqlStr, stmtType);
		) {
			pstmt1.setInt(1, itm1.getCatId());
			pstmt1.setInt(2, itm1.getItmSeq());
			pstmt1.setString(3, itm1.getItmNamePos());
			pstmt1.setString(4, itm1.getItmNameWeb());
			pstmt1.setString(5, itm1.getItmDesc());
			pstmt1.setDouble(6, itm1.getItmTax());
			pstmt1.setDouble(7, itm1.getItmPrice());
			pstmt1.setBoolean(8, itm1.isBypassOpt());
			pstmt1.setInt(9, itm1.getItmStatus());
			
			pstmt1.executeUpdate();
			if (Statement.RETURN_GENERATED_KEYS == stmtType) {
				try (ResultSet rs1 = pstmt1.getGeneratedKeys()) {
					if (rs1.next()) {
						itm1.setItmId(rs1.getInt(1));
					}
				}
			}
		}
	}

	public static void saveTxUser(Connection conn, TxUser usr1) throws SQLException {
		String sqlStr = "";
		int stmtType = -1;
		if (usr1.getUsrId() > 0) {
			stmtType = Statement.NO_GENERATED_KEYS;
			sqlStr = String.format(SqlStr.sqlTxUserUpdate, usr1.getUsrId());
		} else {
			stmtType = Statement.RETURN_GENERATED_KEYS;
			sqlStr = SqlStr.sqlTxUserInsert;
		}
		try (
			PreparedStatement pstmt1 = conn.prepareStatement(sqlStr, stmtType);
		) {
			pstmt1.setString(1, usr1.getUsrName());
			pstmt1.setString(2, usr1.getUsrPosPWD());
			pstmt1.setString(3, usr1.getUsrGrp().toString());
			pstmt1.setInt(4, usr1.isActive()?1:0);
			
			pstmt1.executeUpdate();
			if (Statement.RETURN_GENERATED_KEYS == stmtType) {
				try (ResultSet rs1 = pstmt1.getGeneratedKeys()) {
					if (rs1.next()) {
						usr1.setUsrId(rs1.getInt(1));
					}
				}
			}
		}
	}

	public static void saveTbShift(Connection conn, TbShift cl1) throws SQLException {
		String sqlStr = "";
		int stmtType = -1;
		if (cl1.getClId() > 0) {
			stmtType = Statement.NO_GENERATED_KEYS;
			sqlStr = String.format(SqlStr.sqlTbShiftUpdate, cl1.getClId());
		} else {
			stmtType = Statement.RETURN_GENERATED_KEYS;
			sqlStr = SqlStr.sqlTbShiftInsert;
		}
		try (
			PreparedStatement pstmt1 = conn.prepareStatement(sqlStr, stmtType);
		) {
			pstmt1.setTimestamp(1, new java.sql.Timestamp(cl1.getClDt().getTime()));
			pstmt1.setInt(2, cl1.getClStatus());
			
			pstmt1.executeUpdate();
			if (Statement.RETURN_GENERATED_KEYS == stmtType) {
				try (ResultSet rs1 = pstmt1.getGeneratedKeys()) {
					if (rs1.next()) {
						cl1.setClId(rs1.getInt(1));
					}
				}
			}
		}
	}

	public static void saveTbCust(Connection conn, TbCust cst1) throws SQLException {
		String sqlStr = "";
		int stmtType = -1;
		if (cst1.getCstId() > 0) {
			stmtType = Statement.NO_GENERATED_KEYS;
			sqlStr = String.format(SqlStr.sqlTbCustUpdate, cst1.getCstId());
		} else {
			stmtType = Statement.RETURN_GENERATED_KEYS;
			sqlStr = SqlStr.sqlTbCustInsert;
		}
		try (
			PreparedStatement pstmt1 = conn.prepareStatement(sqlStr, stmtType);
		) {
			pstmt1.setString(1, cst1.getCstCode());
			pstmt1.setString(2, cst1.getCstName());
			pstmt1.setString(3, cst1.getCstEmail());
			pstmt1.setString(4, cst1.getCstPhone());
			pstmt1.setString(5, cst1.getCstAddr1());
			pstmt1.setString(6, cst1.getCstAddr2());
			pstmt1.setString(7, cst1.getCstUnitNo());
			pstmt1.setString(8, cst1.getCstCity());
			pstmt1.setString(9, cst1.getCstState());
			pstmt1.setDouble(10, cst1.getCstLat());
			pstmt1.setDouble(11, cst1.getCstLon());
			pstmt1.setString(12, cst1.getCstNote());
			pstmt1.setInt(13, cst1.getCstStatus());
			
			pstmt1.executeUpdate();
			if (Statement.RETURN_GENERATED_KEYS == stmtType) {
				try (ResultSet rs1 = pstmt1.getGeneratedKeys()) {
					if (rs1.next()) {
						cst1.setCstId(rs1.getInt(1));
					}
				}
			}
		}
	}

	public static void saveTransactionResponse(Connection conn,
			int refId, TransactionResponse resp) throws SQLException {
		try (
			PreparedStatement pstmt1 = 
				conn.prepareStatement(SqlStr.sqlUEP_TxResp, 
					Statement.NO_GENERATED_KEYS);
		) {
			pstmt1.setInt(1, refId);
			pstmt1.setInt(2, resp.getRefNum().intValue());
			pstmt1.setInt(3, resp.getBatchRefNum().intValue());
			pstmt1.setInt(4, resp.getBatchNum().intValue());
			pstmt1.setString(5, resp.getResult());
			pstmt1.setString(6, resp.getResultCode());
			pstmt1.setString(7, resp.getAuthCode());
			pstmt1.setString(8, resp.getAvsResultCode());
			pstmt1.setString(9, resp.getAvsResult());
			pstmt1.setString(10, resp.getCardCodeResultCode());
			pstmt1.setString(11, resp.getCardCodeResult());
			pstmt1.setInt(12, resp.getErrorCode().intValue());
			pstmt1.setInt(13, resp.getCustNum().intValue());
			pstmt1.setString(14, resp.getError());
			pstmt1.setString(15, resp.getAcsUrl());
			pstmt1.setString(16, resp.getPayload());
			pstmt1.setString(17, resp.getVpasResultCode());
			pstmt1.setBoolean(18, resp.isIsDuplicate());
			pstmt1.setDouble(19, resp.getConvertedAmount());
			pstmt1.setString(20, resp.getConvertedAmountCurrency());
			pstmt1.setDouble(21, resp.getConversionRate());
			pstmt1.setString(22, resp.getStatus());
			pstmt1.setString(23, resp.getStatusCode());
			
			pstmt1.executeUpdate();
		}
	}

	public static void saveTbSettle(Connection conn, TbSettle stl1) 
			throws SQLException {
		
		String sqlStr = "";
		int stmtType = -1;
		if (stl1.getStlId() > 0) {
			stmtType = Statement.NO_GENERATED_KEYS;
			sqlStr = String.format(SqlStr.sqlTbSettleUpdate, stl1.getStlId());
		} else {
			stmtType = Statement.RETURN_GENERATED_KEYS;
			sqlStr = SqlStr.sqlTbSettleInsert;
		}
		try (
			PreparedStatement pstmt1 = conn.prepareStatement(sqlStr, stmtType);
		) {
			pstmt1.setTimestamp(1, new java.sql.Timestamp(stl1.getStlDt().getTime()));
			pstmt1.setInt(2, stl1.getStlStatus());
			
			pstmt1.executeUpdate();
			if (Statement.RETURN_GENERATED_KEYS == stmtType) {
				try (ResultSet rs1 = pstmt1.getGeneratedKeys()) {
					if (rs1.next()) {
						stl1.setStlId(rs1.getInt(1));
					}
				}
			}
		}
	}
}
