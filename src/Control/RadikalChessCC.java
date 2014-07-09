package Control;

import Model.ABSearch;
import Model.RadikalChessAction;
import Model.RadikalChessGame;
import Model.RadikalChessPlayer;
import Model.RadikalChessState;
import View.RadikalChessConsole;

/**
 * Clase de control para el juego computador-computador.
 * 
 * @author Quique
 */
public class RadikalChessCC {

	private static RadikalChessCC instance;
	private RadikalChessConsole console;
	private RadikalChessGame game;
	private double[] hWhite;
	private double[] hBlack;
	private ABSearch search;

	/**
	 * Constructor del controlador. Inicia el juego y configura los
	 * parámetros necesarios para la partida computador-computador.
	 * El algoritmo de búsqueda ofrece las mismas condiciones a 
	 * ambos adversarios.
	 */
	private RadikalChessCC() {
		game = new RadikalChessGame();
		search = new ABSearch(game, -900., 900., 10);

		console = new RadikalChessConsole(game, game.getInitialState());

		hWhite = console.configHeuristics(RadikalChessPlayer.WHITE);
		hBlack = console.configHeuristics(RadikalChessPlayer.BLACK);

		mainLoop();
	}

	/**
	 * Inicio del controlador mediante instancia única de clase.
	 */
	public static void start() {
		if (instance == null) {
			instance = new RadikalChessCC();
		}
	}

	/**
	 * Bucle principal del controlador, juego de turnos.
	 */
	private void mainLoop() {
		boolean exit = false;
		RadikalChessState current = console.getCurrent();

		while (!game.isTerminal(current) && !exit) {
			console.out("\n" + current + "\n\n" + current.getPlayer() + "'s thinking...\n");
			
			if (current.getPlayer() == RadikalChessPlayer.BLACK) {
				game.setHeuristicsw(hBlack);
			} else {
				game.setHeuristicsw(hWhite);
			}
			
			proposeMove();
			current = console.getCurrent();
		}

		console.bye();
	}

	/**
	 * Método que propone un movimiento dado el estado actual.
	 * Informa de la operación de búsqueda realizada.
	 */
	private void proposeMove() {
		RadikalChessState current = console.getCurrent();
		RadikalChessAction action = search.makeDecision(current);
		console.printMetrics(search);
		
		if (action == null) {
			return;
		}

		console.updateCurrent(game.getResult(current, action));
	}
}