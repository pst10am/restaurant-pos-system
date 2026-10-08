package app_admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.TxUser;

public class PanelUsers extends JSplitPane implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private static PanelUsers pnMng = null;
 	
	private IxMain mainix;
	
	private DefaultListModel<TxUser> lstMdObj;
	private JList<TxUser> lstObj;
	
	private PanelTxUser pnDtl;
	
	// -----constructor-----
	
	private PanelUsers(IxMain _main) {
		super();
		mainix = _main;
		initComponents();
	}
	
	static PanelUsers getInstance(IxMain _main) {
		if (null == pnMng) {
			pnMng = new PanelUsers(_main);
		}
		return pnMng;
	}
	
	//
	
	private void initComponents() {
		
		// Left
		
		final String[][] mnuCatDef = {
			{"/lfgr/new_docs_24.png", "New", "cmd_new"},
			{"/lfgr/save_24.png", "Save", "cmd_save"},
			{"/lfgr/trash_24.png", "Delete", "cmd_delete"},
		};
		JToolBar tlb1 = SysUtl.crToolBar(mnuCatDef, this);
		
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
		
		pnDtl = new PanelTxUser();
		pnDtl.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY), 
			BorderFactory.createEmptyBorder(20, 20, 20, 20)));
		JPanel pn1 = new JPanel(new BorderLayout());
		pn1.add(pnDtl, BorderLayout.PAGE_START);
		
		// Split Panel
		
		this.setBorder(null);
		this.setLeftComponent(pnMst);
		this.setRightComponent(pn1);
		this.setDividerLocation(185);
	}
	
	private void _showDetail() {
		TxUser selObj = lstObj.getSelectedValue();
		pnDtl.setObject(selObj);
	}
	
	private void _newObject() {
		try {
			TxUser usr1 = SysDialog.newTxUser(mainix.getFrame());
			if (null != usr1) {
				pnDtl.setObject(null);
				lstMdObj.addElement(usr1);
				lstObj.updateUI();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void _saveObject() {
		if (null == lstObj.getSelectedValue()) return;
		//
		TxUser usr1 = lstObj.getSelectedValue();
		try {
			pnDtl.updateObject(usr1);
			usr1.save();
			reset();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void _deleteObject() {
		if (null == lstObj.getSelectedValue()) return;
		//
		try {
			TxUser usrObj = lstObj.getSelectedValue();
			usrObj.delete();
			reset();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// public

	public void reset() {
		pnDtl.setObject(null);
		lstMdObj.clear();
		try {
			TxUser[] datas = TxUser.getAllUserWithDelete();
			if (null == datas || datas.length <= 0) {
				return;
			}
			for (TxUser dt1 : datas) {
				lstMdObj.addElement(dt1);
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
