package app_pos;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import refx.PayBy;
import model.TbPayment;

public class LstRdrPayment extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;
	
    public Component getListCellRendererComponent(
    		JList<?> list, Object value, int index, 
    		boolean isSelected, boolean cellHasFocus) {
    	
        JLabel label = (JLabel) super.getListCellRendererComponent(
            list, value, index, isSelected, cellHasFocus);
        
        TbPayment v1 = (TbPayment)value;
        
        label.setText(v1.dispStr());
        
        label.setBorder(BorderFactory.createCompoundBorder(
    		BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY), 
    		BorderFactory.createEmptyBorder(5, 7, 5, 5)));
        
        if (isSelected) {
        	label.setForeground(Color.WHITE);
	        label.setBackground(Color.GRAY);
        } else {
        	label.setForeground(Color.BLACK);
	        label.setBackground(Color.WHITE);
	        //
	        if ((PayBy.CreditCard == v1.getPmPayBy()) 
        		&& !v1.ccChargeOk()) {
	        	label.setForeground(Color.RED);
	        }
        }
        
        return label;
    }			
}
