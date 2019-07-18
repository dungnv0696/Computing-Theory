

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
	
	public static Otomat OtomatTranduce(String fileName) throws ParserConfigurationException, SAXException, IOException {
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
					case "label":
						state.setLabel(stateNode.getElementsByTagName("label").item(0).getTextContent());
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
								if(!transNode.getElementsByTagName("read").item(0).getTextContent().equals("")) {
									input = transNode.getElementsByTagName("read").item(0).getTextContent().charAt(0);
								}
								else {
									input = null;
								}
								Transition transition = new Transition (input, currentState, nextState);
								currentState.getTransition().add(transition);
								
								//Xac dinh tap cac ki tu dau vao
								if( !listInput.contains(input)){ 
									if (input != null)
										listInput.add(input);
									else 
										Otomat.haveEPSILON = true;
								}
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
        	
        	Element pLabel = doc.createElement("label");
        	pLabel.appendChild(doc.createTextNode(state.getLabel()));
        	pState.appendChild(pLabel);
        	
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
	
}
	