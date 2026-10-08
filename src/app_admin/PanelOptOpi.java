package app_admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.TxOpt;
import model.TxOptItem;

public class PanelOptOpi extends JPanel implements ActionListener, IxPanel, IxOpi {
	private static final long serialVersionUID = 1L;
	
	private IxMain mainix;
	
	private PanelTxOpt pnOpt;
	
	private DefaultListModel<TxOptItem> mdOpi;
	private JList<TxOptItem> lstOpi;
	private PanelTxOptItem pnOpi;
	
	private TxOpt optObj;
	
	//

	PanelOptOpi(IxMain _main) {
		super(new BorderLayout());
		mainix = _main;
		initComponents();
	}
	
	//
	
	private void initComponents() {
		pnOpt = new PanelTxOpt();
		pnOpt.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY), 
			BorderFactory.createEmptyBorder(20, 20, 20, 20)));
		this.add(pnOpt, BorderLayout.PAGE_START);
		
		crOpiPanel();
	}
	
	private void crOpiPanel() {
		final String[][] mnuCatDef = {
			{"/lfgr/new_docs_24.png", "New", "cmd_new"},
			{"/lfgr/trash_24.png", "Delete", "cmd_delete"},
		};
		
		// top:command
		
		JToolBar tlb1 = SysUtl.crToolBar(mnuCatDef, this);
		
		// bottom:left -> list of option items
		
		mdOpi = new DefaultListModel<>();
		lstOpi = new JList<>(mdOpi);
		lstOpi.setCellRenderer(new RdrDef());
		lstOpi.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) return;
				//
				ListSelectionModel smd = lstOpi.getSelectionModel();
				if (smd.isSelectionEmpty()) return;
				//
				_showOpiDetail();
			}
		});
		
		JScrollPane scp1 = new JScrollPane(lstOpi,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scp1.setBorder(null);
		
		// bottom:right -> opt item info
		
		pnOpi = new PanelTxOptItem();
		pnOpi.setOpiLst(this);
		pnOpi.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		JSplitPane splt1 = new JSplitPane();
		splt1.setLeftComponent(scp1);
		splt1.setRightComponent(pnOpi);
		splt1.setDividerLocation(225);
		
		JPanel pn1 = new JPanel(new BorderLayout());
		pn1.add(tlb1, BorderLayout.PAGE_START);
		pn1.add(splt1, BorderLayout.CENTER);
		this.add(pn1, BorderLayout.CENTER);
	}
	
	private void _showOpiDetail() {
		TxOptItem opi1 = lstOpi.getSelectedValue();
		pnOpi.setObject(opi1);
	}
	
	private void _newOpi() {
		if (null == optObj) return;
		//
		lstOpi.clearSelection();
		TxOptItem newOpi = SysDialog.newTxOptItem(mainix.getFrame());
		if (null != newOpi) {
			optObj.addOpi(newOpi);
			mdOpi.addElement(newOpi);
			lstOpi.setSelectedIndex(mdOpi.getSize()-1);
		}
		lstOpi.updateUI();
	}
	
	// public

	@Override
	public JPanel getPanel() {
		return this;
	}

	@Override
	public boolean isDataValid() {
		if (null == optObj) return false;
		return pnOpt.isDataValid();
	}

	@Override
	public Object getObject() {
		return optObj;
	}

	@Override
	public void setObject(Object obj) {
		optObj = null;
		if (null != obj) {
			optObj = (TxOpt)obj;
		}
		//
		pnOpt.setObject(optObj);
		pnOpi.setObject(null);
		//
		mdOpi.clear();
		if (null != optObj && optObj.hasItems()) {
			for (TxOptItem opi1 : optObj.getOptItems()) {
				mdOpi.addElement(opi1);
			}
		}
		lstOpi.updateUI();
	}
	
	boolean saveObject() throws SQLException {
		if (!isDataValid()) return false;
		//
		pnOpt.updateObject(optObj);
		optObj.save();
		for (int j=0; j < mdOpi.getSize(); j++) {
			TxOptItem opi1 = mdOpi.elementAt(j);
			opi1.setOptId(optObj.getOptId());
			opi1.save();
		}
		//
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ("cmd_new".equals(cmd)) {
			_newOpi();
		} else if ("cmd_delete".equals(cmd)) {
			
		}
	}

	public void deleteObject() throws SQLException {
		if (null == optObj) return;
		optObj.delete();
	}

	@Override
	public void defaultChangeTo(TxOptItem dopi) {
		if (mdOpi.getSize() <= 0) return;
		//
		for (int j=0; j < mdOpi.getSize(); j++) {
			TxOptItem opi1 = mdOpi.elementAt(j);
			if (opi1 != dopi) {
				opi1.setOpiDefault(false);
			}
		}
		lstOpi.updateUI();
	}
}
