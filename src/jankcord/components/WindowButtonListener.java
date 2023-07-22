package jankcord.components;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import jankcord.Jankcord;

public class WindowButtonListener implements MouseListener {

	public static final int FRAME_CLOSE = 0;
	public static final int FRAME_FULLSCREEN = 1;
	public static final int FRAME_MINIMIZE = 2;
	
	private int perform;
	
	private Color hoverBack;
	private Color hoverFore;

	private Color clickBack;
	private Color clickFore;

	private Color exitBack;
	private Color exitFore;
	
	public WindowButtonListener(
			int perform,
			Color hoverBack,
			Color hoverFore,
			Color clickBack,
			Color clickFore,
			Color exitBack,
			Color exitFore) {
		this.perform = perform;
		
		this.hoverBack = hoverBack;
		this.hoverFore = hoverFore;

		this.clickBack = clickBack;
		this.clickFore = clickFore;

		this.exitBack = exitBack;
		this.exitFore = exitFore;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		e.getComponent().setBackground(clickBack);
		e.getComponent().setForeground(clickFore);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		e.getComponent().setBackground(hoverBack);
		e.getComponent().setForeground(hoverFore);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		e.getComponent().setBackground(exitBack);
		e.getComponent().setForeground(exitFore);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		switch (perform) {
			case FRAME_CLOSE -> System.exit(0);
			case FRAME_FULLSCREEN -> Jankcord.doFullscreen();
			case FRAME_MINIMIZE -> Jankcord.getFrame().setExtendedState(Frame.ICONIFIED);
		}
	}

}
