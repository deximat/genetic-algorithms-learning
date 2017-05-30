package com.codlex.raf.geneticalgorithm.core;

import java.util.List;


public class NaturalSelectionStrategy {

	public List<Unit> filter(final List<Unit> currentPopulation, int wantedSize) {
		return currentPopulation.subList(0, wantedSize);
	}

}
