package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import model.TxDC;
import refx.AmountType;
import refx.DCType;
import resrc.StdFont;

public class DlgNewDC extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private String usrRsp = "NA";
	private JLabel lbDesc, lbType, lbAmt;
	private DCType dcType;
	private AmountType amtType = AmountType.Percent;
	
	public DlgNewDC(JDialog _dlg, DCType _type) {
		super(_dlg, String.format("New %s", _type.toString()), true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		dcType = _type;
		
		initComponents();
		
		this.pack();
		this.setResizable(false);
		this.setLocationRelativeTo(_dlg);
		this.setVisible(true);
	}
	
	private void initComponents() {
		JLabel lb1 = new JLabel(String.format("New %s", dcType.toString()));
		lb1.setFont(StdFont.Fnt20);
		lb1.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
		this.getContentPane().add(lb1, BorderLayout.PAGE_START);
		
		//
		
		JButton btDesc = UIFactory.buttonB("Description", "bt_desc", this);
		JButton btType = UIFactory.buttonB("Type","bt_type",this);
		JButton btAmt = UIFactory.buttonB("Amount","bt_amount",this);

		lbDesc = new JLabel("-");
		lbDesc.setFont(btDesc.getFont());
		
		lbType = new JLabel(amtType.dispStr());
		lbType.setFont(btType.getFont());
		
		lbAmt = new JLabel("0.00");
		lbAmt.setFont(btAmt.getFont());
		
		JPanel pn1 = new JPanel();
		pn1.setLayout(new BoxLayout(pn1, BoxLayout.PAGE_AXIS));
		
		pn1.add(UIFactory.panelA(btDesc, lbDesc));
		pn1.add(Box.createVerticalStrut(10));
		pn1.add(UIFactory.panelA(btType, lbType));
		pn1.add(Box.createVerticalStrut(10));
		pn1.add(UIFactory.panelA(btAmt, lbAmt));
		
		pn1.setBorder(BorderFactory.createEmptyBorder(0, 20, 25, 20));
		
		this.getContentPane().add(pn1, BorderLayout.CENTER);
		
		//
		
		crPnCmd();
	}
	
	private void crPnCmd() {
		// Command
		
		Button btOk = Button.newOk(this);
		Button btCancel = Button.newCancel(this);

		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.add(btOk);
		pnCmd.add(btCancel);

		pnCmd.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
		
		this.getContentPane().add(pnCmd, BorderLayout.PAGE_END);
	}
	
	public TxDC getValue() {
		TxDC dc1 = TxDC.newInstance(
			dcType, lbDesc.getText(), 
			amtType, Double.parseDouble(lbAmt.getText()));
		return dc1;
	}
	
	public String getUsrRsp() {
		return usrRsp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		usrRsp = e.getActionCommand();
		if ("bt_cancel".equals(usrRsp)) {
			this.dispose();
		} else if ("bt_ok".equals(usrRsp)) {
			this.dispose();
		} else if ("bt_desc".equals(usrRsp)) {
			DlgInpText dlgTxt = new DlgInpText(this);
			dlgTxt.setValue(lbDesc.getText());
			dlgTxt.showDialog(this, "Description?");
			if ("bt_ok".equals(dlgTxt.getUsrRsp())) {
				lbDesc.setText(dlgTxt.getValue());
			}
		} else if ("bt_type".equals(usrRsp)) {
			amtType = AmountType.nextVal(amtType);
			lbType.setText(amtType.dispStr());
		} else if ("bt_amount".equals(usrRsp)) {
			double amt = Double.parseDouble(lbAmt.getText());
			DlgInpNum dlgInp = new DlgInpNum(this);
			dlgInp.showDialog("Amount?", amt);
			if ("bt_ok".equals(dlgInp.getUsrRsp())) {
				amt = dlgInp.getDoubleValue();
				lbAmt.setText(String.format("%.2f", amt));
			}
		}
	}

}
