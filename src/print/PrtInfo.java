package print;

import model.TbOrderItem;
import model.TxPrinter;

class PrtInfo {

	private TxPrinter prt1;
	private java.util.Vector<TbOrderItem> items;
	
	// constructor
	
	private PrtInfo(TxPrinter _prt) {
		prt1 = _prt;
		items = new java.util.Vector<>();
	}
	
	// factory
	
	static PrtInfo newInstance(TxPrinter _prt) {
		PrtInfo prti = new PrtInfo(_prt);
		return prti;
	}

	void addItem(TbOrderItem odi1) {
		items.addElement(odi1);
	}

	String getFileName(int ordId) {
		return String.format("tmp/%d_%d_kitc.tmp", prt1.getPrnId(), ordId);
	}
	
	int getSize() {
		return items.size();
	}

	TbOrderItem getItemAt(int i) {
		return items.get(i);
	}

	String getPrnName() {
		return prt1.getPrnName();
	}
	
	boolean hasBuzzer() {
		return prt1.hasBuzzer();
	}
}
