package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import model.CCData;
import model.TbGift;
import model.TbPayment;
import refx.PayBy;
import refx.PaySrc;
import resrc.StdFont;

public class DlgPayBy extends JDialog implements ActionListener, MSRCCIntf {
	private static final long serialVersionUID = 1L;
	
	private String usrRsp = "NA";
	private Button btOk = null;
	private Button btBal = null;
	private JLabel lbTitle;

	private PayBy payBy = PayBy.NA;
	private double payAmt = 0;
	
	private PnPayIntf pnPay = null;
	
	private PaySrc psrc = PaySrc.NA;
	
	private boolean canUpdAmt = false;
	
	// -----constructor-----
	
	public DlgPayBy(Frame _pr, PayBy _pby, double _amt, PaySrc _src, boolean _canUpdAmt) {
		super(_pr, "-", true);
		payBy = _pby;
		payAmt = _amt;
		psrc = _src;
		canUpdAmt = _canUpdAmt;
		initComponents();
	}
	
	public DlgPayBy(Dialog _pr, PayBy _pby, double _amt, PaySrc _src, boolean _canUpdAmt) {
		super(_pr, "-", true);
		payBy = _pby;
		payAmt = _amt;
		psrc = _src;
		canUpdAmt = _canUpdAmt;
		initComponents();
	}
	
	// -----private-----
	
	private void initComponents() {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		lbTitle = new JLabel(payBy.getDispName());
		lbTitle.setFont(StdFont.Fnt22B);
		lbTitle.setForeground(Color.WHITE);
		lbTitle.setBorder(BorderFactory.createEmptyBorder(13, 13, 13, 13));
		lbTitle.setOpaque(true);
		
		this.getContentPane().add(lbTitle, BorderLayout.PAGE_START);
		
		// Payment + Key
		
		JPanel pnCt = new JPanel();
		pnCt.setLayout(new BoxLayout(pnCt, BoxLayout.LINE_AXIS));
		
		if (PayBy.CreditCard == payBy) {
			this.setTitle("Credit Card ... ");
			pnPay = new PnPayCC(payAmt, psrc, canUpdAmt);
			this.addKeyListener(new MSRCCLst(this));
		} else if (PayBy.Check == payBy) {
			this.setTitle("Check ... ");
			pnPay = new PnPayCheck(payAmt, psrc, canUpdAmt);
		} else if (PayBy.GiftCard == payBy) {
			this.setTitle("Gift Card ... ");
			pnPay = new PnPayGift(payAmt, psrc);
			btBal = Button.newButton("*Bal,bt_balance", this);
			this.addKeyListener(new MSRGiftLst(this));
		}
		pnPay.updateColor(lbTitle, pnCt);
		pnCt.add(pnPay.getPanel());
		
			PnKeyNum pnKey = PnKeyNum.newPanelNoBorder(this);
			pnKey.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
			pnKey.setOpaque(false);
			pnKey.setAlignmentX(LEFT_ALIGNMENT);
		pnCt.add(pnKey);
		
		pnCt.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
		
		this.getContentPane().add(pnCt, BorderLayout.CENTER);
		
		// Command
		
		Button btReset = Button.newButton("Reset,bt_reset", this);
		btOk = Button.newOk(this);
		Button btCancel = Button.newCancel(this);

		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		pnCmd.add(btReset);
		if (null != btBal) {
			pnCmd.add(btBal);
		}
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.add(btOk);
		pnCmd.add(btCancel);

		pnCmd.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

		this.getContentPane().add(pnCmd, BorderLayout.PAGE_END);
				
		this.setResizable(false);
		this.pack();
	}
	
	private void disposeDialog() {
		this.dispose();
	}
	
	// -----public-----
	
	void showDialog() {
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}
	
	String getUsrRsp() {
		return usrRsp;
	}
	
	TbPayment getPayment() {
		return pnPay.getPayment();
	}

	@Override
	public void creditCardSwiped(CCData cardData) {
		pnPay.cardSwiped(cardData);
	}

	@Override
	public void cardReadError() {
		System.out.println("card read error.");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		usrRsp = e.getActionCommand();
		if ("bt_cancel".equals(usrRsp)) {
			disposeDialog();
		} else if ("bt_ok".equals(usrRsp)) {
			disposeDialog();
		} else if ("bt_reset".equals(usrRsp)) {
			pnPay.resetValue();
		}
		if (usrRsp.startsWith("key_")) {
			pnPay.updateValue(usrRsp);
		}
	}

	// -----main-----
	
	public static void main(String[] args) {
		try {
			//UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
			
			javax.swing.JFrame frm1 = new javax.swing.JFrame("Test Dialog");
			frm1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			
			javax.swing.JTextArea txt1 = new javax.swing.JTextArea();
			javax.swing.JScrollPane scp1 = new javax.swing.JScrollPane(txt1);
			frm1.getContentPane().add(scp1, BorderLayout.CENTER);
			
			frm1.pack();
			frm1.setSize(1024, 768);
			frm1.setLocationRelativeTo(null);
			frm1.setVisible(true);

			DlgPayBy dlg1 = new DlgPayBy(frm1, PayBy.Check, 5.89, PaySrc.Order, false); // 592
			//DlgPayBy dlg1 = new DlgPayBy(frm1, PayBy.Check, 5.89); // 525
			//DlgPayBy dlg1 = new DlgPayBy(frm1, PayBy.GiftCard, 5.89); // 593
			System.out.printf("width=[%d]\n", dlg1.getPreferredSize().width);
			dlg1.showDialog();
			if ("bt_ok".equals(dlg1.getUsrRsp())) {
				TbPayment pm1 = dlg1.getPayment();
				System.out.printf("Gift No. [%s]\n", pm1.getPmGiftNo());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		System.exit(0);
	}
}
