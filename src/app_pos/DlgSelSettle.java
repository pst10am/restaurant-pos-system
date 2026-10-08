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

import refx.DlgType;
import resrc.ResData;
import resrc.StdFont;
import model.TbSettle;

public class DlgSelSettle extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private String usrRsp = "NA";
	private LstMdSelSettle mdItm;
	private JList<TbSettle> lstItm;
	private Button btView, btPrint, btClose;
	
	// -----constructor-----
	
	public DlgSelSettle(Frame _pr) {
		super(_pr, "Select Settlement", true);
		initComponents();
	}
	
	// -----private-----
	
	private void initComponents() {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		
		mdItm = new LstMdSelSettle();
		lstItm = new JList<>(mdItm);
		lstItm.setFont(StdFont.Fnt16);
		lstItm.setVisibleRowCount(-1);
		lstItm.setCellRenderer(new LstRdrSettle());
		lstItm.setFocusable(false);
		lstItm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		/*
		lstItm.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) return;
				//
				ListSelectionModel smd = lstItm.getSelectionModel();
				if (smd.isSelectionEmpty()) return;
				//
				usrRsp  ="selected";
				_disposeDialog();
			}
		});
		*/
		
		JScrollPane scpLt = new JScrollPane(lstItm, 
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scpLt.setFocusable(false);
		scpLt.setBorder(null);
		scpLt.getVerticalScrollBar().setPreferredSize(
			new Dimension(30, scpLt.getVerticalScrollBar().getPreferredSize().height));
		
		this.getContentPane().add(scpLt, BorderLayout.CENTER);

		scpLt.setPreferredSize(new Dimension(450, 355));
		
		// Command
		
		btView = Button.newButton("View,bt_view", this);
		btPrint = Button.newButton("Print,bt_print", this);
		btClose = Button.newButton("Close,bt_close", this);

		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		pnCmd.add(btView);
		pnCmd.add(btPrint);
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.add(btClose);
		pnCmd.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

		this.add(pnCmd, BorderLayout.PAGE_END);
		
		this.pack();
		this.setLocationRelativeTo(this.getParent());
	}
	
	private void _disposeDialog() {
		if (!"bt_close".equals(usrRsp)) {
			if (lstItm.isSelectionEmpty()) return;
		}
		this.dispose();
	}
	
	private void _selectPrintDetail() {
		if (lstItm.isSelectionEmpty()) return;
		
		String[] prnOpts = {"No Detail", "With Detail"};
		String[] prnCmdOpts = {"prn_no_detail", "prn_with_detail"};
		
		DlgSelOpt dlgOpt = DlgSelOpt.newInstance(this, "Select Print Option", prnOpts);
		dlgOpt.showDialog();
		if ("bt_ok".equals(dlgOpt.getUsrRsp())) {
			usrRsp = prnCmdOpts[dlgOpt.getUsrSelOptIndex()];
			this.dispose();
		}
	}
	
	// -----public-----
	
	public void showDialog() {
		try {
			TbSettle[] datas = TbSettle.findAll();
			
			mdItm.clear();
			mdItm.setItems(datas);
			mdItm.insertElementAt(TbSettle.newInstance(), 0);

			this.setVisible(true);
			
		} catch (SQLException e) {
			e.printStackTrace();
			//
			DlgBox dlgx = new DlgBox(this);
			dlgx.showDialog("Error", 
				String.format("<html>Exception:<br>%s</html>",  e.getMessage()), 
				DlgType.Warning);
		}
	}
	
	public String getUsrRsp() {
		return usrRsp;
	}
	
	public TbSettle getSelectedValue() {
		return lstItm.getSelectedValue();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		usrRsp = e.getActionCommand();
		if ("bt_close".equals(usrRsp)) {
			_disposeDialog();
		} else if ("bt_view".equals(usrRsp)) {
			_disposeDialog();
		} else if ("bt_print".equals(usrRsp)) {
			_selectPrintDetail();
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
		
		DlgSelSettle dlg1 = new DlgSelSettle(frm1);
		dlg1.showDialog();
		System.out.printf("-> [%s]\n", dlg1.getUsrRsp());
				
		System.exit(0);
	}
}
