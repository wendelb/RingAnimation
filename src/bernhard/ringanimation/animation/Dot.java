package bernhard.ringanimation.animation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class Dot {

	private float x, y, winkel;
	private int centerX, centerY;
	private int step;
	private float colorStep;
	private Color color;
	private boolean directionTowardsCenter;

	public static final float centerDistance = 100f;

	public static final int dotRadius = 10;

	// Movement
	public static final float stepSize = 1.5f;
	public static final int stepCount = 50;

	// Color
	public static final float colorStepSize = 0.01f;

	public Dot(int centerX, int centerY, float winkel, int stepPadding) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.winkel = winkel;

		this.directionTowardsCenter = false;

		// ColorStep zunächst am StepPadding festmachen
		colorStep = (stepPadding * colorStepSize) / 10;

		// Sägezahlfunktion, die sich alle 2 * stepCount wiederholt (auf, ab,
		// auf, ab)
		stepPadding = stepPadding % (stepCount * 2);

		if (stepPadding < stepCount) {
			this.step = stepPadding;
		}
		else {
			this.step = stepCount - (stepPadding - stepCount);
		}

		updateColor();
		calcNewPosition();
	}

	private void calcNewPosition() {
		this.x = (float) (centerX - Math.cos(Math.toRadians(winkel)) * (centerDistance + step * stepSize));
		this.y = (float) (centerY - Math.sin(Math.toRadians(winkel)) * (centerDistance + step * stepSize));
	}

	public void render(Graphics2D g) {
		renderSchweif(g);
		renderDot(g);
	}

	private void renderDot(Graphics2D g) {
		g.setColor(this.color);
		g.fillOval((int) (x - (dotRadius / 2)), (int) (y - (dotRadius / 2)), dotRadius, dotRadius);
	}

	private void renderSchweif(Graphics2D g) {
		g.setColor(this.color.darker().darker().darker().darker());

		Polygon p = new Polygon();

		// Der Punkt, der am weitesten weg ist
		if (directionTowardsCenter) {
			p.addPoint((int) (centerX - Math.cos(Math.toRadians(winkel)) * centerDistance), (int) (centerY - Math.sin(Math.toRadians(winkel)) * centerDistance));
		}
		else {
			p.addPoint((int) (centerX - Math.cos(Math.toRadians(winkel)) * (centerDistance + stepCount * stepSize)), (int) (centerY - Math.sin(Math.toRadians(winkel)) * (centerDistance + stepCount * stepSize)));
		}

		// Und 2 Punkte die am Dot hängen
		p.addPoint((int) (this.x + Math.sin(Math.toRadians(winkel)) * dotRadius / 2), (int) (this.y - Math.cos(Math.toRadians(winkel)) * dotRadius / 2));
		p.addPoint((int) (this.x - Math.sin(Math.toRadians(winkel)) * dotRadius / 2), (int) (this.y + Math.cos(Math.toRadians(winkel)) * dotRadius / 2));

		g.fillPolygon(p);
	}

	private void updateColor() {
		colorStep = colorStep + colorStepSize;
		if (colorStep > 1) {
			colorStep = colorStep - 1;
		}

		this.color = new Color(Color.HSBtoRGB(colorStep, 1.0f, 1.0f));
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

		// Zum Schluss noch eine neue Farbe berechnen
		updateColor();
	}

}
