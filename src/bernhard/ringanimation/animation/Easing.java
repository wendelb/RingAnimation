package bernhard.ringanimation.animation;

public class Easing {
	private static double easeExpo(double p) {
		return Math.pow(p, 4 + 2);
	}

	public static double easeInExpo(double p) {
		return easeExpo(p);
	}
	
	public static double easeOutExpo(double p) {
		return 1 - easeExpo(1 - p);
	}

	public static double easeInOutExpo(double p) {
		return p < 0.5 ? easeExpo(p * 2) / 2 : 1 - easeExpo(p * -2 + 2) / 2;
	}

}
