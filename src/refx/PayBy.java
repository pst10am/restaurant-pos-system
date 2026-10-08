package refx;

public enum PayBy {
	
	NA ("NA", "NA")
	, Cash ("Ca", "Cash :-)")
	, CreditCard ("CC", "<html>Credit Card<br><font size=3>Input [Pay Amount], swipe the card or manually input card info.</font></html>")
	, Check ("Ck", "<html>Gift Card<br><font size=3>Input [Pay Amount], Pay to [<b>%RESTAURANT NAME%</b>].</font></html>")
	, GiftCard ("Gf", "<html>Gift Card<br><font size=3>Input [Pay Amount], swipe the gift card or manually input gift card no.</font></html>");
	
	public static final int size = PayBy.values().length;
	private final String abrv, dispName;
	
	PayBy(String _abrv, String _name) {
		this.abrv = _abrv;
		this.dispName = _name;
	}
	
	public String getDispName() {
		return this.dispName;
	}
	
	public String abrv() {
		return this.abrv;
	}
}
