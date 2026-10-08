package app_pos;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import resrc.ResUtil;
import model.TbPayment;

public class LstRdrTip extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;
	
    public Component getListCellRendererComponent(
    		JList<?> list, Object value, int index, 
    		boolean isSelected, boolean cellHasFocus) {
    	
        JLabel label = (JLabel) super.getListCellRendererComponent(
            list, value, index, isSelected, cellHasFocus);
        
        TbPayment v1 = (TbPayment)value;
        
        label.setText(String.format(
        		"Order# %d %s  xx%s $%8.2f Tip=%8.2f"
        		,v1.getOrdNo()
        		,ResUtil.dtoc(v1.getPmDt(), "hh:mma")
        		,v1.getCCLast4Digits()
        		,v1.getPmAmt()
        		,v1.getPmTip()));
        
        label.setBorder(BorderFactory.createCompoundBorder(
    		BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY), 
    		BorderFactory.createEmptyBorder(5, 7, 5, 5)));
        
        if (v1.getPmTip() <= 0) {
        	label.setForeground(Color.RED);
        }
                
        return label;
    }			
}
