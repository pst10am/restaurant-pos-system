package app_admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import model.TxFdCat;
import model.TxFdItem;
import model.TxOpt;
import model.TxOptItem;
import model.TxPrinter;
import model.TxSct;
import model.TxSctTable;
import model.TxUser;

public class SysDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private String usrRsp = "NA";
	private IxPanel pnObj;
	
	// constructor
	
	private SysDialog(Frame _pr, IxPanel _pn, String _title) {
		super(_pr, _title, true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		
		initComponents(_pn);

		this.pack();
	}
	
	// factory
	
	static TxSct newTxSect(Frame _pr) throws SQLException {
		TxSct newObj = TxSct.newInstance();
		PanelTxSct pnObj = new PanelTxSct(_pr);
		pnObj.setObject(newObj);
		SysDialog dlgNew = new SysDialog(_pr, pnObj, "New Section");
		dlgNew._showDialog();
		if ("bt_ok".equals(dlgNew.getUsrRsp())) {
			pnObj.saveObject();
			return newObj;
		}
		return null;
	}
	
	static boolean updateTxSectTable(Frame _pr, TxSctTable _tbl) {
		PanelTxSctTable pnObj = new PanelTxSctTable();
		pnObj.setObject(_tbl);
		SysDialog dlgNew = new SysDialog(_pr, pnObj, 
			_tbl.getTblId() > 0 ? "Update Table" : "New Table");
		dlgNew._showDialog();
		return "bt_ok".equals(dlgNew.getUsrRsp());
	}
	
	static TxPrinter newTxPrinter(Frame _pr) throws SQLException {
		TxPrinter prn = TxPrinter.newInstance();
		PanelTxPrinter pnObj = new PanelTxPrinter();
		pnObj.setObject(prn);
		SysDialog dlgNew = new SysDialog(_pr, pnObj, "New Printer");
		dlgNew._showDialog();
		if ("bt_ok".equals(dlgNew.getUsrRsp())) {
			pnObj.saveObject();
			return prn;
		}
		return null;
	}
	
	static TxOptItem newTxOptItem(Frame _pr) {
		TxOptItem opi1 = TxOptItem.newInstance();
		PanelTxOptItem pnObj = new PanelTxOptItem();
		pnObj.setObject(opi1);
		SysDialog dlgNew = new SysDialog(_pr, pnObj, "New Option Item");
		dlgNew._showDialog();
		if ("bt_ok".equals(dlgNew.getUsrRsp())) {
			return opi1;
		}
		return null;
	}

	public static TxOpt newTxOpt(Frame _pr) throws SQLException {
		TxOpt opt1 = TxOpt.newInstance();
		PanelTxOpt pnObj = new PanelTxOpt();
		pnObj.setObject(opt1);
		SysDialog dlgNew = new SysDialog(_pr, pnObj, "New Option");
		dlgNew._showDialog();
		if ("bt_ok".equals(dlgNew.getUsrRsp())) {
			pnObj.updateObject(opt1);
			opt1.save();
			return opt1;
		}
		return null;
	}

	public static TxFdCat newTxFdCat(Frame _pr) throws SQLException {
		TxFdCat cat1 = TxFdCat.newInstance();
		PanelTxFdCat pnCat = new PanelTxFdCat(_pr);
		pnCat.setObject(cat1);
		SysDialog dlgNew = new SysDialog(_pr, pnCat, "New Food Category");
		dlgNew._showDialog();
		if ("bt_ok".equals(dlgNew.getUsrRsp())) {
			pnCat.updateObject(cat1);
			cat1.save();
			return cat1;
		}
		return null;
	}

	public static TxFdItem newTxFdItem(Frame _pr) throws SQLException {
		TxFdItem itm1 = TxFdItem.newObj();
		PanelTxFdItem pnItem = new PanelTxFdItem();
		pnItem.setObject(itm1);
		SysDialog dlgNew = new SysDialog(_pr, pnItem, "New Food Item");
		dlgNew._showDialog();
		if ("bt_ok".equals(dlgNew.getUsrRsp())) {
			itm1.save();
			return itm1;
		}
		return null;
	}

	public static TxUser newTxUser(Frame _pr) throws SQLException {
		TxUser usr1 = TxUser.newInstance();
		PanelTxUser pnUser = new PanelTxUser();
		pnUser.setObject(usr1);
		SysDialog dlgNew = new SysDialog(_pr, pnUser, "New User");
		dlgNew._showDialog();
		if ("bt_ok".equals(dlgNew.getUsrRsp())) {
			pnUser.updateObject(usr1);
			usr1.save();
			return usr1;
		}
		return null;
	}
	
	// private
	
	private void initComponents(IxPanel _pn) {
		
		pnObj = _pn;
		
		JPanel wrp = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
		wrp.add(pnObj.getPanel());
		wrp.setBorder(BorderFactory.createEmptyBorder(20, 20, 35, 55));
		
		this.getContentPane().add(wrp, BorderLayout.CENTER);
		
		//
		
		JPanel pnCmd = new JPanel(new FlowLayout(FlowLayout.TRAILING, 0, 0));
		pnCmd.setBackground(Color.decode("#E2E2E2"));
		
		JButton btOk = new JButton("Ok");
		btOk.setActionCommand("bt_ok");
		btOk.addActionListener(this);
		
		JButton btCancel = new JButton("Cancel");
		btCancel.setActionCommand("bt_cancel");
		btCancel.addActionListener(this);
		
		pnCmd.add(btOk);
		pnCmd.add(Box.createHorizontalStrut(5));
		pnCmd.add(btCancel);
		
		pnCmd.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY), 
			BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		
		this.getContentPane().add(pnCmd, BorderLayout.PAGE_END);
	}
	
	private void _showDialog() {
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}
	
	private Object getObject() {
		return pnObj.getObject();
	}
	
	private String getUsrRsp() {
		return usrRsp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		usrRsp = e.getActionCommand();
		if ("bt_cancel".equals(usrRsp)) {
			this.dispose();
		} else if ("bt_ok".equals(usrRsp)) {
			if (pnObj.isDataValid()) {
				this.dispose();
			}
		}
	}

}
