package app_pos;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import model.TbOrderItem;
import model.TxOpt;
import model.TxOptItem;
import refx.OptionType;
import refx.OrderItemType;
import resrc.ResCfg;
import resrc.ResData;
import resrc.StdFont;

public class PnTbOrderItem extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private static int DEF_OPT_COL = 3;
	static {
		DEF_OPT_COL = ResCfg.getOptionColumn();
	}
	
	private JPanel pnCt, pnOpt;
	private JButton btQty, btNote, btExtra, btHold, btOpName, btOpPrice;
	private JLabel lbItmNamePos, lbAmtBf, lbPrice, lbTax, lbOpTax;
	
	private TbOrderItem odi1 = null;
	private PnTbOrderItemIntf odiIntf;
	
	private boolean noEdit = false;
	
	private JPanel pnItmOptEx;
	private JLabel lbItmNoEdit;
	
	// constructor
	
	public PnTbOrderItem(PnTbOrderItemIntf _intf) {
		super(new BorderLayout());
		odiIntf = _intf;
		initComponents();
	}
	
	// private
	
	private void initComponents() {
		
		pnCt = new JPanel();
		pnCt.setLayout(new CardLayout());
		
		pnCt.add(crPnItem(), OrderItemType.Item.toString());
		pnCt.add(crPnOpenItem(), OrderItemType.OpenItem.toString());
		pnCt.add(crPnLine(), OrderItemType.Line.toString());
		
		//
		
		JPanel pnCt2 = new JPanel(new BorderLayout());
		pnCt2.add(pnCt, BorderLayout.CENTER);
		
		// command buttons
		
		JPanel pnCmd = new JPanel();
		pnCmd.setLayout(new BoxLayout(pnCmd, BoxLayout.LINE_AXIS));
		
		pnCmd.add(crCmdBt("Delete", "bt_delete", this));
		pnCmd.add(Box.createHorizontalStrut(5));
		pnCmd.add(crCmdBt("Up", "bt_up", this));
		pnCmd.add(Box.createHorizontalStrut(5));		
		pnCmd.add(crCmdBt("Down", "bt_down", this));
		pnCmd.add(Box.createHorizontalStrut(5));
		btHold = crCmdBt("Hold", "bt_hold", this);
		pnCmd.add(btHold);
		
		pnCmd.add(Box.createHorizontalGlue());
		pnCmd.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		
		pnCt2.add(pnCmd, BorderLayout.PAGE_END);
		
		this.add(pnCt2, BorderLayout.CENTER);
	}
	
	private JPanel crPnLine() {
		return new JPanel();
	}
	
	private JPanel crPnOpenItem() {
		JPanel pnOp = new JPanel();
		pnOp.setLayout(new BoxLayout(pnOp, BoxLayout.LINE_AXIS));
		
		btOpName = new JButton("Item:");
		btOpName.setFocusable(false);
		btOpName.setFont(StdFont.Fnt14);
		btOpName.setHorizontalAlignment(SwingConstants.LEFT);
		btOpName.setActionCommand("bt_op_name");
		btOpName.addActionListener(this);
		btOpName.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY), 
			BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		btOpName.setMaximumSize(new Dimension(Short.MAX_VALUE, 
			btOpName.getPreferredSize().height));
		btOpName.setAlignmentX(LEFT_ALIGNMENT);
		btOpName.setAlignmentY(TOP_ALIGNMENT);
		
		btOpPrice = new JButton("<html>Price($): <b>0000.00</b></html>");
		btOpPrice.setFocusable(false);
		btOpPrice.setFont(StdFont.Fnt14);
		btOpPrice.setActionCommand("bt_op_price");
		btOpPrice.addActionListener(this);
		btOpPrice.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(1, 0, 1, 1, Color.GRAY), 
			BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		btOpPrice.setPreferredSize(btOpPrice.getPreferredSize()); 
		btOpPrice.setMaximumSize(new Dimension(btOpPrice.getPreferredSize().width+45, 
			btOpPrice.getPreferredSize().height));
		btOpPrice.setAlignmentX(LEFT_ALIGNMENT);
		btOpPrice.setAlignmentY(TOP_ALIGNMENT);
		
		pnOp.add(btOpName);
		pnOp.add(btOpPrice);
		
		pnOp.setAlignmentX(LEFT_ALIGNMENT);
		pnOp.setAlignmentY(TOP_ALIGNMENT);
		
		//

		lbOpTax = new JLabel("Qty: 1 Tax(%): ??");
		lbOpTax.setFont(StdFont.Fnt14);
		lbOpTax.setBorder(BorderFactory.createEmptyBorder(15, 10, 0, 0));
		lbOpTax.setMaximumSize(new Dimension(Short.MAX_VALUE, lbOpTax.getPreferredSize().height));
		lbOpTax.setAlignmentX(LEFT_ALIGNMENT);
		lbOpTax.setAlignmentY(TOP_ALIGNMENT);
		
		//
		
		JPanel _pnOp = new JPanel();
		_pnOp.setLayout(new BoxLayout(_pnOp, BoxLayout.PAGE_AXIS));
		
		_pnOp.add(pnOp);
		_pnOp.add(lbOpTax);

		return _pnOp;
	}
	
	private JPanel crPnItem() {
		
		// Item Info
		
		btQty = new JButton("Qty: 1");
		btQty.setFocusable(false);
		btQty.setFont(StdFont.Fnt16B);
		btQty.setActionCommand("bt_qty");
		btQty.addActionListener(this);
		
		lbItmNamePos = new JLabel("<Item-Name>");
		lbItmNamePos.setFont(StdFont.Fnt16);
		
		lbAmtBf = new JLabel("<Amount>");
		lbAmtBf.setFont(StdFont.Fnt16B);
		
		lbPrice = new JLabel("<Price>");
		lbPrice.setFont(StdFont.Fnt14);
		lbPrice.setAlignmentX(LEFT_ALIGNMENT);
		
		lbTax = new JLabel("<Tax>");
		lbTax.setFont(StdFont.Fnt14);
		lbTax.setAlignmentX(LEFT_ALIGNMENT);
		
		JPanel pnInf1 = new JPanel();
		pnInf1.setLayout(new BoxLayout(pnInf1, BoxLayout.LINE_AXIS));
		pnInf1.add(btQty);
		pnInf1.add(Box.createHorizontalStrut(10));
		pnInf1.add(lbItmNamePos);
		pnInf1.add(Box.createHorizontalGlue());
		pnInf1.add(lbAmtBf);
		pnInf1.setAlignmentX(LEFT_ALIGNMENT);
		
		JPanel pnInf2 = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
		pnInf2.add(lbPrice);
		pnInf2.add(Box.createHorizontalStrut(10));
		pnInf2.add(lbTax);
		pnInf2.add(Box.createHorizontalGlue());
		pnInf2.setAlignmentX(LEFT_ALIGNMENT);
		
		JPanel pnInfo = new JPanel();
		pnInfo.setLayout(new BoxLayout(pnInfo, BoxLayout.PAGE_AXIS));
		pnInfo.add(pnInf1);
		pnInfo.add(Box.createVerticalStrut(10));
		pnInfo.add(pnInf2);
		pnInfo.add(Box.createVerticalStrut(10));
		
		// Options
		
		pnOpt = new JPanel();
		pnOpt.setLayout(new FlowLayout(FlowLayout.LEADING));
		pnOpt.setBorder(BorderFactory.createEmptyBorder(0, 5, 30, 0));
		// new BoxLayout(pnOpt, BoxLayout.PAGE_AXIS));
		JScrollPane scpOpt = new JScrollPane(pnOpt, 
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scpOpt.getVerticalScrollBar().setPreferredSize(new Dimension(
			30, scpOpt.getVerticalScrollBar().getPreferredSize().height));
		
		// Extra
		
		JPanel pnExtra = new JPanel(new BorderLayout());
		
		btNote = new JButton("Note:");
		btNote.setFocusable(false);
		btNote.setFont(StdFont.Fnt14);
		btNote.setHorizontalAlignment(SwingConstants.LEFT);
		btNote.setActionCommand("bt_note");
		btNote.addActionListener(this);
		btNote.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY), 
			BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		
		btExtra = new JButton("Extra($): 0");
		btExtra.setFocusable(false);
		btExtra.setFont(StdFont.Fnt14);
		btExtra.setActionCommand("bt_extra");
		btExtra.addActionListener(this);
		btExtra.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(1, 0, 1, 1, Color.GRAY), 
			BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		
		pnExtra.add(btNote, BorderLayout.CENTER);
		pnExtra.add(btExtra, BorderLayout.LINE_END);
		
		// put them together
		JPanel pn1 = new JPanel(new BorderLayout());
		pn1.add(pnInfo, BorderLayout.PAGE_START);
		
			pnItmOptEx = new JPanel();
			pnItmOptEx.setLayout(new CardLayout());
			
				JPanel _pn1a = new JPanel(new BorderLayout());
				_pn1a.add(scpOpt, BorderLayout.CENTER);
				_pn1a.add(pnExtra, BorderLayout.PAGE_END);
			
			pnItmOptEx.add(_pn1a, "pn_opt_edit");
			
				JPanel _pn1b = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
				
				lbItmNoEdit = new JLabel();
				lbItmNoEdit.setFont(StdFont.Fnt18);
				lbItmNoEdit.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
				
				_pn1b.add(lbItmNoEdit);
				
			pnItmOptEx.add(_pn1b, "pn_opt_no_edit");
		
		pn1.add(pnItmOptEx, BorderLayout.CENTER);
		
		return pn1;
	}
	
	private static JButton crCmdBt(String title, String cmd, ActionListener lst) {
		JButton bt1 = new JButton(title);
		bt1.setFocusable(false);
		bt1.setActionCommand(cmd);
		bt1.addActionListener(lst);
		bt1.setFont(StdFont.Fnt16);
		return bt1;
	}
	
	private void showPanel(OrderItemType odt1) {
		btHold.setVisible(OrderItemType.Item == odt1);
		btHold.setVisible(!noEdit);
		//
		CardLayout cl1 = (CardLayout)pnCt.getLayout();
		cl1.show(pnCt, odt1.toString());
	}
	
	private void updatePanelOpenItem() {
		btOpName.setText(String.format("<html>Item: <b>%s</b></html>", odi1.getItmNamePos()));
		btOpPrice.setText(String.format("<html>Price($): <b>%.2f</b></html>", odi1.getItmPrice()));
		lbOpTax.setText(String.format("<html>Qty: 1 &nbsp;Tax(%%): <b>%.2f</b></html>", odi1.getItmTax()));
		itemInfoUpdated();
	}
	
	private void updatePanelItem() {
		
		btNote.setText(String.format("Note: %s", odi1.getOdiNote()));
		btExtra.setText(String.format("Extra($): %.2f", odi1.getOdiExtra()));
		
		lbPrice.setText(String.format("<html>Price($): <b>%.2f</b></html>", odi1.getItmPrice()));
		lbTax.setText(String.format("<html>Tax(%%): <b>%.2f</b></html>", odi1.getItmTax()));

		btQty.setText(String.format("Qty: %d", odi1.getOdiQty()));
		lbItmNamePos.setText(odi1.getItmNamePos());
		lbAmtBf.setText(String.format("$%.2f", odi1.getOdiAmtBf()));

		pnOpt.removeAll();
		
		java.util.Vector<TxOpt> itmOpts = null;
		try {
			itmOpts = TxOpt.getOptsByItmId(odi1.getItmId());
		} catch (SQLException e) {
			e.printStackTrace();
			pnOpt.updateUI();
			return;
		}

		CardLayout cl1 = (CardLayout)pnItmOptEx.getLayout();
		if (noEdit) {
			//
			lbItmNoEdit.setText(odi1.getInfoHtml());
			cl1.show(pnItmOptEx, "pn_opt_no_edit");
			//
			pnOpt.updateUI();
			return;
		}
		
		if (null == itmOpts || itmOpts.size() <= 0) {
			pnOpt.updateUI();
			return;
		}
		
		cl1.show(pnItmOptEx, "pn_opt_edit");

		JPanel tmpPn1 = new JPanel();
		tmpPn1.setLayout(new BoxLayout(tmpPn1, BoxLayout.PAGE_AXIS));
		for (int i=0; i < itmOpts.size(); i++) {
			TxOpt opt1 = itmOpts.get(i);
			//
			Color bdColor = Color.RED;
			if (OptionType.Multiple == opt1.getOptType()) {
				bdColor = Color.GRAY;
			}
			JPanel pnOpt1 = new JPanel();
			pnOpt1.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(
							BorderFactory.createMatteBorder(2, 2, 2, 2, bdColor), 
							opt1.getOptNamePos(),
							TitledBorder.LEFT,
							TitledBorder.TOP,
							StdFont.Fnt16,
							bdColor),
					BorderFactory.createEmptyBorder(5, 5, 15, 15)
				));
				
			pnOpt1.setLayout(new GridLayout(0, DEF_OPT_COL, 2, 10));
			ButtonGroup bgr1 = null;
			if (OptionType.Single == opt1.getOptType()) {
				bgr1 = new ButtonGroup();
			}
			java.util.Vector<TxOptItem> opis = opt1.getOptItems();
			for (TxOptItem opi1 : opis) {
				if (OptionType.Single == opt1.getOptType()) {
					OptItemRadio rdb1 = new OptItemRadio(opt1, opi1, odi1.hasOpi(opi1), this);
					bgr1.add(rdb1);
					pnOpt1.add(rdb1);
				} else { // multiple
					OptItemBox cbx1 = new OptItemBox(opt1, opi1, odi1.hasOpi(opi1), this);
					pnOpt1.add(cbx1);
				}
			}
			pnOpt1.setAlignmentX(LEFT_ALIGNMENT);
			if (i > 0) {
				tmpPn1.add(Box.createVerticalStrut(5));
			}
			tmpPn1.add(pnOpt1);
		}
		//
		pnOpt.add(tmpPn1);
		pnOpt.updateUI();
		//
		itemInfoUpdated();
	}
	
	private void deleteItem() {
		odiIntf.deleteItem();
	}
	
	private void holdItem() {
		odiIntf.holdItem();
	}
	
	private void moveItemSeq(int stp) {
		odiIntf.moveItemSeq(stp);
	}
	
	// public
	
	public void showOrderItem(TbOrderItem _odi) {
		odi1 = _odi;
		noEdit = odi1.getOdiId() > 0 && !odi1.isHold();
		//
		if (OrderItemType.Item == odi1.getOdiType()) {
			updatePanelItem();
		}
		if (OrderItemType.OpenItem == odi1.getOdiType()) {
			updatePanelOpenItem();
		}
		if (OrderItemType.Line == odi1.getOdiType()) {
		}
		//
		showPanel(odi1.getOdiType());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj instanceof OptItemIntf) {
			OptItemIntf oit = (OptItemIntf)obj;
			odi1.updateOptionItem(oit.isSelected(), oit.getTxOpt(), oit.getTxOptItem());
			itemInfoUpdated();
			return;
		}
		String cmd = e.getActionCommand();
		if ("bt_up".equals(cmd)) {
			moveItemSeq(-1);
			return;
		}
		if ("bt_down".equals(cmd)) {
			moveItemSeq(1);
			return;
		}
		if ("bt_delete".equals(cmd)) {
			deleteItem();
			return;
		}
		if ("bt_hold".equals(cmd)) {
			if (noEdit) return;
			//
			holdItem();
			return;
		}
		if ("bt_note".equals(cmd)) {
			if (noEdit) return;
			//
			DlgInpText dlgTxt = new DlgInpText(odiIntf.getFrame());
			dlgTxt.setValue(odi1.getOdiNote());
			dlgTxt.showDialog(odiIntf.getFrame(), "Note?");
			if ("bt_ok".equals(dlgTxt.getUsrRsp())) {
				odi1.setOdiNote(dlgTxt.getValue());
				btNote.setText(String.format("Note: %s", odi1.getOdiNote()));
				itemInfoUpdated();
			}
			return;
		}
		if ("bt_extra".equals(cmd)) {
			if (noEdit) return;
			//
			DlgInpNum dlgNum = new DlgInpNum(odiIntf.getFrame());
			dlgNum.showDialog("Extra?", odi1.getOdiExtra());
			if ("bt_ok".equals(dlgNum.getUsrRsp())) {
				odi1.setOdiExtra(dlgNum.getDoubleValue());
				btExtra.setText(String.format("Extra($): %.2f", odi1.getOdiExtra()));
				itemInfoUpdated();
			}
			return;
		}		
		if ("bt_qty".equals(cmd)) {
			if (noEdit) return;
			//
			DlgInpNum dlgNum = new DlgInpNum(odiIntf.getFrame());
			dlgNum.showDialog("Qty?", odi1.getOdiQty());
			if ("bt_ok".equals(dlgNum.getUsrRsp())) {
				odi1.setOdiQty(dlgNum.getIntValue());
				btQty.setText(String.format("Qty: %d", odi1.getOdiQty()));
				itemInfoUpdated();
			}
			return;
		}
		if ("bt_op_name".equals(cmd)) {
			if (noEdit) return;
			//
			DlgInpText dlgTxt = new DlgInpText(odiIntf.getFrame());
			dlgTxt.setValue(odi1.getItmNamePos());
			dlgTxt.showDialog(odiIntf.getFrame(), "Open Item Description?");
			if ("bt_ok".equals(dlgTxt.getUsrRsp())) {
				odi1.setItmNamePos(dlgTxt.getValue());
				btOpName.setText(String.format("<html>Item: <b>%s</b></html>", odi1.getItmNamePos()));
				itemInfoUpdated();
			}
			return;
		}
		if ("bt_op_price".equals(cmd)) {
			if (noEdit) return;
			//
			DlgInpNum dlgNum = new DlgInpNum(odiIntf.getFrame());
			dlgNum.showDialog("Price?", odi1.getItmPrice());
			if ("bt_ok".equals(dlgNum.getUsrRsp())) {
				odi1.setItmPrice(dlgNum.getDoubleValue());
				btOpPrice.setText(String.format("<html>Price($): <b>%.2f</b></html>", odi1.getItmPrice()));
				itemInfoUpdated();
			}
			return;
		}		
	}
	
	private void itemInfoUpdated() {
		odiIntf.itemInfoUpdated();
		lbAmtBf.setText(String.format("$%.2f", odi1.getOdiAmtBf()));
	}
	

	public boolean hasValidOption() {
		try {
			return odi1.hasValidOption();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
}
