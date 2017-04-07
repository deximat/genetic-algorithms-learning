package com.codlex.raf.geneticalgorithm.core;

import java.util.Collection;

public abstract class Unit {
	public abstract Collection<Unit> makeLoveWith(Unit secondParent);
	public abstract int distanceTo(Unit other);
	public abstract Unit duplicate();
}
