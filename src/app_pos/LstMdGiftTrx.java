package app_pos;

import javax.swing.AbstractListModel;

import model.TbGiftTrx;

public class LstMdGiftTrx extends AbstractListModel<TbGiftTrx> {
	private static final long serialVersionUID = 1L;
	
	private java.util.Vector<TbGiftTrx> items;
	
	public LstMdGiftTrx() {
		items = new java.util.Vector<>();
	}

	@Override
	public int getSize() {
		return items.size();
	}

	@Override
	public TbGiftTrx getElementAt(int index) {
		return items.get(index);
	}

	public void addElement(TbGiftTrx odi1) {
		if (items.contains(odi1)) {
			return;
		}
		items.add(odi1);
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
	
	public void setItems(TbGiftTrx[] _datas) {
		items.clear();
		if (null != _datas) {
			for (TbGiftTrx tx1 : _datas) {
				items.add(tx1);
			}
		}
		super.fireContentsChanged(this, 0, items.size()-1);
	}
	
	public void clear() {
		items.clear();
		super.fireContentsChanged(this, 0, 0);
	}
}
