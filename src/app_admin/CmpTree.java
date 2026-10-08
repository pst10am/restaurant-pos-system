package app_admin;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class CmpTree extends JTree {
	private static final long serialVersionUID = 1L;

	private DefaultMutableTreeNode root;
	private DefaultTreeModel treeMd;
	
	CmpTree(String _title, TreeSelectionListener _lst) {
		super();
		
		root = new DefaultMutableTreeNode(_title);
		treeMd = new DefaultTreeModel(root);
		
		this.setModel(treeMd);
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.addTreeSelectionListener(_lst);
	}

	
	public DefaultMutableTreeNode insertObject(DefaultMutableTreeNode parent, Object obj, boolean willSelected) {
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(obj);
		treeMd.insertNodeInto(newNode, parent, parent.getChildCount());
		this.scrollPathToVisible(new TreePath(newNode.getPath()));
		if (willSelected) {
			this.setSelectionPath(new TreePath(newNode.getPath()));
		}
		return newNode;
	}
	
	public DefaultMutableTreeNode insertObject(DefaultMutableTreeNode parent, Object obj, boolean willSelected, int loc) {
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(obj);
		treeMd.insertNodeInto(newNode, parent, loc);
		this.scrollPathToVisible(new TreePath(newNode.getPath()));
		if (willSelected) {
			this.setSelectionPath(new TreePath(newNode.getPath()));
		}
		return newNode;
	}
	
	public void removeNode(DefaultMutableTreeNode tgNode) {
		if ((null == tgNode) || tgNode.isRoot()) return;
		treeMd.removeNodeFromParent(tgNode);
	}

	public DefaultMutableTreeNode getSelectedNode() {
		return (DefaultMutableTreeNode)this.getLastSelectedPathComponent();
	}

	public DefaultMutableTreeNode getRootNode() {
		return root;
	}
	
	public void relocateNode(DefaultMutableTreeNode srcNode, DefaultMutableTreeNode dstNode) {
		if (null == srcNode || null == dstNode) return;
		if (dstNode.isRoot()) return;
		if (srcNode.getLevel() != dstNode.getLevel()) return;
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)srcNode.getParent();
		treeMd.insertNodeInto(srcNode, parent, parent.getIndex(dstNode));
		treeMd.reload();
		this.setSelectionPath(new TreePath(srcNode.getPath()));
	}

	public void refresh() {
		DefaultMutableTreeNode snode1 = getSelectedNode();
		TreeSelectionListener[] _lst = this.getTreeSelectionListeners();
		this.removeTreeSelectionListener(_lst[0]);
		treeMd.reload();
		this.addTreeSelectionListener(_lst[0]);
		if (null == snode1) return;
		this.scrollPathToVisible(new TreePath(snode1.getPath()));
		this.setSelectionPath(new TreePath(snode1.getPath()));
	}

	public Object[] getData() {
		Object[] objs = new Object[root.getChildCount()];
		for (int i=0; i < objs.length; i++) {
			objs[i] = ((DefaultMutableTreeNode)root.getChildAt(i)).getUserObject();
		}
		return objs;
	}

	public void clear() {
		root.removeAllChildren();
		treeMd.reload();
	}
}
