package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase representativa de la Torre.
 * 
 * @author Quique
 */
@SuppressWarnings("serial")
public class Rook extends ChessPiece {

	public Rook(RadikalChessPlayer owner) {
		super(owner);
	}

	@Override
	public String getName() {
		return "Rook";
	}

	@Override
	public String unicodeSymbol() {
		return (getOwner() == RadikalChessPlayer.BLACK)? "\u265C" : "\u2656";
	}

	@Override
	public List<RadikalChessAction> getAction(RadikalChessState state, int x, int y) {
		List<RadikalChessAction> actionList = new ArrayList<RadikalChessAction>();

		actionList.addAll(xAxisActionList(state, x, y));
		actionList.addAll(yAxisActionList(state, x, y));

		return actionList;
	}

	/**
	 * @return Lista de movimientos en el eje X.
	 */
	private List<RadikalChessAction> xAxisActionList(RadikalChessState state, int x, int y) {
		List<RadikalChessAction> actionList = new ArrayList<RadikalChessAction>();

		for (int i = x + 1; i < state.getBoard().length; i++) {
			if (RadikalChessUtil.isAlly(state, i, y, getOwner())) {
				break;
			}

			if (RadikalChessUtil.inRange(state, x, y, i, y, getOwner())) {
				actionList.add(new RadikalChessAction(x, y, i, y, false));
				continue;
			}

			if (RadikalChessUtil.isEnemy(state, i, y, getOwner())) {
				actionList.add(new RadikalChessAction(x, y, i, y, true));
				break;
			}
		}
		for (int i = x - 1; i >= 0; i--) {
			if (RadikalChessUtil.isAlly(state, i, y, getOwner())) {
				break;
			}

			if (RadikalChessUtil.inRange(state, x, y, i, y, getOwner())){
				actionList.add(new RadikalChessAction(x, y, i, y, false));
				continue;
			} 

			if (RadikalChessUtil.isEnemy(state, i, y, getOwner())) {
				actionList.add(new RadikalChessAction(x, y, i, y, true));
				break;
			}
		}	

		return actionList;
	}

	/**
	 * @return Lista de movimientos en el eje Y.
	 */
	private List<RadikalChessAction> yAxisActionList(RadikalChessState state, int x, int y) {
		List<RadikalChessAction> actionList = new ArrayList<RadikalChessAction>();
		
		for (int j = y + 1; j < state.getBoard()[0].length; j++) {
			if (RadikalChessUtil.isAlly(state, x, j, getOwner())) {
				break;
			}

			if (RadikalChessUtil.inRange(state, x, y, x, j, getOwner())){
				actionList.add(new RadikalChessAction(x, y, x, j, false));
				continue;
			}

			if (RadikalChessUtil.isEnemy(state, x, j, getOwner())) {
				actionList.add(new RadikalChessAction(x, y, x, j, true));
				break;
			}
		}
		for (int j = y - 1; j >= 0; j--) {
			if (RadikalChessUtil.isAlly(state, x, j, getOwner())) {
				break;
			}

			if (RadikalChessUtil.inRange(state, x, y, x, j, getOwner())){
				actionList.add(new RadikalChessAction(x, y, x, j, false));
				continue;
			} 

			if (RadikalChessUtil.isEnemy(state, x, j, getOwner())) {
				actionList.add(new RadikalChessAction(x, y, x, j, true));
				break;
			}
		}	
		return actionList;
	}
}