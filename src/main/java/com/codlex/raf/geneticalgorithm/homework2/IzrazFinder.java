package com.codlex.raf.geneticalgorithm.homework2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;

import com.codlex.raf.geneticalgorithm.core.GeneticAlgorithm;

public class IzrazFinder {

	private static List<Integer> generateSimpleNumbers() {
		List<Integer> sampleNumbers = new ArrayList<>();
//		sampleNumbers.add(112);
//		sampleNumbers.add(55);
//		sampleNumbers.add(2);
//		sampleNumbers.add(2);
//		sampleNumbers.add(2);
//		sampleNumbers.add(1);
		
		sampleNumbers.add(1);
		sampleNumbers.add(2);
		sampleNumbers.add(3);
		// (((112-55)*2)/2-1)/2
		return sampleNumbers;
	}
	
	public static void main(String[] args) throws InvalidMidiDataException, IOException {
		// TODO: implement reading from the file
		List<Integer> sampleNumbers = generateSimpleNumbers();
		int targetResult = 9;
		
		final GeneticAlgorithm solver = new GeneticAlgorithm(new IzrazFactory(sampleNumbers), new IzrazMutator(), new Izraz(targetResult));
		solver.execute();
		
		final Izraz best = (Izraz) solver.getBest().get(0);
		
		System.out.println("Using numbers: " + sampleNumbers);
		System.out.println("Wanted result is: " + targetResult);
		System.out.println("Our winner is: " + best + " which evaluates to: " + best.evaluate());
		System.out.println("Mistake: " + Math.abs(targetResult - best.evaluate()));
	}
}
