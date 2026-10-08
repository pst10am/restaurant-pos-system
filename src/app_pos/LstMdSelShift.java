package app_pos;

import javax.swing.AbstractListModel;

import model.TbShift;

public class LstMdSelShift extends AbstractListModel<TbShift> {
	private static final long serialVersionUID = 1L;
	
	private java.util.Vector<TbShift> dcs;
	
	public LstMdSelShift() {
		dcs = new java.util.Vector<>();
	}

	@Override
	public int getSize() {
		return dcs.size();
	}

	@Override
	public TbShift getElementAt(int index) {
		return dcs.get(index);
	}
	
	public void insertElementAt(TbShift odi1, int idx) {
		if (dcs.contains(odi1)) {
			return;
		}
		dcs.insertElementAt(odi1, idx);
		super.fireContentsChanged(this, idx, idx);
	}

	public void addElement(TbShift odi1) {
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
	
	public void setItems(TbShift[] _datas) {
		dcs.clear();
		if (null != _datas) {
			for (TbShift pxy1 : _datas) {
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
