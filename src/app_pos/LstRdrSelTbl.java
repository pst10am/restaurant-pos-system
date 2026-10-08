package app_pos;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import refx.TableService;
import model.TxSctTable;

public class LstRdrSelTbl extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;
	
    public Component getListCellRendererComponent(
    		JList<?> list, Object value, int index, 
    		boolean isSelected, boolean cellHasFocus) {
    	
        JLabel label = (JLabel) super.getListCellRendererComponent(
            list, value, index, isSelected, cellHasFocus);
        
        TxSctTable v1 = (TxSctTable)value;
        
        String tblStr = "";
        if (TableService.Ready != v1.getTblService()) {
        	tblStr = " "+v1.getTblService().toString();
        }
        
        label.setText(String.format("Table %s%s", v1.getTblName(), tblStr));
        
        label.setBorder(BorderFactory.createCompoundBorder(
    		BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY), 
    		BorderFactory.createEmptyBorder(10, 7, 10, 7)));
        
        if (isSelected) {
        	label.setForeground(Color.WHITE);
	        label.setBackground(Color.GRAY);
        } else {
        	label.setForeground(Color.BLACK);
	        label.setBackground(Color.WHITE);
        }
        
        return label;
    }			
}
