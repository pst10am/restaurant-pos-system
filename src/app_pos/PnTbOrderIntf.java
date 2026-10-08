package app_pos;

import java.awt.Frame;

import model.TbOrderItem;

public interface PnTbOrderIntf {
	
	public void orderItemSelected(TbOrderItem odi1);
	public Frame getFrame();
}
