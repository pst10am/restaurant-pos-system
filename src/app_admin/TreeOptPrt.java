package app_admin;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import model.TxOpt;
import model.TxPrinter;

public class TreeOptPrt extends JScrollPane {
	private static final long serialVersionUID = 1L;
	
	private CmpTree tree;
	private DefaultMutableTreeNode optNode;
	private DefaultMutableTreeNode prtNode;
	
	TreeOptPrt(TreeSelectionListener _lst) {
		super();
		this.setBorder(null);
		initComponents(_lst);
	}
	
	private void initComponents(TreeSelectionListener _lst) {
		tree = new CmpTree("Options & Printers", _lst);
		optNode = tree.insertObject(tree.getRootNode(), "Options", false);
		prtNode = tree.insertObject(tree.getRootNode(), "Printers", false);
		tree.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.setViewportView(tree);
		this.setBorder(null);
	}
	
	public DefaultMutableTreeNode getSelectedNode() {
		return (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
	}

	public void refresh() {
		tree.refresh();
		for (int x=0; x < tree.getRowCount(); x++) {
			tree.expandRow(x);
		}
	}

	public void clear() {
		optNode.removeAllChildren();
		prtNode.removeAllChildren();
		tree.refresh();
	}

	public void addOption(TxOpt opt1) {
		tree.insertObject(optNode, opt1, false);
	}

	public void addPrinter(TxPrinter prt1) {
		tree.insertObject(prtNode, prt1, false);
	}
	
	public void setEnabled(boolean flg) {
		tree.setEnabled(flg);
	}
	
	public TxPrinter[] getPrinters() {
		int cnt1 = prtNode.getChildCount();
		if (cnt1 <= 0) return null;
		//
		TxPrinter[] values = new TxPrinter[cnt1];
		for (int x=0; x < cnt1; x++) {
			DefaultMutableTreeNode trnd = 
				(DefaultMutableTreeNode)prtNode.getChildAt(x);
			values[x] = (TxPrinter)trnd.getUserObject();
		}
		return values;
	}
	
	public TxOpt[] getOptions() {
		int cnt1 = optNode.getChildCount();
		if (cnt1 <= 0) return null;
		//
		TxOpt[] values = new TxOpt[cnt1];
		for (int x=0; x < cnt1; x++) {
			DefaultMutableTreeNode trnd = 
				(DefaultMutableTreeNode)optNode.getChildAt(x);
			values[x] = (TxOpt)trnd.getUserObject();
		}
		return values;
	}

	public void clearOptions() {
		optNode.removeAllChildren();
	}

	public void clearPrinters() {
		prtNode.removeAllChildren();
	}
}
