package org.kelog.end;

import org.kelog.core.Trainer;
import org.kelog.exceptions.EpochNumberExceeded;

import java.util.Scanner;

public class TrainInteractive {
	public static void main(String[] args) throws EpochNumberExceeded {
		Scanner scanner = new Scanner(System.in);
		String input;

		String trainingDir = Config.TRDATA_DIRECTORY;
		String networkFilename = Config.NETWORK_FILENAME;

		System.out.printf("Give path for training data (empty=default): ");
		input = scanner.nextLine();
		if (input.length() != 0) {
			trainingDir = input;
		}

		System.out.printf("Give path for network blob (empty=default): ");
		input = scanner.nextLine();
		if (input.length() != 0) {
			networkFilename = input;
		}

		Trainer.createNetwork(trainingDir, networkFilename);
	}
}
