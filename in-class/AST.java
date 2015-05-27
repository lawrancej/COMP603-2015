import java.util.HashMap;
import java.util.Map;

public class AST {
	public interface Node {
		<T> T accept (Visitor<T> v);
	}
	public interface Visitor<T> {
		// Statements go here
		T visit (Assign assign);
		T visit (Block block);
		T visit (Branch branch);
		T visit (Loop loop);
		// Expressions
		T visit (Id id);
		T visit (Operator op);
		T visit (Number num);
	}
	/* Statement nodes */
	public interface Statement extends Node {}
	public static class Assign implements Statement {
		Id variable; Expression value;
		public Assign (Id variable, Expression value) { this.variable = variable; this.value = value; }
		public <T> T accept(Visitor<T> v) { return v.visit(this); }		
	}
	public static class Block implements Statement {
		Statement[] statements;
		public Block (Statement ... statements) { this.statements = statements; }
		public <T> T accept(Visitor<T> v) { return v.visit(this); }		
	}
	public static class Branch implements Statement {
		Expression predicate; Statement ifBranch; Statement elseBranch;
		public Branch (Expression p, Statement a, Statement b) { predicate = p; ifBranch = a; elseBranch = b; }
		public <T> T accept(Visitor<T> v) { return v.visit(this); }		
	}
	public static class Loop implements Statement {
		Expression predicate; Statement body;
		public Loop (Expression p, Statement body) { predicate = p; this.body = body; }
		public <T> T accept(Visitor<T> v) { return v.visit(this); }
	}
	/* Expression nodes */
	public interface Expression extends Node {}
	public static class Id implements Expression {
		String id;
		public Id (String id) { this.id = id; }
		public <T> T accept(Visitor<T> v) { return v.visit(this); }		
	}
	public static enum Op {
		MULTIPLY, DIVIDE, SUBTRACT, ADD;
		public char show() {
			final String operators = "*/-+";
			return operators.charAt(this.ordinal());
		}
	};
	public static class Operator implements Expression {
		final Op op;
		final Expression left;
		final Expression right;
		private Operator (Op op, Expression left, Expression right) {
			this.op = op;
			this.left = left;
			this.right = right;
		}
		public <T> T accept(Visitor<T> v) { return v.visit(this); }		
	}
	public static class Number implements Expression {
		int n;
		public Number (int n) { this.n = n; }
		public <T> T accept(Visitor<T> v) { return v.visit(this); }		
	}
	/* Builders */
	public static Assign assign (Id var, Expression val) { return new Assign (var, val); }
	public static Block block (Statement... statements) { return new Block (statements); }
	public static Branch branch (Expression predicate, Statement ifBranch, Statement elseBranch) { return new Branch (predicate, ifBranch, elseBranch); }
	public static Loop loop (Expression predicate, Statement body) { return new Loop (predicate, body); }
	public static Id id (String id) { return new Id (id); }
	public static Operator plus (Expression left, Expression right) { return new Operator (Op.ADD, left, right); }
	public static Operator minus (Expression left, Expression right) { return new Operator (Op.SUBTRACT, left, right); }
	public static Operator times (Expression left, Expression right) { return new Operator (Op.MULTIPLY, left, right); }
	public static Operator dividedBy (Expression left, Expression right) { return new Operator (Op.DIVIDE, left, right); }
	public static Number number (int n) { return new Number (n); }
	/* Visitors */
	public static class ExpressionInterpreter implements Visitor<Integer> {
		Map<String, Integer> symbols;
		public ExpressionInterpreter(Map<String, Integer> symbols) {
			this.symbols = symbols;
		}
		public Integer visit(Id id) {
			if (symbols.containsKey(id.id))
				return symbols.get(id.id);
			else return 0;
		}
		public Integer visit(Operator op) {
			switch(op.op) {
			case ADD:      return op.left.accept(this) + op.right.accept(this);
			case DIVIDE:   return op.left.accept(this) / op.right.accept(this);
			case MULTIPLY: return op.left.accept(this) * op.right.accept(this);
			case SUBTRACT: return op.left.accept(this) - op.right.accept(this);
			default:       return null;
			}
		}
		public Integer visit(Number num) {
			return num.n;
		}
		public Integer visit(Loop loop) { return null; }
		public Integer visit(Branch branch) { return null; }
		public Integer visit(Block block) { return null; }
		public Integer visit(Assign assign) { return null; }
	}
	public static class StatementInterpreter implements Visitor<Void> {
		Map<String,Integer> symbols = new HashMap<String, Integer>();
		ExpressionInterpreter eval = new ExpressionInterpreter(symbols);
		public Void visit(Loop loop) {
			while (loop.predicate.accept(eval) != 0)
				loop.body.accept(this);
			return null;
		}
		public Void visit(Branch branch) {
			if (branch.predicate.accept(eval) != 0)
				branch.ifBranch.accept(this);
			else
				branch.elseBranch.accept(this);
			return null;
		}
		public Void visit(Block block) {
			for (Statement s : block.statements)
				s.accept(this);
			return null;
		}
		public Void visit(Assign assign) {
			symbols.put(assign.variable.id, assign.value.accept(eval));
			return null;
		}
		public Void visit(Id id) { return null; }
		public Void visit(Operator op) { return null; }
		public Void visit(Number num) { return null; }
	}
	public static class Printer implements Visitor<StringBuilder> {
		StringBuilder builder = new StringBuilder();
		int tabs = 0;
		public StringBuilder visit(Assign assign) {
			assign.variable.accept(this);
			builder.append(" = ");
			assign.value.accept(this);
			builder.append(";\n");
			return builder;
		}
		public StringBuilder visit(Block block) {
			for (int i = 0; i < tabs; i++) {
				builder.append("    ");
			}
			builder.append("{\n");
			tabs++;
			for (Statement s : block.statements) {
				for (int i = 0; i < tabs; i++) {
					builder.append("    ");
				}
				s.accept(this);
			}
			tabs--;
			for (int i = 0; i < tabs; i++) {
				builder.append("    ");
			}
			builder.append("}\n");
			return null;
		}
		public StringBuilder visit(Branch branch) {
			builder.append("if (");
			branch.predicate.accept(this);
			builder.append(")\n");
			branch.ifBranch.accept(this);
			builder.append("else\n");
			branch.elseBranch.accept(this);
			return builder;
		}
		public StringBuilder visit(Loop loop) {
			builder.append("while (");
			loop.predicate.accept(this);
			builder.append(" != 0)\n");
			loop.body.accept(this);
			return builder;
		}
		public StringBuilder visit(Id id) {
			builder.append(id.id);
			return builder;
		}
		public StringBuilder visit(Operator op) {
			builder.append("(");
			op.left.accept(this);
			builder.append(" ");
			builder.append(op.op.show());
			builder.append(" ");
			op.right.accept(this);
			builder.append(")");
			return builder;
		}
		public StringBuilder visit(Number num) {
			builder.append(num.n);
			return builder;
		}
	}
	public static void main (String[] args) {
		/*
		 * Factorial Program equivalent to:
		 * int factorial = 1;
		 * int i = 5;
		 * while (i != 0) {
		 * 		factorial = factorial * i;
		 * 		i = i - 1;
		 * }
		 */
		Node factorial = block(
				// int factorial = 1;
				assign (id ("factorial"), number(1)),
				// int i = 5;
				assign (id ("i"), number(5)),
				// while (i != 0) {
				loop (id ("i"),
						block(
								// factorial = factorial * i;
								assign (id("factorial"),times(id("factorial"), id("i"))),
								// i = i - 1;
								assign (id ("i"), minus(id("i"), number(1)))))
				// }
				);
		StatementInterpreter runner = new StatementInterpreter();
		factorial.accept(runner);
		Printer printer = new Printer();
		factorial.accept(printer);
		System.out.println(printer.builder.toString());
		/* 
		 * Print the symbols in the interpreter.
		 */
		for (String s : runner.symbols.keySet())
			System.out.format("%s: %d\n", s, runner.symbols.get(s));
	}
}
