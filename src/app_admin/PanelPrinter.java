package app_admin;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Vector;

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

import model.TxFdItem;
import model.TxOpt;
import model.TxPrinter;

public class PanelPrinter extends JSplitPane implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private IxMain mainix;
	
	private DefaultListModel<TxPrinter> mdObj;
	private JList<TxPrinter> lstObj;
	
	private PanelTxPrinter pnDtl;
	
	//
	
	PanelPrinter(IxMain _main) {
		super();
		mainix = _main;
		initComponents();
	}
	
	//
	
	private void initComponents() {
		final String[][] mnuCatDef = {
			{"/lfgr/new_docs_24.png", "New", "cmd_new"},
			{"/lfgr/save_24.png", "Save", "cmd_save"},
			{"/lfgr/trash_24.png", "Delete", "cmd_delete"},
		};
		
		// Left
		
		JToolBar tlb1 = SysUtl.crToolBar(mnuCatDef, this);
		
		mdObj = new DefaultListModel<>();
		lstObj = new JList<>(mdObj);
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
		
		pnDtl = new PanelTxPrinter();
		pnDtl.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		// Split Panel
		
		this.setBorder(null);
		this.setLeftComponent(pnMst);
		this.setRightComponent(pnDtl);
		this.setDividerLocation(185);
	}
	
	//
	
	private void _showDetail() {
		TxPrinter selObj = lstObj.getSelectedValue();
		pnDtl.setObject(selObj);
	}
	
	private void _newObject() {
		lstObj.clearSelection();
		try {
			TxPrinter newObj = SysDialog.newTxPrinter(mainix.getFrame());
			if (null != newObj) {
				mdObj.addElement(newObj);
				lstObj.setSelectedIndex(mdObj.getSize()-1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void _saveObject() {
		if (!pnDtl.isDataValid()) return;
		//
		final DialogWait dlgw = new DialogWait(mainix.getFrame(), "Save");
		Thread trd1 = new Thread() {
			public void run() {
				dlgw.setText("saving ... please wait.\n");
				try {
					if (pnDtl.saveObject()) {
						lstObj.clearSelection();
						pnDtl.setObject(null);
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
	}

	private void _deleteObject() {
		try {
			pnDtl.deleteObject();
			reset();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// -----public-----
	
	// public

	public void reset() {
		pnDtl.setObject(null);
		mdObj.clear();
		try {
			TxPrinter[] datas = TxPrinter.getAllPrintersWithDelete();
			if (null == datas || datas.length <= 0) {
				return;
			}
			for (TxPrinter dt1 : datas) {
				mdObj.addElement(dt1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String cmd = e.getActionCommand();
		if ("cmd_new".equals(cmd)) {
			_newObject();
		} else if ("cmd_save".equals(cmd)) {
			_saveObject();
		} else if ("cmd_delete".equals(cmd)) {
			_deleteObject();
		}
	}

	public Vector<TxPrinter> getPrintersByItm(TxFdItem itm1) {
		if (null == itm1.getPrnIds()) return null;
		//
		java.util.Vector<TxPrinter> _tmprs = new java.util.Vector<>();
		for (int prnId : itm1.getPrnIds()) {
			for (int i1=0; i1 < mdObj.size(); i1++) {
				TxPrinter prt1 = mdObj.get(i1);
				if (prnId == prt1.getPrnId()) {
					_tmprs.add(prt1);
					break;
				}
			}
		}
		return _tmprs;
	}

	public TxPrinter[] getPrinters() {
		TxPrinter[] values = new TxPrinter[mdObj.getSize()];
		mdObj.copyInto(values);
		return values;
	}

}
