package app_admin;

import java.awt.BorderLayout;
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

import model.TxFdCat;
import model.TxFdItem;

public class PanelMenuCatInfo extends JSplitPane implements ActionListener, IxPanel {
	private static final long serialVersionUID = 1L;
	
	private IxMain mainix;
	
	private PanelTxFdCat pnCat;

	private DefaultListModel<TxFdItem> mdItm;
	private JList<TxFdItem> lstItm;
	
	private PanelItmOptPrt pnItmOp;
	
    private TxFdCat catObj;
    
    private PanelOption pOpt;
    private PanelPrinter pPrt;
    
    private boolean updFlg = false;
    
	//

	PanelMenuCatInfo(IxMain _main, PanelOption _opt, PanelPrinter _prt) {
		super(JSplitPane.VERTICAL_SPLIT);
		mainix = _main;
		pOpt = _opt;
		pPrt = _prt;
		initComponents();
	}
	
	private void initComponents() {

		// category
		
		pnCat = new PanelTxFdCat(mainix.getFrame());
		pnCat.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JScrollPane scpCat = new JScrollPane(pnCat);
		scpCat.setBorder(null);
		scpCat.setPreferredSize(pnCat.getPreferredSize());
		
		// items in category
		
		this.setBorder(null);
		this.setTopComponent(scpCat);
		this.setBottomComponent(crItemsPanel());
		this.setDividerLocation(pnCat.getPreferredSize().height);
	}
	
	private JSplitPane crItemsPanel() {

		final String[][] mnuItmDef = {
			{"/lfgr/new_docs_24.png", "New", "cmd_item_new"},
			{"/lfgr/trash_24.png", "Delete", "cmd_item_delete"},
		};
		
		// Left
		
		JToolBar tlb1 = SysUtl.crToolBar(mnuItmDef, this);
		
		mdItm = new DefaultListModel<>();
		lstItm = new JList<>(mdItm);
		lstItm.setCellRenderer(new RdrDef());
		lstItm.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) return;
				//
				ListSelectionModel smd = lstItm.getSelectionModel();
				if (smd.isSelectionEmpty()) return;
				//
				_showDetail();
			}
		});
		
		JScrollPane scp1 = new JScrollPane(lstItm,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scp1.setBorder(null);
		
		JPanel pnMst = new JPanel(new BorderLayout());
		pnMst.add(tlb1, BorderLayout.PAGE_START);
		pnMst.add(scp1, BorderLayout.CENTER);

		// Right
		
		pnItmOp = new PanelItmOptPrt(mainix.getFrame(), pOpt, pPrt);
		
		// Split Panel
		
		JSplitPane splt1 = new JSplitPane();
		splt1.setBorder(null);
		splt1.setLeftComponent(pnMst);
		splt1.setRightComponent(pnItmOp);
		splt1.setDividerLocation(185);
		
		return splt1;
	}
	
	private void _showDetail() {
		if (null == lstItm.getSelectedValue()) return;
		//
		pnItmOp.setObject(lstItm.getSelectedValue());
	}
	
	private void _newObject() {
		try {
			TxFdItem itm1 = SysDialog.newTxFdItem(mainix.getFrame());
			if (null != itm1) {
				lstItm.clearSelection();
				pnItmOp.setObject(null);
				//
				mdItm.addElement(itm1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void _deleteObject() {
		if (null == lstItm.getSelectedValue()) return;
		//
		pnItmOp.setObject(null);
		//
		TxFdItem selItm = lstItm.getSelectedValue();
		selItm.setItmStatus(selItm.getItmStatus()==1?2:1);
		//
		lstItm.clearSelection();
		lstItm.updateUI();
	}

	// public
	
	boolean saveObject() throws SQLException {
		if (!updFlg) return false;
		//
		if (!pnCat.isDataValid()) {
			return false;
		}
		pnCat.updateObject(catObj);
		catObj.save();
		for (int x=0; x < mdItm.size(); x++) {
			TxFdItem itm1 = mdItm.get(x);
			itm1.setCatId(catObj.getCatId());
			itm1.save();
		}
		return true;
	}
	
	void setObject(TxFdCat _cat) {
		updFlg = false;
		catObj = _cat;
		pnCat.setObject(catObj);
		//
		mdItm.clear();
		pnItmOp.setObject(null);
		try {
			if (null != catObj) {
				java.util.Vector<TxFdItem> items = 
					TxFdItem.getItemsInCatWithDelete(catObj.getCatId());
				if (null != items && items.size() > 0) {
					for (TxFdItem itm1 : items) {
						mdItm.addElement(itm1);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		lstItm.updateUI();
		//
		updFlg = null != catObj;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!updFlg) return;
		//
		String cmd = e.getActionCommand();
		if ("cmd_item_new".equals(cmd)) {
			_newObject();
		} else if ("cmd_item_delete".equals(cmd)) {
			_deleteObject();
		}
	}

	@Override
	public JPanel getPanel() {
		return null;
	}

	@Override
	public boolean isDataValid() {
		return pnCat.isDataValid();
	}

	@Override
	public Object getObject() {
		return null;
	}

	@Override
	public void setObject(Object obj) {
	}
}
