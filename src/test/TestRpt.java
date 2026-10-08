package test;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import resrc.ResData;

public class TestRpt extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;

	private TestRpt(Frame _parent) {
		super(_parent, "Report:", true);
		initComponents();
	}
	
	public static TestRpt newInstance(Frame _parent) {
		return new TestRpt(_parent);
	}
	
	private void initComponents() {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
	}
	
	private void _showDialog() {
		this.pack();
		this.setSize(800, 600);
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}
	
	public void showCloseShift() {
		this.setTitle("Report: Close Shift");
		_showDialog();
	}
	
	public void showSettlement() {
		this.setTitle("Report: Settlement");
		_showDialog();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ResData.status();
		
		javax.swing.JFrame frm1 = new javax.swing.JFrame("Test Dialog");
		frm1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frm1.pack();
		frm1.setSize(1024, 768);
		frm1.setLocationRelativeTo(null);
		frm1.setVisible(true);
		
		TestRpt dlg1 = TestRpt.newInstance(frm1);
		dlg1.showCloseShift();
		
	}
}
