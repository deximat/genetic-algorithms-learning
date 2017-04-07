package com.codlex.raf.geneticalgorithm.tasks.homework1;

public class Tone {
	
	private final Note note;
	private final int octave;

	public Tone(final Note note, final int octave) {
		this.note = note;
		this.octave = octave;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.note).append(this.octave);
		return builder.toString();
	}
	
	public int getMidiSound() {
		return this.note.getKey() + (this.octave + 1) * 12;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result + octave;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tone other = (Tone) obj;
		if (note != other.note)
			return false;
		if (octave != other.octave)
			return false;
		return true;
	}
	
}
