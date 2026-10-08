package app_pos;

import javax.swing.AbstractListModel;

import model.TbSettle;

public class LstMdSelSettle extends AbstractListModel<TbSettle> {
	private static final long serialVersionUID = 1L;
	
	private java.util.Vector<TbSettle> dcs;
	
	public LstMdSelSettle() {
		dcs = new java.util.Vector<>();
	}

	@Override
	public int getSize() {
		return dcs.size();
	}

	@Override
	public TbSettle getElementAt(int index) {
		return dcs.get(index);
	}
	
	public void insertElementAt(TbSettle odi1, int idx) {
		if (dcs.contains(odi1)) {
			return;
		}
		dcs.insertElementAt(odi1, idx);
		super.fireContentsChanged(this, idx, idx);
	}

	public void addElement(TbSettle odi1) {
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
	
	public void setItems(TbSettle[] _datas) {
		dcs.clear();
		if (null != _datas) {
			for (TbSettle pxy1 : _datas) {
				dcs.add(pxy1);
			}
		}
		super.fireContentsChanged(this, 0, dcs.size()-1);
	}
	
	public void clear() {
		dcs.clear();
		super.fireContentsChanged(this, 0, 0);
	}
}
