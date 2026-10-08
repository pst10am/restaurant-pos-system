package app_pos;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JDialog;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import model.TbPayment;
import model.TbShift;
import refx.DlgType;
import resrc.ResData;

public class DlgReport extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	TbShift frShft, toShft;

	private DlgReport(Frame _parent) {
		super(_parent, "Report:", true);
		initComponents();
	}
	
	public static DlgReport newInstance(Frame _parent) {
		return new DlgReport(_parent);
	}
	
	private void initComponents() {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
	}
	
	private boolean refreshData() {
		String msg1 = _refreshData();
		if ("Done".equals(msg1)) { 
			return true;
		}
		DlgBox dlg1 = new DlgBox(this);
		dlg1.showDialog("Info", msg1, DlgType.Warning);
		if ("bt_close".equals(dlg1.getUsrRsp())) {
			
		}
		return false;
	}
	
	private String _refreshData() {
		System.out.printf(" -> getting data from [%s] -> [%s]\n", 
			frShft.getClDt(), toShft.getClDt());
		try {
			TbPayment[] pmnts = TbPayment.getData(frShft.getClDt(), toShft.getClDt());
			if (null == pmnts || pmnts.length <= 0) {
				return "No payments found.";
			}
			
			
			
			return "Done";
		} catch (SQLException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	private void _showDialog() {
		this.pack();
		this.setSize(800, 600);
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}
	
	public void showCloseShift() {
		this.setTitle("Report: Close Shift");
		try {
			toShft = TbShift.newInstance();
			frShft = TbShift.findPrevShift(toShft);
			if (refreshData()) {
				_showDialog();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
		
		DlgReport dlg1 = DlgReport.newInstance(frm1);
		dlg1.showCloseShift();
		
	}
}
