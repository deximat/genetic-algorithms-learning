package com.codlex.raf.geneticalgorithm.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GeneticAlgorithm {
	
	private static final int GENERATION_COUNT_LIMIT = 1500;

	private final PopulationFactory populationFactory;
	private final NaturalSelectionStrategy naturalSelection;
	private final MatingStrategy mating = new MatingStrategy();

	private final MutatingStrategy mutator;

	private List<Unit> best;

	private Unit perfect;
	
	private final Comparator<Unit> PERFECT_DISTANCE_COMPARATOR = new Comparator<Unit>() {
		@Override
		public int compare(Unit o1, Unit o2) {
			return Integer.compare(o1.distanceTo(GeneticAlgorithm.this.perfect),
					o2.distanceTo(GeneticAlgorithm.this.perfect));
		}
	};
	
	public GeneticAlgorithm(final PopulationFactory populationFactory, final MutatingStrategy mutator,  final Unit perfect) {
		this.perfect = perfect;
		this.populationFactory = populationFactory;
		this.naturalSelection = new NaturalSelectionStrategy();
		this.mutator = mutator;
	}
	
	public void execute() {
		int currentGeneration = 0;
		List<Unit> currentPopulation = this.populationFactory.generate();
		while (currentGeneration < GENERATION_COUNT_LIMIT) { // TODO: create convergence checker
			System.out.println("Executing generation: " + currentGeneration);
			Collections.sort(currentPopulation, PERFECT_DISTANCE_COMPARATOR);

			List<Couple> parents = this.mating.match(currentPopulation);
			
			List<Unit> children = new ArrayList<>();
			for (Couple couple : parents) {
				children.addAll(couple.reproduce());
			}
			
			List<Unit> mutatedChildren = new ArrayList<>();
			for (final Unit unit : children) {
				mutatedChildren.add(this.mutator.mutate(unit));
			}
			
			for (final Unit unit : currentPopulation) {
				mutatedChildren.add(this.mutator.mutate(unit));
			}
			
			List<Unit> newGeneration = new ArrayList<Unit>();
			newGeneration.addAll(currentPopulation);
			newGeneration.addAll(children);
			newGeneration.addAll(mutatedChildren);
			
			System.out.println("New generation size: " + newGeneration.size());
			
			Collections.sort(newGeneration, PERFECT_DISTANCE_COMPARATOR);
			newGeneration = this.naturalSelection.filter(newGeneration);
			System.out.println("Natural survivals: " + newGeneration.size());
			
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
