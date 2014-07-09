package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Clase que representa un estado. Implementa Cloneable y Serializable 
 * para permitir el clonado del estado y el guardado en disco.
 * 
 * @author Quique
 */
public class RadikalChessState implements Cloneable, Serializable {

	private static final long serialVersionUID = 2748054592927672122L;
	private ChessPiece[][] board;
	private double utility = 0.;
	private RadikalChessPlayer player = RadikalChessPlayer.WHITE;

	/**
	 * Constructor de estado, permite modificación de tamaño del tablero.
	 */
	public RadikalChessState() {
		board = new ChessPiece[4][6];
		fillClassic();
	}

	/**
	 * Rellena el tablero con la configuración especificada.
	 */
	private void fillClassic() {
		Pawn blackPawn = new Pawn(RadikalChessPlayer.BLACK);
		Pawn whitePawn = new Pawn(RadikalChessPlayer.WHITE);

		board[3][5] = new King(RadikalChessPlayer.WHITE);
		board[2][5] = new Queen(RadikalChessPlayer.WHITE);
		board[1][5] = new Bishop(RadikalChessPlayer.WHITE);
		board[0][5] = new Rook(RadikalChessPlayer.WHITE);
		board[3][4] = whitePawn;
		board[2][4] = whitePawn;
		board[1][4] = whitePawn;
		board[0][4] = whitePawn;

		board[0][0] = new King(RadikalChessPlayer.BLACK);
		board[1][0] = new Queen(RadikalChessPlayer.BLACK);
		board[2][0] = new Bishop(RadikalChessPlayer.BLACK);
		board[3][0] = new Rook(RadikalChessPlayer.BLACK);
		board[0][1] = blackPawn;
		board[1][1] = blackPawn;
		board[2][1] = blackPawn;
		board[3][1] = blackPawn;
	}

	public RadikalChessState(ChessPiece[][] board) {
		this.board = board;
	}

	public ChessPiece[][] getBoard() {
		return board;
	}

	public void setBoard(ChessPiece[][] board) {
		this.board = board;
	}

	/**
	 * Imprime con caracteres unicode el tablero.
	 */
	@Override
	public String toString() {
		final String blank = "\u0020";
		final String hbar = "\u2500";
		final String vbar = "\u2502";
		final String cbar = "\u253C";
		final String tbar = "\u252C";
		final String itbar = "\u2534";
		final String vlbar = "\u251C";
		final String vrbar = "\u2524";
		final String tlcorner = "\u250C";
		final String trcorner = "\u2510";
		final String blcorner = "\u2514";
		final String brcorner = "\u2518";

		StringBuilder builder = new StringBuilder();

		builder.append(blank + blank + tlcorner);

		for (int i = 0; i < board.length-1; i++) {
			builder.append(hbar + hbar + hbar);
			builder.append(tbar);
		}

		builder.append(hbar + hbar + hbar);
		builder.append(trcorner);

		builder.append("\n");

		for (int j = 0; j < board[0].length; j++) {
			builder.append((board[0].length - j) + blank + vbar);

			for (int i = 0; i < board.length; i++) {
				if (board[i][j] != null) {
					builder.append(blank + board[i][j].toString() + blank);
				} else {
					builder.append(blank + blank + blank);
				}
				builder.append(vbar);
			}
			builder.append("\n");

			if (j != board[0].length-1) {
				builder.append(blank + blank + vlbar);
			} else {
				builder.append(blank + blank + blcorner);
			}

			for (int i = 0; i < board.length; i++) {
				if (i != board.length-1) {
					if (j != board[0].length-1) {
						builder.append(hbar + hbar + hbar + cbar);
					} else {
						builder.append(hbar + hbar + hbar + itbar);
					}
				} else {
					if (j == board[0].length-1) {
						builder.append(hbar + hbar + hbar + brcorner);
					} else {
						builder.append(hbar + hbar + hbar + vrbar);
					}
				}
			}
			builder.append("\n");
		}

		builder.append(blank + blank + blank);

		for (int i = 0; i < board.length; i++) {
			builder.append(blank + (char) (65+i) + blank + blank);
		}

		return builder.toString();
	}

	/**
	 * Realmente solo se crean las piezas en el tablero original,
	 * luego jugamos con las referencias de las fichas. Con esto
	 * conseguimos que el clonado sea ligero.
	 */
	@Override
	public RadikalChessState clone() {
		RadikalChessState copy = null;

		try {
			copy = (RadikalChessState) super.clone();
			ChessPiece[][] newBoard = new ChessPiece[board.length][board[0].length];

			for (int i = 0; i < newBoard.length; i++) {
				for (int j = 0; j < newBoard[0].length; j++) {
					newBoard[i][j] = board[i][j];
				}
			}
			copy.board =  newBoard;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		return copy;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RadikalChessState) {
			RadikalChessState s = (RadikalChessState) obj;
			ChessPiece[][] sboard = s.getBoard();

			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[0].length; j++) {
					if (board[i][j] != sboard[i][j])
						return false;
				}
			}
			return true;
		}

		return false;
	}

	public RadikalChessPlayer getPlayer() {
		return player;
	}

	public void setPlayer(RadikalChessPlayer player) {
		this.player = player;
	}

	/**
	 * Función que comprueba si algún peón ha llegado al límite y puede 
	 * ser una nueva reina.
	 */
	public void newQueen() {
		int row = (player == RadikalChessPlayer.BLACK) ?  board[0].length-1 : 0;

		for (int i = 0; i < board.length; i++) {
			if (board[i][row] != null && board[i][row] instanceof Pawn) {
				board[i][row] = getQueen();
				break;
			}
		}
	}

	/**
	 * Como jugamos con referencias en el estado, optimizamos si 
	 * encontramos una reina del jugador (para obtener su referencia)
	 * en lugar de crear otra. Todo sea por agilizar el clonado de un 
	 * estado.
	 * 
	 * @return Una reina.
	 */
	private ChessPiece getQueen() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] != null && 
						board[i][j] instanceof Queen && 
						board[i][j].getOwner() == player) {
					return board[i][j];
				}
			}
		}

		return new Queen(player);
	}

	/**
	 * Tabla de heurísticas implementadas:
	 * 
	 * 		- BasicLogic: win/lose/normal state.
	 *		- HeuristicOne: diferencia de valor de piezas.
	 *		- HeuristicTwo: diferencia de movimientos disponibles.
	 *		- HeuristicThree: diferencia de piezas disponibles.
	 *		- HeuristicFour: suma de movimientos seguros.
	 */
	public double getUtility() {
		return utility;
	}

	public void setUtility(double utility) {
		this.utility = utility;
	}

	/**
	 * @return Valor de heurística básica (victoria, derrota, nulo).
	 */
	public double basicLogic() {
		double base = 0.;

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] != null && board[i][j] instanceof King) {
					if (board[i][j].getOwner() == player) {
						base += 1000.;
					} else {
						base -= 1000.;
					}
				}
			}
		}

		return base ;
	}

	/**
	 * @return Diferencia del valor de las piezas.
	 */
	public double heuristicOne() {
		double utility = 0.;

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] != null) {
					utility += (board[i][j].getOwner() == player) ? 
							valueOf(board[i][j], i, j) :
								-valueOf(board[i][j], i , j);
				}
			}
		}

		return utility;
	}

	/**
	 * Función que nos devuelve el valor de la pieza que le 
	 * pasemos por parámetro.
	 * 
	 * @param chessPiece Pieza.
	 * @param i Coord.
	 * @param j Coord.
	 * @return Valor de la pieza.
	 */
	private int valueOf(ChessPiece chessPiece, int i, int j) {
		if (chessPiece instanceof King) {
			return 10;
		} else if (chessPiece instanceof Queen) {
			return 5;
		} else if (chessPiece instanceof Rook) {
			return 4;
		} else if (chessPiece instanceof Bishop) {
			return 3;
		} else if (chessPiece instanceof Pawn) {
			if (player == RadikalChessPlayer.WHITE) {
				return (i >= board.length/2 -1) ? 2 : 1;	
			} else {
				return (i <= board.length/2 -1) ? 2 : 1;
			}
		}
		else return 0;
	}

	/**
	 * @return Diferencia de movimientos disponibles.
	 */
	public double heuristicTwo() {
		double utility = 0.;

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] != null) {
					utility += (board[i][j].getOwner() == player) ?
							board[i][j].getAction(this, i, j).size() :
								-board[i][j].getAction(this, i, j).size();
				}
			}
		}

		return utility;
	}

	/**
	 * @return Diferencia de piezas.
	 */
	public double heuristicThree() {
		double utility = 0.;

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] != null) {
					utility += (board[i][j].getOwner() == player) ? 1.0 : -1.0;
				}
			}
		}

		return utility;
	}

	/**
	 * @return Suma de movimientos seguros.
	 */
	public double heuristicFour() {
		List<RadikalChessAction> moves = new ArrayList<>();
		List<RadikalChessAction> enemy = new ArrayList<>();

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] != null) {
					if (board[i][j].getOwner() == player) {
						moves.addAll(board[i][j].getAction(this, i, j));
					} else {
						enemy.addAll(board[i][j].getAction(this, i, j));
					}
				}
			}
		}

		Iterator<RadikalChessAction> iterator = moves.iterator();
		while (iterator.hasNext()) {
			RadikalChessAction action = iterator.next();

			Iterator<RadikalChessAction> eiterator = enemy.iterator();
			while (eiterator.hasNext()) {
				RadikalChessAction eaction = eiterator.next();

				if (eaction.getX1() == action.getX1() && eaction.getY1() == action.getY1()) {
					iterator.remove();
					break;
				}
			}
		}

		return moves.size();
	}

}