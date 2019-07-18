package EQ_DFA;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class OtomatTranducer {
	public OtomatTranducer() {
		
	}
	
	public Otomat OtomatTranduce(String fileName) throws ParserConfigurationException, SAXException, IOException {
		Otomat otomat = new Otomat();
		otomat.setOtomatName(fileName);
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
		
		//Xac dinh cac State cua Otomat
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
								char input = transNode.getElementsByTagName("read").item(0).getTextContent().charAt(0);
								Transition transition = new Transition (input, currentState, nextState);
								currentState.getTransition().add(transition);
								
								//Xac dinh tap cac ki tu dau vao
								if( ! listInput.contains(input))
									listInput.add(input);
							}
					}		
			}
		}
		
		//Xac dinh trang thai bat dau va ket thuc cua Otomat
		for (int i = 0 ; i < listState.size(); i++) {
			if (listState.get(i).getIsStartState()) 
				otomat.setStartState(listState.get(i));
			if (listState.get(i).getIsFinalState())
				listFinalState.add(listState.get(i));
		}
		
		//Kiem tra ton tai trang thai bat dau hoac ket thuc khong
		if (otomat.getStartState() == null) {
			System.out.println(otomat.getOtomatName()+" khong co trang thai bat dau");
			System.exit(0);
		}
		if (listFinalState.isEmpty()) {
			System.out.println(otomat.getOtomatName()+" Khong co trang thai ket thuc");
			System.exit(0);
		} 
		
		otomat.setListState(listState);
		otomat.setListFinalState(listFinalState);
		otomat.setListInput(listInput);
		
		return otomat;
	}
	
	public static void createDocument(Otomat otomat,String fileName) throws Exception {
		//Tao document 
		File f = new File(fileName);
		Document doc = null;
		Element root = null;
		
		//Neu file da ton tai thi xoa file cu de tao file moi
        if (f.exists())
        	f.delete();
        
        //Khoi tao root <structure>
        doc = DocumentBuilderFactory.newInstance().
                newDocumentBuilder().newDocument();
        root = doc.createElement("structure");
        doc.appendChild(root);
        
        //Tao type cua automata <type>fa<type>
        Element pType = doc.createElement("type");
        pType.appendChild(doc.createTextNode("fa"));
        root.appendChild(pType);
        
        //Ghi thong tin cua automata vao jff <automata> ... </automata>
        Element pAutomata = doc.createElement("automaton");
        pAutomata.appendChild(doc.createComment("The list of states."));
        //Ghi thong tin states 
        for (int i = 0 ; i < otomat.getListState().size(); i++) {
        	State state = otomat.getListState().get(i);
        	Element pState = doc.createElement("state");
        	
        	pState.setAttribute("id", state.getID());
        	pState.setAttribute("name", state.getName());
        	
        	Element pStatePointX = doc.createElement("x");
        	pStatePointX.appendChild(doc.createTextNode(String.valueOf(state.getPoint().getX())));
        	pState.appendChild(pStatePointX);
        	Element pStatePointY = doc.createElement("y");
        	pStatePointY.appendChild(doc.createTextNode(String.valueOf(state.getPoint().getY())));
        	pState.appendChild(pStatePointY);
        	if (state.getIsStartState())
        		pState.appendChild(doc.createElement("initial"));
        	if (state.getIsFinalState())
        		pState.appendChild(doc.createElement("final"));
        	
        	pAutomata.appendChild(pState);
        }
        pAutomata.appendChild(doc.createComment("The list of transitions."));
        //Ghi thong tin transitions
        for (int i = 0; i < otomat.getListState().size(); i++) {
        	State state = otomat.getListState().get(i);
        	for (int j = 0 ; j < state.getTransition().size(); j++) {
        		Transition transition = state.getTransition().get(j);
        		Element pTrans = doc.createElement("transition");
        		
        		Element pFrom = doc.createElement("from");
        		pFrom.appendChild(doc.createTextNode(transition.getCurrentState().getID()));
        		pTrans.appendChild(pFrom);
        		Element pTo = doc.createElement("to");
        		pTo.appendChild(doc.createTextNode(transition.getNextState().getID()));
        		pTrans.appendChild(pTo);
        		Element pRead = doc.createElement("read");
        		pRead.appendChild(doc.createTextNode(String.valueOf(transition.getInput())));
        		pTrans.appendChild(pRead);
        		
        		pAutomata.appendChild(pTrans);
        	}
        }
        root.appendChild(pAutomata);
        
        //Xuat document vua tao ra file
        Source source = new DOMSource(doc);
        Result result = new StreamResult(f);
        Transformer trans = TransformerFactory.newInstance().newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        trans.transform(source, result);
	}
	
	public Otomat mixOtomat(Otomat a, Otomat b, String operator){
		Otomat mixOtomat = new Otomat();
		mixOtomat.setOtomatName(operator);
		
		int id = 0;
		ArrayList<State> listState = new ArrayList<State>();
		
		for (int i = 0; i < a.getListState().size(); i++)
			for (int j = 0; j < b.getListState().size(); j++) {
				//Cai dat id va ten State
				State stateA = a.getListState().get(i);
				State stateB = b.getListState().get(j);
				State stateMix = new State(Integer.toString(id++), "q"+stateA.getID()+stateB.getID());
				
				//Cai dat toa do State
				float x = 0.0f, y = 0.0f;
				x += 100.0 * i;
				y += 100.0 * j;
				stateMix.setPoint(x, y);
				
				//Cai dat trang thai cuoi cho State
				if (operator.equals("Hop")) { 
					if (stateA.getIsFinalState() || stateB.getIsFinalState())
						stateMix.setIsFinalState(true);
				}
				else if (operator.equals("Giao")){
					if (stateA.getIsFinalState() && stateB.getIsFinalState())
						stateMix.setIsFinalState(true);
				}
				
				//Cai dat trang thai dau cho Otomat va State
				if (stateA.getID().equals(a.getStartState().getID()) && stateB.getID().equals(b.getStartState().getID())) {
					stateMix.setIsStartState(true);	
					mixOtomat.setStartState(stateMix);
				}
				
				/*//Cai dat alphabet cho Otomat: 
				if (a.getListInput().equals(b.getListInput()))
					mixOtomat.setListInput(a.getListInput());
				else {
					System.out.println("2 otomat co 2 alphabet khac nhau");
					System.exit(0);
					}
				*/
				for (int k = 0 ; k < stateA.getTransition().size() ; k++)
					for (int m = 0; m < stateB.getTransition().size(); m++){
						Transition tranA = stateA.getTransition().get(k);
						Transition tranB = stateB.getTransition().get(m);
						if (tranA.getInput() == tranB.getInput()) {
							Transition mixTrans = new Transition(tranA.getInput(), stateMix, "q"+tranA.getNextState().getID()+tranB.getNextState().getID());
							stateMix.getTransition().add(mixTrans);
						}
					}
				listState.add(stateMix);
			}
		
		for (int i = 0 ; i < listState.size() ; i++) {
			State stateTemp = listState.get(i);
			for (int j = 0 ; j < stateTemp.getTransition().size(); j++) {
				Transition transTemp = stateTemp.getTransition().get(j);
				for (int k = 0; k < listState.size(); k++) {
					if (listState.get(k).getName().equals(transTemp.getNextStateName())) {
						transTemp.setNextState(listState.get(k));
					}	
				}	
			}
		}
		
		
		Queue<State> OPEN = new LinkedList<State>();
		ArrayList<State> CLOSE = new ArrayList<State>();
		
		OPEN.offer(mixOtomat.getStartState());
		
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
		
		for(int i = 0 ; i < listState.size(); i++) {
			State state = listState.get(i);
			if(CLOSE.contains(state))
				continue;
			else if(!CLOSE.contains(state)) {
				listState.remove(state);
			}
		}
		
		
		mixOtomat.setListState(listState);
		return mixOtomat; 
	}
	
	public Otomat minusOtomat(Otomat a) {
		Otomat minusA = new Otomat();
		
		minusA.setStartState(a.getStartState());
		ArrayList<State> listState= new ArrayList<State>();
		for(int i = 0; i < a.getListState().size(); i++) {
			State state_a = a.getListState().get(i);
			State state = new State(state_a.getID(), state_a.getName());
			if(state_a.getIsStartState())
				state.setIsStartState(true);
			if(state_a.getIsFinalState())
				state.setIsFinalState(false);
			else 
				state.setIsFinalState(true);
			
			for(int j = 0; j <state_a.getTransition().size(); j++) {
				Transition trans_a = state_a.getTransition().get(j);
				Transition trans = new Transition(trans_a.getInput(), trans_a.getCurrentState(), trans_a.getNextState());
				state.getTransition().add(trans);
			}
			listState.add(state);	
		}
		minusA.setListState(listState);
		return minusA;
	}
}
	