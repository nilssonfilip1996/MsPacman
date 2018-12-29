package id3.id3Utilities;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dataRecording.ID3DataTuple;
import id3.id3Main.Settings;
import id3.id3Pojo.Attribute;

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

	public static boolean doTuplesHaveSameClass(ArrayList<ID3DataTuple> tuples) {
		if (tuples.size() < 2) {
			return true;
		}
		for (int i = 0; i < tuples.size() - 1; i++) {
			if (!(tuples.get(i).DirectionChosen.name().equals(tuples.get(i + 1).DirectionChosen.name()))) {
				return false;
			}
		}
		return true;
	}

	// Fixed for Pac-man
	public static int[] getLabelFrequency(ArrayList<ID3DataTuple> tuples) {
		int[] counter = new int[Settings.CLASS_LABELS.length];
		for (int i = 0; i < tuples.size(); i++) {
			for (int j = 0; j < Settings.CLASS_LABELS.length; j++) {
				if (tuples.get(i).DirectionChosen.name().equals(Settings.CLASS_LABELS[j])) { 
					counter[j]++;
					break;
				}
			}
		}
		return counter;
	}

	public static String getMajorityClassInList(ArrayList<ID3DataTuple> tuples) {
		int[] counter = getLabelFrequency(tuples);
		int max = counter[0];
		int index = 0;
		for (int i = 0; i < counter.length; i++) {
			if (max < counter[i]) {
				max = counter[i];
				index = i;
			}
		}
		return Settings.CLASS_LABELS[index];
	}

	// Fixed for pac-man
	public static double[] getLableRatios(ArrayList<ID3DataTuple> tuples) {
		double[] labelRatios = new double[Settings.CLASS_LABELS.length];
		int length = tuples.size();
		int[] counter = getLabelFrequency(tuples);
		for (int i = 0; i < Settings.CLASS_LABELS.length; i++) {
			labelRatios[i] = (double) counter[i] / length;
		}
		return labelRatios;
	}

	// Fixed for pac-man
	public static double calculateEntropy(double[] ratios) {
		double entropy = 0.0;
		for (int i = 0; i < ratios.length; i++) {
			entropy -= ratios[i] * MathHelper.log2(ratios[i]);
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
	public static double informationGain(ArrayList<ID3DataTuple> tuples, Attribute attr) {
		double gain = 0;

		gain = calculateEntropy(getLableRatios(tuples));
		ArrayList<ArrayList<ID3DataTuple>> splitData = splitData(tuples, attr);

		for (int i = 0; i < attr.getNumberOfValues(); i++) {
			double subSetSize = splitData.get(i).size();
			if(subSetSize==0) continue;		
			double setSize = tuples.size();
			gain -= (subSetSize / setSize) * calculateEntropy(getLableRatios(splitData.get(i)));
		}

		return gain;
	}

	public static ArrayList<ArrayList<ID3DataTuple>> splitData(ArrayList<ID3DataTuple> tuples, Attribute attr) {
		ArrayList<ArrayList<ID3DataTuple>> splitData = new ArrayList<ArrayList<ID3DataTuple>>(attr.getNumberOfValues());
		for (int i = 0; i < attr.getNumberOfValues(); i++) {
			splitData.add(new ArrayList<ID3DataTuple>());
		}
		ArrayList<String> attrValues = attr.getAttributeValues();
		for (int i = 0; i < attrValues.size(); i++) { // For all attribute values.
			for (int j = 0; j < tuples.size(); j++) { // For all tuples
				ID3DataTuple tempTuple = tuples.get(j);
				if (attrValues.get(i).equals(tempTuple.getAttributeValueAt(attr.getIndex()))) { // if a certain attr
																								// value equals the one
																								// found in the tuple.
					splitData.get(i).add(tempTuple);
				}
			}
		}
		return splitData;
	}

	public static Attribute attributeSelection(ArrayList<ID3DataTuple> tuples, ArrayList<Attribute> attributes) {
		double best = 0;
		double current = 0;
		Attribute attribute = attributes.get(0);
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
		attrValues.add("false");
		attrValues.add("true");
		attributes.add(new Attribute(10, "isGhostClose", attrValues));

		ArrayList<String> attrValues1 = new ArrayList<>();
		attrValues1.add("UP");
		attrValues1.add("RIGHT");
		attrValues1.add("DOWN");
		attrValues1.add("LEFT");
		attrValues1.add("NEUTRAL");
		attributes.add(new Attribute(12, "closestPillDir", attrValues1));
		
		ArrayList<String> attrValues2 = new ArrayList<>();
		attrValues2.add("UP");
		attrValues2.add("RIGHT");
		attrValues2.add("DOWN");
		attrValues2.add("LEFT");
		attrValues2.add("NEUTRAL");
		attributes.add(new Attribute(13, "dirAwayFromClosestGhost", attrValues2));
		
		ArrayList<String> attrValues3 = new ArrayList<>();
		attrValues3.add("false");
		attrValues3.add("true");
		attributes.add(new Attribute(14, "isPPClose", attrValues3));
		
		ArrayList<String> attrValues4 = new ArrayList<>();
		attrValues4.add("UP");
		attrValues4.add("RIGHT");
		attrValues4.add("DOWN");
		attrValues4.add("LEFT");
		attrValues4.add("NEUTRAL");
		attributes.add(new Attribute(15, "closestPPDir", attrValues4));



		return attributes;
	}

	public static ArrayList<Attribute> getAttributeListCopy(ArrayList<Attribute> list) {
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
