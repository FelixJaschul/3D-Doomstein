package src;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.MouseInfo;
import java.awt.Point;

import javax.swing.JFrame;

public class Camera implements KeyListener, MouseMotionListener {

	public double xPos, yPos, xDir, yDir, xPlane, yPlane;
	public boolean left, right, forward, back;

	public final double MOVE_SPEED = 0.06;
	public final double ROTATION_SPEED = 0.045;

	private int centerX, centerY;
	private JFrame gameWindow;
	private Robot robot;

	public Camera(double x, double y, double xd, double yd, double xp, double yp, int screenWidth, int screenHeight, JFrame gameWindow) {

		xPos = x;
		yPos = y;
		xDir = xd;
		yDir = yd;
		xPlane = xp;
		yPlane = yp;

		this.gameWindow = gameWindow;

		centerX = screenWidth / 2;
		centerY = screenHeight / 2;

		try { robot = new Robot(); }
		catch (AWTException e) { e.printStackTrace(); }

		Toolkit.getDefaultToolkit().getBestCursorSize(1, 1); // Hide cursor
	}

	public void keyPressed(KeyEvent key) {

		if (key.getKeyCode() == KeyEvent.VK_W) forward = true;
		if (key.getKeyCode() == KeyEvent.VK_S) back = true;
		if (key.getKeyCode() == KeyEvent.VK_A) left = true;
		if (key.getKeyCode() == KeyEvent.VK_D) right = true;
	}

	public void keyReleased(KeyEvent key) {

		if (key.getKeyCode() == KeyEvent.VK_W) forward = false;
		if (key.getKeyCode() == KeyEvent.VK_S) back = false;
		if (key.getKeyCode() == KeyEvent.VK_A) left = false;
		if (key.getKeyCode() == KeyEvent.VK_D) right = false;
	}

	public void update(int[][] map) {

		if (forward) {
			if (map[(int) (xPos + xDir * MOVE_SPEED)][(int) yPos] == 0) xPos += xDir * MOVE_SPEED;
			if (map[(int) xPos][(int) (yPos + yDir * MOVE_SPEED)] == 0) yPos += yDir * MOVE_SPEED;
		}

		if (back) {
			if (map[(int) (xPos - xDir * MOVE_SPEED)][(int) yPos] == 0) xPos -= xDir * MOVE_SPEED;
			if (map[(int) xPos][(int) (yPos - yDir * MOVE_SPEED)] == 0) yPos -= yDir * MOVE_SPEED;
		}

		if (right) {
			if (map[(int) (xPos + yDir * MOVE_SPEED)][(int) yPos] == 0) xPos += yDir * MOVE_SPEED;
			if (map[(int) xPos][(int) (yPos - xDir * MOVE_SPEED)] == 0) yPos -= xDir * MOVE_SPEED;
		}

		if (left) {
			if (map[(int) (xPos - yDir * MOVE_SPEED)][(int) yPos] == 0) xPos -= yDir * MOVE_SPEED;
			if (map[(int) xPos][(int) (yPos + xDir * MOVE_SPEED)] == 0) yPos += xDir * MOVE_SPEED;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {

		if (robot == null || gameWindow == null) return;

		Point windowPos = gameWindow.getLocationOnScreen();
		int dx = e.getX() - centerX;

		if (dx != 0) { // Only update if there's actual movement
			double rotationAmount = -dx * ROTATION_SPEED * 0.1;
			rotate(rotationAmount);
		}

		robot.mouseMove(centerX + windowPos.x, centerY + windowPos.y);
	}

	private void rotate(double angle) {

		double oldDirX = xDir;
		xDir = xDir * Math.cos(angle) - yDir * Math.sin(angle);
		yDir = oldDirX * Math.sin(angle) + yDir * Math.cos(angle);

		double oldPlaneX = xPlane;
		xPlane = xPlane * Math.cos(angle) - yPlane * Math.sin(angle);
		yPlane = oldPlaneX * Math.sin(angle) + yPlane * Math.cos(angle);
	}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
}
