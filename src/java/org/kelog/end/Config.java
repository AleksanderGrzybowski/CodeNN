package org.kelog.end;

import java.io.File;

public class Config {
	public static final String TRDATA_DIRECTORY = "/home/kelog/Kodzenie/CodeNN/trdata";
	public static final String WORDS_FILENAME= TRDATA_DIRECTORY + File.separator + "words.txt";
	public static final String NETWORK_FILENAME = "/tmp/network.eg";
	public static final int MAXIMUM_EPOCHS = 10000;
}
