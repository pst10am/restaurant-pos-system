package model;

public class RptSaleInfo {
	
	public static int gtOrder = 0;
	public static double gtSumAmtBf = 0;
	public static double gtSumAmtTax = 0;
	public static double gtSumAmtDeliFee = 0;
	public static double gtSumAmtDisc = 0;
	public static double gtSumAmtCharge = 0;
	public static double gtSumAmtNet = 0;
	public static double gtSumPmChange = 0;
	
	public int frmOrdNo = -1;
	public int toOrdNo = -1;
	public int totalOrder = 0;
	public double sumAmtBf = 0;
	public double sumAmtTax = 0;
	public double sumAmtDeliFee = 0;
	public double sumAmtDisc = 0;
	public double sumAmtCharge = 0;
	public double sumAmtNet = 0;
	public double sumPmChange = 0;

	public static void init() {
		gtOrder = 0;
		gtSumAmtBf = 0;
		gtSumAmtTax = 0;
		gtSumAmtDeliFee = 0;
		gtSumAmtDisc = 0;
		gtSumAmtCharge = 0;
		gtSumAmtNet = 0;
		gtSumPmChange = 0;
	}
}
