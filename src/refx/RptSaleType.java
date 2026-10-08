package refx;

public enum RptSaleType {
	
	DineIn (OrderType.DineIn, ToGoType.NA), 
	PickUp (OrderType.ToGo, ToGoType.PickUp), 
	Waiting (OrderType.ToGo, ToGoType.Waiting), 
	Delivery (OrderType.ToGo, ToGoType.Delivery), 
	WebPickUp (OrderType.ToGo, ToGoType.NA), 
	WebDelivery (OrderType.ToGo, ToGoType.NA), 
	Other (OrderType.NA, ToGoType.NA);
	
	public static final int size = RptSaleType.values().length;
	
	private final OrderType ordType;
	private final ToGoType togoType;
	
	RptSaleType(OrderType _ord, ToGoType _togo) {
		ordType = _ord;
		togoType = _togo;
	}
	
	public OrderType getOrderType() {
		return ordType;
	}
	
	public ToGoType getToGoType() {
		return togoType;
	}
	
	public static RptSaleType getRptSaleType(OrderType _ordType, ToGoType _togoType) {
		if (OrderType.DineIn == _ordType) {
			return RptSaleType.DineIn;
		}
		if (OrderType.ToGo == _ordType) {
			if (ToGoType.PickUp == _togoType) {
				return RptSaleType.PickUp;
			}
			if (ToGoType.Waiting == _togoType) {
				return RptSaleType.Waiting;
			}
			if (ToGoType.Delivery == _togoType) {
				return RptSaleType.Delivery;
			}
		}
		return RptSaleType.Other;
	}
}
