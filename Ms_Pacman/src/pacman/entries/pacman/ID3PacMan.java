package pacman.entries.pacman;

import java.util.ArrayList;

import dataRecording.ID3DataTuple;
import id3.id3Pojo.Attribute;
import id3.id3Pojo.Node;
import id3.id3Utilities.HelperClass;
import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;


public class ID3PacMan extends Controller<MOVE>{
	private Node rootNode;
	private MOVE pacManMove = MOVE.NEUTRAL;
	
	public ID3PacMan() {
		super();
		//Initialize tuples- and attributeList.
		ArrayList<ID3DataTuple> tuples = HelperClass.getALFromFile("myData/trainingData.txt");
		ArrayList<Attribute> attributes = HelperClass.generateAttributes();
		rootNode = generateTree(tuples, attributes);
		rootNode.PrintPretty("", true);
	}
	

	/**
	 * Creates an ID3 Decision tree.
	 * @param tuples , training data
	 * @param attributes , available attributes
	 * @return a Node instance, the root of the tree.
	 */
	private Node generateTree(ArrayList<ID3DataTuple> tuples, ArrayList<Attribute> attributes) {
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
		ArrayList<ArrayList<ID3DataTuple>> splitData = HelperClass.splitData(tuples, attr);
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
	
	/**
	 * Traverses the pre-built decision tree in a certain way based on the content of the tuple.
	 * @param node
	 * @param tuple
	 * @return
	 */
	public String predictClassLabel(Node node, ID3DataTuple tuple) {
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
	

	public MOVE getMove(Game game, long timeDue) {
		// ID3 is called and after execution returns a move	
		ID3DataTuple tuple = new ID3DataTuple(game, pacManMove);
		String pred = predictClassLabel(rootNode, tuple);
		//System.out.println(pred);
		if(pred.equals("UP")) {
			pacManMove = MOVE.UP;
		}
		if(pred.equals("DOWN")) {
			pacManMove = MOVE.DOWN;
		}
		if(pred.equals("LEFT")) {
			pacManMove = MOVE.LEFT;
		}
		if(pred.equals("RIGHT")) {
			pacManMove = MOVE.RIGHT;
		}
		return pacManMove;
	}

}
