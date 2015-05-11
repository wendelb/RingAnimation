package bernhard.ringanimation.animation;

import java.awt.Color;
import java.awt.Graphics2D;

public class Dot {

	private float x, y, winkel;
	private int centerX, centerY;
	private int step;
	private Color color;
	private boolean directionTowardsCenter;

	public static final float centerDistance = 100f;

	public static final int dotRadius = 10;

	public static final float stepSize = 1.5f;
	public static final int stepCount = 50;

	public Dot(int centerX, int centerY, float winkel, int stepPadding) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.winkel = winkel;

		this.directionTowardsCenter = false;

		// Sägezahlfunktion, die sich alle 2 * stepCount wiederholt (auf, ab,
		// auf, ab)
		stepPadding = stepPadding % (stepCount * 2);

		if (stepPadding < stepCount) {
			this.step = stepPadding;
		}
		else {
			this.step = stepCount - (stepPadding - stepCount);
		}

		this.color = Color.LIGHT_GRAY;
		calcNewPosition();
	}

	private void calcNewPosition() {
		this.x = (float) (centerX - Math.cos(Math.toRadians(winkel)) * (centerDistance + step * stepSize));
		this.y = (float) (centerY - Math.sin(Math.toRadians(winkel)) * (centerDistance + step * stepSize));
	}

	public void render(Graphics2D g) {
		renderDot(g);
		renderSchweif(g);
	}

	private void renderDot(Graphics2D g) {
		g.setColor(this.color);
		g.fillOval((int) (x - (dotRadius / 2)), (int) (y - (dotRadius / 2)), dotRadius, dotRadius);
	}
	
	private void renderSchweif(Graphics2D g) {
		
	}
	
	public void update() {
		// Aktuellen Schritt berechnen
		if (this.directionTowardsCenter) {
			this.step++;

			if (this.step >= stepCount) {
				this.directionTowardsCenter = false;
			}
		}
		else {
			this.step--;

			if (this.step <= 0) {
				this.directionTowardsCenter = true;
			}
		}

		// Aus aktuellem Schritt die neue Position berechnen
		calcNewPosition();
	}

}
