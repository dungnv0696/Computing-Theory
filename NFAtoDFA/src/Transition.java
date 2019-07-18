

import java.awt.Point;
import java.util.HashSet;
import java.util.Iterator;

public class Transition {
	private Character input;
	private State nextState;
	private String nextStateLabel;
	private State currentState;
	private HashSet<String> nextStatesSet;
	
	public static Character EPSILON = null;
	
	public Transition() {
		this.nextStatesSet = new HashSet<String>();
	}
	
	public Transition(Character input,State currentState, State nextState) {
		this.input = input;
		this.nextState = nextState;
		this.currentState = currentState;
		this.nextStatesSet = new HashSet<String>();
	}	
	
	public void setInput(Character input) {
		this.input = input;
	}
	
	public Character getInput() {
		return input;
	}
	
	public void setNextState(State nextState) {
		this.nextState = nextState;
	}
	
	public State getNextState() {
		return nextState;
	}
	
	public void setNextStateLabel(String nextStateLabel) {
		this.nextStateLabel = nextStateLabel;
	}
	
	public void setNextStateLabel(HashSet<String> nextStatesSet) {
		if(!nextStatesSet.isEmpty()) {
			setNextStateLabel("");
			Iterator<String> iterator = nextStatesSet.iterator();
			while(iterator.hasNext()) {
				String currentStateID = iterator.next();
				setNextStateLabel(getNextStateLabel().concat(","+currentStateID));
			}		
			setNextStateLabel(getNextStateLabel().substring(1));
		}
		else { 
			setNextStateLabel("null");
		}
	}
	
	public String getNextStateLabel() {
		return nextStateLabel;
	}
	
	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}
	
	public State getCurrentState() {
		return currentState;
	}
	
	public void setNextStatesSet(HashSet<String> nextStatesSet) {
		this.nextStatesSet = nextStatesSet;
	}
	
	public void addAllNextStatesSet(HashSet<String> nextStatesSet) {
		this.nextStatesSet.addAll(nextStatesSet);
	}
	
	public HashSet<String> getNextStatesSet(){
		return nextStatesSet;
	}
}
