package Model;

import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;

/**
 * Clase extendida de la búsqueda iterativa alpha-beta.
 * Se ha optimizado la propia clase superior (situación de una sola acción,
 * o ninguna).
 * 
 * @author Quique
 */
public class ABSearch extends 
		IterativeDeepeningAlphaBetaSearch<RadikalChessState, RadikalChessAction, RadikalChessPlayer>{

	public ABSearch(RadikalChessGame game, double utilMin, double utilMax, int time) {
		super(game, utilMin, utilMax, time);
	}
	
	/* 
	 * Cambios en la manera de evaluación (no solo nodos hoja).
	 */
	@Override
	protected double eval(RadikalChessState state, RadikalChessPlayer player) {
		if (game.isTerminal(state)) {
			return game.getUtility(state, player);
		} else {
			maxDepthReached = true;
			return game.getUtility(state, player);
		}
	}
}