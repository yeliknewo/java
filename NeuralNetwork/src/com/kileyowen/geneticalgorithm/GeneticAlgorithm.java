package com.kileyowen.geneticalgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.kileyowen.neuralnetwork.NeuralNetwork;

public class GeneticAlgorithm {
	private Random rand;
	private List<NeuralNetwork> networks;
	private List<NetworkData> thisGen;
	private FitnessFunction fitFunc;
	private NetworkData bestData;

	public GeneticAlgorithm() {
		rand = new Random();
		System.out.println(rand.nextDouble() * (Double.MAX_VALUE - Double.MIN_VALUE) + Double.MIN_VALUE);
	}

	public GeneticAlgorithm setNetworks(List<NeuralNetwork> networks) {
		this.networks = networks;
		thisGen = new ArrayList<NetworkData>();
		for (int networkIndex = 0; networkIndex < thisGen.size(); networkIndex++) {
			NetworkData data = new NetworkData(networks.get(networkIndex).getAllNeuronWeights());
			thisGen.add(data);
		}
		bestData = thisGen.get(0);
		return this;
	}

	public GeneticAlgorithm setFitFunc(FitnessFunction fitFunc) {
		this.fitFunc = fitFunc;
		return this;
	}

	public GeneticAlgorithm evolve(List<Double> input, int genSize, double swapRate, double mutateRate) {
		List<NetworkData> fitFat = new ArrayList<NetworkData>();

		for (int p1Index = 0; p1Index < thisGen.size(); p1Index++) {
			for (int p2Index = p1Index; p2Index < thisGen.size(); p2Index++) {
				List<NetworkData> parents = new ArrayList<NetworkData>();
				parents.add(thisGen.get(p1Index));
				parents.add(thisGen.get(p2Index));
				List<NetworkData> kids = breed(parents, swapRate, mutateRate);
				for (int kidIndex = 0; kidIndex < kids.size(); kidIndex++) {
					NetworkData kid = kids.get(kidIndex);
					fitFunc.updateFitness(kid);
					fitFat.add(kid);
				}
			}
		}

		List<NetworkData> sorted = quicksort(fitFat);

		bestData = sorted.get(0);

		thisGen.clear();

		int kidScew = Math.max(1, (int) (Math.ceil(genSize / 10f)));

		for (int sortedIndex = 0; sortedIndex < genSize; sortedIndex++) {
			for (int scewIndex = 0; scewIndex < kidScew; scewIndex++) {
				thisGen.add(sorted.get(sortedIndex));
			}
			kidScew = Math.max(kidScew - 1, 1);
		}

		return this;
	}

	public NeuralNetwork getBestNetwork() {
		NeuralNetwork bestNet = networks.get(0).cloneWeightless();
		bestNet.setAllNeuronWeights(bestData.getWeights());
		return bestNet;
	}

	private List<NetworkData> quicksort(List<NetworkData> input) {
		if (input.size() <= 1) {
			return input;
		}
		int pivotIndex = rand.nextInt(input.size());
		List<NetworkData> smaller = new ArrayList<NetworkData>(), larger = new ArrayList<NetworkData>();
		for (int listIndex = 0; listIndex < input.size(); listIndex++) {
			if (listIndex == pivotIndex) {
				continue;
			}
			if (input.get(pivotIndex).getFitness() < input.get(listIndex).getFitness()) {
				larger.add(input.get(listIndex));
			} else {
				smaller.add(input.get(listIndex));
			}
		}
		if (smaller.size() < larger.size()) {
			smaller.add(input.get(pivotIndex));
		} else {
			larger.add(input.get(pivotIndex));
		}
		smaller = quicksort(smaller);
		smaller.addAll(quicksort(larger));
		return smaller;
	}

	private List<NetworkData> breed(List<NetworkData> parents, double swapRate, double mutateRate) {
		List<NetworkData> kids = new ArrayList<NetworkData>();

		for (int parentIndex = 0; parentIndex < parents.size(); parentIndex++) {
			List<Double> kidWeights = new ArrayList<Double>();
			NetworkData kid = new NetworkData(kidWeights, -1);
			NetworkData parent = parents.get(parentIndex);
			NetworkData swapParent = parents.get(parentIndex + 1 < parents.size() ? parentIndex + 1 : 0);
			for (int weightIndex = 0; weightIndex < parent.getWeights().size(); weightIndex++) {
				double d = rand.nextDouble();
				if (d < mutateRate) {
					kidWeights.add(rand.nextDouble() * (Double.MAX_VALUE - Double.MIN_VALUE) + Double.MIN_VALUE);
				} else if (d < swapRate) {
					kidWeights.add(swapParent.getWeights().get(weightIndex));
				} else {
					kidWeights.add(parent.getWeights().get(weightIndex));
				}
			}
			kids.add(kid);
		}

		return kids;
	}
}
