package com.codlex.raf.geneticalgorithm.homework2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.codlex.raf.geneticalgorithm.core.Unit;
import com.codlex.raf.geneticalgorithm.homework2.Izraz.Operation.OperationType;

public class Izraz extends Unit {

	public static class Operation {

		private static final ScriptEngine EVALUATOR;
		static {
			ScriptEngineManager mgr = new ScriptEngineManager();
			EVALUATOR = mgr.getEngineByName("JavaScript");
		}

		public static enum OperationType {
			Addition("+", 1), Substraction("-", 1), Multipliatio("*", 2), Division("/", 2), Value("", 10);

			private String symbol;
			private int presence;

			private OperationType(String symbol, int presence) {
				this.symbol = symbol;
				this.presence = presence;
			}

			@Override
			public String toString() {
				return this.symbol;
			}

			public boolean isStrongerThan(OperationType type) {
				return this.presence > type.presence;
			}

			public static OperationType getRandom() {
				OperationType type = values()[ThreadLocalRandom.current().nextInt(0, values().length - 1)]; // without
																											// value
				return type;
			};

		}

		private OperationType operation;
		private Operation parent;
		private Operation leftGroup;
		private Operation rightGroup;
		private int value;

		public Operation(Operation parent) {
			this.parent = parent;
		}

		public Operation(int value) {
			this.operation = OperationType.Value;
			this.value = value;
		}

		public void randomizeWith(List<Integer> allowedNumbers, int takeNumbers) {
			if (takeNumbers == 1) {
				this.operation = OperationType.Value;
				int whichNumber = ThreadLocalRandom.current().nextInt(0, allowedNumbers.size());
				this.value = allowedNumbers.remove(whichNumber);
			} else {
				this.operation = OperationType.getRandom();
				int leftNumbers = ThreadLocalRandom.current().nextInt(1, takeNumbers);
				this.leftGroup = new Operation(this);
				this.leftGroup.randomizeWith(allowedNumbers, leftNumbers);

				int rightNumbers = takeNumbers - leftNumbers;
				this.rightGroup = new Operation(this);
				this.rightGroup.randomizeWith(allowedNumbers, rightNumbers);
			}
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();

			if (this.operation == OperationType.Value) {
				builder.append(this.value);
			} else {
				// TODO: brackets
				boolean brackets = this.parent != null && this.parent.operation.isStrongerThan(this.operation);
				if (brackets)
					builder.append("(");
				builder.append(this.leftGroup.toString());
				builder.append(this.operation);
				builder.append(this.rightGroup.toString());
				if (brackets)
					builder.append(")");
			}

			return builder.toString();
		}

		public static Operation bracketTest() {
			Operation operation = new Operation(null);
			operation.operation = OperationType.Multipliatio;
			operation.leftGroup = new Operation(operation);
			operation.leftGroup.operation = OperationType.Addition;
			operation.leftGroup.leftGroup = new Operation(operation.leftGroup);
			operation.leftGroup.leftGroup.operation = OperationType.Value;
			operation.leftGroup.rightGroup = new Operation(operation.leftGroup);
			operation.leftGroup.rightGroup.operation = OperationType.Value;
			operation.rightGroup = new Operation(operation);
			operation.rightGroup.operation = OperationType.Value;
			 
			return operation;
		}

		public int evalate() {
			try {
				return (Integer) EVALUATOR.eval(toString());
			} catch (ScriptException e) {
				return Integer.MIN_VALUE + 1;
			} catch (ClassCastException e) {
				return Integer.MIN_VALUE + 1;
			}
		}

		public int countOperands() {
			if (this.operation == OperationType.Value) {
				return 1;
			} else {
				return this.leftGroup.countOperands() + this.rightGroup.countOperands();
			}
		}

		public Integer getOperandValue(AtomicInteger currentIndex, int operandIndex) {
			if (this.operation == OperationType.Value) {
				int index = currentIndex.getAndIncrement();
				if (index == operandIndex) {
					return this.value;
				} else {
					return null;
				}
			} else {
				Integer value = this.leftGroup.getOperandValue(currentIndex, operandIndex);
				if (value == null) {
					value = this.rightGroup.getOperandValue(currentIndex, operandIndex);
				}
				return value;
			}
		}

		public Integer setOperandValue(AtomicInteger currentIndex, int operandIndex, int newValue) {
			if (this.operation == OperationType.Value) {
				int index = currentIndex.getAndIncrement();
				if (index == operandIndex) {
					int oldValue = this.value;
					this.value = newValue;
					return oldValue;
				} else {
					return null;
				}
			} else {
				Integer value = this.leftGroup.setOperandValue(currentIndex, operandIndex, newValue);
				if (value == null) {
					value = this.rightGroup.setOperandValue(currentIndex, operandIndex, newValue);
				}
				return value;
			}
		}

		public int countOperations() {
			if (this.operation == OperationType.Value) {
				return 0;
			}

			return 1 + this.leftGroup.countOperations() + this.rightGroup.countOperations();
		}

		public OperationType setOperation(AtomicInteger currentIndex, int operationIndex, OperationType newValue) {
			if (this.operation == OperationType.Value) {
				return null;
			}

			int index = currentIndex.getAndIncrement();
			if (index == operationIndex) {
				OperationType oldValue = this.operation;
				this.operation = newValue;
				return oldValue;
			} else {
				OperationType value = this.leftGroup.setOperation(currentIndex, operationIndex, newValue);
				if (value == null) {
					value = this.rightGroup.setOperation(currentIndex, operationIndex, newValue);
				}
				return value;
			}
		}

		public OperationType removeOperation(final AtomicInteger currentIndex, int operationIndex) {
			if (this.operation == OperationType.Value) {
				return null;
			}

			int index = currentIndex.getAndIncrement();
			if (index == operationIndex) {
				OperationType oldValue = this.operation;
				this.operation = OperationType.Value;
				this.value = Math.random() < 0.5 ? this.leftGroup.getRandomOperand()
						: this.rightGroup.getRandomOperand();
				this.leftGroup.parent = null;
				this.rightGroup.parent = null;
				this.leftGroup = null;
				this.rightGroup = null;
				return oldValue;
			} else {
				OperationType value = this.leftGroup.removeOperation(currentIndex, operationIndex);
				if (value == null) {
					value = this.rightGroup.removeOperation(currentIndex, operationIndex);
				}
				return value;
			}
		}

		private int getRandomOperand() {
			if (this.operation == OperationType.Value) {
				return this.value;
			}

			if (Math.random() < 0.5) {
				return this.leftGroup.getRandomOperand();
			} else {
				return this.rightGroup.getRandomOperand();
			}
		}

		public Operation duplicate(final Operation parent) {
			Operation operation = new Operation(parent);
			operation.operation = this.operation;
			if (this.operation != OperationType.Value) {
				operation.leftGroup = this.leftGroup.duplicate(operation);
				operation.rightGroup = this.rightGroup.duplicate(operation);
			} else {
				operation.value = this.value;
			}
			return operation;
		}

		private void getOperands(List<Integer> operands) {
			if (this.operation == OperationType.Value) {
				operands.add(this.value);
				return;
			}
			this.leftGroup.getOperands(operands);
			this.rightGroup.getOperands(operands);
		}

		public List<Integer> getOperands() {
			final List<Integer> operands = new ArrayList<>();
			getOperands(operands);
			return operands;
		}

		public Operation getOperationObject(final AtomicInteger currentIndex, final int operationIndex) {
			if (this.operation == OperationType.Value) {
				return null;
			}

			int index = currentIndex.getAndIncrement();
			if (index == operationIndex) {
				return this;
			} else {
				Operation value = this.leftGroup.getOperationObject(currentIndex, operationIndex);
				if (value == null) {
					value = this.rightGroup.getOperationObject(currentIndex, operationIndex);
				}
				return value;
			}
		}

		public boolean setOperationObject(final AtomicInteger atomicInteger, int toReplace, Operation operationObject) {
			if (this.operation == OperationType.Value) {
				return false;
			}

			int currentIndex = atomicInteger.getAndIncrement();
			if (currentIndex == toReplace) {
				return true;
			}

			boolean shouldReplace = this.leftGroup.setOperationObject(atomicInteger, toReplace, operationObject);
			if (shouldReplace) {
				this.leftGroup = operationObject.duplicate(this);
				return false;
			}
			shouldReplace = this.rightGroup.setOperationObject(atomicInteger, toReplace, operationObject);
			if (shouldReplace) {
				this.rightGroup = operationObject.duplicate(this);
				return false;
			}

			return false;
		}
	}

	private Operation topOperation;
	private final List<Integer> allAllowedOperands;

	public Izraz(final List<Integer> numbers) {
		this.allAllowedOperands = new ArrayList<>(numbers);
		int takeNumbers = ThreadLocalRandom.current().nextInt(1, numbers.size() + 1);
		this.topOperation = new Operation(null);
		this.topOperation.randomizeWith(numbers, takeNumbers);
	}

	public static Izraz generate(final List<Integer> numbers) {
		return new Izraz(new ArrayList<>(numbers));
	}

	public int evaluate() {
		return this.topOperation.evalate();
	}

	public List<Integer> getOperands() {
		return this.topOperation.getOperands();
	}
	@Override
	public String toString() {
		return this.topOperation.toString();
	}

	public static void main(String[] args) {

		List<Integer> b = new ArrayList<>();
		b.add(1);
		b.add(2);
		b.add(2);

		List<Integer> a = new ArrayList<>();
		a.add(1);
		a.add(2);
		b.removeAll(a);

		System.out.println(b);
	}

	private static class Replacement {
		int toReplace;
		int replacement;
		boolean reverse;

		public Replacement(int toReplace, int replacement, boolean reverse) {
			this.reverse = reverse;
			this.toReplace = toReplace;
			this.replacement = replacement;
		}

		public Izraz born(Izraz firstIzraz, Izraz secondIzraz) {
			if (this.reverse) {
				Izraz tmp = firstIzraz;
				firstIzraz = secondIzraz;
				secondIzraz = tmp;
			}

//			System.out.println("Replacing: " + firstIzraz.getOperationObject(this.toReplace) + " with "
//					+ secondIzraz.getOperationObject(this.replacement));
//			System.out.println("ToRepalce: " + this.toReplace + " with " + this.replacement);
			Izraz firstChildClone = (Izraz) firstIzraz.duplicate();
			firstChildClone.setOperationObject(this.toReplace, secondIzraz.getOperationObject(this.replacement));

			return firstChildClone;
		}
	}

	@Override
	public Collection<Unit> makeLoveWith(Unit secondParent) {
		// dozvoljeni su: (svi - iskoristeni_u_prvom + oni_iz_podstabla)
		Izraz secondParentIzraz = (Izraz) secondParent;

		List<Replacement> replacements = new ArrayList<>();
		replacements.addAll(makeChildren(this, secondParentIzraz, false));
		replacements.addAll(makeChildren(secondParentIzraz, this, true));
		
		//System.out.println("Replacements: " + replacements.size());
		
		List<Unit> children = new ArrayList<>();

		if (replacements.size() >= 2) {
			Replacement firstChild = replacements.get(ThreadLocalRandom.current().nextInt(replacements.size()));
			children.add(firstChild.born(this, secondParentIzraz));
			replacements.remove(firstChild);

			Replacement secondChild = replacements.get(ThreadLocalRandom.current().nextInt(replacements.size()));
			children.add(secondChild.born(this, secondParentIzraz));

		} else {
			children.add(this);
			children.add(secondParent);
		}

		return children;
	}

	private static List<Replacement> makeChildren(Izraz firstParentIzraz, Izraz secondParentIzraz, boolean reverse) {
		List<Integer> allFirstParentOperands = firstParentIzraz.topOperation.getOperands();

		List<Replacement> replacements = new ArrayList<>();
		int operations = firstParentIzraz.topOperation.countOperations();
		for (int i = 1; i < operations; i++) {
			Operation operation = firstParentIzraz.getOperationObject(i);
			List<Integer> allowedOperands = new ArrayList<>();
			allowedOperands.addAll(firstParentIzraz.allAllowedOperands); // initialize
																			// with
																			// all
			// operands
			for (Integer operand : allFirstParentOperands) {
				allowedOperands.remove(operand);
			}
			allowedOperands.addAll(operation.getOperands()); // add ones we will
																// swap out

			int secondParentOperations = secondParentIzraz.countOperations();
			for (int j = 0; j < secondParentOperations; j++) {
				Operation secondOperation = secondParentIzraz.getOperationObject(j);
				if (secondOperation == null) {
					System.out.println(secondParentIzraz);
				}
				List<Integer> neededOperands = secondOperation.getOperands();
				if (firstParentIzraz.canBeReplaced(allowedOperands, secondOperation.getOperands())) {
					replacements.add(new Replacement(i, j, reverse));
//					System.out.println("replaced " + operation + " with " + secondOperation + " allowed: "
//							+ allowedOperands + " needed: " + neededOperands);
				}
			}
		}
		return replacements;
	}

	private void setOperationObject(int toReplace, Operation operationObject) {
		boolean shouldSet = this.topOperation.setOperationObject(new AtomicInteger(), toReplace, operationObject);
		if (shouldSet) {
			this.topOperation = operationObject.duplicate(null);
		}
	}

	private boolean canBeReplaced(List<Integer> allowedOperands, List<Integer> operands) {
		Map<Integer, Integer> allowedHash = new HashMap<>();
		for (Integer allowedOperand : allowedOperands) {
			Integer count = allowedHash.get(allowedOperand);
			if (count == null) {
				count = 0;
			}
			count++;
			allowedHash.put(allowedOperand, count);
		}

		for (Integer operand : operands) {
			Integer count = allowedHash.get(operand);
			if (count == null || count == 0) {
				return false;
			}
			count--;
			allowedHash.put(operand, count);
		}
		return true;
	}

	private Operation getOperationObject(int index) {
		return this.topOperation.getOperationObject(new AtomicInteger(), index);
	}

	@Override
	public int distanceTo(Unit other) {
		Izraz otherIzraz = (Izraz) other;
		return Math.abs(evaluate() - otherIzraz.evaluate());
	}

	@Override
	public Unit duplicate() {
		return new Izraz(this.topOperation.duplicate(null), this.allAllowedOperands);
	}

	public Izraz(Operation topOperation, List<Integer> allAllowedNumbers) {
		this.topOperation = topOperation;
		this.allAllowedOperands = allAllowedNumbers;
	}

	public Izraz(int value) {
		this.topOperation = new Operation(value);
		this.allAllowedOperands = null; // not pairable so it is ok
	}

	public int countOperands() {
		return this.topOperation.countOperands();
	}

	public int getOperandValue(int operandIndex) {
		return this.topOperation.getOperandValue(new AtomicInteger(), operandIndex);
	}

	public void setOperandValue(int operandIndex, int value) {
		this.topOperation.setOperandValue(new AtomicInteger(), operandIndex, value);
	}

	public int countOperations() {
		return this.topOperation.countOperations();
	}

	public void setOperation(int operation, OperationType type) {
		this.topOperation.setOperation(new AtomicInteger(), operation, type);
	}

	public void removeOperation(int operationIndex) {
		this.topOperation.removeOperation(new AtomicInteger(), operationIndex);
	}

	@Override
	public String getKey() {
		return toString();
	}
}
