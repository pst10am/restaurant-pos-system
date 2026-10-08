package app_admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import model.TbShift;

public class DialogSettlement extends JDialog 
	implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private DefaultListModel<Object> mdObj;
	private JList<Object> lstObj;

	private String usrRsp = "NA";
	
	// constructor
	
	DialogSettlement(Frame _pr) {
		super(_pr, "Select Settlement", true);
		initComponents();
	}
	
	private void initComponents() {
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		mdObj = new DefaultListModel<>();
		lstObj = new JList<>(mdObj);
		
		JScrollPane scp1 = new JScrollPane(lstObj);
		scp1.setBorder(null);

		this.getContentPane().add(scp1, BorderLayout.CENTER);
		
		// command
		
		JPanel pnCmd = new JPanel(new FlowLayout(FlowLayout.TRAILING, 0, 0));
		
		JButton btOk = new JButton("Ok");
		btOk.setActionCommand("bt_ok");
		btOk.addActionListener(this);
		pnCmd.add(btOk);
		
		pnCmd.add(Box.createHorizontalStrut(5));
		
		JButton btCancel = new JButton("Cancel");
		btCancel.setActionCommand("bt_cancel");
		btCancel.addActionListener(this);
		pnCmd.add(btCancel);

		pnCmd.setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY), 
				BorderFactory.createEmptyBorder(5,5,5,5)));
		
		this.getContentPane().add(pnCmd, BorderLayout.PAGE_END);

		this.setResizable(true);
		this.pack();
		this.setSize(310, 310);
	}
	
	void showDialog() {
		try {
			TbShift[] values = TbShift.findAll();
			mdObj.clear();
			if (null != values) {
				for (TbShift shft1 : values) {
					mdObj.addElement(shft1);
				}
			}
			this.setLocationRelativeTo(this.getParent());
			this.setVisible(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	TbShift getValue() {
		return (TbShift)lstObj.getSelectedValue();
	}

	String getUsrRsp() {
		return usrRsp;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		usrRsp = e.getActionCommand();
		if ("bt_cancel".equals(usrRsp)) {
			this.dispose();
		} else if ("bt_ok".equals(usrRsp)) {
			if (null != lstObj.getSelectedValue()) {
				this.dispose();
			}
		}
	}

}
