package test;

import java.awt.BorderLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.text.DefaultCaret;

import model.CCData;
import resrc.StdFont;
import app_pos.MSRCCIntf;
import app_pos.MSRCCLst;

public class TestMSR extends JFrame implements MSRCCIntf {
	private static final long serialVersionUID = 1L;
	
	private JTextArea ta1;

	public TestMSR() {
		super();
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		initComponents();
		
		this.pack();
		this.setSize(640, 480);
		this.setLocation(100, 100);
		this.setVisible(true);
	}
	
	private void initComponents() {
		
		ta1 = new JTextArea("for logging purpose ... \n\n");
		ta1.setEditable(false);
		ta1.setFocusable(false);
		ta1.setFont(StdFont.Fnt14);
		ta1.setMargin(new Insets(5, 5, 5, 5));
		DefaultCaret caret = (DefaultCaret)ta1.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		JScrollPane scp1 = new JScrollPane(ta1);
		scp1.setBorder(null);
		
		this.getContentPane().add(scp1, BorderLayout.CENTER);
		
		this.addKeyListener(new MSRCCLst(this));
	}
	
	@Override
	public void creditCardSwiped(CCData cdt1) {
		ta1.append("Card Swiped ... \n");
		ta1.append(String.format(
			"%s\nAcct No [%s]\nAcct Name [%s]\nExp Month[%s]\nExp Year[%s]\nTrack 1[%s]\nTrack 2[%s]\n",
			cdt1.isValid() ? "Card is valid.":"Card is not valid.",
			cdt1.getAcctNo(), 
			cdt1.getAcctName(), 
			cdt1.getExpMonth(), 
			cdt1.getExpYear(), 
			cdt1.getTrk1(), 
			cdt1.getTrk2()));
		ta1.append("\n\n");
	}

	public static void main(String[] args) {
		MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
		//
		new TestMSR();
	}

	@Override
	public void cardReadError() {
		// TODO Auto-generated method stub
		
	}
}
