package com.codlex.raf.geneticalgorithm.homework2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

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
				return Integer.MIN_VALUE;
			} catch (ClassCastException e) {
				return Integer.MIN_VALUE;
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

			return 1 + this.leftGroup.countOperands() + this.rightGroup.countOperands();
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
				this.value = Math.random() < 0.5 ? this.leftGroup.getRandomOperand() : this.rightGroup.getRandomOperand();
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
	}

	private final Operation topOperation;

	public Izraz(final List<Integer> numbers) {
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

	@Override
	public String toString() {
		return this.topOperation.toString();
	}

	@Override
	public Collection<Unit> makeLoveWith(Unit secondParent) {
		List<Unit> children = new ArrayList<>();
		children.add(this);
		children.add(secondParent);
		return children;
	}

	@Override
	public int distanceTo(Unit other) {
		Izraz otherIzraz = (Izraz) other;
		return Math.abs(evaluate() - otherIzraz.evaluate());
	}

	@Override
	public Unit duplicate() {
		return null;
	}

	public Izraz(int value) {
		this.topOperation = new Operation(value);
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
}
