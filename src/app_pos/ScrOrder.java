package app_pos;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import model.TbOrder;
import model.TbOrderItem;
import model.TxFdItem;
import model.TxSctTable;
import refx.DlgType;
import refx.OrderType;
import refx.TableService;
import resrc.StdFont;

public class ScrOrder extends JPanel 
	implements ActionListener, PnMenuIntf, PnTicketIntf, PnTbOrderItemIntf, PnSplitIntf {
	
	private static final long serialVersionUID = 1L;
	
	private static final String SCR_MAIN = "scr_main";
	private static final String SCR_SPLIT = "scr_split";
	
	private ScrIntf scrIntf;
	
	private PnTicket pnTk;
	private PnTbOrderItem pnOdi;
	private PnMenu pnMenu;	
	private PnSplit pnSplit;
	private JPanel pnMain, pnOdiMnu;
	
	private static String PNAME_MENU = "pn_menu";
	private static String PNAME_ITEM_DETAIL = "pn_item";
	
	private JButton btCust, btMenu;
	private Button btCheck, btSplit, btDisc, 
		btCharge, btDeli, btDone, btCancel;
	
	private DlgTbOrder dlgOrd;
	
	// -----constructor-----
	
	public ScrOrder(ScrIntf _scrIntf) {
		super(new BorderLayout());
		
		scrIntf = _scrIntf;
		dlgOrd = new DlgTbOrder(scrIntf.getFrame());
		
		initComponents();
	}
	
	// -----private-----
	
	private void initComponents() {
		pnMain = new JPanel();
		pnMain.setLayout(new CardLayout());
		
		pnMain.add(initScrMain(), SCR_MAIN);
		
		pnSplit = new PnSplit(this);
		pnMain.add(pnSplit, SCR_SPLIT);
		
		this.add(pnMain, BorderLayout.CENTER);
	}
	
	private JPanel initScrMain() {
		JPanel pnRs = new JPanel(new BorderLayout());
		
		btCust = new JButton("Customer");
		btCust.setFocusable(false);
		btCust.setFont(StdFont.Fnt14B);
		btCust.setHorizontalAlignment(SwingConstants.LEFT);
		btCust.setActionCommand("bt_cust");
		btCust.addActionListener(this);
		btCust.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		btMenu = new JButton("  Menu  ");
		btMenu.setFocusable(false);
		btMenu.setFont(StdFont.Fnt14B);
		btMenu.setActionCommand("bt_menu");
		btMenu.addActionListener(this);
		btMenu.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 1, 0, 0, Color.GRAY), 
			BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		
		JPanel pnTop = new JPanel();
		pnTop.setLayout(new BorderLayout());
		pnTop.add(btCust, BorderLayout.CENTER);
		pnTop.add(btMenu, BorderLayout.LINE_END);
		pnTop.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY));
		
		pnRs.add(pnTop, BorderLayout.PAGE_START);
		
		// Left
		
		JPanel pnCt = new JPanel(new BorderLayout());
		
		pnTk = new PnTicket(this);
		pnCt.add(pnTk, BorderLayout.LINE_START);
		
		// Right
		
		pnOdiMnu = new JPanel();
		pnOdiMnu.setLayout(new CardLayout());
		
		pnMenu = new PnMenu(this);
		pnOdi = new PnTbOrderItem(this);
		
		pnOdiMnu.add(pnMenu, PNAME_MENU);
		pnOdiMnu.add(pnOdi, PNAME_ITEM_DETAIL);
		pnOdiMnu.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 1, 0, 0, Color.GRAY),
			BorderFactory.createEmptyBorder(10, 10, 10, 10)
		));
		pnCt.add(pnOdiMnu, BorderLayout.CENTER);
		
		pnRs.add(pnCt, BorderLayout.CENTER);
		
		// Bottom
		
		crBtmCmd(pnRs);
		
		return pnRs;
	}
	
	private void crBtmCmd(JPanel pnRs) {
		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		
		btCheck = Button.newButton("Check,bt_check", this);
		btSplit = Button.newButton("Split,bt_split", this);
		btDisc = Button.newButton("Disc,bt_disc", this);
		btCharge = Button.newButton("Charge,bt_charge", this);
		btDeli = Button.newButton("Deli+$,bt_deli", this);
		btDone = Button.newButton("Done,bt_done", this);
		btCancel = Button.newButton("Cancel,bt_cancel", this);
		
		pnCmd.add(btCheck);
		pnCmd.add(btSplit);
		pnCmd.add(btDisc);
		pnCmd.add(btCharge);
		pnCmd.add(btDeli);
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.add(btDone);
		pnCmd.add(btCancel);
		
		pnCmd.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.DARK_GRAY));
		pnRs.add(pnCmd, BorderLayout.PAGE_END);
	}
	
	private void showPanel(String pnName) {
		if (PNAME_MENU.equals(pnName)) {
			pnTk.clearSelection();
			pnMenu.clearSelection();
		}
		CardLayout cl1 = (CardLayout)pnOdiMnu.getLayout();
		cl1.show(pnOdiMnu, pnName);
	}
	
	private void updateCustInfoBt() {
		TbOrder ord1 = pnTk.getCurrentOrder();
		//
		btCust.setText(ord1.getCstTxt());
		btSplit.setVisible(OrderType.DineIn == ord1.getOrdType());
		btDeli.setVisible(OrderType.ToGo == ord1.getOrdType());
		btCheck.setVisible(OrderType.DineIn == ord1.getOrdType());
		//
		pnMenu.updateBtCommand(ord1.getOrdType());
	}
	
	private void updateCustomer() {
		dlgOrd.showCustomer(pnTk.getCurrentOrder());
		updateCustInfoBt();
		pnTk.updateOrderTotalInfo();
	}
	
	private void updateDiscount() {
		dlgOrd.showDiscount(pnTk.getCurrentOrder());
		updateCustInfoBt();
		pnTk.updateOrderTotalInfo();
	}
	
	private void updateCharge() {
		dlgOrd.showCharge(pnTk.getCurrentOrder());
		updateCustInfoBt();
		pnTk.updateOrderTotalInfo();
	}
	
	private void showScreen(String _name) {
		CardLayout cl1 = (CardLayout)pnMain.getLayout();
		cl1.show(pnMain, _name);
	}
	
	private void enableComponents(boolean flg1) {
		btCust.setEnabled(flg1);
		btMenu.setEnabled(flg1);
		//
		btCheck.setEnabled(flg1);
		btSplit.setEnabled(flg1);
		btDisc.setEnabled(flg1); 
		btCharge.setEnabled(flg1);
		btDeli.setEnabled(flg1);
		btDone.setEnabled(flg1);
		pnTk.setEnabled(flg1);
	}

	private void refresh() {
		pnMenu.refresh();
		showPanel(PNAME_MENU);
	}
	
	private void splitTicket() {
		showPanel(PNAME_MENU);
		pnSplit.setOrders(pnTk.getOrders(), pnTk.getCurrentOrder().getOrdNo());
		showScreen(SCR_SPLIT);
	}
	
	// -----public-----

	public void setOrders(TbOrder[] orders) {
		refresh();
		pnTk.setOrders(orders);
		updateCustInfoBt();
	}

	@Override
	public void menuItemSelected(TxFdItem fdItm) {
		pnTk.addItem(fdItm);
	}

	@Override
	public void orderItemSelected(TbOrderItem odi1) {
		pnOdi.showOrderItem(odi1);
		showPanel(PNAME_ITEM_DETAIL);
	}

	@Override
	public void itemInfoUpdated() {
		pnTk.itemHasUpdated();
		boolean validFlg = pnOdi.hasValidOption();
		enableComponents(validFlg);
	}

	@Override
	public Frame getFrame() {
		return scrIntf.getFrame();
	}

	@Override
	public void showScrMain() {
		int _ordNo = pnSplit.getSelectedOrderNo();
		pnTk.selectOrderByOrderNo(_ordNo);
		showScreen(SCR_MAIN);
	}

	@Override
	public void ticketNoClicked() {
		splitTicket();
	}

	@Override
	public void deleteItem() {
		pnTk.deleteItem();
		showPanel(PNAME_MENU);
	}

	@Override
	public void holdItem() {
		pnTk.holdItem();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ("bt_done".equals(cmd)) {
			if (pnTk.noNeedToSave()) {
				if (pnTk.getTblId() > 0) {
					try {
						TxSctTable.lockTable(pnTk.getTblId(), false);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				scrIntf.showMainScreen(false);
				return;
			}
			//
			if (OrderType.DineIn == pnTk.getOrdType()) {
				if (pnTk.saveOrders()) {
					try {
						int tblId = pnTk.getTblId();
						TxSctTable.updateServiceStatus(tblId, TableService.Service);
						TxSctTable.lockTable(tblId, false);
						scrIntf.showMainScreen(false);
					} catch (Exception excp) {
						excp.printStackTrace();
					}
				} else {
					System.out.println("errort: can not save dinein order.");
				}
			} else if (OrderType.ToGo == pnTk.getOrdType()) {
				String chkStr = pnTk.checkOrderInfo();
				System.out.printf("chkStr [%s]\n", chkStr);
				if (!chkStr.equals("Ok")) {
					DlgBox dlgChk = new DlgBox(scrIntf.getFrame());
					dlgChk.showDialog("Incomplete Order", 
						String.format("<html>Incomplete Order,<br><b>%s</b></html>", chkStr), 
						DlgType.Warning);
					return;
				}
				// if order information is correct, then save order.
				pnTk.checkCurrentOrder();
				if (pnTk.saveOrders()) {
					TbOrder cord = pnTk.getCurrentOrder();
					try {
						DlgPayment dlgPm = null;
						int retFlg = 0;
						do {
							DlgPayToGo dlgPay = new DlgPayToGo(scrIntf.getFrame());
							dlgPay.showDialog();
							if ("bt_cancel".equals(dlgPay.getUsrRsp())) {
								return;
							} else if ("pay_now".equals(dlgPay.getUsrRsp())) {
								if (null == dlgPm) {
									dlgPm = new DlgPayment(scrIntf.getFrame());
								}
								dlgPm.showDialog(cord);
								if (dlgPm.paid()) {
									break;
								}
							} else if ("pay_later".equals(dlgPay.getUsrRsp())) {
								break;
							}
						} while (retFlg == 0);
						scrIntf.showMainScreen(false);
					} catch (Exception e1) {
						e1.printStackTrace();
					} 
				} else {
					System.out.println("error: can not save dinein order.");
				}
			}
		} else if ("bt_check".equals(cmd)) {
			pnTk.checkCurrentOrder();
			if (pnTk.saveOrders()) {
				int tblId = pnTk.getTblId();
				try {
					if (pnTk.allOrdersChecked()) {
						TxSctTable.updateServiceStatus(tblId, TableService.Checked);
					}
					TxSctTable.lockTable(tblId, false);
					scrIntf.showMainScreen(false);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		} else if ("bt_menu".equals(cmd)) {
			showPanel(PNAME_MENU);
		} else if ("bt_deli".equals(cmd)) {
			DlgInpNum dlgNum = new DlgInpNum(scrIntf.getFrame());
			dlgNum.showDialog("Delivery Charge?", pnTk.getDeliFee());
			if ("bt_ok".equals(dlgNum.getUsrRsp())) {
				pnTk.setDeliFee(dlgNum.getDoubleValue());
			}
		} else if ("bt_cust".equals(cmd)) {
			updateCustomer();
		} else if ("bt_disc".equals(cmd)) {
			updateDiscount();
		} else if ("bt_charge".equals(cmd)) {
			updateCharge();
		} else if ("bt_split".equals(cmd)) {
			splitTicket();
		} else if ("bt_cancel".equals(cmd)) {
			pnTk.clearEmptyOrder();
			if (OrderType.DineIn == pnTk.getOrdType()) {
				try {
					int tblId = pnTk.getTblId();
					TxSctTable.lockTable(tblId, false);
				} catch (Exception excp) {
					excp.printStackTrace();
				}
			}
			scrIntf.showMainScreen(true);
		}
	}

	@Override
	public void moveItemSeq(int stp) {
		pnTk.moveItemSeq(stp);
	}

	public java.util.Vector<TbOrder> getOrders() {
		java.util.Vector<TbOrder> values = new java.util.Vector<>();
		for (TbOrder ord1 : pnTk.getOrders()) {
			if (ord1.getOrdId() > 0 && ord1.isActive()) {
				values.add(ord1);
			}
		}
		return values.size()==0 ? null : values;
	}

	public boolean isDineIn() {
		return OrderType.DineIn == pnTk.getOrdType();
	}

	public int getSrvTblId() {
		return pnTk.getTblId();
	}

	@Override
	public void itemHasDeleted() {
		enableComponents(true);
	}
}
