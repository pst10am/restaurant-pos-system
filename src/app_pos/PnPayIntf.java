package app_pos;

import javax.swing.JLabel;
import javax.swing.JPanel;

import model.CCData;
import model.TbPayment;

public interface PnPayIntf {
	
	public void updateColor(JLabel _title, JPanel _ct);
	public void resetValue();
	public void updateValue(String keyStr);
	public void cardSwiped(CCData cdt);
	public JPanel getPanel();
	public TbPayment getPayment();
}
