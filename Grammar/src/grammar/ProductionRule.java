package grammar;

import java.util.ArrayList;

public class ProductionRule {
	ArrayList<String> N;
	ArrayList<String> V;

	public ProductionRule(ArrayList<String> N, ArrayList<String> V) {
		this.N = N;
		this.V = V;
	}

	public String toString() {
		String str = "";
		for (String s : N) {
			str += s + " ";
		}
		str += "->";
		for (String s : V) {
			str += " " + s;
		}
		return str;
	}
}
