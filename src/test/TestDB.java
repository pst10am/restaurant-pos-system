package test;

import java.sql.SQLException;

import model.TbSettle;
import resrc.ResData;

public class TestDB {
	
	static void testSettle() {
		try {
			TbSettle.saveInstance();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		{
			ResData.status();
		}
		testSettle();
	}

}
