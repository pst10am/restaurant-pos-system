package loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/* for old database */
public class MainLoad {
	
	// Connection settings come from system properties (-Dloader.db.url=... etc.)
	private static String db_conn_str = System.getProperty("loader.db.url", "jdbc:mysql://127.0.0.1/pos_legacy_db?autoReconnect=true");
	private static String db_user = System.getProperty("loader.db.user", "pos_user");
	private static String db_pass = System.getProperty("loader.db.pass", "");
	
	private static java.util.Vector<Integer> options = new java.util.Vector<>();
	private static java.util.Vector<Integer> printers = new java.util.Vector<>();
	
	private static Connection conn = null;
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			conn = DriverManager.getConnection(
				db_conn_str,
				db_user,
				db_pass);
			
			conn.setAutoCommit(true);
			
			System.out.printf("> database connected\n> [%s]\n", db_conn_str);
			
		} catch (Exception excp) {
			excp.printStackTrace();
		}
	}
	
	
	static void run1() throws SQLException, FileNotFoundException {
		String outFileName = "c:/temp/current_menu2.txt";
		try (
			PrintWriter pw1 = new PrintWriter(new File(outFileName));
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery("SELECT * FROM wb_cat ORDER BY cat_seq");
		) {
			if (rs1.next()) {
				do {
					int catId = rs1.getInt("cat_id");
					String catName1 = rs1.getString("cat_name_1");
					String catDesc = rs1.getString("cat_desc");
					
					pw1.printf("[CAT]\n%s\n%s\n\n", catName1, catDesc);
					
					getFdItem(catId, pw1);
					
					pw1.printf("\n\n\n");
					
				} while (rs1.next());
				//
				pw1.println("\n*Options:");
				for (int optId : options) {
					ResultSet rs2 = stmt1.executeQuery(String.format("SELECT * FROM wb_opt WHERE opt_id=%d", optId));
					if (rs2.next()) {
						String optName1 = rs2.getString("opt_name_1");
						pw1.printf("   %2d : %s\n", optId, optName1);
					}
				}
				pw1.println("\n*Printers:");
				for (int prnId : printers) {
					ResultSet rs2 = stmt1.executeQuery(String.format("SELECT * FROM wb_printer WHERE prn_id=%d", prnId));
					if (rs2.next()) {
						String prnName = rs2.getString("prn_name");
						pw1.printf("   %2d : %s\n", prnId, prnName);
					}
				}
			}
		}
	}
	
	static void getFdItem(int catId, PrintWriter pw1) throws SQLException {
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(
				String.format("SELECT * FROM wb_item WHERE cat_id=%d ORDER BY itm_seq", catId));
		) {
			if (rs1.next()) {
				do {
					int itmId = rs1.getInt("itm_id");
					String itmName1 = rs1.getString("itm_name_1").trim();
					String itmName4 = rs1.getString("itm_name_4").trim();
					String itmDesc = rs1.getString("itm_desc").trim();
					double itmPrice = rs1.getDouble("itm_price_1");
					
					pw1.printf("   %s\n   %s\n   $%.2f\n   %s\n", 
						itmName1, 
						itmName4.isEmpty() ? itmName1: itmName4, 
						itmPrice,
						itmDesc.isEmpty() ? "-": itmDesc
						);
					
					itemOption(itmId, pw1);
					itemPrinter(itmId, pw1);
					
					pw1.printf("\n");
				} while (rs1.next());
			}
		}
	}
	
	static void itemOption(int itmId, PrintWriter pw1) throws SQLException {
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(
				String.format("SELECT * FROM wb_item_opt WHERE itm_id=%d ORDER BY opt_id", itmId));
		) {
			if (rs1.next()) {
				pw1.print("   Options: ");
				int cnt1 = 0;
				do {
					int optId = rs1.getInt("opt_id");
					pw1.printf("%s%d", 
						cnt1 > 0 ? ", ":"",
						optId);
					cnt1 += 1;
					
					if (!options.contains(optId)) {
						options.addElement(optId);
					}
					
				} while (rs1.next());
				pw1.print("\n");
			}
		}
	}
	
	static void itemPrinter(int itmId, PrintWriter pw1) throws SQLException {
		try (
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(
				String.format("SELECT * FROM wb_item_printer WHERE itm_id=%d ORDER BY prn_id", itmId));
		) {
			if (rs1.next()) {
				pw1.print("   Printers: ");
				int cnt1 = 0;
				do {
					int prnId = rs1.getInt("prn_id");
					pw1.printf("%s%d", 
						cnt1 > 0 ? ", ":"",
						prnId);
					cnt1 += 1;
					
					if (!printers.contains(prnId)) {
						printers.addElement(prnId);
					}
					
				} while (rs1.next());
				pw1.print("\n");
			}
		}
	}
	
	static class FdItem {
		int catId = 0;
		int itmId = 0;
		String itmName1 = "";
		String itmName4 = "";
		String itmDesc = "";
		double itmPrice = 0;
		String options = "";
		String printers = "";
	}
	
	static class FdCat {
		int catId = 0;
		String catName = "";
		String catDesc = "";
		java.util.Vector<FdItem> items = 
			new java.util.Vector<>();
	}
	
	static void loadFromText(String menuFile, boolean saveFlg) 
			throws FileNotFoundException, SQLException {

		java.util.Vector<FdCat> cats = new java.util.Vector<>();
		try (
			Scanner scn1 = new Scanner(new File(menuFile));
		) {
			int dtCnt = 0;
			while (scn1.hasNext()) {
				String line1 = scn1.nextLine().trim();
				if (line1.isEmpty()) {
					if (dtCnt == 1) {
						dtCnt = 2; // item
					}
					if (dtCnt == 2) {
						FdCat cat1 = cats.lastElement();
						if (cat1.items.isEmpty()) {
							cat1.items.addElement(new FdItem());
						}
						FdItem itm1 = cat1.items.lastElement();
						if (!itm1.itmName1.isEmpty()) {
							cat1.items.addElement(new FdItem());
						}
					}
				}
				if ("[CAT]".equals(line1)) {
					FdCat cat1 = new FdCat();
					cats.add(cat1);
					dtCnt = 1; // cat
					continue;
				}
				if (dtCnt == 1) { // cat
					FdCat cat1 = cats.lastElement();
					if (cat1.catName.isEmpty()) {
						cat1.catName = line1;
						System.out.println(cat1.catName);
						continue;
					}
					cat1.catDesc = line1;
				}
				if (dtCnt == 2) { // item
					FdCat cat1 = cats.lastElement();
					FdItem itm1 = cat1.items.lastElement();
					if (itm1.itmName1.isEmpty()) {
						itm1.itmName1 = line1;
						continue;
					}
					if (itm1.itmName4.isEmpty()) {
						itm1.itmName4 = line1;
						continue;
					}
					if (itm1.itmPrice <= 0) {
						itm1.itmPrice = Double.parseDouble(line1.substring(1));
						if (itm1.itmPrice <= 0) {
							System.out.println(" >>> "+ itm1.itmName1);
						}
						continue;
					}
					if (itm1.itmDesc.isEmpty()) {
						itm1.itmDesc = line1;
						continue;
					}
					if (line1.startsWith("Options:")) {
						itm1.options = line1.substring(8);
						continue;
					}
					if (line1.startsWith("Printers:")) {
						itm1.printers = line1.substring(9);
						continue;
					}
				}
			}
		}
		//
		if (!saveFlg || cats.isEmpty()) return;
		saveToDB(cats);
	}
	
	static void saveToDB(java.util.Vector<FdCat> cats) throws SQLException {
		String sqlStr = "INSERT INTO wb_cat ("
			+ "cat_seq, cat_name_1, cat_desc"
			+ ") VALUES (?, ?, ?)";
		try (
			PreparedStatement pstmt1 = conn.prepareStatement(sqlStr,
				Statement.RETURN_GENERATED_KEYS);
		) {
			int seq = 1;
			for (FdCat cat1 : cats) {
				pstmt1.setInt(1, (seq++) * 5);
				pstmt1.setString(2, cat1.catName);
				pstmt1.setString(3, cat1.catDesc);
				pstmt1.execute();
				ResultSet rs1 = pstmt1.getGeneratedKeys();
				if (rs1.next()) {
					cat1.catId = rs1.getInt(1);
				}
				rs1.close();
				//
				saveItems(cat1.catId, cat1.items);
			}
		}
	}
	
	static void saveItems(int catId, java.util.Vector<FdItem> items) throws SQLException {
		String sqlStr = "INSERT INTO wb_item ("
			+ "cat_id, itm_seq, itm_name_1, "
			+ "itm_name_2, itm_name_3, itm_name_4, "
			+ "itm_pic, itm_desc, itm_tax_1, "
			+ "itm_price_1, itm_bypass_opt"
			+ ") VALUES ("
			+ "?, ?, ?, "
			+ "?, ?, ?, "
			+ "?, ?, ?, "
			+ "?, ?)";
		try (
			PreparedStatement pstmt1 = conn.prepareStatement(sqlStr,
				Statement.RETURN_GENERATED_KEYS);
		) {
			int seq = 1;
			for (FdItem itm1 : items) {
				if (itm1.itmName1.isEmpty()) {
					continue;
				}
				itm1.catId = catId;
				pstmt1.setInt(1, itm1.catId);
				pstmt1.setInt(2, (seq++)*5);
				pstmt1.setString(3, itm1.itmName1);
				pstmt1.setString(4, itm1.itmName1);
				pstmt1.setString(5, itm1.itmName1);
				pstmt1.setString(6, itm1.itmName4);
				pstmt1.setString(7, "");
				pstmt1.setString(8, itm1.itmDesc.equals("-")?"":itm1.itmDesc);
				pstmt1.setDouble(9, 10.25);
				pstmt1.setDouble(10, itm1.itmPrice);
				pstmt1.setBoolean(11, itm1.options.isEmpty());
				pstmt1.execute();
				ResultSet rs1 = pstmt1.getGeneratedKeys();
				if (rs1.next()) {
					itm1.itmId = rs1.getInt(1);
				}
				rs1.close();
				//
				if (!itm1.options.isEmpty()) {
					updateOptPrt(itm1, "wb_item_opt", "opt_id", itm1.options.split(","));
				}
				if (!itm1.printers.isEmpty()) {
					updateOptPrt(itm1, "wb_item_printer", "prn_id", itm1.printers.split(","));
				}
			}
		}
	}
	
	static void updateOptPrt(FdItem itm1, String tbName, String fldIdName, String[] values) throws SQLException {
		try (
			Statement stmt1 = conn.createStatement();
		) {
			for (String val : values) {
				stmt1.addBatch(String.format(
					"INSERT INTO %s (itm_id, %s) VALUES (%d, %s)", 
					tbName, fldIdName,
					itm1.itmId, val.trim()));
			}
			stmt1.executeBatch();
		}
	}
	

	static void loadDataFromText() throws FileNotFoundException, SQLException {
		try (Statement stmt1 = conn.createStatement()) {
			stmt1.executeUpdate("TRUNCATE wb_cat");
			stmt1.executeUpdate("TRUNCATE wb_item");
			stmt1.executeUpdate("TRUNCATE wb_item_opt");
			stmt1.executeUpdate("TRUNCATE wb_item_printer");
		}
		loadFromText(System.getProperty("loader.menu.file", "menu.txt"), true);
	}

	public static void main(String[] args) {
		try {
			//run1();
			loadDataFromText();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
