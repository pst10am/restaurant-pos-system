package print;

public class PrnConst {

	public final static String pESC = ((char) 0x1b) + "";
	public final static String pLF = ((char) 0x0a) + "";
	public final static String p18LF = pESC+ (char)0x49 +"1";
	public final static String pBUZZER = pESC +"|"+ ((char)0x7) ;

	public final static String pLEFT = pESC + "|N"; // |lA
	public final static String pCENTER = pESC + "|cA";
	public final static String pRIGHT = pESC + "|rA";
	
	public final static String pBOLD = pESC + "|bC";
	public final static String pUNDERLINE = pESC + "|uC";
	public final static String pREVERSE = pESC+"|rvC";
	public final static String pITALICS = pESC+"|iC";
	
	public final static String pF1 = pESC + "|1C";
	public final static String pF2 = pESC + "|2C";
	public final static String pF3 = pESC + "|3C";
	public final static String pF4 = pESC + "|4C";
	
	public final static String pF1h = pESC + "|1hC";
	public final static String pF2h = pESC + "|2hC";
	public final static String pF3h = pESC + "|3hC";
	public final static String pF4h = pESC + "|4hC";
	
	public final static String pF1v = pESC + "|1vC";
	public final static String pF2v = pESC + "|2vC";
	public final static String pF3v = pESC + "|3vC";
	public final static String pF4v = pESC + "|4vC";

}
