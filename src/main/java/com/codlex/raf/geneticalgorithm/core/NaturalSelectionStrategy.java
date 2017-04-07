package com.codlex.raf.geneticalgorithm.core;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class NaturalSelectionStrategy {

	private final Comparator<Unit> PERFECT_DISTANCE_COMPARATOR = new Comparator<Unit>() {
		@Override
		public int compare(Unit o1, Unit o2) {
			return Integer.compare(o1.distanceTo(NaturalSelectionStrategy.this.perfect),
					o2.distanceTo(NaturalSelectionStrategy.this.perfect));
		}
	};

	private final Unit perfect;

	public NaturalSelectionStrategy(Unit perfect) {
		this.perfect = perfect;
	}

	public List<Unit> filter(final List<Unit> currentPopulation) {
		Collections.sort(currentPopulation, PERFECT_DISTANCE_COMPARATOR);
		return currentPopulation.subList(0, currentPopulation.size() / 2);
	}

}
