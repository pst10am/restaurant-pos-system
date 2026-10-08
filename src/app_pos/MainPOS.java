package app_pos;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import print.RcpPrinter;
import model.TbOrder;
import model.TxSctTable;
import refx.PrintTo;
import refx.TableService;
import resrc.CallerID;
import resrc.ResCfg;
import resrc.ResData;
import resrc.StdFont;

public class MainPOS extends JFrame implements ScrIntf {
	
	private static final long serialVersionUID = 1L;
	
	private static final String SCR_MAIN = "scr_main";
	private static final String SCR_ORDER = "scr_order";
	
	private JPanel pnMain;
	private ScrMain scrMain;
	private ScrOrder scrOrder;
	
	{
		ResData.status();
	}

	private MainPOS() {
		super("POS:1503");
		this.setUndecorated(ResCfg.scrNoBorder());
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		initComponents();
		
		this.pack();
		
		if (ResCfg.scrNoBorder()) {
			Dimension scrDim = Toolkit.getDefaultToolkit().getScreenSize();			
			this.setSize(scrDim.width, scrDim.height);
			this.setLocation(0, 0);
		} else {
			this.setSize(ResCfg.scrWidth(), ResCfg.scrHeight());
			if (ResCfg.scrFullScreen()) {
				this.setExtendedState(MAXIMIZED_BOTH);
			}
			this.setLocationRelativeTo(null);
		}
		
		this.setVisible(true);
		
		refresh();
		
		CallerID cid = CallerID.getInstance();
		cid.start();
	}
	
	private void initComponents() {
		crPnTop();
		
		pnMain = new JPanel();
		pnMain.setLayout(new CardLayout());
		
		scrMain = new ScrMain(this);
		pnMain.add(scrMain, SCR_MAIN);
		
		scrOrder = new ScrOrder(this);
		pnMain.add(scrOrder, SCR_ORDER);
		
		this.getContentPane().add(pnMain, BorderLayout.CENTER);
	}
	
	private void crPnTop() {
		JPanel pnInfo = new JPanel();
		pnInfo.setLayout(new BoxLayout(pnInfo, BoxLayout.LINE_AXIS));
		pnInfo.setBackground(Color.decode("#00527A"));
		
		JLabel lbTerm = new JLabel(
			String.format("%s@%s", ResCfg.getTrmName(), ResCfg.getShopName()));
		lbTerm.setFont(StdFont.Fnt14B);
		lbTerm.setForeground(Color.WHITE);
		
		pnInfo.add(Box.createHorizontalGlue());
		pnInfo.add(lbTerm);
		
		pnInfo.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(1, 0, 0, 0, Color.DARK_GRAY),
			BorderFactory.createEmptyBorder(5, 5, 5, 5)
			));
		this.getContentPane().add(pnInfo, BorderLayout.PAGE_END);
	}
	
	private void showScreen(String scrName) {
		CardLayout cl1 = (CardLayout)pnMain.getLayout();
		cl1.show(pnMain, scrName);
	}
	
	private void refresh() {
		scrMain.refresh();
	}
	
	// *******************************************
	// public
	// *******************************************

	public void processWindowEvent(WindowEvent wev) {
		if (wev.getID() == WindowEvent.WINDOW_CLOSING) {
			ResData.close();
			while (!ResData.isClose()) {
				try {
					Thread.sleep(2000);
					System.out.println("> waiting connection to be closed.");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.exit(0);
		}
	}

	@Override
	public Frame getFrame() {
		return this;
	}

	@Override
	public void showOrderScreen(TbOrder[] orders) {
		scrOrder.setOrders(orders);
		showScreen(SCR_ORDER);
	}

	@Override
	public void showMainScreen(boolean isCancel) {

		final java.util.Vector<TbOrder> _orders = 
			scrOrder.getOrders();
		if (!isCancel && (null != _orders)) {
			Thread trd1 = new Thread() {
				public void run() {
					try {
						RcpPrinter.printOrderTo(_orders, 
							PrintTo.Kitchen, 
							PrintTo.Packing, 
							PrintTo.Cashier);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			trd1.start();
		}
		
		if (scrOrder.isDineIn()) {
			try {
				int tblId = scrOrder.getSrvTblId();
				TxSctTable.updateServiceStatus(tblId);
				TxSctTable.lockTable(tblId, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		scrMain.refresh();
		showScreen(SCR_MAIN);
	}
	
	// *******************************************
	// main
	// *******************************************

	public static void main(String[] args) {
		try {
			if ("system".equals(ResCfg.theme())) {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} else {
				MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
			}
		} catch (Exception e) {} 
		new MainPOS();
	}
}
