package app_admin;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.TxSct;

public class PanelSection extends JSplitPane implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	/*
	{"/lfgr/new_docs_24.png", "New", "cmd_new"},
	{"/lfgr/edit_24.png", "Edit", "cmdEdit"},
	{"/lfgr/save_24.png", "Save", "cmd_save"},
	{"/lfgr/copy_24.png", "Copy", "cmdCopy"},
	{"/lfgr/paste_24.png", "Paste", "cmdPaste"},
	{"/lfgr/trash_24.png", "Delete", "cmd_delete"},
	{"gap"},
	{"/lfgr/close_24.png", "Close", "cmd_close"},
	*/
	
	private static PanelSection pnMng = null;
	
	private IxMain mainix;
	
	private DefaultListModel<TxSct> lstMdObj;
	private JList<TxSct> lstObj;
	
	private PanelSectionInfo pnDtl;
	
	// constructor

	private PanelSection(IxMain _intf) {
		super();
		mainix = _intf;
		initComponents();
	}
	
	public static PanelSection getInstance(IxMain _intf) {
		if (null == pnMng) {
			pnMng = new PanelSection(_intf);
		}
		return pnMng;
	}
	
	// private
	
	private void initComponents() {
		
		// Left
		
		final String[][] mnuDef = {
			{"/lfgr/new_docs_24.png", "New", "cmd_new"},
			{"/lfgr/save_24.png", "Save", "cmd_save"},
			{"/lfgr/trash_24.png", "Delete", "cmd_delete"},
		};
		JToolBar tlb1 = SysUtl.crToolBar(mnuDef, this);
		
		lstMdObj = new DefaultListModel<>();
		lstObj = new JList<>(lstMdObj);
		lstObj.setCellRenderer(new RdrDef());
		lstObj.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) return;
				//
				ListSelectionModel smd = lstObj.getSelectionModel();
				if (smd.isSelectionEmpty()) return;
				//
				_showDetail();
			}
		});
		
		JScrollPane scp1 = new JScrollPane(lstObj,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scp1.setBorder(null);
		
		JPanel pnMst = new JPanel(new BorderLayout());
		pnMst.add(tlb1, BorderLayout.PAGE_START);
		pnMst.add(scp1, BorderLayout.CENTER);

		// Right
		
		pnDtl = new PanelSectionInfo(mainix);
		
		// Main
		
		this.setLeftComponent(pnMst);
		this.setRightComponent(pnDtl);
		this.setDividerLocation(185);
		this.setBorder(null);
	}
	
	private void _newObject() {
		try {
			TxSct newObj = SysDialog.newTxSect(mainix.getFrame());
			if (null != newObj) {
				lstMdObj.addElement(newObj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	private void _showDetail() {
		TxSct selObj = lstObj.getSelectedValue();
		pnDtl.setData(selObj);
	}

	private void _saveObject() {
		TxSct selObj = lstObj.getSelectedValue();
		if (null == selObj) return;
		//
		try {
			pnDtl.save();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		reset();
	}

	private void _deleteObject() {
		TxSct selObj = lstObj.getSelectedValue();
		if (null == selObj) return;
		//
		try {
			selObj.delete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		reset();
	}
	
	// public
	
	public void reset() {
		pnDtl.setData(null);
		lstMdObj.clear();
		try {
			java.util.Vector<TxSct> sects = TxSct.getSectionsWithDelete();
			if (null == sects || sects.size() <= 0) {
				return;
			}
			for (TxSct sct1 : sects) {
				lstMdObj.addElement(sct1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ("cmd_new".equals(cmd)) {
			_newObject();
		} else if ("cmd_save".equals(cmd)) {
			_saveObject();
		} else if ("cmd_delete".equals(cmd)) {
			_deleteObject();
		}
	}
}
