package app_pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import refx.OrderType;
import resrc.ResData;
import resrc.StdFont;
import model.TxFdCat;
import model.TxFdItem;

public class PnMenu extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private DefaultListModel<TxFdCat> mdCat;
	private DefaultListModel<TxFdItem> mdItem;
	private JList<TxFdCat> lstCat;
	private JList<TxFdItem> lstItem;
	private JScrollPane scpItem;
	
	private PnMenuIntf mnuIntf;
	private JButton btSep;
	
	public PnMenu(PnMenuIntf _mnuIntf) {
		super(new BorderLayout());
		
		mnuIntf = _mnuIntf;
		
		initComponents();
	}
	
	private void initComponents() {
		mdCat = new DefaultListModel<TxFdCat>();
		lstCat = new JList<TxFdCat>(mdCat);
		lstCat.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		lstCat.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		lstCat.setVisibleRowCount(-1);
		lstCat.setFont(StdFont.Fnt18B);
		lstCat.setCellRenderer(new DefaultListCellRenderer () {
			private static final long serialVersionUID = 1L;
			@Override
		    public Component getListCellRendererComponent(JList<?> list, Object
		            value, int index, boolean isSelected, boolean cellHasFocus) {
		        JLabel label = (JLabel) super.getListCellRendererComponent(
		            list, value, index, isSelected, cellHasFocus);
		        /*
		        label.setBorder(BorderFactory.createCompoundBorder(
		        		BorderFactory.createMatteBorder(0,0,1,1,Color.LIGHT_GRAY), 
		        		BorderFactory.createEmptyBorder(10, 15, 10, 20)));
		        if (isSelected) {
		        	label.setForeground(Color.WHITE);
			        label.setBackground(Color.GRAY);
		        } else {
		        	label.setForeground(Color.BLACK);
			        label.setBackground(Color.WHITE);
		        }
		        */
		        TxFdCat cat1 = (TxFdCat)value;
	        	label.setForeground(Color.BLACK);
		        label.setBackground(cat1.getCatBgColor());
		        if (isSelected) {
			        label.setBorder(BorderFactory.createCompoundBorder(
		        		BorderFactory.createMatteBorder(3, 3, 3, 3, Color.MAGENTA), 
		        		BorderFactory.createEmptyBorder(12, 7, 13, 13)));
			        //label.setHorizontalAlignment(CENTER);
		        } else {
			        label.setBorder(BorderFactory.createCompoundBorder(
		        		BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY), 
		        		BorderFactory.createEmptyBorder(15, 10, 15, 15)));
			        //label.setHorizontalAlignment(LEFT);
		        }
		        return label;
		    }			
		});
		lstCat.setBackground(Color.decode("#E4E4E4"));
		lstCat.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) return;
				ListSelectionModel smd = lstCat.getSelectionModel();
				if (smd.isSelectionEmpty()) return;
				fdCatSelected(lstCat.getSelectedValue());
			}
		});
		
		mdItem = new DefaultListModel<TxFdItem>();
		lstItem = new JList<TxFdItem>(mdItem);
		lstItem.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		lstItem.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		lstItem.setVisibleRowCount(-1);
		lstItem.setFont(StdFont.Fnt18);
		lstItem.setCellRenderer(new DefaultListCellRenderer () {
			private static final long serialVersionUID = 1L;
			@Override
		    public Component getListCellRendererComponent(JList<?> list, Object
		            value, int index, boolean isSelected, boolean cellHasFocus) {
		        JLabel label = (JLabel) super.getListCellRendererComponent(
		            list, value, index, isSelected, cellHasFocus);
		        label.setBorder(BorderFactory.createCompoundBorder(
		        		BorderFactory.createMatteBorder(0,0,1,1,Color.GRAY), 
		        		BorderFactory.createEmptyBorder(15, 15, 15, 5)));
		        if (isSelected) {
		        	label.setForeground(Color.WHITE);
			        label.setBackground(Color.GRAY);
		        } else {
		        	label.setForeground(Color.BLACK);
			        label.setBackground(Color.WHITE);
		        }
		        return label;
		    }			
		});
		lstItem.setBackground(Color.decode("#CACAC1"));
		lstItem.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) return;
				ListSelectionModel smd = lstItem.getSelectionModel();
				if (smd.isSelectionEmpty()) return;
				
				int sind = lstItem.getSelectedIndex();
				Rectangle rct1 = lstItem.getCellBounds(sind, sind);
				if (!rct1.contains(lstItem.getMousePosition())) {
					lstItem.clearSelection();
				} else {
					menuItemSelected(lstItem.getSelectedValue());
				}
			}
		});
		
		JScrollPane scpCat = new JScrollPane(lstCat, 
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		scpItem = new JScrollPane(lstItem, 
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		scpCat.getVerticalScrollBar().setPreferredSize(new Dimension(30, scpCat.getVerticalScrollBar().getPreferredSize().height));
		scpItem.getVerticalScrollBar().setPreferredSize(new Dimension(30, scpItem.getVerticalScrollBar().getPreferredSize().height));

		scpCat.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
		scpItem.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
		
		JSplitPane splt1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scpCat, scpItem);
		splt1.setDividerLocation(250);
		splt1.setDividerSize(20);
		
		this.add(splt1, BorderLayout.CENTER);
		
		crPnCmd();
		//this.setBackground(Color.CYAN);
		//this.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
	}
	
	private void crPnCmd() {
		JButton btOpenItm = UIFactory.buttonC("Open Item", "bt_open_item", this);
		btSep = UIFactory.buttonC("Separator", "bt_sep_item", this);
		
		JPanel pnCmd = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
		pnCmd.add(btOpenItm);
		pnCmd.add(btSep);
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.GRAY));

		this.add(pnCmd, BorderLayout.PAGE_END);
	}
	
	private void fdCatSelected(TxFdCat cat1) {
		lstItem.setBackground(cat1.getCatBgColor());
		mdItem.clear();
		try {
			java.util.Vector<TxFdItem> items = 
				TxFdItem.getItemsInCategory(cat1.getCatId());
			for (TxFdItem item1 : items) {
				mdItem.addElement(item1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		lstItem.updateUI();
	}
	
	private void menuItemSelected(TxFdItem itm1) {
		mnuIntf.menuItemSelected(itm1);
	}
	
	public void refresh() {
		mdCat.clear();
		mdItem.clear();
		try {
			java.util.Vector<TxFdCat> cats = TxFdCat.getFoodCats();
			for (TxFdCat cat1 : cats) {
				mdCat.addElement(cat1);
			}
			lstCat.updateUI();
			lstItem.updateUI();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ("bt_open_item".equals(cmd)) {
			menuItemSelected(TxFdItem.newOpenItem());
		} else if ("bt_sep_item".equals(cmd)) {
			menuItemSelected(TxFdItem.newSeparatorItem());
		}
	}

	public void clearSelection() {
		lstItem.clearSelection();
	}

	public void updateBtCommand(OrderType otype) {
		btSep.setVisible(OrderType.DineIn == otype);
	}
}
