package resrc;

class SqlStr {
	private SqlStr() {}
	
	// tb_order
	
	static final String sqlTbOrderInsert = 
		"INSERT INTO tb_order("
			+"ord_no, ord_dt, ord_type"
			+",ord_togo_type, tbl_id, tbl_name"
			+",tbl_party_size, cst_id, cst_code"
			+",cst_name, cst_phone, cst_email"
			+",cst_note, deli_addr_1, deli_addr_2"
			+",deli_unit_no, deli_city, deli_state"
			+",deli_lat, deli_lon, deli_dist"
			+",deli_fee, ord_cnt_check, ord_cnt_receipt"
			+",ord_amt_bf, ord_amt_tax, ord_amt_disc"
			+",ord_amt_charge, ord_amt_net, ord_paid"
			+",ord_pick, ord_status, ord_pm_change"
		+") VALUES ("
			+"?,?,? "
			+",?,?,? "
			+",?,?,? "
			+",?,?,? "
			+",?,?,? "
			+",?,?,? "
			+",?,?,? "
			+",?,?,? "
			+",?,?,? "
			+",?,?,? "
			+",?,?,? "
		+") ";
	
	static final String sqlTbOrderUpdate = 
		"UPDATE tb_order SET "
			+"ord_no=?, ord_dt=?, ord_type=?"
			+",ord_togo_type=?, tbl_id=?, tbl_name=?"
			+",tbl_party_size=?, cst_id=?, cst_code=?"
			+",cst_name=?, cst_phone=?, cst_email=?"
			+",cst_note=?, deli_addr_1=?, deli_addr_2=?"
			+",deli_unit_no=?, deli_city=?, deli_state=?"
			+",deli_lat=?, deli_lon=?, deli_dist=?"
			+",deli_fee=?, ord_cnt_check=?, ord_cnt_receipt=?"
			+",ord_amt_bf=?, ord_amt_tax=?, ord_amt_disc=?"
			+",ord_amt_charge=?, ord_amt_net=?, ord_paid=?"
			+",ord_pick=?, ord_status=?, ord_pm_change=? "
		+ "WHERE ord_id=%d";
	
	// tb_order_item
	
	static final String sqlTbOrderItemInsert =
		"INSERT INTO tb_order_item ("
			+ "ord_id, odi_dt, odi_type"
			+ ",cat_id, cat_name_pos, itm_id"
			+ ",itm_name_pos, itm_name_web, itm_bypass_opt"
			+ ",itm_price, itm_tax, odi_qty"
			+ ",odi_amt_bf, odi_amt_tax, odi_amt_opt"
			+ ",odi_amt_net, odi_note, odi_extra"
			+ ",odi_hold, odi_printed, odi_status"
			+ ",odi_seq"
		+") VALUES ("
			+ "?,?,? "
			+ ",?,?,? "
			+ ",?,?,? "
			+ ",?,?,? "
			+ ",?,?,? "
			+ ",?,?,? "
			+ ",?,?,? "
			+ ",?"
		+") ";
	
	static final String sqlTbOrderItemUpdate = 
		"UPDATE tb_order_item SET "
			+ "ord_id=?, odi_dt=?, odi_type=?"
			+ ",cat_id=?, cat_name_pos=?, itm_id=?"
			+ ",itm_name_pos=?, itm_name_web=?, itm_bypass_opt=?"
			+ ",itm_price=?, itm_tax=?, odi_qty=?"
			+ ",odi_amt_bf=?, odi_amt_tax=?, odi_amt_opt=?"
			+ ",odi_amt_net=?, odi_note=?, odi_extra=?"
			+ ",odi_hold=?, odi_printed=?, odi_status=?"
			+ ",odi_seq=? "
		+ "WHERE odi_id=%d";

	// tb_order_dc
	
	static final String sqlTbOrderDCInsert = 
			"INSERT INTO tb_order_dc ("
			+ "ord_id, dc_id, dc_desc,"
			+ "dc_type, dc_amt_type, dc_amt,"
			+ "odc_amt, odc_status"
			+ ") VALUES (?,?,?,?,?,?,?,1)";

	// tb_order_item_opi
	
	static final String sqlTbOrderItemOpiInsert = 
			"INSERT INTO tb_order_item_opi ("
			+ "odi_id, opt_id, opi_id, "
			+ "opi_seq, opi_name_pos, opi_name_web, "
			+ "opi_price, odp_status"
			+ ") VALUES ("
			+ "?,?,?,"
			+ "?,?,'',"
			+ "?,1"
			+ ")";
	
	// tb_gift_trx
	
	static final String sqlTbGiftTrxInsert = 
		"INSERT INTO tb_gift_trx ("
		+ "gft_id, tx_dt, tx_type, "
		+ "tx_amt, void_tx_id"
		+ ") VALUES (?,?,?,?,?)";
	
	// tb_payment
	
	static final String sqlTbPaymentInsert = 
		"INSERT INTO tb_payment ( "
		+ "ref_id, pm_dt, pm_pay_by, "
		+ "pm_chk_no, pm_gift_no, pm_amt, "
		+ "pm_tip, pm_status, cc_trk1, "
		+ "cc_trk2, cc_no, cc_acct_name, "
		+ "cc_exp_month, cc_exp_year, cc_cvv, "
		+ "tx_no, tx_result, tx_message,"
		+ "ref_src, ord_no, pm_settle, "
		+ "pm_capture, pm_no "
		+ ") VALUES ("
		+ "?, ?, ?, "
		+ "?, ?, ?, "
		+ "?, ?, ?, "
		+ "?, ?, ?, "
		+ "?, ?, ?, "
		+ "?, ?, ?, "
		+ "?, ?, ?, "
		+ "?, ?)";

	static final String sqlTbPaymentUpdate = 
		"UPDATE tb_payment SET "
		+ "ref_id=?, "
		+ "pm_dt=?, "
		+ "pm_pay_by=?, "
		+ "pm_chk_no=?, "
		+ "pm_gift_no=?, "
		+ "pm_amt=?, "
		+ "pm_tip=?, "
		+ "pm_status=?, "
		+ "cc_trk1=?, "
		+ "cc_trk2=?, "
		+ "cc_no=?, "
		+ "cc_acct_name=?, "
		+ "cc_exp_month=?, "
		+ "cc_exp_year=?, "
		+ "cc_cvv=?, "
		+ "tx_no=?, "
		+ "tx_result=?, "
		+ "tx_message=?,"
		+ "ref_src=?, "
		+ "ord_no=?,"
		+ "pm_settle=?,"
		+ "pm_capture=?,"
		+ "pm_no=? "
		+ "WHERE pm_id=%d";
	
	// tx_del
	
	static final String sqlTxDelInsert = "INSERT INTO tx_del (dl_desc) VALUES (?)";
	static final String sqlTxDelUpdate = "UPDATE tx_del SET dl_desc=? WHERE dl_id=%d";
	
	// tb_gift
	
	static final String sqlTbGiftInsert = 
		"INSERT INTO tb_gift ("
		+ "gft_act_dt, gft_code, gft_balance, "
		+ "gft_status"
		+ ") VALUES (?, ?, ?, ?)";
	static final String sqlTbGiftUpdate = 
		"UPDATE tb_gift SET "
		+ "gft_act_dt=?, gft_code=?, gft_balance=?, "
		+ "gft_status=? "
		+ "WHERE gft_id=%d";
	
	// tx_sct
	
	static final String sqlTxSctInsert = 
		"INSERT INTO tx_sct ("
		+ "sct_seq, sct_name, sct_bg_name, "
		+ "sct_bg_color, sct_width, sct_height, "
		+ "sct_status) VALUES (?,?,?,?,?,?,?)";
	
	static final String sqlTxSctUpdate = 
		"UPDATE tx_sct SET "
		+ "sct_seq=?, sct_name=?, sct_bg_name=?, "
		+ "sct_bg_color=?, sct_width=?, sct_height=?, "
		+ "sct_status=? "
		+ "WHERE sct_id=%d";
	
	// tx_sct_table
	
	static final String sqlTxSctTableInsert = 
		"INSERT INTO tx_sct_table ("
		+ "sct_id, tbl_name, tbl_x, "
		+ "tbl_y, tbl_w, tbl_h, "
		+ "tbl_is_locked, tbl_service, tbl_status"
		+ ") VALUES (?,?,?,?,?,?,?,?,?)";
	
	static final String sqlTxSctTableUpdate = 
		"UPDATE tx_sct_table SET "
		+ "sct_id =?, tbl_name =?, tbl_x =?, "
		+ "tbl_y =?, tbl_w =?, tbl_h =?, "
		+ "tbl_is_locked=?, tbl_service =?, tbl_status =? "
		+ "WHERE tbl_id=%d";
	
	// tx_printer
	
	static final String sqlTxPrinterInsert = 
		"INSERT INTO tx_printer ( "
		+ "prn_ip,prn_name,prn_has_buzzer,"
		+ "prn_status "
		+ ") VALUES (?,?,?,?) ";
	
	static final String sqlTxPrinterUpdate = 
		"UPDATE tx_printer SET "
		+ "prn_ip=?,prn_name=?,prn_has_buzzer=?,"
		+ "prn_status=? "
		+ "WHERE prn_id=%d ";
	
	// tx_opt
	
	static final String sqlTxOptInsert = 
		"INSERT INTO tx_opt ("+
		"opt_seq, opt_name_pos, opt_name_web, "+
		"opt_type, opt_mand, opt_comment, "+
		"opt_status "+
		") VALUES (?,?,?,?,?,?,?) ";
	
	static final String sqlTxOptUpdate =
		"UPDATE tx_opt SET "+
		"opt_seq=?, opt_name_pos=?, opt_name_web=?, "+
		"opt_type=?, opt_mand=?, opt_comment=?, "+
		"opt_status=? "+
		"WHERE opt_id=%d";
	
	// tx_opt_item
	
	static final String sqlTxOptItemInsert = 
		"INSERT INTO tx_opt_item ("+
		"opt_id, opi_seq, opi_name_pos, "+
		"opi_name_web, opi_price, opi_default, "+
		"opi_status "+
		") VALUES (?,?,?,?,?,?,?) ";
	
	static final String sqlTxOptItemUpdate =
		"UPDATE tx_opt_item SET "+
		"opt_id=?, opi_seq=?, opi_name_pos=?, "+
		"opi_name_web=?, opi_price=?, opi_default=?, "+
		"opi_status=? "+
		"WHERE opi_id=%d";
	
	// tx_fd_cat
	
	static final String sqlTxFdCatInsert = 
		"INSERT INTO tx_fd_cat ( "+
		"cat_seq, cat_name_pos, cat_name_web, "+
		"cat_desc, cat_bg_color, cat_status "+
		") VALUES (?,?,?,?,?,?) ";
	
	static final String sqlTxFdCatUpdate =
		"UPDATE tx_fd_cat SET "+
		"cat_seq=?, cat_name_pos=?, cat_name_web=?, "+
		"cat_desc=?, cat_bg_color=?, cat_status=? "+
		"WHERE cat_id=%d ";
	
	// tx_fd_item
	
	static final String sqlTxFdItemInsert = 
		"INSERT INTO tx_fd_item ("+
		"cat_id, itm_seq, itm_name_pos, "+
		"itm_name_web, itm_desc, itm_tax, "+
		"itm_price, itm_bypass_opt, itm_status "+
		") VALUES ("+
		"?,?,?,"+
		"?,?,?,"+
		"?,?,?)";
	
	static final String sqlTxFdItemUpdate =
		"UPDATE tx_fd_item SET "+
		"cat_id=?, itm_seq=?, itm_name_pos=?, "+
		"itm_name_web=?, itm_desc=?, itm_tax=?, "+
		"itm_price=?, itm_bypass_opt=?, itm_status=? "+
		"WHERE itm_id=%d ";
	
	// tx_user
	
	static final String sqlTxUserInsert = 
		"INSERT INTO tx_user ("+
		"usr_name, "+
		"usr_pos_pwd, "+
		"usr_grp, "+
		"is_active "+
		") VALUES (?,?,?,?) ";
	
	static final String sqlTxUserUpdate =
		"UPDATE tx_user SET "+
		"usr_name=?, "+
		"usr_pos_pwd=?, "+
		"usr_grp=?, "+
		"is_active=? "+
		"WHERE usr_id=%d ";

	// tb_shift
	
	static final String sqlTbShiftInsert = 
		"INSERT INTO tb_shift ("
		+ "cl_dt, cl_status"
		+ ") VALUES (?, ?)";
	
	static final String sqlTbShiftUpdate = 
		"UPDATE tb_shift SET "
		+ "cl_dt=?, cl_status=? "
		+ "WHERE cl_id=%d";

	// tb_shift
	
	static final String sqlTbSettleInsert = 
		"INSERT INTO tb_settle ("
		+ "stl_dt, stl_status"
		+ ") VALUES (?, ?)";
	
	static final String sqlTbSettleUpdate = 
		"UPDATE tb_settle SET "
		+ "stl_dt=?, stl_status=? "
		+ "WHERE stl_id=%d";
	
	// tb_cust
	
	static final String sqlTbCustInsert = 
		"INSERT INTO tb_cust ( "
		+"cst_code ,cst_name, cst_email "
		+",cst_phone ,cst_addr_1 ,cst_addr_2 "
		+",cst_unit_no ,cst_city ,cst_state "
		+",cst_lat ,cst_lon ,cst_note "
		+",cst_status "
		+") VALUES ( "
		+"?, ?, ?, "
		+"?, ?, ?, "
		+"?, ?, ?, "
		+"?, ?, ?, "
		+"?) ";
	
	static final String sqlTbCustUpdate = 
		"UPDATE tb_cust SET "
			+"cst_code=? ,cst_name=? ,cst_email=? "
			+",cst_phone=? ,cst_addr_1=? ,cst_addr_2=? "
			+",cst_unit_no=? ,cst_city=? ,cst_state=? "
			+",cst_lat=? ,cst_lon=? ,cst_note=? "
			+",cst_status=? "
		+"WHERE cst_id=%d ";

	static final String sqlUEP_TxResp = "INSERT INTO tb_uep_resp ( "+
			"ref_id ,RefNum ,BatchRefNum "+
			",BatchNum ,Result ,ResultCode "+
			",AuthCode ,AvsResultCode ,AvsResult "+
			",CardCodeResultCode ,CardCodeResult, ErrorCode "+
			",CustNum, Error, AcsUrl "+
			",Payload, VpasResultCode, isDuplicate "+
			",ConvertedAmount, ConvertedAmountCurrency, ConversionRate "+
			",Status, StatusCode "+
		") VALUES ( "+
			"?,?,?, "+
			"?,?,?, "+
			"?,?,?, "+
			"?,?,?, "+
			"?,?,?, "+
			"?,?,?, "+
			"?,?,?, "+
			"?,? "+
		") ";

}
