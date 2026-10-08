package app_pos;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import refx.PaySrc;
import model.CCData;
import model.TbPayment;

public class PnPayCheck implements ActionListener, PnPayIntf {
	
	private ButtonCC btPayAmt, btChqNo;
	private InpNumHandler nhPay, nhChqNo;
	private int selBt = 0;
	private double payAmt = 0;
	private PaySrc psrc;
	private boolean canUpdAmt = false;
	
	// -----constructor-----

	public PnPayCheck(double _payAmt, PaySrc _src, boolean _canUpdAmt) {
		payAmt = _payAmt;
		psrc = _src;
		canUpdAmt = _canUpdAmt;
	}
	
	// -----private-----

	private void updateSelectedButton(int btnum) {
		selBt = btnum;
		btPayAmt.setSelected(selBt == 1);
		btChqNo.setSelected(selBt == 2);
	}
	
	// -----public-----
	
	@Override
	public void updateColor(JLabel _title, JPanel _ct) {
		_title.setBackground(Color.decode("#754719"));
		//_ct.setBackground(Color.decode("#9F7E5F"));
		_ct.setBackground(Color.decode("#DCDCDC"));
	}

	@Override
	public void resetValue() {
		if (selBt == 1) {
			nhPay.resetValue();
		} else if (selBt == 2) {
			nhChqNo.resetValue();
		}
	}
	
	@Override
	public void updateValue(String keyStr) {
		if (selBt == 1) {
			if (!canUpdAmt) return;
			nhPay.updateValue(keyStr);
		} else if (selBt == 2) {
			nhChqNo.updateValue(keyStr);
		}
	}
	
	@Override
	public JPanel getPanel() {
		JPanel pnCC = new JPanel();
		pnCC.setLayout(new BoxLayout(pnCC, BoxLayout.PAGE_AXIS));
		pnCC.setOpaque(false);
		
		btPayAmt = ButtonCC.newButton(String.format("%.2f", payAmt), "bt_pay_amt", canUpdAmt ? this : null);
		btChqNo = ButtonCC.newButton("-", "bt_chq_no", this);
		
		nhPay = new InpNumHandler(btPayAmt);
		nhPay.setDoubleValue(payAmt);
		
		nhChqNo = new InpNumHandler(btChqNo);
		nhChqNo.setAnyValue("-");
		
		pnCC.add(ButtonCC.panelButtonCC("Pay Amount", btPayAmt));
		pnCC.add(Box.createVerticalStrut(10));
		pnCC.add(ButtonCC.panelButtonCC("Check No.", btChqNo));
		pnCC.add(Box.createVerticalGlue());
		
		pnCC.setPreferredSize(new Dimension(350, pnCC.getPreferredSize().height));
		
		updateSelectedButton(canUpdAmt ? 1 : 2);
		return pnCC;
	}

	@Override
	public void cardSwiped(CCData cdt) {}

	@Override
	public TbPayment getPayment() {
		TbPayment pm1 = TbPayment.newPayCheck(
			Double.parseDouble(btPayAmt.getText()),
			btChqNo.getText(),
			psrc);
		return pm1;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ("bt_pay_amt".equals(cmd)) {
			updateSelectedButton(1);
		} else if ("bt_chq_no".equals(cmd)) {
			updateSelectedButton(2);
		}
	}
}
