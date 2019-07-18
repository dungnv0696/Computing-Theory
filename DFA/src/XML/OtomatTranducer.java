package XML;

import Otomat.Otomat;
import Otomat.State;
import Otomat.Transition;

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
	
	public static Otomat mixOtomat(Otomat a, Otomat b, String operator){
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
				
				//Cai dat alphabet cho Otomat: 
				if (a.getListInput().equals(b.getListInput()))
					mixOtomat.setListInput(a.getListInput());
				else {
					System.out.println("2 otomat co 2 alphabet khac nhau");
					System.exit(0);
					}
				
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
		
		mixOtomat.setListState(listState);
		return mixOtomat; 
	}
	
	//Ham nay chua' code de doc file .jflap ; Do doc nham` de` nen em code thua`, ma` tiec' cong code qua' nen em de day ma` khong xoa' 
	public void unusedCode() {
		/*for (int i = 0; i < structureList.getLength(); i++) {
		Node node = structureList.item(i);
		if (node.getNodeType() == Node.ELEMENT_NODE){
			Element structure = (Element) node;
			String type = structure.getAttribute("type");
			switch (type) {
				//Xac dinh tat ca trang thai trong Otomat
				case "state_set":
					NodeList stateListS = structure.getElementsByTagName("state");
					for (int j = 0; j < stateListS.getLength(); j++) {
						Node nodeState = stateListS.item(j);
						if (nodeState.getNodeType() == Node.ELEMENT_NODE) {												
								Element state = (Element) nodeState;
								listState.add(new State(state.getElementsByTagName("id").item(0).getTextContent()
										, state.getElementsByTagName("name").item(0).getTextContent()));
						}						
					}
				break;
				
				//Xac dinh cac trang thai ket thuc luu vao ArrayList stateFinalState
				case "final_states":
					NodeList stateListF = structure.getElementsByTagName("state");
					for (int j = 0; j < stateListF.getLength(); j++) {
						Node nodeState = stateListF.item(j);
						if (nodeState.getNodeType() == Node.ELEMENT_NODE) {												
								Element state = (Element) nodeState;
								listFinalState.add(new State(state.getElementsByTagName("id").item(0).getTextContent()
										, state.getElementsByTagName("name").item(0).getTextContent()));
						}						
					}
				break;
				
				//Xac dinh va them cac ham chuyen trang thai ung voi moi trang thai
				case "transition_set":
					NodeList tranList = structure.getElementsByTagName("fsa_trans");
					for (int j = 0; j < tranList.getLength(); j++) {
						Node nodeTran = tranList.item(j);
						if (nodeTran.getNodeType() == Node.ELEMENT_NODE) {
							Element input = (Element) nodeTran.getFirstChild().getNextSibling();
							Element currentState = (Element) input.getNextSibling().getNextSibling();
							Element nextState = (Element) currentState.getNextSibling().getNextSibling();
							for (int k = 0; k < listState.size(); k++)
								if (listState.get(k).getID().equals(currentState.getElementsByTagName("id").item(0).getTextContent()))
									for (int m = 0; m < listState.size(); m++)
										if (listState.get(m).getID().equals(nextState.getElementsByTagName("id").item(0).getTextContent()))
											listState.get(k).addTransitionToArray(input.getTextContent().charAt(0), listState.get(k),listState.get(m));
						}		
					}
				break;
				
				//Them trang thai bat dau vao Otomat
				case "start_state":
					for(int k = 0; k < listState.size(); k++) 							
						if (listState.get(k).getID().equals(structure.getElementsByTagName("id").item(0).getTextContent())) {								
							otomat.setStartState(listState.get(k));
						}	
				break;
				
				case "input_alph":
					NodeList inputList = structure.getElementsByTagName("symbol");
					for (int j = 0; j < inputList.getLength(); j++) {
						Node input = inputList.item(j);
						listInput.add(input.getTextContent().charAt(0));
					}
			}	
		}				
	}
	
	//Them thuoc tinh trang thai ket thuc cho State
	for(int i = 0; i < listState.size(); i++)
		for(int j = 0; j < listFinalState.size(); j++)
			if (listState.get(i).getID().equals(listFinalState.get(j).getID()))								
				listState.get(i).setIsFinalState(true);
	
	//Xac dinh toa do State
	NodeList statePointMap = structures.getElementsByTagName("state_point");
	for (int i = 0; i < statePointMap.getLength(); i++) {
		Node statePoint = statePointMap.item(i);
		if(statePoint.getNodeType() == Node.ELEMENT_NODE) {
			Element stateID = (Element) statePoint.getFirstChild().getNextSibling();
			Element point = (Element) stateID.getNextSibling().getNextSibling();
			for (int j = 0 ; j < listState.size(); j++)
				if (listState.get(j).getID().equals(stateID.getTextContent())) {
					float x = Float.parseFloat(point.getElementsByTagName("x").item(0).getTextContent());
					float y = Float.parseFloat(point.getElementsByTagName("y").item(0).getTextContent());
					Point toaDo = new Point();
					toaDo.setLocation(x, y);
					listState.get(j).setPoint(toaDo);
				}
		}
	}
	
	//Xac dinh toa do Transition
	NodeList tranPointMap = structures.getElementsByTagName("control_point");
	for (int i = 0; i < tranPointMap.getLength(); i++) {
		Node tranPoint = tranPointMap.item(i);
		if(tranPoint.getNodeType() == Node.ELEMENT_NODE) {
			Element currentState = (Element) tranPoint.getFirstChild().getNextSibling();
			Element nextState = (Element) currentState.getNextSibling().getNextSibling();
			Element point = (Element) nextState.getNextSibling().getNextSibling();
			for (int j = 0 ; j < listState.size(); j++)
				if(listState.get(j).getID().equals(currentState.getTextContent())) {
					for (int k = 0 ; k < listState.get(j).getTransition().size(); k++)
						if(listState.get(j).getTransition().get(k).getNextState().getID().equals(nextState.getTextContent())) {
							float x = Float.parseFloat(point.getElementsByTagName("x").item(0).getTextContent());
							float y = Float.parseFloat(point.getElementsByTagName("y").item(0).getTextContent());
							Point toaDo = new Point();
							toaDo.setLocation(x, y);
							listState.get(j).getTransition().get(k).setTransPoint(toaDo);
						}
				}
		}
	}*/
	}
}
	