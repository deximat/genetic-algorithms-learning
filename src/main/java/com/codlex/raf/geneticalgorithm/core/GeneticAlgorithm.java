package com.codlex.raf.geneticalgorithm.core;

import java.util.ArrayList;
import java.util.List;

public class GeneticAlgorithm {
	
	private static final int GENERATION_COUNT_LIMIT = 1000;

	private final PopulationFactory populationFactory;
	private final NaturalSelectionStrategy naturalSelection;
	private final MatingStrategy mating = new MatingStrategy();

	private final MutatingStrategy mutator = new MutatingStrategy();

	private List<Unit> best;

	private Unit perfect;
	
	public GeneticAlgorithm(final PopulationFactory populationFactory, final Unit perfect) {
		this.perfect = perfect;
		this.populationFactory = populationFactory;
		this.naturalSelection = new NaturalSelectionStrategy(perfect);
	}
	
	public void execute() {
		int currentGeneration = 0;
		List<Unit> currentPopulation = this.populationFactory.generate();
		while (currentGeneration < GENERATION_COUNT_LIMIT) { // TODO: create convergence checker
			System.out.println("Executing generation: " + currentGeneration);
			List<Couple> parents = this.mating.match(currentPopulation);
			List<Unit> newGeneration = new ArrayList<Unit>();
			newGeneration.addAll(currentPopulation);
			for (Couple couple : parents) {
				newGeneration.addAll(couple.reproduce());
			}
			
			System.out.println("New generation size: " + newGeneration.size());
			
			newGeneration = this.naturalSelection.filter(newGeneration);
			System.out.println("Natural survivals: " + newGeneration.size());
			

			for (final Unit unit : newGeneration) {
				this.mutator.mutate(unit);
			}
			
			currentPopulation = newGeneration;
			double averageDistance = currentPopulation.stream().mapToInt(u -> u.distanceTo(this.perfect)).average().getAsDouble();
			System.out.println("Generation distance: " + averageDistance);
			currentGeneration++;
		}
		
		this.best = currentPopulation;
	}

	public List<Unit> getBest() {
		return this.best;
	}
	
}
