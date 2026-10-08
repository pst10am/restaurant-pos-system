package app_pos;

import java.awt.event.ActionListener;

import javax.swing.JRadioButton;

import resrc.StdFont;
import model.TxOpt;
import model.TxOptItem;

public class OptItemRadio extends JRadioButton implements OptItemIntf {
	private static final long serialVersionUID = 1L;
	
	private TxOpt opt1;
	private TxOptItem opi1;
	
	public OptItemRadio(TxOpt _opt1, TxOptItem _opi1, boolean _sel, ActionListener _lst) {
		super(_opi1.getDispStr());
		opt1 = _opt1;
		opi1 = _opi1;
		this.setSelected(_sel);
		this.setFocusable(false);
		this.setFont(StdFont.Fnt16);
		this.addActionListener(_lst);
	}

	@Override
	public TxOpt getTxOpt() {
		return opt1;
	}

	@Override
	public TxOptItem getTxOptItem() {
		return opi1;
	}
}
