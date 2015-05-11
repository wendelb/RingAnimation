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
		BufferedImage r =  new BufferedImage(width, height, 1);
		Graphics2D g = r.createGraphics();
		g.drawImage(backgroundImage, 0, 0, null);
		
		// Show the Center
		g.setColor(Color.RED);
		g.fillOval(256-2, 256-2,4,4);
		
		for (Dot d : dots) {
			d.render(g);
		}
		
		//BufferedImage blured = Blur.boxBlur(r, 5);
		
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
		
/*		for (int i = 0; i < ps.length; i++) {
			double rand = Math.random();
			if (ps[i] == null) {
				if (rand < .25) {
					double r = Math.random() * 360;
					int x = (int) (Math.cos(Math.toRadians(r)) * 235 + 256);
					int y = (int) (Math.sin(Math.toRadians(r)) * 235 + 256);
					ps[i] = new Particle(x, y);
				}
			} else if (ps[i].dead && rand < .25) {
				double r = Math.random() * 360;
				int x = (int) (Math.cos(Math.toRadians(r)) * 235 + 256);
				int y = (int) (Math.sin(Math.toRadians(r)) * 235 + 256);
				ps[i] = new Particle(x, y);
			} else {
				ps[i].update();
			}
		} */
	}

	@Override
	protected void init() {

		backgroundImage = new BufferedImage(width, height, 1);
		
		Graphics2D g = backgroundImage.createGraphics();
		
		for (int i = 0; i < dots.length; i++) {
			dots[i] = new Dot(width/2, height/2, 360.0f/dots.length * i, i*30);
		}

		g.setColor(Color.black);
		g.fillRect(0, 0, width, height);
		g.dispose();
		
		/*
		for (int i = 0; i < ps.length * 0.75; i++) {
			double r = Math.random() * 360;
			int x = (int) (Math.cos(Math.toRadians(r)) * 235 + 256);
			int y = (int) (Math.sin(Math.toRadians(r)) * 235 + 256);
			ps[i] = new Particle(x, y);
		}
		*/
		
	}

}
