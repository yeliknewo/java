package com.kileyowen.neuralnetwork;

import java.util.ArrayList;
import java.util.List;

public class Neuron {

	private int numInputs;
	private List<Double> weights;

	public Neuron(int numInputs, List<Double> weights) {
		this.numInputs = numInputs;
		this.weights = weights;

	}

	public Neuron(int numInputs, double minWeight, double maxWeight) {
		this.numInputs = numInputs;
		this.weights = new ArrayList<Double>();
		while (weights.size() < numInputs + 1) {
			this.weights.add(Main.rand.nextDouble() * (maxWeight - minWeight) + minWeight);
		}
	}

	public Double fire(List<Double> input) {
		if (input.size() != numInputs) {
			System.err.println("Invalid Input for Neuron");
			return null;
		}
		double activationSum = -1 * weights.get(weights.size() - 1);
		for (int i = 0; i < input.size(); i++) {
			activationSum += input.get(i) * weights.get(i);
		}
		return Main.sigmoid(activationSum);
	}

	public List<Double> getAllWeights() {
		return weights;
	}

	public int getWeightCount() {
		return weights.size();
	}

	public void setAllWeights(List<Double> subList) {
		for (int i = 0; i < weights.size(); i++) {
			weights.set(i, subList.get(i));
		}
	}
}
