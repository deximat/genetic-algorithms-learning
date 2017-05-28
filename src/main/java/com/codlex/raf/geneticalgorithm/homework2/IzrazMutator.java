package com.codlex.raf.geneticalgorithm.homework2;

import java.util.concurrent.ThreadLocalRandom;

import com.codlex.raf.geneticalgorithm.core.MutatingStrategy;
import com.codlex.raf.geneticalgorithm.core.Unit;
import com.codlex.raf.geneticalgorithm.homework2.Izraz.Operation.OperationType;

public class IzrazMutator extends MutatingStrategy {

	@Override
	public Unit mutate(Unit unit) {
		Izraz izraz = (Izraz) unit.duplicate();
		
		switch (ThreadLocalRandom.current().nextInt(3)) {
		case 0:
			// swap two numbers in expression
			swapOperands(izraz);
			break;
		case 1:
			// re roll operation
			reRollOperation(izraz);
			break;
		case 2:
			// change one number with new number in expression
			removeOperation(izraz);
			break;
		}
		
		
		// add operand TODO
		// addOperand(izraz);
		
		return unit;
	}

	private void removeOperation(Izraz izraz) {
		int operation = getRandomOperation(izraz);
		if (operation == -1) {
			return;
		}
		izraz.removeOperation(operation);
	}

	private void reRollOperation(Izraz izraz) {
		int operation = getRandomOperation(izraz);
		if (operation == -1) {
			return;
		}
		izraz.setOperation(operation, OperationType.getRandom());
	}

	private int getRandomOperation(Izraz izraz) {
		int numberOfOperations = izraz.countOperations();
		if (numberOfOperations == 0) {
			return -1;
		}
		int operation = ThreadLocalRandom.current().nextInt(0, numberOfOperations);
		return operation;
	}

	private void swapOperands(Izraz izraz) {
		int numberOfOperands = izraz.countOperands();
		int firstOperand = ThreadLocalRandom.current().nextInt(0, numberOfOperands);
		int secondOperand = ThreadLocalRandom.current().nextInt(0, numberOfOperands);
		int firstOperandValue = izraz.getOperandValue(firstOperand);
		int secondOperandValue = izraz.getOperandValue(secondOperand);
		izraz.setOperandValue(firstOperand, secondOperandValue);
		izraz.setOperandValue(secondOperand, firstOperandValue);
	}

}
