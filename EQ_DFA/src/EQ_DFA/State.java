package EQ_DFA;

import java.awt.Point;
import java.util.ArrayList;

public class State {
	private boolean isFinalState;
	private boolean isStartState;
	private String id;
	private String name;
	private Point point;
	private boolean isMarked;
	private ArrayList<Transition> transition;
	
	
	public State (String id, String name) {
		this.id = id;
		this.name = name;
		//this.point = point;
		//this.isFinalState = isFinalState;
		this.transition = new ArrayList<Transition>();
		this.isMarked = false;
	}
	
	
	public String getID(){
		return id;
	}
	
	public void setID(String id){
		this.id = id;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setIsFinalState(boolean isFinalState) {
		this.isFinalState = isFinalState;
	}
	
	public boolean getIsFinalState() {
		return isFinalState;
	}
	
	public void setIsStartState(boolean isStartState) {
		this.isStartState = isStartState;
	}
	
	public boolean getIsStartState() {
		return isStartState;
	}
	
	public void setPoint(Point point) {
		this.point = point;
	}
	
	public void setPoint(double x, double y) {
		Point point = new Point();
		point.setLocation(x, y);
		this.point = point;
	}
	
	public Point getPoint() {
		return point;
	}
	
	public ArrayList<Transition> getTransition(){
		return transition;
	}
	
	public void setIsMarked(boolean isMarked) {
		this.isMarked = isMarked;
	}
	
	public boolean getIsMarked() {
		return isMarked;
	}
	
	public void addTransitionToArray(char input, State currentState, State nextState) {
		this.transition.add(new Transition(input, currentState ,nextState));
	}
}
