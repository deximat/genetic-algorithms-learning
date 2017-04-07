package com.codlex.raf.geneticalgorithm.tasks.homework1;

import java.util.concurrent.ThreadLocalRandom;

public enum Note {
	
    C(0), CSharp(1), D(2), DSharp(3), E(4), F(5), FSharp(6), G(7), GSharp(8), A(9), ASharp(10), B(11);
	
	private final int key;
	
	private Note(final int key) {
		this.key = key;
	}
	
	public static Note getByKey(int key) {
		return values()[key];
	}

	public static Note getRandom() {
		final int notes = values().length;
		return values()[ThreadLocalRandom.current().nextInt(notes)];
	}

	public int getKey() {
		return this.key;
	}
}
