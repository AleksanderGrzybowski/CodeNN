package org.kelog.core;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;

import java.io.File;


public class EncogAdapter {

	private BasicNetwork network;

	@Inject
	public EncogAdapter(@Named("network-file") File file) {
		network = (BasicNetwork) EncogDirectoryPersistence.loadObject(file);
	}

	public double[] ask(double[] input) {
		return network.compute(new BasicMLData(input)).getData();
	}
}
