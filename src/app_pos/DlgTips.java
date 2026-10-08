package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.usaepay.api.jaxws.TransactionResponse;

import model.TbPayment;
import model.TbSettle;
import refx.CCCapture;
import refx.DlgType;
import refx.PayBy;
import refx.PaySrc;
import resrc.ResData;
import resrc.ResUtil;
import resrc.StdFont;

public class DlgTips extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private LstMdTbPayment lstPmMd;
	private JList<TbPayment> lstPm;
	private Button btSubmit, btClose;
	private DlgInpNum dlgNum;
	private DlgBox dlgMsg;
	
	// Constructor
	
	private DlgTips(Frame _pr) {
		super(_pr, "Adjust Tips", true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		initComponents();
		
		dlgNum = new DlgInpNum(this);
		dlgMsg = new DlgBox(this);
		
		this.pack();
	}
	
	private DlgTips(Dialog _pr) {
		super(_pr, "Adjust Tips", true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		initComponents();
		this.pack();
	}
	
	private void initComponents() {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		
		lstPmMd = new LstMdTbPayment();
		lstPm = new JList<>(lstPmMd);
		lstPm.setFont(StdFont.Fnt14Mono);
		lstPm.setVisibleRowCount(-1);
		lstPm.setCellRenderer(new LstRdrTip());
		lstPm.setFocusable(false);
		lstPm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstPm.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) return;
				//
				ListSelectionModel smd = lstPm.getSelectionModel();
				if (smd.isSelectionEmpty()) return;
				//
				_adjustTip();
			}
		});
		
		JScrollPane scpLt = new JScrollPane(lstPm, 
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scpLt.setFocusable(false);
		scpLt.setBorder(null);
		scpLt.getVerticalScrollBar().setPreferredSize(
			new Dimension(30, scpLt.getVerticalScrollBar().getPreferredSize().height));
		
		this.getContentPane().add(scpLt, BorderLayout.CENTER);

		scpLt.setPreferredSize(new Dimension(490, 355));
		
		// Command
		
		btSubmit = Button.newButton("Submit,bt_submit", this);
		btClose = Button.newButton("Close,bt_close", this);

		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		pnCmd.add(btSubmit);
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
	
	private void _adjustTip() {
		if (lstPm.isSelectionEmpty()) return;
		
		TbPayment pmnt = lstPm.getSelectedValue();
		
		dlgNum.showDialog("Tip?", pmnt.getPmTip());
		if ("bt_ok".equals(dlgNum.getUsrRsp())) {
			double tipAmt = dlgNum.getDoubleValue();
			System.out.printf("tip -> [%.2f]\n", tipAmt);
			boolean tipOk = true;
			if (tipAmt > (pmnt.getPmAmt() * 0.20)) {
				dlgMsg.showConfirmDialog("Tip > 20%", 
					String.format("<html>Warning!<br>Tip [$%.2f] is over 20%% of [$%.2f],<br>Continue?</html>", tipAmt, pmnt.getPmAmt()), 
					DlgType.Warning);
				tipOk = "bt_ok".equals(dlgMsg.getUsrRsp());
			}
			if (!tipOk) {
				return;
			}
			// update tip .. temporary
			pmnt.setPmTip(tipAmt);
			lstPmMd.validateInfo();
		}
	}
	
	private void _submitTips() {
		if (lstPmMd.getSize() <= 0) {
			return;
		}
		final DlgThread dlgTrd = new DlgThread(this);
		dlgTrd.setTextFont(StdFont.Fnt12Mono);
		dlgTrd.setDlgsize(550, 450);
		Thread trd1 = new Thread() {
			public void run() {
				dlgTrd.setExitCode(0);
				dlgTrd.setText("Processing Tip\n");
				dlgTrd.append("------------------------------------------------------------------------\n");
				int tot1 = lstPmMd.getSize();
				for (int x=0; x < tot1; x++) {
					TbPayment v1 = lstPmMd.getElementAt(x);
					try {
						dlgTrd.append(String.format(
			        		"%3d/%3d: Order# %7d xx%s $%8.2f Tip=%8.2f .. "
			        		,(x+1),tot1
							,v1.getOrdNo()
			        		,v1.getCCLast4Digits()
			        		,v1.getPmAmt()
			        		,v1.getPmTip()));

						TransactionResponse resp = PaymentGateway.captureTrans(v1);
						ResData.saveTransactionResponse(v1.getPmNo(), resp);
						if ("A".equals(resp.getResultCode())) {
							v1.setPmCapture(CCCapture.Success);
							v1.save();
							dlgTrd.append("Approved\n");
						} else { // Not Approved
							dlgTrd.append(String.format("!Error!\nCode=%d Result=%s:%s\n", 
								resp.getErrorCode(), resp.getResult(), resp.getError()));
						}

						//dlgTrd.append("Approved\n");
						Thread.sleep(120);
					} catch (Exception e) {
						dlgTrd.append(String.format("!Error![%s]\n", e.getMessage()));
					}
					//double chk1 = (100*(x+1))/tot1;
					//dlgTrd.append(String.format("%.2f%%\n", chk1));
				}
				//dlgTrd.setExitCode(1);
				//dlgTrd.dispose();
				//dlgTrd.append(String.format("Exit Code [%d]", dlgTrd.getExitCode()));
				dlgTrd.append("------------------------------------------------------------------------\n");
				dlgTrd.append("Done.\n");
				dlgTrd.processEnd();
			}
		};
		dlgTrd.showDialog(trd1);
	}
	
	// -----public-----
	
	public static DlgTips newInstance(Frame _pr) {
		return new DlgTips(_pr);
	}
	
	public static DlgTips newInstance(Dialog _pr) {
		return new DlgTips(_pr);
	}
	
	public void showDialog() {
		try {
			TbSettle stl1 = TbSettle.findLastSettle();
			
			System.out.printf(
				"From [%s]\n To [%s]\n", 
				stl1.getStlDt(), 
				new java.util.Date());

			TbPayment[] datas = TbPayment.findTrans(
				PaySrc.Order, PayBy.CreditCard, 
				stl1.getStlDt(), new java.util.Date());

			lstPm.clearSelection();
			lstPmMd.setItems(datas);

			this.setLocationRelativeTo(this.getParent());
			this.setVisible(true);
			
		} catch (SQLException e) {
			DlgBox dlgx = new DlgBox(this);
			dlgx.showDialog("Error", 
				String.format("<html>Error:<br>%s</html>",  e.getMessage()), 
				DlgType.Warning);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String usrRsp = e.getActionCommand();
		if ("bt_close".equals(usrRsp)) {
			_disposeDialog();
		} else if ("bt_submit".equals(usrRsp)) {
			_submitTips();
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
		
		DlgTips dlg1 = DlgTips.newInstance(frm1);
		dlg1.showDialog();
				
		System.exit(0);
	}
}
