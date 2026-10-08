package app_admin;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Vector;

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
import model.TxFdItem;
import model.TxOpt;

public class PanelOption extends JSplitPane implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private IxMain mainix;
	
	private DefaultListModel<TxOpt> mdObj;
	private JList<TxOpt> lstObj;
	
	private PanelOptOpi pnOptOpi;
	
	//
	
	PanelOption(IxMain _main) {
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
		
		pnOptOpi = new PanelOptOpi(mainix);
		
		// Split Panel
		
		this.setBorder(null);
		this.setLeftComponent(pnMst);
		this.setRightComponent(pnOptOpi);
		this.setDividerLocation(185);
	}
	
	//
	
	private void _showDetail() {
		TxOpt selObj = lstObj.getSelectedValue();
		pnOptOpi.setObject(selObj);
	}
	
	private void _newObject() {
		try {
			TxOpt newObj = SysDialog.newTxOpt(mainix.getFrame());
			if (null != newObj) {
				mdObj.addElement(newObj);
				lstObj.setSelectedIndex(mdObj.size()-1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void _saveObject() {
		if (!pnOptOpi.isDataValid()) {
			//System.out.println("data is not valid.");
			return;
		}
		//
		final DialogWait dlgw = new DialogWait(mainix.getFrame(), "Save");
		Thread trd1 = new Thread() {
			public void run() {
				dlgw.setText("saving ... please wait.\n");
				try {
					if (pnOptOpi.saveObject()) {
						lstObj.clearSelection();
						pnOptOpi.setObject(null);
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
			pnOptOpi.deleteObject();
			reset();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// -----public-----
	
	// public

	public void reset() {
		pnOptOpi.setObject(null);
		mdObj.clear();
		try {
			java.util.Vector<TxOpt> sects = TxOpt.getOptionsWithDelete();
			if (null == sects || sects.size() <= 0) {
				return;
			}
			for (TxOpt sct1 : sects) {
				mdObj.addElement(sct1);
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

	public java.util.Vector<TxOpt> getOptionsByItm(TxFdItem itm1) {
		if (null == itm1.getOptIds()) return null;
		//
		java.util.Vector<TxOpt> _tmprs = new java.util.Vector<>();
		for (int optId : itm1.getOptIds()) {
			for (int i1=0; i1 < mdObj.size(); i1++) {
				TxOpt opt1 = mdObj.get(i1);
				if (optId == opt1.getOptId()) {
					_tmprs.add(opt1);
					break;
				}
			}
		}
		return _tmprs;
	}

	public TxOpt[] getOptions() {
		TxOpt[] values = new TxOpt[mdObj.size()];
		mdObj.copyInto(values);
		return values;
	}

}
