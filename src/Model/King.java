package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase representativa del Rey.
 * 
 * @author Quique
 */
@SuppressWarnings("serial")
public class King extends ChessPiece {

	public King(RadikalChessPlayer owner) {
		super(owner);
	}

	@Override
	public String getName() {
		return "King";
	}

	@Override
	public String unicodeSymbol() {
		return (getOwner() == RadikalChessPlayer.BLACK)? "\u265A" : "\u2654";
	}

	@Override
	public List<RadikalChessAction> getAction(RadikalChessState state, int x, int y) {
		return getActionList(state,x ,y);
	}


	/**
	 * @param state Un estado dado.
	 * @param x Coord.
	 * @param y Coord.
	 * @return Lista de acciones que puede realizar el rey dado un estado y coordenadas.
	 */
	private List<RadikalChessAction> getActionList(RadikalChessState state, int x, int y) {
		List<RadikalChessAction> actionList = new ArrayList<RadikalChessAction>();
		
		// X + 1
		if (RadikalChessUtil.inRange(state, x, y, x + 1, y, getOwner())) {
			actionList.add(new RadikalChessAction(x, y, x + 1, y, false));
		} else if (RadikalChessUtil.isEnemy(state, x + 1, y, getOwner())) {
			actionList.add(new RadikalChessAction(x, y, x + 1, y, true));
		}
		// X - 1
		if (RadikalChessUtil.inRange(state, x, y, x - 1, y, getOwner())) {
			actionList.add(new RadikalChessAction(x, y, x - 1, y, false));
		} else if (RadikalChessUtil.isEnemy(state, x - 1, y, getOwner())) {
			actionList.add(new RadikalChessAction(x, y, x - 1, y, true));
		}
		// Y + 1
		if (RadikalChessUtil.inRange(state, x, y, x, y + 1, getOwner())) {
			actionList.add(new RadikalChessAction(x, y, x, y + 1, false));
		} else if (RadikalChessUtil.isEnemy(state, x, y + 1, getOwner())) {
			actionList.add(new RadikalChessAction(x, y, x, y + 1, true));
		}
		// Y - 1
		if (RadikalChessUtil.inRange(state, x, y, x, y - 1, getOwner())) {
			actionList.add(new RadikalChessAction(x, y, x, y - 1, false));
		} else if (RadikalChessUtil.isEnemy(state, x, y - 1, getOwner())) {
			actionList.add(new RadikalChessAction(x, y, x, y - 1, true));
		}
		
		// X + 1, Y + 1
		if (RadikalChessUtil.inRange(state, x, y, x + 1, y + 1, getOwner())) {
			actionList.add(new RadikalChessAction(x, y, x + 1, y + 1, false));
		} else if (RadikalChessUtil.isEnemy(state, x + 1, y + 1, getOwner())) {
			actionList.add(new RadikalChessAction(x, y, x + 1, y + 1, true));
		}
		// X + 1, Y - 1
		if (RadikalChessUtil.inRange(state, x, y, x + 1, y - 1, getOwner())) {
			actionList.add(new RadikalChessAction(x, y, x + 1, y - 1, false));
		} else if (RadikalChessUtil.isEnemy(state, x + 1, y - 1, getOwner())) {
			actionList.add(new RadikalChessAction(x, y, x + 1, y - 1, true));
		}
		// X - 1, Y + 1
		if (RadikalChessUtil.inRange(state, x, y, x - 1, y + 1, getOwner())) {
			actionList.add(new RadikalChessAction(x, y, x - 1, y + 1, false));
		} else if (RadikalChessUtil.isEnemy(state, x - 1, y + 1, getOwner())) {
			actionList.add(new RadikalChessAction(x, y, x - 1, y + 1, true));
		}
		// X - 1, Y - 1
		if (RadikalChessUtil.inRange(state, x, y, x - 1, y - 1, getOwner())) {
			actionList.add(new RadikalChessAction(x, y, x - 1, y - 1, false));
		} else if (RadikalChessUtil.isEnemy(state, x - 1, y - 1, getOwner())) {
			actionList.add(new RadikalChessAction(x, y, x - 1, y - 1, true));
		}
			
		return actionList;
	}

}