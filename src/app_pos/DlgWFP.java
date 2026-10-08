package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import refx.AmountType;
import refx.DlgType;
import refx.OrderType;
import refx.ToGoType;
import resrc.ResData;
import resrc.ResUtil;
import resrc.StdFont;
import model.TbOrder;
import model.TbOrderDC;
import model.TbOrderItem;

public class DlgWFP extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private String usrRsp = "NA";
	private LstMdSelOrd mdItm;
	private JList<TbOrder> lstItm;
	private JTextArea taDt;
	private JButton btPick;
	
	// -----constructor-----
	
	public DlgWFP(Frame _pr) {
		super(_pr, "Select Order", true);
		initComponents();
	}
	
	// -----private-----
	
	private void initComponents() {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		
		mdItm = new LstMdSelOrd();
		lstItm = new JList<>(mdItm);
		lstItm.setFont(StdFont.Fnt16);
		lstItm.setVisibleRowCount(-1);
		lstItm.setCellRenderer(new LstRdrOrdWFP());
		lstItm.setFocusable(false);
		lstItm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstItm.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) return;
				//
				ListSelectionModel smd = lstItm.getSelectionModel();
				if (smd.isSelectionEmpty()) return;
				//
				_showOrderDetail();
			}
		});
		
		JScrollPane scpLt = new JScrollPane(lstItm, 
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scpLt.setFocusable(false);
		scpLt.setBorder(null);
		scpLt.getVerticalScrollBar().setPreferredSize(
			new Dimension(30, scpLt.getVerticalScrollBar().getPreferredSize().height));
		
		this.getContentPane().add(scpLt, BorderLayout.LINE_START);
		
		taDt = new JTextArea();
		taDt.setFont(StdFont.Fnt14Mono);
		taDt.setMargin(new Insets(5, 5, 5, 5));
		taDt.setFocusable(false);
		taDt.setEditable(false);
		taDt.setText("- order detail -");
		JScrollPane scpDt = new JScrollPane(taDt,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scpDt.setFocusable(false);
		scpDt.setBorder(null);
		scpDt.getVerticalScrollBar().setPreferredSize(
			new Dimension(30, scpDt.getVerticalScrollBar().getPreferredSize().height));
		
		JPanel pnDt = new JPanel(new BorderLayout());
		pnDt.add(scpDt, BorderLayout.CENTER);
		
		btPick = new JButton("Picked Up/Delivered");
		btPick.setFont(StdFont.Fnt16);
		btPick.setFocusable(false);
		btPick.setActionCommand("order_is_picked");
		btPick.addActionListener(this);
		btPick.setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)
			));
		
		pnDt.add(btPick, BorderLayout.PAGE_END);
		pnDt.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.GRAY));
		
		this.getContentPane().add(pnDt, BorderLayout.CENTER);

		scpLt.setPreferredSize(new Dimension(295, scpLt.getPreferredSize().height));
		scpDt.setPreferredSize(new Dimension(385, 425));
		
		// Command
		
		Button btClose = Button.newButton("Close,bt_close", this);

		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.add(btClose);
		pnCmd.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

		this.add(pnCmd, BorderLayout.PAGE_END);
		
		this.pack();
		this.setLocationRelativeTo(this.getParent());
	}
	
	private void _disposeDialog() {
		this.dispose();
	}
	
	private void _showOrderDetail() {
		TbOrder pord = lstItm.getSelectedValue();
		if (null == pord) {
			return;
		}
		final int ordId = pord.getOrdId();
		Thread trd1 = new Thread(){
			public void run() {
				lstItm.setEnabled(false);
				taDt.setText("loading order detail\nplease wait.\n");
				try {
					TbOrder ord1 = TbOrder.findOrderById(ordId);

					taDt.setText(PosReport.genOrderDetail(ord1));
					
					
					/* ################################## */

					/*
					String _typeStr = OrderType.DineIn.toString();
					if (OrderType.ToGo == ord1.getOrdType()) {
						_typeStr = ord1.getOrdToGoType().toString();
					}
					taDt.append(ResUtil.padl("*"+_typeStr+"*", 24, ' ')+"\n");
					
					if (ord1.isPaid()) {
						taDt.append("[$PAID$]\n");
					} else {
						taDt.append(">CHECK<\n");
					}
					
					taDt.append(String.format("Order: %d\n", ord1.getOrdNo()));
					
					// Customer/Table 
					if (OrderType.DineIn == ord1.getOrdType()) {
						taDt.append(String.format("Table: %s\n", ord1.getTblName()));
					} else {
						taDt.append(String.format("Name: %s\n", ord1.getCstName()));
						taDt.append(String.format("Phone: %s\n", ResUtil.formatPhone(ord1.getCstPhone())));
					}
					

					// Delivery
					if ((OrderType.ToGo == ord1.getOrdType()) && 
						(ToGoType.Delivery == ord1.getOrdToGoType())) {
						taDt.append(ResUtil.padStr(40, '-') +"\n");
						taDt.append("Address:\n");
						taDt.append(String.format("%s", ord1.getDeliAddr1()));
						if (ord1.getDeliAddr2().trim().length() > 0) {
							taDt.append(String.format(" %s", ord1.getDeliAddr2()));
						}
						if (ord1.getDeliUnitNo().trim().length() > 0) {
							taDt.append(String.format(" APT.  %s", ord1.getDeliUnitNo()));
						}
						taDt.append(String.format(" %s, %s", ord1.getDeliCity(), ord1.getDeliState()));
						taDt.append(String.format(" (%.2f miles.)\n", ord1.getDeliDist()));
					}
					
					if (!ord1.getCstNote().trim().isEmpty()) {
						taDt.append(ResUtil.padStr(40, '-')+"\n");
						taDt.append("Note:\n");
						taDt.append(ord1.getCstNote()+"\n");
					}
					
					taDt.append(ResUtil.padStr(40, '-')+"\n");
					taDt.append(String.format("Date/Time:%s\n", ResUtil.dtoc(ord1.getOrdDt(), "MM/dd/yyyy hh:mm:ss aaa")));

					if (OrderType.ToGo == ord1.getOrdType()) {
						// promise date-time.
						taDt.append("Promise Time:");
						java.util.Date prmDt = null;
						if (ToGoType.Delivery == ord1.getOrdToGoType()) {
							// delivery: 1 hour.
							prmDt = ResUtil.dtadd(ord1.getOrdDt(), 1, Calendar.HOUR_OF_DAY);
						} else {
							// waiting, pickup: 20 minutes.
							prmDt = ResUtil.dtadd(ord1.getOrdDt(), 20, Calendar.MINUTE);
						}
						taDt.append(ResUtil.dtoc(prmDt, "MM/dd/yyyy hh:mm:ss aaa")+"\n");
					}
					
					taDt.append(ResUtil.padStr(40, '=')+"\n");
					
					double tmp_sub_total = 0;
					double tmp_tax = 0;
					
					for (int i=0; i < ord1.getItemCount(); i++) {
						if (i > 0) {
							taDt.append(ResUtil.padStr(40, '-')+"\n");
						}
						TbOrderItem odi1 = ord1.getItems().get(i);

						taDt.append(String.format("[%s] $[%.2f] %s\n", 
							odi1.getOdiQty(), 
							odi1.getOdiAmtBf(), 
							odi1.getItmNamePos()));
						
						if (!odi1.getOpiStr().isEmpty()) {
							taDt.append(odi1.getOpiStr()+"\n");
						}
						if (!odi1.getOdiNote().trim().isEmpty()) {
							taDt.append(String.format("Note:%s\n", odi1.getOdiNote()));
						}
						//
						tmp_sub_total += odi1.getOdiAmtBf();
						tmp_tax += odi1.getOdiAmtTax();
					}
					
					double amtDisc = 0;
					if (ord1.getDiscounts().size() > 0) {
						taDt.append(ResUtil.padStr(40, '-')+"\n");
						taDt.append("Discount:\n");
						for (TbOrderDC dc1 : ord1.getDiscounts()) {
							double val1 = 0;
							if (AmountType.Percent == dc1.getDcAmtType()) {
								val1 = (tmp_sub_total+tmp_tax) * ((dc1.getDcAmt())/100);
							} else if (AmountType.Fixed == dc1.getDcAmtType()) {
								val1 = dc1.getDcAmt();
							}
							taDt.append(ResUtil.padStr(40, '-')+"\n");
							taDt.append(String.format("$%.2f (%s)%s\n",
								val1,
								dc1.getDcAmtType(),
								dc1.getDcDesc()));
							amtDisc += dc1.getOdcAmt();
						}
					}
					
					double amtCharge = 0;
					if (ord1.getCharges().size() > 0) {
						taDt.append(ResUtil.padStr(40, '-')+"\n");
						taDt.append("Charge:\n");
						for (TbOrderDC dc1 : ord1.getCharges()) {
							double val1 = 0;
							if (AmountType.Percent == dc1.getDcAmtType()) {
								val1 = (tmp_sub_total+tmp_tax) * ((dc1.getDcAmt())/100);
							} else if (AmountType.Fixed == dc1.getDcAmtType()) {
								val1 = dc1.getDcAmt();
							}
							taDt.append(ResUtil.padStr(40, '-')+"\n");
							taDt.append(String.format("$%.2f (%s)%s\n",
								val1,
								dc1.getDcAmtType(),
								dc1.getDcDesc()));
							amtCharge += dc1.getOdcAmt();
						}
					}
					
					taDt.append(ResUtil.padStr(40, '=')+"\n");
					
					// sub total
					taDt.append(String.format("Sub Total: $%.2f\n", tmp_sub_total));
					
					// tax
					taDt.append(String.format("Tax: $%.2f\n", tmp_tax));
					
					if (amtDisc > 0) {
						taDt.append(String.format("Discount: $%.2f\n", amtDisc));
					}
					
					if (amtCharge > 0) {
						taDt.append(String.format("Charge: $%.2f\n", amtCharge));
					}
					
					// delivery
					if (ToGoType.Delivery == ord1.getOrdToGoType()) {
						taDt.append(String.format("Delivery: $%.2f\n", ord1.getDeliFee()));
					}
					
					// total
					double netTotal = (tmp_sub_total+tmp_tax+ord1.getDeliFee()) - amtDisc;
					taDt.append(String.format("Total: $%.2f\n", netTotal));
					
					taDt.append("\n");
					*/

					/* ################################## */
					
				} catch (Exception e) {
					e.printStackTrace();
					taDt.setText(String.format("Error:\n%s", e.getMessage()));
				}
				lstItm.setEnabled(true);
			}
		};
		trd1.start();
	}
	
	private void _orderIsPicked() {
		if (lstItm.getSelectedIndex() < 0) {
			return;
		}
		TbOrder pord = lstItm.getSelectedValue();
		if (!pord.isPaid()) {
			return;
		}
		try {
			pord.picked();
			int selIdx = lstItm.getSelectedIndex();
			mdItm.removeElementAt(selIdx);
			taDt.setText("");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// -----public-----
	
	public void showDialog() {
		try {
			TbOrder[] datas = TbOrder.getTbOrderWFP();
			
			mdItm.clear();
			mdItm.setItems(datas);

			this.setVisible(true);
			
		} catch (SQLException e) {
			e.printStackTrace();
			//
			DlgBox dlgx = new DlgBox(this);
			dlgx.showDialog("Error", 
				String.format("<html>Exception:<br>%s</html>",  e.getMessage()), 
				DlgType.Warning);
		}
	}
	
	public String getUsrRsp() {
		return usrRsp;
	}
	
	public TbOrder getSelectedValue() {
		return lstItm.getSelectedValue();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		usrRsp = e.getActionCommand();
		if ("bt_close".equals(usrRsp)) {
			_disposeDialog();
		} else if ("order_is_picked".equals(usrRsp)) {
			_orderIsPicked();
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
		
		DlgWFP dlg1 = new DlgWFP(frm1);
		dlg1.showDialog();
				
		System.exit(0);
	}
}
