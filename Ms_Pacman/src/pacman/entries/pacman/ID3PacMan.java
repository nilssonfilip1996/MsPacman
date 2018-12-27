package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ID3PacMan extends Controller<MOVE>{
	private MOVE pacManMove = MOVE.RIGHT;
	
	public MOVE getMove(Game game, long timeDue) {
		// ID3 is called and after execution returns a move
		
		return pacManMove;
	}

}
