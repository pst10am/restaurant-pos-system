package app_pos;

import java.awt.Frame;

import model.TbOrderItem;

public interface PnTicketIntf {

	public Frame getFrame();
	public void orderItemSelected(TbOrderItem odi1);
	public void ticketNoClicked();
	public void itemHasDeleted();
}
