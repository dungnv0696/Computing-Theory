package EQ_DFA;

import java.awt.Point;

public class Transition {
	private char input;
	private State nextState;
	private String nextStateName;
	private State currentState;
	private Point transPoint;
	
	public Transition(char input, State currentState, String nextStateName) {
		this.input = input;
		this.nextStateName = nextStateName;
		this.currentState = currentState;
	}
	
	public Transition(char input,State currentState, State nextState) {
		this.input = input;
		this.nextState = nextState;
		this.currentState = currentState;
		//this.nextStatePoint = nextStatePoint;
	}
	
	public void setInput(char input) {
		this.input = input;
	}
	
	public char getInput() {
		return input;
	}
	
	public void setNextState(State nextState) {
		this.nextState = nextState;
	}
	
	public State getNextState() {
		return nextState;
	}
	
	public void setNextStateName(String nextStateName) {
		this.nextStateName = nextStateName;
	}
	
	public String getNextStateName() {
		return nextStateName;
	}
	
	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}
	
	public State getCurrentState() {
		return currentState;
	}
	
	public void setTransPoint (Point transPoint) {
		this.transPoint = transPoint;
	}
	
	public Point getTransPoint () {
		return transPoint;
	}
}
