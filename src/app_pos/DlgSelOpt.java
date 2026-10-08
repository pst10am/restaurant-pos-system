package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
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

public class DlgSelOpt extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private String usrRsp = "NA";
	private JList<String> lst1;
	
	public static DlgSelOpt newInstance(Frame _pr, String _title, String[] _opts) {
		return new DlgSelOpt(_pr, _title, _opts);
	}
	
	public static DlgSelOpt newInstance(Dialog _pr, String _title, String[] _opts) {
		return new DlgSelOpt(_pr, _title, _opts);
	}
	
	private DlgSelOpt(Frame _pr, String _title, String[] _opts) {
		super(_pr, _title, true);
		initComponents(_opts);
	}
	
	private DlgSelOpt(Dialog _pr, String _title, String[] _opts) {
		super(_pr, _title, true);
		initComponents(_opts);
	}
	
	private void initComponents(String[] opts) {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		lst1 = new JList<>(opts);
		lst1.setFont(StdFont.Fnt16);
		lst1.setVisibleRowCount(-1);
		lst1.setCellRenderer(new LstRdrString());
		lst1.setFocusable(false);
		lst1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane scpOpt = new JScrollPane(lst1, 
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scpOpt.setFocusable(false);
		scpOpt.setBorder(null);
		scpOpt.getVerticalScrollBar().setPreferredSize(
			new Dimension(30, scpOpt.getVerticalScrollBar().getPreferredSize().height));
		scpOpt.setBorder(null);
		
		this.getContentPane().add(scpOpt, BorderLayout.CENTER);
		
		Button btOk = Button.newOk(this);
		Button btCancel = Button.newCancel(this);

		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.add(btOk);
		pnCmd.add(btCancel);
		pnCmd.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

		this.add(pnCmd, BorderLayout.PAGE_END);
		
		this.pack();
		this.setResizable(false);
		this.setSize(new Dimension(400, 300));
	}
	
	// --
	
	public void showDialog() {
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}
	
	public String getUsrSelOpt() {
		if (lst1.isSelectionEmpty()) return null;
		return lst1.getSelectedValue();
	}
	
	public int getUsrSelOptIndex() {
		if (lst1.isSelectionEmpty()) return -1;
		return lst1.getSelectedIndex();
	}
	
	public String getUsrRsp() {
		return usrRsp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		usrRsp = e.getActionCommand();
		if ("bt_ok".equals(usrRsp)) {
			if (lst1.isSelectionEmpty()) {
				return;
			}
		}
		this.dispose();
	}

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
		
		String[] prnOpts = {"No Detail", "With Detail"};
		
		DlgSelOpt dlgOpt = DlgSelOpt.newInstance(frm1, "Select Print Option", prnOpts);
		dlgOpt.showDialog();
		
		System.out.println(dlgOpt.getUsrRsp());
		if ("bt_ok".equals(dlgOpt.getUsrRsp())) {
			System.out.println(" >>> "+ dlgOpt.getUsrSelOpt());
		}
		
		System.exit(0);
	}
}
