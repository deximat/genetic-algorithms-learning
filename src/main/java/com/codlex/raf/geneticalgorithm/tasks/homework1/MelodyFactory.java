package com.codlex.raf.geneticalgorithm.tasks.homework1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import com.codlex.raf.geneticalgorithm.core.PopulationFactory;
import com.codlex.raf.geneticalgorithm.core.Unit;

public class MelodyFactory extends PopulationFactory {

	private final int melodyLength;
	private final int maxConcurrentTones = 5;
	private final int populationSize = 100;

	public MelodyFactory(int melodyLength) {
		this.melodyLength = melodyLength;
	}

	@Override
	public List<Unit> generate() {
		List<Unit> initialPopulation = new ArrayList<Unit>();
		for (int i = 0; i < this.populationSize; i++) {
			initialPopulation.add(generateMelody());
		}
		return initialPopulation;
	}

	private Melody generateMelody() {
		final Melody melody = new Melody(this.melodyLength);
		for (int i = 0; i < this.melodyLength; i++) {
			int tones = ThreadLocalRandom.current().nextInt(this.maxConcurrentTones + 1);
			for (int j = 0; j < tones; j++) {
				melody.addTone(i, new Tone(Note.getRandom(), Octave.getRandom()));
			}
		}
		return melody;
	}

	public static void main(String[] args) {
		 for (Unit melody : new MelodyFactory(10).generate()) {
			 System.out.println(melody);
			 ((Melody) melody).play();
		 }
	}

}
