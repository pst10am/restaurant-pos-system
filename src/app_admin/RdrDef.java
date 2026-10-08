package app_admin;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

public class RdrDef extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;
	
    public Component getListCellRendererComponent(
    		JList<?> list, Object value, int index, 
    		boolean isSelected, boolean cellHasFocus) {
    	
        JLabel label = (JLabel) super.getListCellRendererComponent(
            list, value, index, isSelected, cellHasFocus);

        label.setBorder(BorderFactory.createCompoundBorder(
    		BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY), 
    		BorderFactory.createEmptyBorder(3, 5, 3, 3)));

        return label;
    }			
}
