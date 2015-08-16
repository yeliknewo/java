package com.kileyowen.neuralnetwork;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.kileyowen.geneticalgorithm.Color;
import com.kileyowen.geneticalgorithm.Image;

public class Main {
	public static Random rand;

	public static double sigmoid(double input) {
		return 1d / (1 + Math.pow(Math.E, -input));
	}

	public static List<Double> fromImageToNeural(List<Integer> input) {
		List<Double> output = new ArrayList<Double>();
		for (int i = 0; i < input.size(); i++) {
			output.add((double) (input.get(i)) / 0xFFFFFF);
		}
		return output;
	}

	public static List<Integer> fromNeuralToImage(List<Double> input) {
		List<Integer> output = new ArrayList<Integer>();
		for (int i = 0; i < input.size(); i++) {
			output.add((int) Math.round(input.get(i) * 0xFFFFFF));
		}
		return output;
	}

	private static Double getFitness(List<Integer> working, List<Integer> goal) {
		double total = working.size(), sum = 0;
		for (int i = 0; i < working.size(); i++) {
			Color workingColor = new Color(working.get(i)), goalColor = new Color(goal.get(i));
			double colorSum = 0;
			colorSum += Math.abs(workingColor.getRed() - goalColor.getRed());
			colorSum += Math.abs(workingColor.getGreen() - goalColor.getGreen());
			colorSum += Math.abs(workingColor.getBlue() - goalColor.getBlue());
			sum += 1d / Math.max(colorSum, 1d);
		}
		return 1d / (total - sum);
	}

	private static void setAllWeights(List<NeuralNetwork> networks, List<List<Double>> weights) {
		for (int i = 0; i < networks.size(); i++) {
			networks.get(i).setAllNeuronWeights(weights.get(i));
		}
	}

	private static int findSpot(List<Double> fitnessList, double fitness) {
		for (int i = 0; i < fitnessList.size(); i++) {
			if (fitness > fitnessList.get(i)) {
				return i;
			}
		}
		return 0;
	}

	private static List<List<Double>> evolve(List<List<Double>> input, double swapRate, double mutateRate,
			int generationSize, double minWeight, double maxWeight) {
		List<List<Double>> output = new ArrayList<List<Double>>();
		for (int parentIndex1 = 0; parentIndex1 < input.size(); parentIndex1++) {
			for (int parentIndex2 = parentIndex1; parentIndex2 < input.size(); parentIndex2++) {
				List<Double> p1 = input.get(parentIndex1), p2 = input.get(parentIndex2), kid1 = new ArrayList<Double>(),
						kid2 = new ArrayList<Double>();
				for (int i = 0; i < p1.size(); i++) {
					if (Main.rand.nextDouble() < swapRate) {
						kid1.add(p2.get(i));
						kid2.add(p1.get(i));
					} else {
						kid1.add(p1.get(i));
						kid2.add(p2.get(i));
					}
					if (Main.rand.nextDouble() < mutateRate) {
						kid1.add(Main.rand.nextDouble() * (maxWeight - minWeight) + minWeight);
					}
					if (Main.rand.nextDouble() < mutateRate) {
						kid2.add(Main.rand.nextDouble() * (maxWeight - minWeight) + minWeight);
					}
				}
				output.add(kid1);
				output.add(kid2);
			}
		}
		return output;
	}

	public static void main(String[] args) {
		rand = new Random();

		Image goal = new Image("assets/in.png");
		List<Integer> imageGoal = goal.getAllPixelRGB();
		Image base = new Image(goal.getWidth(), goal.getHeight());
		String timestamp = Long.toString(new Date().getTime());

		int numInputs = goal.getWidth() * goal.getHeight(), numOutputs = numInputs, numLayers = 1, numNetworks = 50;
		double minWeight = -1, maxWeight = 1, swapRate = 0.7, mutateRate = 0.05;

		List<NeuralNetwork> networks = new ArrayList<NeuralNetwork>();
		while (networks.size() < numNetworks) {
			networks.add(new NeuralNetwork(numInputs, numOutputs, numLayers, minWeight, maxWeight));
		}

		List<Double> neuralInput = fromImageToNeural(base.getAllPixelRGB()), bestFired = null;
		double bestFitness = 0;

		for (int generationIndex = 0; true; generationIndex++) {
			List<List<Double>> gaInput = new ArrayList<List<Double>>();
			List<Double> fitnessList = new ArrayList<Double>();
			for (int networkIndex = 0; networkIndex < networks.size(); networkIndex++) {
				NeuralNetwork network = networks.get(networkIndex);
				List<Double> fired = network.fire(neuralInput);
				List<Integer> neuralOutput = fromNeuralToImage(fired);
				double fitness = getFitness(neuralOutput, imageGoal);
				int fitnessIndex = findSpot(fitnessList, fitness);
				if (fitnessIndex < numNetworks) {
					gaInput.add(fitnessIndex, network.getAllNeuronWeights());
					fitnessList.add(fitnessIndex, fitness);
					if (fitness > bestFitness) {
						bestFired = fired;
						bestFitness = fitness;
					}
				}
			}
			Image bestImage = new Image(goal.getWidth(), goal.getHeight());
			bestImage.setAllPixelRGB(fromNeuralToImage(bestFired));
			bestImage.writeFile("assets/" + timestamp + "/" + generationIndex + ".png");
			System.out.println(generationIndex + " " + bestFitness);
			List<List<Double>> gaOutput = evolve(gaInput, swapRate, mutateRate, numNetworks, minWeight, maxWeight);
			setAllWeights(networks, gaOutput);
		}
	}
}
