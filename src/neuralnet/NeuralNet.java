package neuralnet;

public class NeuralNet {
    private Layer[] layers;

    public NeuralNet(double[][][] weights) {
        layers = new Layer[weights.length];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = new Layer(weights[i]);
        }
    }
}
