package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import resrc.ResCfg;
import resrc.ResData;
import resrc.ResUtil;
import resrc.StdFont;
import model.AddrUS;
import model.TxArea;

public class DlgChkAddr extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private JLabel lbMsg = null;
	private String usrRsp = "NA";
	private Button btOk, btCancel;

	private AddrUS addrObj;
	private String chkStatus = "";
	private TxArea chkArea;
	
	// -----constructor-----

	public DlgChkAddr(Frame _pr) {
		super(_pr, "Checking Address", true);
		initComponents();
	}

	public DlgChkAddr(Dialog _pr) {
		super(_pr, "Checking Address", true);
		initComponents();
	}
	
	// -----private-----
	
	private void initComponents() {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		lbMsg = new JLabel("-");
		lbMsg.setFont(StdFont.Fnt18);
		lbMsg.setBorder(BorderFactory.createEmptyBorder(0, 45, 0, 45));
		
		this.add(lbMsg, BorderLayout.CENTER);
		
		// Command
		
		btOk = Button.newOk(this);
		btCancel = Button.newCancel(this);

		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.add(btOk);
		pnCmd.add(btCancel);
		pnCmd.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

		this.add(pnCmd, BorderLayout.PAGE_END);
		
		this.setResizable(false);
		this.setPreferredSize(new Dimension(560, 225));
		this.pack();
	}
	
	private void _updateResult(String _status, double _lat, double _lon) {
		//
		chkStatus = _status;
		//
		boolean validAddr = "OK".equals(chkStatus) && _lat != 0 && _lon != 0;
		if (validAddr) {
			//
			addrObj.setAddrLat(_lat);
			addrObj.setAddrLon(_lon);
			//
			chkArea = ResUtil.getDeliArea(addrObj);
			if (null != chkArea) {
				lbMsg.setText(String.format(
					"<html>%s<br>Distance: <b>%.2f</b> mi. Delivery Fee: <b>$%.2f</b></html>", 
					addrObj.toStringWithUnit(),
					ResUtil.getDistanceFromShop(addrObj),
					chkArea.getAreaDeliFee()));
			} else {
				lbMsg.setText(String.format(
					"<html>%s<br><font color='red'><b>Out Of Service Area (%.2f mi.)</b></font></html>", 
					addrObj.toStringWithUnit(),
					ResUtil.getDistanceFromShop(addrObj)));
			}
		} else {
			lbMsg.setText(String.format("<html>Error:<br><b>%s</b></html>", _status));
		}
		//
		btOk.setEnabled(validAddr);
		btCancel.setEnabled(true);
	}
	
	// -----public-----
	
	// !addr1 must not be empty.
	// !addr1 must not be empty.
	// !addr1 must not be empty.
	// !addr1 must not be empty.
	public void showDialog(AddrUS _addr) {
		addrObj = _addr;
		//
		btOk.setEnabled(false);
		btCancel.setEnabled(false);
		//
		final String _addrTxt = addrObj.toGoogleAddrString();
		if (_addrTxt.isEmpty()) {
			throw new IllegalArgumentException("invalid adress: address can't be empty.");
		}
		lbMsg.setText(String.format("<html>Checking...<br><b>%s</b></html>", addrObj.toStringNoUnit()));
		//
		Thread trd1 = new Thread(){
			public void run() {
				String _status = "NA";
				double _lat1 = 0;
				double _lon1 = 0;
				String urlStr = String.format(
					ResCfg.googleGeoCodeURL(), _addrTxt);
				try {
					URL url1 = new URL(urlStr);
					try (
						InputStream is = url1.openStream();
						JsonReader rdr = Json.createReader(is);
					) {
						JsonObject obj = rdr.readObject();
						_status = obj.getString("status");
						if ("OK".equals(_status)) {
							JsonArray _results = obj.getJsonArray("results");
							for (JsonObject result : _results.getValuesAs(JsonObject.class)) {
								JsonObject loc1 = result.getJsonObject("geometry").getJsonObject("location");
								_lat1 = loc1.getJsonNumber("lat").doubleValue();
								_lon1 = loc1.getJsonNumber("lng").doubleValue();
							}
						}
					}
					Thread.sleep(2000);
				} catch (Exception e) {
					_status = e.getMessage();
					_lat1 = 0;
					_lon1 = 0;
				}
				_updateResult(_status, _lat1, _lon1);
			}
		};
		trd1.start();
		//
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}
	
	public String getUsrRsp() {
		return usrRsp;
	}
	
	public String getChkStatus() {
		return chkStatus;
	}

	public TxArea getArea() {
		return chkArea;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		usrRsp = e.getActionCommand();
		if ("bt_cancel".equals(usrRsp)) {
			this.dispose();
		} else if ("bt_ok".equals(usrRsp)) {
			if (!"OK".equals(chkStatus)) {
				btOk.setEnabled(false);
				return;
			}
			this.dispose();
		}
	}

	public static void main(String[] args) {
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
		
		DlgChkAddr dlg1 = new DlgChkAddr(frm1);
		
		AddrUS addr1 = AddrUS.newInstance();
		addr1.setAddrLine1("123 Main St");

		addr1.setAddrCity("Springfield");
		addr1.setAddrState("IL");
		
		dlg1.showDialog(addr1);
		
		System.exit(0);
	}
}
