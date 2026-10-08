package app_pos;

import model.CCData;

public interface MSRCCIntf {
	
	public void creditCardSwiped(CCData crdDt);
	public void cardReadError();
}
