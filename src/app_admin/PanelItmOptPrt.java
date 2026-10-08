package app_admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import model.TxFdItem;
import model.TxOpt;
import model.TxPrinter;

public class PanelItmOptPrt extends JSplitPane 
		implements ActionListener, TreeSelectionListener {
	
	private static final long serialVersionUID = 1L;
	
	private PanelTxFdItem pnItm;
	private PanelOption pnOpt;
	private PanelPrinter pnPrt;
	private TreeOptPrt trOp;
	private TxFdItem fdItm;
	private JLabel lbInfo1, lbInfo2;
	
	private Frame prFrm;
	
	//

	PanelItmOptPrt(Frame _pr, PanelOption _opt, PanelPrinter _prt) {
		super(JSplitPane.VERTICAL_SPLIT);
		prFrm = _pr;
		pnOpt = _opt;
		pnPrt = _prt;
		initComponents();
	}
	
	private void initComponents() {
		
		// item
		
		pnItm = new PanelTxFdItem();
		pnItm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JScrollPane scpItm = new JScrollPane(pnItm);
		scpItm.setBorder(null);
		scpItm.setPreferredSize(pnItm.getPreferredSize());
		
		// options + printers

		JPanel pnOp = new JPanel(new BorderLayout());
		
		final String[][] mnuOptDef = {
			{"/lfgr/prefs_24.png", "Option", "cmd_update_option"},
			{"/lfgr/print_24.png", "Printer", "cmd_update_printer"},
		};
		pnOp.add(SysUtl.crToolBar(mnuOptDef, this), BorderLayout.PAGE_START);
		
		trOp = new TreeOptPrt(this);
		
		//JPanel pnInfo = new JPanel();
		
		lbInfo1 = new JLabel();
		lbInfo1.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		lbInfo2 = new JLabel();
		lbInfo2.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY),
			BorderFactory.createEmptyBorder(5, 0, 5, 0)));
		
		JPanel pnInf = new JPanel();
		pnInf.setLayout(new BoxLayout(pnInf, BoxLayout.PAGE_AXIS));
		pnInf.add(lbInfo1);
		pnInf.add(lbInfo2);
		pnInf.setBorder(BorderFactory.createEmptyBorder(10,10,25,10));
		
		JScrollPane scpInfo = new JScrollPane(pnInf);
		
		JSplitPane splt1 = new JSplitPane();
		splt1.setLeftComponent(trOp);
		splt1.setRightComponent(scpInfo);
		splt1.setDividerLocation(195);
		pnOp.add(splt1, BorderLayout.CENTER);
		
		this.setBorder(null);
		this.setTopComponent(scpItm);
		this.setBottomComponent(pnOp);
		this.setDividerLocation(scpItm.getPreferredSize().height);
	}
	
	private void _selectOptions() {
		DialogSelOpt dlgSel = new DialogSelOpt(prFrm);
		TxOpt[] opts = pnOpt.getOptions();
		TxOpt[] sels = trOp.getOptions();
		if (null != opts && opts.length > 0) {
			dlgSel.showDialogOption(opts, sels);
			if ("bt_ok".equals(dlgSel.getUsrRsp())) {
				trOp.clearOptions();
				fdItm.clearOptions();
				TxOpt[] sels2 = dlgSel.getSelectedOptions();
				if (null != sels2 && sels2.length > 0) {
					for (TxOpt sopt : sels2) {
						trOp.addOption(sopt);
						fdItm.addOptId(sopt.getOptId());
					}
				}
				trOp.refresh();
			}
		}
	}
	
	private void _selectPrinters() {
		DialogSelOpt dlgSel = new DialogSelOpt(prFrm);
		TxPrinter[] alls = pnPrt.getPrinters();
		TxPrinter[] sels = trOp.getPrinters();
		if (null != alls && alls.length > 0) {
			dlgSel.showDialogPrinter(alls, sels);
			if ("bt_ok".equals(dlgSel.getUsrRsp())) {
				trOp.clearPrinters();
				fdItm.clearPrinters();
				TxPrinter[] sels2 = dlgSel.getSelectedPrinters();
				if (null != sels2 && sels2.length > 0) {
					for (TxPrinter sprt : sels2) {
						trOp.addPrinter(sprt);
						fdItm.addPrnId(sprt.getPrnId());
					}
				}
				trOp.refresh();
			}
		}
	}
	
	// public

	public void setObject(TxFdItem _itm) {
		fdItm = _itm;
		pnItm.setObject(fdItm);
		trOp.clear();
		if (null != fdItm) {
			java.util.Vector<TxOpt> opts = pnOpt.getOptionsByItm(fdItm);
			if (null != opts && opts.size() > 0) {
				for (TxOpt opt1 : opts) {
					trOp.addOption(opt1);
				}
			}
			java.util.Vector<TxPrinter> prts = pnPrt.getPrintersByItm(fdItm);
			if (null != prts && prts.size() > 0) {
				for (TxPrinter prt1 : prts) {
					trOp.addPrinter(prt1);
				}
			}
		}
		//
		trOp.setEnabled(null!=fdItm);
		trOp.updateUI();
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		if (null == fdItm) return;
		//
		DefaultMutableTreeNode selNode = trOp.getSelectedNode();
		lbInfo1.setText("");
		lbInfo2.setText("");
		if (null == selNode) {
		} else if (selNode.isRoot()) {
		} else {
			Object selObj = selNode.getUserObject();
			if (selObj instanceof TxOpt) {
				lbInfo2.setVisible(true);
				TxOpt opt1 = (TxOpt)selObj;
				lbInfo1.setText(opt1.getHtmlStr());
				lbInfo2.setText(opt1.getHtmlOpiStr());
			} else if (selObj instanceof TxPrinter) {
				lbInfo2.setVisible(false);
				TxPrinter prt1 = (TxPrinter)selObj;
				lbInfo1.setText(prt1.getHtmlStr());
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (null == fdItm) return;
		//
		String cmd = e.getActionCommand();
		if ("cmd_update_option".equals(cmd)) {
			_selectOptions();
		} else if ("cmd_update_printer".equals(cmd)) {
			_selectPrinters();
		}
	}
}
