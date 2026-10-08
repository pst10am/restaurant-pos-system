package app_admin;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import resrc.ResData;
import model.TxSct;
import model.TxSctTable;

public class PanelFloorPlan extends JPanel implements MouseListener, IxFloor, ActionListener {
	private static final long serialVersionUID = 1L;
	
	private IxMain mainix;
	private CmpTable selTbl;
	private TxSct sctObj;
	private TxSctTable copyObj;
	
	private byte[] bgImgData = null;
	private Image bgImg = null;
	
	private boolean cmpEnable = true;
	
	private java.util.Vector<TxSctTable> arrDel;
	
	private JPopupMenu popMnu;
	
	// constructor

	PanelFloorPlan(IxMain _main) {
		super();

		mainix = _main;
		arrDel = new java.util.Vector<>();
		
		popMnu = new JPopupMenu();
		JMenuItem mitm1 = new JMenuItem("Paste");
		mitm1.setActionCommand("mnu_paste");
		mitm1.addActionListener(this);
		popMnu.add(mitm1);
		popMnu.pack();
		
		this.setLayout(null);
		this.setBackground(Color.decode("#99D6EB"));
		this.addMouseListener(this);
		this.setPreferredSize(new Dimension(1280, 1024));
	}
	
	// private
	
	private void _newTable(MouseEvent e) {
		TxSctTable tbl1 = TxSctTable.newObj();
		tbl1.setTblX((e.getX()/10)*10);
		tbl1.setTblY((e.getY()/10)*10);
		if (SysDialog.updateTxSectTable(mainix.getFrame(), tbl1)) {
			this.add(new CmpTable(tbl1, this));
			this.updateUI();
		}
	}
	
	private void _showPopup(MouseEvent e) {
		popMnu.show(this, e.getX(), e.getY());
	}
	
	//
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//g.setColor(this.getBackground());
		//g.fillRect(0, 0, this.getPreferredSize().width, this.getPreferredSize().height);
		if (null != bgImg) {
			g.drawImage(bgImg, 0, 0, null);
		}
		g.setColor(Color.BLACK);
		g.drawString(String.format("h[%d], w[%d]", 
			this.getPreferredSize().width, this.getPreferredSize().height)
			, 15, 25);
	}

	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!cmpEnable) return;
		//
		if (SwingUtilities.isLeftMouseButton(e)) {
			_newTable(e);
		} else if (SwingUtilities.isRightMouseButton(e)) {
			if (null != copyObj) {
				copyObj.setTblX(e.getX());
				copyObj.setTblY(e.getY());
				_showPopup(e);
			}
		}
	}

	@Override
	public void selectedTable(CmpTable cmpTbl) {
		if (null == selTbl || selTbl != cmpTbl) {
			if (null != selTbl) {
				selTbl.setSelected(false);
			}
			selTbl = cmpTbl;
			selTbl.setSelected(true);
		}
	}

	public void setSctObj(TxSct _obj) {
		arrDel.clear();
		sctObj = _obj;
		cmpEnable = null != sctObj;
		bgImg = null;
		bgImgData = null;
		selTbl = null;
		this.removeAll();
		if (null != sctObj) {
			Vector<TxSctTable> tables = TxSctTable.getTablesInSection(sctObj);
			if (null != tables && tables.size() > 0) {
				for (TxSctTable tbl1 : tables) {
					this.add(new CmpTable(tbl1, this));
				}
				this.updateUI();
			}
		}
		updateBgColor(null == sctObj ? Color.GRAY:sctObj.getSctBgColor());
		if (null == sctObj) {
			updateBgSize(1024, 768);
		} else {
			updateBgSize(sctObj.getSctWidth(), sctObj.getSctHeight());
		}
		updateBgPict(null == sctObj ? "":sctObj.getSctBgName());
	}
	
	//
	
	public void updateBgColor(Color bgcol) {
		this.setBackground(bgcol);
		this.updateUI();
		this.repaint();
	}
	
	public void updateBgSize(int _w, int _h) {
		this.setPreferredSize(new Dimension(_w, _h));
		this.updateUI();
	}
	
	public void updateBgPict(String pictName) {
		bgImg = null;
		bgImgData = null;
		if (null == pictName || pictName.isEmpty()) {
			this.updateUI();
			this.repaint();
			return;
		}
		try {
			bgImgData = ResData.findImage(pictName);
			if (null != bgImgData) {
				bgImg = new ImageIcon(bgImgData).getImage();
			}
		} catch (Exception e) {
			bgImgData = null;
			bgImg = null;
			e.printStackTrace();
		}
		this.updateUI();
		this.repaint();
	}
	
	public void updateBgPict(File bgFile) {
		bgImg = null;
		bgImgData = null;
		try (
			InputStream is = new FileInputStream(bgFile);
		) {
			bgImgData = new byte[(int)bgFile.length()];
			is.read(bgImgData, 0, bgImgData.length);
			bgImg = new ImageIcon(bgImgData).getImage();
		} catch (IOException e) {
			bgImgData = null;
			bgImg = null;
			e.printStackTrace();
		}
		this.updateUI();
		this.repaint();
	}

	public void save() throws SQLException {
		if (!cmpEnable) return;
		if (null == sctObj) return;
		//
		// background
		if (null != bgImgData || !sctObj.getSctBgName().isEmpty()) {
			ResData.saveTxPict(sctObj.getSctBgName(), bgImgData);
		}
		// tables
		Component[] comps = this.getComponents();
		for (Component cmp1 : comps) {
			if (cmp1 instanceof CmpTable) {
				CmpTable ctb = (CmpTable)cmp1;
				TxSctTable tbl1 = ctb.getTable();
				tbl1.setSctId(sctObj.getSctId());
				tbl1.save();
			}
		}
		// delete tables
		for (TxSctTable tbl1 : arrDel) {
			tbl1.delete();
		}
		arrDel.clear();
	}

	public void editTable() {
		if (!cmpEnable) return;
		if (null == selTbl) return;
		//
		TxSctTable tbl1 = selTbl.getTable();
		if (SysDialog.updateTxSectTable(mainix.getFrame(), tbl1)) {
			selTbl.update();
		}
	}

	public void deleteTable() {
		if (!cmpEnable) return;
		if (null == selTbl) return;
		//
		int usrRsp = JOptionPane.showConfirmDialog(mainix.getFrame(), 
			String.format("Delete Table [%s]?", selTbl.getTable().getTblName()));
		if (JOptionPane.YES_OPTION == usrRsp) {
			if (selTbl.getTable().getTblId() > 0) {
				arrDel.addElement(selTbl.getTable());
			}
			this.remove(selTbl);
			this.updateUI();
			selTbl = null;
		}
	}

	public void copyTable() {
		if (!cmpEnable) return;
		if (null == selTbl) return;
		//
		copyObj = TxSctTable.fromTable(selTbl.getTable());
	}
	
	private void _pasteTable() {
		if (!cmpEnable) return;
		if (null == copyObj) return;
		//
		if (SysDialog.updateTxSectTable(mainix.getFrame(), copyObj)) {
			this.add(new CmpTable(copyObj, this));
			this.updateUI();
		}
		copyObj = null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!cmpEnable) return;
		//
		String cmd = e.getActionCommand();
		if ("mnu_paste".equals(cmd)) {
			_pasteTable();
		}
	}
}


/*
	private class PnSectDetail extends JPanel {
		private static final long serialVersionUID = 1L;
		private Image bgImg;
		void setBgImg(PosImg _psimg) {
			bgImg = null;
			if (null != _psimg && null != _psimg.getImgData()) {
				bgImg = new ImageIcon(_psimg.getImgData()).getImage();
			}
			this.updateUI();
		}
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (null != bgImg) {
				g.drawImage(bgImg, 0, 0, null);
			}
		}
	}
 */


