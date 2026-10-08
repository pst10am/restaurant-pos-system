package app_pos;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.CCData;
import model.TbPayment;
import refx.PaySrc;
import resrc.ResData;
import resrc.StdFont;

public class PnPayCC implements ActionListener, PnPayIntf {
	
	private ButtonCC btPayAmt, btCCNo, btCVV;
	private InpNumHandler nhPay, nhCCNo, nhCVV;
	private JComboBox<String> cmbMonth, cmbYear;
	private int selBt = 0;
	private int curYr = 0;
	private double payAmt = 0;
	
	private CCData ccdt;
	
	private PaySrc psrc;
	
	private boolean canUpdAmt = false;
	
	// -----constructor-----

	public PnPayCC(double _payAmt, PaySrc _src, boolean _canUpdAmt) {
		payAmt = _payAmt;
		psrc = _src;
		ccdt = CCData.newCard();
		canUpdAmt = _canUpdAmt;
	}
	
	// -----private-----
	
	private JPanel pnExpMY() {
		JPanel pn1 = new JPanel(); // new FlowLayout(FlowLayout.LEADING, 0, 0));
		pn1.setLayout(new BoxLayout(pn1, BoxLayout.LINE_AXIS));
		pn1.setOpaque(false);
		
		cmbMonth = new JComboBox<>(ResData.months());
		cmbMonth.setFocusable(false);
		cmbMonth.setFont(StdFont.Fnt20);
		cmbMonth.setMaximumSize(cmbMonth.getPreferredSize());
		
		String[] years = new String[30];
		curYr = ResData.currentYear();
		for (int i=0; i < years.length; i++) {
			years[i] = String.format("%d  ", curYr+i);
		}
		cmbYear = new JComboBox<>(years);
		cmbYear.setFocusable(false);
		cmbYear.setFont(StdFont.Fnt20);
		cmbYear.setMaximumSize(cmbMonth.getPreferredSize());
		
		//pn1.add(Box.createHorizontalStrut(117));
		pn1.add(Box.createHorizontalGlue());
		pn1.add(cmbMonth);
		pn1.add(Box.createHorizontalStrut(10));
		pn1.add(cmbYear);
		
		pn1.setMaximumSize(new Dimension(Short.MAX_VALUE, pn1.getPreferredSize().height));
		pn1.setAlignmentX(Component.LEFT_ALIGNMENT);
		return pn1;
	}

	private void updateSelectedButton(int btnum) {
		selBt = btnum;
		btPayAmt.setSelected(selBt == 1);
		btCCNo.setSelected(selBt == 2);
		btCVV.setSelected(selBt == 3);
	}
	
	private String getCCNo() {
		return btCCNo.getText().trim().replaceAll("-", "").replaceAll("_", "");
	}
	
	private String getCVV() {
		return btCVV.getText().trim().replaceAll("-", "").replaceAll("_", "");
	}
	
	private void validateCardInfo() {
		if (!ccdt.getAcctNo().equals(getCCNo())) {
			ccdt.clearData();
			return;
		}
		/*
		if (!ccdt.getCVV().equals(btCVV.getText())) {
			ccdt.clearData();
			return;
		}
		*/
	}
	
	// -----public-----
	
	@Override
	public void updateColor(JLabel _title, JPanel _ct) {
		_title.setBackground(Color.decode("#596666"));
		_ct.setBackground(Color.decode("#DCDCDC"));
	}

	@Override
	public void resetValue() {
		ccdt.clearData();
		if (selBt == 1) {
			nhPay.resetValue();
		} else if (selBt == 2) {
			nhCCNo.resetValue();
		} else if (selBt == 3) {
			nhCVV.resetValue();
		}
	}
	
	@Override
	public void updateValue(String keyStr) {
		if (selBt == 1) { // Amount
			if (!canUpdAmt) return;
			nhPay.updateValue(keyStr);
		} else if (selBt == 2) { // Credit Card No.
			nhCCNo.updateValue(keyStr);
		} else if (selBt == 3) { // CVV
			nhCVV.updateValue(keyStr);
		}
	}
	
	@Override
	public JPanel getPanel() {
		JPanel pnCC = new JPanel();
		pnCC.setLayout(new BoxLayout(pnCC, BoxLayout.PAGE_AXIS));
		pnCC.setOpaque(false);
		
		btPayAmt = ButtonCC.newButton(String.format("%.2f", payAmt), "bt_pay_amt", canUpdAmt ? this : null);
		btCCNo = ButtonCC.newButton("-", "bt_cc_no", this);
		btCVV = ButtonCC.newButton("-", "bt_cvv", this);
		
		nhPay = new InpNumHandler(btPayAmt);
		nhPay.setDoubleValue(payAmt);
		
		nhCCNo = new InpNumHandler(btCCNo);
		nhCCNo.setCCNoValue("");
		
		nhCVV = new InpNumHandler(btCVV);
		nhCVV.setCVVValue("");
		
		pnCC.add(ButtonCC.panelButtonCC("Pay Amount", btPayAmt));
		pnCC.add(Box.createVerticalStrut(10));
		pnCC.add(ButtonCC.panelButtonCC("CC No.", btCCNo));
		pnCC.add(Box.createVerticalStrut(10));
		pnCC.add(ButtonCC.panelButtonCC("CVV(3 Digits)", btCVV));
		pnCC.add(Box.createVerticalStrut(10));
		pnCC.add(pnExpMY());
		pnCC.add(Box.createVerticalGlue());
		
		pnCC.setPreferredSize(new Dimension(350, pnCC.getPreferredSize().height));

		updateSelectedButton(canUpdAmt ? 1 : 2);
		return pnCC;
	}

	@Override
	public void cardSwiped(CCData _ccdt) {
		ccdt = CCData.fromObject(_ccdt);
		nhCCNo.setCmpText(ccdt.getAcctNo()); // .setCCNoValue(ccdt.getAcctNo());
		nhCVV.setCmpText(""); // .setCVVValue("");
		cmbMonth.setSelectedIndex(Integer.parseInt(ccdt.getExpMonth())-1);
		//int expYr = Integer.parseInt(String.format("20%s", ccdt.getExpYear()));
		int expYr = Integer.parseInt(ccdt.getExpYear());
		for (int i=0; i < cmbYear.getItemCount(); i++) {
			if (expYr == curYr+i) {
				cmbYear.setSelectedIndex(i);
				break;
			}
		}
	}

	@Override
	public TbPayment getPayment() {
		validateCardInfo();
		//
		ccdt.setAcctNo(getCCNo());
		ccdt.setCVV(getCVV());
		ccdt.setExpMonth(String.format("%02d", cmbMonth.getSelectedIndex()+1));
		ccdt.setExpYear((String)cmbYear.getSelectedItem());
		//
		double amt1 = Double.parseDouble(btPayAmt.getText());
		TbPayment pm1 = TbPayment.newPayCreditCard(amt1, ccdt, psrc);
		//
		return pm1;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ("bt_pay_amt".equals(cmd)) {
			updateSelectedButton(1);
		} else if ("bt_cc_no".equals(cmd)) {
			updateSelectedButton(2);
		} else if ("bt_cvv".equals(cmd)) {
			updateSelectedButton(3);
		}
	}
}
