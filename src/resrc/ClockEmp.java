package resrc;

import java.sql.SQLException;

import refx.ClockType;
import model.TbClock;
import model.TbShift;

public class ClockEmp {

	public static ClockType getLastClock(int usrId) throws SQLException {
		TbShift cl1 = TbShift.findLastShift();
		return ResData.getLastClock(cl1.getClDt(), usrId);
	}

	public static void clock(TbClock clk1) throws SQLException {
		ResData.clockEmp(clk1);
	}
}
