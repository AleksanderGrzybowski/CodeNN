package org.kelog.core;

public class Utils {
	public static double[] normalizedHistogram(double[] histogram) {
		double[] h = histogram.clone();
		double sum = 0.0;

		for (double val : h) {
			sum += val;
		}
		if (sum == 0) {
			return h;
		}
		for (int i = 0; i < h.length; ++i) {
			h[i] /= sum;
		}
		return h;
	}
}
