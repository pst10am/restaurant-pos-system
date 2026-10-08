package test;

import java.awt.BorderLayout;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TestFormatField extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TestFormatField() {
		super();
		initComponents();
		pack();
		setSize(480, 240);
		setVisible(true);
	}
	
	private void initComponents() {
		NumberFormat numfmt = NumberFormat.getIntegerInstance();
		JFormattedTextField fld1 = new JFormattedTextField(numfmt);
		fld1.setColumns(20);
		JPanel pn1 = new JPanel();
		pn1.add(fld1);
		this.getContentPane().add(pn1, BorderLayout.PAGE_START);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new TestFormatField();
	}

}
