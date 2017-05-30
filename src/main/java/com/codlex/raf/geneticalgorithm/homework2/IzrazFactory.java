package com.codlex.raf.geneticalgorithm.homework2;

import java.util.ArrayList;
import java.util.List;

import com.codlex.raf.geneticalgorithm.core.PopulationFactory;
import com.codlex.raf.geneticalgorithm.core.Unit;

public class IzrazFactory extends PopulationFactory {
	
	private final List<Integer> allowedNumbers;
	private final int populationSize = 200;
	
	public IzrazFactory(List<Integer> allowedNumbers) {
		this.allowedNumbers = allowedNumbers;
	}
	
	@Override
	public List<Unit> generate(int size) {
		final List<Unit> izrazi = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			izrazi.add(Izraz.generate(this.allowedNumbers));
		}
		return izrazi;
	}
	@Override
	public List<Unit> generate() {
		return generate(this.populationSize);
	}
}
