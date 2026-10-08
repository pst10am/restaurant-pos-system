package app_admin;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import resrc.StdFont;
import model.TxSctTable;

public class CmpTable extends JLabel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	
	private int disx, disy;
	private TxSctTable tbl1;
	
	private IxFloor iflr;
	
	private boolean selFlg;
	
	// constructor
	
	CmpTable(TxSctTable _tbl1, IxFloor _flr) {
		super();
		
		iflr = _flr;
		tbl1 = _tbl1;
		
		this.setFont(StdFont.Fnt18);
		this.setOpaque(true);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setBackground(Color.GREEN);
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		
		this.setText(tbl1.getTblName());
		this.setBounds(tbl1.getTblX(), tbl1.getTblY(), tbl1.getTblW(), tbl1.getTblH());
		
		iflr.selectedTable(this);
	}
	
	// private
	
	private void updateBounds(int nx, int ny) {
		tbl1.setTblX((nx/10) * 10);
		tbl1.setTblY((ny/10) * 10);
		this.setBounds(tbl1.getTblX(), tbl1.getTblY(), tbl1.getTblW(), tbl1.getTblH());
	}
	
	/*
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Color bcol = selFlg ? Color.MAGENTA : Color.GRAY;
		this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, bcol));
	}
	*/
	
	// public
	
	void setSelected(boolean val) {
		selFlg = val;
		Color bcol = selFlg ? Color.MAGENTA : Color.GRAY;
		this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, bcol));
		this.updateUI();
	}

	@Override
	public void mouseMoved(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		int x1 = e.getXOnScreen();
		int y1 = e.getYOnScreen();
		int x2 = x1 - this.getParent().getLocationOnScreen().x;
		int y2 = y1 - this.getParent().getLocationOnScreen().y;
		updateBounds(x2-disx, y2-disy);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		iflr.selectedTable(this);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!selFlg) {
			iflr.selectedTable(this);
		}
		disx = e.getLocationOnScreen().x - this.getLocationOnScreen().x;
		disy = e.getLocationOnScreen().y - this.getLocationOnScreen().y;
		this.repaint();
	}
	
	public TxSctTable getTable() {
		return tbl1;
	}

	public void update() {
		this.setText(tbl1.getTblName());
		this.setBounds(tbl1.getTblX(), tbl1.getTblY(), tbl1.getTblW(), tbl1.getTblH());
		this.updateUI();
	}
}
