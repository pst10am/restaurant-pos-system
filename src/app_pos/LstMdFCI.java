package app_pos;

import javax.swing.AbstractListModel;

import model.FindCustInfo;

public class LstMdFCI extends AbstractListModel<FindCustInfo> {
	private static final long serialVersionUID = 1L;
	
	private java.util.Vector<FindCustInfo> finds;
	
	public LstMdFCI() {
		finds = new java.util.Vector<>();
	}

	@Override
	public int getSize() {
		return finds.size();
	}

	@Override
	public FindCustInfo getElementAt(int index) {
		return finds.get(index);
	}

	public void addElement(FindCustInfo fci1) {
		finds.add(fci1);
		int idx1 = finds.size()-1;
		super.fireContentsChanged(this, idx1, idx1);
	}

	public void removeElement(FindCustInfo fci1) {
		int idx1 = finds.indexOf(fci1);
		finds.remove(idx1);
		super.fireContentsChanged(this, idx1, idx1);
	}
	
	public void itemHasChanged(int idx) {
		super.fireContentsChanged(this, idx, idx);
	}

	public void clear() {
		finds.clear();
		super.fireContentsChanged(this, 0, 0);
	}
}
