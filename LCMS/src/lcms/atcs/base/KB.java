package lcms.atcs.base;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Hashtable;

public class KB extends KeyAdapter{
	public static Hashtable<String, Boolean> key;
	public static boolean keyDown;
	public KB(){
		key = new Hashtable<String, Boolean>();
	}
	@Override
	public void keyPressed(KeyEvent evt) {
		key.put(KeyEvent.getKeyText(evt.getKeyCode()), true);
		keyDown = true;
		evt.consume();
	}

	@Override
	public void keyReleased(KeyEvent evt) {	
		key.put(KeyEvent.getKeyText(evt.getKeyCode()), false);
		keyDown = false;
		evt.consume();
	}

	@Override
	public void keyTyped(KeyEvent evt) {		
	}

}
