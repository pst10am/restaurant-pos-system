
odi_id          INT(10) UNSIGNED  (NULL)             NO      PRI     (NULL)   AUTO_INCREMENT  SELECT,INSERT,UPDATE,REFERENCES         

ord_id          INT(10)           (NULL)             NO              (NULL)                   SELECT,INSERT,UPDATE,REFERENCES         
odi_dt          DATETIME          (NULL)             NO              (NULL)                   SELECT,INSERT,UPDATE,REFERENCES         
odi_type        VARCHAR(10)       utf8_unicode_ci    NO              NA                       SELECT,INSERT,UPDATE,REFERENCES         
cat_id          INT(10)           (NULL)             YES             0                        SELECT,INSERT,UPDATE,REFERENCES         
cat_name_pos    VARCHAR(50)       utf8_unicode_ci    YES             (NULL)                   SELECT,INSERT,UPDATE,REFERENCES         
itm_id          INT(10)           (NULL)             YES             0                        SELECT,INSERT,UPDATE,REFERENCES         
itm_name_pos    VARCHAR(75)       latin1_general_ci  YES             (NULL)                   SELECT,INSERT,UPDATE,REFERENCES         
itm_name_web    VARCHAR(75)       utf8_unicode_ci    YES             (NULL)                   SELECT,INSERT,UPDATE,REFERENCES         
itm_bypass_opt  TINYINT(1)        (NULL)             YES             0                        SELECT,INSERT,UPDATE,REFERENCES         
itm_price       DOUBLE            (NULL)             NO              0                        SELECT,INSERT,UPDATE,REFERENCES         
itm_tax         DOUBLE            (NULL)             NO              0                        SELECT,INSERT,UPDATE,REFERENCES         
odi_qty         INT(10)           (NULL)             NO              0                        SELECT,INSERT,UPDATE,REFERENCES         
odi_amt_bf      DOUBLE            (NULL)             NO              0                        SELECT,INSERT,UPDATE,REFERENCES         
odi_amt_tax     DOUBLE            (NULL)             NO              0                        SELECT,INSERT,UPDATE,REFERENCES         
odi_amt_opt     DOUBLE            (NULL)             NO              0                        SELECT,INSERT,UPDATE,REFERENCES         
odi_amt_net     DOUBLE            (NULL)             NO              0                        SELECT,INSERT,UPDATE,REFERENCES         
odi_note        VARCHAR(100)      latin1_general_ci  YES             (NULL)                   SELECT,INSERT,UPDATE,REFERENCES         
odi_extra       DOUBLE            (NULL)             NO              0                        SELECT,INSERT,UPDATE,REFERENCES         
odi_hold        TINYINT(1)        (NULL)             NO              0                        SELECT,INSERT,UPDATE,REFERENCES         
odi_status      INT(2)            (NULL)             NO              0                        SELECT,INSERT,UPDATE,REFERENCES         


ord_id=?, odi_dt=?, odi_type=?
,cat_id=?, cat_name_pos=?, itm_id=?
,itm_name_pos=?, itm_name_web=?, itm_bypass_opt=?
,itm_price=?, itm_tax=?, odi_qty=?
,odi_amt_bf=?, odi_amt_tax=?, odi_amt_opt=?
,odi_amt_net=?, odi_note=?, odi_extra=?
,odi_hold=?, odi_status=?


ord_id, odi_dt, odi_type
,cat_id, cat_name_pos, itm_id
,itm_name_pos, itm_name_web, itm_bypass_opt
,itm_price, itm_tax, odi_qty
,odi_amt_bf, odi_amt_tax, odi_amt_opt
,odi_amt_net, odi_note, odi_extra
,odi_hold, odi_status

+"ord_id=?, odi_dt=?, odi_type=?"
+",cat_id=?, cat_name_pos=?, itm_id=?"
+",itm_name_pos=?, itm_name_web=?, itm_bypass_opt=?"
+",itm_price=?, itm_tax=?, odi_qty=?"
+",odi_amt_bf=?, odi_amt_tax=?, odi_amt_opt=?"
+",odi_amt_net=?, odi_note=?, odi_extra=?"
+",odi_hold=?, odi_status=?"


+"ord_id, odi_dt, odi_type"
+",cat_id, cat_name_pos, itm_id"
+",itm_name_pos, itm_name_web, itm_bypass_opt"
+",itm_price, itm_tax, odi_qty"
+",odi_amt_bf, odi_amt_tax, odi_amt_opt"
+",odi_amt_net, odi_note, odi_extra"
+",odi_hold, odi_status"



OrdId          INT(10)           (NULL)             NO              (NULL)                   SELECT,INSERT,UPDATE,REFERENCES         
OdiDt          DATETIME          (NULL)             NO              (NULL)                   SELECT,INSERT,UPDATE,REFERENCES         
OdiType        VARCHAR(10)       utf8_unicode_ci    NO              NA                       SELECT,INSERT,UPDATE,REFERENCES         
CatId          INT(10)           (NULL)             YES             0                        SELECT,INSERT,UPDATE,REFERENCES         
CatNamePos    VARCHAR(50)       utf8_unicode_ci    YES             (NULL)                   SELECT,INSERT,UPDATE,REFERENCES         
ItmId          INT(10)           (NULL)             YES             0                        SELECT,INSERT,UPDATE,REFERENCES         
ItmNamePos    VARCHAR(75)       latin1_general_ci  YES             (NULL)                   SELECT,INSERT,UPDATE,REFERENCES         
ItmNameWeb    VARCHAR(75)       utf8_unicode_ci    YES             (NULL)                   SELECT,INSERT,UPDATE,REFERENCES         
ItmBypassOpt  TINYINT(1)        (NULL)             YES             0                        SELECT,INSERT,UPDATE,REFERENCES         
ItmPrice       DOUBLE            (NULL)             NO              0                        SELECT,INSERT,UPDATE,REFERENCES         
ItmTax         DOUBLE            (NULL)             NO              0                        SELECT,INSERT,UPDATE,REFERENCES         
OdiQty         INT(10)           (NULL)             NO              0                        SELECT,INSERT,UPDATE,REFERENCES         
OdiAmtBf      DOUBLE            (NULL)             NO              0                        SELECT,INSERT,UPDATE,REFERENCES         
OdiAmtTax     DOUBLE            (NULL)             NO              0                        SELECT,INSERT,UPDATE,REFERENCES         
OdiAmtOpt     DOUBLE            (NULL)             NO              0                        SELECT,INSERT,UPDATE,REFERENCES         
OdiAmtNet     DOUBLE            (NULL)             NO              0                        SELECT,INSERT,UPDATE,REFERENCES         
OdiNote        VARCHAR(100)      latin1_general_ci  YES             (NULL)                   SELECT,INSERT,UPDATE,REFERENCES         
OdiExtra       DOUBLE            (NULL)             NO              0                        SELECT,INSERT,UPDATE,REFERENCES         
OdiHold        TINYINT(1)        (NULL)             NO              0                        SELECT,INSERT,UPDATE,REFERENCES         
OdiStatus      INT(2)            (NULL)             NO              0                        SELECT,INSERT,UPDATE,REFERENCES         


pstmt1.setString(1, odi1.getOrdId());
pstmt1.setString(2, odi1.getOdiDt());
pstmt1.setString(3, odi1.getOdiType());
pstmt1.setString(4, odi1.getCatId());
pstmt1.setString(5, odi1.getCatNamePos());
pstmt1.setString(6, odi1.getItmId());
pstmt1.setString(7, odi1.getItmNamePos());
pstmt1.setString(8, odi1.getItmNameWeb());
pstmt1.setString(9, odi1.getItmBypassOpt());
pstmt1.setString(10, odi1.getItmPrice());
pstmt1.setString(11, odi1.getItmTax());
pstmt1.setString(12, odi1.getOdiQty());
pstmt1.setString(13, odi1.getOdiAmtBf());
pstmt1.setString(14, odi1.getOdiAmtTax());
pstmt1.setString(15, odi1.getOdiAmtOpt());
pstmt1.setString(16, odi1.getOdiAmtNet());
pstmt1.setString(17, odi1.getOdiNote());
pstmt1.setString(18, odi1.getOdiExtra());
pstmt1.setString(19, odi1.getOdiHold());
pstmt1.setString(20, odi1.getOdiStatus());

