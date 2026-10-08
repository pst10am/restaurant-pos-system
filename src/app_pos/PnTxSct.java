package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import model.TxSct;

public class PnTxSct extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private JPanel pnSect;
	private PnTxSctTable pnTable;
	private PnTxSctIntf sctIntf;
	
	public PnTxSct(PnTxSctIntf _sctIntf) {
		super(new BorderLayout());
		sctIntf = _sctIntf;
		initComponents();
	}
	
	// private
	
	private void initComponents() {
		pnSect = new JPanel();
		pnSect.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		pnSect.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
		this.add(pnSect, BorderLayout.PAGE_START);
		
		pnTable = new PnTxSctTable(this);
		pnTable.setLayout(null);
		pnTable.setBackground(Color.BLACK);
		this.add(pnTable, BorderLayout.CENTER);
	}
	
	private void showTable(TxSct sct1) {
		pnTable.showTables(sct1);
	}
	
	// public
	
	public void refresh() {
		pnSect.removeAll();
		pnTable.removeAll();
		try {
			java.util.Vector<TxSct> sects = TxSct.getSections();
			ButtonGroup btGrp = new ButtonGroup();
			TxSct selSct = null;
			for (TxSct sct1 : sects) {
				ButtonSect bt1 = ButtonSect.newButton(sct1, null == selSct, this);
				pnSect.add(bt1);
				btGrp.add(bt1);
				if (null == selSct) {
					selSct = sct1;
				}
			}
			showTable(selSct);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void tableHasUpdated() {
		pnTable.refresh();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object objSrc = e.getSource();
		if (objSrc instanceof ButtonSect) {
			ButtonSect btSct = (ButtonSect)objSrc;
			showTable(btSct.getSection());
			return;
		}
		if (objSrc instanceof ButtonTable) {
			ButtonTable btTbl = (ButtonTable)objSrc;
			sctIntf.tableSelected(btTbl.getTable());
			return;
		}
	}
}
