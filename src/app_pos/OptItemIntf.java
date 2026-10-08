package app_pos;

import model.TxOpt;
import model.TxOptItem;

public interface OptItemIntf {

	public TxOpt getTxOpt();
	public TxOptItem getTxOptItem();
	public boolean isSelected();
}
