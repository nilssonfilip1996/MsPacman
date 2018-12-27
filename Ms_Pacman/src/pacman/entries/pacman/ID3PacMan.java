package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ID3PacMan extends Controller<MOVE>{
	private MOVE pacManMove = MOVE.RIGHT;
	private static final int DISTANCE_CLOSE=20;	//if a ghost is this close set 
	boolean ghostClose = false;
	
	public MOVE getMove(Game game, long timeDue) {
		// ID3 is called and after execution returns a move
		
		int current=game.getPacmanCurrentNodeIndex();
		
		for(GHOST ghost : GHOST.values())
			if(game.getGhostEdibleTime(ghost)==0 && game.getGhostLairTime(ghost)==0)
				if(game.getShortestPathDistance(current,game.getGhostCurrentNodeIndex(ghost))<DISTANCE_CLOSE)
					ghostClose = true;
		if(ghostClose)
			System.out.print(" Ghost close! ");
		
		return pacManMove;
	}

}
