package Model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * Clase con funciones de utilidad para el programa.
 * 
 * @author Quique
 */
public class RadikalChessUtil {

	/**
	 * @return Colisión con las fichas del mismo jugador o límite.
	 */
	public static boolean foundCollision(RadikalChessState state, int x, int y, RadikalChessPlayer owner) {
		if (!inBounds(state, x, y)) return true;

		ChessPiece[][] board = state.getBoard();

		if (board[x][y] != null && board[x][y].getOwner() == owner) return true;

		return false;
	}

	/**
	 * @return Colisión con cualquier ficha o limite.
	 */
	public static boolean foundCollisionAll(RadikalChessState state, int x, int y) {
		if (!inBounds(state, x, y)) return true;

		ChessPiece[][] board = state.getBoard();

		if (board[x][y] != null) return true;

		return false;
	}

	/**
	 * @return Si las coordenadas en el estado se encuentra en los límites de este.
	 */
	public static boolean inBounds(RadikalChessState state, int x, int y) {
		if (x < 0 || y < 0) return false;

		ChessPiece[][] board = state.getBoard();

		if (x >= board.length || y >= board[0].length) return false;

		return true;
	}

	/**
	 * @return Si la ficha en el estado y la posición indicada es del jugador enemigo.
	 */
	public static boolean isEnemy(RadikalChessState state, int x, int y, RadikalChessPlayer owner) {
		ChessPiece[][] board = state.getBoard();

		if (inBounds(state, x, y)  && 
				board[x][y] != null && 
				board[x][y].getOwner() != owner) {
			return true;
		}

		return false;
	}

	/**
	 * @return Si la ficha en el estado y la posición indicada es del juador.
	 */
	public static boolean isAlly(RadikalChessState state, int x, int y, RadikalChessPlayer owner) {
		ChessPiece[][] board = state.getBoard();

		if (inBounds(state, x, y)  && 
				board[x][y] != null && 
				board[x][y].getOwner() == owner) {
			return true;
		}

		return false;
	}

	/**
	 * @return Si una ficha no ha colisionado con algo y está en el rango del rey enemigo.
	 */
	public static boolean inRange(RadikalChessState state, int x0, int y0, int x1, int y1, RadikalChessPlayer player) {
		return  !foundCollisionAll(state, x1, y1) && kingRange(state, x0, y0, x1, y1, player);
	}
	
	/**
	 * @param state Un estado.
	 * @param x0,y0 Coordenada inicial.
	 * @param x1,y1 Coordenada final.
	 * @param player Jugador.
	 * @return La distancia con el rey enemigo del jugador.
	 */
	public static boolean kingRange(RadikalChessState state, int x0, int y0, int x1, int y1, RadikalChessPlayer player) {
		int[] king = getEnemyKing(state, player);
		
		return euclidea(king[0], king[1], x1, y1) < euclidea(king[0], king[1], x0, y0);
	}

	/**
	 * @return La distancia manhattan
	 */
	public static int manhattan(int kx, int ky, int x0, int y0) {
		return Math.abs(kx - x0) + Math.abs(ky - y0);
	}
	
	/**
	 * @return La distancia euclídea.
	 */
	public static int euclidea(int kx, int ky, int x0, int y0) {
		return ((kx-x0) * (kx-x0)) + ((ky-y0) * (ky-y0));
	}

	/**
	 * @param state Un estado.
	 * @param owner Dueño.
	 * @return La posición del rey enemigo.
	 */
	public static int[] getEnemyKing(RadikalChessState state, RadikalChessPlayer owner) {
		ChessPiece[][] board = state.getBoard();
		int[] king = new int[2];

		switch (owner) {
			case BLACK:
				for (int i = 0; i < board.length; i++) {
					for (int j = 0; j < board[0].length; j++) {
						if (board[i][j] != null && 
								board[i][j] instanceof King && 
								board[i][j].getOwner() == RadikalChessPlayer.WHITE) {
							king[0] = i;
							king[1] = j;
							return king;
						}
					}
				}
				break;
			case WHITE:
				for (int i = 0; i < board.length; i++) {
					for (int j = 0; j < board[0].length; j++) {
						if (board[i][j] != null && 
								board[i][j] instanceof King && 
								board[i][j].getOwner() == RadikalChessPlayer.BLACK) {
							king[0] = i;
							king[1] = j;
							return king;
						}
					}
				}
		}

		return king;
	}
	
	/**
	 * @param state Un estado.
	 * @param action Una acción.
	 * @return La string con las coordenadas en versión "natural".
	 */
	public static String actionToNatural(RadikalChessState state, RadikalChessAction action) {
		int i = action.getX0();
		int j = action.getY0();
		int x = action.getX1();
		int y = action.getY1();
		
		return "[" + (char) (65 + i) + "" +  (state.getBoard()[0].length - j) + " to " + (char) (65 + x) + "" +  (state.getBoard()[0].length - y) + 
				(action.isCapture()? ", capture" : "") + "]";
	}

	/**
	 * @param player Jugador.
	 * @return El otro jugador.
	 */
	public static RadikalChessPlayer otherPlayer(RadikalChessPlayer player) {
		if (player == RadikalChessPlayer.BLACK) {
			return RadikalChessPlayer.WHITE;
		} else {
			return RadikalChessPlayer.BLACK;
		}
	}

	/**
	 * Utilidad para guardar un estado en un fichero (objeto serializable).
	 * 
	 * @param state Estado que se ha de guardar.
	 * @param filename Nombre del fichero.
	 */
	public static void saveToFile(RadikalChessState state, String filename) {
		FileOutputStream out = null;

		try {
			out = new FileOutputStream(filename);
		} catch (FileNotFoundException e) {
			System.out.println("Couldn't save to file.");
		}

		ObjectOutputStream obj;

		if (out != null)
			try {
				obj = new ObjectOutputStream(out);
				obj.writeObject(state);
				obj.close();
			} catch (IOException e) {
				System.out.println("Couldn't save to file.");
			}
	}

	/**
	 * Utilidad para cargar un estado de un fichero (objeto serializable).
	 * 
	 * @param filename Nombre del fichero a cargar.
	 * @return Estado del fichero cargado.
	 */
	public static RadikalChessState loadFromFile(String filename) {
		FileInputStream in = null;
		RadikalChessState state = null;

		try {
			in = new FileInputStream(filename);
		} catch (FileNotFoundException e) {
			System.out.println("Couldn't load from file.");
		}

		ObjectInputStream obj;

		if (in != null)
			try {
				obj = new ObjectInputStream(in);
				state = (RadikalChessState) obj.readObject();
				obj.close();
			} catch (IOException | ClassNotFoundException e) {
				System.out.println("Couldn't load from file.");
			}

		return state;
	}
	
	/**
	 * La partida media en este juego es sobre los 20-25 movimientos.
	 * La generación aleatoria puede adelantar la situación como máximo
	 * hasta 10 movimientos.
	 * 
	 * Aleatorio es par? -> tablero para blancas.
	 * Aleatorio es impar? -> tablero para negras.
	 * 
	 * @return Un tablero aleatorio para realizar pruebas en el.
	 */
	public static RadikalChessState getRandomBoard(RadikalChessGame game, RadikalChessPlayer player) {
		RadikalChessState random = new RadikalChessState();
		int rand = new Random().nextInt(11); // 0..10 inclusive
		
		for (int i = 0; i < rand; i++) {
			random = game.getResult(random, randomAction(game, random));
		}
		
		if (rand % 2 == 0) {
			if (player == RadikalChessPlayer.BLACK) {
				random = game.getResult(random, randomAction(game, random));
			}
		} else {
			if (player == RadikalChessPlayer.WHITE) {
				random = game.getResult(random, randomAction(game, random));
			}
		}
		
		return random;
	}
	
	/**
	 * @param game Juego
	 * @param state Un estado dado
	 * @return Una acción aleatoria para ese estado
	 */
	public static RadikalChessAction randomAction(RadikalChessGame game, RadikalChessState state) {
		ArrayList<RadikalChessAction> actions = (ArrayList<RadikalChessAction>) game.getActions(state);
		int rand = new Random().nextInt(actions.size());
		int count = 0;
		
		for (RadikalChessAction action : actions) {
			if (count == rand) {
				return action;
			} else {
				count++;
			}
		}
		
		return null;
	}
}