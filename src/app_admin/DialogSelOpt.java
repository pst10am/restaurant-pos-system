package app_admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import model.TxOpt;
import model.TxPrinter;

public class DialogSelOpt extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private DefaultListModel<Object>[] mdObj;
	private JList<Object>[] lstObj;
	
	private String usrRsp = "NA";
	
	// constructor
	
	DialogSelOpt(Frame _pr) {
		super(_pr, "Selection Option", true);
		initComponents();
	}
	
	// private
	
	@SuppressWarnings("unchecked")
	private void initComponents() {
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		JPanel pnAll = new JPanel();
		pnAll.setLayout(new BoxLayout(pnAll, BoxLayout.LINE_AXIS));
		
		mdObj = new DefaultListModel[2];
		mdObj[0] = new DefaultListModel<>();
		mdObj[1] = new DefaultListModel<>();
		
		lstObj = new JList[2];
		lstObj[0] = new JList<>(mdObj[0]);
		lstObj[0].setCellRenderer(new RdrDef());
		lstObj[1] = new JList<>(mdObj[1]);
		lstObj[1].setCellRenderer(new RdrDef());
		
		pnAll.add(crPnList(lstObj[0], "Available", 1));
		
		pnAll.add(crToolBar());
		
		pnAll.add(crPnList(lstObj[1], "Selected", 2));

		this.getContentPane().add(pnAll, BorderLayout.CENTER);
		
		// command
		
		JPanel pnCmd = new JPanel(new FlowLayout(FlowLayout.TRAILING, 0, 0));
		
		JButton btOk = new JButton("Ok");
		btOk.setActionCommand("bt_ok");
		btOk.addActionListener(this);
		pnCmd.add(btOk);
		
		pnCmd.add(Box.createHorizontalStrut(5));
		
		JButton btCancel = new JButton("Cancel");
		btCancel.setActionCommand("bt_cancel");
		btCancel.addActionListener(this);
		pnCmd.add(btCancel);

		pnCmd.setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY), 
				BorderFactory.createEmptyBorder(5,5,5,5)));
		
		this.getContentPane().add(pnCmd, BorderLayout.PAGE_END);

		this.setResizable(true);
		this.pack();
		this.setSize(550, 420);
	}
	
	private JToolBar crToolBar() {
		JToolBar tbar = new JToolBar(SwingConstants.VERTICAL);
		tbar.setFloatable(false);
		
		JButton btAdd = new JButton(SysUtl.getIcon("/lfgr/forwd_24.png"));
		btAdd.setFocusable(false);
		btAdd.setActionCommand("bt_add");
		btAdd.addActionListener(this);
		btAdd.setMaximumSize(new Dimension(Short.MAX_VALUE, btAdd.getPreferredSize().height));
		
		JButton btRem = new JButton(SysUtl.getIcon("/lfgr/back_24.png"));
		btRem.setFocusable(false);
		btRem.setActionCommand("bt_remove");
		btRem.addActionListener(this);
		btRem.setMaximumSize(new Dimension(Short.MAX_VALUE, btRem.getPreferredSize().height));

		tbar.add(btAdd);
		tbar.add(Box.createVerticalStrut(5));
		tbar.add(btRem);
		tbar.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
		tbar.setMaximumSize(new Dimension(tbar.getPreferredSize().width, tbar.getPreferredSize().height));
		tbar.setAlignmentX(CENTER_ALIGNMENT);
		tbar.setAlignmentY(CENTER_ALIGNMENT);
		
		return tbar;
	}
	
	private JPanel crPnList(JList<?> _lst, String _name, int _side) {
		JPanel pn1 = new JPanel();
		pn1.setLayout(new BoxLayout(pn1, BoxLayout.PAGE_AXIS));
		
		JLabel lbR = new JLabel(_name);
		lbR.setFont(new Font("", Font.PLAIN, 16));
		lbR.setHorizontalAlignment(SwingConstants.LEFT);
		lbR.setBorder(BorderFactory.createEmptyBorder(5, 7, 5, 5));
		lbR.setMaximumSize(new Dimension(Short.MAX_VALUE, lbR.getPreferredSize().height));
		lbR.setAlignmentX(LEFT_ALIGNMENT);
		
		pn1.add(lbR);
		
		JScrollPane scp1 = new JScrollPane(_lst);
		scp1.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
		scp1.setPreferredSize(new Dimension(200, scp1.getPreferredSize().height));
		scp1.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		scp1.setAlignmentX(LEFT_ALIGNMENT);
		
		pn1.add(scp1);
		
		pn1.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		pn1.setAlignmentX(CENTER_ALIGNMENT);
		pn1.setAlignmentY(CENTER_ALIGNMENT);
		pn1.setBorder(BorderFactory.createMatteBorder(0, _side==1?0:1, 0, _side==1?1:0, Color.GRAY));
		return pn1;
	}
	
	private void moveItemFrom(int lstNum) {
		int frmLst = lstNum;
		int toLst = frmLst==0?1:0;
		if (null == lstObj[frmLst].getSelectedValue()) return;
		mdObj[toLst].addElement(lstObj[frmLst].getSelectedValue());
		mdObj[frmLst].removeElementAt(lstObj[frmLst].getSelectedIndex());
	}
	
	// public
	
	void showDialogOption(TxOpt[] allDt, TxOpt[] selDt) {
		if (null != allDt) {
			mdObj[0].clear();
			lstObj[0].clearSelection();
			for (TxOpt obj : allDt) {
				boolean fndFlg = false;
				if (null != selDt) {
					for (TxOpt opt1 : selDt) {
						if (opt1.getOptId() == obj.getOptId()) {
							fndFlg = true;
							break;
						}
					}
				}
				if (fndFlg) continue;
				mdObj[0].addElement(obj);
			}
			//
			mdObj[1].clear();
			lstObj[1].clearSelection();
			if (null != selDt) {
				for (TxOpt obj : selDt) {
					mdObj[1].addElement(obj);
				}
			}
			//
			lstObj[0].updateUI();
			lstObj[1].updateUI();
		}
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}
	
	void showDialogPrinter(TxPrinter[] allDt, TxPrinter[] selDt) {
		if (null != allDt) {
			mdObj[0].clear();
			lstObj[0].clearSelection();
			for (TxPrinter obj : allDt) {
				boolean fndFlg = false;
				if (null != selDt) {
					for (TxPrinter prt1 : selDt) {
						if (prt1.getPrnId() == obj.getPrnId()) {
							fndFlg = true;
							break;
						}
					}
				}
				if (fndFlg) continue;
				mdObj[0].addElement(obj);
			}
			//
			mdObj[1].clear();
			lstObj[1].clearSelection();
			if (null != selDt) {
				for (TxPrinter obj : selDt) {
					mdObj[1].addElement(obj);
				}
			}
			//
			lstObj[0].updateUI();
			lstObj[1].updateUI();
		}
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}
	
	String getUsrRsp() {
		return usrRsp;
	}
	
	TxOpt[] getSelectedOptions() {
		TxOpt[] values = new TxOpt[mdObj[1].getSize()];
		for (int x=0; x < values.length; x++) {
			values[x] = (TxOpt)mdObj[1].getElementAt(x);
		}
		return values;
	}
	
	TxPrinter[] getSelectedPrinters() {
		TxPrinter[] values = new TxPrinter[mdObj[1].getSize()];
		for (int x=0; x < values.length; x++) {
			values[x] = (TxPrinter)mdObj[1].getElementAt(x);
		}
		return values;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		usrRsp = e.getActionCommand();
		if ("bt_cancel".equals(usrRsp)) {
			this.dispose();
		} else if ("bt_ok".equals(usrRsp)) {
			this.dispose();
		} else if ("bt_add".equals(usrRsp)) {
			moveItemFrom(0);
		} else if ("bt_remove".equals(usrRsp)) {
			moveItemFrom(1);
		}
	}
}
