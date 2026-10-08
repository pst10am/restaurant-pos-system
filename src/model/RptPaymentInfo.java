package model;

public class RptPaymentInfo {
	
	public static int gtTotalPayment = 0;
	public static double gtSumPmAmt = 0;
	public static double gtSumPmTip = 0;
	
	public int totalPayment = 0;
	public double sumPmAmt = 0;
	public double sumPmTip = 0;

	public static void init() {
		gtTotalPayment = 0;
		gtSumPmAmt = 0;
		gtSumPmTip = 0;
	}
}

