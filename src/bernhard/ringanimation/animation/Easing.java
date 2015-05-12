package bernhard.ringanimation.animation;

public class Easing {
	public static double easeOutExpo(double p) {
		return 1 - Math.pow(1 - p, 4 + 2);
	}
}
