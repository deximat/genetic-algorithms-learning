package com.codlex.raf.geneticalgorithm.tasks.homework1;

import java.util.concurrent.ThreadLocalRandom;

public class Octave {
	private final static int NUMBER_OF_OCTAVES = 10;
	
	public static int getRandom() {
		return ThreadLocalRandom.current().nextInt(NUMBER_OF_OCTAVES + 1);
	}
}
