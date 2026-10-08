package app_pos;

import javax.swing.AbstractListModel;

import model.TxDel;

public class LstMdTxDel extends AbstractListModel<TxDel> {
	private static final long serialVersionUID = 1L;
	
	private java.util.Vector<TxDel> dcs;
	
	public LstMdTxDel() {
		dcs = new java.util.Vector<>();
	}

	@Override
	public int getSize() {
		return dcs.size();
	}

	@Override
	public TxDel getElementAt(int index) {
		return dcs.get(index);
	}

	public void addElement(TxDel odi1) {
		if (dcs.contains(odi1)) {
			return;
		}
		dcs.add(odi1);
		int idx1 = dcs.size()-1;
		super.fireContentsChanged(this, idx1, idx1);
	}

	public void removeElementAt(int idx1) {
		dcs.remove(idx1);
		super.fireContentsChanged(this, idx1, idx1);
	}
	
	public void itemHasChanged(int idx) {
		super.fireContentsChanged(this, idx, idx);
	}
	
	public void setItems(TxDel[] _datas) {
		if (null == _datas) {
			super.fireContentsChanged(this, 0, dcs.size()-1);
			return;
		}
		//
		dcs = new java.util.Vector<>();
		for (TxDel dl1 : _datas) {
			dcs.add(dl1);
		}
		super.fireContentsChanged(this, 0, dcs.size()-1);
	}
	
	public void clear() {
		dcs.clear();
		super.fireContentsChanged(this, 0, 0);
	}
}
