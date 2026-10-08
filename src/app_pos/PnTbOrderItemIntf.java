package app_pos;

import java.awt.Frame;

public interface PnTbOrderItemIntf {

	public void itemInfoUpdated();
	public Frame getFrame();
	public void deleteItem();
	public void holdItem();
	public void moveItemSeq(int stp);
}
