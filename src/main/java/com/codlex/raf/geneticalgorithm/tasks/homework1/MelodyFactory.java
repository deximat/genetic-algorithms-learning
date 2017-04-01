package com.codlex.raf.geneticalgorithm.tasks.homework1;

import java.util.ArrayList;
import java.util.List;

import com.codlex.raf.geneticalgorithm.core.PopulationFactory;
import com.codlex.raf.geneticalgorithm.core.Unit;

public class MelodyFactory extends PopulationFactory {

	@Override
	public List<Unit> generate() {
		List<Unit> initialPopulation = new ArrayList<Unit>();
		for (int i = 0; i < 100; i ++) {
			initialPopulation.add(Melody.generateRandom());
		}
		return initialPopulation;
	}

}
