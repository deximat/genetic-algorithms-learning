package com.codlex.raf.geneticalgorithm.core;

import java.util.Collection;

public class Couple {

	private final Unit firstParent;
	private final Unit secondParent;

	public Couple(final Unit firstParent, final Unit secondParent) {
		this.firstParent = firstParent;
		this.secondParent = secondParent;
	}
	
	public Collection<Unit> reproduce() {
		return this.firstParent.makeLoveWith(this.secondParent);
	}
}
