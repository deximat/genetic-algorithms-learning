package com.codlex.raf.geneticalgorithm.tasks.homework1;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

import com.codlex.raf.geneticalgorithm.core.GeneticAlgorithm;

public class MelodyRecognizer {

	public static void main(String[] args) throws InvalidMidiDataException, IOException {
		String filename = "test.mid";
		int notesToFetch = 25;
		int track = 1;
		int channel = 0;
		
		
		Melody perfectMelody = MelodyLoader.loadMelodyFromMidi(filename, track, channel, notesToFetch);
		
		// System.out.println("Playing perfect melody");
		// perfectMelody.play();
		
		final GeneticAlgorithm solver = new GeneticAlgorithm(new MelodyFactory(perfectMelody.getLength()), new MelodyMutator(), perfectMelody);
		solver.execute();
		
		final Melody best = (Melody) solver.getBest().get(0);
//		best.play();
		System.out.println("Perfect melody: " + perfectMelody.toStringMusicSheet());
		System.out.println("Our winner is: " + best.toStringMusicSheet());
	}
}
