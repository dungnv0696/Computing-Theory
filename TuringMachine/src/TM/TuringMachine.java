package TM;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class TuringMachine {
	protected String TuringMachineName;
	protected ArrayList<State> listState;
	protected State startState;
	protected ArrayList<Character> listInput;
	protected ArrayList<State> listFinalState;
	//protected static Tape tape;
	
	public static char currentChar;
	public static State currentState;
	public int charCount;
	public boolean cannotMove;
	
	public TuringMachine() {	
	}
	
	public TuringMachine(ArrayList<State> listState, State startState) {
		this.listState = listState;
		this.startState = startState;
	}
				
	public void inThongTinTuringMachine() {
		//System.out.println("Thong tin TuringMachine: "+TuringMachineName);
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
				System.out.println("  + input: "+ tran.getInput() 
								  +"  write: "+ tran.getWrite()
								  +"  move: "+tran.getMove()
								  +"  from: "+tran.getCurrentState().getID()
								  +"  to: "+tran.getNextState().getID());
			}
		}
		System.out.println();
	}
		
	public static TuringMachine readXML(String fileName) throws ParserConfigurationException, SAXException, IOException {
		TuringMachine TuringMachine = new TuringMachine();
		TuringMachine.setTuringMachineName(fileName);
		ArrayList<State> listState = new ArrayList<State>();
		ArrayList<State> listFinalState = new ArrayList<State>();
		ArrayList<Character> listInput = new ArrayList<Character>();
		
		File f = new File(fileName);
		if (!f.exists()) {
			System.out.println("Khong tim thay file. Hay kiem tra lai");
			System.exit(0);
		}
	
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder buider = factory.newDocumentBuilder();
		Document doc = buider.parse(f);		
		
		Element structures = doc.getDocumentElement();	
		
		//Xac dinh cac State cua TuringMachine
		NodeList stateList = structures.getElementsByTagName("state");
		for(int i = 0; i < stateList.getLength(); i ++) {
			Node nodeState = stateList.item(i);
			if ( nodeState.getNodeType() == Node.ELEMENT_NODE) {
				Element stateNode = (Element) nodeState;
				State state = new State(stateNode.getAttribute("id"), stateNode.getAttribute("name"));
				
				float x = Float.parseFloat(stateNode.getElementsByTagName("x").item(0).getTextContent());
				float y = Float.parseFloat(stateNode.getElementsByTagName("y").item(0).getTextContent());
				state.setPoint(x, y);
				
				for (int j = 0 ; j < nodeState.getChildNodes().getLength(); j++) {
					Node statePropertyNode = nodeState.getChildNodes().item(j); 
					switch (statePropertyNode.getNodeName()) {
					case "initial":
						state.setIsStartState(true);
						break;
					case "final":
						state.setIsFinalState(true);
						break;
					}
				}
				listState.add(state);
			}
		}

		//Xac dinh cac Transition tuong ung voi moi State 
		NodeList transitionList = structures.getElementsByTagName("transition");
		for (int i = 0 ; i < transitionList.getLength(); i++) {
			Node nodeTrans = transitionList.item(i);
			if ( nodeTrans.getNodeType() == Node.ELEMENT_NODE) {
				Element transNode = (Element) nodeTrans;
				for (int j = 0 ; j < listState.size(); j++)
					if (listState.get(j).getID().equals(transNode.getElementsByTagName("from").item(0).getTextContent())) {
						State currentState = listState.get(j);
						for (int k = 0; k < listState.size(); k++)
							if (listState.get(k).getID().equals(transNode.getElementsByTagName("to").item(0).getTextContent())) {
								State nextState = listState.get(k);
								Character input;
								Character write;
								//input
								if(!transNode.getElementsByTagName("read").item(0).getTextContent().equals("")) {
									input = transNode.getElementsByTagName("read").item(0).getTextContent().charAt(0);
								}
								else {
									input = null;
								}
								//write
								if(!transNode.getElementsByTagName("write").item(0).getTextContent().equals("")) {
									write = transNode.getElementsByTagName("write").item(0).getTextContent().charAt(0);
								}
								else {
									write = null;
								}
								//move
								char move = transNode.getElementsByTagName("move").item(0).getTextContent().charAt(0);
								Transition transition = new Transition (input, write, move, currentState, nextState);
								currentState.getTransition().add(transition);
							}
					}		
			}
		}
		
		//Xac dinh trang thai bat dau va ket thuc cua TuringMachine
		for (int i = 0 ; i < listState.size(); i++) {
			if (listState.get(i).getIsStartState()) 
				TuringMachine.setStartState(listState.get(i));
			if (listState.get(i).getIsFinalState())
				listFinalState.add(listState.get(i));
		}
		
		//Kiem tra ton tai trang thai bat dau hoac ket thuc khong
		if (TuringMachine.getStartState() == null) {
			System.out.println(TuringMachine.getTuringMachineName()+" khong co trang thai bat dau");
			System.exit(0);
		}
		if (listFinalState.isEmpty()) {
			System.out.println(TuringMachine.getTuringMachineName()+" Khong co trang thai ket thuc");
			System.exit(0);
		} 
		
		TuringMachine.setListState(listState);
		TuringMachine.setListFinalState(listFinalState);
		TuringMachine.setListInput(listInput);
		
		return TuringMachine;
	}
	
	public boolean checkAStarLanguage() {
		for(int i = 0 ; i < listState.size(); i++ ) {
			State currentState = listState.get(i);
			for(int j = 0 ; j < currentState.getTransition().size(); j++) {
				Transition trans = currentState.getTransition().get(j);
				if(trans.getInput() != null)
					if(trans.getInput() == 'b' && !trans.getNextState().getTransition().isEmpty()) 
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
	
	public void setTuringMachineName(String TuringMachineName) {
		this.TuringMachineName = TuringMachineName;
	}
	
	public String getTuringMachineName() {
		return TuringMachineName;
	}
}
