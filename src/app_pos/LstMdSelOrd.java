package app_pos;

import javax.swing.AbstractListModel;

import model.TbOrder;

public class LstMdSelOrd extends AbstractListModel<TbOrder> {
	private static final long serialVersionUID = 1L;
	
	private java.util.Vector<TbOrder> dcs;
	
	public LstMdSelOrd() {
		dcs = new java.util.Vector<>();
	}

	@Override
	public int getSize() {
		return dcs.size();
	}

	@Override
	public TbOrder getElementAt(int index) {
		return dcs.get(index);
	}

	public void addElement(TbOrder odi1) {
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
	
	public void setItems(TbOrder[] _datas) {
		dcs.clear();
		if (null != _datas) {
			for (TbOrder pxy1 : _datas) {
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
