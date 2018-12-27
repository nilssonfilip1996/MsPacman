package pacman.entries.pacman;

import java.util.ArrayList;

import dataRecording.ID3DataTuple;
import id3.id3Pojo.Attribute;
import id3.id3Pojo.DataTuple;
import id3.id3Pojo.Node;
import id3.id3Utilities.HelperClass;
import pacman.controllers.Controller;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;


public class ID3PacMan extends Controller<MOVE>{
	private Node rootNode;
	private MOVE pacManMove = MOVE.RIGHT;
	
	public ID3PacMan() {
		super();
		//Initialize tuples- and attributeList.
		ArrayList<ID3DataTuple> tuples = HelperClass.getALFromFile("myData/trainingData.txt");
		for (int i = 0; i < tuples.size(); i++) {
			System.out.println(tuples.get(i).DirectionChosen + " is: " + tuples.get(i).DirectionChosen.ordinal());
		}
		rootNode = generateTree(tuples, null);
	}
	
	private Node generateTree(ArrayList<ID3DataTuple> tuples, ArrayList<Attribute> attributes) {
		Node node = new Node();
		return node;
	}
	
	
	public MOVE getMove(Game game, long timeDue) {
		// ID3 is called and after execution returns a move
		
		
		
//		int current=game.getPacmanCurrentNodeIndex();
//		
//		for(GHOST ghost : GHOST.values())
//			if(game.getGhostEdibleTime(ghost)==0 && game.getGhostLairTime(ghost)==0)
//				if(game.getShortestPathDistance(current,game.getGhostCurrentNodeIndex(ghost))<DISTANCE_CLOSE) {
//					isGhostClose = true;
//				}
//				else {
//					isGhostClose = false;
//				}		
	
		return pacManMove;
	}

}
