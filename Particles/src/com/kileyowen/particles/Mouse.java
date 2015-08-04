package com.kileyowen.particles;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener {
	private int x = -1;
	private int y = -1;
	private boolean left = false;
	private boolean right = false;
	private boolean mouseIn = false;
	
	public Mouse(Component leader){
		leader.addMouseListener(this);
		leader.addMouseMotionListener(this);
	}
	
	public boolean getMouseIn(){
		return mouseIn;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean getLeftButton() {
		return left;
	}

	public boolean getRightButton() {
		return right;
	}

	public void mouseDragged(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}

	public void mouseMoved(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {
		mouseIn = true;
	}

	public void mouseExited(MouseEvent e) {
		mouseIn = false;
	}

	public void mousePressed(MouseEvent e) {
		if (e.getButton() == 1) {
			left = true;
		}
		if (e.getButton() == 3) {
			right = true;
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == 1) {
			left = false;
		}
		if (e.getButton() == 3) {
			right = false;
		}
	}
}
