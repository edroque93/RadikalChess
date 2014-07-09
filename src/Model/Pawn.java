package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase representativa del Peón.
 * 
 * @author Quique
 */
@SuppressWarnings("serial")
public class Pawn extends ChessPiece {

	public Pawn(RadikalChessPlayer owner) {
		super(owner);
	}

	@Override
	public String getName() {
		return "Pawn";
	}

	@Override
	public String unicodeSymbol() {
		return (getOwner() == RadikalChessPlayer.BLACK)? "\u265F" : "\u2659";
	}

	@Override
	public List<RadikalChessAction> getAction(RadikalChessState state, int x, int y) {
		ArrayList<RadikalChessAction> ret = new ArrayList<>();

		ArrayList<RadikalChessAction> capture = tryCapture(state, x, y);
		RadikalChessAction forward = tryForward(state, x, y);

		if (!capture.isEmpty()) {
			ret.addAll(capture);
		}

		if (forward != null) {
			ret.add(forward);
		}

		return ret;
	}

	/**
	 * @param state Un estado dado.
	 * @param x Coord.
	 * @param y Coord.
	 * @return Acción de movimiento pasivo si posible.
	 */
	private RadikalChessAction tryForward(RadikalChessState state, int x, int y) {	
		switch (this.getOwner()) {
			case BLACK:
				if (!RadikalChessUtil.foundCollisionAll(state, x, y + 1)) {
					return new RadikalChessAction(x, y, x, y + 1, false);
				}
				break;
			case WHITE:
				if (!RadikalChessUtil.foundCollisionAll(state, x, y - 1)) {
					return new RadikalChessAction(x, y, x, y - 1, false);
				}
		}

		return null;
	}

	/**
	 * @param state Un estado dado.
	 * @param x Coord.
	 * @param y Coord.
	 * @return Movimientos de captura, si existen.
	 */
	private ArrayList<RadikalChessAction> tryCapture(RadikalChessState state, int x, int y) {
		ArrayList<RadikalChessAction> list = new ArrayList<>();
		
		switch (this.getOwner()) {
			case BLACK:
				if (RadikalChessUtil.isEnemy(state, x + 1, y + 1, RadikalChessPlayer.BLACK)) {
					list.add(new RadikalChessAction(x, y, x + 1, y + 1, true));
				}
				
				if (RadikalChessUtil.isEnemy(state, x - 1, y + 1, RadikalChessPlayer.BLACK)) {
					list.add(new RadikalChessAction(x, y, x - 1, y + 1, true));
				}
				break;
			case WHITE:
				if (RadikalChessUtil.isEnemy(state, x + 1, y - 1, RadikalChessPlayer.WHITE)) {
					list.add(new RadikalChessAction(x, y, x + 1, y - 1, true));
				}
				
				if (RadikalChessUtil.isEnemy(state, x - 1, y - 1, RadikalChessPlayer.WHITE)) {
					list.add(new RadikalChessAction(x, y, x - 1, y - 1, true));
				}
		}

		return list;
	}
}