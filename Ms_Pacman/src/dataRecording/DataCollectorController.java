package dataRecording;

import pacman.controllers.*;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

/**
 * The DataCollectorHumanController class is used to collect training data from playing PacMan.
 * Data about game state and what MOVE chosen is saved every time getMove is called.
 * @author andershh
 *
 */
public class DataCollectorController extends HumanController{
	
	private String fileName;

	public DataCollectorController(KeyBoardInput input, String fileName){
		super(input);
		this.fileName = fileName;
	}
	
	@Override
	public MOVE getMove(Game game, long dueTime) {
		MOVE move = super.getMove(game, dueTime);
		
		//DataTuple data = new DataTuple(game, move);
		
		ID3DataTuple data = new ID3DataTuple(game, move);
				
		DataSaverLoader.SavePacManData(fileName, data);		
		return move;
	}

}
