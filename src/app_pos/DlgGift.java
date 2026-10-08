package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import model.CCData;
import model.TbGift;
import model.TbGiftTrx;
import refx.DlgType;
import resrc.ResData;
import resrc.StdFont;

public class DlgGift extends JDialog implements ActionListener, MSRCCIntf {
	private static final long serialVersionUID = 1L;
	
	private JButton btGiftNo;
	private JLabel lbBal;
	private Button btPrint, btRefill;
	
	private TbGift gft1;
	
	private LstMdGiftTrx mdItm;
	private JList<TbGiftTrx> lstItm;

	
	// -----constructor-----
	
	private DlgGift(Frame _pr) {
		super(_pr, "Gift Card", true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		initComponents();
		
		this.pack();
		this.setResizable(false);
		this.setSize(615, 500);
		this.setLocationRelativeTo(_pr);
		//this.setVisible(true);
	}
	
	public static DlgGift newInstance(Frame _pr) {
		return new DlgGift(_pr);
	}
	
	// -----private-----
	
	private void initComponents() {
		crPnTop();
		crPnGiftHist();
		crPnCmd();
		this.addKeyListener(new MSRGiftLst(this));
	}
	
	private void crPnTop() {
		
		// Gift No.
		
		btGiftNo = FctButton.newBtGiftNo(
			"Swipe or input Gift Card No. (Click Here.)", 
			"bt_gift_no", this);
		
		// Balance
		
		lbBal = new JLabel("0.00");
		lbBal.setFont(StdFont.Fnt18B);
		lbBal.setForeground(Color.WHITE);
		
		JLabel _lb1 = new JLabel("Balance");
		_lb1.setFont(lbBal.getFont());
		_lb1.setForeground(Color.WHITE);
		
		JPanel pnBal = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
		pnBal.add(_lb1);
		pnBal.add(Box.createHorizontalStrut(10));
		pnBal.add(lbBal);
		pnBal.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY),
			BorderFactory.createEmptyBorder(10, 20, 10, 20)));
		pnBal.setBackground(Color.decode("#338533"));
		
		// Put it all together.
		
		JPanel pnTop = new JPanel(new BorderLayout());
		pnTop.add(btGiftNo, BorderLayout.PAGE_START);
		pnTop.add(pnBal, BorderLayout.PAGE_END);

		this.getContentPane().add(pnTop, BorderLayout.PAGE_START);
	}
	
	private void crPnGiftHist() {
		
		mdItm = new LstMdGiftTrx();
		lstItm = new JList<>(mdItm);
		lstItm.setFont(StdFont.Fnt22);
		lstItm.setVisibleRowCount(-1);
		lstItm.setCellRenderer(new LstRdrGiftTrx());
		lstItm.setFocusable(false);
		lstItm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane scp1 = new JScrollPane(lstItm, 
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scp1.setFocusable(false);
		
		scp1.getVerticalScrollBar().setPreferredSize(
			new Dimension(30, scp1.getVerticalScrollBar().getPreferredSize().height));
		
		scp1.setBorder(null);
		scp1.setPreferredSize(new Dimension(330, 256));
		
		this.getContentPane().add(scp1, BorderLayout.CENTER);
	}
	
	private void crPnCmd() {
		
		btPrint = Button.newButton("Print,bt_print", this);
		btPrint.setEnabled(true);
		btRefill = Button.newButton("Add $,bt_add_money", this);
		btRefill.setEnabled(false);
		
		Button btClose = Button.newButton("Close,bt_close", this);

		JPanel pnBt = new JPanel();
		pnBt.setLayout(new BoxLayout(pnBt, BoxLayout.LINE_AXIS));
		pnBt.add(btPrint);
		pnBt.add(btRefill);
		pnBt.add(Box.createHorizontalGlue());
		pnBt.add(btClose);
		
		pnBt.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

		this.getContentPane().add(pnBt, BorderLayout.PAGE_END);
	}
	
	private void _updateCardInfo() {
		btPrint.setEnabled(gft1.getGftId() > 0);
		btRefill.setEnabled(gft1.getGftId() > 0);

		btGiftNo.setText(String.format("Card No. %s", gft1.getGftCode()));
		lbBal.setText(String.format("%.2f", gft1.getGftBal()));

		_showCardTrans();
	}
	
	private boolean _createNewCard(TbGift gft1) {
		try {
			DlgBox dlg1 = new DlgBox(this);
			dlg1.showConfirmDialog("New Card?", 
				String.format("<html>Card No.[<b>%s</b>]<br>Create New Card?</html>", gft1.getGftCode()), 
				DlgType.Information);
			if ("bt_ok".equals(dlg1.getUsrRsp())) {
				gft1.save();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void _inputGiftNo() {
		DlgInpNum dlg1 = new DlgInpNum(this);
		dlg1.showDialog("Gift No?", null == gft1 ? "" : gft1.getGftCode());
		if ("bt_ok".equals(dlg1.getUsrRsp())) {
			String _cardCode = dlg1.getStrValue();
			if (!_cardCode.isEmpty()) {
				gft1 = TbGift.findCard(dlg1.getStrValue());
				if (null == gft1) {
					gft1 = TbGift.newCard(_cardCode);
					if (!_createNewCard(gft1)) {
						return;
					}
				}
			} else {
				gft1.clear();
			}
			_updateCardInfo();
		}
	}
	
	private void _showCardTrans() {
		if (null == gft1 || gft1.getGftId() <= 0) return;
		//
		try {
			TbGiftTrx[] trans = TbGiftTrx.getTransactions(gft1);
			mdItm.setItems(trans);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void _addMoney() {
		if (null == gft1 || gft1.getGftId() <= 0) return;
		//
		DlgPaymentGift dlg1 = new DlgPaymentGift(this);
		dlg1.showDialog(gft1);
		if ("paid".equals(dlg1.getUsrRsp())) {
			try {
				double netPay = dlg1.getNetPay();
				gft1.recharge(netPay);
				gft1.save();
				_updateCardInfo();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	// -----public-----
	
	public void showDialog() {
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}

	@Override
	public void creditCardSwiped(CCData cdt) {
		gft1 = TbGift.findCard(cdt.getTrk1());
		if (null == gft1) {
			gft1 = TbGift.newCard(cdt.getTrk1());
			if (!_createNewCard(gft1)) {
				return;
			}
		}
		_updateCardInfo();
	}

	@Override
	public void cardReadError() {
		DlgBox dlg1 = new DlgBox(this);
		dlg1.showDialog("Error", 
			"<html><center><font size=10>Card read error.</font><br>Please try again</center></html>",
			DlgType.Warning);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ("bt_close".equals(cmd)) {
			this.dispose();
		} else if ("bt_gift_no".equals(cmd)) {
			_inputGiftNo();
		} else if ("bt_add_money".equals(cmd)) {
			_addMoney();
		} else if ("bt_print".equals(cmd)) {
		}
	}
	
	// -----main-----

	public static void main(String[] args) {
		ResData.status();
		try {
			//UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
		} catch (Exception e) {} 
		
		javax.swing.JFrame frm1 = new javax.swing.JFrame("Test Dialog");
		frm1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		javax.swing.JTextArea txt1 = new javax.swing.JTextArea();
		javax.swing.JScrollPane scp1 = new javax.swing.JScrollPane(txt1);
		frm1.getContentPane().add(scp1, BorderLayout.CENTER);
		
		frm1.pack();
		frm1.setSize(1024, 768);
		frm1.setLocationRelativeTo(null);
		frm1.setVisible(true);
		
		DlgGift dlgGft = DlgGift.newInstance(frm1);
		dlgGft.showDialog();
		
		System.exit(0);
	}
}
