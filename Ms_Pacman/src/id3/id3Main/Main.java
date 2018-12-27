package id3.id3Main;

import java.util.ArrayList;
import java.util.Collections;

import id3.id3Pojo.Attribute;
import id3.id3Pojo.DataTuple;
import id3.id3Pojo.Node;
import id3.id3Utilities.HelperClass;



/**
 * Test project for learning ID3.
 * @author nilss
 *
 */
public class Main {
	
	public Main() {
		ArrayList<DataTuple> tuples;
		//tuples = HelperClass.getALFromFile("myData/testData.txt");
		tuples = null;
		ArrayList<Attribute> attributes = HelperClass.generateAttributes(); 
		ArrayList<Attribute> attributesTemp = HelperClass.getAttributeListCopy(attributes);
		Node node = generateTree(tuples, attributesTemp);
		String[] tokens = {"Youth", "Medium", "Yes", "Excellent", "No"};
		DataTuple tuple = new DataTuple(tokens, tokens[0], tokens[1], tokens[2], tokens[3], tokens[4]);
		System.out.println(predictClassLabel(node, tuple));
		System.out.println(node.getChildrenNodes());
		
		
		System.out.println(HelperClass.informationGain(tuples, attributes.get(0)));
		
		node.PrintPretty("", true);
		
		
//		System.out.println(node.getChildrenNodes().get(0).getAttribute().getName());
//		System.out.println(node.getChildrenNodes().get(1).getChildrenNodes().size());
//		System.out.println(node.getChildrenNodes().get(2).getAttribute().getName());
//		ArrayList<ArrayList<DataTuple>> splitData = HelperClass.splitData(tuples, attributes.get(1));
//		for (int i = 0; i < splitData.size(); i++) {
//			System.out.println("------------------------------");
//			System.out.println(splitData.get(i));
//		}
//		System.out.println(HelperClass.informationGain(tuples, attributes.get(1)));
	}
	
	public String predictClassLabel(Node node, DataTuple tuple) {
		String prediction = "";
		if(node.getChildrenNodes().isEmpty()) {
			return node.getClassLabel();
		}
		int index = node.getAttribute().getIndex();
		String tupleFieldValue = tuple.getAttributeValueAt(index);
		ArrayList<Node> childrenNodes = node.getChildrenNodes();
		for (int i = 0; i < childrenNodes.size(); i++) {
			if(childrenNodes.get(i).getBranchName().equals(tupleFieldValue)) {
				prediction =  predictClassLabel(childrenNodes.get(i), tuple);
				break;
			}
		}
		return prediction;
	}
	
	public Node generateTree(ArrayList<DataTuple> tuples, ArrayList<Attribute> attributes) {
		Node node = new Node();
		String majorityClassStr = HelperClass.getMajorityClassInList(tuples);
		if(HelperClass.doTuplesHaveSameClass(tuples)) { 		//All tuples have same class label.(Yes or No).
			node.setClassLabel(majorityClassStr);
			return node;
		}
		if(attributes.size()==0) {
			node.setClassLabel(majorityClassStr);
			return node;
		}
		Attribute attr = HelperClass.attributeSelection(tuples, attributes);
		HelperClass.removeAttribute(attributes, attr);
		node.setAttribute(attr);
		ArrayList<ArrayList<DataTuple>> splitData = HelperClass.splitData(tuples, attr);
		for (int i = 0; i < splitData.size(); i++) {
			Node childNode = new Node();
			if(splitData.get(i).isEmpty()) {
				childNode.setClassLabel(majorityClassStr);
			}
			else {
				childNode = generateTree(splitData.get(i), attributes);
			}
			childNode.setBranchName(attr.getAttributeValues().get(i));
			node.addChildNode(childNode);
		}
		return node;
		
	}
	
	public static void main(String[] args) {
		new Main();
	}

}
