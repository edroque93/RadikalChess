package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase representativa de la Reina.
 * 
 * @author Quique
 */
@SuppressWarnings("serial")
public class Queen extends ChessPiece {

	public Queen(RadikalChessPlayer owner) {
		super(owner);
	}

	@Override
	public String getName() {
		return "Queen";
	}

	@Override
	public String unicodeSymbol() {
		return (getOwner() == RadikalChessPlayer.BLACK)? "\u265B" : "\u2655";
	}

	@Override
	public List<RadikalChessAction> getAction(RadikalChessState state, int x, int y) {
		return retrieveActionList(state, x, y, true);
	}

	/**
	 * @param state Un estado dado.
	 * @param x Coord.
	 * @param y Coord.
	 * @param depth Esta ficha puede alejarse del rey para dar jaque, check de profundidad.
	 * @return Acciones posibles.
	 */
	private List<RadikalChessAction> retrieveActionList(RadikalChessState state, int x, int y, boolean depth) {
		List<RadikalChessAction> actionList = new ArrayList<RadikalChessAction>();

		actionList.addAll(xAxisActionList(state, x, y, depth));
		actionList.addAll(yAxisActionList(state, x, y, depth));
		actionList.addAll(upperAxisActionList(state, x, y, depth));
		actionList.addAll(lowerAxisActionList(state, x, y, depth));

		return actionList;
	}
	
	/**
	 * @return Movimientos como una torre, eje X.
	 */
	private List<RadikalChessAction> xAxisActionList(RadikalChessState state, int x, int y, boolean depth) {
		List<RadikalChessAction> actionList = new ArrayList<RadikalChessAction>();

		for (int i = x + 1; i < state.getBoard().length; i++) {
			if (RadikalChessUtil.isAlly(state, i, y, getOwner())) {
				break;
			}

			if (!RadikalChessUtil.foundCollisionAll(state, i, y)) {
				if (RadikalChessUtil.kingRange(state, x, y, i, y, getOwner())) {
					actionList.add(new RadikalChessAction(x, y, i, y, false));	
				} else if (depth) {
					List<RadikalChessAction> testChechmate = retrieveActionList(state, i, y, false);

					for (RadikalChessAction action : testChechmate) {
						if (action.isCapture() && state.getBoard()[action.getX1()][action.getY1()] instanceof King) {
							actionList.add(new RadikalChessAction(x, y, i, y, false));
							break;
						}
					}
				}

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

			if (!RadikalChessUtil.foundCollisionAll(state, i, y)) {
				if (RadikalChessUtil.kingRange(state, x, y, i, y, getOwner())) {
					actionList.add(new RadikalChessAction(x, y, i, y, false));	
				} else if (depth) {
					List<RadikalChessAction> testChechmate = retrieveActionList(state, i, y, false);

					for (RadikalChessAction action : testChechmate) {
						if (action.isCapture() && state.getBoard()[action.getX1()][action.getY1()] instanceof King) {
							actionList.add(new RadikalChessAction(x, y, i, y, false));
							break;
						}
					}
				}

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
	 * @return Movimientos como una torre, eje Y.
	 */
	private List<RadikalChessAction> yAxisActionList(RadikalChessState state, int x, int y, boolean depth) {
		List<RadikalChessAction> actionList = new ArrayList<RadikalChessAction>();

		for (int j = y + 1; j < state.getBoard()[0].length; j++) {
			if (RadikalChessUtil.isAlly(state, x, j, getOwner())) {
				break;
			}

			if (!RadikalChessUtil.foundCollisionAll(state, x, j)) {
				if (RadikalChessUtil.kingRange(state, x, y, x, j, getOwner())) {
					actionList.add(new RadikalChessAction(x, y, x, j, false));	
				} else if (depth) {
					List<RadikalChessAction> testChechmate = retrieveActionList(state, x, j, false);

					for (RadikalChessAction action : testChechmate) {
						if (action.isCapture() && state.getBoard()[action.getX1()][action.getY1()] instanceof King) {
							actionList.add(new RadikalChessAction(x, y, x, j, false));
							break;
						}
					}
				}

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

			if (!RadikalChessUtil.foundCollisionAll(state, x, j)) {
				if (RadikalChessUtil.kingRange(state, x, y, x, j, getOwner())) {
					actionList.add(new RadikalChessAction(x, y, x, j, false));	
				} else if (depth) {
					List<RadikalChessAction> testChechmate = retrieveActionList(state, x, j, false);

					for (RadikalChessAction action : testChechmate) {
						if (action.isCapture() && state.getBoard()[action.getX1()][action.getY1()] instanceof King) {
							actionList.add(new RadikalChessAction(x, y, x, j, false));
							break;
						}
					}
				}

				continue;
			}

			if (RadikalChessUtil.isEnemy(state, x, j, getOwner())) {
				actionList.add(new RadikalChessAction(x, y, x, j, true));
				break;
			}
		}	
		return actionList;
	}

	/**
	 * @return Movimientos como un alfil, eje inferior.
	 */
	private List<RadikalChessAction> lowerAxisActionList(RadikalChessState state, int x, int y, boolean depth) {
		List<RadikalChessAction> actionList = new ArrayList<RadikalChessAction>();

		for (int j = y + 1; j < state.getBoard()[0].length; j++) {
			if (RadikalChessUtil.isAlly(state, x + (j - y), j, getOwner())) {
				break;
			}

			if (!RadikalChessUtil.foundCollisionAll(state, x + (j - y), j)) {
				if (RadikalChessUtil.kingRange(state, x, y, x + (j - y), j, getOwner())) {
					actionList.add(new RadikalChessAction(x, y, x + (j - y), j, false));	
				} else if (depth) {
					List<RadikalChessAction> testChechmate = retrieveActionList(state, x + (j - y), j, false);

					for (RadikalChessAction action : testChechmate) {
						if (action.isCapture() && state.getBoard()[action.getX1()][action.getY1()] instanceof King) {
							actionList.add(new RadikalChessAction(x, y, x + (j - y), j, false));
							break;
						}
					}
				}

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

			if (!RadikalChessUtil.foundCollisionAll(state, x - (j - y), j)) {
				if (RadikalChessUtil.kingRange(state, x, y, x - (j - y), j, getOwner())) {
					actionList.add(new RadikalChessAction(x, y, x - (j - y), j, false));	
				} else if (depth) {
					List<RadikalChessAction> testChechmate = retrieveActionList(state, x - (j - y), j, false);

					for (RadikalChessAction action : testChechmate) {
						if (action.isCapture() && state.getBoard()[action.getX1()][action.getY1()] instanceof King) {
							actionList.add(new RadikalChessAction(x, y, x - (j - y), j, false));
							break;
						}
					}
				}

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
	 * @return Movimientos como un alfil, eje superior.
	 */
	private List<RadikalChessAction> upperAxisActionList(RadikalChessState state, int x, int y, boolean depth) {
		List<RadikalChessAction> actionList = new ArrayList<RadikalChessAction>();
		for (int j = y - 1; j >= 0; j--) {
			if (RadikalChessUtil.isAlly(state, x + (y - j), j, getOwner())) {
				break;
			}

			if (!RadikalChessUtil.foundCollisionAll(state, x + (y - j), j)) {
				if (RadikalChessUtil.kingRange(state, x, y, x + (y - j), j, getOwner())) {
					actionList.add(new RadikalChessAction(x, y, x + (y - j), j, false));	
				} else if (depth) {
					List<RadikalChessAction> testChechmate = retrieveActionList(state, x + (y - j), j, false);

					for (RadikalChessAction action : testChechmate) {
						if (action.isCapture() && state.getBoard()[action.getX1()][action.getY1()] instanceof King) {
							actionList.add(new RadikalChessAction(x, y, x + (y - j), j, false));
							break;
						}
					}
				}

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

			if (!RadikalChessUtil.foundCollisionAll(state, x - (y - j), j)) {
				if (RadikalChessUtil.kingRange(state, x, y, x - (y - j), j, getOwner())) {
					actionList.add(new RadikalChessAction(x, y, x - (y - j), j, false));	
				} else if (depth) {
					List<RadikalChessAction> testChechmate = retrieveActionList(state, x - (y - j), j, false);

					for (RadikalChessAction action : testChechmate) {
						if (action.isCapture() && state.getBoard()[action.getX1()][action.getY1()] instanceof King) {
							actionList.add(new RadikalChessAction(x, y, x - (y - j), j, false));
							break;
						}
					}
				}

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