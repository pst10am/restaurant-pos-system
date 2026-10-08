package app_pos;

import javax.swing.AbstractListModel;

import model.TbPayment;

public class LstMdTbPayment extends AbstractListModel<TbPayment> {
	private static final long serialVersionUID = 1L;
	
	private java.util.Vector<TbPayment> items;
	
	public LstMdTbPayment() {
		items = new java.util.Vector<>();
	}

	@Override
	public int getSize() {
		return items.size();
	}

	@Override
	public TbPayment getElementAt(int index) {
		return items.get(index);
	}

	public void addElement(TbPayment dc1) {
		items.add(dc1);
		int idx1 = items.size()-1;
		super.fireContentsChanged(this, idx1, idx1);
	}

	public void removeElementAt(int idx1) {
		items.remove(idx1);
		super.fireContentsChanged(this, idx1, idx1);
	}
	
	public void itemHasChanged(int idx) {
		super.fireContentsChanged(this, idx, idx);
	}
	
	public void setItems(TbPayment[] _datas) {
		items.clear();
		if ((null != _datas) && (_datas.length > 0)) {
			for (TbPayment pm1 : _datas) {
				items.add(pm1);
			}
		}
		super.fireContentsChanged(this, 0, items.size()-1);
	}
	
	public java.util.Vector<TbPayment> getItems() {
		return items;
	}
	
	public void validateInfo() {
		super.fireContentsChanged(this, 0, items.size()-1);
	}
	
	public void clear() {
		items.clear();
	}
}
