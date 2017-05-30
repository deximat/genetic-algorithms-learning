package com.codlex.raf.geneticalgorithm.homework2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.codlex.raf.geneticalgorithm.core.MutatingStrategy;
import com.codlex.raf.geneticalgorithm.core.Unit;
import com.codlex.raf.geneticalgorithm.homework2.Izraz.Operation.OperationType;

public class IzrazMutator extends MutatingStrategy {

	private List<Integer> allAllowedNumbers;

	public IzrazMutator(final List<Integer> allAllowedNumbers) {
		this.allAllowedNumbers = allAllowedNumbers;
	}

	@Override
	public Unit mutate(Unit unit) {
		Izraz izraz = (Izraz) unit.duplicate();
		for (int i = 0; i < 2; i++) {
			switch (ThreadLocalRandom.current().nextInt(4)) {
			case 0:
				// swap two numbers in expression
//				 System.out.println("svap operands " + izraz);
				swapOperands(izraz);

//				 System.out.println("svap operands mutated" + izraz);
				break;
			case 1:
//				 System.out.println("re roll operation " + izraz);
				// re roll operation
				reRollOperation(izraz);
//				 System.out.println("re roll operation mutated " + izraz);

				break;
			case 2:
//				 System.out.println("remove operation " + izraz);
				// change one number with new number in expression
				removeOperation(izraz);
//				 System.out.println("remove operation mutated" + izraz);

				break;
			case 3:
//				System.out.println("set operand " + izraz);
				setOperand(izraz);
//				System.out.println("set operand mutated" + izraz);

				break;
			}
		}

		return izraz;
	}

	private void setOperand(Izraz izraz) {
		List<Integer> operands = izraz.getOperands();
		List<Integer> allowedOperands = new ArrayList<>(this.allAllowedNumbers);
		for (int i = 0; i < operands.size(); i++) {
			allowedOperands.remove(operands.get(i));
		}

		if (!allowedOperands.isEmpty()) {
			Integer operandToAdd = allowedOperands.get(ThreadLocalRandom.current().nextInt(allowedOperands.size()));
			izraz.setOperandValue(ThreadLocalRandom.current().nextInt(operands.size()), operandToAdd);
		}
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
