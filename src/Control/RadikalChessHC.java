package Control;

import Model.ABSearch;
import Model.RadikalChessAction;
import Model.RadikalChessGame;
import Model.RadikalChessPlayer;
import Model.RadikalChessState;
import View.RadikalChessConsole;

/**
 * Clase de control para el juego humano-computador.
 * 
 * @author Quique
 */
public class RadikalChessHC {

	private static RadikalChessHC instance;
	private RadikalChessConsole console;
	private RadikalChessGame game;
	private RadikalChessPlayer human;
	private ABSearch search;

	/**
	 * Constructor del controlador. Inicia el juego y configura los
	 * parámetros necesarios para la partida.
	 */
	private RadikalChessHC() {
		game = new RadikalChessGame();
		search = new ABSearch(game, -900., 900., 15);

		console = new RadikalChessConsole(game, game.getInitialState());

		human = console.getHumanPlayer();
		RadikalChessPlayer enemy = (human == RadikalChessPlayer.BLACK)? RadikalChessPlayer.WHITE : RadikalChessPlayer.BLACK;
		game.setHeuristicsw(console.configHeuristics(enemy));

		mainLoop();
	}

	/**
	 * Inicio del controlador mediante instancia única de clase.
	 */
	public static void start() {
		if (instance == null) {
			instance = new RadikalChessHC();
		}
	}

	
	/**
	 * Bucle principal del controlador.
	 */
	private void mainLoop() {
		boolean exit = false;
		boolean input = true;
		RadikalChessState current = console.getCurrent();

		while (!game.isTerminal(current) && !exit) {
			if (current.getPlayer() != human) {
				console.out("\n" + current + "\n\n" + current.getPlayer() + "'s thinking...\n");

				proposeMove();
			} else {
				console.out("\n" + current + "\n\n" + human + "'s turn to move: ");

				while (input && !exit)
					switch (console.parse()) {
						case -1:
							exit = true;
							break;
						case 0:
							input = false;
							break;
						case 1:
							break;
					}
			}

			input = true;
			current = console.getCurrent();
		}

		console.bye();
	}

	
	/**
	 * Proposición de movimiento para el jugador máquina.
	 * Imprime información de la búsqueda realizada. 
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