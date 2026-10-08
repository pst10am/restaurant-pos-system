package app_admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.TxCfgGrp;
import model.TxCfgItem;
import refx.ConfigType;
import resrc.ResCfg;
import resrc.ResData;

public class DialogPref extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private JPanel dtPn;
	private java.util.Vector<JComponent> comps;
	
	DialogPref(Frame _pr) {
		super(_pr, "Preference", true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		//
		initComponents();
	}
	
	private void initComponents() {
		
		crPnPref();
		
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
		this.setSize(640, 420);
	}
	
	private void crPnPref() {

		dtPn = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 10));
		JScrollPane scpDt = new JScrollPane(dtPn);
		scpDt.setBorder(null);
		
		this.getContentPane().add(scpDt, BorderLayout.CENTER);
	}
	
	private void crPnGrp(JPanel pnPrf, TxCfgGrp grp1) {
		JPanel pn1 = new JPanel();
		pn1.setLayout(new BoxLayout(pn1, BoxLayout.PAGE_AXIS));
		pn1.setAlignmentX(LEFT_ALIGNMENT);
		
		JLabel lb1 = new JLabel(grp1.getGrpName());
		lb1.setFont(new Font("", Font.BOLD, 18));
		lb1.setAlignmentX(LEFT_ALIGNMENT);
		lb1.setMaximumSize(lb1.getPreferredSize());
		pn1.add(lb1);
		pn1.add(Box.createVerticalStrut(5));
		
		JPanel pnItm = new JPanel();
		pnItm.setLayout(new BoxLayout(pnItm, BoxLayout.PAGE_AXIS));
		pnItm.setAlignmentX(LEFT_ALIGNMENT);
		for (TxCfgItem itm1 : grp1.getItems()) {
			JPanel pnDt = new JPanel();
			pnDt.setAlignmentX(LEFT_ALIGNMENT);
			pnDt.setLayout(new BoxLayout(pnDt, BoxLayout.PAGE_AXIS));
			
			JLabel lb2 = new JLabel(itm1.getCfgName());
			lb2.setAlignmentX(LEFT_ALIGNMENT);
			pnDt.add(lb2);
			
			JPanel pnVal = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 5));
			pnVal.setAlignmentX(LEFT_ALIGNMENT);
			if (ConfigType.FREETEXT == itm1.getCfgType()) {
				JTextField tfld1 = new JTextField(45);
				tfld1.setText(itm1.getCfgValue());
				tfld1.setName(itm1.getCfgKey());
				comps.addElement(tfld1);
				pnVal.add(tfld1);
			} else {
				String[] choices = itm1.getCfgChoices().split(",");
				ButtonGroup btGrp = new ButtonGroup();
				for (String ch1 : choices) {
					if (ConfigType.MULTIPLE == itm1.getCfgType()) {
						JCheckBox cxb1 = new JCheckBox(ch1);
						cxb1.setSelected(itm1.getCfgValue().indexOf(ch1) > -1);
						cxb1.setName(itm1.getCfgKey());
						comps.addElement(cxb1);
						pnVal.add(cxb1);
					} else if (ConfigType.SINGLE == itm1.getCfgType()) {
						JRadioButton rdb1 = new JRadioButton(ch1);
						rdb1.setSelected(itm1.getCfgValue().indexOf(ch1) > -1);
						rdb1.setName(itm1.getCfgKey());
						comps.addElement(rdb1);
						btGrp.add(rdb1);
						pnVal.add(rdb1);
					}
				}
			}
			pnDt.add(pnVal);
			
			pnDt.setBorder(BorderFactory.createEmptyBorder(2, 10, 0, 0));
			pnItm.add(pnDt);
		}
		pnItm.setMaximumSize(pnItm.getPreferredSize());
		pn1.add(pnItm);
		
		pn1.add(Box.createVerticalStrut(10));

		pn1.setMaximumSize(pn1.getPreferredSize());
		pnPrf.add(pn1);
	}
	
	private void saveChanges() {
		Properties prop1 = new Properties();
		for (JComponent cmp1 : comps) {
			if (cmp1 instanceof JTextField) {
				JTextField tfld1 = (JTextField)cmp1;
				//System.out.printf(" Free Text -> [%s=%s]\n", tfld1.getName(), tfld1.getText());
				prop1.setProperty(tfld1.getName(), tfld1.getText());
			} else if (cmp1 instanceof JCheckBox) {
				JCheckBox cxb1 = (JCheckBox)cmp1;
				if (!cxb1.isSelected()) continue;
				//System.out.printf(" Multiple -> [%s=%s]\n", cxb1.getName(), cxb1.getText());
				
				if (!prop1.containsKey(cxb1.getName())) {
					prop1.setProperty(cxb1.getName(), "");
				}
				String val1 = prop1.getProperty(cxb1.getName());
				StringBuilder bld1 = new StringBuilder();
				bld1.append(val1);
				if (bld1.length() > 0) {
					bld1.append(",");
				}
				bld1.append(cxb1.getText());
				
				prop1.setProperty(cxb1.getName(), bld1.toString());
				
			} else if (cmp1 instanceof JRadioButton) {
				JRadioButton rdb1 = (JRadioButton)cmp1;
				if (!rdb1.isSelected()) continue;
				//System.out.printf(" Single -> [%s=%s]\n", rdb1.getName(), rdb1.getText());
				prop1.setProperty(rdb1.getName(), rdb1.getText());
			}
		}
		//		
		try {
			ResData.saveConfig(prop1);
			this.dispose();
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", ERROR);
		}
	}
	
	// --
	
	void showDialog() {
		if (null == comps) {
			comps = new java.util.Vector<>();
		}
		comps.clear();
		try {
			dtPn.removeAll();
			
			JPanel pnPrf = new JPanel();
			pnPrf.setLayout(new BoxLayout(pnPrf, BoxLayout.PAGE_AXIS));
			
			java.util.Vector<TxCfgGrp> cfgGrp = TxCfgGrp.getConfigGroups();
			for (TxCfgGrp grp1 : cfgGrp) {
				crPnGrp(pnPrf, grp1);
			}
			
			dtPn.add(pnPrf);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ("bt_cancel".equals(cmd)) {
			this.dispose();
		} else if ("bt_ok".equals(cmd)) {
			saveChanges();
		}
	}
}
