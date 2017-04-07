package com.codlex.raf.geneticalgorithm.tasks.homework1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import com.codlex.raf.geneticalgorithm.core.Unit;

public class Melody extends Unit {

	private final int length;

	public Melody(int length) {
		this.length = length;
	}

	private final Map<Integer, List<Tone>> sequence = new TreeMap<>();

	@Override
	public Collection<Unit> makeLoveWith(final Unit secondParent) {
		final List<Unit> children = new ArrayList<Unit>();
		children.add(secondParent); // isti mama
		return children;
	}

	public void addTone(final int time, final Tone tone) {
		List<Tone> tones = this.sequence.get(time);
		if (tones == null) {
			tones = new ArrayList<>();
			this.sequence.put(time, tones);
		}
		tones.add(tone);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < this.length; i++) {
			List<Tone> tones = this.sequence.get(i);
			if (tones == null) {
				builder.append("P ");
			} else {
				if (tones.size() > 1) {
					builder.append("(");
				}
				for (Tone tone : tones) {
					builder.append(tone).append(" ");
				}
				if (tones.size() > 1) {
					builder.append(")");
				}
			}
		}
		return builder.toString();
	}

	public void play() {
		try {
			int velocity = 90; // max volume
			Synthesizer synthesizer = MidiSystem.getSynthesizer();
			synthesizer.open();
			
			MidiChannel channel = synthesizer.getChannels()[0]; // piano
			double multiplier = 1.5;
			for (List<Tone> tones : this.sequence.values()) {
				if (tones == null) {
					Thread.sleep((long) (Test.resolution * multiplier));
				} else {
					for (Tone tone : tones) {
						channel.noteOn(tone.getMidiSound(), velocity);
						System.out.println("Playing: " + tone);
						System.out.println("Tone: " + tone.getMidiSound());

					}
					Thread.sleep((long) (Test.resolution * multiplier));
					for (Tone tone : tones) {
						channel.noteOff(tone.getMidiSound());
					}
				}
				
			}

		} catch (MidiUnavailableException | InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
