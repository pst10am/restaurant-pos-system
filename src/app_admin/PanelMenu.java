package app_admin;

import javax.swing.JTabbedPane;

public class PanelMenu extends JTabbedPane {
	private static final long serialVersionUID = 1L;
	
	private static PanelMenu pnMnu = null;
	
	private IxMain mainix;

	private PanelMenuCat pnItm;
	private PanelOption pnOpt;
	private PanelPrinter pnPrt;
	
	// constructor

	private PanelMenu(IxMain _main) {
		super();
		mainix = _main;
		initComponents();
	}
	
	public static PanelMenu getInstance(IxMain _intf) {
		if (null == pnMnu) {
			pnMnu = new PanelMenu(_intf);
		}
		return pnMnu;
	}
	
	// private
	
	private void initComponents() {
		pnOpt = new PanelOption(mainix);
		pnPrt = new PanelPrinter(mainix);
		pnItm = new PanelMenuCat(mainix, pnOpt, pnPrt);
		//
		this.add("Food Menu", pnItm);
		this.add("Options", pnOpt);
		this.add("Printers", pnPrt);
	}
	
	// public
	
	public void reset() {
		pnItm.reset();
		pnOpt.reset();
		pnPrt.reset();
	}
}
