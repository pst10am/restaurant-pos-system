package app_pos;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import model.TbOrder;

public class LstRdrOrdWFP extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;
	
    public Component getListCellRendererComponent(
    		JList<?> list, Object value, int index, 
    		boolean isSelected, boolean cellHasFocus) {
    	
        JLabel label = (JLabel) super.getListCellRendererComponent(
            list, value, index, isSelected, cellHasFocus);
        
        TbOrder v1 = (TbOrder)value;
        
        label.setText(String.format("#%d [%s] %s $%.2f", 
    		v1.getOrdNo(),
    		v1.getOrdToGoType().toString(),
    		v1.getCstName(),
    		v1.getOrdAmtNet()
    		));
        
        label.setBorder(BorderFactory.createCompoundBorder(
    		BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY), 
    		BorderFactory.createEmptyBorder(10, 7, 10, 7)));
        
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
        
        return label;
    }			
}
