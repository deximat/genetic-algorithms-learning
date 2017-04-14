package com.codlex.raf.geneticalgorithm.tasks.homework1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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
	private Integer distance;

	@Override
	public Collection<Unit> makeLoveWith(final Unit secondParent) {
		final Melody secondMelody = (Melody) secondParent;

		int childrenLength = Math.max(this.length, secondMelody.length);

		int middle = ThreadLocalRandom.current().nextInt(childrenLength);

		Melody firstChild = new Melody(childrenLength);
		Melody secondChild = new Melody(childrenLength);
		for (int i = 0; i < childrenLength; i++) {
			List<Tone> firstTones = this.sequence.get(i) != null ? this.sequence.get(i) : Collections.emptyList();
			List<Tone> secondTones = secondMelody.sequence.get(i) != null ? secondMelody.sequence.get(i)
					: Collections.emptyList();
//
//			if (i < middle) {
//				for (Tone tone : firstTones) {
//					firstChild.addTone(i, tone);
//				}
//				for (Tone tone : secondTones) {
//					secondChild.addTone(i, tone);
//				}
//			} else {
//				for (Tone tone : firstTones) {
//					secondChild.addTone(i, tone);
//				}
//				for (Tone tone : secondTones) {
//					firstChild.addTone(i, tone);
//				}
//			}
			
			if (Math.random() < 0.5) {
				for (Tone tone : firstTones) {
					firstChild.addTone(i, tone);
				}
				for (Tone tone : secondTones) {
					secondChild.addTone(i, tone);
				}
			} else {
				
				for (Tone tone : firstTones) {
					secondChild.addTone(i, tone);
				}
				
				for (Tone tone : secondTones) {
					firstChild.addTone(i, tone);
				}
			}
		}

		final List<Unit> children = new ArrayList<Unit>();
		children.add(firstChild);
		children.add(secondChild);
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
			double multiplier = 2;
			for (List<Tone> tones : this.sequence.values()) {
				if (tones == null) {
					Thread.sleep((long) (MelodyLoader.resolution * multiplier));
				} else {
					for (Tone tone : tones) {
						channel.noteOn(tone.getMidiSound(), velocity);
//						System.out.println("Playing: " + tone);
//						System.out.println("Tone: " + tone.getMidiSound());

					}
					Thread.sleep((long) (MelodyLoader.resolution * multiplier));
					for (Tone tone : tones) {
						channel.noteOff(tone.getMidiSound());
					}
				}

			}

		} catch (MidiUnavailableException | InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public int distanceTo(final Unit unit) {
		if (this.distance == null) {

			final Melody melody = (Melody) unit;
			int distance = 0;

			if (this.length != melody.length) {
				final int lengthCoefficient = 10000;
				System.exit(0);
				distance += Math.abs(this.length - melody.length) * lengthCoefficient;
			}

			for (int i = 0; i < Math.max(this.length, melody.length); i++) {
				Set<Tone> myTones = getTonesAt(i);
				Set<Tone> hisTones = melody.getTonesAt(i);

				List<Integer> myTonesPitch = new ArrayList<>();
				for (Tone tone : myTones) {
					myTonesPitch.add(tone.getMidiSound());
				}
				Collections.sort(myTonesPitch);

				List<Integer> hisTonesPitch = new ArrayList<>();
				for (Tone tone : hisTones) {
					hisTonesPitch.add(tone.getMidiSound());
				}
				Collections.sort(hisTonesPitch);

				final int differenceCoefficient = 1;
				for (int j = 0; j < Math.max(myTonesPitch.size(), hisTonesPitch.size()); j++) {
					int myTone = myTonesPitch.size() > j ? myTonesPitch.get(j) : 0;
					int hisTone = hisTonesPitch.size() > j ? hisTonesPitch.get(j) : 0;
					if (myTone != hisTone) {
						distance += 1000 + Math.abs(myTone - hisTone) * differenceCoefficient;
					}
				}
			}

			this.distance = distance;
		}

		return this.distance;
	}

	private Set<Tone> getTonesAt(final int time) {
		List<Tone> tones = this.sequence.get(time);
		if (tones == null) {
			tones = Collections.emptyList();
		}

		return tones.stream().collect(Collectors.toSet());
	}

	public int getLength() {
		return this.length;
	}

	public void clear(int index) {
		this.sequence.remove(index);
		this.distance = null;
	}
	
	@Override
	public Unit duplicate() {
		Melody melody = new Melody(this.length);
		for (Entry<Integer, List<Tone>> entry : this.sequence.entrySet()) {
			for (Tone tone : entry.getValue()) {
				melody.addTone(entry.getKey(), tone);
			}
		}
		return melody;
	}
	
	public String toStringMusicSheet() {
		StringBuilder builder = new StringBuilder();	
		
		int i = 0;
		while (i < this.length) {
			int start = i;
			List<Tone> tones = this.sequence.get(start);
			int duration = 0;
			while (Objects.equals(tones, this.sequence.get(start + duration)) && start + duration < this.length) {
				duration++;
			}
			
			if (tones == null) {
				builder.append("P");
				builder.append("(");
//				builder.append(start);
//				builder.append("-");
//				builder.append(start + duration);
//				builder.append(":");
				builder.append(duration);
				builder.append(")");
			} else if (tones.size() == 1) {
				builder.append(tones.get(0));
				builder.append("(");
//				builder.append(start);
//				builder.append("-");
//				builder.append(start + duration);
//				builder.append(":");
				builder.append(duration);
				builder.append(")");
			} else {
				builder.append("(");
				for (Tone tone : tones) {
					builder.append(tone);
					builder.append("(");
//					builder.append(start);
//					builder.append("-");
//					builder.append(start + duration);
//					builder.append(":");
					builder.append(duration);
					builder.append(")");
				}
				builder.append(")");
			}
			
			i += duration;
		}
		
		return builder.toString();
	}

	public void removeToneAt(int index) {
		List<Tone> tones = this.sequence.get(index);
		if (tones != null && !tones.isEmpty()) {
			tones.remove(ThreadLocalRandom.current().nextInt(tones.size()));
		}
	}
}
