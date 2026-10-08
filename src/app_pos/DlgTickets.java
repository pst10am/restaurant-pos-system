package app_pos;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import print.PrnModel;
import print.RcpPrinter;
import model.TbOrder;
import model.TbShift;
import refx.DlgType;
import refx.OrderType;
import refx.PrintTo;
import refx.RptSaleType;
import resrc.ResData;
import resrc.StdFont;

public class DlgTickets extends JDialog 
		implements ActionListener {
	private static final long serialVersionUID = 1L;

	private String ordPaid = "NA";
	private String ordType = "NA";
	
	private JLabel lbInfo;
	
	private LstMdSelOrd ordLstMd;
	private JList<TbOrder> ordLst;
	private JTextArea ordTxt;
	
	//

	private DlgTickets(Frame _pr) {
		super(_pr, "Tickets", true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		initComponents();
		
		this.pack();
		this.setResizable(false);
		this.setSize(740, 550);
	}
	
	private JToggleButton crTgBt(String title) {
		JToggleButton bt1 = new JToggleButton(title);
		
		bt1.setFont(StdFont.Fnt16);
		bt1.setHorizontalAlignment(SwingConstants.RIGHT);
		bt1.setFocusable(false);
		bt1.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#A1A1A1")),
			BorderFactory.createEmptyBorder(10, 15, 10, 15)
			));
		
		bt1.setMaximumSize(new Dimension(
			Short.MAX_VALUE, bt1.getPreferredSize().height));
		
		bt1.setActionCommand(title);
		bt1.addActionListener(this);
		
		return bt1;
	}
	
	private void initComponents() {
		
		// -- Payment --
		
		JPanel pn1a = new JPanel();
		pn1a.setLayout(new BoxLayout(pn1a, BoxLayout.PAGE_AXIS));
		
		ButtonGroup bg2 = new ButtonGroup();
		
		JToggleButton btUnPaid = crTgBt("Un-Paid");
		btUnPaid.setHorizontalAlignment(SwingConstants.LEFT);
		
		JToggleButton btPaid = crTgBt("Paid");
		btPaid.setHorizontalAlignment(SwingConstants.LEFT);
		
		pn1a.add(btUnPaid);
		pn1a.add(btPaid);
		
		bg2.add(btUnPaid);
		bg2.add(btPaid);
		
		// -- Order Type --
		
		JPanel pn1b = new JPanel();
		pn1b.setLayout(new BoxLayout(pn1b, BoxLayout.PAGE_AXIS));
		
		ButtonGroup bg1 = new ButtonGroup();
		for (int x=0; x < RptSaleType.size; x++) {
			RptSaleType rst1 = RptSaleType.values()[x];
			
			JToggleButton bt1 = crTgBt(rst1.toString());
			
			pn1b.add(bt1);
			bg1.add(bt1);
		}
		pn1b.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.decode("#A1A1A1")));
		
		JPanel pnLeft = new JPanel();
		pnLeft.setLayout(new BoxLayout(pnLeft, BoxLayout.PAGE_AXIS));
		pnLeft.setBackground(Color.decode("#669999"));
		
		pnLeft.add(pn1a);
		pnLeft.add(Box.createVerticalStrut(10));
		pnLeft.add(pn1b);
		
		pnLeft.add(Box.createVerticalGlue());
		pnLeft.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.decode("#336699")));
		
		JPanel pnCt = new JPanel(new BorderLayout());
		pnCt.add(pnLeft, BorderLayout.LINE_START);
		pnCt.add(crPnItems(), BorderLayout.CENTER);
		
		this.getContentPane().add(pnCt, BorderLayout.CENTER);
		
		// command

		Button btClose = Button.newButton("Close,bt_close", this);

		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.add(btClose);
		pnCmd.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

		this.add(pnCmd, BorderLayout.PAGE_END);
	}
	
	private JButton crBt(String txt) {
		JButton bt1 = new JButton(txt);
		bt1.setFont(StdFont.Fnt14);
		bt1.setFocusable(false);
		bt1.setActionCommand(txt);
		bt1.addActionListener(this);
		return bt1;
	}
	
	private JPanel crPnItems() {
		
		JPanel pnInfo = new JPanel(new FlowLayout(FlowLayout.LEADING, 12, 10));
		lbInfo = new JLabel("-");
		lbInfo.setFont(StdFont.Fnt14B);
		pnInfo.add(lbInfo);
		pnInfo.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
		
		//
		
		ordLstMd = new LstMdSelOrd();
		ordLst = new JList<>(ordLstMd);
		ordLst.setFont(StdFont.Fnt12);
		ordLst.setVisibleRowCount(-1);
		ordLst.setCellRenderer(new LstRdrTbOrder());
		ordLst.setFocusable(false);
		ordLst.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ordLst.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) return;
				//
				ListSelectionModel smd = ordLst.getSelectionModel();
				if (smd.isSelectionEmpty()) return;
				//
				_showOrderDetail();
			}
		});
		
		JScrollPane scpOrdLst = new JScrollPane(ordLst, 
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scpOrdLst.setFocusable(false);
		scpOrdLst.setBorder(null);
		scpOrdLst.getVerticalScrollBar().setPreferredSize(
			new Dimension(30, scpOrdLst.getVerticalScrollBar().getPreferredSize().height));
		scpOrdLst.setPreferredSize(new Dimension(285, scpOrdLst.getPreferredSize().height));
		
		//
		
		ordTxt = new JTextArea();
		ordTxt.setMargin(new Insets(5, 5, 5, 5));
		ordTxt.setFont(StdFont.Fnt12Mono);
		ordTxt.setFocusable(false);
		ordTxt.setBackground(Color.decode("#F0F5F5"));
		JScrollPane scpOrdTxt = new JScrollPane(ordTxt);
		scpOrdTxt.getVerticalScrollBar().setPreferredSize(
				new Dimension(30, scpOrdTxt.getVerticalScrollBar().getPreferredSize().height));
		scpOrdTxt.setBorder(null);
		
		//
		
		JPanel pnOrdCmd = new JPanel();
		pnOrdCmd.setLayout(new BoxLayout(pnOrdCmd, BoxLayout.LINE_AXIS));
		pnOrdCmd.add(Box.createHorizontalGlue());
		pnOrdCmd.add(crBt("RePrint"));
		pnOrdCmd.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(1, 0, 0, 0, Color.decode("#979797")),
			BorderFactory.createEmptyBorder(5, 10, 5, 5)));
		
		JPanel pnOrd = new JPanel(new BorderLayout());
		pnOrd.add(scpOrdTxt, BorderLayout.CENTER);
		pnOrd.add(pnOrdCmd, BorderLayout.PAGE_END);
		
		//
		
		JPanel pnItm = new JPanel(new BorderLayout());
		
		pnItm.add(pnInfo, BorderLayout.PAGE_START);
		pnItm.add(scpOrdLst, BorderLayout.LINE_START);
		pnItm.add(pnOrd, BorderLayout.CENTER);
		
		pnItm.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(5, 5, 5, 5, Color.decode("#979797")),
			BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY)));
		return pnItm;
	}
	
	private void updateTickets() {
		ordTxt.setText("");
		ordLstMd.clear();
		ordLst.clearSelection();
		if ("NA".equals(ordPaid) || "NA".equals(ordType)) {
			return;
		}
		//
		lbInfo.setText(String.format("%s / %s", ordPaid, ordType));
		
		final RptSaleType rtype = RptSaleType.valueOf(ordType);
		
		Thread trd1 = new Thread() {
			public void run() {
				_onProcess(true);
				try {
					TbShift sh1 = TbShift.findLastShift();
					//System.out.printf("last shift -> %s, %s\n", sh1.getClId(), sh1.getClDt());
					TbOrder[] ords = TbOrder.findOrderByPayAndType(
						ordPaid.equals("Paid"), 
						rtype.getOrderType(), 
						rtype.getToGoType(),
						sh1.getClDt(),
						new java.util.Date());
					if (null == ords || ords.length <= 0) {
						ordTxt.setText("-No Orders-");
						_onProcess(false);
						return;
					}
					ordLstMd.setItems(ords);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				_onProcess(false);
			}
		};
		trd1.start();
	}
	
	private void _onProcess(boolean flg) {
		ordLst.setEnabled(!flg);
		if (flg) {
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		} else {
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	private void _showOrderDetail() {
		if (ordLst.getSelectedIndex() < 0) {
			return;
		}
		TbOrder ord1 = ordLst.getSelectedValue();
		//System.out.printf("selected order no -> #%d\n", ord1.getOrdNo());
		ordTxt.setText(PrnModel.printTbOrder(ord1));
	}
	
	private void _showError(String msg) {
		DlgBox dlgx = new DlgBox(this);
		dlgx.showDialog("Error", msg, DlgType.Critical);
	}
	
	private void fnTicket(String fnStr) {
		if (ordLst.getSelectedIndex() < 0) {
			return;
		}
		if (!"RePrint".equals(fnStr)) {
			return;
		}
		
		final String[] prnOpts = {
			"Kitchen", 
			ordType.equals("DineIn") ? "Runner" : "Packing", 
			"Check/Receipt"};
		
		DlgSelOpt dlgOpt = DlgSelOpt.newInstance(this, "Select Print Option", prnOpts);
		dlgOpt.showDialog();
		System.out.println(dlgOpt.getUsrRsp());
		if (!"bt_ok".equals(dlgOpt.getUsrRsp())) {
			return;
		}
		
		String cmd1 = dlgOpt.getUsrSelOpt();
		
		TbOrder ord1 = ordLst.getSelectedValue();
		try {
			final TbOrder ord2 = TbOrder.getTbOrderByOrdNo(ord1.getOrdNo());
			if (!ord2.isActive()) {
				_showError(String.format("Order# [%d] has been deleted.", ord2.getOrdNo()));
				return;
			}
			if ("Kitchen".equals(cmd1)) {
				Thread trd1 = new Thread() {
					public void run() {
						try {
							RcpPrinter.printKitchen(ord2, true);
						} catch (Exception e) {
							_showError(e.getMessage());
						}
					}
				};
				trd1.start();
			} else if ("Runner".equals(cmd1)) {
				Thread trd1 = new Thread() {
					public void run() {
						try {
							RcpPrinter.printRunner(ord2, true);
						} catch (Exception e) {
							_showError(e.getMessage());
						}
					}
				};
				trd1.start();
			} else if ("Packing".equals(cmd1)) {
				Thread trd1 = new Thread() {
					public void run() {
						try {
							RcpPrinter.printOrderTo(ord2, PrintTo.Packing);
						} catch (Exception e) {
							_showError(e.getMessage());
						}
					}
				};
				trd1.start();
			} else if ("Check/Receipt".equals(cmd1)) {
				if (!ord2.isChecked()) {
					_showError(String.format("Order# [%d] is not checked.", ord2.getOrdNo()));
					return;
				}
				Thread trd1 = new Thread() {
					public void run() {
						try {
							RcpPrinter.printOrderTo(ord2, PrintTo.Cashier);
						} catch (Exception e) {
							_showError(e.getMessage());
						}
					}
				};
				trd1.start();
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		updateTickets();
	}
	
	// --
	
	public static DlgTickets newInstance(Frame _pr) {
		return new DlgTickets(_pr);
	}
	
	public void showDialog() {
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ("bt_close".equals(cmd)) {
			this.dispose();
		} else if ("Un-Paid".equals(cmd)) {
			ordPaid = "Un-Paid";
			updateTickets();
		} else if ("Paid".equals(cmd)) {
			ordPaid = "Paid";
			updateTickets();
		} else if ("RePrint".equals(cmd)) {
			fnTicket("RePrint");
		} else {
			RptSaleType rst1 = RptSaleType.valueOf(cmd);
			ordType = rst1.toString();
			updateTickets();
		}
	}

	public static void main(String[] args) {
		
		{
			ResData.status();
		}
		
		MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
		
		javax.swing.JFrame frm1 = new javax.swing.JFrame("Test Dialog");
		frm1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		frm1.pack();
		
		frm1.setSize(1024, 768);
		frm1.setLocationRelativeTo(null);
		frm1.setVisible(true);
		
		DlgTickets dlgTk = DlgTickets.newInstance(frm1);
		dlgTk.showDialog();
		
		System.out.println("done.");
		System.exit(0);
	}
}
