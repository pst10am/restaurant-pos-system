
/*Table: tx_fd_cat*/

Field         Type              Collation          Null    Key     Default  Extra           Privileges                       Comment
------------  ----------------  -----------------  ------  ------  -------  --------------  -------------------------------  ---------
cat_id        int(10) unsigned  (NULL)             NO      PRI     (NULL)   auto_increment  select,insert,update,references
cat_seq       int(11)           (NULL)             NO              0                        select,insert,update,references
cat_name_pos  varchar(50)       latin1_swedish_ci  NO              (NULL)                   select,insert,update,references
cat_name_web  varchar(50)       latin1_swedish_ci  YES             (NULL)                   select,insert,update,references
cat_desc      varchar(250)      latin1_swedish_ci  YES             (NULL)                   select,insert,update,references
cat_bg_color  varchar(8)        latin1_swedish_ci  YES             (NULL)                   select,insert,update,references
cat_status    int(11)           (NULL)             NO              1                        select,insert,update,references

/*Table: wb_cat*/

Field          Type              Collation          Null    Key     Default  Extra           Privileges                       Comment
-------------  ----------------  -----------------  ------  ------  -------  --------------  -------------------------------  ---------
cat_id         int(10) unsigned  (NULL)             NO      PRI     (NULL)   auto_increment  select,insert,update,references
cat_seq        int(11)           (NULL)             NO              0                        select,insert,update,references
cat_name_1     varchar(50)       latin1_swedish_ci  NO              (NULL)                   select,insert,update,references
cat_desc       varchar(250)      latin1_swedish_ci  YES             (NULL)                   select,insert,update,references
cat_bg_color   varchar(8)        latin1_swedish_ci  YES             #c2e0e0                  select,insert,update,references
cat_for_local  tinyint(1)        (NULL)             YES             0                        select,insert,update,references
cat_for_web    tinyint(1)        (NULL)             YES             0                        select,insert,update,references

TRUNCATE tx_fd_cat;

INSERT INTO tx_fd_cat (
cat_id
,cat_seq
,cat_name_pos
,cat_name_web
,cat_desc
,cat_bg_color
,cat_status
) SELECT
cat_id
,cat_seq
,cat_name_1
,cat_name_1
,cat_desc
,cat_bg_color
,cat_for_web
FROM wb_cat
;



/*Table: tx_fd_item*/

Field           Type              Collation          Null    Key     Default  Extra           Privileges                       Comment
--------------  ----------------  -----------------  ------  ------  -------  --------------  -------------------------------  ---------
cat_id          int(10) unsigned  (NULL)             NO      MUL     (NULL)                   select,insert,update,references
itm_id          int(10) unsigned  (NULL)             NO      PRI     (NULL)   auto_increment  select,insert,update,references
itm_seq         int(11)           (NULL)             NO              0                        select,insert,update,references
itm_name_pos    varchar(75)       latin1_swedish_ci  NO              (NULL)                   select,insert,update,references
itm_name_web    varchar(75)       latin1_swedish_ci  YES             (NULL)                   select,insert,update,references
itm_desc        varchar(250)      latin1_swedish_ci  YES             (NULL)                   select,insert,update,references
itm_tax         double            (NULL)             NO              0                        select,insert,update,references
itm_price       double            (NULL)             NO              0                        select,insert,update,references
itm_bypass_opt  tinyint(1)        (NULL)             YES             0                        select,insert,update,references
itm_status      int(11)           (NULL)             YES             (NULL)                   select,insert,update,references

/*Table: wb_item*/

Field           Type              Collation          Null    Key     Default  Extra           Privileges                       Comment
--------------  ----------------  -----------------  ------  ------  -------  --------------  -------------------------------  ---------
cat_id          int(10) unsigned  (NULL)             NO              (NULL)                   select,insert,update,references
itm_id          int(10) unsigned  (NULL)             NO      PRI     (NULL)   auto_increment  select,insert,update,references
itm_seq         int(11)           (NULL)             NO              0                        select,insert,update,references
itm_name_1      varchar(75)       latin1_swedish_ci  NO              (NULL)                   select,insert,update,references
itm_name_2      varchar(75)       latin1_swedish_ci  YES             (NULL)                   select,insert,update,references
itm_name_3      varchar(75)       latin1_swedish_ci  YES             (NULL)                   select,insert,update,references
itm_name_4      varchar(75)       latin1_swedish_ci  YES             (NULL)                   select,insert,update,references
itm_pic         varchar(100)      latin1_swedish_ci  YES             (NULL)                   select,insert,update,references
itm_desc        varchar(250)      latin1_swedish_ci  YES             (NULL)                   select,insert,update,references
itm_spicy       int(1)            (NULL)             NO              0                        select,insert,update,references
itm_tax_1       double            (NULL)             NO              0                        select,insert,update,references
itm_price_1     double            (NULL)             NO              0                        select,insert,update,references
itm_for_local   tinyint(1)        (NULL)             NO              0                        select,insert,update,references
itm_for_web     tinyint(1)        (NULL)             NO              0                        select,insert,update,references
itm_bypass_opt  tinyint(1)        (NULL)             YES             0                        select,insert,update,references

TRUNCATE tx_fd_item;

INSERT INTO tx_fd_item (
 cat_id
,itm_id
,itm_seq
,itm_name_pos
,itm_name_web
,itm_desc
,itm_tax
,itm_price
,itm_bypass_opt
,itm_status
) SELECT
 cat_id
,itm_id
,itm_seq
,itm_name_1
,itm_name_4
,itm_desc
,itm_tax_1
,itm_price_1
,itm_bypass_opt
,1
FROM wb_item
;



/*Table: tx_opt*/

Field         Type              Collation          Null    Key     Default  Extra           Privileges                       Comment
------------  ----------------  -----------------  ------  ------  -------  --------------  -------------------------------  ---------
opt_id        int(10) unsigned  (NULL)             NO      PRI     (NULL)   auto_increment  select,insert,update,references
opt_seq       int(11)           (NULL)             NO              0                        select,insert,update,references
opt_name_pos  varchar(75)       latin1_swedish_ci  NO              (NULL)                   select,insert,update,references
opt_name_web  varchar(75)       latin1_swedish_ci  YES             (NULL)                   select,insert,update,references
opt_type      varchar(10)       latin1_swedish_ci  NO              (NULL)                   select,insert,update,references
opt_mand      tinyint(1)        (NULL)             NO              0                        select,insert,update,references
opt_comment   varchar(150)      latin1_swedish_ci  YES             (NULL)                   select,insert,update,references
opt_status    int(11)           (NULL)             NO              0                        select,insert,update,references

/*Table: wb_opt*/

Field         Type              Collation          Null    Key     Default  Extra           Privileges                       Comment
------------  ----------------  -----------------  ------  ------  -------  --------------  -------------------------------  ---------
opt_id        int(10) unsigned  (NULL)             NO      PRI     (NULL)   auto_increment  select,insert,update,references
opt_seq       int(11)           (NULL)             YES             (NULL)                   select,insert,update,references
opt_name_1    varchar(75)       latin1_swedish_ci  NO              (NULL)                   select,insert,update,references
opt_multiple  tinyint(1)        (NULL)             YES             0                        select,insert,update,references
opt_comment   varchar(150)      latin1_swedish_ci  YES             (NULL)                   select,insert,update,references

TRUNCATE tx_opt;
INSERT INTO tx_opt (
 opt_id
,opt_seq
,opt_name_pos
,opt_name_web
,opt_type
,opt_mand
,opt_comment
,opt_status
) SELECT
 opt_id
,opt_seq
,opt_name_1
,opt_name_1
,IF (opt_multiple=1,'Multiple','Single') opt_type
,NOT opt_multiple opt_mand
,opt_comment
,1
FROM wb_opt;



/*Table: tx_opt_item*/

Field         Type              Collation          Null    Key     Default  Extra           Privileges                       Comment
------------  ----------------  -----------------  ------  ------  -------  --------------  -------------------------------  ---------
opt_id        int(10) unsigned  (NULL)             NO      MUL     (NULL)                   select,insert,update,references
opi_id        int(10) unsigned  (NULL)             NO      PRI     (NULL)   auto_increment  select,insert,update,references
opi_seq       int(11)           (NULL)             NO              0                        select,insert,update,references
opi_name_pos  varchar(50)       latin1_swedish_ci  NO              (NULL)                   select,insert,update,references
opi_name_web  varchar(50)       latin1_swedish_ci  YES             (NULL)                   select,insert,update,references
opi_price     double            (NULL)             NO              0                        select,insert,update,references
opi_default   tinyint(1)        (NULL)             NO              0                        select,insert,update,references
opi_status    int(11)           (NULL)             NO              0                        select,insert,update,references

/*Table: wb_opt_item*/

Field        Type              Collation          Null    Key     Default  Extra           Privileges                       Comment
-----------  ----------------  -----------------  ------  ------  -------  --------------  -------------------------------  ---------
opt_id       int(10) unsigned  (NULL)             NO              (NULL)                   select,insert,update,references
opi_id       int(10) unsigned  (NULL)             NO      PRI     (NULL)   auto_increment  select,insert,update,references
opi_seq      int(11)           (NULL)             YES             0                        select,insert,update,references
opi_name_1   varchar(50)       latin1_swedish_ci  NO              (NULL)                   select,insert,update,references
opi_price    float             (NULL)             YES             0                        select,insert,update,references
opi_default  tinyint(1)        (NULL)             YES             0                        select,insert,update,references

TRUNCATE tx_opt_item;
INSERT INTO tx_opt_item (
 opt_id
,opi_id
,opi_seq
,opi_name_pos
,opi_name_web
,opi_price
,opi_default
,opi_status
) SELECT
 opt_id
,opi_id
,opi_seq
,opi_name_1
,opi_name_1
,opi_price
,opi_default
,1
FROM wb_opt_item;



/*Table: tx_printer*/

Field           Type              Collation          Null    Key     Default  Extra           Privileges                       Comment
--------------  ----------------  -----------------  ------  ------  -------  --------------  -------------------------------  ---------
prn_id          int(10) unsigned  (NULL)             NO      PRI     (NULL)   auto_increment  select,insert,update,references
prn_ip          varchar(30)       latin1_swedish_ci  NO      MUL     (NULL)                   select,insert,update,references
prn_name        varchar(30)       latin1_swedish_ci  NO              (NULL)                   select,insert,update,references
prn_has_buzzer  tinyint(1)        (NULL)             NO              0                        select,insert,update,references
prn_status      int(11)           (NULL)             NO              1                        select,insert,update,references

/*Table: wb_printer*/
---------------------

Field           Type              Collation          Null    Key     Default  Extra           Privileges                       Comment
--------------  ----------------  -----------------  ------  ------  -------  --------------  -------------------------------  ---------
prn_id          int(10) unsigned  (NULL)             NO      PRI     (NULL)   auto_increment  select,insert,update,references
prn_ip          varchar(20)       latin1_swedish_ci  NO              (NULL)                   select,insert,update,references
prn_name        varchar(30)       latin1_swedish_ci  NO              (NULL)                   select,insert,update,references
prn_ticket_chk  tinyint(1)        (NULL)             NO              0                        select,insert,update,references
prn_buzzer      tinyint(1)        (NULL)             NO              0                        select,insert,update,references
prn_chk_pack    tinyint(1)        (NULL)             YES             0                        select,insert,update,references
prn_chk_dinein  tinyint(1)        (NULL)             YES             0                        select,insert,update,references

TRUNCATE tx_printer;
INSERT INTO tx_printer (
 prn_id
,prn_ip
,prn_name
,prn_has_buzzer
,prn_status
) SELECT
 prn_id
,prn_ip
,prn_name
,prn_buzzer
,1
FROM wb_printer;



TRUNCATE tx_fd_item_opt;
INSERT INTO tx_fd_item_opt (itm_id, opt_id) SELECT itm_id, opt_id FROM wb_item_opt;

TRUNCATE tx_fd_item_printer;
INSERT INTO tx_fd_item_printer (itm_id, prn_id) SELECT itm_id, prn_id FROM wb_item_opt;

COMMIT;




/*Table: tx_sct*/

Field         Type              Collation        Null    Key     Default  Extra           Privileges                       Comment
------------  ----------------  ---------------  ------  ------  -------  --------------  -------------------------------  ---------
sct_id        int(11) unsigned  (NULL)           NO      PRI     (NULL)   auto_increment  select,insert,update,references
sct_seq       int(10) unsigned  (NULL)           YES             0                        select,insert,update,references
sct_name      varchar(30)       utf8_general_ci  NO              (NULL)                   select,insert,update,references
sct_bg_name   varchar(30)       utf8_general_ci  YES             (NULL)                   select,insert,update,references
sct_bg_color  varchar(7)        utf8_general_ci  YES             (NULL)                   select,insert,update,references
sct_width     int(11)           (NULL)           NO              0                        select,insert,update,references
sct_height    int(11)           (NULL)           NO              0                        select,insert,update,references
sct_status    int(11)           (NULL)           NO              1                        select,insert,update,references

/*Table: tb_sect*/

Field      Type              Collation        Null    Key     Default  Extra           Privileges                       Comment
---------  ----------------  ---------------  ------  ------  -------  --------------  -------------------------------  ---------
sect_id    int(11) unsigned  (NULL)           NO      PRI     (NULL)   auto_increment  select,insert,update,references
sect_name  varchar(30)       utf8_general_ci  YES             (NULL)                   select,insert,update,references
sect_bg    int(11)           (NULL)           YES             (NULL)                   select,insert,update,references
is_active  tinyint(1)        (NULL)           YES             (NULL)                   select,insert,update,references

TRUNCATE tx_sct;

INSERT INTO tx_sct (
 sct_id
,sct_seq
,sct_name
,sct_bg_name
,sct_bg_color
,sct_width
,sct_height
,sct_status
) SELECT
 sect_id
,sect_id
,sect_name
,''
,'#D8D8D8'
,1280
,1024
,1
FROM tb_sect;




/*Table: tx_sct_table*/

Field          Type              Collation        Null    Key     Default  Extra           Privileges                       Comment
-------------  ----------------  ---------------  ------  ------  -------  --------------  -------------------------------  ---------
sct_id         int(11) unsigned  (NULL)           NO      MUL     (NULL)                   select,insert,update,references
tbl_id         int(11) unsigned  (NULL)           NO      PRI     (NULL)   auto_increment  select,insert,update,references
tbl_name       varchar(30)       utf8_general_ci  NO              (NULL)                   select,insert,update,references
tbl_x          int(11)           (NULL)           NO              0                        select,insert,update,references
tbl_y          int(11)           (NULL)           NO              0                        select,insert,update,references
tbl_w          int(3)            (NULL)           NO              0                        select,insert,update,references
tbl_h          int(3)            (NULL)           NO              0                        select,insert,update,references
tbl_is_locked  tinyint(1)        (NULL)           NO              0                        select,insert,update,references
tbl_service    varchar(10)       utf8_general_ci  NO              Ready                    select,insert,update,references
tbl_status     int(11)           (NULL)           NO              1                        select,insert,update,references

/*Table: tb_table*/

Field      Type              Collation        Null    Key     Default  Extra           Privileges                       Comment
---------  ----------------  ---------------  ------  ------  -------  --------------  -------------------------------  ---------
sect_id    int(11) unsigned  (NULL)           NO              (NULL)                   select,insert,update,references
tb_id      int(11) unsigned  (NULL)           NO      PRI     (NULL)   auto_increment  select,insert,update,references
tb_name    varchar(30)       utf8_general_ci  YES             (NULL)                   select,insert,update,references
tb_status  varchar(10)       utf8_general_ci  YES             (NULL)                   select,insert,update,references
tb_x       int(11)           (NULL)           YES             (NULL)                   select,insert,update,references
tb_y       int(11)           (NULL)           YES             (NULL)                   select,insert,update,references
is_locked  tinyint(1)        (NULL)           YES             (NULL)                   select,insert,update,references
is_active  tinyint(1)        (NULL)           YES             (NULL)                   select,insert,update,references


TRUNCATE tx_sct_table;

INSERT INTO tx_sct_table (
 sct_id
,tbl_id
,tbl_name
,tbl_x
,tbl_y
,tbl_w
,tbl_h
,tbl_is_locked
,tbl_service
,tbl_status
) SELECT
 sect_id
,tb_id
,tb_name
,tb_x
,tb_y
,65
,65
,0
,'Ready'
,1
FROM tb_table
;






