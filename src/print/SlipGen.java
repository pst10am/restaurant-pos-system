package print;

import java.io.File;

import refx.SlipType;
import model.TbClock;
import model.TbOrder;
import model.TbPayment;

interface SlipGen {
	public File[] genKitchenSlip(TbOrder ord1, boolean allFlg, boolean runnerOnly) throws Exception;
	public File genSlip(TbOrder ord1, SlipType slpType) throws Exception;
	public File genCreditCardSign(TbPayment pm1) throws Exception;
	public File genClockSlip(TbClock clk1) throws Exception;
}
