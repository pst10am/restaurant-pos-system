package test;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class TestDir1 extends JDialog {
	private static final long serialVersionUID = 1L;
	
	JLabel lbMap;
	
	public TestDir1() {
		super(new Frame(), "Directions", true);
		
		initComponents();
		
		this.pack();
		this.setSize(640, 480);
	}
	
	private void initComponents() {
		lbMap = new JLabel();
		JScrollPane scp1 = new JScrollPane(lbMap);
		this.getContentPane().add(scp1, BorderLayout.CENTER);
	}
	
	public void showMap() {
		String url1 = "https://maps.googleapis.com/maps/api/staticmap?"
				+ "center=Seattle,WA"
				+ "&zoom=15"
				+ "&size=640x480"
				+ "&markers=color:red%7CSeattle,WA"
				+ "&key=" + resrc.ResCfg.getMapsApiKey();
		try {
			lbMap.setIcon(new ImageIcon(new URL(url1)));
			lbMap.validate();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		this.setVisible(true);
	}

	public static void main(String[] args) {
		TestDir1 dlg1 = new TestDir1();
		dlg1.showMap();
	}

}
