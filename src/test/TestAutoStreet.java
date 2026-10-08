package test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import model.TxStreet;

public class TestAutoStreet extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	JTextArea ta1;
	
	JList<TxStreet> lst1;
	DefaultListModel<TxStreet> md1;
	
	public TestAutoStreet() {
		super("test auto fill street name");
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		initComponents();
		
		this.pack();
		this.setSize(640, 220);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	private void initComponents() {
		
		ta1 = new JTextArea();
		ta1.addKeyListener(new StLst());
		
		JScrollPane scp1 = new JScrollPane(ta1);
		
		this.getContentPane().add(scp1, BorderLayout.CENTER);
		
		md1 = new DefaultListModel<TxStreet>();
		lst1 = new JList<TxStreet>(md1);
		
		JScrollPane scp2 = new JScrollPane(lst1, 
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scp2.getVerticalScrollBar().setPreferredSize(
			new Dimension(25, 0)); 
		scp2.setPreferredSize(new Dimension(200, scp2.getPreferredSize().height));
		
		JPanel pnInfo = new JPanel(new BorderLayout());
		pnInfo.add(scp2, BorderLayout.CENTER);
		JButton btSel = new JButton("Select");
		btSel.setFocusable(false);
		btSel.setActionCommand("addr_select");
		btSel.addActionListener(this);
		pnInfo.add(btSel, BorderLayout.PAGE_END);
		
		this.getContentPane().add(pnInfo, BorderLayout.LINE_END);
	}

	class StLst implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
			String txt1 = ta1.getText();
			if (TxStreet.partMatched(txt1)) {
				lst1.clearSelection();
				md1.clear();
				for (TxStreet sif : TxStreet.values) {
					if (null == sif) break;
					md1.addElement(sif);
				}
			}
		}

	
	}
	
	private void updateAddrText() {
		TxStreet sif = lst1.getSelectedValue();
		String curTxt = ta1.getText();

		// address no.
		String addrNo = "";
		int locNo = curTxt.indexOf(" ");
		if (locNo > -1) {
			try {
				Integer.parseInt(curTxt.substring(0, locNo));
				addrNo = String.format("%s ",curTxt.substring(0, locNo));
			} catch (Exception e) {
				addrNo = "";
			}
		}
		
		// appt no, other infos.
		String othInfo = "";
		int loc1 = curTxt.lastIndexOf(","); 
		if (loc1 > 0) {
			othInfo = curTxt.substring(loc1);
		}
		
		ta1.setText(String.format("%s%s%s", addrNo, sif.stFullName, othInfo));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String cmd = e.getActionCommand();
		if ("addr_select".equals(cmd)) {
			TxStreet sif = lst1.getSelectedValue();
			if (null == sif) return;
			System.out.printf(">>-->> [%s]\n", sif);
			updateAddrText();
		}
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		TxStreet.init();
		new TestAutoStreet();
	}
}
