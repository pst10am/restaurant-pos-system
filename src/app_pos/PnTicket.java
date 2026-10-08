package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import refx.OrderType;
import model.TbOrder;
import model.TbOrderItem;
import model.TxFdItem;

public class PnTicket extends JPanel
	implements ActionListener, PnTbOrderIntf {
	private static final long serialVersionUID = 1L;

	private JButton btTk;
	private PnTbOrder pnOrd;
	private PnTicketIntf tkIntf;
	private java.util.Vector<TbOrder> orders = new java.util.Vector<>();
	private int ordIdx = -1;
	private boolean allowTicketClick = true;
	
	// -----constructor-----
	
	public PnTicket(PnTicketIntf _tkIntf) {
		super(new BorderLayout());
		tkIntf = _tkIntf;
		initComponents();
	}
	
	// -----private-----
	
	private void initComponents() {
		this.setBackground(Color.decode("#E0E0D6"));
		
		btTk = UIFactory.buttonH("#-", "bt_tkno", this);
		this.add(btTk, BorderLayout.PAGE_START);
		
		//
		
		pnOrd = new PnTbOrder(this);
		
		this.add(pnOrd, BorderLayout.CENTER);
	}
	
	private void showOrder(int _idx) {
		TbOrder ord1 = orders.get(_idx);
		allowTicketClick = OrderType.DineIn == ord1.getOrdType();
		//
		String title = String.format("#%d", ord1.getOrdNo());
		if (orders.size() > 1) {
			title = String.format("#%d (of %d)", ord1.getOrdNo(), orders.size());
		}
		//
		btTk.setText(title);
		pnOrd.setOrder(ord1);
	}
	
	// -----public-----
	
	public OrderType getOrdType() {
		return getCurrentOrder().getOrdType();
	}
	
	public int getTblId() {
		return getCurrentOrder().getTblId();
	}
	
	public TbOrder getCurrentOrder() {
		return orders.get(ordIdx);
	}

	public void addItem(TxFdItem fdItm) {
		pnOrd.addItem(fdItm);
	}
	
	public void itemHasUpdated() {
		pnOrd.itemHasUpdated();
	}
	
	public void setEnabled(boolean flg1) {
		btTk.setEnabled(flg1);
		pnOrd.setEnabled(flg1);
	}

	public void clearSelection() {
		pnOrd.clearSelection();
	}

	public double getDeliFee() {
		return pnOrd.getDeliFee();
	}
	public void setDeliFee(double value) {
		pnOrd.setDeliFee(value);
	}

	public void setOrders(TbOrder[] _orders) {
		clearSelection();
		orders.clear();
		for (TbOrder ord1 : _orders) {
			orders.add(ord1);
		}
		ordIdx = 0;
		showOrder(ordIdx);
	}

	public java.util.Vector<TbOrder> getOrders() {
		return orders;
	}
	
	public void updateOrderTotalInfo() {
		pnOrd.updateOrderTotalInfo();
	}

	public void selectOrderByOrderNo(int _ordNo) {
		int idx1 = -1;
		for (int i=0; i < orders.size(); i++) {
			TbOrder ord1 = orders.get(i);
			if (_ordNo == ord1.getOrdNo()) {
				idx1 = i;
				break;
			}
		}
		ordIdx = idx1;
		showOrder(ordIdx);
	}

	public boolean saveOrders() {
		
		final DlgThread dlg1 = new DlgThread(tkIntf.getFrame());
		Thread trd1 = new Thread(){
			public void run() {
				dlg1.setText("Saving Orders\n");
				try {
					for (TbOrder ord1 : orders) {
						dlg1.append(String.format(" - Saving Order -> #%d ... ", ord1.getOrdNo()));
						ord1.save();
						dlg1.append("Ok\n");
					}
					dlg1.setExitCode(1);
					dlg1.dispose();
				} catch (Exception e) {
					dlg1.append("Error\n");
					dlg1.append(e.getMessage());
				}
				dlg1.processEnd();
			}
		};
		dlg1.showDialog(trd1);
		
		return 1 == dlg1.getExitCode();
	}

	public void checkCurrentOrder() {
		final TbOrder ord1 = getCurrentOrder();
		ord1.setOrdCntCheck(1);
	}

	public boolean allOrdersChecked() {
		boolean allChk = true;
		for (TbOrder ord1 : orders) {
			if (ord1.getOrdCntCheck() == 0) {
				allChk = false;
				break;
			}
		}
		return allChk;
	}

	public void removeCurrentOrder() {
		orders.remove(ordIdx);
		if (ordIdx > orders.size()-1) {
			ordIdx = orders.size()-1;
		}
		showOrder(ordIdx);
	}

	public void deleteItem() {
		if (pnOrd.deleteItem()) {
			tkIntf.itemHasDeleted();
		}
	}

	public void holdItem() {
		pnOrd.holdItem();
	}

	@Override
	public void orderItemSelected(TbOrderItem odi1) {
		tkIntf.orderItemSelected(odi1);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ("bt_tkno".equals(cmd)) {
			if (!allowTicketClick) return;
			tkIntf.ticketNoClicked();
		}
	}

	@Override
	public Frame getFrame() {
		return tkIntf.getFrame();
	}

	public void moveItemSeq(int stp) {
		pnOrd.moveItemSeq(stp);
	}

	public boolean noNeedToSave() {
		for (TbOrder ord1 : orders) {
			if (ord1.getOrdId() > 0 || ord1.getItemCount() > 0) {
				return false;
			}
		}
		return true;
	}

	public String checkOrderInfo() {
		return pnOrd.checkOrder();
	}

	public void clearEmptyOrder() {
		boolean chkFlg = false;
		for (TbOrder ord1 : orders) {
			if (ord1.getOrdId() > 0 && ord1.getItemCount() <= 0) {
				chkFlg = true;
			}
		}
		
		if (!chkFlg) return;
		
		final DlgThread dlg1 = new DlgThread(tkIntf.getFrame());
		Thread trd1 = new Thread(){
			public void run() {
				dlg1.setText("Clear Empty Order ... \n");
				try {
					for (TbOrder ord1 : orders) {
						if (ord1.getOrdId() > 0 && ord1.getItemCount() <= 0) {
							dlg1.append(String.format(" - Order -> #%d ... ", ord1.getOrdNo()));
							ord1.delete();
							dlg1.append("Ok\n");
						}
					}
					dlg1.setExitCode(1);
					dlg1.dispose();
				} catch (Exception e) {
					dlg1.append("Error\n");
					dlg1.append(e.getMessage());
				}
				dlg1.processEnd();
			}
		};
		dlg1.showDialog(trd1);
	}
}
