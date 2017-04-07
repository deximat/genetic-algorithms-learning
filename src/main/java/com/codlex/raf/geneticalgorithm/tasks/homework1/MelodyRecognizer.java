package com.codlex.raf.geneticalgorithm.tasks.homework1;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

import com.codlex.raf.geneticalgorithm.core.GeneticAlgorithm;
import com.codlex.raf.geneticalgorithm.core.Unit;

public class MelodyRecognizer {

	public static void main(String[] args) throws InvalidMidiDataException, IOException {
		Melody perfectMelody = Test.loadMelodyFromMidi("test.mid", 1, 0, 50);
		
		final GeneticAlgorithm solver = new GeneticAlgorithm(new MelodyFactory(120), new MelodyMutator(), perfectMelody);
		solver.execute();
		for (Unit unit : solver.getBest()) {
			System.out.println("Playing: " + unit);
			Melody melody = (Melody) unit;
			melody.play();
		};
		System.out.println("finished");
	}
}
