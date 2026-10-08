package test;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class TestMono extends JFrame {
	private static final long serialVersionUID = 1L;

	public TestMono() {
		super("TestMono");
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		initComponents();
		
		this.pack();
		this.setSize(800, 600);
		this.setVisible(true);
	}
	
	private static String cellFmt = 
		"<html>"
		+ "<table border=0 cellspacing=0 cellpadding=0 width='355'>"
		+ "<tr>"
		+ "<td width=35 align='right'>%d</td><td width=5></td>"
		+ "<td>%s</td>"
		+ "<td width=90 align='right'>%.2f</td>"
		+ "</tr>"
		+ "<tr>"
		+ "<td colspan=2></td>"
		+ "<td colspan=2>%s</td>"
		+ "</tr>"
		+ "</table></html>";
	private void initComponents() {
		String[] dtLst = {
			String.format(cellFmt, 1, "Green Curry ExE", 7.97, "Hot,Beef"),
			"<html> 2 Kanom Mor Kang     16.35</html>",
			//"<html>10 Mussaman           20.95<br>Hot,Shrimp,No Fish Sauce(+$1)</html>",
			"<html> 2 Pepsi               6.95</html>"
		};
		JList<String> lst1 = new JList<>(dtLst);
		lst1.setFont(new Font("monospaced", Font.PLAIN, 22));
		JScrollPane scp1 = new JScrollPane(lst1);
		this.getContentPane().add(scp1, BorderLayout.LINE_START);
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			//UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
		} catch (Exception e) {} 
		new TestMono();
	}

}
