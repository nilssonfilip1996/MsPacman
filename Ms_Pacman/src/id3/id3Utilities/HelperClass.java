package id3.id3Utilities;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import dataRecording.ID3DataTuple;
import id3.id3Main.Settings;
import id3.id3Pojo.Attribute;
import id3.id3Pojo.DataTuple;

import java.lang.Math;

public class HelperClass {

	public static ArrayList<ID3DataTuple> getALFromFile(String fileDir) {
		ArrayList<ID3DataTuple> tuples = new ArrayList<>();
		FileInputStream fstream;
		try {
			fstream = new FileInputStream(fileDir);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				tuples.add(new ID3DataTuple(strLine));
			}
			in.close();
			return tuples;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static boolean doTuplesHaveSameClass(ArrayList<DataTuple> tuples) {
		if (tuples.size() < 2) {
			return true;
		}
		for (int i = 0; i < tuples.size() - 1; i++) {
			if (!(tuples.get(i).getClassName().equals(tuples.get(i + 1).getClassName()))) {
				return false;
			}
		}
		return true;
	}
	
	//Fixed for Pac-man
	public static int[] getLabelFrequency(ArrayList<DataTuple> tuples) {
		int[] counter = new int[Settings.CLASS_LABELS.length];
		for (int i = 0; i < tuples.size(); i++) {
			for (int j = 0; j < Settings.CLASS_LABELS.length; j++) {
				if(tuples.get(i).getClassName().equals(Settings.CLASS_LABELS[j])) {		//{"Yes", "No"}
					counter[j]++;
					break;
				}
			}
		}
		return counter;
	}

	//Fixed for pac-man
	public static String getMajorityClassInList(ArrayList<DataTuple> tuples) {
		int[] counter = getLabelFrequency(tuples);
		//Change here for Pac-man!
		if(counter[0]>=counter[1]) {								//{"Yes", "No"}
			return "Yes";
		}
		else {
			return "No";
		}
	}
	
	//Fixed for pac-man
	public static double[] getLableRatios(ArrayList<DataTuple> tuples) {
		double[] labelRatios = new double[Settings.CLASS_LABELS.length];
		int length = tuples.size();
		int[] counter = getLabelFrequency(tuples);
		for (int i = 0; i < Settings.CLASS_LABELS.length; i++) {
			labelRatios[i] = (double) counter[i] / length;			//{"Yes", "No"}
		}
		return labelRatios;
	}
	
	//Fixed for pac-man
	public static double calculateEntropy(double[] ratios) {
		double entropy = 0.0;
		for (int i = 0; i < ratios.length; i++) {
			entropy-=ratios[i]*MathHelper.log2(ratios[i]);			//{"Yes", "No"}
		}
		return entropy;
	}

	/**
	 * Calculate the info gain from picking attribute attr.
	 * 
	 * @param tuples , dataset
	 * @param attr   , attribute to compute gain on.
	 * @return
	 */
	public static double informationGain(ArrayList<DataTuple> tuples, Attribute attr) {
		double gain = 0;

		gain = calculateEntropy(getLableRatios(tuples));
		ArrayList<ArrayList<DataTuple>> splitData = splitData(tuples, attr);

		for (int i = 0; i < attr.getNumberOfValues(); i++) {
			double subSetSize = splitData.get(i).size();
			double setSize = tuples.size();
			gain -= (subSetSize / setSize) * calculateEntropy(getLableRatios(splitData.get(i)));
		}

		return gain;
	}

	public static ArrayList<ArrayList<DataTuple>> splitData(ArrayList<DataTuple> tuples, Attribute attr) {
		ArrayList<ArrayList<DataTuple>> splitData = new ArrayList<ArrayList<DataTuple>>(attr.getNumberOfValues());
		for (int i = 0; i < attr.getNumberOfValues(); i++) {
			splitData.add(new ArrayList<DataTuple>());
		}
		ArrayList<String> attrValues = attr.getAttributeValues();
		for (int i = 0; i < attrValues.size(); i++) { // For all attribute values.
			for (int j = 0; j < tuples.size(); j++) { // For all tuples
				DataTuple tempTuple = tuples.get(j);
				if (attrValues.get(i).equals(tempTuple.getAttributeValueAt(attr.getIndex()))) { // if a certain attr
																								// value equals the one
																								// found in the tuple.
					splitData.get(i).add(tempTuple);
				}
			}
		}
		return splitData;
	}

	public static Attribute attributeSelection(ArrayList<DataTuple> tuples, ArrayList<Attribute> attributes) {
		double best = 0;
		double current = 0;
		Attribute attribute = null;
		for (int i = 0; i < attributes.size(); i++) {
			current = informationGain(tuples, attributes.get(i));
			if (current > best) {
				best = current;
				attribute = attributes.get(i);
			}
		}
		return attribute;
	}

	public static void removeAttribute(ArrayList<Attribute> attributes, Attribute attr) {
		attributes.remove(attr);
	}

	public static ArrayList<Attribute> generateAttributes() {
		ArrayList<Attribute> attributes = new ArrayList<>();

		ArrayList<String> attrValues = new ArrayList<>();
		attrValues.add("0");											//False
		attrValues.add("1");											//True
		attributes.add(new Attribute(0, "isGhostClose", attrValues));

		ArrayList<String> attrValues1 = new ArrayList<>();
		attrValues1.add("UP");
		attrValues1.add("RIGHT");
		attrValues1.add("DOWN");
		attrValues1.add("LEFT");
		attrValues1.add("NEUTRAL");
		attributes.add(new Attribute(1, "closestPillDir", attrValues1));

		return attributes;
	}
	
	public static ArrayList<Attribute> getAttributeListCopy(ArrayList<Attribute> list){
		ArrayList<Attribute> newList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			int index = list.get(i).getIndex();
			String name = list.get(i).getName();
			ArrayList<String> attributeValues = new ArrayList<>();
			for (int j = 0; j < list.get(i).getAttributeValues().size(); j++) {
				attributeValues.add(list.get(i).getAttributeValues().get(j));
			}
			newList.add(new Attribute(index, name, attributeValues));
		}
		return newList;
	}
}
