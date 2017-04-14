package com.codlex.raf.geneticalgorithm.tasks.homework1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class MelodyLoader {
	public static final int NOTE_ON = 0x90;
	public static final int NOTE_OFF = 0x80;
	public static final String[] NOTE_NAMES = { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };
	public static int resolution = 100;
	
	
	public static Melody loadMelodyFromMidi(final String file, int trackNumber, int channel, int length)
			throws InvalidMidiDataException, IOException {
		
		
		int melodyLength = 0;
		final Sequence sequence = MidiSystem.getSequence(new File(file));
		Map<Tone, MidiTone> currentTones = new HashMap<>();
		List<MidiTone> allTones = new ArrayList<>();
		Track track = sequence.getTracks()[trackNumber];
		System.out.println("Track " + trackNumber + ": size = " + track.size());
		
		for (int i = 0; i < track.size(); i++) {
			MidiEvent event = track.get(i);
			
			System.out.print("@" + event.getTick() + " ");

			MidiMessage message = event.getMessage();
			if (message instanceof ShortMessage) {
				ShortMessage sm = (ShortMessage) message;
				if (sm.getChannel() != channel) {
					System.out.println("skip");
					continue;
				} 
				
				if (sm.getCommand() == NOTE_ON) {
					int key = sm.getData1();
					int octave = (key / 12) - 1;
					int note = key % 12;
					String noteName = NOTE_NAMES[note];
					int velocity = sm.getData2();
					MidiTone tone = new MidiTone((int) event.getTick() / resolution, new Tone(Note.getByKey(note), octave - 1));
					currentTones.put(tone.getTone(), tone);
					System.out.println("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity);
				} else if (sm.getCommand() == NOTE_OFF) {
					int key = sm.getData1();
					int octave = (key / 12) - 1;
					int note = key % 12;
					String noteName = NOTE_NAMES[note];
					int velocity = sm.getData2();

					Tone tone = new Tone(Note.getByKey(note), octave - 1);
					MidiTone midiTone = currentTones.remove(tone);
					midiTone.setTo((int) event.getTick() / resolution);
					allTones.add(midiTone);
					if (event.getTick() / resolution > melodyLength) {
						melodyLength = (int) (event.getTick() / resolution);
					}

					System.out.println("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);
				} else {
					System.out.println("Command:" + sm.getCommand());
				}
			} else {
				System.out.println("Other message: " + message.getClass());
			}
			
			
			if (allTones.size() > length) {
				break;
			}
		}

		System.out.println("size: " + allTones.size());
		System.out.println("Melody length is: " + melodyLength);
		final Melody melody = new Melody(melodyLength);
		for (MidiTone midiTone : allTones) {
			Tone tone = midiTone.getTone();
			for (int i = midiTone.getFrom(); i < midiTone.getTo(); i++) {
				melody.addTone(i, tone);
			}
		}
		//
		//melody.play();

		return melody;

	}

	public static void main(String[] args) throws Exception {
		loadMelodyFromMidi("test.mid", 1, 0, 50);

	}
}
