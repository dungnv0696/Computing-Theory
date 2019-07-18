package grammar;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import grammar.ProductionRule;

public class XML {
	public XML() {
		
	}
	
	public static void grammarTranducer(String fileName, Grammar G) throws Exception {
		File f = new File(fileName);
		if (!f.exists()) {
			System.out.println("Khong tim thay file. Hay kiem tra lai");
			System.exit(0);
		}
	
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder buider = factory.newDocumentBuilder();
		Document doc = buider.parse(f);		
		
		Element structures = doc.getDocumentElement();	
		
		boolean haveStartSymbol = false;
		NodeList pList = structures.getElementsByTagName("production");
		for(int i = 0; i < pList.getLength(); i++) {
			Node nodeP = pList.item(i);
			if ( nodeP.getNodeType() == Node.ELEMENT_NODE) {
				Element pNode = (Element) nodeP;
				String left = null, right = null;
				String startSymbol = null;
				for (int j = 0 ; j < nodeP.getChildNodes().getLength(); j++) {
					Node pPropertyNode = nodeP.getChildNodes().item(j); 
					switch (pPropertyNode.getNodeName()) {
					case "left":
						left = pNode.getElementsByTagName("left").item(0).getTextContent();
						//left = insert_space(left);
						if(!haveStartSymbol) {
							startSymbol = left;
							haveStartSymbol = true;
						}
						break;
					case "right":
						right = pNode.getElementsByTagName("right").item(0).getTextContent();
						right = insert_space(right);
						break;
					}
				}
				G.addProductionRule(left+" -> "+right);
				if(startSymbol != null)
					G.setStartSymbol(startSymbol);
			}
		}
	}
	
	public static void createDocument(Grammar G, String fileName) throws Exception {
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
        pType.appendChild(doc.createTextNode("grammar"));
        root.appendChild(pType);
        
        //Ghi thong tin Production cua grammar
        root.appendChild(doc.createComment("The list of productions."));
		for ( int i = 0 ; i < G.getProductionRule().size(); i++) {
			ProductionRule p = G.getProductionRule().get(i);
			
			for(int k = 0 ; k < p.N.size(); k++) 
				if(p.N.get(k).equals(G.getStartSymbol())){
					//System.out.println("âfsasf");
					Element pProduction = doc.createElement("production");
					
					Element pLeft = doc.createElement("left");
					String left = "";
					for (int j = 0; j < p.N.size(); j++) {
						left = left.concat(p.N.get(j));
					}
					pLeft.appendChild(doc.createTextNode(left));
					pProduction.appendChild(pLeft);
					
					Element pRight = doc.createElement("right");
					String right = "";
					for (int j = 0; j < p.V.size(); j++) {
						right = right.concat(p.V.get(j));
					}
					pRight.appendChild(doc.createTextNode(right));
					pProduction.appendChild(pRight);
					
					root.appendChild(pProduction);
			}
		}
		
		for ( int i = 0 ; i < G.getProductionRule().size(); i++) {
			ProductionRule p = G.getProductionRule().get(i);
			
			for(int k = 0 ; k < p.N.size(); k++) 
				if(!p.N.get(k).equals(G.getStartSymbol())){
					//System.out.println("");
					Element pProduction = doc.createElement("production");
					
					Element pLeft = doc.createElement("left");
					String left = "";
					for (int j = 0; j < p.N.size(); j++) {
						left = left.concat(p.N.get(j));
					}
					pLeft.appendChild(doc.createTextNode(left));
					pProduction.appendChild(pLeft);
					
					Element pRight = doc.createElement("right");
					String right = "";
					for (int j = 0; j < p.V.size(); j++) {
						right = right.concat(p.V.get(j));
					}
					pRight.appendChild(doc.createTextNode(right));
					pProduction.appendChild(pRight);
					
					root.appendChild(pProduction);
			}
		}
		
        //Xuat document vua tao ra file
        Source source = new DOMSource(doc);
        Result result = new StreamResult(f);
        Transformer trans = TransformerFactory.newInstance().newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        trans.setOutputProperty(OutputKeys.INDENT, "yes");
        trans.transform(source, result);
	}
	
		
	public static String insert_space(String str){
		if (str.isEmpty())
			return "$";
		else {
			String temp = ""; 
			for(int k = 0 ; k < str.length(); k++) {
				temp = temp.concat(" "+String.valueOf(str.charAt(k)));
			}
			return temp.substring(1);
		}
	}
}
