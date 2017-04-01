package com.codlex.raf.geneticalgorithm.core;

import java.util.List;

public class NaturalSelectionStrategy {

	public List<Unit> filter(final List<Unit> currentPopulation) {
		// TODO: implement comparation by function
		return currentPopulation.subList(0, currentPopulation.size() / 2);
	}

}
