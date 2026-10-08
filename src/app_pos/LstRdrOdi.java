package app_pos;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import refx.OrderItemType;
import resrc.ResUtil;
import model.TbOrderItem;

public class LstRdrOdi extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;

	/*
	private static final String html1 = "<html>"
		+"<table border=1 cellspacing=0 cellpadding=0 width=290>"
		+"<tr>"
		+ "<td align=left valign=top width=30 ><font size=4>%d</font></td>"
		+ "<td align=left valign=top>%s</td>"
		+ "<td align=right valign=top width=80><font size=4>%.2f</font></td>"
		+"</tr>"
		+"%s"
		+"%s"
		+"</table></html>";
		*/
	private static final String html1 = "<html>"
		+"<table border=0 cellspacing=0 cellpadding=0 width=300>"
		+"<tr>"
		+ "<td align=right width=30><b>%d&nbsp;&nbsp;</b></td>"
		+ "<td align=left width=200><b>%s</b></td>"
		+ "<td align=right width=70><b>%.2f</b></td>"
		+"</tr>"
		+"%s"
		+"%s"
		+"</table></html>";
	
    public Component getListCellRendererComponent(
    		JList<?> list, Object value, int index, 
    		boolean isSelected, boolean cellHasFocus) {
    	
        JLabel label = (JLabel) super.getListCellRendererComponent(
            list, value, index, isSelected, cellHasFocus);
        
        TbOrderItem odi1 = (TbOrderItem)value;
        
        if (OrderItemType.Line == odi1.getOdiType()) {
        	label.setText("- SEPARATOR -");
        	label.setHorizontalAlignment(CENTER);
        } else {
            // option
            String optTxt = odi1.getOpiStr();
            if (!optTxt.isEmpty()) {
            	//optTxt = String.format("<tr><td colspan=3 align=left width=290>%s</td></tr>", optTxt);
            	optTxt = String.format("<tr><td></td><td colspan=2 width=260><font size=3>%s</font></td></tr>", optTxt);
            }
            
            // note
            String optNote = "";
            if (!odi1.getOdiNote().isEmpty()) {
            	optNote = odi1.getOdiNote().trim();
            }
            if (odi1.getOdiExtra() > 0) {
            	optNote = String.format("%s(+$%.2f)", optNote, odi1.getOdiExtra());
            }
            if (!optNote.isEmpty()) {
            	//optNote = String.format("<tr><td colspan=3 align=left width=290>%s</td></tr>", optNote);
            	optNote = String.format(
        			"<tr><td></td><td colspan=2><b>*Note/Special*</b></td></tr>"
        			+ "<tr><td></td><td colspan=2 width=260>%s</td></tr>", optNote);
            }
            
            label.setText(String.format(html1, 
        		odi1.getOdiQty(), ResUtil.padl(odi1.getItmNamePos(), 20), odi1.getOdiAmtBf(), 
        		optTxt, 
        		optNote));
        }

        label.setBorder(BorderFactory.createCompoundBorder(
    		BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY), 
    		BorderFactory.createEmptyBorder(8, 7, 8, 7)));
        
        if (isSelected) {
        	label.setForeground(Color.WHITE);
	        label.setBackground(Color.GRAY);
        } else {
	        if (odi1.getOdiId() > 0) {
	        	label.setForeground(Color.decode("#438AD1"));
	        } else {
	        	label.setForeground(Color.BLACK);
	        }
	        label.setBackground(Color.WHITE);
        }
        
        if (odi1.isHold()) {
            label.setForeground(Color.RED);
        }
        
        label.setMaximumSize(new Dimension(340, Short.MAX_VALUE));
        
        return label;
    }			
}
