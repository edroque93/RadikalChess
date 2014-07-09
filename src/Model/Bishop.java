package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase representativa del Alfil.
 * 
 * @author Quique
 */
@SuppressWarnings("serial")
public class Bishop extends ChessPiece {

	public Bishop(RadikalChessPlayer owner) {
		super(owner);
	}

	@Override
	public String getName() {
		return "Bishop";
	}

	@Override
	public String unicodeSymbol() {
		return (getOwner() == RadikalChessPlayer.BLACK)? "\u265D" : "\u2657";
	}

	@Override
	public List<RadikalChessAction> getAction(RadikalChessState state, int x, int y) {
		return retrieveActionList(state, x, y);
	}

	/**
	 * @param state Un estado dado.
	 * @param x Coord.
	 * @param y Coord.
	 * @return Lista de acciones posibles dado el estado y las coordenadas.
	 */
	private List<RadikalChessAction> retrieveActionList(RadikalChessState state, int x, int y) {
		List<RadikalChessAction> actionList = new ArrayList<RadikalChessAction>();

		actionList.addAll(upperAxisActionList(state, x, y));
		actionList.addAll(lowerAxisActionList(state, x, y));

		return actionList;
	}

	/**
	 * @return Trabaja la parte inferior del tablero (visto como matriz).
	 */
	private List<RadikalChessAction> lowerAxisActionList(RadikalChessState state, int x, int y) {
		List<RadikalChessAction> actionList = new ArrayList<RadikalChessAction>();

		for (int j = y + 1; j < state.getBoard()[0].length; j++) {
			if (RadikalChessUtil.isAlly(state, x + (j - y), j, getOwner())) {
				break;
			}

			if (RadikalChessUtil.inRange(state, x, y, x + (j - y), j, getOwner())){
				actionList.add(new RadikalChessAction(x, y, x + (j - y), j, false));
				continue;
			}

			if (RadikalChessUtil.isEnemy(state, x + (j - y), j, getOwner())) {
				actionList.add(new RadikalChessAction(x, y, x + (j - y), j, true));
				break;
			}
		}
		for (int j = y + 1; j < state.getBoard()[0].length; j++) {
			if (RadikalChessUtil.isAlly(state, x - (j - y), j, getOwner())) {
				break;
			}

			if (RadikalChessUtil.inRange(state, x, y, x - (j - y), j, getOwner())){
				actionList.add(new RadikalChessAction(x, y, x - (j - y), j, false));
				continue;
			}

			if (RadikalChessUtil.isEnemy(state, x - (j - y), j, getOwner())) {
				actionList.add(new RadikalChessAction(x, y, x - (j - y), j, true));
				break;
			}
		}

		return actionList;
	}

	/**
	 * @return Trabaja la parte superior del tablero (visto como matriz).
	 */
	private List<RadikalChessAction> upperAxisActionList(RadikalChessState state, int x, int y) {
		List<RadikalChessAction> actionList = new ArrayList<RadikalChessAction>();
		for (int j = y - 1; j >= 0; j--) {
			if (RadikalChessUtil.isAlly(state, x + (y - j), j, getOwner())) {
				break;
			}

			if (RadikalChessUtil.inRange(state, x, y, x + (y - j), j, getOwner())){
				actionList.add(new RadikalChessAction(x, y, x + (y - j), j, false));
				continue;
			}

			if (RadikalChessUtil.isEnemy(state, x + (y - j), j, getOwner())) {
				actionList.add(new RadikalChessAction(x, y, x + (y - j), j, true));
				break;
			}
		}
		for (int j = y - 1; j >= 0; j--) {
			if (RadikalChessUtil.isAlly(state, x - (y - j), j, getOwner())) {
				break;
			}

			if (RadikalChessUtil.inRange(state, x, y, x - (y - j), j, getOwner())){
				actionList.add(new RadikalChessAction(x, y, x - (y - j), j, false));
				continue;
			}

			if (RadikalChessUtil.isEnemy(state, x - (y - j), j, getOwner())) {
				actionList.add(new RadikalChessAction(x, y, x - (y - j), j, true));
				break;
			}
		}
		return actionList;
	}

}