package refx;

import java.awt.Color;

public enum DlgType {
	
	Information (Color.decode("#3366FF"), Color.WHITE), 
	Warning (Color.YELLOW, Color.DARK_GRAY), 
	Critical (Color.decode("#E64016"), Color.WHITE);
	
	private final Color bgColor, fgColor;
	
	DlgType(Color _bg, Color _fg) {
		bgColor = _bg;
		fgColor = _fg;
	}
	
	public Color getBackground() {
		return bgColor;
	}
	
	public Color getForeground() {
		return fgColor;
	}
}
