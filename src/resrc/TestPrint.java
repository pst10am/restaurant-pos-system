package resrc;

import print.RcpPrinter;
import refx.PrintTo;
import model.TbOrder;

public class TestPrint {

	static void testKitchen() throws Exception {
		TbOrder ord1 = TbOrder.findOrderByNo(641);
		RcpPrinter.printOrderTo(ord1, PrintTo.Kitchen);
	}

	static void testPacking() throws Exception {
		TbOrder ord1 = TbOrder.findOrderByNo(642);
		RcpPrinter.printOrderTo(ord1, PrintTo.Packing);
	}

	static void testCashier() throws Exception {
		TbOrder ord1 = TbOrder.findOrderByNo(641);
		RcpPrinter.printOrderTo(ord1, PrintTo.Cashier);
	}
	
	public static void main(String[] args) {
		try {
			ResData.status();
			//testKitchen();
			//testPacking();
			testCashier();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
