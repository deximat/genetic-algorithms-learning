package com.codlex.raf.geneticalgorithm.tasks.homework1;

public class MidiTone {

	private int from;
	private Tone tone;
	private int to;

	public MidiTone(int from, Tone tone) {
		this.from = from;
		this.tone = tone;
	}
	
	public void setTo(int to) {
		this.to = to;
	}
	
	public Tone getTone() {
		return this.tone;
	}
	
	public int getTo() {
		return this.to;
		
	}
	
	public int getFrom() {
		return this.from;
	}
}
