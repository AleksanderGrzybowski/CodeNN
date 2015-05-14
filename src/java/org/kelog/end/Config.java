package org.kelog.end;

import java.io.File;

public class Config {
	public static final String TRAINING_DATA_DIRECTORY = "/home/kelog/Kodzenie/CodeNN/trdata";
	public static final String WORDS_FILENAME= TRAINING_DATA_DIRECTORY + File.separator + "words.txt";
	public static final String NETWORK_FILENAME = "/tmp/network.eg";
	public static final int MAXIMUM_EPOCHS = 10000;
	public static final double MAXIMUM_ERROR = 0.00001;
}
