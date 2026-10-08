package app_admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import model.TxSct;

public class PanelSectionInfo extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private static final String[][] mnuDef = {
		{"/lfgr/new_docs_24.png", "New", "cmd_new"},
		{"/lfgr/edit_24.png", "Edit", "cmd_edit"},
		{"/lfgr/copy_24.png", "Copy", "cmd_copy"},
		{"/lfgr/paste_24.png", "Paste", "cmd_paste"},
		{"/lfgr/trash_24.png", "Delete", "cmd_delete"},
		{"/lfgr/remov_24.png", "Clear Background", "cmd_clear_bg"},
	};
	
	private IxMain mainix;
	
	private PanelTxSct pnSct;
	private PanelFloorPlan pnFlr;
	
	private boolean runFlg = true;
	
	PanelSectionInfo(IxMain _main) {
		super(new BorderLayout());
		mainix = _main;
		initComponents();
	}
	
	private void initComponents() {
		
		pnSct = new PanelTxSct(mainix.getFrame());
		
		JPanel wrp = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
		wrp.add(pnSct);
		wrp.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
			BorderFactory.createEmptyBorder(15, 15, 20, 10)));
		
		this.add(wrp, BorderLayout.PAGE_START);
		
		JPanel pnSctDt = new JPanel(new BorderLayout());
		
			JToolBar tlb1 = SysUtl.crToolBar(mnuDef, this);
		pnSctDt.add(tlb1, BorderLayout.PAGE_START);
		
			pnFlr = new PanelFloorPlan(mainix);
			JScrollPane scp1 = new JScrollPane(pnFlr);
			scp1.setBorder(null);
		pnSctDt.add(scp1, BorderLayout.CENTER);
		
		pnSct.setFloorPlan(pnFlr);
		
		this.add(pnSctDt, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!runFlg) return;
		String cmd = e.getActionCommand();
		if ("cmd_new".equals(cmd)) {
			JOptionPane.showMessageDialog(mainix.getFrame(), "Click on the floor to add new table.");
		} else if ("cmd_clear_bg".equals(cmd)) {
			pnSct.clearBg();
		} else if ("cmd_edit".equals(cmd)) {
			pnFlr.editTable();
		} else if ("cmd_copy".equals(cmd)) {
			pnFlr.copyTable();
		} else if ("cmd_paste".equals(cmd)) {
			JOptionPane.showMessageDialog(mainix.getFrame(), 
				"<html>Select table, [Copy]<br>Right Click on the floor, then select [Paste].</html>");
		} else if ("cmd_delete".equals(cmd)) {
			pnFlr.deleteTable();
		}
	}

	public void setData(TxSct selObj) {
		runFlg = (null != selObj);
		pnSct.setObject(selObj);
	}

	public void save() throws SQLException {
		pnSct.saveObject();
	}
}
