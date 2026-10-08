package app_pos;

import java.awt.Frame;

import model.TbOrder;

public interface ScrIntf {
	public Frame getFrame();
	public void showOrderScreen(TbOrder[] orders);
	public void showMainScreen(boolean isCancel);
}
