package neuralnet;

public class Layer {
    private Neuron[] neurons;

    public Layer(double[][] weights) {
        neurons = new Neuron[weights.length];
        for (int i = 0; i < neurons.length; i++) {
            neurons[i] = new Neuron(weights[i]);
        }
    }
}
