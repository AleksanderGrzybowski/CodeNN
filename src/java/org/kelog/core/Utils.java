package org.kelog.core;

public class Utils {
	public static void normalizeHistogram(double[] histogram) { // mutating
		double sum = 0;
		for (double val : histogram) {
			sum += val;
		}
		for (int i = 0; i < histogram.length; ++i) {
			histogram[i] /= sum;
		}
	}
}
