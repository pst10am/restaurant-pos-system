package app_pos;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import model.CCData;

public class MSRGiftLst implements KeyListener {

	private StringBuilder bld1 = new StringBuilder();
	private String trk1 = "";

	private MSRCCIntf mintf;

	public MSRGiftLst(MSRCCIntf _mintf) {
		mintf = _mintf;
	}
	
	private boolean isTrack1Valid() {
		if (!trk1.startsWith(";")) {
			return false;
		}
		if (!trk1.endsWith("?")) {
			return false;
		}
		if (trk1.contains("=")) {
			return false;
		}
		return true;
	}
	
	private String getGiftNo() {
		return trk1.substring(1, trk1.length()-1);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		bld1.append(e.getKeyChar());
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getID() != KeyEvent.KEY_RELEASED) {
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (trk1.isEmpty()) {
				trk1 = bld1.toString().trim();
				//System.out.println("Track-1 ->\n"+ trk1);
				if (isTrack1Valid()) {
					CCData cdt1 = CCData.newCard();
					cdt1.setTrk1(getGiftNo());
					mintf.creditCardSwiped(cdt1);
				} else {
					mintf.cardReadError();
				}
				trk1 = "";
			}
			bld1.delete(0, bld1.length());
		}
	}
}
