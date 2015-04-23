package org.kelog.core;

public class Utils {
	public static void normalizeHistogram(double[] histogram) { // mutating
		double sum = 0.0;
		for (double val : histogram) {
			sum += val;
		}
		if (sum == 0) {
			return; // would create NaN's
		}
		for (int i = 0; i < histogram.length; ++i) {
			histogram[i] /= sum;
		}
	}
}
