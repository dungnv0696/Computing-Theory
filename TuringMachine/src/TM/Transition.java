package TM;

import java.awt.Point;

public class Transition {
	private Character input;
	private State nextState;
	private String nextStateName;
	private State currentState;
	private Point transPoint;
	private Character write;
	private char move;
	
	public Transition(Character input, State currentState, String nextStateName) {
		this.input = input;
		this.nextStateName = nextStateName;
		this.currentState = currentState;
	}
	
	public Transition(Character input, Character write, char move, State currentState, State nextState) {
		this.input = input;
		this.nextState = nextState;
		this.currentState = currentState;
		//this.nextStatePoint = nextStatePoint;
	}
	
	public void setInput(Character input) {
		this.input = input;
	}
	
	public Character getInput() {
		return input;
	}
	
	public void setWrite(Character write) {
		this.write = write;
	}
	
	public Character getWrite() {
		return write;
	}
	public void setMove(Character move) {
		this.move = move;
	}
	
	public char getMove() {
		return move;
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
