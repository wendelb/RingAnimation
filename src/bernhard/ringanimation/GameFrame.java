package bernhard.ringanimation;

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public abstract class GameFrame extends JFrame implements Runnable {
	
	private static final long serialVersionUID = 4731991501451359207L;

	private static double FramesPerSecound = 60;
	private static double TicksPerSecound = 60;
	public static double AspectRatio = 16d / 9d;

	public int width = 1280;
	public int height = 720;

	private String Title = "";
	public static Thread GameThread;

	public static GameInputHandler GameInputHandler = new GameInputHandler();

	private Canvas c;
	private BufferStrategy bs;
	
	private Font font = new Font("Consolas", Font.PLAIN, 12);

	public void construct() {

		c = new Canvas();
		c.setSize(width, height);
		c.addKeyListener(GameInputHandler);
		c.addMouseListener(GameInputHandler);
		c.addMouseMotionListener(GameInputHandler);
		add(c);

		setTitle(Title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		pack();

		GameThread = new Thread(this);

		init();
		
	}

	public void startGame() {
		if (GameThread == null) {
			return;
		}
		if (GameThread.isInterrupted()) {
			return;
		}
		if (GameThread.isAlive()) {
			return;
		}
		GameThread.start();
	}

	@Override
	public void run() {

		setVisible(true);
		
		if (bs == null) {
			c.createBufferStrategy(2);
			bs = c.getBufferStrategy();
		}
		
		long initialTime = System.nanoTime();
		final double timeU = 1000000000 / TicksPerSecound;
		final double timeR = 1000000000 / FramesPerSecound;
		double deltaU = 0, deltaR = 0;
		int frames = 0, ticks = 0;
		long timer = System.currentTimeMillis();

		while (!Thread.interrupted()) {

			long currentTime = System.nanoTime();
			long passedTime = currentTime - initialTime;
			deltaU += passedTime / timeU;
			deltaR += passedTime / timeR;
			initialTime = currentTime;

			if (deltaU >= 1) {
				update();
				ticks++;
				deltaU--;
			}

			if (deltaR >= 1) {
				drawOnScreen(render());
				frames++;
				deltaR--;
			}

			if (System.currentTimeMillis() - timer > 1000) {
				setTitle(String.format("%s - UPS: %s, FPS: %s", Title, ticks, frames));
				frames = 0;
				ticks = 0;
				timer += 1000;
			}
		}
	}

	private void drawOnScreen(BufferedImage i) {

		Graphics2D g = (Graphics2D) bs.getDrawGraphics();

		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

		g.drawImage(i, 0, 0, width, height, null);

		g.dispose();

		bs.show();

	}

	public Font getFont() {
		return font;
	}
	
	public void setGlobalRate(double rate) {
		FramesPerSecound = rate;
		TicksPerSecound = rate;
	}
	
	public void setFPS(double fps) {
		FramesPerSecound = fps;
	}
	
	public void setUPS(double ups) {
		TicksPerSecound = ups;
	}
	
	public void setGameName(String Title) {
		this.Title = Title;
	}
	
	public Canvas getDrawingCanvas(){
		return c;
	}

	protected abstract BufferedImage render();

	protected abstract void update();
	
	protected abstract void init();

}
