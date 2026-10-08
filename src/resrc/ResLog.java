package resrc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import resrc.ResUtil;

public class ResLog {
	private ResLog() {}
	
	private static File logFile = null;
	
	static {
		File logPath = new File("log");
		if (!logPath.exists() || !logPath.isDirectory()) {
			logPath.mkdir();
		}
		java.util.Date cdate = new java.util.Date();
		logFile = new File(String.format("log/log_%s.txt", 
			ResUtil.dtoc(cdate, "yyMMdd")));
	}
	
	public static void append(String msg) {
		java.util.Date cdate = new java.util.Date();
		String _msg = String.format("%s\n%s\n-----\n", 
			ResUtil.dtoc(cdate, "yyyy-MM-dd HH:mm:ss"), msg);
		try (
			FileWriter pw = new FileWriter(logFile, true)
		) {
			pw.append(_msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
