package app_pos;

import javax.swing.AbstractListModel;

import model.TbOrderDC;
import model.TxDC;

public class LstMdODC extends AbstractListModel<TbOrderDC> {
	private static final long serialVersionUID = 1L;
	
	private java.util.Vector<TbOrderDC> odcs;
	
	public LstMdODC() {
		odcs = new java.util.Vector<>();
	}

	@Override
	public int getSize() {
		return odcs.size();
	}

	@Override
	public TbOrderDC getElementAt(int index) {
		return odcs.get(index);
	}

	public void addElement(TxDC dc1) {
		TbOrderDC odc1 = TbOrderDC.newInstance(dc1);
		if (odcs.contains(odc1)) {
			return;
		}
		odcs.add(odc1);
		int idx1 = odcs.size()-1;
		super.fireContentsChanged(this, idx1, idx1);
	}

	public void removeElementAt(int idx1) {
		odcs.remove(idx1);
		super.fireContentsChanged(this, idx1, idx1);
	}
	
	public void itemHasChanged(int idx) {
		super.fireContentsChanged(this, idx, idx);
	}
	
	public void setItems(java.util.Vector<TbOrderDC> _datas) {
		//dcs.clear();
		odcs = _datas;
		super.fireContentsChanged(this, 0, odcs.size()-1);
	}
	
	/*
	public void clear() {
		dcs.clear();
		super.fireContentsChanged(this, 0, 0);
	}
	*/
}
