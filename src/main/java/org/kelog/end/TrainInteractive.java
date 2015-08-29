package org.kelog.end;

import org.kelog.core.Trainer;

import java.util.Scanner;

public class TrainInteractive {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String input;

		String trainingDir = Config.TRAINING_DATA_DIRECTORY;
		String networkFilename = Config.NETWORK_FILENAME;

		System.out.printf("Give path for training data (empty=%s): ", trainingDir);
		input = scanner.nextLine();
		if (input.length() != 0) {
			trainingDir = input;
		}

		System.out.printf("Give path for network blob (empty=%s): ", networkFilename);
		input = scanner.nextLine();
		if (input.length() != 0) {
			networkFilename = input;
		}

		Trainer.createNetwork(trainingDir, networkFilename);
	}
}
