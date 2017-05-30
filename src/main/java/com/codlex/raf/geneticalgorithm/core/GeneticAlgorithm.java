package com.codlex.raf.geneticalgorithm.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GeneticAlgorithm {
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

	public GeneticAlgorithm(final PopulationFactory populationFactory, final MutatingStrategy mutator,
			final Unit perfect) {
		this.perfect = perfect;
		this.populationFactory = populationFactory;
		this.naturalSelection = new NaturalSelectionStrategy();
		this.mutator = mutator;
	}

	public void execute() {
		this.startedAt = System.currentTimeMillis();
		
		int playOnGeneration = 20;
		int currentGeneration = 0;
		List<Unit> currentPopulation = this.populationFactory.generate();
		double bestDistance = Double.MAX_VALUE;
		while (!shouldEnd(currentGeneration, bestDistance)) {
			// add some random seeds
			currentPopulation.addAll(this.populationFactory.generate(10));
			// System.out.println("Executing generation: " + currentGeneration);
			// Collections.sort(currentPopulation, PERFECT_DISTANCE_COMPARATOR);
			Collections.shuffle(currentPopulation);
			
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

			// System.out.println("New generation size: " +
			// newGeneration.size());
			int wantedSize = newGeneration.size() / 4;
			filterUniqueOnly(newGeneration);
			
			// add random if filtered out a lot
			List<Unit> additional = this.populationFactory.generate(Math.max(10, wantedSize - newGeneration.size()));
			newGeneration.addAll(additional);
			
			Collections.sort(newGeneration, PERFECT_DISTANCE_COMPARATOR);
			newGeneration = this.naturalSelection.filter(newGeneration, wantedSize);
			// System.out.println("Natural survivals: " + newGeneration.size());

			currentPopulation = newGeneration;
			double averageDistance = currentPopulation.stream().mapToInt(u -> u.distanceTo(this.perfect)).average()
					.getAsDouble();
			bestDistance = currentPopulation.get(0).distanceTo(this.perfect);

			System.out.println("Generation(" + currentGeneration + ") distance from perfection: " + bestDistance + " avg: " + averageDistance);
			// System.out.println(currentPopulation);
			
			currentGeneration++;
			
//			if (currentGeneration == 5) {
//				System.exit(0);
//			}
		}

		this.best = currentPopulation;
	}

	private void filterUniqueOnly(List<Unit> newGeneration) {
		Map<String, Unit> generation = new HashMap<>();
		for (Unit unit : newGeneration) {
			generation.put(unit.getKey(), unit);
		}
		newGeneration.clear();
		newGeneration.addAll(generation.values());
	}
	

	private int lastGenerationSimilarDistance = 0;
	private double lastDistance = Double.MAX_VALUE;
	private long startedAt;

	private boolean shouldEnd(int currentGeneration, double bestDistance) {
		if (bestDistance == 0) {
			return true;
		}
		
		if (timeElapsed()) {
			return true;
		}
		
		return false;
		
//		
//		int compareCoefficient = 0;
//		double differenceFromLast = Math.abs(this.lastDistance - bestDistance);
//		if (differenceFromLast >= compareCoefficient) {
//			this.lastGenerationSimilarDistance = currentGeneration;
//		}
//		this.lastDistance = bestDistance;
//
//		int similarDistanceCount = currentGeneration - this.lastGenerationSimilarDistance;
//
//		final int endTimesSimilar = 200;
//		if (similarDistanceCount > endTimesSimilar) {
//			return true;
//		} else {
//			return false;
//		}
		
		
	}

	private boolean timeElapsed() {
		return System.currentTimeMillis() - this.startedAt >= TimeUnit.SECONDS.toMillis(45);
	}

	public List<Unit> getBest() {
		return this.best;
	}

}
