package app_pos;

import javax.swing.AbstractListModel;

import model.TxDC;

public class LstMdDC extends AbstractListModel<TxDC> {
	private static final long serialVersionUID = 1L;
	
	private java.util.Vector<TxDC> dcs;
	
	public LstMdDC() {
		dcs = new java.util.Vector<>();
	}

	@Override
	public int getSize() {
		return dcs.size();
	}

	@Override
	public TxDC getElementAt(int index) {
		return dcs.get(index);
	}

	public void addElement(TxDC odi1) {
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
	
	public void setItems(java.util.Vector<TxDC> _datas) {
		//dcs.clear();
		dcs = _datas;
		super.fireContentsChanged(this, 0, dcs.size()-1);
	}
	
	/*
	public void clear() {
		dcs.clear();
		super.fireContentsChanged(this, 0, 0);
	}
	*/
}
