package Control;

import Model.RadikalChessGame;
import Model.RadikalChessState;
import View.RadikalChessConsole;

/**
 * Clase controlador el juego humano-humano.
 * 
 * @author Quique
 */
public class RadikalChessHH {

	private static RadikalChessHH instance;
	private RadikalChessGame game;
	private RadikalChessConsole console;

	/**
	 * Setup del juego para jugadores humanos.
	 */
	private RadikalChessHH() {
		console.out("\nHuman vs Human\n\nReady? Go!");

		game = new RadikalChessGame();
		console = new RadikalChessConsole(game, game.getInitialState());
		mainLoop();
	}
	
	/**
	 * Inicio del controlador mediante instancia única de clase.
	 */
	public static void start() {
		if (instance == null) {
			instance = new RadikalChessHH();
		}
	}

	/**
	 * Bucle principal del controlador. 
	 */
	private void mainLoop() {
		boolean exit = false;
		boolean input;
		RadikalChessState current = console.getCurrent();

		while (!game.isTerminal(current) && !exit) {
			console.out("\n" + current + "\n\n" + current.getPlayer() + "'s turn to move: ");
			input = true;

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

			current = console.getCurrent();
		}

		console.bye();
	}

}