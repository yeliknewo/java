package com.kileyowen.neuralnetwork;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {
	private List<NeuralLayer> layers;

	public NeuralNetwork(int numInputs, int numOutputs, int numHiddenLayers, double minWeight, double maxWeight) {
		int hiddenLayerSize = numInputs;
		layers = new ArrayList<NeuralLayer>();
		while (layers.size() < numHiddenLayers) {
			hiddenLayerSize -= Math
					.round((float) (hiddenLayerSize - numOutputs) / (float) (numHiddenLayers - layers.size()));
			layers.add(new NeuralLayer(numInputs, hiddenLayerSize, minWeight, maxWeight));
			numInputs = hiddenLayerSize;
		}
	}

	public NeuralNetwork(List<NeuralLayer> layers) {
		this.layers = layers;
	}

	public List<Double> fire(List<Double> input) {
		for (int i = 0; i < layers.size(); i++) {
			input = layers.get(i).fire(input);
		}
		return input;
	}

	public List<Double> getAllNeuronWeights() {
		List<Double> weights = new ArrayList<Double>();
		for (int i = 0; i < layers.size(); i++) {
			weights.addAll(layers.get(i).getAllNeuronWeights());
		}
		return weights;
	}

	public void setAllNeuronWeights(List<Double> list) {
		int weightCount = 0;
		for (int i = 0; i < layers.size(); i++) {
			NeuralLayer layer = layers.get(i);
			int layerWeightCount = layer.getWeightCount();
			layer.setAllNeuronWeights(list.subList(weightCount, weightCount + layerWeightCount));
			weightCount += layerWeightCount;
		}
	}

	public int getWeightCount() {
		int weightCount = 0;
		for (int i = 0; i < layers.size(); i++) {
			weightCount += layers.get(i).getWeightCount();
		}
		return weightCount;
	}
}
