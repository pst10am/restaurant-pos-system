package app_pos;

import refx.TableService;
import resrc.ResData;
import model.TbOrder;
import model.TxSctTable;

public class ThreadTableInfo extends Thread {
	
	private DlgThread dlg1;
	private TxSctTable tbl1;
	private TbOrder[] orders = null;
	
	public ThreadTableInfo(DlgThread _dlg1, TxSctTable _tbl1) {
		dlg1 = _dlg1;
		tbl1 = _tbl1;
	}
	
	public TbOrder[] getOrders() {
		return orders;
	}
	
	public void run() {
		dlg1.setText(String.format("Checking Table ... [%s]\n", tbl1.getTblName()));
		try {
			Thread.sleep(255);
			tbl1.refreshTableService();
			if (TableService.Paid == tbl1.getTblService()) {
				dlg1.append("> Table is PAID ...\n");
				dlg1.setExitCode(10);
			} else if (TableService.Checked == tbl1.getTblService()) {
				dlg1.append("> Table is CHECKED ...\n");
				dlg1.setExitCode(20);
			} else {
				dlg1.append("> Finding Orders ...\n");
				// Ready, Service
				orders = TbOrder.findOrderByTableId(tbl1.getTblId());
				if (null == orders) {
					dlg1.append("> No Orders, Create new order\n");
					orders = new TbOrder[1];
					orders[0] = TbOrder.newDineInOrder(tbl1);
				} else {
					dlg1.append(String.format("> Found Orders = [%d]\n", orders.length));
				}
				dlg1.setExitCode(30);
			}
			dlg1.append("> done \n");
			Thread.sleep(100);
			dlg1.dispose();
		} catch (Exception e) {
			dlg1.append("Error\n");
			dlg1.append(e.getMessage());
		}
		dlg1.processEnd();
	}

}
