package com.codlex.raf.geneticalgorithm.core;

import java.util.ArrayList;
import java.util.List;

public class GeneticAlgorithm {
	
	private static final int GENERATION_COUNT_LIMIT = 100;

	private final PopulationFactory populationFactory;
	private final NaturalSelectionStrategy naturalSelection = new NaturalSelectionStrategy();
	private final MatingStrategy mating = new MatingStrategy();

	private final MutatingStrategy mutator = new MutatingStrategy();
	
	public GeneticAlgorithm(final PopulationFactory populationFactory) {
		this.populationFactory = populationFactory;
	}
	
	public void execute() {
		int currentGeneration = 0;
		List<Unit> currentPopulation = this.populationFactory.generate();
		while (currentGeneration < GENERATION_COUNT_LIMIT) { // TODO: create convergence checker
			List<Unit> survivals = this.naturalSelection.filter(currentPopulation);
			List<Couple> parents = this.mating.match(survivals);
			List<Unit> newGeneration = new ArrayList<Unit>();
			for (Couple couple : parents) {
				newGeneration.addAll(couple.reproduce());
			}
			
			for (final Unit unit : newGeneration) {
				this.mutator.mutate(unit);
			}
			
			currentPopulation = newGeneration;
			currentGeneration++;
		}
	}
	
}
