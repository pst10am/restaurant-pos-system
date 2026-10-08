package app_pos;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import model.AddrUS;
import model.TbCust;
import model.TbOrder;
import model.TxArea;
import refx.OrderType;
import refx.ToGoType;
import resrc.ResUtil;
import resrc.StdFont;

public class PnOrdCust extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private static final String PN_DINEIN = OrderType.DineIn.toString();
	private static final String PN_TOGO = OrderType.ToGo.toString();
	
	private ButtonGroup grpToGoType;
	private ButtonToGo btDeli, btPickup, btWait;
	
	private JLabel lbName, lbPhone, lbEmail, lbNote, lbAddr, lbParty;
	private JPanel pnOrdType, pnToGo;
	
	private TbOrder ordObj = null;
	private OrdUpdIntf updIntf = null;
	
	// -----constructor-----
	
	public PnOrdCust(OrdUpdIntf _intf) {
		super(new BorderLayout());
		updIntf = _intf;
		initComponents();
	}
	
	// -----private-----
	
	private void initComponents() {

		this.add(crPnCustBasicInfo(), BorderLayout.PAGE_START);
		this.add(crPnOrdType(), BorderLayout.CENTER);
		
		this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	}
	
	private JPanel crPnCustBasicInfo() {
		JPanel pn1 = new JPanel();
		pn1.setLayout(new BoxLayout(pn1, BoxLayout.PAGE_AXIS));
		
		JButton btName = UIFactory.buttonB("Name", "bt_name", this);
		JButton btPhone = UIFactory.buttonB("Phone","bt_phone",this);
		JButton btEmail = UIFactory.buttonB("Email","bt_email",this);
		JButton btNote = UIFactory.buttonB("Note","bt_note",this);

		lbName = new JLabel("-");
		lbName.setFont(btName.getFont());
		
		lbPhone = new JLabel("-");
		lbPhone.setFont(btPhone.getFont());
		
		lbEmail = new JLabel("-");
		lbEmail.setFont(btEmail.getFont());
		
		lbNote = new JLabel("-");
		lbNote.setFont(btNote.getFont());
		
		pn1.add(UIFactory.panelA(btName, lbName));
		pn1.add(Box.createVerticalStrut(10));
		pn1.add(UIFactory.panelA(btPhone, lbPhone));
		pn1.add(Box.createVerticalStrut(10));
		pn1.add(UIFactory.panelA(btEmail, lbEmail));
		pn1.add(Box.createVerticalStrut(10));
		pn1.add(UIFactory.panelA(btNote, lbNote));
		pn1.add(Box.createVerticalStrut(10));
		pn1.add(new JSeparator());
		pn1.add(Box.createVerticalStrut(10));

		return pn1;
	}
	
	private JPanel crPnOrdType() {
		pnOrdType = new JPanel();
		pnOrdType.setLayout(new CardLayout());
		pnOrdType.add(crPnToGo(), PN_TOGO);
		pnOrdType.add(crPnDineIn(), PN_DINEIN);
		pnOrdType.setPreferredSize(new Dimension(pnOrdType.getPreferredSize().width, 215));
		return pnOrdType;
	}
	
	private JPanel crPnDineIn() {
		JPanel pn1 = new JPanel();
		pn1.setLayout(new BoxLayout(pn1, BoxLayout.PAGE_AXIS));
		
			JButton btParty = UIFactory.buttonB("Party Size", "bt_party_size", this);
			lbParty = new JLabel("0");
			lbParty.setFont(btParty.getFont());
		JPanel pnParty = UIFactory.panelA(btParty, lbParty);
		pnParty.setAlignmentX(LEFT_ALIGNMENT);
		pnParty.setMaximumSize(new Dimension(Short.MAX_VALUE, pnParty.getPreferredSize().height));
		
		pn1.add(pnParty);
		return pn1;
	}
	
	private JPanel crPnToGo() {
		JPanel pn1 = new JPanel(new BorderLayout());
		
			JPanel pnCmd = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
			
			btDeli = ButtonToGo.newButton("Delivery", "bt_deli", this);
			btPickup = ButtonToGo.newButton("PickUp", "bt_pickup", this);
			btWait = ButtonToGo.newButton("Waiting", "bt_wait", this); 

			grpToGoType = new ButtonGroup();
			grpToGoType.add(btDeli);
			grpToGoType.add(btPickup);
			grpToGoType.add(btWait);
			
			pnCmd.add(btDeli);
			pnCmd.add(btPickup);
			pnCmd.add(btWait);
		
		pn1.add(pnCmd, BorderLayout.PAGE_START);
		
			pnToGo = new JPanel();
			pnToGo.setLayout(new CardLayout());
			
			pnToGo.add(new JPanel(), "blank");
		
				JPanel pnAddr = new JPanel(new BorderLayout());
				
				lbAddr = new JLabel("Street Address");
				lbAddr.setFont(StdFont.Fnt18B);
				lbAddr.setBorder(BorderFactory.createEmptyBorder(20, 25, 0, 0));
				pnAddr.add(lbAddr, BorderLayout.PAGE_START);
				
				ButtonAddr btAddrEdit = ButtonAddr.newButton("Edit Address", "bt_addr_edit", this);
				ButtonAddr btAddrMap = ButtonAddr.newButton("Map", "bt_addr_map", this);
				ButtonAddr btAddrChk = ButtonAddr.newButton("Verify", "bt_addr_check", this);
				
				JPanel pnAddrCmd = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
				pnAddrCmd.add(btAddrEdit);
				pnAddrCmd.add(btAddrMap);
				pnAddrCmd.add(btAddrChk);
				
				pnAddr.add(pnAddrCmd, BorderLayout.PAGE_END);
			
			pnToGo.add(pnAddr, ToGoType.Delivery.toString());
	
				JLabel lbPk = new JLabel(ToGoType.PickUp.toString());
				lbPk.setFont(StdFont.Fnt20B);
				lbPk.setBorder(BorderFactory.createEmptyBorder(20, 25, 0, 0));
				JPanel pnPk = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
				pnPk.add(lbPk);
				
			pnToGo.add(pnPk, ToGoType.PickUp.toString());
	
				JLabel lbWt = new JLabel(ToGoType.Waiting.toString());
				lbWt.setFont(StdFont.Fnt20B);
				lbWt.setBorder(BorderFactory.createEmptyBorder(20, 25, 0, 0));
				JPanel pnWt = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
				pnWt.add(lbWt);
				
			pnToGo.add(pnWt, ToGoType.Waiting.toString());
			
		pn1.add(pnToGo, BorderLayout.CENTER);
		
		pn1.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
		return pn1;
	}
	
	private void _showPanel(String pname) {
		CardLayout cl1 = (CardLayout)pnOrdType.getLayout();
		cl1.show(pnOrdType, pname);
	}
	
	private void _updateCustomer(String cstFld) {
		if ("name".equals(cstFld)) {
			DlgInpText dlg1 = new DlgInpText(updIntf.getDialog());
			dlg1.setValue(ordObj.getCstName());
			dlg1.showDialog(updIntf.getDialog(), "Customer Name?");
			if ("bt_ok".equals(dlg1.getUsrRsp())) {
				ordObj.setCstName(dlg1.getValue());
				_updateInfo();
			}
		}
		if ("phone".equals(cstFld)) {
			DlgInpPhone dlg1 = new DlgInpPhone(updIntf.getDialog());
			dlg1.showDialog(updIntf.getDialog(), ordObj.getCstPhone());
			if ("bt_ok".equals(dlg1.getUsrRsp())) {
				ordObj.setCstPhone(dlg1.getPhoneValue());
				_updateInfo();
			}
		}
		if ("email".equals(cstFld)) {
			DlgInpText dlg1 = new DlgInpText(updIntf.getDialog());
			dlg1.setValue(ordObj.getCstEmail());
			dlg1.showDialog(updIntf.getDialog(), "Email?");
			if ("bt_ok".equals(dlg1.getUsrRsp())) {
				ordObj.setCstEmail(dlg1.getValue());
				_updateInfo();
			}
		}
		if ("note".equals(cstFld)) {
			DlgInpText dlg1 = new DlgInpText(updIntf.getDialog());
			dlg1.setValue(ordObj.getCstNote());
			dlg1.showDialog(updIntf.getDialog(), "Note (Buzzer Code, etc.)?");
			if ("bt_ok".equals(dlg1.getUsrRsp())) {
				ordObj.setCstNote(dlg1.getValue());
				_updateInfo();
			}
		}
	}
	
	private void _updateInfo() {
		if (OrderType.DineIn == ordObj.getOrdType()) {
			lbParty.setText(String.format("%d", ordObj.getPartySize()));
		} else if (OrderType.ToGo == ordObj.getOrdType()) {
			AddrUS addr1 = AddrUS.fromOrder(ordObj);
			if (addr1.isEmpty()) {
				lbAddr.setText("Street Address");
			} else {
				if (addr1.isValid()) {
					if (addr1.hasLatLon()) {
						TxArea chkArea = ResUtil.getDeliArea(addr1);
						if (null != chkArea) {
							lbAddr.setText(String.format(
								"<html>%s<br>Distance: <b>%.2f</b> mi. Delivery Fee: <b>$%.2f</b></html>", 
								addr1.toStringWithUnit(),
								ResUtil.getDistanceFromShop(addr1),
								chkArea.getAreaDeliFee()));
							//
							ordObj.updateDeliInfo(addr1);
							//
						} else {
							lbAddr.setText(String.format(
								"<html>%s<br><font color='red'><b>Out Of Service Area (%.2f mi.)</b></font></html>", 
								addr1.toStringWithUnit(),
								ResUtil.getDistanceFromShop(addr1)));
						}
					} else {
						lbAddr.setText(String.format(
							"<html>%s<br><font color='red'><b>Please verify address.</b></font></html>", 
							addr1.toStringWithUnit()));
					}
				} else {
					// invalid address
				}
			}
			CardLayout cl1 = (CardLayout)pnToGo.getLayout();
			String tgt = "blank";
			if (ToGoType.NA != ordObj.getOrdToGoType()) {
				tgt = ordObj.getOrdToGoType().toString();
			}
			cl1.show(pnToGo, tgt);
		}
		lbName.setText(ordObj.getCstName());
		lbPhone.setText(ResUtil.formatPhone(ordObj.getCstPhone()));
		lbEmail.setText(ordObj.getCstEmail());
		lbNote.setText(ordObj.getCstNote());
		//
		_showPanel(ordObj.getOrdType().toString());
		//
		updIntf.updateOrderInfo();
	}
	
	private void _editAddress() {
		System.out.println("_editAddress ... ");
		AddrUS addr1 = AddrUS.fromOrder(ordObj);
		DlgInpAddr dlg1 = new DlgInpAddr(updIntf.getDialog());
		dlg1.showDialog(addr1);
		if ("bt_ok".equals(dlg1.getUsrRsp())) {
			System.out.println("edit address ok ... ");
			AddrUS chkAddr = dlg1.getAddress();
			/*
			TxArea chkArea = dlg1.getArea();
			//
			if (null != chkArea) {
				lbAddr.setText(String.format(
					"<html>%s<br>Distance: <b>%.2f</b> mi. Delivery Fee: <b>$%.2f</b></html>", 
					chkAddr.toStringWithUnit(),
					ResUtil.getDistanceFromShop(chkAddr),
					chkArea.getAreaDeliFee()));
			} else {
				lbAddr.setText(String.format(
					"<html>%s<br><font color='red'><b>Out Of Service Area (%.2f mi.)</b></font></html>", 
					chkAddr.toStringWithUnit(),
					ResUtil.getDistanceFromShop(chkAddr)));
			}
			*/
			//
			ordObj.updateDeliInfo(chkAddr);
			_updateInfo();
		}
	}
	
	private void _showMap() {
		if (ordObj.getDeliAddr1().isEmpty()) {
			return;
		}
		AddrUS addr1 = AddrUS.fromOrder(ordObj);
		DlgMap dlg1 = new DlgMap(updIntf.getDialog());
		dlg1.showDialog(addr1);
	}
	
	private void _verifyAddress() {
		AddrUS chkAddr = AddrUS.fromOrder(ordObj);
		if (!chkAddr.isValid()) {
			return;
		}
		DlgChkAddr dlgChk = new DlgChkAddr(updIntf.getDialog());
		dlgChk.showDialog(chkAddr);
		if ("bt_ok".equals(dlgChk.getUsrRsp())) {
			ordObj.updateDeliInfo(chkAddr);
			_updateInfo();
		}
	}
	
	// -----public-----

	public void setOrder(TbOrder ord1) {
		ordObj = ord1;
		_updateInfo();
		//
		if (ToGoType.NA == ord1.getOrdToGoType()) {
			grpToGoType.clearSelection();
		} else {
			btDeli.setSelected(ToGoType.Delivery == ord1.getOrdToGoType());
			btPickup.setSelected(ToGoType.PickUp == ord1.getOrdToGoType());
			btWait.setSelected(ToGoType.Waiting == ord1.getOrdToGoType());
		}
	}
	
	public void setCustomer(TbCust cst1) {
		ordObj.updateCustomerInfo(cst1);
		_updateInfo();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ("bt_pickup".equals(cmd)) {
			ordObj.setOrdToGoType(ToGoType.PickUp);
			_updateInfo();
		} else if ("bt_wait".equals(cmd)) {
			ordObj.setOrdToGoType(ToGoType.Waiting);
			_updateInfo();
		} else if ("bt_deli".equals(cmd)) {
			ordObj.setOrdToGoType(ToGoType.Delivery);
			_updateInfo();
		} else if ("bt_name".equals(cmd)) {
			_updateCustomer("name");
		} else if ("bt_phone".equals(cmd)) {
			_updateCustomer("phone");
		} else if ("bt_email".equals(cmd)) {
			_updateCustomer("email");
		} else if ("bt_note".equals(cmd)) {
			_updateCustomer("note");
		} else if ("bt_addr_edit".equals(cmd)) {
			_editAddress();
		} else if ("bt_addr_map".equals(cmd)) {
			_showMap();
		} else if ("bt_addr_check".equals(cmd)) {
			_verifyAddress();
		} else if ("bt_party_size".equals(cmd)) {
			DlgInpNum dlg1 = new DlgInpNum(updIntf.getDialog());
			dlg1.showDialog("Party Size?", ordObj.getPartySize());
			if ("bt_ok".equals(dlg1.getUsrRsp())) {
				ordObj.setPartySize(dlg1.getIntValue());
				lbParty.setText(String.format("%d", ordObj.getPartySize()));
			}
		}
	}
}
