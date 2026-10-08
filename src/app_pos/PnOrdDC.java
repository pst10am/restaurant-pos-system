package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import model.TbOrder;
import model.TbOrderDC;
import model.TxDC;
import refx.DCType;
import refx.DlgType;
import resrc.ResData;
import resrc.StdFont;

public class PnOrdDC extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private JDialog prDlg;
	private DCType dcType;
	private LstMdDC mdItm;
	private JList<TxDC> lstItm;
	private LstMdODC mdSel;
	private JList<TbOrderDC> lstSel;
	
	private TbOrder ordObj;
	
	// -----constructor-----
	
	public PnOrdDC(JDialog _dlg, DCType _type) {
		super(new BorderLayout());
		prDlg = _dlg;
		dcType = _type;
		initComponents();
		initData();
	}
	
	// -----private-----
	
	private void initComponents() {
		
		JPanel pnCmd = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
		pnCmd.add(UIFactory.buttonA("Add","bt_add",this));
		pnCmd.add(UIFactory.buttonA("Delete","bt_delete",this));
		
		this.add(pnCmd, BorderLayout.PAGE_START);
		
		// ready items
		
		mdItm = new LstMdDC();
		lstItm = new JList<>(mdItm);
		lstItm.setFont(StdFont.Fnt22);
		lstItm.setVisibleRowCount(-1);
		lstItm.setCellRenderer(new LstRdrDC());
		lstItm.setFocusable(false);
		lstItm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scp1 = new JScrollPane(lstItm, 
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scp1.setFocusable(false);
		scp1.getVerticalScrollBar().setPreferredSize(
			new Dimension(30, scp1.getVerticalScrollBar().getPreferredSize().height));
		scp1.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
		scp1.setPreferredSize(new Dimension(330, scp1.getPreferredSize().height));
		
		// selected items

		mdSel = new LstMdODC();
		lstSel = new JList<>(mdSel);
		lstSel.setBackground(
			DCType.Discount == dcType ? Color.decode("#FFFF85") : Color.decode("#FFB2B2"));
		lstSel.setFont(StdFont.Fnt22);
		lstSel.setVisibleRowCount(-1);
		lstSel.setCellRenderer(new LstRdrODC());
		lstSel.setFocusable(false);
		lstSel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scp2 = new JScrollPane(lstSel, 
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scp2.setFocusable(false);
		scp2.getVerticalScrollBar().setPreferredSize(
			new Dimension(30, scp1.getVerticalScrollBar().getPreferredSize().height));
		scp2.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
		scp2.setPreferredSize(new Dimension(330, scp2.getPreferredSize().height));
		
		// select, de-select
		
		JPanel pnBt = new JPanel();
		pnBt.setLayout(new BoxLayout(pnBt, BoxLayout.PAGE_AXIS));
		
		JButton btSel = new JButton(new ImageIcon("img/in.png"));
		btSel.setActionCommand("bt_select");
		btSel.addActionListener(this);
		btSel.setFocusable(false);
		btSel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JButton btRem = new JButton(new ImageIcon("img/out.png"));
		btRem.setActionCommand("bt_deselect");
		btRem.addActionListener(this);
		btRem.setFocusable(false);
		btRem.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		pnBt.add(Box.createVerticalGlue());
		pnBt.add(btSel);
		pnBt.add(Box.createVerticalStrut(20));
		pnBt.add(btRem);
		pnBt.add(Box.createVerticalGlue());
		pnBt.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		
		JPanel pnLst = new JPanel(new BorderLayout());//new FlowLayout(FlowLayout.LEADING, 0, 0));
		pnLst.add(scp1, BorderLayout.LINE_START);
		pnLst.add(pnBt, BorderLayout.CENTER);
		pnLst.add(scp2, BorderLayout.LINE_END);
		
		this.add(pnLst, BorderLayout.CENTER);
		
		this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	}
	
	private void initData() {
		TxDC[] datas = ResData.getTxDC(dcType);
		if (null == datas || datas.length <= 0) {
			return;
		}
		for (TxDC dc1 : datas) {
			mdItm.addElement(dc1);
		}
	}
	
	// -----public-----
	
	public void setOrder(TbOrder ord1) {
		ordObj = ord1;
		//
		java.util.Vector<TbOrderDC> datas = null;
		if (DCType.Discount == dcType) {
			datas = ordObj.getDiscounts();
		}
		if (DCType.Charge == dcType) {
			datas = ordObj.getCharges();
		}
		//
		//mdSel.clear();
		mdSel.setItems(datas);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ("bt_add".equals(cmd)) {
			DlgNewDC dlgNew = new DlgNewDC(prDlg, dcType);
			if ("bt_ok".equals(dlgNew.getUsrRsp())) {
				mdItm.addElement(dlgNew.getValue());
			}
		} else if ("bt_delete".equals(cmd)) {
			if (lstItm.getSelectedIndex() < 0) {
				return;
			}
			DlgBox dlg1 = new DlgBox(prDlg);
			dlg1.showConfirmDialog(
				"Delete", 
				String.format("Delete [%s]?", lstItm.getSelectedValue().dispStr()),
				DlgType.Warning);
			if ("bt_ok".equals(dlg1.getUsrRsp())) {
				mdItm.removeElementAt(lstItm.getSelectedIndex());
				lstItm.clearSelection();
			}
		} else if ("bt_select".equals(cmd)) {
			if (lstItm.getSelectedIndex() < 0) {
				return;
			}
			mdSel.addElement(lstItm.getSelectedValue());
			lstItm.clearSelection();
			lstSel.clearSelection();
		} else if ("bt_deselect".equals(cmd)) {
			if (lstSel.getSelectedIndex() < 0) {
				return;
			}
			mdSel.removeElementAt(lstSel.getSelectedIndex());
			lstItm.clearSelection();
			lstSel.clearSelection();
		}
	}

}
