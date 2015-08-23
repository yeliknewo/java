package com.kileyowen.geneticalgorithm;

import java.util.List;

public class NetworkData {
	private double fitness;
	private List<Double> weights;

	public NetworkData(List<Double> weights, double fitness) {
		this.fitness = fitness;
		this.weights = weights;
	}

	public NetworkData(List<Double> weights) {
		this.weights = weights;
		this.fitness = -1;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public List<Double> getWeights() {
		return weights;
	}
}
