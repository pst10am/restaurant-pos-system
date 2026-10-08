package app_pos;

import java.util.Vector;

import javax.swing.AbstractListModel;

import model.TbOrderItem;

public class LstMdOdi extends AbstractListModel<TbOrderItem> {
	private static final long serialVersionUID = 1L;
	
	private java.util.Vector<TbOrderItem> odis;
	
	public LstMdOdi() {
		odis = new java.util.Vector<>();
	}

	@Override
	public int getSize() {
		return odis.size();
	}

	@Override
	public TbOrderItem getElementAt(int index) {
		return odis.get(index);
	}

	public void addElement(TbOrderItem odi1) {
		odis.add(odi1);
		int idx1 = odis.size()-1;
		super.fireContentsChanged(this, idx1, idx1);
	}

	public void removeElement(TbOrderItem odi1) {
		int idx1 = odis.indexOf(odi1);
		odis.remove(idx1);
		super.fireContentsChanged(this, idx1, idx1);
	}
	
	public void itemHasChanged(int idx) {
		super.fireContentsChanged(this, idx, idx);
	}

	public void setItems(Vector<TbOrderItem> items) {
		//odis.clear();
		odis = items;
		super.fireContentsChanged(this, 0, odis.size()-1);
	}

	public void removeElementAt(int idx1) {
		odis.remove(idx1);
		super.fireContentsChanged(this, idx1, idx1);
	}
	
	public void itemInfoChanged(int idx1) {
		super.fireContentsChanged(this, idx1, idx1);
	}

	public boolean moveItemFromTo(int idxFrm, int idxTo) {
		// TODO Auto-generated method stub
		if (idxTo < 0) {
			return false;
		}
		if (idxTo >= odis.size()) {
			return false;
		}
		//
		TbOrderItem odi1 = odis.get(idxFrm);
		odis.remove(idxFrm);
		odis.insertElementAt(odi1, idxTo);
		//
		super.fireContentsChanged(this, 0, odis.size()-1);
		//
		return true;
	}
}
