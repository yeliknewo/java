package com.kileyowen.geneticalgorithm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

public class GeneticAlgorithm {
	private HashMap2D<Color> in;
	private List<HashMap2D<Color>> currentGen;
	private List<HashMap2D<Color>> nextGen;
	public static Random rand;

	public GeneticAlgorithm(HashMap2D<Color> in) {
		this.in = in;
		currentGen = new ArrayList<HashMap2D<Color>>();
		HashMap2D<Color> start = new HashMap2D<Color>();
		for (int y = 0; y < in.getHeight(); y++) {
			for (int x = 0; x < in.getWidth(); x++) {
				start.set(x, y, new Color(0));
			}
		}
		currentGen.add(start);
	}

	public List<HashMap2D<Color>> breed(HashMap2D<Color> parent1, HashMap2D<Color> parent2, double swapRate,
			double mutateRate) {
		Iterator<Integer> p1Y = parent1.getYIterator(), p1X, p2Y = parent2.getYIterator(), p2X;
		int p1YNext, p1XNext, p2YNext, p2XNext;
		HashMap2D<Color> r1 = new HashMap2D<Color>(), r2 = new HashMap2D<Color>();
		while (p1Y.hasNext() && p2Y.hasNext()) {
			p1YNext = p1Y.next();
			p2YNext = p2Y.next();
			while (p1YNext != p2YNext && (p1YNext != -1 || p2YNext != -1)) {
				if (p2YNext == -1) {
					if (p1Y.hasNext()) {
						p1YNext = p1Y.next();
					} else {
						p1YNext = -1;
					}
				} else if (p1YNext == -1) {
					if (p2Y.hasNext()) {
						p2YNext = p2Y.next();
					} else {
						p2YNext = -1;
					}
				} else {
					if (p1YNext < p2YNext) {
						if (p1Y.hasNext()) {
							p1YNext = p1Y.next();
						} else {
							p1YNext = -1;
						}
					} else if (p2YNext < p1YNext) {
						if (p2Y.hasNext()) {
							p2YNext = p2Y.next();
						} else {
							p2YNext = -1;
						}
					}
				}
			}
			if (p1YNext == -1 || p2YNext == -1) {
				break;
			}
			p1X = parent1.getXIterator(p1YNext);
			p2X = parent2.getXIterator(p2YNext);
			while (p1X.hasNext() && p2X.hasNext()) {
				p1XNext = p1X.next();
				p2XNext = p2X.next();
				while (p1XNext != p2XNext && (p1XNext != -1 || p2XNext != -1)) {
					if (p2XNext == -1) {
						if (p1X.hasNext()) {
							p1XNext = p1X.next();
						} else {
							p1XNext = -1;
						}
					} else if (p1XNext == -1) {
						if (p2X.hasNext()) {
							p2XNext = p2X.next();
						} else {
							p2XNext = -1;
						}
					} else {
						if (p1XNext < p2XNext) {
							if (p1X.hasNext()) {
								p1XNext = p1X.next();
							} else {
								p1XNext = -1;
							}
						} else {
							if (p2X.hasNext()) {
								p2XNext = p2X.next();
							} else {
								p2XNext = -1;
							}
						}
					}
				}
				if (p1XNext == -1 && p2XNext == -1) {
					break;
				}
				r1.set(p1XNext, p1YNext, parent1.get(p1XNext, p1YNext));
				r2.set(p1XNext, p1YNext, parent2.get(p1XNext, p1YNext));
				if (Math.random() < swapRate) {
					r1.set(p1XNext, p1YNext, parent2.get(p1XNext, p1YNext));
					r2.set(p1XNext, p1YNext, parent1.get(p1XNext, p1YNext));
				}
				if (Math.random() < mutateRate) {
					r1.set(p1XNext, p1YNext, new Color(rand.nextInt(0xFFFFFF)));
				}
				if (Math.random() < mutateRate) {
					r2.set(p1XNext, p1YNext, new Color(rand.nextInt(0xFFFFFF)));
				}
			}
		}
		List<HashMap2D<Color>> result = new ArrayList<HashMap2D<Color>>();
		result.add(r1);
		result.add(r2);
		return result;
	}

	public void makeNextGen(double swapRate, double mutateRate, int maxFit, int genSize) {
		nextGen = new ArrayList<HashMap2D<Color>>();
		List<Integer> pool = new ArrayList<Integer>();
		for (int i = 0; i < currentGen.size(); i++) {
			List<HashMap2D<Color>> kids = breed(currentGen.get(i), currentGen.get(rand.nextInt(currentGen.size())),
					swapRate, mutateRate);
			for (int i2 = 0; i2 < kids.size(); i2++) {
				double fit = getFitness(in, kids.get(i2));
				long count = Math.max(Math.round(fit * maxFit), 1);
				System.out.println(fit);
				for (int i3 = 0; i3 < count; i3++) {
					pool.add(i2);
				}
			}
			while (nextGen.size() < genSize) {
				nextGen.add(kids.get(pool.get(rand.nextInt(Math.max(pool.size(), 1)))));
			}
		}
		currentGen = nextGen;
	}

	private double getFitness(HashMap2D<Color> base, HashMap2D<Color> working) {
		Iterator<Integer> baseY = base.getYIterator(), baseX, workingY = working.getYIterator(), workingX;
		int baseYNext = -1, baseXNext = -1, workingYNext = -1, workingXNext = -1;
		double sum = 0, total = 0;
		while (baseY.hasNext() || workingY.hasNext()) {
			if (baseY.hasNext()) {
				baseYNext = baseY.next();
			}
			if (workingY.hasNext()) {
				workingYNext = workingY.next();
			}
			while (baseYNext != workingYNext && (baseYNext != -1 || workingYNext != -1)) {
				if (baseYNext < workingYNext || workingYNext == -1) {
					total++;
					if (baseY.hasNext()) {
						baseYNext = baseY.next();
					} else {
						baseYNext = -1;
					}
				} else {
					total++;
					if (workingY.hasNext()) {
						workingYNext = workingY.next();
					} else {
						workingYNext = -1;
					}
				}
			}
			if (baseYNext == -1 && workingYNext == -1) {
				break;
			}
			baseX = base.getXIterator(baseYNext);
			workingX = working.getXIterator(workingYNext);
			while (baseX.hasNext() || workingX.hasNext()) {
				if (baseX.hasNext()) {
					baseXNext = baseX.next();
				}
				if (workingX.hasNext()) {
					workingXNext = workingX.next();
				}
				while (baseXNext != workingXNext && (workingXNext != -1 || baseXNext != -1)) {
					if (baseXNext < workingXNext || workingXNext == -1) {
						total++;
						if (baseX.hasNext()) {
							baseXNext = baseX.next();
						} else {
							baseXNext = -1;
						}
					} else {
						total++;
						if (workingX.hasNext()) {
							workingXNext = workingX.next();
						} else {
							workingXNext = -1;
						}
					}
				}
				sum++;
				total++;
			}
		}
		return 1d / (total - sum);
	}

	public HashMap2D<Color> getOutput() {
		return currentGen.get(rand.nextInt(currentGen.size()));
	}

	public static HashMap2D<Color> readImage(String path) {
		BufferedImage bufferedImage;
		try {
			bufferedImage = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		HashMap2D<Color> image = new HashMap2D<Color>();
		for (int y = 0; y < bufferedImage.getHeight(); y++) {
			for (int x = 0; x < bufferedImage.getWidth(); x++) {
				image.set(x, y, new Color(bufferedImage.getRGB(x, y)));
			}
		}
		return image;
	}

	public static void writeImage(HashMap2D<Color> in, String path) {
		BufferedImage image = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				Color c = in.get(x, y);
				if (c != null) {
					image.setRGB(x, y, c.getRGB());
				}
			}
		}
		try {
			ImageIO.write(image, "png", new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		rand = new Random();
		GeneticAlgorithm genetic = new GeneticAlgorithm(readImage("assets/cat.png"));
		for (int i = 0; i < 1000; i++) {
			genetic.makeNextGen(0.7, 0.01, 1000, 100);
			writeImage(genetic.getOutput(), "assets/output" + i + ".png");
		}
	}

}
