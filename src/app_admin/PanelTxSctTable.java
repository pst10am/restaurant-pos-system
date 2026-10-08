/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app_admin;

import javax.swing.JPanel;
import model.TxSctTable;

/**
 *
 * @author pphongsa
 */
public class PanelTxSctTable extends javax.swing.JPanel implements IxPanel {
	private static final long serialVersionUID = 1L;

	public PanelTxSctTable() {
		initComponents();
	}

    private void initComponents() {

        javax.swing.JLabel lbname = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        javax.swing.JPanel pnSize = new javax.swing.JPanel();
        txt_h = new javax.swing.JTextField();
        javax.swing.JLabel lbw = new javax.swing.JLabel();
        javax.swing.JLabel lbh = new javax.swing.JLabel();
        txt_w = new javax.swing.JTextField();
        javax.swing.JPanel pnLoc = new javax.swing.JPanel();
        javax.swing.JLabel lbx = new javax.swing.JLabel();
        txt_x = new javax.swing.JTextField();
        txt_y = new javax.swing.JTextField();
        javax.swing.JLabel lby = new javax.swing.JLabel();

        lbname.setText("Table Name");

        pnSize.setBorder(javax.swing.BorderFactory.createTitledBorder("Size"));

        txt_h.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        txt_h.setText("0");

        lbw.setText("Height");

        lbh.setText("Width");

        txt_w.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        txt_w.setText("0");

        javax.swing.GroupLayout pnSizeLayout = new javax.swing.GroupLayout(pnSize);
        pnSize.setLayout(pnSizeLayout);
        pnSizeLayout.setHorizontalGroup(
            pnSizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnSizeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnSizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_w, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbh))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnSizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbw)
                    .addComponent(txt_h, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        pnSizeLayout.setVerticalGroup(
            pnSizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnSizeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnSizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbh)
                    .addComponent(lbw))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnSizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_w, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_h, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnLoc.setBorder(javax.swing.BorderFactory.createTitledBorder("Location"));

        lbx.setText("X-axis");

        txt_x.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        txt_x.setText("0");

        txt_y.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        txt_y.setText("0");

        lby.setText("Y-axis");

        javax.swing.GroupLayout pnLocLayout = new javax.swing.GroupLayout(pnLoc);
        pnLoc.setLayout(pnLocLayout);
        pnLocLayout.setHorizontalGroup(
            pnLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnLocLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_x, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbx))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lby)
                    .addComponent(txt_y, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnLocLayout.setVerticalGroup(
            pnLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnLocLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbx)
                    .addComponent(lby))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_x, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_y, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbname)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnLoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbname)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnLoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }

    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txt_h;
    private javax.swing.JTextField txt_w;
    private javax.swing.JTextField txt_x;
    private javax.swing.JTextField txt_y;
    
    private TxSctTable tbl1;
    
	@Override
	public JPanel getPanel() {
		return this;
	}

	@Override
	public boolean isDataValid() {
		if (txtName.getText().trim().isEmpty()) return false;
		//
		tbl1.setTblName(txtName.getText());
		tbl1.setTblW(Integer.parseInt(txt_w.getText()));
		tbl1.setTblH(Integer.parseInt(txt_h.getText()));
		tbl1.setTblX(Integer.parseInt(txt_x.getText()));
		tbl1.setTblY(Integer.parseInt(txt_y.getText()));
		//
		return true;
	}

	@Override
	public Object getObject() {
		return null;
	}

	@Override
	public void setObject(Object obj) {
		tbl1 = (TxSctTable)obj; 
		txtName.setText(tbl1.getTblName());
		txt_w.setText(String.format("%d", tbl1.getTblW()));
		txt_h.setText(String.format("%d", tbl1.getTblH()));
		txt_x.setText(String.format("%d", tbl1.getTblX()));
		txt_y.setText(String.format("%d", tbl1.getTblY()));
	}
}
