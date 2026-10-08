package app_pos;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import model.CCData;

public class MSRCCLst implements KeyListener {

	private StringBuilder bld1 = new StringBuilder();
	private String trk1 = "";
	private String trk2 = "";

	private MSRCCIntf mintf;

	public MSRCCLst(MSRCCIntf _mintf) {
		mintf = _mintf;
	}
	
	private boolean isTrack1Valid() {
		if (!trk1.startsWith("%")) {
			return false;
		}
		if (!trk1.endsWith("?")) {
			return false;
		}
		return true;
	}
	
	private boolean isTrack2Valid() {
		if (!trk2.startsWith(";")) {
			return false;
		}
		if (!trk2.endsWith("?")) {
			return false;
		}
		return true;
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
				System.out.println("Track-1 ->\n"+ trk1);
				if (!isTrack1Valid()) {
					trk1 = "";
				}
			} else {
				trk2 = bld1.toString().trim();
				System.out.println("Track-2 ->\n"+ trk2);
				if (isTrack2Valid()) {
					// Track 1 length = 79 (78+Enter)
					// Track 2 length = 40 (39+Enter)
					System.out.println("card swiped ok!");
					mintf.creditCardSwiped(CCData.newCard(trk1, trk2));
				} else {
					System.out.println("card swiped error!");
					mintf.cardReadError();
				}
				trk1 = "";
				trk2 = "";
			}
			bld1.delete(0, bld1.length());
		}
	}
}
