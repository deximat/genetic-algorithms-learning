package com.codlex.raf.geneticalgorithm.core;

import java.util.List;

public abstract class PopulationFactory {

	public abstract List<Unit> generate();
	public abstract List<Unit> generate(int size);
}
