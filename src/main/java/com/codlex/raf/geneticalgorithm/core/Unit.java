package com.codlex.raf.geneticalgorithm.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import com.codlex.raf.geneticalgorithm.tasks.homework1.Note;
import com.codlex.raf.geneticalgorithm.tasks.homework1.Tone;

public abstract class Unit {
	public abstract Collection<Unit> makeLoveWith(Unit secondParent);
	
}
