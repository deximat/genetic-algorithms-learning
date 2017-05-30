package com.codlex.raf.geneticalgorithm.homework2;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.sound.midi.InvalidMidiDataException;

import com.codlex.raf.geneticalgorithm.core.GeneticAlgorithm;

public class IzrazFinder {
	
	public static void main(String[] args) throws InvalidMidiDataException, IOException {
		final Scanner scanner = new Scanner(new FileReader(new File("ulaz.txt")));
		List<Integer> givenNumbers = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			givenNumbers.add(scanner.nextInt());
		}
		int targetResult = scanner.nextInt();
		scanner.close();
		
		
		
//		Izraz izraz1 = Izraz.generate(givenNumbers);
//		Izraz izraz2 = Izraz.generate(givenNumbers);
//		System.out.println("Izraz1: " + izraz1);
////		System.out.println("Izraz2: " + izraz2);
//		
//		System.out.println(new IzrazMutator().mutate(izraz1));
//		
//		System.out.println(izraz1.makeLoveWith(izraz2));
//		
		final GeneticAlgorithm solver = new GeneticAlgorithm(new IzrazFactory(givenNumbers), new IzrazMutator(givenNumbers), new Izraz(targetResult));
		solver.execute();
		
		final Izraz best = (Izraz) solver.getBest().get(0);
		
		System.out.println("Using numbers: " + givenNumbers);
		System.out.println("Wanted result is: " + targetResult);
		System.out.println("Our winner is: " + best + " which evaluates to: " + best.evaluate());
		System.out.println("Mistake: " + Math.abs(targetResult - best.evaluate()));
	}
}
