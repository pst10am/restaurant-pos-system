package app_pos;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import model.CCData;
import model.TbPayment;
import refx.PaySrc;
import resrc.StdFont;

public class PnPayGift implements ActionListener, PnPayIntf {
	
	private ButtonCC btPayAmt, btGiftNo;
	private InpNumHandler nhPay, nhGiftNo;
	private JLabel lbBal;
	private int selBt = 0;
	private double payAmt = 0;
	
	private PaySrc psrc = PaySrc.NA;
	
	// -----constructor-----

	public PnPayGift(double _payAmt, PaySrc _src) {
		payAmt = _payAmt;
		psrc = _src;
	}
	
	// -----private-----
	
	private JPanel crPnBal() {
		JPanel pn1 = new JPanel();
		pn1.setLayout(new BoxLayout(pn1, BoxLayout.LINE_AXIS));
		pn1.setOpaque(false);
		
		pn1.add(Box.createHorizontalGlue());
		
			JLabel lbTxt = new JLabel("Balance");
			lbTxt.setFont(StdFont.Fnt20);
			lbTxt.setAlignmentX(Component.LEFT_ALIGNMENT);
		pn1.add(lbTxt);
		
		pn1.add(Box.createHorizontalStrut(15));
		
			lbBal = new JLabel("0.00");
			lbBal.setFont(StdFont.Fnt20);
			lbBal.setHorizontalAlignment(SwingConstants.RIGHT);
			lbBal.setPreferredSize(new Dimension(105, lbBal.getPreferredSize().height));
			lbTxt.setAlignmentX(Component.LEFT_ALIGNMENT);
		pn1.add(lbBal);
		
		pn1.setMaximumSize(new Dimension(Short.MAX_VALUE, pn1.getPreferredSize().height));
		pn1.setAlignmentX(Component.LEFT_ALIGNMENT);
		return pn1;
	}

	private void updateSelectedButton(int btnum) {
		selBt = btnum;
		btPayAmt.setSelected(selBt == 1);
		btGiftNo.setSelected(selBt == 2);
	}
	
	// -----public-----
	
	@Override
	public void updateColor(JLabel _title, JPanel _ct) {
		_title.setBackground(Color.decode("#006600"));
		_ct.setBackground(Color.decode("#DCDCDC"));
	}

	@Override
	public void resetValue() {
		if (selBt == 1) {
			nhPay.resetValue();
		} else if (selBt == 2) {
			nhGiftNo.resetValue();
		}
	}
	
	@Override
	public void updateValue(String keyStr) {
		if (selBt == 1) {
			nhPay.updateValue(keyStr);
		} else if (selBt == 2) {
			nhGiftNo.updateValue(keyStr);
		}
	}
	
	@Override
	public JPanel getPanel() {
		JPanel pnCC = new JPanel();
		pnCC.setLayout(new BoxLayout(pnCC, BoxLayout.PAGE_AXIS));
		pnCC.setOpaque(false);
		
		btPayAmt = ButtonCC.newButton(String.format("%.2f", payAmt), "bt_pay_amt", this);
		btGiftNo = ButtonCC.newButton("-", "bt_gift_no", this);
		
		nhPay = new InpNumHandler(btPayAmt);
		nhPay.setDoubleValue(payAmt);
		
		nhGiftNo = new InpNumHandler(btGiftNo);
		nhGiftNo.setAnyValue("-");
		
		pnCC.add(ButtonCC.panelButtonCC("Pay Amount", btPayAmt));
		pnCC.add(Box.createVerticalStrut(10));
		pnCC.add(ButtonCC.panelButtonCC("Gift No.", btGiftNo));
		pnCC.add(Box.createVerticalStrut(10));
		pnCC.add(crPnBal());
		pnCC.add(Box.createVerticalGlue());
		
		pnCC.setPreferredSize(new Dimension(350, pnCC.getPreferredSize().height));

		updateSelectedButton(1);
		return pnCC;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ("bt_pay_amt".equals(cmd)) {
			updateSelectedButton(1);
		} else if ("bt_gift_no".equals(cmd)) {
			updateSelectedButton(2);
		}
	}

	@Override
	public void cardSwiped(CCData cdt) {
		nhGiftNo.setAnyValue(cdt.getTrk1());
	}

	@Override
	public TbPayment getPayment() {
		TbPayment pm1 = TbPayment.newPayGiftCard(
			Double.parseDouble(btPayAmt.getText()),
			btGiftNo.getText(),
			psrc);
		return pm1;
	}
}
