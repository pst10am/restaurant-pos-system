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

import model.TxFdCat;

public class PanelMenuCat extends JSplitPane implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private IxMain mainix;
	
	private DefaultListModel<TxFdCat> mdCat;
	private JList<TxFdCat> lstCat;
	
	private PanelMenuCatInfo pnCatInfo;
	
	// constructor
	
	PanelMenuCat(IxMain _main, PanelOption _opt, PanelPrinter _prt) {
		super();
		mainix = _main;
		initComponents(_opt, _prt);
	}
	
	// private

	private void initComponents(PanelOption _opt, PanelPrinter _prt) {
		
		// Left
		
		final String[][] mnuDef = {
			{"/lfgr/new_docs_24.png", "New", "cmd_new"},
			{"/lfgr/save_24.png", "Save", "cmd_save"},
			{"/lfgr/trash_24.png", "Delete", "cmd_delete"},
		};
		JToolBar tlb1 = SysUtl.crToolBar(mnuDef, this);
		
		mdCat = new DefaultListModel<>();
		lstCat = new JList<>(mdCat);
		lstCat.setCellRenderer(new RdrDef());
		lstCat.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) return;
				//
				ListSelectionModel smd = lstCat.getSelectionModel();
				if (smd.isSelectionEmpty()) return;
				//
				_showDetail();
			}
		});
		
		JScrollPane scp1 = new JScrollPane(lstCat,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scp1.setBorder(null);
		
		JPanel pnMst = new JPanel(new BorderLayout());
		pnMst.add(tlb1, BorderLayout.PAGE_START);
		pnMst.add(scp1, BorderLayout.CENTER);

		// Right
		
		pnCatInfo = new PanelMenuCatInfo(mainix, _opt, _prt);
		
		// Split Panel
		
		this.setLeftComponent(pnMst);
		this.setRightComponent(pnCatInfo);
		this.setBorder(null);
		this.setDividerLocation(185);
	}
	
	private void _showDetail() {
		TxFdCat selObj = lstCat.getSelectedValue();
		pnCatInfo.setObject(selObj);
	}
	
	private void _newObject() {
		try {
			TxFdCat newObj = SysDialog.newTxFdCat(mainix.getFrame());
			if (null != newObj) {
				//
				lstCat.clearSelection();
				pnCatInfo.setObject(null);
				//
				mdCat.addElement(newObj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void _saveObject() {
		if (!pnCatInfo.isDataValid()) {
			return;
		}
		//
		final DialogWait dlgw = new DialogWait(mainix.getFrame(), "Save");
		Thread trd1 = new Thread() {
			public void run() {
				dlgw.setText("saving ... please wait.\n");
				try {
					if (pnCatInfo.saveObject()) {
						lstCat.clearSelection();
						pnCatInfo.setObject(null);
					}
					dlgw.append("done.\n");
					dlgw.disposeDialog();
				} catch (Exception e) {
					dlgw.append("error.\n");
					dlgw.append(e.getMessage());
				}
				dlgw.done();
			}
		};
		dlgw.showDialog(trd1);
		reset();
	}

	private void _deleteObject() {
		if (null == lstCat.getSelectedValue()) return;
		//
		TxFdCat selObj = lstCat.getSelectedValue();
		try {
			selObj.delete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		reset();
	}
	
	// public
	
	public void reset() {
		pnCatInfo.setObject(null);
		mdCat.clear();
		try {
			java.util.Vector<TxFdCat> sects = TxFdCat.getFoodCatsWithDelete();
			if (null == sects || sects.size() <= 0) {
				return;
			}
			for (TxFdCat sct1 : sects) {
				mdCat.addElement(sct1);
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
