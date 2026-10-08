package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import resrc.StdFont;
import model.TbOrder;
import model.TbOrderItem;

public class PnSpltOrd extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private TbOrder ordObj = null;
	private LstMdOdi mdOdi = null;
	private JList<TbOrderItem> lstOdi = null;
	private boolean isSel = false;
	private JPanel pnCt;
	private PnSpltOrdIntf sintf;
	
	// --------------------------------------------

	public PnSpltOrd(TbOrder _ord, boolean _selFlg, PnSpltOrdIntf _intf) {
		super(new FlowLayout(FlowLayout.LEADING, 0, 0));
		//
		ordObj = _ord;
		isSel = _selFlg;
		sintf = _intf;
		//
		initComponents();
		//
		updateBorder();
	}

	private void initComponents() {
		this.setOpaque(false);
		
		pnCt = new JPanel(new BorderLayout());
		
		JButton btTk = UIFactory.buttonH(
			String.format("#%d", ordObj.getOrdNo()), "bt_tkno", this);
		pnCt.add(btTk, BorderLayout.PAGE_START);
		
		mdOdi = new LstMdOdi();
		mdOdi.setItems(ordObj.getItems());
		
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
				//ordIntf.orderItemSelected(lstOdi.getSelectedValue());
				selectThisTicket(false);
			}
		});
		
		JScrollPane scp1 = new JScrollPane(lstOdi,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scp1.setFocusable(false);
		scp1.setOpaque(true);
		scp1.setPreferredSize(new Dimension(352, 300));
		scp1.getVerticalScrollBar().setPreferredSize(
			new Dimension(35,
				scp1.getVerticalScrollBar().getPreferredSize().height));
		//scp1.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
		scp1.setBorder(null);
		
		pnCt.add(scp1, BorderLayout.CENTER);
		
		this.add(pnCt);
		
		this.setPreferredSize(new Dimension(358, 345));
		this.setMaximumSize(this.getPreferredSize());
		this.setMinimumSize(this.getPreferredSize());
	}
	
	private void selectThisTicket(boolean moveItem) {
		if (isSel) return;
		sintf.headClick(this, moveItem);
	}
	
	// ----------------------------------------------------
	
	public TbOrder getOrder() {
		return ordObj;
	}
	
	public TbOrderItem popItem() {
		if (lstOdi.isSelectionEmpty()) {
			return null;
		}
		TbOrderItem odi1 = lstOdi.getSelectedValue();
		mdOdi.removeElementAt(lstOdi.getSelectedIndex());
		return odi1;
	}
	
	public void pushItem(TbOrderItem odi1) {
		mdOdi.addElement(odi1);
	}
	
	public void updateBorder() {
		Color selColor = Color.LIGHT_GRAY;
		if (isSel) {
			selColor = Color.RED;
		} else {
			lstOdi.clearSelection();
		}
		pnCt.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, selColor));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ("bt_tkno".equals(cmd)) {
			selectThisTicket(true);
		}
	}

	public void setSelected(boolean b) {
		isSel = b;
		updateBorder();
	}

	public int getOrdNo() {
		return ordObj.getOrdNo();
	}

	public void clearSelection() {
		lstOdi.clearSelection();
	}
}
