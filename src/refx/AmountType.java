package refx;

public enum AmountType {
	
	Fixed(11), 
	Percent(22);
	
	private final int aType;
	
	AmountType(int _type) {
		aType = _type;
	}
	
	public String dispStr() {
		if (11 == aType) {
			return "Fixed Amount ($)";
		}
		return "Percentage (%)";
	}
	
	public static AmountType nextVal(AmountType _type) {
		if (AmountType.Fixed == _type) {
			return AmountType.Percent;
		}
		return AmountType.Fixed;
	}
}
