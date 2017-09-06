package org.kelog.core;

import lombok.RequiredArgsConstructor;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;

@RequiredArgsConstructor
class EncogAdapter {
    
    private final BasicNetwork network;
    
    double[] ask(double[] input) {
        return network.compute(new BasicMLData(input)).getData();
    }
}
