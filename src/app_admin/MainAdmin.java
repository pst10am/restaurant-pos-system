package app_admin;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import resrc.ResData;

public class MainAdmin extends JFrame implements ActionListener, IxMain {
	private static final long serialVersionUID = 1L;
	
	private JPanel pnCt;
	
	{
		ResData.status();
	}

	MainAdmin() {
		super("POS:Admin");
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		initComponents();
		
		this.pack();
		this.setSize(925, 640);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	// private
	
	private void initComponents() {
		initMenu();
		pnCt = new JPanel(new BorderLayout());
		this.getContentPane().add(pnCt, BorderLayout.CENTER);
	}
	
	private void initMenu() {
		String[][] mnuBarArr = {
			{"Setup",
				"Section,mnu_sect",
				"Food Menu,mnu_food_menu", 
				"User,mnu_user", 
				"Order,mnu_order", 
				"-", 
				"Preferences,mnu_pref", 
				"-", 
				"Close,mnu_close", 
				"-", 
				"Exit,mnu_exit"},
			{"Data", 
				"Backup,mnu_backup", 
				"Compact,mnu_compact", 
				"Move,mnu_move",
				"-", 
				"Clear,mnu_clear"},
		};
		JMenuBar mbar1 = new JMenuBar();
		for (String[] mnuArr : mnuBarArr) {
			JMenu mnu1 = null;
			for (int k=0; k < mnuArr.length; k++) {
				String str1 = mnuArr[k];
				if (k == 0) {
					mnu1 = new JMenu(str1);
					continue;
				}
				if ("-".equals(str1)) {
					mnu1.addSeparator();
					continue;
				}
				String[] itemStr = str1.split(",");
				JMenuItem mnuItm = new JMenuItem(itemStr[0]);
				mnuItm.setActionCommand(itemStr[1]);
				mnuItm.addActionListener(this);
				mnu1.add(mnuItm);
			}
			mbar1.add(mnu1);
		}
		this.setJMenuBar(mbar1);
	}
	
	private void closeProgram() {
		//int usrRsp = JOptionPane.showConfirmDialog(this, "Exit?");
		int usrRsp = JOptionPane.YES_OPTION;
		if (JOptionPane.YES_OPTION == usrRsp) {
			System.out.println("> exiting program ... ");
			ResData.close();
			while (!ResData.isClose()) {
				try {
					Thread.sleep(500);
					System.out.println("> waiting connection to be closed.");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.exit(0);
		}
	}

	private void closePanel() {
		pnCt.removeAll();
		pnCt.updateUI();
	}
	
	private void menuSect() {
		pnCt.removeAll();
		//
		PanelSection mng1 = PanelSection.getInstance(this);
		mng1.reset();
		//
		pnCt.add(mng1, BorderLayout.CENTER);
		pnCt.updateUI();
	}
	
	private void menuFoodMenu() {
		pnCt.removeAll();
		//
		PanelMenu mng1 = PanelMenu.getInstance(this);
		mng1.reset();
		//
		pnCt.add(mng1, BorderLayout.CENTER);
		pnCt.updateUI();
	}
	
	private void menuUser() {
		pnCt.removeAll();
		//
		PanelUsers mng1 = PanelUsers.getInstance(this);
		mng1.reset();
		//
		pnCt.add(mng1, BorderLayout.CENTER);
		pnCt.updateUI();
	}
	
	private void menuOrder() {
		pnCt.removeAll();
		//
		PanelOrder mng1 = PanelOrder.getInstance(this);
		mng1.reset();
		//
		pnCt.add(mng1, BorderLayout.CENTER);
		pnCt.updateUI();
	}
	
	private void menuPref() {
		DialogPref dlg1 = new DialogPref(this);
		dlg1.showDialog();
	}
	
	// public
	
	public void processWindowEvent(WindowEvent wev) {
		if (wev.getID() == WindowEvent.WINDOW_CLOSING) {
			closeProgram();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ("mnu_exit".equals(cmd)) {
			closeProgram();
		} else if ("mnu_close".equals(cmd)) {
			closePanel();
		} else if ("mnu_sect".equals(cmd)) {
			menuSect();
		} else if ("mnu_food_menu".equals(cmd)) {
			menuFoodMenu();
		} else if ("mnu_user".equals(cmd)) {
			menuUser();
		} else if ("mnu_order".equals(cmd)) {
			menuOrder();
		} else if ("mnu_pref".equals(cmd)) {
			menuPref();
		}
	}

	@Override
	public Frame getFrame() {
		return this;
	}

	// main
	public static void main(String[] args) {
		try {
			// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new MainAdmin();
	}
}
