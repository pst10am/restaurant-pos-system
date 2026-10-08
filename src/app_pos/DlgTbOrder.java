package app_pos;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import model.AddrUS;
import model.TbCust;
import model.TbOrder;
import refx.DCType;
import refx.OrderType;
import refx.ToGoType;
import resrc.StdFont;

public class DlgTbOrder extends JDialog implements ActionListener, OrdUpdIntf {
	private static final long serialVersionUID = 1L;
	
	private JTabbedPane tab1;
	private PnOrdCust pnCst;
	private PnOrdDC pnDisc, pnCharge;
	private JLabel lbOrdNo;
	
	private TbOrder ordObj;
	
	// -----constructor-----
	
	public DlgTbOrder(Frame _frame) {
		super(_frame, "#-", true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		
		initComponents();
		
		this.pack();
	}
	
	// -----private-----
	
	private void initComponents() {
		
		lbOrdNo = new JLabel("#?");
		lbOrdNo.setFont(StdFont.Fnt22);
		lbOrdNo.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 10));
		
		this.getContentPane().add(lbOrdNo, BorderLayout.PAGE_START);
		
		tab1 = new JTabbedPane();
		tab1.setFocusable(false);
		tab1.setFont(StdFont.Fnt20);
		tab1.addTab(" Customer ", crPnCustomer());
		tab1.addTab(" Discount ", crPnDiscount());
		tab1.addTab(" Charge ", crPnCharge());
		tab1.addChangeListener(new ChangeListener() {
		      public void stateChanged(ChangeEvent changeEvent) {
		        int idx = tab1.getSelectedIndex();
		        _tabSelected(idx);
		      }
		    });
		this.getContentPane().add(tab1, BorderLayout.CENTER);
		crPnCmd();
	}
	
	private JPanel crPnCustomer() {
		pnCst = new PnOrdCust(this);
		return pnCst;
	}
	
	private JPanel crPnDiscount() {
		pnDisc = new PnOrdDC(this, DCType.Discount);
		return pnDisc;
	}
	
	private JPanel crPnCharge() {
		pnCharge = new PnOrdDC(this, DCType.Charge);
		return pnCharge;
	}
	
	private void crPnCmd() {
		// Command
		
		Button btFind = Button.newButton("Find,bt_find", this);
		Button btNewCst = Button.newButton("*Cust,bt_new_cust", this);
		Button btOk = Button.newOk(this);

		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		pnCmd.add(btFind);
		pnCmd.add(btNewCst);
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.add(btOk);

		this.getContentPane().add(pnCmd, BorderLayout.PAGE_END);
	}
	
	private void _tabSelected(int idx) {
		//System.out.printf("> tab selected -> [%d]\n", idx);
	}
	
	private void _setOrder(TbOrder ord1) {
		ordObj = ord1;
		
		// customer
		pnCst.setOrder(ordObj);
		// discount
		pnDisc.setOrder(ordObj);
		// charge
		pnCharge.setOrder(ordObj);

		updateOrderInfo();
	}
	
	// -----public-----
	
	public void showCustomer(TbOrder ord1) {
		_setOrder(ord1);
		tab1.setSelectedIndex(0);
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}
	
	public void showDiscount(TbOrder ord1) {
		_setOrder(ord1);
		tab1.setSelectedIndex(1);
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}
	
	public void showCharge(TbOrder ord1) {
		_setOrder(ord1);
		tab1.setSelectedIndex(2);
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}

	@Override
	public Dialog getDialog() {
		return this;
	}

	@Override
	public void updateOrderInfo() {
		this.setTitle(ordObj.getCstTxt());
		
		//String title = "";
		if (OrderType.DineIn == ordObj.getOrdType()) {
			//title = String.format("#%d Table: %s", ord1.getOrdNo(), ord1.getTblName());
			tab1.setTitleAt(0, String.format(" Table: %s ", ordObj.getTblName()));
		} else if (OrderType.ToGo == ordObj.getOrdType()) {
			//title = String.format("#%d %s: %s", ord1.getOrdNo(), ord1.getOrdToGoType(), ord1.getCstName());
			tab1.setTitleAt(0, " Customer ");
		} else {
			//title = String.format("#%d Error Undefined Order Type", ord1.getOrdNo());
			tab1.setTitleAt(0, " !Customer! ");
		}
		tab1.validate();

		lbOrdNo.setText(String.format("#%d", ordObj.getOrdNo()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ("bt_ok".equals(cmd)) {
			this.dispose();
		} else if ("bt_find".equals(cmd)) {
			DlgFindCust dlg1 = new DlgFindCust(this);
			dlg1.showDialog();
			if ("bt_ok".equals(dlg1.getUsrRsp())) {
				pnCst.setCustomer(dlg1.getCustomer());
				updateOrderInfo();
			}
		} else if ("bt_new_cust".equals(cmd)) {
			pnCst.setCustomer(TbCust.newInstance());
			updateOrderInfo();
		}
	}
	
	// -----main-----

	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
		
		DlgTbOrder dlg1 = new DlgTbOrder(frm1);

		// Dine In
		/*
		TxSctTable tbl1 = TxSctTable.newObj();
		tbl1.setTblName("T5");
		TbOrder ord1 = TbOrder.newDineInOrder(tbl1);
		*/
		
		// ToGo
		TbOrder ord1 = TbOrder.newToGoOrder();
		
		dlg1.showCustomer(ord1);

		if (OrderType.DineIn == ord1.getOrdType()) {
			System.out.println(">dine-in");
			System.out.printf("      table: %s\n", ord1.getTblName());
			System.out.printf(" party size: %d\n", ord1.getPartySize());
		} else if (OrderType.ToGo == ord1.getOrdType()) {
			System.out.printf(">togo: %s\n", ord1.getOrdToGoType());
			if (ToGoType.Delivery == ord1.getOrdToGoType()) {
				model.AddrUS addr = AddrUS.fromOrder(ord1);
				System.out.println(">address");
				System.out.printf(" - [%s]\n", addr.toStringWithUnit());
			}
		} else {
			System.out.println(">error order type<");
		}
		
		System.out.println();
		System.out.println(">customer");
		System.out.printf("    id: %s\n", ord1.getCstId());
		System.out.printf("  name: %s\n", ord1.getCstName());
		System.out.printf(" phone: %s\n", ord1.getCstPhone());
		System.out.printf(" email: %s\n", ord1.getCstEmail());
		System.out.println();
		System.out.println(">discount");
		for (model.TbOrderDC odc1 : ord1.getDiscounts()) {
			System.out.printf(" - [%s=%.2f]\n", odc1.getDcDesc(), odc1.getOdcAmt());
		}
		System.out.println();
		System.out.println(">charge");
		for (model.TbOrderDC odc1 : ord1.getCharges()) {
			System.out.printf(" - [%s=%.2f]\n", odc1.getDcDesc(), odc1.getOdcAmt());
		}
		
		
		System.exit(0);
	}
}
