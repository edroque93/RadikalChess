package Control;

import java.util.ArrayList;

import Model.ABSearch;
import Model.RadikalChessAction;
import Model.RadikalChessGame;
import Model.RadikalChessPlayer;
import Model.RadikalChessState;
import Model.RadikalChessUtil;
import View.RadikalChessConsole;

public class RadikalChessExperiment {

	private static RadikalChessExperiment instance;
	private RadikalChessGame game = new RadikalChessGame();
	private RadikalChessState current = new RadikalChessState();
	private RadikalChessConsole console;
	private ABSearch search;

	private double[] testOne;
	private double[] testTwo;
	private ArrayList<RadikalChessState> testBoards;
	private int experimentsCount = 100;

	private RadikalChessExperiment() {
		testBoards = new ArrayList<>();
		console = new RadikalChessConsole(game, current);
		// 3s -> depth ~7
		// 1s -> depth ~6
		search = new ABSearch(game, -900., 900., 3);

		console.out("\nRadikalChessExperiment initialized");

		experiment();
	}

	/**
	 * Prepara y lanza los experimentos
	 */
	private void experiment() {
		generateRandomGames();
		console.out("\nTesting count efficiency...\n");
		countEfficiency();
		console.out("\nTesting weight vs count...\n");
		weightCount();
	}

	/**
	 * Prueba de eficiencia de la heurística de contar fichas
	 */
	private void countEfficiency() {
		testOne = new double[]{1., 1., 1., 1., 1.}; // Full
		testTwo = new double[]{1., 1., 1., 0., 1.}; // Without count
		int fwcount = 0, fwocount = 0, swcount = 0, swocount = 0;

		for (int i = 0; i < experimentsCount; i++) {
			current = testBoards.get(i);
			console.out("Simulating board " + (i + 1) + "/" + experimentsCount);

			while (!game.isTerminal(current)) {
				if (current.getPlayer() == RadikalChessPlayer.BLACK) {
					game.setHeuristicsw(testOne); // full
				} else {
					game.setHeuristicsw(testTwo);
				}

				proposeMove();
			}

			if (RadikalChessUtil.otherPlayer(current.getPlayer()) == RadikalChessPlayer.BLACK) {
				fwcount++;
			} else {
				fwocount++;
			}
		}

		console.out("\nStatistics playing as the first player: ");
		console.out("\tWith count heuristic: " + fwcount + "/" + experimentsCount);
		console.out("\tWithout count heuristic: " + fwocount + "/" + experimentsCount);
		console.out("");

		for (int i = 0; i < experimentsCount; i++) {
			current = testBoards.get(i);
			console.out("Simulating board " + (i + 1) + "/" + experimentsCount);

			while (!game.isTerminal(current)) {
				if (current.getPlayer() == RadikalChessPlayer.WHITE) {
					game.setHeuristicsw(testOne); // Full
				} else {
					game.setHeuristicsw(testTwo);
				}

				proposeMove();
			}

			if (RadikalChessUtil.otherPlayer(current.getPlayer()) == RadikalChessPlayer.WHITE) {
				swcount++;
			} else {
				swocount++;
			}
		}

		console.out("\nStatistics playing as the second player: ");
		console.out("\tWith count heuristic: " + swcount + "/" + experimentsCount);
		console.out("\tWithout count heuristic: " + swocount + "/" + experimentsCount);

		console.out("\nStatistics overall: ");
		console.out("\tWith count heuristic: " + (fwcount + swcount) + "/" + experimentsCount*2);
		console.out("\tWithout count heuristic: " + (fwocount + swocount) + "/" + experimentsCount*2);
	}

	/**
	 * Pesos vs Cuenta
	 */
	private void weightCount() {
		testOne = new double[]{1., 1., 1., 0., 1.}; // Weight
		testTwo = new double[]{1., 0., 1., 1., 1.}; // Count
		
		int fweight = 0, fcount = 0, sweight = 0, scount = 0;

		for (int i = 0; i < experimentsCount; i++) {
			current = testBoards.get(i);
			console.out("Simulating board " + (i + 1) + "/" + experimentsCount);

			while (!game.isTerminal(current)) {
				if (current.getPlayer() == RadikalChessPlayer.BLACK) {
					game.setHeuristicsw(testOne); // Weight
				} else {
					game.setHeuristicsw(testTwo);
				}

				proposeMove();
			}

			if (RadikalChessUtil.otherPlayer(current.getPlayer()) == RadikalChessPlayer.BLACK) {
				fweight++;
			} else {
				fcount++;
			}
		}

		console.out("\nStatistics playing as the first player: ");
		console.out("\tWeight heuristic: " + fweight + "/" + experimentsCount);
		console.out("\tCount heuristic: " + fcount + "/" + experimentsCount);
		console.out("");

		for (int i = 0; i < experimentsCount; i++) {
			current = testBoards.get(i);
			console.out("Simulating board " + (i + 1) + "/" + experimentsCount);

			while (!game.isTerminal(current)) {
				if (current.getPlayer() == RadikalChessPlayer.WHITE) {
					game.setHeuristicsw(testOne); // Weight
				} else {
					game.setHeuristicsw(testTwo);
				}

				proposeMove();
			}

			if (RadikalChessUtil.otherPlayer(current.getPlayer()) == RadikalChessPlayer.WHITE) {
				sweight++;
			} else {
				scount++;
			}
		}

		console.out("\nStatistics playing as the second player: ");
		console.out("\tWeight heuristic: " + sweight + "/" + experimentsCount);
		console.out("\tCount heuristic: " + scount + "/" + experimentsCount);

		console.out("\nStatistics overall: ");
		console.out("\tWeight heuristic: " + (fweight + sweight) + "/" + experimentsCount*2);
		console.out("\tCount heuristic: " + (fcount + scount) + "/" + experimentsCount*2);
	}

	/**
	 * Con la ayuda de las utilidades genera diferentes tableros
	 */
	private void generateRandomGames() {
		RadikalChessState buffer;

		for (int i = 0; i < experimentsCount; i++) {
			buffer = RadikalChessUtil.getRandomBoard(game, RadikalChessPlayer.WHITE);
			if (!game.isTerminal(buffer)) testBoards.add(buffer);
			else i--;
		}

		console.out("\nRandom games generated");		
	}

	/**
	 * Método que propone un movimiento dado el estado actual.
	 * Informa de la operación de búsqueda realizada.
	 */
	private void proposeMove() {
		RadikalChessAction action = search.makeDecision(current);

		if (action == null) {
			return;
		}

		current = game.getResult(current, action);
	}

	public static void start() {
		if (instance == null) {
			instance = new RadikalChessExperiment();
		}
	}

}
