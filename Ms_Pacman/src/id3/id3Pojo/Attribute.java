package id3.id3Pojo;

import java.util.ArrayList;

public class Attribute {
	private int index;
	private String name;
	private ArrayList<String> attributeValues;
	
	public Attribute(int i, String name, ArrayList<String> attributeValues) {
		this.index  =  i;
		this.name = name;
		this.attributeValues = attributeValues;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getAttributeValues() {
		return attributeValues;
	}

	public void setAttributeValues(ArrayList<String> attributeValues) {
		this.attributeValues = attributeValues;
	}
	
	public int getNumberOfValues() {
		return attributeValues.size();
	}

	@Override
	public String toString() {
		return "Attribute [name=" + name + ", attributeValues=" + attributeValues + "]";
	}
	
	
}
