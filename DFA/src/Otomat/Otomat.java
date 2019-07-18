package Otomat;

import Otomat.State;

import java.util.ArrayList;

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
	
	public void kiemTraXau(String inputString){
		kiemTraInput(inputString);
		
		currentState = startState;
		charCount = 0;
		currentChar = nextChar(inputString);
		
		while (currentChar != '\0') {
			currentState = nextState(currentState, currentChar);
			if (cannotMove) break;
			currentChar = nextChar(inputString);
		}
		
		if(currentState.getIsFinalState() && !cannotMove) {
			System.out.println("Ket qua: Yes");
			cannotMove = false;
		}
		else if (!currentState.getIsFinalState() || cannotMove) {
			System.out.println("Ket qua: No");
			cannotMove = false;
		}
	}
	
	public void kiemTraInput(String inputString) {
		charCount = 0;
		currentChar = nextChar(inputString);
		
		while(currentChar != '\0') {
			if (listInput.contains(currentChar))
				currentChar = nextChar(inputString);
			else {
				System.out.println("Xau input co ki tu khong thuoc alphabet");
				break;
			}
		}
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
		System.out.println("Thong tin Otomat: "+otomatName);
		System.out.print("alphabet: ");
		for (int i = 0 ; i < listInput.size(); i++)
			System.out.print(listInput.get(i).toString()+" ");
		System.out.println();
		System.out.println("Trang thai bat dau: "+startState.getName());
		
		for(int i =0; i<listState.size(); i++) {
			State state = listState.get(i);
			System.out.println("- id: "+state.getID()
							  +"  name: "+state.getName()
							  +"  x: "+state.getPoint().getX()
							  +"  y: "+state.getPoint().getY()
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
