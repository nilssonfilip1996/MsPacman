package dataRecording;

import pacman.game.util.*;

/**
 * This class uses the IO class in the PacMan framework to do the actual saving/loading of
 * training data.
 * @author andershh
 *
 */
public class DataSaverLoader {
	
	private static String FileNameTraining = "trainingData.txt";
	private static String FileNameTesting = "testData.txt";
	
//	public static void SavePacManData(DataTuple data)
//	{
//		IO.saveFile(FileName, data.getSaveString(), true);
//	}
	
	public static void SavePacManData(String fileName, ID3DataTuple data)
	{
		IO.saveFile(fileName, data.getSaveString(), true);
	}
	
	public static DataTuple[] LoadPacManData()
	{
		String data = IO.loadFile(FileNameTraining);
		String[] dataLine = data.split("\n");
		DataTuple[] dataTuples = new DataTuple[dataLine.length];
		
		for(int i = 0; i < dataLine.length; i++)
		{
			dataTuples[i] = new DataTuple(dataLine[i]);
		}
		
		return dataTuples;
	}
}
