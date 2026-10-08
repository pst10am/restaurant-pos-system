package print;

import java.io.File;
import java.util.Scanner;

import model.TxPrinter;
import resrc.ResCfg;
import resrc.ResLog;
import jpos.CashDrawer;
import jpos.JposConst;
import jpos.JposException;
import jpos.POSPrinter;
import jpos.POSPrinterConst;
import jpos.events.ErrorEvent;
import jpos.events.ErrorListener;
import jpos.events.OutputCompleteEvent;
import jpos.events.OutputCompleteListener;
import jpos.events.StatusUpdateEvent;
import jpos.events.StatusUpdateListener;
import jpos.util.JposPropertiesConst;

final public class StarPrinter implements 
	OutputCompleteListener, StatusUpdateListener, ErrorListener {
	
	private static String ESC = ((char) 0x1b) + "";
	private static String LF = ((char) 0x0a) + "";
	static {
		// System.setProperty(JposPropertiesConst.JPOS_POPULATOR_FILE_URL_PROP_NAME, "http://some-where-remote.com/jpos.xml");
		System.setProperty(JposPropertiesConst.JPOS_POPULATOR_FILE_PROP_NAME, "cfg/jpos.xml");
	}
	
	private java.util.HashMap<String, java.util.Vector<File>> prLst;
	private StarPrinter() {
		prLst = new java.util.HashMap<>();
	}
	
	// factory
	
	public static StarPrinter newInstance() {
		return new StarPrinter();
	}
	
	// miscellaneous
	
	public void addFile(File pfile) {
		String fname = pfile.getName();
		int prnId = Integer.parseInt(fname.substring(0, fname.indexOf("_")));
		//System.out.printf("prn_id -> [%s] from [%s]\n", prnId, pfile.getName());
		TxPrinter prt1 = TxPrinter.getPrinterById(prnId);
		if (!prLst.containsKey(prt1.getPrnIp())) {
			prLst.put(prt1.getPrnIp(), new java.util.Vector<File>());
		}
		java.util.Vector<File> flst = prLst.get(prt1.getPrnIp());
		flst.add(pfile);
	}
	
	@Override
	public void outputCompleteOccurred(OutputCompleteEvent event) {
		ResLog.append("OutputCompleteEvent received: time = "
			+ System.currentTimeMillis() + " output id = "
			+ event.getOutputID());
	}

	@Override
	public void statusUpdateOccurred(StatusUpdateEvent event) {
		ResLog.append("StatusUpdateEvent : status id = " + event.getStatus());
	}

	@Override
	public void errorOccurred(ErrorEvent event) {
		ResLog.append("ErrorEvent received: time = "
				+ System.currentTimeMillis() + " error code = "
				+ event.getErrorCode() + " error code extended = "
				+ event.getErrorCodeExtended());
		try {
			Thread.sleep(1000);
		} catch (Exception e) {}
		event.setErrorResponse(JposConst.JPOS_ER_RETRY);
	}
	
	// 1/31/2015 SUN
	// Revised
	private boolean _printTo(String prnIp, java.util.Vector<File> prnFiles) 
			throws Exception {
		boolean prFlg = false;
		// instantiate a new jpos.POSPrinter object
		POSPrinter printer = new POSPrinter();
		try {
			printer.addOutputCompleteListener(this);
			printer.addStatusUpdateListener(this);
			printer.addErrorListener(this);
			// System.out.println("print to -> "+ prtname);
			printer.open(prnIp);

			// claim exclusive usage of the printer object
			printer.claim(1);

			// enable the device for input and output
			printer.setDeviceEnabled(true);

			printer.setAsyncMode(true);

			// set map mode to metric - all dimensions specified in 1/100mm units
			printer.setMapMode(POSPrinterConst.PTR_MM_METRIC); 
			// unit = 1/100 mm - i.e. 1 cm = 10 mm = 10 * 100 units
			do {
				// register for asynchronous StatusUpdateEvent notification
				// see the JavaPOS specification for details on this

				// printer.checkHealth(JposConst.JPOS_CH_EXTERNAL);
				// printer.checkHealth(JposConst.JPOS_CH_INTERACTIVE);

				// check if the cover is open
				if (printer.getCoverOpen() == true) {
					ResLog.append("printer.getCoverOpen() == true");
					break;
				}

				// check if the printer is out of paper
				if (printer.getRecEmpty() == true) {
					ResLog.append("printer.getRecEmpty() == true");
					break;
				}

				// being a transaction
				// transaction mode causes all output to be buffered
				// once transaction mode is terminated, the buffered data is
				// output to the printer in one shot - increased reliability
				printer.transactionPrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_TP_TRANSACTION);
				
				for (File f1 : prnFiles) {
					try (Scanner scn1 = new Scanner(f1)) {
						while (scn1.hasNext()) {
							String line1 = scn1.nextLine();
							if ("[LOGO]".equals(line1)) {
								if (printer.getCapRecBitmap() == true) {
									try {
										printer.printBitmap(POSPrinterConst.PTR_S_RECEIPT, 
											"img/logo.gif", 
											POSPrinterConst.PTR_BM_ASIS, 
											POSPrinterConst.PTR_BM_CENTER);
									} catch (JposException e) {
										if (e.getErrorCode() != JposConst.JPOS_E_NOEXIST) {
											throw e;
										}
										// image file not found - ignore this error & proceed
									}
								}
								continue;
							}
							printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, line1 + LF);
						}
						// the ESC + "|100fP" control code causes the printer to execute
						// a paper cut after feeding to the cutter position
						printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|100fP");
					}
					//
				}

				// terminate the transaction causing all of the above buffered
				// data to be sent to the printer
				printer.transactionPrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_TP_NORMAL);

				ResLog.append("Async transaction print submited: time = "
					+ System.currentTimeMillis() + " output id = " + printer.getOutputID());
				
				// exit our printing loop
			} while (false);
			
			prFlg = true;
		/*
		} catch (Exception e) {
			e.printStackTrace();
		*/
		} finally {
			// close the printer object
			if (printer.getState() != JposConst.JPOS_S_CLOSED) {
				try {
					while (printer.getState() != JposConst.JPOS_S_IDLE) {
						Thread.sleep(50);
					}
					printer.close();
				} catch (Exception e) {
				}
			}
		}
		return prFlg;
	}

	// 1/31/2015 SUN
	// Revised
	public void startPrint() throws Exception {
		System.out.println("> start printing ... ");
		java.util.Iterator<String> itr1 = prLst.keySet().iterator();
		while (itr1.hasNext()) {
			String prnIp = itr1.next();
			System.out.printf("> print to -> [%s]\n", prnIp);
			_printTo(prnIp, prLst.get(prnIp));
		}
	}

	
	public static void kickDrawer() {
		final String drawerName = ResCfg.getTrmCashDrawer();
		if (null == drawerName || drawerName.isEmpty()) return;
		//
		Thread trd1 = new Thread() {
			public void run() {
				CashDrawer cashDrawer = new CashDrawer();
				try {
					cashDrawer.open(drawerName);
					cashDrawer.claim(1);
					cashDrawer.setDeviceEnabled(true);
					boolean drawerOpenedStatus = cashDrawer.getDrawerOpened();
					if (drawerOpenedStatus == true) {
						//System.out.println("cashDrawer.getDrawerOpened() == true");
					} else {
						//System.out.println("cashDrawer.getDrawerOpened() == false");
					}
					cashDrawer.openDrawer();
					if (drawerOpenedStatus != cashDrawer.getDrawerOpened()) {
						//System.out.println("Drawer opened successfully");
					} else {
						//System.out.println("Drawer status remains the same after openDrawer() call");
					}
					// just wait for 5000ms to watch StatusUpdateEvent
				} catch (JposException e) {
					//e.printStackTrace();
				} finally {
					try {
						cashDrawer.close();
					} catch (Exception e) {}
				}
			}
		};
		trd1.start();
	}
	
}
