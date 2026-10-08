package app_pos;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import resrc.StdFont;

public class DlgInpText extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JTextArea txtData;
	private String usrRsp = "NA";
	private String orgVal = "";
	private JLabel lbTitle;
	
	// -----------------------------------------
	
	public DlgInpText(Frame prFrm) {
		super(prFrm, "Keyboard", true);
		initComponents();
	}
	
	public DlgInpText(Dialog prDlg) {
		super(prDlg, "Keyboard", true);
		initComponents();
	}
	
	private void initComponents() {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		JPanel pnTop = new JPanel(new BorderLayout());
		lbTitle = new JLabel("XX");
		lbTitle.setFont(StdFont.Fnt18);
		lbTitle.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));
		pnTop.add(lbTitle, BorderLayout.PAGE_START);
		
		txtData = new JTextArea();
		txtData.setFont(StdFont.Fnt16);
		txtData.setRows(4);
		txtData.setMargin(new Insets(5, 5, 5, 5));
		JScrollPane scp1 = new JScrollPane(txtData,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		pnTop.add(scp1, BorderLayout.CENTER);
		
		this.add(pnTop, BorderLayout.PAGE_START);
		
		// Key
		
		PnKeyboard pnTxtPad = PnKeyboard.getInstance();
		this.add(pnTxtPad, BorderLayout.CENTER);
		
		// Command
		
		Button btReset = Button.newButton("Reset,bt_reset", this);
		Button btOk = Button.newOk(this);
		Button btCancel = Button.newCancel(this);

		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		pnCmd.add(btReset);
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.add(btOk);
		pnCmd.add(btCancel);

		this.add(pnCmd, BorderLayout.PAGE_END);
		
		this.setResizable(false);
		this.pack();
	}
	
	private void selectAllText() {
		if (txtData.getText().length() <= 0) return;
		txtData.setSelectionStart(0);
		txtData.setSelectionEnd(txtData.getText().length());
	}
	
	public void setValue(String val) {
		orgVal = val;
		txtData.setText(orgVal);
		selectAllText();
	}
	
	public String getValue() {
		return txtData.getText();
	}
	
	public void showDialog(Dialog _parent, String title) {
		this.setTitle(title);
		lbTitle.setText(title);
		usrRsp = "NA";
		this.validate();
		this.setLocationRelativeTo(_parent);
		this.setVisible(true);
	}
	
	public void showDialog(Frame _parent, String title) {
		this.setTitle(title);
		lbTitle.setText(title);
		usrRsp = "NA";
		this.validate();
		this.setLocationRelativeTo(_parent);
		this.setVisible(true);
	}
	
	public String getUsrRsp() {
		return usrRsp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		usrRsp = e.getActionCommand();
		if ("bt_reset".equals(usrRsp)) {
			txtData.setText(orgVal);
			selectAllText();
		} else if ("bt_cancel".equals(usrRsp)) {
			this.dispose();
		} else if ("bt_ok".equals(usrRsp)) {
			this.dispose();
		}
	}

	public static void main(String[] args) {
		try {
			//UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
		} catch (Exception e) {} 
		
		javax.swing.JFrame frm1 = new javax.swing.JFrame("Test Key Dialog");
		frm1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		javax.swing.JTextArea txt1 = new javax.swing.JTextArea();
		javax.swing.JScrollPane scp1 = new javax.swing.JScrollPane(txt1);
		frm1.getContentPane().add(scp1, BorderLayout.CENTER);
		
		frm1.pack();
		frm1.setSize(1024, 768);
		frm1.setLocationRelativeTo(null);
		frm1.setVisible(true);
		
		DlgInpText dlg1 = new DlgInpText(frm1);
		dlg1.setValue("Input your note.");
		dlg1.showDialog(frm1, "Note?");
		
		System.exit(0);
	}
}
