package dataRecording;

import java.util.ArrayList;

import pacman.game.Constants;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.STRATEGY;
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

	public STRATEGY strategyChosen;

	// General game state this - not normalized!

	public boolean isGhostClose;
	public int DISTANCE_CLOSE = 35;
	public boolean isPPClose = false;
	public boolean closestGhostEdible = false;
	// Util data - useful for normalization
	private int maximumDistance = 150;

	/**
	 * Used for playing Pacman real-time.
	 * 
	 * @param game
	 * @param move
	 */
	public ID3DataTuple(Game game, MOVE move) {
		if (move == MOVE.NEUTRAL) {
			move = game.getPacmanLastMoveMade();
		}

		this.strategyChosen = game.getStrategy(DISTANCE_CLOSE);

		this.isGhostClose = game.isGhostClose(DISTANCE_CLOSE);
		this.isPPClose = game.isPowerPillClose(game.distanceToClosestGhost());
		this.closestGhostEdible = game.isClosestGhostEdible();

		attributeValues = new String[4];
		attributeValues[0] = strategyChosen.name();
		attributeValues[1] = String.valueOf(isGhostClose);
		attributeValues[2] = String.valueOf(isPPClose);
		attributeValues[3] = String.valueOf(closestGhostEdible);
	}

	/**
	 * Used to building ID3 Tree
	 * 
	 * @param data
	 */

	public ID3DataTuple(String data) {
		String[] dataSplit = data.split(";");
		attributeValues = dataSplit;
		this.strategyChosen = STRATEGY.valueOf(dataSplit[0]);
		// custom attributes
		this.isGhostClose = Boolean.parseBoolean(dataSplit[1]);
		this.isPPClose = Boolean.parseBoolean(dataSplit[2]);
		this.closestGhostEdible = Boolean.parseBoolean(dataSplit[3]);
	}

	public String getSaveString() {
		StringBuilder stringbuilder = new StringBuilder();

		stringbuilder.append(this.strategyChosen + ";");
		stringbuilder.append(this.isGhostClose + ";");
		stringbuilder.append(this.isPPClose + ";");
		stringbuilder.append(this.closestGhostEdible + ";");

		return stringbuilder.toString();
	}

	public String getAttributeValueAt(int i) {
		return attributeValues[i];
	}

	/**
	 * Used to normalize distances. Done via min-max normalization. Supposes that
	 * minimum possible distance is 0. Supposes that the maximum possible distance
	 * is 150.
	 * 
	 * @param dist Distance to be normalized
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
	 * Max score value lifted from highest ranking PacMan controller on PacMan vs
	 * Ghosts website: http://pacman-vs-ghosts.net/controllers/1104
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ID3DataTuple))
			return false;
		ID3DataTuple other = (ID3DataTuple) obj;
		if (closestGhostEdible != other.closestGhostEdible)
			return false;
		if (isGhostClose != other.isGhostClose)
			return false;
		if (isPPClose != other.isPPClose)
			return false;
		if (strategyChosen != other.strategyChosen)
			return false;
		return true;
	}

}
