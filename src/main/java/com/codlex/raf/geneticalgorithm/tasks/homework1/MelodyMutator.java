package com.codlex.raf.geneticalgorithm.tasks.homework1;

import java.util.concurrent.ThreadLocalRandom;

import com.codlex.raf.geneticalgorithm.core.MutatingStrategy;
import com.codlex.raf.geneticalgorithm.core.Unit;

public class MelodyMutator extends MutatingStrategy {
	@Override
	public Unit mutate(Unit unit) {
		final Melody melody = (Melody) unit.duplicate();
		
		int maxSlotsToClear = (melody.getLength() * 7) / 100;
		int slotsToClear = ThreadLocalRandom.current().nextInt(maxSlotsToClear);
		for (int i = 0; i < slotsToClear; i++) {
			// melody.clear(ThreadLocalRandom.current().nextInt(melody.getLength()));
			melody.removeToneAt(ThreadLocalRandom.current().nextInt(melody.getLength()));
		}
		
		// add tone
		int maxTonesToAdd = (melody.getLength() * 7) / 100;
		int tonesToAdd = ThreadLocalRandom.current().nextInt(maxTonesToAdd);
		for (int i = 0; i < tonesToAdd; i++) {
			melody.addTone(ThreadLocalRandom.current().nextInt(melody.getLength()),
					new Tone(Note.getRandom(), Octave.getRandom()));
		}
		
		return melody;
	}
}
