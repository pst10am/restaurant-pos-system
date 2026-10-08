package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.TbOrder;
import model.TbOrderItem;
import model.TxDel;
import model.TxFdItem;
import refx.OrderItemType;
import refx.OrderType;
import resrc.StdFont;

public class PnTbOrder extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JLabel lbTotBf, lbTax, lbDisc, 
		lbCharge, lbDeli, lbNet;
	private LstMdOdi mdOdi;
	private JList<TbOrderItem> lstOdi;
	private PnTbOrderIntf ordIntf;
	private JPanel pnDeli;
	
	private TbOrder ordObj = null;
	
	// -----constructor-----
	
	public PnTbOrder(PnTbOrderIntf _ordIntf) {
		super(new BorderLayout());
		ordIntf = _ordIntf;
		initComponents();
	}
	
	// -----private-----
	
	private void initComponents() {

		mdOdi = new LstMdOdi();
		
		lstOdi = new JList<>(mdOdi);
		lstOdi.setFont(StdFont.Fnt14);
		lstOdi.setBackground(Color.decode("#E8E8FF"));
		lstOdi.setVisibleRowCount(-1);
		lstOdi.setCellRenderer(new LstRdrOdi());
		lstOdi.setFocusable(false);
		lstOdi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstOdi.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) return;
				//
				ListSelectionModel smd = lstOdi.getSelectionModel();
				if (smd.isSelectionEmpty()) return;
				//
				ordIntf.orderItemSelected(lstOdi.getSelectedValue());
			}
		});
		
		JScrollPane scp1 = new JScrollPane(lstOdi,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scp1.setFocusable(false);
		scp1.setPreferredSize(new Dimension(350, scp1.getPreferredSize().height));
		scp1.getVerticalScrollBar().setPreferredSize(
			new Dimension(35,
				scp1.getVerticalScrollBar().getPreferredSize().height));
		scp1.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
		
		this.add(scp1, BorderLayout.CENTER);
		
		crPnBtm();
	}
	
	private void crPnBtm() {
		
		lbTotBf = _crLabel("0.00", 100, 1);
		lbTax = _crLabel("0.00", 100, 0);
		lbDisc = _crLabel("0.00", 100, 0);
		lbDeli = _crLabel("0.00", 100, 0);
		lbCharge = _crLabel("0.00", 100, 2);
		lbNet = _crLabel("0.00", 100, 3);
		
		JPanel pnBtm = new JPanel();
		pnBtm.setLayout(new BoxLayout(pnBtm, BoxLayout.PAGE_AXIS));
		
		pnBtm.add(_crPanel(_crLabel("Total (BF)", 0, 1), lbTotBf));
		pnBtm.add(_crPanel(_crLabel("Tax", 0, 0), lbTax));
		pnBtm.add(_crPanel(_crLabel("Discount", 0, 0), lbDisc));
		
		pnDeli = _crPanel(_crLabel("Delivery Fee", 0, 0), lbDeli);
		pnBtm.add(pnDeli);
		
		pnBtm.add(_crPanel(_crLabel("Charge", 0, 2), lbCharge));
		pnBtm.add(new JSeparator());
		pnBtm.add(_crPanel(_crLabel("Net Total", 0, 3), lbNet));
		
		this.add(pnBtm, BorderLayout.PAGE_END);
	}
	
	private static JPanel _crPanel(JComponent cmpL, JComponent cmpR) {
		JPanel pn1 = new JPanel();
		pn1.setLayout(new BoxLayout(pn1, BoxLayout.LINE_AXIS));
		pn1.add(cmpL);
		if (cmpL instanceof JLabel) {
			pn1.add(Box.createHorizontalGlue());
		}
		pn1.add(cmpR);
		return pn1;
	}
	
	private static JLabel _crLabel(String txt, int w, int pst) {
		JLabel lb1 = new JLabel(txt);
		if (w > 0) {
			lb1.setFont(StdFont.Fnt14B);
			lb1.setHorizontalAlignment(SwingConstants.RIGHT);
			lb1.setPreferredSize(new Dimension(w, lb1.getPreferredSize().height));
			lb1.setBackground(Color.GRAY);
			lb1.setForeground(Color.WHITE);
			lb1.setOpaque(true);
		} else {
			lb1.setFont(StdFont.Fnt14);
		}
		if (pst == 1) { // top
			lb1.setBorder(BorderFactory.createEmptyBorder(10, 10, 3, 10));
		} else if (pst == 2) { // bottom
			lb1.setBorder(BorderFactory.createEmptyBorder(3, 10, 10, 10));
		} else if (pst == 3) { // net
			lb1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		} else { // middle
			lb1.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
		}
		return lb1;
	}
	
	// -----public-----
	
	public void updateOrderTotalInfo() {
		ordObj.updateAmount();
		lbTotBf.setText(String.format("%.2f", ordObj.getOrdAmtBf()));
		lbTax.setText(String.format("%.2f", ordObj.getOrdAmtTax()));
		lbDisc.setText(String.format("%.2f", ordObj.getOrdAmtDisc()));
		lbDeli.setText(String.format("%.2f", ordObj.getDeliFee()));
		lbCharge.setText(String.format("%.2f", ordObj.getOrdAmtCharge()));
		lbNet.setText(String.format("%.2f", ordObj.getOrdAmtNet()));
	}

	public void addItem(TxFdItem fdItm) {
		TbOrderItem odi1 = null;
		if (OrderItemType.Item == fdItm.getOrderItemType()) {
			odi1 = TbOrderItem.newItem(fdItm);
		} else if (OrderItemType.OpenItem == fdItm.getOrderItemType()) {
			odi1 = TbOrderItem.newOpenItem();
		} else if (OrderItemType.Line == fdItm.getOrderItemType()) {
			odi1 = TbOrderItem.newLineItem();
		}
		if (null == odi1) return;
		//
		mdOdi.addElement(odi1);
		int idx1 = mdOdi.getSize()-1;
		if (idx1 < 0) {
			idx1 = 0;
		}
		lstOdi.ensureIndexIsVisible(idx1);
		if (odi1.isBypassOpt()) {
			updateOrderTotalInfo();
			return;
		}
		lstOdi.setSelectedIndex(idx1);
	}

	public void itemHasUpdated() {
		int idx = lstOdi.getSelectedIndex();
		if (idx < 0) return;
		mdOdi.itemHasChanged(idx);
		updateOrderTotalInfo();
	}

	public void setEnabled(boolean flg1) {
		lstOdi.setEnabled(flg1);
	}

	public void clearSelection() {
		lstOdi.clearSelection();
	}

	public double getDeliFee() {
		return ordObj.getDeliFee();
	}
	
	public void setDeliFee(double value) {
		ordObj.setDeliFee(value);
		updateOrderTotalInfo();
	}

	public void setOrder(TbOrder ord1) {
		ordObj = ord1;
		mdOdi.setItems(ordObj.getItems());
		lstOdi.clearSelection();
		pnDeli.setVisible(OrderType.ToGo == ordObj.getOrdType());
		updateOrderTotalInfo();
	}

	public boolean deleteItem() {
		TbOrderItem dlOdi = lstOdi.getSelectedValue();
		int delId = dlOdi.getOdiId();
		TxDel delObj = null;
		if (delId > 0) {
			DlgDelReason dlg1 = new DlgDelReason(ordIntf.getFrame());
			dlg1.showDialog();
			if ("bt_ok".equals(dlg1.getUsrRsp())) {
				delObj = dlg1.getSelectedValue();
			} else {
				return false;
			}
		}
		//
		int idx1 = lstOdi.getSelectedIndex();
		mdOdi.removeElementAt(idx1);
		updateOrderTotalInfo();
		//
		if (delId > 0) {
			deleteItemById(delId, delObj);
		}
		//
		return true;
	}

	private void deleteItemById(int delId, TxDel delObj) {
		if (delId <= 0) return;
		try {
			TbOrderItem.delete(delId, delObj);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void holdItem() {
		// TODO Auto-generated method stub
		int idx1 = lstOdi.getSelectedIndex();
		TbOrderItem odi1 = lstOdi.getSelectedValue();
		odi1.setOdiHold(!odi1.isHold());
		mdOdi.itemInfoChanged(idx1);
	}

	public void moveItemSeq(int stp) {
		// TODO Auto-generated method stub
		int idx1 = lstOdi.getSelectedIndex();
		if (mdOdi.moveItemFromTo(idx1, idx1+stp)) {
			lstOdi.setSelectedIndex(idx1+stp);
		}
	}

	public String checkOrder() {
		return ordObj.checkOrderInfo();
	}
}
