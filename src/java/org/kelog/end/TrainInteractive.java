package org.kelog.end;

import org.kelog.core.Trainer;

public class TrainInteractive {
	public static void main(String[] args) {

		String dirname, filename;

		if (args.length == 2) {
			dirname = args[0];
			filename = args[1];
		} else {
			dirname = "/home/kelog/Kodzenie/EncogAdapter/trdata";
			filename = "/tmp/network.eg";
		}

		Trainer.createNetwork(dirname, filename);

	}
}
