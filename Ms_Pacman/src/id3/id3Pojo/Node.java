package id3.id3Pojo;

import java.util.ArrayList;

public class Node {
	private ArrayList<Node> childrenNodes;
	private Attribute attribute = null;
	private String branchName;
	private String classLabel;

	public Node() {
		childrenNodes = new ArrayList<>();
		branchName="";
	}

	public ArrayList<Node> getChildrenNodes() {
		return childrenNodes;
	}

	public void setChildrenNodes(ArrayList<Node> childrenNodes) {
		this.childrenNodes = childrenNodes;
	}
	
	public void addChildNode(Node node) {
		childrenNodes.add(node);
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	public void setClassLabel(String classLabel) {
		this.classLabel = classLabel;
	}
	
	public String getClassLabel() {
		return classLabel;
	}

	public String getBranchName() { 
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	@Override
	public String toString() {
		return "Node [childrenNodes=--------" + childrenNodes + "-------------, branchName=" + branchName + ", classLabel=" + classLabel
				+ "]";
	}
	
	public void PrintPretty(String indent, boolean last)
	   {
	       System.out.print(indent);
	       if (last)
	       {
	           System.out.print("\\-");
	           indent += "  ";
	       }
	       else
	       {
	           System.out.print("|-");
	           indent += "| ";
	       }
	       System.out.print(branchName + "-");
	       if(attribute!=null) {
	    	  System.out.println(attribute.getName());
	       }
	       else {
			System.out.println(classLabel);
	       }

	       for (int i = 0; i < childrenNodes.size(); i++) {
	           childrenNodes.get(i).PrintPretty(indent, i == childrenNodes.size()-1);
	       }
	   }

	

	

	
	
	
	
	
	
	

}
