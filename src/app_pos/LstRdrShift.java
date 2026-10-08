package app_pos;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import resrc.ResUtil;
import model.TbShift;

public class LstRdrShift extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;
	
    public Component getListCellRendererComponent(
    		JList<?> list, Object value, int index, 
    		boolean isSelected, boolean cellHasFocus) {
    	
        JLabel label = (JLabel) super.getListCellRendererComponent(
            list, value, index, isSelected, cellHasFocus);
        
        TbShift v1 = (TbShift)value;
        
        label.setText(String.format("%s%s",
    		ResUtil.dtoc(v1.getClDt(), "dd/MM/yy HH:mm:ss"),
    		(v1.getClId()==0?" CURRENT":"")
    		));
        
        label.setBorder(BorderFactory.createCompoundBorder(
    		BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY), 
    		BorderFactory.createEmptyBorder(10, 7, 10, 7)));
        
        return label;
    }			
}
