package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import resrc.StdFont;
import model.TxSctTable;

public class DlgUnlockTbl extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;

	private String usrRsp = "NA";
	
	public DlgUnlockTbl(Frame _prFrm, TxSctTable _tbl) {
		super(_prFrm, "Unlock Table", true);
		
		initComponents(_tbl);
		
		this.setVisible(true);
	}
	
	private void initComponents(TxSctTable _tbl) {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		// Label
		
		JLabel lb1 = new JLabel(String.format("Unlock Table [%s]?", _tbl.getTblName()));
		lb1.setFont(StdFont.Fnt22);
		lb1.setBorder(BorderFactory.createEmptyBorder(20, 20, 45, 0));
		lb1.setPreferredSize(new Dimension(350, lb1.getPreferredSize().height));
		lb1.setBackground(Color.decode("#FFD699"));
		lb1.setOpaque(true);
		this.getContentPane().add(lb1, BorderLayout.CENTER);
		
		// Command
		
		Button btUnlock = Button.newButtonSizeToText("Unlock,bt_unlock", this);
		Button btCancel = Button.newCancel(this);

		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		pnCmd.add(btUnlock);
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.add(btCancel);
		pnCmd.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

		this.add(pnCmd, BorderLayout.PAGE_END);
		
		this.setResizable(false);
		this.pack();
		this.setLocationRelativeTo(this.getParent());
	}
	
	public String getUsrRsp() {
		return usrRsp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		usrRsp = e.getActionCommand();
		if ("bt_unlock".equals(usrRsp)) {
			this.dispose();
		} else if ("bt_cancel".equals(usrRsp)) {
			this.dispose();
		}
	}

}
