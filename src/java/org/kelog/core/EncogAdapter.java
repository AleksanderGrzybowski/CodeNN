package org.kelog.core;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogDirectoryPersistence;

import java.io.File;
import java.util.logging.Logger;

/**
 * This library is fucking unusable without some abstraction over
 * all the complex shit involved in neural networks.
 * Let's make our lives easier.
 */
public class EncogAdapter {

	public double inputs[][];
	public double outputs[][];
	MLDataSet trainingSet;

	BasicNetwork network;

	private static Logger logger = Logger.getLogger(EncogAdapter.class.getName());

	public EncogAdapter() {
	}


	public void train(double[][] inputs, double[][] outputs, int hiddenLayerSize, double maximumError) {
		if (inputs.length != outputs.length) {
			throw new AssertionError();
		}
		logger.info("Training network, " + inputs.length + " vector(s)");

		this.inputs = inputs;
		this.outputs = outputs;
		trainingSet = new BasicMLDataSet(inputs, outputs);

		// create a neural network, without using a factory
		network = new BasicNetwork();
		network.addLayer(new BasicLayer(null, true, inputs[0].length));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, hiddenLayerSize));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), false, outputs[0].length));
		network.getStructure().finalizeStructure();
		network.reset();

		// train the neural network
		final ResilientPropagation train = new ResilientPropagation(network, trainingSet);

		int epoch = 1;

		do {
			train.iteration();
			if (epoch % 10 == 0) {
				logger.info("Epoch #" + epoch + " Error:" + train.getError());
			}
			epoch++;
		} while (train.getError() > maximumError);
		logger.info("Epoch #" + epoch + " -> finished.");
		train.finishTraining();

		Encog.getInstance().shutdown(); // is this needed
	}

	public double[] ask(double[] input) {
		MLData output = network.compute(new BasicMLData(input));
		return output.getData();
	}

	public void saveToFile(String fname) {
		logger.info("Saving network to file " + fname);
		EncogDirectoryPersistence.saveObject(new File(fname), network);
	}

	public void restoreFromFile(String fname) {
		logger.info("Restoring network from " + fname);
		network = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File(fname));
	}
}
