/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app_admin;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.sql.SQLException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;

import model.TxSct;

/**
 *
 * @author pphongsa
 */
public class PanelTxSct extends javax.swing.JPanel implements IxPanel, ActionListener {
	private static final long serialVersionUID = 1L;
	
	private Frame prFrm;

	public PanelTxSct(Frame _pr) {
		prFrm = _pr;
		initComponents();
	}

    private void initComponents() {//GEN-BEGIN:initComponents

        JLabel lb1 = new JLabel();
        txtSeq = new JTextField();
        JLabel lb2 = new JLabel();
        txtName = new JTextField();
        
        btBgColor = new JButton();
        btBgColor.setActionCommand("bt_bg_color");
        btBgColor.addActionListener(this);
        lbBgColor = new JLabel();
        lbBgColor.setOpaque(true);
        
        btBgPict = new JButton();
        btBgPict.setActionCommand("bt_bg_pict");
        btBgPict.addActionListener(this);
        
        lbBgPict = new JLabel();
        
        JLabel lb3 = new JLabel();
        txt_w = new JTextField();
        JLabel lb4 = new JLabel();
        txt_h = new JTextField();

        lb1.setText("Seq");

        txtSeq.setHorizontalAlignment(JTextField.RIGHT);
        txtSeq.setText("0");

        lb2.setText("Name");

        btBgColor.setText("BG Color");

        lbBgColor.setText("-Bg Color Code-");

        btBgPict.setText("BG Pict");

        lbBgPict.setText("-BG Pict File Name-");

        lb3.setText("Width");

        txt_w.setHorizontalAlignment(JTextField.RIGHT);
        txt_w.setText("0");

        lb4.setText("Height");

        txt_h.setHorizontalAlignment(JTextField.RIGHT);
        txt_h.setText("0");

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(lb2)
                    .addComponent(lb1))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lb3)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_w, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lb4)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_h, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtSeq, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtName, GroupLayout.PREFERRED_SIZE, 255, GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btBgPict, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btBgColor, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(lbBgColor)
                            .addComponent(lbBgPict))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lb1)
                    .addComponent(txtSeq, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lb2)
                    .addComponent(txtName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btBgColor)
                    .addComponent(lbBgColor))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btBgPict)
                    .addComponent(lbBgPict))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lb3)
                    .addComponent(txt_w, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb4)
                    .addComponent(txt_h, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        
        KeyListener klst = new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {
				int kcode = e.getKeyCode();
				if (KeyEvent.VK_ENTER == kcode) {
					updateBgSize();
				}
			}
        };
        
        txt_w.addKeyListener(klst);
        txt_h.addKeyListener(klst);
    }

    private JLabel lbBgColor;
    private JLabel lbBgPict;
    private JTextField txtName;
    private JTextField txtSeq;
    private JTextField txt_h;
    private JTextField txt_w;
    private JButton btBgColor, btBgPict;
    
    private TxSct sctObj;
    private PanelFloorPlan pnFlr;
    private File bgFile;
    
	@Override
	public JPanel getPanel() {
		return this;
	}

	@Override
	public boolean isDataValid() {
		return !txtName.getText().trim().isEmpty();
	}

	@Override
	public Object getObject() {
		return sctObj;
	}

	@Override
	public void setObject(Object obj) {
		sctObj = null != obj ? (TxSct)obj : null;
		//
		bgFile = null;
		txtName.setText(null == sctObj ? "":sctObj.getSctName());
		lbBgColor.setText(null == sctObj ? "":sctObj.getSctBgColor().toString());
		lbBgColor.setBackground(null == sctObj ? Color.GRAY:sctObj.getSctBgColor());
		lbBgPict.setText(null == sctObj ? "":sctObj.getSctBgName());
		txtSeq.setText(null == sctObj ? "0":String.format("%d", sctObj.getSctSeq()));
		txt_h.setText(null == sctObj ? "0":String.format("%d", sctObj.getSctHeight()));
		txt_w.setText(null == sctObj ? "0":String.format("%d", sctObj.getSctWidth()));
		//
		txtName.setEnabled(sctObj != null);
		txtSeq.setEnabled(sctObj != null);
		txt_h.setEnabled(sctObj != null);
		txt_w.setEnabled(sctObj != null);
		btBgColor.setEnabled(sctObj != null);
		btBgPict.setEnabled(sctObj != null);
		//
		if (null != pnFlr) {
			pnFlr.setSctObj(sctObj);
		}
	}
	
	void setFloorPlan(PanelFloorPlan _flr) {
		pnFlr = _flr;
	}
	
	void updateBgSize() {
		if (null == pnFlr) return;
		int w1 = Integer.parseInt(txt_w.getText());
		int h1 = Integer.parseInt(txt_h.getText());
		pnFlr.updateBgSize(w1, h1);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ("bt_bg_color".equals(cmd)) {
			Color scol = JColorChooser.showDialog(
				prFrm, "Background Color", lbBgColor.getBackground());
			if (null != scol) {
				lbBgColor.setText(scol.toString());
				lbBgColor.setBackground(scol);
				if (null != pnFlr) {
					pnFlr.updateBgColor(scol);
				}
			}
		} else if ("bt_bg_pict".equals(cmd)) {
			final JFileChooser fch1 = new JFileChooser();
			int retVal = fch1.showOpenDialog(prFrm);
			if (JFileChooser.APPROVE_OPTION == retVal) {
				bgFile = fch1.getSelectedFile();
				lbBgPict.setText(bgFile.getName());
				if (null != pnFlr) {
					pnFlr.updateBgPict(bgFile);
				}
			}
		}
	}

	public void saveObject() throws SQLException {
		if (null == sctObj) return;
		//
		sctObj.setSctName(txtName.getText());
		sctObj.setSctBgColor(lbBgColor.getBackground());
		sctObj.setSctBgName(lbBgPict.getText());
		sctObj.setSctSeq(Integer.parseInt(txtSeq.getText()));
		sctObj.setSctWidth(Integer.parseInt(txt_w.getText()));
		sctObj.setSctHeight(Integer.parseInt(txt_h.getText()));
		//
		sctObj.save();
		//
		if (null != pnFlr) {
			pnFlr.save();
		}
	}

	public void clearBg() {
		lbBgPict.setText("");
		pnFlr.updateBgPict("");
	}
}
