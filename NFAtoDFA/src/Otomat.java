import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Otomat {
	protected String otomatName;
	protected ArrayList<State> listState;
	protected State startState;
	protected ArrayList<Character> listInput;
	protected ArrayList<State> listFinalState;
	
	public static boolean haveEPSILON;	
	public Otomat() {
		
	}
	
	public Otomat(ArrayList<State> listState, State startState) {
		this.listState = listState;
		this.startState = startState;
	}
		
	public Otomat convertNFAtoDFA() {
		Otomat DFA = new Otomat();
		DFA.setOtomatName("DFA.jff");
		
		//Xac dinh trang thai bat dau va tap tat ca cac trang thai co the chuyen tu trang thai bat dau bang EPSILON
		HashSet<String> startStateSet = new HashSet<String>(); 
		startStateSet.add(getStartState().getID());
		startStateSet.addAll(statesReachableFrom(getStartState().getID()));
		
		Queue<HashSet<String>> OPEN = new LinkedList<HashSet<String>>();
		OPEN.offer(startStateSet);
		HashSet<HashSet<String>> CLOSE = new HashSet<HashSet<String>>();
		
		int id = -1;
		ArrayList<State> newStateList = new ArrayList<State>();
		boolean haveStartState = false;
		while(!OPEN.isEmpty()) {
			HashSet<String> setStateID = OPEN.poll();
			CLOSE.add(setStateID);
			
			
			//Khoi tao State moi cua DFA
			State newDFAState = new State(Integer.toString(++id), "q"+id);
			newDFAState.setLabel("");
			for (int i = 0 ; i < listInput.size(); i++) {
				Transition trans = new Transition();
				trans.setInput(listInput.get(i));
				trans.setCurrentState(newDFAState);
				newDFAState.getTransition().add(trans);
			}
			if(!haveStartState) {
				newDFAState.setIsStartState(true);
				haveStartState = true;
			}
			
			Iterator<String> iterator = setStateID.iterator();
			while (iterator.hasNext()) {
				String currentStateID = iterator.next();
				for (int i = 0 ; i < listState.size(); i++)
					if (currentStateID.equals(listState.get(i).getID())) {
						State currentNFAState = listState.get(i);
						
						//Cai dat label
						newDFAState.setLabel(newDFAState.getLabel().concat(","+currentNFAState.getID()));
						
						//Cai dat trang thai bat dau va ket thuc cua State moi dua tren State cu
						if(currentNFAState.getIsFinalState())
							newDFAState.setIsFinalState(true);
						
						//Cai dat Transition cho State moi 
						for (int j = 0; j < listInput.size(); j++) {
							HashSet<String> nextStateSet = (statesReachableOn(currentStateID, listInput.get(j)));
							if(!nextStateSet.isEmpty())
								newDFAState.getTransition().get(j).addAllNextStatesSet(nextStateSet);
						}						
						
						//Cai dat toa do cho State moi
						double x = Math.random()*500.0;
						double y = Math.random()*500.0; 
						newDFAState.setPoint(x, y);
					}
			}
			
			System.out.println(newDFAState.getLabel());
			
			for (int i = 0 ; i < listInput.size(); i++) {
				Transition trans = newDFAState.getTransition().get(i);
				trans.setNextStateLabel(trans.getNextStatesSet());
			}
			
			newDFAState.setLabel(newDFAState.getLabel().substring(1));
			newStateList.add(newDFAState);
			
			//Duyet theo chieu rong de tim cac State co the di den tu trang thai bat dau va them vao OPEN
			for (int i = 0 ; i < listInput.size(); i++) {
				HashSet<String> nextStatesSet = newDFAState.getTransition().get(i).getNextStatesSet();		    
				if (!CLOSE.contains(nextStatesSet) && !OPEN.contains(nextStatesSet) &&!nextStatesSet.isEmpty())
					OPEN.offer(nextStatesSet);
			}
			
		}
		
		State nullState = new State(Integer.toString(++id),null);
		nullState.setLabel("null");
		nullState.setPoint(Math.random()*500.0, Math.random()*500.0);
		boolean haveNullState = false;
		for(int i = 0; i < listInput.size(); i++) {
			Transition trans = new Transition(listInput.get(i), nullState, nullState);
			nullState.getTransition().add(trans);
		}
		for (int i = 0 ; i < newStateList.size() ; i++) {
			State stateTemp = newStateList.get(i);
			for (int j = 0 ; j < stateTemp.getTransition().size(); j++) {
				Transition transTemp = stateTemp.getTransition().get(j);
				for (int k = 0; k < newStateList.size(); k++) {
					if (newStateList.get(k).getLabel().equals(transTemp.getNextStateLabel())) {
						transTemp.setNextState(newStateList.get(k));
					}
					if (transTemp.getNextStateLabel().equals("null")){
						if (!haveNullState) {
							haveNullState = true;
							transTemp.setNextState(nullState);
						}
						else
							transTemp.setNextState(nullState);
					}
				}	
			}
		}
		
		ArrayList<State> newFinalStateList = new ArrayList<State>();
		for (int i = 0; i < newStateList.size(); i++) {
			State stateTemp = newStateList.get(i);
			if (stateTemp.getIsStartState())
				DFA.setStartState(stateTemp);
			if (stateTemp.getIsFinalState()) {
				newFinalStateList.add(stateTemp);
				DFA.setListFinalState(newFinalStateList);
			}
		}
		if(haveNullState)
			newStateList.add(nullState);		
		
		DFA.setListState(newStateList);
		DFA.setListInput(listInput);
		return DFA;
	}
	
	//Tra ve tap trang thai co the den duoc qua inputCharacter
	public HashSet<String> statesReachableOn(String fromID, Character inputChar){
		HashSet<String> reachableSet = new HashSet<String>();
		for (int i = 0 ; i < listState.size(); i++) {
			if (listState.get(i).getID().equals(fromID)) {
				ArrayList<Transition> transList = listState.get(i).getTransition();
				for ( int j = 0 ; j < transList.size() ; j++) {
					Transition transition = transList.get(j);
					boolean equals = false;
					if ( inputChar == null )
						equals = inputChar == (Character) transition.getInput();
					else 
						equals = (inputChar.equals(transition.getInput()));
					
					if ( equals &&  !reachableSet.contains(transition.getNextState().getID())) {
						reachableSet.add(transition.getNextState().getID());
						reachableSet.addAll(statesReachableFrom(transition.getNextState().getID()));
					}
				}
			}
		}
		return reachableSet;
	}
	
	//Tra ve tap hop cac trang thai co the den duoc qua EPSILON 
	public HashSet<String> statesReachableFrom(String from){
		return statesReachableOn(from, null);
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
