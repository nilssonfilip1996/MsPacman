package pacman.entries.pacman;

import id3.id3Pojo.Node;

import pacman.controllers.Controller;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;


public class ID3PacMan extends Controller<MOVE>{
	private Node rootNode;

	public ID3PacMan(Node rootNode) {
		super();
		this.rootNode = rootNode;
	}
	private MOVE pacManMove = MOVE.RIGHT;
	
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
