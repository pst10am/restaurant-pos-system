package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import resrc.ResData;
import resrc.StdFont;
import model.AddrUS;

public class DlgMap extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;

	private static final String mapUrl = "https://maps.googleapis.com/maps/api/staticmap?"
		+ "center=<ADDR>"
		+ "&zoom=<ZOOM>"
		+ "&size=640x480"
		+ "&markers=color:red%7C<ADDR>"
		+ "&key=<KEY>";

	private JLabel lbAddr, lbMap, lbStatus;
	private int zoom = 15;
	private AddrUS addrObj;
	
	// -----constructor-----

	public DlgMap(Dialog _pr) {
		super(_pr, "Map", true);
		initComponents();
	}

	public DlgMap(Frame _pr) {
		super(_pr, "Map", true);
		initComponents();
	}
	
	// -----private-----
	
	private void initComponents() {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		
		// Label
		
		lbAddr = new JLabel();
		lbAddr.setFont(StdFont.Fnt18);
		lbAddr.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY),
			BorderFactory.createEmptyBorder(10, 10, 10, 10)
			));
		
		this.getContentPane().add(lbAddr, BorderLayout.PAGE_START);
		
		// Map
		
		lbMap = new JLabel();
		JScrollPane scp1 = new JScrollPane(lbMap);
		scp1.setBorder(null);
		scp1.getVerticalScrollBar().setPreferredSize(
			new Dimension(25, 0)); 
		scp1.getHorizontalScrollBar().setPreferredSize(
			new Dimension(0, 25)); 
		this.getContentPane().add(scp1, BorderLayout.CENTER);
		
		// Command
		
		Button btZmIn = Button.newButton("Zm+,bt_zm_in", this);
		Button btZmOut = Button.newButton("Zm-,bt_zm_out", this);
		Button btOk = Button.newOk(this);

		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		pnCmd.add(btZmIn);
		pnCmd.add(btZmOut);
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.add(btOk);
		pnCmd.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY));
		
		lbStatus = new JLabel("-");
		lbStatus.setFont(StdFont.Fnt12);
		lbStatus.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		lbStatus.setBackground(Color.GRAY);
		lbStatus.setForeground(Color.WHITE);
		lbStatus.setOpaque(true);
		
		JPanel _pn1 = new JPanel();
		_pn1.setLayout(new BorderLayout());
		_pn1.add(pnCmd, BorderLayout.PAGE_START);
		_pn1.add(lbStatus, BorderLayout.PAGE_END);

		this.getContentPane().add(_pn1, BorderLayout.PAGE_END);
		
		this.setResizable(false);
		this.pack();
		this.setSize(640, 585);
	}
	
	private void _updateMap() {
		lbAddr.setText(addrObj.toStringWithUnit());
		try {
			String _url = mapUrl.replaceAll("<ADDR>", addrObj.toGoogleAddrString())
				.replace("<KEY>", resrc.ResCfg.getMapsApiKey());
			_url = _url.replaceAll("<ZOOM>", ""+zoom);
			lbMap.setIcon(new ImageIcon(new URL(_url)));
			lbMap.validate();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		lbStatus.setText(String.format("zoom level = %d", zoom));
	}
	
	// -----public-----
	
	public void showDialog(AddrUS addr1) {
		addrObj = addr1;
		_updateMap();
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ("bt_ok".equals(cmd)) {
			this.dispose();
		} else if ("bt_zm_in".equals(cmd)) {
			zoom += 1;
			if (zoom > 19) {
				zoom = 19;
				return;
			}
			_updateMap();
		} else if ("bt_zm_out".equals(cmd)) {
			zoom -= 1;
			if (zoom < 10) {
				zoom = 10;
				return;
			}
			_updateMap();
		}
	}
	
	// -----main-----

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ResData.status();
			//UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
		} catch (Exception e) {} 
		
		javax.swing.JFrame frm1 = new javax.swing.JFrame("Test Key Dialog");
		frm1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frm1.pack();
		frm1.setSize(1024, 768);
		frm1.setLocationRelativeTo(null);
		frm1.setVisible(true);
		
		DlgMap dlg1 = new DlgMap(frm1);
		
		AddrUS addr1 = AddrUS.newInstance();
		//addr1.setAddrLine1("64c44 W BE 88 asas LMONT AVE");
		//addr1.setAddrLine1("2147 W concord pl"); // $3
		//addr1.setAddrLine1("1670 N Claremont Ave"); // $3
		//addr1.setAddrLine1("3322 W Grand Ave"); // $4
		//addr1.setAddrLine1("3215 W Crystal St"); // $4
		addr1.setAddrLine1("1867 N Halsted St");
		//addr1.setAddrLine1("1742 N Larrabee St"); // out of service area
		
		addr1.setAddrCity("Chicago");
		addr1.setAddrState("IL");
		
		dlg1.showDialog(addr1);
		
		System.exit(0);

	}
}
