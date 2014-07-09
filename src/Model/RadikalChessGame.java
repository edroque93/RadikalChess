package Model;

import java.util.ArrayList;
import java.util.List;

import aima.core.search.adversarial.Game;

/**
 * Clase que implementa la clase Game de AIMA, donde se define como
 * una representación de estado, una representación de acción y representación
 * de jugador. Se implementan los métodos establecidos como getActions o isTerminal, 
 * entre otros.
 * 
 * @author Quique
 */
public class RadikalChessGame implements Game<RadikalChessState, RadikalChessAction, RadikalChessPlayer>{

	private RadikalChessPlayer[] players = {RadikalChessPlayer.WHITE, RadikalChessPlayer.BLACK};
	private RadikalChessState initialState;
	private double[] heuristicsw = new double[]{1., 1., 1., 1., 1.};

	public RadikalChessGame() {
		initialState = new RadikalChessState();
	}

	public RadikalChessGame(RadikalChessState initialState) {
		this.initialState = initialState;
	}

	@Override
	public RadikalChessState getInitialState() {
		return initialState;
	}

	@Override
	public RadikalChessPlayer[] getPlayers() {
		return players;
	}

	@Override
	public RadikalChessPlayer getPlayer(RadikalChessState state) {
		return state.getPlayer();
	}

	@Override
	public List<RadikalChessAction> getActions(RadikalChessState state) {
		ChessPiece[][] board = state.getBoard();
		RadikalChessPlayer player = state.getPlayer();
		List<RadikalChessAction> actions = new ArrayList<>();

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] != null && board[i][j].getOwner() == player) {
					actions.addAll(board[i][j].getAction(state, i, j));
				}
			}
		}

		return actions;
	}

	@Override
	public RadikalChessState getResult(RadikalChessState state, RadikalChessAction action) {
		RadikalChessState result = state.clone();
		ChessPiece[][] board = result.getBoard();

		board[action.getX1()][action.getY1()] = board[action.getX0()][action.getY0()];
		board[action.getX0()][action.getY0()] = null;
		result.newQueen();
		result.setPlayer((state.getPlayer() == RadikalChessPlayer.BLACK) ?
				RadikalChessPlayer.WHITE :
					RadikalChessPlayer.BLACK);

		double utility = 0.;

		utility += result.basicLogic() * heuristicsw[0];
		utility += result.heuristicOne() * heuristicsw[1];
		utility += result.heuristicTwo() * heuristicsw[2];
		utility += result.heuristicThree() * heuristicsw[3];
		utility += result.heuristicFour() * heuristicsw[4];

		result.setUtility(utility);
		
		return result;
	}

	@Override
	public boolean isTerminal(RadikalChessState state) {
		ChessPiece[][] board = state.getBoard();
		int kingCount = 0;

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] != null && board[i][j] instanceof King) {
					if (++kingCount == 2) return !canMove(state);
				}
			}
		}

		return true;
	}

	/**
	 * @param state Un estado dado.
	 * @return Si existen movimientos para un jugador dado un estado.
	 */
	private boolean canMove(RadikalChessState state) {
		ChessPiece[][] board = state.getBoard();

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] != null && 
						board[i][j].getOwner() == state.getPlayer() && 
						board[i][j].getAction(state, i, j).size() > 0) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public double getUtility(RadikalChessState state, RadikalChessPlayer player) {
		if (player == state.getPlayer())
			return state.getUtility();
		else
			return -state.getUtility();
	}

	/**
	 * @return Pesos usados por la IA, Getter.
	 */
	public double[] getHeuristicsw() {
		return heuristicsw;
	}

	/**
	 * @param heuristicsw Pesos usados por la IA, Setter.
	 */
	public void setHeuristicsw(double[] heuristicsw) {
		this.heuristicsw = heuristicsw;
	}
	
}