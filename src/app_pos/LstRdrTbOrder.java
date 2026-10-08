package app_pos;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import refx.OrderType;
import resrc.ResUtil;
import model.TbOrder;

public class LstRdrTbOrder extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;
	
    public Component getListCellRendererComponent(
    		JList<?> list, Object value, int index, 
    		boolean isSelected, boolean cellHasFocus) {
    	
        JLabel label = (JLabel) super.getListCellRendererComponent(
            list, value, index, isSelected, cellHasFocus);
        
        TbOrder v1 = (TbOrder)value;
        
        label.setText(String.format("<html>#%d %s = $%.2f<br>%s</html>" 
    		,v1.getOrdNo()
    		,ResUtil.dtoc(v1.getOrdDt(), "M/d/yy hh:mma")
    		,v1.getOrdAmtNet()
    		,v1.getCstTxtShort()
		));
        
        label.setBorder(BorderFactory.createCompoundBorder(
    		BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY), 
    		BorderFactory.createEmptyBorder(10, 7, 10, 7)));
        
        /*
        if (isSelected) {
        	label.setForeground(Color.WHITE);
	        label.setBackground(Color.GRAY);
        } else {
	        if (v1.isPaid()) {
	        	label.setForeground(Color.BLACK);
		        label.setBackground(Color.WHITE);
	        } else {
	        	label.setBackground(Color.RED);
	        	label.setForeground(Color.WHITE);
	        }
        }
        */
        
        return label;
    }			
}
