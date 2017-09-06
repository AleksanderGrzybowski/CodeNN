package org.kelog.core;

import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;

public class EncogAdapter {
    
    private BasicNetwork network;
    
    public EncogAdapter(BasicNetwork network) {
        this.network = network;
    }
    
    public double[] ask(double[] input) {
        return network.compute(new BasicMLData(input)).getData();
    }
}
