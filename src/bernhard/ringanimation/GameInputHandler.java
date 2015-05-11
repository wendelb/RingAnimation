package bernhard.ringanimation;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GameInputHandler implements KeyListener, MouseMotionListener, MouseListener {

	public boolean[] inKey = new boolean[256];
	public boolean[] inMouse = new boolean[24];
	public int x = 0;
	public int y = 0;

	public boolean isKeyDown(int i) {
		return inKey[i];
	}

	public boolean isButtonDown(int i) {
		return inMouse[i];
	}

	@Override
	public void keyPressed(KeyEvent e) {
		inKey[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		inKey[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		x = e.getPoint().x;
		y = e.getPoint().y;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		x = e.getPoint().x;
		y = e.getPoint().y;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		inMouse[e.getButton()] = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		inMouse[e.getButton()] = false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public Point getMousePosition() {
		return MouseInfo.getPointerInfo().getLocation();
	}
}
