package bernhard.ringanimation.animation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class Dot {

	private float x, y, winkel;
	private int centerX, centerY;
	private float colorStep;
	private Color color;
	private long lastTime;

	/**
	 * Minimaler Abstand zum Mittelpunkt
	 */
	public static final float centerDistance = 100f;

	/**
	 * Radius des einzelnen Puntktes
	 */
	public static final int dotRadius = 8;

	// Movement
	/**
	 * Bewegungsweite (centerDistance + movingDistance = Maximaler Abstand zum
	 * Mittelpunkt)
	 */
	public static final int movingDistance = 100; // px (wie weit bewegt sich
													// der Punkt)

	/**
	 * Bewegungsgeschwindigkeit der Animation auf linearbasis Einheit:
	 * Linearpixel pro Millisekunde Der aktuelle Wert sorgt dafür, dass die
	 * komplette Animation (raus/rein) in 2 Sekunden abläuft
	 */
	public static final double animationSpeed = movingDistance * 2.0 / 1000.0 / 2.0;

	/**
	 * Aktueller Schritt, Einheit px; Maximaler Wert: movingDistance; Minimaler
	 * Wert: 0.0 Verläuft linear, die Werte x und y verlaufen dagegen so, wie es
	 * die Easing-Funktion verlangt
	 */
	private double step;

	/**
	 * Bewegt sich der Punkt gerade in Richtung Mittelpunkt?
	 */
	private boolean directionTowardsCenter;

	// Color Management
	/**
	 * Schrittweite, wie viel Grad sich die Farbe auf dem HUE bewegen soll
	 */
	public static final float colorStepSize = 0.005f;

	/**
	 * 
	 * @param centerX
	 *            Position des Mittelpunktes
	 * @param centerY
	 *            Position des Mittelpunktes
	 * @param winkel
	 *            Drehwinkel der Animation in Grad
	 * @param stepPadding
	 *            Um wie viel soll sich der Punkt beim Start bereits bewegt
	 *            haben?
	 */
	public Dot(int centerX, int centerY, float winkel, double stepPadding) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.winkel = winkel;

		this.directionTowardsCenter = false;

		// ColorStep zunächst am StepPadding festmachen
		colorStep = winkel / 360 * 3;

		// Sägezahlfunktion, die sich alle 2 * stepCount wiederholt (auf, ab,
		// auf, ab)
		while (stepPadding > movingDistance * 2) {
			// mod-Funktion für Floats
			stepPadding -= movingDistance * 2;
		}

		if (stepPadding < movingDistance) {
			this.step = stepPadding;
		}
		else {
			this.step = movingDistance - (stepPadding - movingDistance);
		}

		updateColor();
		calcNewPosition();

		lastTime = System.nanoTime();
	}

	private void calcNewPosition() {

		double p = step / (double) movingDistance;
		double factor;

		if (!this.directionTowardsCenter) {
			// Nach außen
			p = 1 - p;
			factor = (centerDistance + movingDistance - Easing.easeOutExpo(p) * movingDistance);
		}
		else {
			// Nach innen
			// Achtung: Hier gibt es einen anderen Ansatzpunkt
			factor = (centerDistance + Easing.easeOutExpo(p) * movingDistance);

		}

		this.x = (float) (centerX - Math.cos(Math.toRadians(winkel)) * factor);
		this.y = (float) (centerY - Math.sin(Math.toRadians(winkel)) * factor);
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

		if (directionTowardsCenter) {
			// Ausgehend vom Punkt der am nächsten dran ist hin zum anderen Ende
			double factor = centerDistance + Easing.easeInOutExpo(this.step / movingDistance) * movingDistance;
			p.addPoint((int) (centerX - Math.cos(Math.toRadians(winkel)) * factor), (int) (centerY - Math.sin(Math.toRadians(winkel)) * factor));
		}
		else {
			// Ausgehend vom Punkt, der am weitesten weg ist, nach innen
			double factor = (centerDistance + movingDistance) - Easing.easeInOutExpo((movingDistance - this.step) / movingDistance) * movingDistance;
			p.addPoint((int) (centerX - Math.cos(Math.toRadians(winkel)) * factor), (int) (centerY - Math.sin(Math.toRadians(winkel)) * factor));
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

	/**
	 * Berechnung des neuen Steps und anschließender Berechnung der neuen
	 * X/Y-Position. Dazu die nächste Farbe
	 */
	public void update() {
		long currTime = System.nanoTime();

		// Zeitliche Differenz zwischen 2 Update-Läufen in ms
		double diff = (currTime - lastTime) / 1000000.0;

		lastTime = currTime;

		if (this.directionTowardsCenter) {
			this.step += diff * animationSpeed;

			if (this.step >= movingDistance) {
				this.directionTowardsCenter = false;
			}
		}
		else {
			this.step -= diff * animationSpeed;

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
