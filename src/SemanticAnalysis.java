
public class SemanticAnalysis {
	public static void typeMismatch() {
		// Type mismatch
		int a = args;
	}
	public static void unreachable() {
		while(true) {
			
		}
		// Unreachable code
		System.out.println("hey!");
	}
	public static void main(String[] args) {
		// Dead code
		if (false) {
			System.out.println("wtf");
		}
	}
}
