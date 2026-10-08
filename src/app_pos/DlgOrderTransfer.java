package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import model.TbOrder;

public class DlgOrderTransfer extends JDialog 
	implements PnSpltOrdIntf, ActionListener {
	private static final long serialVersionUID = 1L;
	
	private PnSpltOrd ordFrm, ordTo;
	private String usrRsp = "NA";
	
	DlgOrderTransfer(Frame _pr, TbOrder _frm, TbOrder _to) {
		super(_pr, "Transfer Order", true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		initComponents(_frm, _to);
		
		this.pack();
		this.setLocationRelativeTo(_pr);
		this.setVisible(true);
	}
	
	private void initComponents(TbOrder _frm, TbOrder _to) {
		ordFrm = new PnSpltOrd(_frm, false, this);
		ordTo = new PnSpltOrd(_to, false, this);
		
		JSplitPane spltPn = new JSplitPane(
			JSplitPane.HORIZONTAL_SPLIT, ordFrm, ordTo);
		spltPn.setBorder(null);
		
		this.getContentPane().add(spltPn, BorderLayout.CENTER);
		
		// Command
		
		Button btOk = Button.newOk(this);
		Button btCancel = Button.newOk(this);

		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.add(btOk);
		pnCmd.add(btCancel);
		pnCmd.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

		this.add(pnCmd, BorderLayout.PAGE_END);
	}
	
	private void disposeDialog() {
		this.dispose();
	}
	
	public String getUsrRsp() {
		return usrRsp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		usrRsp = e.getActionCommand();
		if ("bt_cancel".equals(usrRsp)) {
			disposeDialog();
		}
	}

	@Override
	public void headClick(PnSpltOrd spOrd, boolean moveItem) {
		// TODO Auto-generated method stub
	}
}
