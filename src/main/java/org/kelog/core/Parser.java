package org.kelog.core;

import com.google.common.base.Charsets;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Singleton
public class Parser {
	private CommentRemover commentRemover;
	private Words words;

	@Inject
	public Parser(CommentRemover commentRemover, Words words) {
		this.commentRemover = commentRemover;
		this.words = words;
	}

	public double[] histogram(String snippet) {
		double[] histogram = parse(snippet);

		return normalizedHistogram(histogram);
	}

	public double[] histogram(File file) {
		String snippet = readFromFile(file);
        snippet = commentRemover.removeComments(snippet);
        return histogram(snippet);
	}

	private String readFromFile(File file) {
		try {
			return new String(Files.readAllBytes(file.toPath()), Charsets.UTF_8);
		} catch (IOException e) {
			throw new AssertionError();
		}
	}

	private double[] parse(String snippet) {
		int index = 0;
		int length = snippet.length();
		double[] histogram = new double[words.list.size()];

		try {
			outer:
			while (index < length) {
				for (String guess : words.list) {
					if (snippet.startsWith(guess, index)) {
						index += guess.length();
						histogram[words.list.indexOf(guess)] += 1;
						continue outer;
					}
				}
				index++;
			}
		} catch (IndexOutOfBoundsException ignored) {
		}

		return histogram;
	}

	public double[] normalizedHistogram(double[] histogram) {
		double[] h = histogram.clone();
		double sum = 0.0;

		for (double val : h) {
			sum += val;
		}

		if (sum != 0) {
			for (int i = 0; i < h.length; ++i) {
				h[i] /= sum;
			}
		}

		return h;
	}
}
