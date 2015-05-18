/**
 * Recursive descent parser example from the textbook.
 * 
 * ----
 * S -> A C
 * C -> c
 *    | epsilon
 * A -> a B C d
 *    | B Q
 * B -> b B
 *    | epsilon
 * Q -> q
 *    | epsilon
 * ----
 *
 */
public class RecursiveDescent {
	String input;
	int position = 0;
	RecursiveDescent(String input) {
		this.input = input;
	}
	public boolean parse() {
		S();
		return position == input.length();
	}
	private char peek() {
		if (position == input.length())
			return '\0';
		return input.charAt(position);
	}
	private void eat(char letter) {
		if (letter == input.charAt(position)) {
			position++;
		} else {
			throw new RuntimeException("Error! Bad things!");
		}
	}
	// S -> A C
	private void S() {
		A();
		C();
	}
	// C -> c
	// C -> epsilon
	private void C() {
		if (peek() == 'c') {
			eat('c');
		}
	}
	// A -> a B C d
	// A -> B Q
	private void A() {
		switch (peek()) {
		case 'a':
			eat('a');
			B();
			C();
			eat('d');
			break;
		case 'b':
		case 'q':
			B();
			Q();
			break;
		}
	}
	// B -> b B
	//    | epsilon
	private void B() {
		if (peek() == 'b') {
			eat('b');
			B();
		}
	}
	// Q -> q
	//    | epsilon
	private void Q() {
		if (peek() == 'q') {
			eat('q');
		}
	}
	public static void main(String[] args) {
		RecursiveDescent parser = new RecursiveDescent("abbbd");
		System.out.println(parser.parse());
	}
}
