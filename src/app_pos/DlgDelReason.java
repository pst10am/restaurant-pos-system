package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import resrc.ResData;
import resrc.StdFont;
import model.TxDel;

public class DlgDelReason extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private String usrRsp = "NA";
	private LstMdTxDel mdItm;
	private JList<TxDel> lstItm;
	
	// -----constructor-----
	
	public DlgDelReason(Frame _pr) {
		super(_pr, "Delete Reason", true);
		initComponents();
		try {
			TxDel[] dels = TxDel.getDeleteReasons();
			mdItm.setItems(dels);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// -----private-----
	
	private void initComponents() {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		mdItm = new LstMdTxDel();
		lstItm = new JList<>(mdItm);
		lstItm.setFont(StdFont.Fnt22);
		lstItm.setVisibleRowCount(-1);
		lstItm.setCellRenderer(new LstRdrTxDel());
		lstItm.setFocusable(false);
		lstItm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane scp1 = new JScrollPane(lstItm, 
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scp1.setFocusable(false);
		scp1.getVerticalScrollBar().setPreferredSize(
			new Dimension(30, scp1.getVerticalScrollBar().getPreferredSize().height));
		scp1.setBorder(null);
		scp1.setPreferredSize(new Dimension(330, 256));
		
		this.getContentPane().add(scp1, BorderLayout.CENTER);
		
		// Command
		
		Button btNew = Button.newButton("New,bt_new", this);
		Button btOk = Button.newOk(this);
		Button btCancel = Button.newCancel(this);

		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		pnCmd.add(btNew);
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.add(btOk);
		pnCmd.add(btCancel);
		pnCmd.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

		this.add(pnCmd, BorderLayout.PAGE_END);
		
		this.pack();
	}
	
	private void _disposeDialog() {
		this.dispose();
	}
	
	private void _newReason() {
		DlgInpText dlg1 = new DlgInpText(this);
		dlg1.setValue("");
		dlg1.showDialog(this, "New Delete Reason?");
		if ("bt_ok".equals(dlg1.getUsrRsp())) {
			System.out.printf("-> [%s]\n", dlg1.getValue());
			try {
				TxDel dl1 = TxDel.newReason(dlg1.getValue());
				mdItm.addElement(dl1);
				lstItm.setSelectedIndex(mdItm.getSize()-1);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	// -----public-----
	
	public void showDialog() {
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}
	
	public String getUsrRsp() {
		return usrRsp;
	}
	
	public TxDel getSelectedValue() {
		return lstItm.getSelectedValue();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		usrRsp = e.getActionCommand();
		if ("bt_cancel".equals(usrRsp)) {
			_disposeDialog();
		} else if ("bt_ok".equals(usrRsp)) {
			if (lstItm.getSelectedIndex() >= 0) {
				_disposeDialog();
			}
		} else if ("bt_new".equals(usrRsp)) {
			_newReason();
		}
	}
	
	// -----main-----
	
	public static void main(String[] args) {
		ResData.status();
		try {
			//UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
		} catch (Exception e) {} 
		
		javax.swing.JFrame frm1 = new javax.swing.JFrame("Test Dialog");
		frm1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		javax.swing.JTextArea txt1 = new javax.swing.JTextArea();
		javax.swing.JScrollPane scp1 = new javax.swing.JScrollPane(txt1);
		frm1.getContentPane().add(scp1, BorderLayout.CENTER);
		
		frm1.pack();
		frm1.setSize(1024, 768);
		frm1.setLocationRelativeTo(null);
		frm1.setVisible(true);
		
		DlgDelReason dlg1 = new DlgDelReason(frm1);
		dlg1.showDialog();
		if ("bt_ok".equals(dlg1.getUsrRsp())) {
			TxDel dl1 = dlg1.getSelectedValue();
			System.out.printf("delete reason ... [%d] = [%s]\n", 
				dl1.getDlId(), dl1.getDlDesc());
		}
				
		System.exit(0);
	}
}
