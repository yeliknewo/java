package com.kileyowen.geneticalgorithm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {
	public static Random rand;
	private Image goal, best;
	private List<Image> currentGeneration;
	private double bestFitness;

	public GeneticAlgorithm(Image goal) {
		this.goal = goal;
		bestFitness = -1;
	}

	private void makeNextGeneration(double swapRate, double mutateRate, int generationSize) {
		if (currentGeneration == null) {
			currentGeneration = new ArrayList<Image>();
			while (currentGeneration.size() < generationSize) {
				Image base = new Image(goal.getWidth(), goal.getHeight());
				for (int y = 0; y < base.getHeight(); y++) {
					for (int x = 0; x < base.getWidth(); x++) {
						base.setPixel(x, y, new Color(rand.nextInt(0xFFFFFF)));
					}
				}
				currentGeneration.add(base);
			}
		}
		List<Image> nextGeneration = new ArrayList<Image>(), kidsPile = new ArrayList<Image>();
		double fitnessSum = 0;
		for (int parent1Index = 0; parent1Index < currentGeneration.size(); parent1Index++) {
			for (int parent2Index = parent1Index; parent2Index < currentGeneration.size(); parent2Index++) {
				List<Image> kids = breed(currentGeneration.get(parent1Index), currentGeneration.get(parent2Index),
						swapRate, mutateRate);
				for (int kidIndex = 0; kidIndex < kids.size(); kidIndex++) {
					Image kid = kids.get(kidIndex);
					kid.setFitness(getFitness(kid));
					if (kid.getFitness() > bestFitness) {
						best = kids.get(kidIndex);
						bestFitness = kid.getFitness();
					}
					kidsPile.add(kids.get(kidIndex));
				}
			}
		}
		kidsPile = quicksort(kidsPile);
		for (int sumIndex = 0; sumIndex < generationSize; sumIndex++) {
			fitnessSum += kidsPile.get(kidsPile.size() - 1 - sumIndex).getFitness();
		}
		for (int kidsPileIndex = 0; kidsPileIndex < generationSize
				&& nextGeneration.size() < generationSize; kidsPileIndex++) {
			Image kid = kidsPile.get(kidsPile.size() - 1 - kidsPileIndex);
			int count = (int) Math.round((kid.getFitness() / fitnessSum) * generationSize);
			for (int countIndex = 0; countIndex < count; countIndex++) {
				nextGeneration.add(kid);
			}
		}
		currentGeneration = nextGeneration;
	}

	private List<Image> breed(Image parent1, Image parent2, double swapRate, double mutateRate) {
		Image result1 = parent1.clone(), result2 = parent2.clone();

		for (int y = 0; y < parent1.getHeight(); y++) {
			for (int x = 0; x < parent1.getWidth(); x++) {
				if (Math.random() < swapRate) {
					result1.setPixel(x, y, parent2.getPixel(x, y));
					result2.setPixel(x, y, parent1.getPixel(x, y));
				}
				if (Math.random() < mutateRate) {
					result1.setPixel(x, y, new Color(rand.nextInt(0xFFFFFF)));
				}
				if (Math.random() < mutateRate) {
					result2.setPixel(x, y, new Color(rand.nextInt(0xFFFFFF)));
				}
			}
		}

		List<Image> resultList = new ArrayList<Image>();
		resultList.add(result1);
		resultList.add(result2);
		return resultList;
	}

	private double getFitness(Image image) {
		double sum = 0, total = image.getWidth() * image.getHeight();
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				double colorSum = 0;
				colorSum += Math.abs(image.getPixel(x, y).getRed() - goal.getPixel(x, y).getRed());
				colorSum += Math.abs(image.getPixel(x, y).getGreen() - goal.getPixel(x, y).getGreen());
				colorSum += Math.abs(image.getPixel(x, y).getBlue() - goal.getPixel(x, y).getBlue());
				sum += 1d / Math.max(colorSum, 1d);
			}
		}
		return 1d / (total - sum);
	}

	private List<Image> quicksort(List<Image> list) {
		if (list.size() <= 1) {
			return list;
		}
		int pivotIndex = rand.nextInt(list.size());
		List<Image> smaller = new ArrayList<Image>(), larger = new ArrayList<Image>();
		for (int listIndex = 0; listIndex < list.size(); listIndex++) {
			if (listIndex == pivotIndex) {
				continue;
			}
			if (list.get(pivotIndex).getFitness() < list.get(listIndex).getFitness()) {
				larger.add(list.get(listIndex));
			} else {
				smaller.add(list.get(listIndex));
			}
		}
		if (smaller.size() < larger.size()) {
			smaller.add(list.get(pivotIndex));
		} else {
			larger.add(list.get(pivotIndex));
		}
		smaller = quicksort(smaller);
		smaller.addAll(quicksort(larger));
		return smaller;
	}

	public static void main(String[] args) {
		rand = new Random();
		GeneticAlgorithm main = new GeneticAlgorithm(new Image("assets/hedgehog.jpeg"));
		String startTime = Long.toString(new Date().getTime());
		for (int generationIndex = 0; true; generationIndex++) {
			main.makeNextGeneration(0.7, 0.01, 50);
			System.out.println(generationIndex + " " + main.bestFitness);
			if (generationIndex % 100 == 0)
				main.best.writeFile("assets/" + startTime + "/out" + generationIndex + ".png");
		}
	}
}
