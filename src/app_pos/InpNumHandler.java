package app_pos;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

import resrc.ResUtil;

public class InpNumHandler {
	
	private KeyInputType inpType;
	private final JComponent updCmp;
	private boolean clearFlg;
	
	private int orgIntVal;
	private double orgDblVal;
	private String orgStrVal;
	
	private int cmpType = 0; // 0 -> JLabel, 1 -> JButton
	
	// -----constructor-----
	
	public InpNumHandler(JComponent _cmp) {
		updCmp = _cmp;
		if (updCmp instanceof JLabel) {
			cmpType = 1;
		} else if (updCmp instanceof JButton) {
			cmpType = 2;
		}
	}
	
	// -----private-----
	
	private boolean _isValidNum(String num) {
		try {
			Double.parseDouble(num);
			return true;
		} catch (Exception e) {}
		return false;
	}
	
	private String _cleanNumber(String raw) {
		if (raw.isEmpty()) {
			return "0";
		}
		int ix1 = -1;
		for (int i=0; i < raw.length(); i++) {
			if ((ix1 == -1) && raw.charAt(i) == '0') {
				continue;
			}
			ix1 = i;
			break;
		}
		if (ix1 == -1) {
			return "0";
		}
		if (raw.charAt(ix1) == '.') {
			return "0"+raw.substring(ix1);
		}
		return raw.substring(ix1);
	}
	
	private String _cleanValue(String raw) {
		if (KeyInputType.Integer == inpType) {
			return _cleanNumber(raw);
		}
		if (KeyInputType.Double == inpType) {
			return _cleanNumber(raw);
		}
		return raw;
	}
	
	// -----
	
	void setIntValue(int val) {
		inpType = KeyInputType.Integer;
		orgIntVal = val;
		clearFlg = true;
		resetValue();
	}
	
	void setDoubleValue(double val) {
		inpType = KeyInputType.Double;
		orgDblVal = val;
		clearFlg = true;
		resetValue();
	}
	
	void setCCNoValue(String val) {
		inpType = KeyInputType.CreditCard;
		orgStrVal = val;
		clearFlg = true;
		resetValue();
	}
	
	void setCVVValue(String val) {
		inpType = KeyInputType.CVV;
		orgStrVal = val;
		clearFlg = true;
		resetValue();
	}
	
	void setAnyValue(String val) {
		inpType = KeyInputType.Any;
		orgStrVal = val;
		clearFlg = true;
		resetValue();
	}
	
	// -----
	
	void resetValue() {
		if (KeyInputType.Integer == inpType) {
			setCmpText(String.format("%d", orgIntVal));
		} else if (KeyInputType.Double == inpType) {
			setCmpText(String.format("%.2f", orgDblVal));
		} else { // Any
			setCmpText(orgStrVal);
		}
		clearFlg = true;
		updCmp.setBackground(Color.decode("#A3C2FF"));
	}
	
	void setCmpText(String _txt) {
		String txt = _txt;
		if (KeyInputType.CreditCard == inpType) {
			txt = ResUtil.formatCCNo(_txt);
		} else if (KeyInputType.CVV == inpType) {
			txt = ResUtil.formatCVV(_txt);
		}
		if (cmpType == 1) { // JLabel
			((JLabel)updCmp).setText(txt);
		} else if (cmpType == 2) { // JButton
			((JButton)updCmp).setText(txt);
		}
	}
	
	String getCmpText() {
		String cmpTxt = "";
		if (cmpType == 1) { // JLabel
			cmpTxt = ((JLabel)updCmp).getText();
		} else {
			cmpTxt = ((JButton)updCmp).getText();
		}
		if (KeyInputType.CreditCard == inpType) {
			cmpTxt = cmpTxt.replaceAll("-", "").replaceAll("_", "");
		} else if (KeyInputType.CVV == inpType) {
			cmpTxt = cmpTxt.replaceAll("_", "");
		}
		return cmpTxt;
	}

	void updateValue(String keyStr) {
		if (!keyStr.startsWith("key_")) {
			return;
		}
		
		String val = keyStr.substring(4);
		if (KeyInputType.Integer == inpType) {
			if (".".equals(val)) {
				return;
			}
		}
		
		if (clearFlg) {
			if ("BkSp".equals(val)) {
				if (KeyInputType.Integer == inpType) {
					setCmpText("0");
				} else if (KeyInputType.Double == inpType) {
					setCmpText("0");
				} else {
					setCmpText("");
				}
			} else {
				setCmpText(val);
			}
			clearFlg = false;
			updCmp.setBackground(Color.WHITE);
			return;
		}
		
		if ("BkSp".equals(val)) {
			String dtclr = getCmpText();
			if (!dtclr.isEmpty()) {
				dtclr = dtclr.substring(0, dtclr.length()-1);
				setCmpText(_cleanValue(dtclr));
			}
			return;
		}
		
		String dt0 = getCmpText() + val;
		if (KeyInputType.Integer == inpType) {
			if (!_isValidNum(dt0)) return;
			setCmpText(_cleanValue(dt0));
		} else if (KeyInputType.Double == inpType) {
			if (!_isValidNum(dt0)) return;
			setCmpText(_cleanValue(dt0));
		} else {
			setCmpText(dt0);
		}
	}
}
