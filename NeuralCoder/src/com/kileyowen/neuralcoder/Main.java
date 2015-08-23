package com.kileyowen.neuralcoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.kileyowen.geneticalgorithm.GeneticAlgorithm;
import com.kileyowen.neuralnetwork.NeuralNetwork;
import com.kileyowen.neuralnetwork.NeuralRecorder;

public class Main {
	private static String networkName;

	public static void main(String[] args) {
		new GeneticAlgorithm();
		Random rand = new Random();
		System.out.println("Enter Network Name");
		Scanner in = new Scanner(System.in);
		networkName = in.nextLine();
		NeuralRecorder recorder = new NeuralRecorder("data/" + networkName + "/", "neuralData.dat");
		NeuralNetwork neural = recorder.readNeural();
		int charCount = 10;
		if (neural == null) {
			System.out.println("File not found. Making new Network");
			neural = new NeuralNetwork(1, 7 * charCount, 1, -1, 1);
		}
		List<Double> neuralIn = new ArrayList<Double>();
		neuralIn.add(1d);
		CodeWriter codeWriter = new CodeWriter("data/" + networkName + "/", "code.java");
		for (int genCount = 0; true; genCount++) {
			
		}
		/*List<Double> weights = neural.getAllNeuronWeights();
		weights.set(rand.nextInt(weights.size()), rand.nextDouble() * 2 - 1);
		neural.setAllNeuronWeights(weights);
		List<Double> neuralOut = neural.fire(neuralIn);
		int fitness = getFitness(neuralOut);
		recorder.writeNeural(neural);
		in.close();*/
	}

	public static int getFitness(List<Double> neuralOut) {
		CodeWriter codeWriter = new CodeWriter("data/" + networkName + "/", neuralOut.hashCode() + ".java");
		String code = neuralToString(neuralOut);
		codeWriter.writeCode(code);
		return codeWriter.compilePython();
	}

	public static String neuralToString(List<Double> neural) {
		String binaryIn = "";
		for (int outIndex = 0; outIndex < neural.size(); outIndex++) {
			binaryIn += Math.round(neural.get(outIndex));
		}
		String code = "";
		for (int i = 0; i < binaryIn.length() / 7; i++) {
			code += (char) (Integer.parseInt(binaryIn.substring(i * 7, (i + 1) * 7), 2));
		}
		return code;
	}
}
