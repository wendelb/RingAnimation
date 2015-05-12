package bernhard.ringanimation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import bernhard.ringanimation.animation.Dot;

public class Init extends GameFrame {

	private static final long serialVersionUID = -6445501664185646223L;
	BufferedImage backgroundImage;

	Dot[] dots = new Dot[32];

	public static void main(String[] args) {
		Init mf = new Init();

		mf.setGameName("Ring Animation");
		mf.width = 512;
		mf.height = 512;
		mf.construct();
		mf.startGame();
	}

	@Override
	protected BufferedImage render() {
		BufferedImage r = new BufferedImage(width, height, 1);
		Graphics2D g = r.createGraphics();
		g.drawImage(backgroundImage, 0, 0, null);

		for (Dot d : dots) {
			d.render(g);
		}

		g.setComposite(BlendComposite.Add.derive(1f));
		g.drawImage(r, 0, 0, null);
		g.dispose();
		return r;
	}

	@Override
	protected void update() {
		for (Dot d : dots) {
			d.update();
		}
	}

	@Override
	protected void init() {

		backgroundImage = new BufferedImage(width, height, 1);

		Graphics2D g = backgroundImage.createGraphics();

		for (int i = 0; i < dots.length; i++) {
			dots[i] = new Dot(width / 2, height / 2, 360.0f / dots.length * i, (int) (i * Dot.movingDistance * 2 / 5.5));
		}

		g.setColor(Color.black);
		g.fillRect(0, 0, width, height);
		g.dispose();
	}

}
