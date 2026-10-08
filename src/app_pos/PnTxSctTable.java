package app_pos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import resrc.ResData;
import model.TxSct;
import model.TxSctTable;

public class PnTxSctTable extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private ActionListener pLst;
	private Image bgImg;
	private TxSct curSct;
	
	public PnTxSctTable(ActionListener _lst) {
		super();
		this.setLayout(null);
		pLst = _lst;
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (null != bgImg) {
			g.drawImage(bgImg, 0, 0, null);
		}
		//g.setColor(this.getForeground());
		int w1 = this.getSize().width;
		int h1 = this.getSize().height;
		g.setColor(Color.BLACK);
		g.drawString(String.format("w=%d,h=%d", w1, h1), 10, h1-10);
	}
	
	private void _refresh() {
		bgImg = null;
		this.removeAll();
		if (null == curSct) {
			this.updateUI();
			this.repaint();
			return;
		}
		//
		this.setBackground(curSct.getSctBgColor());
		
		/*
		File bgf1 = new File(String.format("img/%s", curSct.getSctBgName()));
		if (bgf1.exists() && bgf1.isFile()) {
			try {
				bgImg = ImageIO.read(bgf1);
			} catch (IOException e) {
				bgImg = null;
				e.printStackTrace();
			}
		}
		*/
		
		try {
			byte[] bgImgData = ResData.findImage(curSct.getSctBgName());
			if (null != bgImgData) {
				bgImg = new ImageIcon(bgImgData).getImage();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		java.util.Vector<TxSctTable> tables = TxSctTable.getTablesInSection(curSct);
		for (TxSctTable tbl1 : tables) {
			this.add(ButtonTable.newTable(tbl1, pLst));
		}
		//
		this.updateUI();
		this.repaint();
	}
	
	public void showTables(TxSct sct1) {
		curSct = sct1;
		_refresh();
	}
	
	public void refresh() {
		_refresh();
	}
}
