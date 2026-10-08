
TRUNCATE TABLE tx_config;

insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('CITY_LIST','Chicago,Evanston,Aurora','');
insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('DELI_CHECK_BY','AREA','AREA,DISTANCE');

insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('SHOP_NAME','Demo Restaurant','');
insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('SHOP_ADDR','123 Main St, Springfield','');
insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('SHOP_LAT_LON','41.878100,-87.629800','');
insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('SHOP_LINE_1','Demo Restaurant','');
insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('SHOP_LINE_2','123 Main St, Springfield','');
insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('SHOP_LINE_3','Tel.(555)555-0100','');
insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('SHOP_LINE_4','www.example.com','');
insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('SHOP_LINE_5','','');

insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('OPEN_ITEM_TAX','10.25','');
insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('OPEN_PRN_NAME','ENTREE_1','');

insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('PRINT_LOGO','YES','YES,NO');
insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('PRINT_BARCODE','NO','YES,NO');
insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('PRINT_RUNNER','YES','YES,NO');

insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('PHONE_AREA','773,312','');

insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('DEFAULT_UNIT_TYPE','US','US,Metric');
insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('SLIP_GEN_CLASS','print.SlipGenDefault','');

insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('PACKING_PRN_IP','tsp_25','');

insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('SEND_TO_PRINTER','NO','');

insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('THEME','METAL','');
insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('NO_FRAME','NO','');
insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('FULL_SCREEN','NO','');
insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('SCR_WIDTH','1024','');
insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('SCR_HEIGHT','768','');
insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('OPTION_COLUMN','3','');

insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('TRM_PRN_IP','tsp_25','');
insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('TRM_CASH_DRAWER','','');

insert into `tx_config`(`cfg_key`,`cfg_value`,`cfg_cmment`) values ('BC_IP','192.0.2.255','');

COMMIT;
