package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.TbOrder;
import model.TbOrderItem;
import resrc.StdFont;

public class PnSplit extends JPanel 
	implements ActionListener, PnSpltOrdIntf {
	
	private static final long serialVersionUID = 1L;
	
	private PnSplitIntf spIntf;
	private JScrollPane scpCt = null;
	private java.util.Vector<TbOrder> orders;
	private JPanel pnOrds;
	private PnSpltOrd curSpOrd = null;

	public PnSplit(PnSplitIntf _intf) {
		super(new BorderLayout());
		spIntf = _intf;
		initComponents();
	}
	
	private void initComponents() {
		
		JPanel pnTop = new JPanel();
		pnTop.setLayout(new BoxLayout(pnTop, BoxLayout.LINE_AXIS));
		
			JLabel lb1 = new JLabel("Split Ticket ... ");
			lb1.setFont(StdFont.Fnt22);
			lb1.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
		pnTop.add(lb1);
		
		pnTop.add(Box.createHorizontalGlue());
		
		pnTop.add(UIFactory.buttonD("New", "bt_new", this));
		pnTop.add(UIFactory.buttonD("Delete", "bt_del", this));
		
		pnTop.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
		
		this.add(pnTop, BorderLayout.PAGE_START);
		
		pnOrds = new JPanel();
		pnOrds.setLayout(new GridLayout(0, 3, 3, 3));
		pnOrds.setBackground(Color.decode("#F5FFD6"));
		pnOrds.setBorder(BorderFactory.createEmptyBorder(10, 10, 50, 10));
		
		//JPanel tmpPn = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
		//tmpPn.add(pnOrds);
		
		scpCt = new JScrollPane(pnOrds, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scpCt.getVerticalScrollBar().setPreferredSize(new Dimension(30, 0)); 
		scpCt.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 30)); 
		scpCt.setBorder(null);
		
		this.add(scpCt, BorderLayout.CENTER);
		
		// Command
		
		//Button btReset = Button.newButton("Reset,bt_reset", this);
		Button btOk = Button.newOk(this);
		//Button btCancel = Button.newCancel(this);

		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		//pnCmd.add(btReset);
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.add(btOk);
		//pnCmd.add(btCancel);
		
		pnCmd.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

		this.add(pnCmd, BorderLayout.PAGE_END);
	}
	
	private void clear() {
		orders = null;
		pnOrds.removeAll();
		//scpCt.setViewportView(null);
		scpCt.updateUI();
	}
	
	private void newOrder() {
		TbOrder ord1 = TbOrder.newDineInOrder(orders.get(0));
		orders.add(ord1);
		pnOrds.add(new PnSpltOrd(ord1, false, this));
		//scpCt.setViewportView(pnOrds);
		scpCt.updateUI();
	}
	
	// ------------------------------------

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ("bt_ok".equals(cmd)) {
			spIntf.showScrMain();
		} else if ("bt_new".equals(cmd)) {
			newOrder();
		} else if ("bt_del".equals(cmd)) {
			
		}
	}

	public void setOrders(java.util.Vector<TbOrder> _orders, int _ordNo) {
		clear();
		orders = _orders;
		//
		pnOrds.removeAll();
		//
		curSpOrd = null;
		for (TbOrder ord1 : orders) {
			PnSpltOrd tmpPn = new PnSpltOrd(ord1, _ordNo == ord1.getOrdNo(), this);
			if (_ordNo == ord1.getOrdNo()) {
				curSpOrd = tmpPn;
			}
			pnOrds.add(tmpPn);
		}
		//
		//scpCt.setViewportView(pnOrds);
		scpCt.updateUI();
	}

	public int getSelectedOrderNo() {
		return curSpOrd.getOrdNo();
	}

	@Override
	public void headClick(PnSpltOrd spOrd, boolean moveItem) {
		if (moveItem) {
			TbOrderItem selOdi = curSpOrd.popItem();
			if (null != selOdi) {
				spOrd.pushItem(selOdi);
			}
		}
		//
		curSpOrd.setSelected(false);
		curSpOrd.clearSelection();
		
		spOrd.setSelected(true);
		curSpOrd = spOrd;
		//
		//spOrd.clearSelection();
	}
}
