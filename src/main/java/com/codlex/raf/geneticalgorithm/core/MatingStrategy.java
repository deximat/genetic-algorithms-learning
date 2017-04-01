package com.codlex.raf.geneticalgorithm.core;

import java.util.ArrayList;
import java.util.List;

public class MatingStrategy {

	public List<Couple> match(final List<Unit> singles) {
		final List<Couple> couples = new ArrayList<Couple>();
		
		for (int i = 0; i < singles.size() - 1; i += 2) {
			couples.add(new Couple(singles.get(i), singles.get(i + 1)));
		}
		
		return couples;
	}

}
