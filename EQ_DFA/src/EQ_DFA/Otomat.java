package EQ_DFA;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


public class Otomat {
	protected String otomatName;
	protected ArrayList<State> listState;
	protected State startState;
	protected ArrayList<Character> listInput;
	protected ArrayList<State> listFinalState;
	
	public static char currentChar;
	public static State currentState;
	public int charCount;
	public boolean cannotMove;
	
	public Otomat() {	
	}
	
	public Otomat(ArrayList<State> listState, State startState) {
		this.listState = listState;
		this.startState = startState;
	}
	
	public State nextState(State currentState, char currentChar) {
		for (int i = 0 ; i < currentState.getTransition().size(); i++) {
			Transition trans = currentState.getTransition().get(i);
			if (trans.getInput() == currentChar)
					return trans.getNextState();
		}	
		cannotMove = true;
		return currentState;
	}
	
	public char nextChar(String inputString) {
		if (inputString.isEmpty() || charCount == inputString.length())
			return '\0';
		else 
			return inputString.charAt(charCount++);
	}	
	
	public void inThongTinOtomat() {
		//System.out.println("Thong tin Otomat: "+otomatName);
		//System.out.print("alphabet: ");
		//for (int i = 0 ; i < listInput.size(); i++)
			//System.out.print(listInput.get(i).toString()+" ");
		//System.out.println();
		System.out.println("Trang thai bat dau: "+startState.getName());
		
		for(int i =0; i<listState.size(); i++) {
			State state = listState.get(i);
			System.out.println("- id: "+state.getID()
							  +"  name: "+state.getName()
							  //+"  x: "+state.getPoint().getX()
							  //+"  y: "+state.getPoint().getY()
							  +"  => Final: "+state.getIsFinalState()
							  +"  => Start: "+state.getIsStartState());
			for(int j =0; j<state.getTransition().size(); j++) {
				Transition tran = state.getTransition().get(j);
				System.out.println("  + input: "+tran.getInput()
								  +"  from: "+tran.getCurrentState().getID()
								  +"  to: "+tran.getNextState().getID());
			}
		}
		System.out.println();
	}
	
	public boolean EDFA() {
		Queue<State> OPEN = new LinkedList<State>();
		ArrayList<State> CLOSE = new ArrayList<State>();
		
		OPEN.offer(startState);
		
		while(!OPEN.isEmpty()) {
			State state = OPEN.poll();
			CLOSE.add(state);
			
			for(int i = 0 ; i <state.getTransition().size(); i++) {
				Transition trans = state.getTransition().get(i);
				State nextState = trans.getNextState();
				if(!CLOSE.contains(nextState)) {
					OPEN.offer(nextState);
				}
			}
		}
		
		for(int i = 0 ; i < getListState().size(); i++) {
			State state = getListState().get(i);
			if(state.getIsFinalState() == true)
				if(!CLOSE.contains(state))
					continue;
				else if(CLOSE.contains(state)) {
					return false;
				}
		}	
		return true;
	}
	
	
	
	public void setStartState(State startState) {
		this.startState = startState;
	}
	
	public State getStartState() {
		return startState;
	}
	
	public void setListState(ArrayList<State> listState) {
		this.listState = listState;
	}
	
	public ArrayList<State> getListState(){
		return listState;
	}
	
	public void setListFinalState(ArrayList<State> listFinalState) {
		this.listFinalState = listFinalState;
	}
	
	public ArrayList<State> getListFinalState(){
		return listFinalState;
	}
	
	public void setListInput(ArrayList<Character> listInput) {
		this.listInput = listInput;
	}
	
	public ArrayList<Character> getListInput(){
		return listInput;
	}
	
	public void setOtomatName(String otomatName) {
		this.otomatName = otomatName;
	}
	
	public String getOtomatName() {
		return otomatName;
	}
}
