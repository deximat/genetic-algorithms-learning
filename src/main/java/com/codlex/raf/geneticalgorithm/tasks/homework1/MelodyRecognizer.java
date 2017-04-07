package com.codlex.raf.geneticalgorithm.tasks.homework1;

import com.codlex.raf.geneticalgorithm.core.GeneticAlgorithm;

public class MelodyRecognizer {

	public static void main(String[] args) {
		final GeneticAlgorithm solver = new GeneticAlgorithm(new MelodyFactory(100));
		solver.execute();
		// solver.getBest();
	}
}
