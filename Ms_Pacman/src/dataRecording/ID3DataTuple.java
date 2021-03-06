package dataRecording;

import java.util.ArrayList;

import pacman.game.Constants;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ID3DataTuple {

	public enum DiscreteTag {
		VERY_LOW, LOW, MEDIUM, HIGH, VERY_HIGH, NONE;

		public static DiscreteTag DiscretizeDouble(double aux) {
			if (aux < 0.1)
				return DiscreteTag.VERY_LOW;
			else if (aux <= 0.3)
				return DiscreteTag.LOW;
			else if (aux <= 0.5)
				return DiscreteTag.MEDIUM;
			else if (aux <= 0.7)
				return DiscreteTag.HIGH;
			else
				return DiscreteTag.VERY_HIGH;
		}
	}

	private String[] attributeValues;
	
	public MOVE DirectionChosen;

	// General game state this - not normalized!
	public int pacmanPosition;

	// Ghost this, dir, dist, edible - BLINKY, INKY, PINKY, SUE

	public int blinkyDist = -1;
	public int inkyDist = -1;
	public int pinkyDist = -1;
	public int sueDist = -1;

	public MOVE blinkyDir;
	public MOVE inkyDir;
	public MOVE pinkyDir;
	public MOVE sueDir;
	
	public boolean isGhostClose;
	public int closestGhostDistance;
	public MOVE directionToClosestPill;
	public int DISTANCE_CLOSE = 5;
	public int CLOSE = 20;
	public int MID = 60;
	public int FAR = 100;
	public MOVE dirAwayFromClosestGhost;
	public boolean isPPClose = false;
	public MOVE directionToClosestPP;
	public GHOST closestGhost;
	// Util data - useful for normalization
	private int maximumDistance = 150;

	/**
	 * Used for playing Pacman real-time.
	 * @param game
	 * @param move
	 */
	public ID3DataTuple(Game game, MOVE move) {
		if (move == MOVE.NEUTRAL) {
			move = game.getPacmanLastMoveMade();
		}

		this.DirectionChosen = move;

		this.pacmanPosition = game.getPacmanCurrentNodeIndex();

		if (game.getGhostLairTime(GHOST.BLINKY) == 0) {
			this.blinkyDist = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.BLINKY));
		}

		if (game.getGhostLairTime(GHOST.INKY) == 0) {
			this.inkyDist = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.INKY));
		}

		if (game.getGhostLairTime(GHOST.PINKY) == 0) {
			this.pinkyDist = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.PINKY));
		}

		if (game.getGhostLairTime(GHOST.SUE) == 0) {
			this.sueDist = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.SUE));
		}

		this.blinkyDir = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.BLINKY), DM.PATH);
		this.inkyDir = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.INKY), DM.PATH);
		this.pinkyDir = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.PINKY), DM.PATH);
		this.sueDir = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.SUE), DM.PATH);
		
		this.isGhostClose = game.isGhostClose(DISTANCE_CLOSE);
		this.closestGhostDistance = game.distanceToClosestGhost();
		this.directionToClosestPill = game.directionToClosesPill();
		this.closestGhost = game.closestGhost();
		this.dirAwayFromClosestGhost = game.getMoveAwayFromThreat(closestGhost);
		this.isPPClose = game.isPowerPillClose(DISTANCE_CLOSE); 		//Verify
		this.directionToClosestPP = game.directionToClosestPP();
		
		attributeValues = new String[17];
		attributeValues[0] = DirectionChosen.name();
		attributeValues[1] = String.valueOf(pacmanPosition);
		attributeValues[2] = String.valueOf(blinkyDist);
		attributeValues[3] = String.valueOf(inkyDist);
		attributeValues[4] = String.valueOf(pinkyDist);
		attributeValues[5] = String.valueOf(sueDist);
		attributeValues[6] = blinkyDir.name();
		attributeValues[7] = inkyDir.name();
		attributeValues[8] = pinkyDir.name();
		attributeValues[9] = sueDir.name();
		attributeValues[10] = String.valueOf(isGhostClose);
		attributeValues[11] = String.valueOf(closestGhostDistance);
		attributeValues[12] = directionToClosestPill.name();
		// add attribute as string
		attributeValues[13] = closestGhost.name();
		attributeValues[14] = dirAwayFromClosestGhost.name();
		attributeValues[15] = String.valueOf(isPPClose);
		attributeValues[16] = directionToClosestPP.name();
	}

	/**
	 * Used to building ID3 Tree
	 * @param data
	 */

	public ID3DataTuple(String data) {
		String[] dataSplit = data.split(";");
		attributeValues = dataSplit;
		this.DirectionChosen = MOVE.valueOf(dataSplit[0]);

		this.pacmanPosition = Integer.parseInt(dataSplit[1]);
		this.blinkyDist = Integer.parseInt(dataSplit[2]);
		this.inkyDist = Integer.parseInt(dataSplit[3]);
		this.pinkyDist = Integer.parseInt(dataSplit[4]);
		this.sueDist = Integer.parseInt(dataSplit[5]);
		this.blinkyDir = MOVE.valueOf(dataSplit[6]);
		this.inkyDir = MOVE.valueOf(dataSplit[7]);
		this.pinkyDir = MOVE.valueOf(dataSplit[8]);
		this.sueDir = MOVE.valueOf(dataSplit[9]);
		//custom attributes		
		this.isGhostClose = Boolean.parseBoolean(dataSplit[10]);
		this.closestGhostDistance = Integer.parseInt(dataSplit[11]);
		this.directionToClosestPill = MOVE.valueOf(dataSplit[12]);
		this.closestGhost = GHOST.valueOf(dataSplit[13]);
		this.dirAwayFromClosestGhost = MOVE.valueOf(dataSplit[14]);
		this.isPPClose = Boolean.parseBoolean(dataSplit[15]);
		this.directionToClosestPP = MOVE.valueOf(dataSplit[16]);
	}

	public String getSaveString() {
		StringBuilder stringbuilder = new StringBuilder();

		stringbuilder.append(this.DirectionChosen + ";");
		stringbuilder.append(this.pacmanPosition + ";");
		stringbuilder.append(this.blinkyDist + ";");
		stringbuilder.append(this.inkyDist + ";");
		stringbuilder.append(this.pinkyDist + ";");
		stringbuilder.append(this.sueDist + ";");
		stringbuilder.append(this.blinkyDir + ";");
		stringbuilder.append(this.inkyDir + ";");
		stringbuilder.append(this.pinkyDir + ";");
		stringbuilder.append(this.sueDir + ";");
		stringbuilder.append(this.isGhostClose + ";");
		stringbuilder.append(this.closestGhostDistance + ";"); 		//Not an actual attribute
		stringbuilder.append(this.directionToClosestPill + ";");
		stringbuilder.append(this.closestGhost + ";");
		stringbuilder.append(this.dirAwayFromClosestGhost + ";");
		stringbuilder.append(this.isPPClose + ";");
		stringbuilder.append(this.directionToClosestPP + ";");

		return stringbuilder.toString();
	}
	
	public String getAttributeValueAt(int i) {
		return attributeValues[i];
	}


	/**
	 * Used to normalize distances. Done via min-max normalization. Supposes
	 * that minimum possible distance is 0. Supposes that the maximum possible
	 * distance is 150.
	 * 
	 * @param dist
	 *            Distance to be normalized
	 * @return Normalized distance
	 */
	public double normalizeDistance(int dist) {
		return ((dist - 0) / (double) (this.maximumDistance - 0)) * (1 - 0) + 0;
	}

	public DiscreteTag discretizeDistance(int dist) {
		if (dist == -1)
			return DiscreteTag.NONE;
		double aux = this.normalizeDistance(dist);
		return DiscreteTag.DiscretizeDouble(aux);
	}

	public double normalizeLevel(int level) {
		return ((level - 0) / (double) (Constants.NUM_MAZES - 0)) * (1 - 0) + 0;
	}


	public double normalizeBoolean(boolean bool) {
		if (bool) {
			return 1.0;
		} else {
			return 0.0;
		}
	}

	public double normalizeTotalGameTime(int time) {
		return ((time - 0) / (double) (Constants.MAX_TIME - 0)) * (1 - 0) + 0;
	}

	public DiscreteTag discretizeTotalGameTime(int time) {
		double aux = this.normalizeTotalGameTime(time);
		return DiscreteTag.DiscretizeDouble(aux);
	}

	public double normalizeCurrentLevelTime(int time) {
		return ((time - 0) / (double) (Constants.LEVEL_LIMIT - 0)) * (1 - 0) + 0;
	}

	public DiscreteTag discretizeCurrentLevelTime(int time) {
		double aux = this.normalizeCurrentLevelTime(time);
		return DiscreteTag.DiscretizeDouble(aux);
	}

	/**
	 * 
	 * Max score value lifted from highest ranking PacMan controller on PacMan
	 * vs Ghosts website: http://pacman-vs-ghosts.net/controllers/1104
	 * 
	 * @param score
	 * @return
	 */
	public double normalizeCurrentScore(int score) {
		return ((score - 0) / (double) (82180 - 0)) * (1 - 0) + 0;
	}

	public DiscreteTag discretizeCurrentScore(int score) {
		double aux = this.normalizeCurrentScore(score);
		return DiscreteTag.DiscretizeDouble(aux);
	}

	@Override
	public String toString() {
		return "ID3DataTuple [DirectionChosen=" + DirectionChosen + ", pacmanPosition=" + pacmanPosition
				+ ", blinkyDist=" + blinkyDist + ", inkyDist=" + inkyDist + ", pinkyDist=" + pinkyDist + ", sueDist="
				+ sueDist + ", blinkyDir=" + blinkyDir + ", inkyDir=" + inkyDir + ", pinkyDir=" + pinkyDir + ", sueDir="
				+ sueDir + ", isGhostClose=" + isGhostClose + ", directionToClosestPill=" + directionToClosestPill
				+ "distanceToClosestGhost=" + closestGhostDistance + "]";
	}

	

	
	

}
