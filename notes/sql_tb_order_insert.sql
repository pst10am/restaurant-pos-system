
ord_id           int(10) unsigned  (NULL)             NO      PRI     (NULL)   auto_increment  select,insert,update,references
ord_no           int(10)           (NULL)             NO              (NULL)                   select,insert,update,references
ord_dt           datetime          (NULL)             NO              (NULL)                   select,insert,update,references
ord_type         varchar(10)       latin1_general_ci  NO              NA                       select,insert,update,references
ord_togo_type    varchar(10)       utf8_unicode_ci    NO              NA                       select,insert,update,references
tbl_id           int(11)           (NULL)             NO              0                        select,insert,update,references
tbl_name         varchar(30)       utf8_unicode_ci    YES             (NULL)                   select,insert,update,references
tbl_party_size   int(3)            (NULL)             NO              0                        select,insert,update,references
cst_id           int(11)           (NULL)             NO              0                        select,insert,update,references
cst_code         varchar(19)       utf8_unicode_ci    YES             (NULL)                   select,insert,update,references
cst_name         varchar(50)       latin1_general_ci  YES             (NULL)                   select,insert,update,references
cst_phone        varchar(50)       latin1_general_ci  YES             (NULL)                   select,insert,update,references
cst_email        varchar(50)       latin1_general_ci  YES             (NULL)                   select,insert,update,references
cst_note         varchar(150)      latin1_general_ci  YES             (NULL)                   select,insert,update,references
deli_addr_1      varchar(50)       latin1_general_ci  YES             (NULL)                   select,insert,update,references
deli_addr_2      varchar(50)       latin1_general_ci  YES             (NULL)                   select,insert,update,references
deli_unit_no     varchar(50)       latin1_general_ci  YES             (NULL)                   select,insert,update,references
deli_city        varchar(50)       latin1_general_ci  YES             (NULL)                   select,insert,update,references
deli_state       varchar(10)       latin1_general_ci  YES             (NULL)                   select,insert,update,references
deli_lat         double            (NULL)             YES             0                        select,insert,update,references
deli_lon         double            (NULL)             YES             0                        select,insert,update,references
deli_dist        double            (NULL)             YES             0                        select,insert,update,references
deli_fee         double            (NULL)             YES             0                        select,insert,update,references
ord_cnt_check    int(10)           (NULL)             NO              0                        select,insert,update,references
ord_cnt_receipt  int(10)           (NULL)             NO              0                        select,insert,update,references
ord_amt_bf       double            (NULL)             NO              0                        select,insert,update,references
ord_amt_tax      double            (NULL)             NO              0                        select,insert,update,references
ord_amt_disc     double            (NULL)             NO              0                        select,insert,update,references
ord_amt_charge   double            (NULL)             NO              0                        select,insert,update,references
ord_amt_net      double            (NULL)             NO              0                        select,insert,update,references
ord_paid         tinyint(1)        (NULL)             NO              0                        select,insert,update,references
ord_pick         tinyint(1)        (NULL)             NO              0                        select,insert,update,references
ord_status       int(2)            (NULL)             NO              0                        select,insert,update,references

OrdId           int(10) unsigned  (NULL)             NO      PRI     (NULL)   auto_increment  select,insert,update,references

OrdNo           int(10)           (NULL)             NO              (NULL)                   select,insert,update,references
OrdDt           datetime          (NULL)             NO              (NULL)                   select,insert,update,references
OrdType         varchar(10)       latin1_general_ci  NO              NA                       select,insert,update,references
OrdTogoType    varchar(10)       utf8_unicode_ci    NO              NA                       select,insert,update,references
TblId           int(11)           (NULL)             NO              0                        select,insert,update,references
TblName         varchar(30)       utf8_unicode_ci    YES             (NULL)                   select,insert,update,references
TblPartySize   int(3)            (NULL)             NO              0                        select,insert,update,references
CstId           int(11)           (NULL)             NO              0                        select,insert,update,references
CstCode         varchar(19)       utf8_unicode_ci    YES             (NULL)                   select,insert,update,references
CstName         varchar(50)       latin1_general_ci  YES             (NULL)                   select,insert,update,references
CstPhone        varchar(50)       latin1_general_ci  YES             (NULL)                   select,insert,update,references
CstEmail        varchar(50)       latin1_general_ci  YES             (NULL)                   select,insert,update,references
CstNote         varchar(150)      latin1_general_ci  YES             (NULL)                   select,insert,update,references
DeliAddr1      varchar(50)       latin1_general_ci  YES             (NULL)                   select,insert,update,references
DeliAddr2      varchar(50)       latin1_general_ci  YES             (NULL)                   select,insert,update,references
DeliUnitNo     varchar(50)       latin1_general_ci  YES             (NULL)                   select,insert,update,references
DeliCity        varchar(50)       latin1_general_ci  YES             (NULL)                   select,insert,update,references
DeliState       varchar(10)       latin1_general_ci  YES             (NULL)                   select,insert,update,references
DeliLat         double            (NULL)             YES             0                        select,insert,update,references
DeliLon         double            (NULL)             YES             0                        select,insert,update,references
DeliDist        double            (NULL)             YES             0                        select,insert,update,references
DeliFee         double            (NULL)             YES             0                        select,insert,update,references
OrdCntCheck    int(10)           (NULL)             NO              0                        select,insert,update,references
OrdCntReceipt  int(10)           (NULL)             NO              0                        select,insert,update,references
OrdAmtBf       double            (NULL)             NO              0                        select,insert,update,references
OrdAmtTax      double            (NULL)             NO              0                        select,insert,update,references
OrdAmtDisc     double            (NULL)             NO              0                        select,insert,update,references
OrdAmtCharge   double            (NULL)             NO              0                        select,insert,update,references
OrdAmtNet      double            (NULL)             NO              0                        select,insert,update,references
OrdPaid         tinyint(1)        (NULL)             NO              0                        select,insert,update,references
OrdPick         tinyint(1)        (NULL)             NO              0                        select,insert,update,references
OrdStatus       int(2)            (NULL)             NO              0                        select,insert,update,references


pstmt1.setString(1, orbj1.getOrdNo());
pstmt1.setString(2, orbj1.getOrdDt());
pstmt1.setString(3, orbj1.getOrdType());
pstmt1.setString(4, orbj1.getOrdTogoType());
pstmt1.setString(5, orbj1.getTblId());
pstmt1.setString(6, orbj1.getTblName());
pstmt1.setString(7, orbj1.getTblPartySize());
pstmt1.setString(8, orbj1.getCstId());
pstmt1.setString(9, orbj1.getCstCode());
pstmt1.setString(10, orbj1.getCstName());
pstmt1.setString(11, orbj1.getCstPhone());
pstmt1.setString(12, orbj1.getCstEmail());
pstmt1.setString(13, orbj1.getCstNote());
pstmt1.setString(14, orbj1.getDeliAddr1());
pstmt1.setString(15, orbj1.getDeliAddr2());
pstmt1.setString(16, orbj1.getDeliUnitNo());
pstmt1.setString(17, orbj1.getDeliCity());
pstmt1.setString(18, orbj1.getDeliState());
pstmt1.setString(19, orbj1.getDeliLat());
pstmt1.setString(20, orbj1.getDeliLon());
pstmt1.setString(21, orbj1.getDeliDist());
pstmt1.setString(22, orbj1.getDeliFee());
pstmt1.setString(23, orbj1.getOrdCntCheck());
pstmt1.setString(24, orbj1.getOrdCntReceipt());
pstmt1.setString(25, orbj1.getOrdAmtBf());
pstmt1.setString(26, orbj1.getOrdAmtTax());
pstmt1.setString(27, orbj1.getOrdAmtDisc());
pstmt1.setString(28, orbj1.getOrdAmtCharge());
pstmt1.setString(29, orbj1.getOrdAmtNet());
pstmt1.setString(20, orbj1.getOrdPaid());
pstmt1.setString(31, orbj1.getOrdPick());
pstmt1.setString(32, orbj1.getOrdStatus());





,ord_no
,ord_dt
,ord_type
,ord_togo_type
,tbl_id
,tbl_name
,tbl_party_size
,cst_id
,cst_code
,cst_name
,cst_phone
,cst_email
,cst_note
,deli_addr_1
,deli_addr_2
,deli_unit_no
,deli_city
,deli_state
,deli_lat
,deli_lon
,deli_dist
,deli_fee
,ord_cnt_check
,ord_cnt_receipt
,ord_amt_bf
,ord_amt_tax
,ord_amt_disc
,ord_amt_charge
,ord_amt_net
,ord_paid
,ord_pick
,ord_status



, ord_no=? , ord_dt=? , ord_type=?
, ord_togo_type=? , tbl_id=? , tbl_name=?
, tbl_party_size=? , cst_id=? , cst_code=?
, cst_name=? , cst_phone=? , cst_email=?
, cst_note=? , deli_addr_1=? , deli_addr_2=?
, deli_unit_no=? , deli_city=? , deli_state=?
, deli_lat=? , deli_lon=? , deli_dist=?
, deli_fee=? , ord_cnt_check=? , ord_cnt_receipt=?
, ord_amt_bf=? , ord_amt_tax=? , ord_amt_disc=?
, ord_amt_charge=? , ord_amt_net=? , ord_paid=?
, ord_pick=? , ord_status=?


, ord_no, ord_dt, ord_type
, ord_togo_type, tbl_id, tbl_name
, tbl_party_size, cst_id, cst_code
, cst_name, cst_phone, cst_email
, cst_note, deli_addr_1, deli_addr_2
, deli_unit_no, deli_city, deli_state
, deli_lat, deli_lon, deli_dist
, deli_fee, ord_cnt_check, ord_cnt_receipt
, ord_amt_bf, ord_amt_tax, ord_amt_disc
, ord_amt_charge, ord_amt_net, ord_paid
, ord_pick, ord_status


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
+",ord_pick=?, ord_status=?"


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
+",ord_pick, ord_status"


