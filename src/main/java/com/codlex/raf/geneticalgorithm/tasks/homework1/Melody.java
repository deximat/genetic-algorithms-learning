package com.codlex.raf.geneticalgorithm.tasks.homework1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.codlex.raf.geneticalgorithm.core.Unit;

public class Melody extends Unit {
	
	@Override
	public Collection<Unit> makeLoveWith(final Unit secondParent) {
		final List<Unit> children = new ArrayList<Unit>();
		children.add(secondParent); // isti mama
		return children;
	}

	public static Melody generateRandom() {
		return new Melody();
	}
	
}
