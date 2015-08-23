package com.kileyowen.neuralnetwork;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.kileyowen.geneticalgorithm.Color;
import com.kileyowen.utilities.Image;

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

	private static int findSpot(List<Double> fitnessList, double fitness) {
		for (int i = 0; i < fitnessList.size(); i++) {
			if (fitness > fitnessList.get(i)) {
				return i;
			}
		}
		return 0;
	}

	private static void draw(List<Double> workingList, List<Double> fired, int imageWidth, int imageHeight) {
		int x0 = (int) Math.round(fired.get(0) * imageWidth), x1 = (int) Math.round(fired.get(2) * imageWidth),
				y0 = (int) Math.round(fired.get(1) * imageHeight), y1 = (int) Math.round(fired.get(3) * imageHeight);

		if (x0 == x1) {
			int yDiff = y1 - y0;
			for (int i = 0; i < yDiff; i++) {
				int index = (y0 + i) * imageWidth + x0;
				if (index >= workingList.size() || index < 0) {
					continue;
				}
				workingList.set(index, fired.get(4));
			}
		} else {
			if (x0 > x1) {
				int temp = x0;
				x0 = x1;
				x1 = temp;
				temp = y0;
				y0 = y1;
				y1 = temp;
			}
			int xDiff = x1 - x0, yDiff = y1 - y0;
			double slope = ((double) yDiff) / ((double) xDiff);
			for (int i = 0; i < xDiff; i++) {
				int index = (int) Math.round((y0 + i * slope) * imageWidth + x0 + i);
				if (index >= workingList.size() || index < 0) {
					continue;
				}
				workingList.set(index, fired.get(4));
			}
		}
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

		int numLinesDraw = 25;
		int numInputs = goal.getWidth() * goal.getHeight(), numOutputs = 5 * numLinesDraw, numLayers = 1,
				numNetworks = 10;
		double minWeight = -10, maxWeight = 10, swapRate = 0.7, mutateRate = 0.1;

		NeuralNetwork network = new NeuralNetwork(numInputs, numOutputs, numLayers, minWeight, maxWeight);
		List<List<Double>> networksData = new ArrayList<List<Double>>();
		while (networksData.size() < numNetworks) {
			networksData.add(network.getAllNeuronWeights());
		}

		List<Double> neuralInput = fromImageToNeural(base.getAllPixelRGB()), bestNetworkData = null;
		List<Integer> bestRGB = null;
		double bestFitness = 0;
		boolean shouldWrite = true;

		for (int generationIndex = 0; true; generationIndex++) {
			List<List<Double>> gaInput = new ArrayList<List<Double>>();
			List<Double> fitnessList = new ArrayList<Double>();
			for (int networkIndex = 0; networkIndex < networksData.size(); networkIndex++) {
				List<Double> networkData = networksData.get(networkIndex);
				network.setAllNeuronWeights(networkData);
				List<Double> workingList = neuralInput.subList(0, neuralInput.size());
				List<Double> fired = network.fire(workingList);
				for (int i = 0; i < numLinesDraw; i++) {
					draw(workingList, fired.subList(i * 5, (i + 1) * 5), goal.getWidth(), goal.getHeight());
				}
				List<Integer> neuralOutput = fromNeuralToImage(workingList);
				double fitness = getFitness(neuralOutput, imageGoal);
				int fitnessIndex = findSpot(fitnessList, fitness);
				if (fitnessIndex < numNetworks) {
					gaInput.add(fitnessIndex, network.getAllNeuronWeights());
					fitnessList.add(fitnessIndex, fitness);
					if (fitness > bestFitness) {
						bestNetworkData = networkData.subList(0, networkData.size());
						bestRGB = neuralOutput;
						bestFitness = fitness;
						shouldWrite = true;
					}
				}
			}
			while (fitnessList.size() > numNetworks) {
				int index = fitnessList.size() - 1;
				fitnessList.remove(index);
				gaInput.remove(index);
			}
			if (shouldWrite) {
				Image bestImage = new Image(goal.getWidth(), goal.getHeight());
				bestImage.setAllPixelRGB(bestRGB);
				bestImage.writeFile("assets/" + timestamp + "/" + generationIndex + ".png");
				shouldWrite = false;
			}
			System.out.println(generationIndex + " " + bestFitness);
			networksData = evolve(gaInput, swapRate, mutateRate, numNetworks, minWeight, maxWeight);
			networksData.add(bestNetworkData);
		}
	}
}
