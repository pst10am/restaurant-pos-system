package test;

import static print.PrnConst.pBOLD;
import static print.PrnConst.pCENTER;
import static print.PrnConst.pESC;
import static print.PrnConst.pF1;
import static print.PrnConst.pF3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import print.StarPrinter;
import refx.PaySrc;
import resrc.ResCfg;
import resrc.ResData;
import resrc.ResUtil;

public class TestPrinter {

	public static void main(String[] args) {
		ResData.status();
		File pf1 = new File("tmp/2_test.tmp");
		try (
			PrintWriter pw1 = new PrintWriter(pf1);
		) {
			pw1.println(pESC + "|3lF");
			for (int k=0; k < 2; k++) {
				for (int i=1; i <= ResCfg.getTotalShopLine(); i++) {
					if (ResCfg.getShopLine(i).trim().isEmpty()) {
						continue;
					}
					pw1.println(pF1+pBOLD+pCENTER+ ResCfg.getShopLine(i));
				}
				pw1.println();
				pw1.println(pBOLD+pF3+ "Order No. <TEST>");
				pw1.println(pBOLD+ "Date-Time: "+ ResUtil.dtoc(new java.util.Date(), "MM/dd/YYYY HH:mm:ss"));
				pw1.println( "================================================");
				pw1.println( "<AcctNo> ... <5678>");
				pw1.println( ResUtil.padl("Amount",38,' ')+String.format("%10.2f", 55.3));
				pw1.println();
				pw1.println( "Tip                            _________________");
				pw1.println();
				pw1.println( "Total                          _________________");
				pw1.println();
				pw1.println( "Signature  _____________________________________");
				pw1.println( "================================================");
				pw1.println( (k==0 ? "Restaurant Copy" : "Customer Copy"));
				pw1.println(pESC + "|3lF");
				if (k==0) {
					pw1.println(pESC + "|100fP");
				}
				pw1.println(pESC + "|2lF");
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		StarPrinter prn1 = StarPrinter.newInstance();
		prn1.addFile(pf1);
		try {
			prn1.startPrint();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
