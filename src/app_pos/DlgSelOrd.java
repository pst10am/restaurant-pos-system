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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import resrc.ResData;
import resrc.StdFont;
import model.TbOrder;
import model.TxSctTable;

public class DlgSelOrd extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private String usrRsp = "NA";
	private LstMdSelOrd mdItm;
	private JList<TbOrder> lstItm;
	
	// -----constructor-----
	
	public DlgSelOrd(Frame _pr) {
		super(_pr, "Select Order", true);
		initComponents();
	}
	
	// -----private-----
	
	private void initComponents() {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		mdItm = new LstMdSelOrd();
		lstItm = new JList<>(mdItm);
		lstItm.setFont(StdFont.Fnt22);
		lstItm.setVisibleRowCount(-1);
		lstItm.setCellRenderer(new LstRdrSelOrd());
		lstItm.setFocusable(false);
		lstItm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstItm.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) return;
				//
				ListSelectionModel smd = lstItm.getSelectionModel();
				if (smd.isSelectionEmpty()) return;
				//
				usrRsp = "OK";
				_disposeDialog();
			}
		});
		
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
		
		Button btClose = Button.newButton("Close,bt_close", this);

		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.add(btClose);
		pnCmd.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

		this.add(pnCmd, BorderLayout.PAGE_END);
		
		this.pack();
	}
	
	private void _disposeDialog() {
		this.dispose();
	}
	
	// -----public-----
	
	public void showDialog(TxSctTable tbl1, TbOrder[] pxyords) {
		this.setTitle(String.format("Table %s", tbl1.getTblName()));
		//
		mdItm.clear();
		mdItm.setItems(pxyords);
		//
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}
	
	public String getUsrRsp() {
		return usrRsp;
	}
	
	public TbOrder getSelectedValue() {
		return lstItm.getSelectedValue();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		usrRsp = e.getActionCommand();
		if ("bt_close".equals(usrRsp)) {
			_disposeDialog();
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
		
		try {
			
			model.TxSctTable tbl1 = model.TxSctTable.findById(75);
			
			TbOrder[] datas = TbOrder.getTbOrderByTblId_Checked(tbl1.getTblId());
			DlgSelOrd dlg1 = new DlgSelOrd(frm1);
			dlg1.showDialog(tbl1, datas);
			if ("OK".equals(dlg1.getUsrRsp())) {
				TbOrder pord = dlg1.getSelectedValue();
				System.out.printf("\n -> ord_id=%d ord_no=%d ord_amt_net=$%.2f\n", pord.getOrdId(), pord.getOrdNo(), pord.getOrdAmtNet());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
				
		System.exit(0);
	}
}
