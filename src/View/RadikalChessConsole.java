package View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import Model.ABSearch;
import Model.RadikalChessAction;
import Model.RadikalChessGame;
import Model.RadikalChessPlayer;
import Model.RadikalChessState;
import Model.RadikalChessUtil;

/**
 * Clase para la manipulación de la E/S del programa.
 * 
 * @author Quique
 */
public class RadikalChessConsole {
	
	private RadikalChessGame game;
	private RadikalChessState current;
	private ArrayList<RadikalChessState> history = new ArrayList<>();
	private Scanner in = new Scanner(System.in);

	public RadikalChessConsole(RadikalChessGame game, RadikalChessState current) {
		this.game = game;
		this.current = current;
		history.add(current);
	}
	
	public void out(String s) {
		System.out.println(s);
	}

	public void outp(String s) {
		System.out.print(s);
	}
	
	/**
	 * @param player Jugador a configurar.
	 * @return Configuración de pesos.
	 */
	public double[] configHeuristics(RadikalChessPlayer player) {
		double[] weight = new double[]{0., 0., 0., 0., 0.};
		String[] info = new String[5];
		info[0] = "Basic logic";
		info[1] = "Value of the pieces";
		info[2] = "Available movements";
		info[3] = "Pieces count";
		info[4] = "Safe movements";
		
		System.out.println("\nAI configuration for " + player + " (to fit your own flavour!):\n");
		String buffer;

		for (int i = 0; i < weight.length; i++) {
			System.out.print("\tWeight of heuristic \"" + info[i] + "\": ");

			buffer = in.nextLine();

			while(!validDouble(buffer)) {
				System.out.print("\t\tEnter a valid number: ");
				buffer = in.nextLine();
			}

			weight[i] = Integer.parseInt(buffer);
		}

		 return weight;
	}
	
	/**
	 * @return Quién será el jugador humano (HC).
	 */
	public RadikalChessPlayer getHumanPlayer() {
		boolean exit = false;
		System.out.print("\nHuman vs Computer\n\nChoose a color for you: ");

		while (!exit) {
			switch (in.nextLine().toLowerCase()) {
				case "w":
				case "white":
					return RadikalChessPlayer.WHITE;
				case "b":
				case "black":
					return RadikalChessPlayer.BLACK;
				default:
					System.out.print("\nUnknown color, try again: ");
			}
		}

		return null;
	}
	
	/**
	 * @return Intérprete de los comandos.
	 */
	public int parse() {
		String s = in.nextLine().toUpperCase();
		switch (s.split("\\s+")[0]) {
			case "QUIT":
			case "EXIT":
			case "END":
				return -1;
			case "?":
			case "HELP":
				printHelp();
				break;
			case "HINT":
				showHint(s);
				break;
			case "HINTALL":
				showHintAll();
				break;
			case "SAVE":
				saveState(s);
				break;
			case "LOAD":
				loadState(s);
				break;
			case "HISTORY":
				printHistory();
				break;
			case "UNDO":
				undo();
				break;
			case "DUNDO":
				undo();
				undo();
				break;
			default:
				if (!validMove(s)) {
					System.out.print("Invalid command, use help or ?\n> ");
					return 1;
				}
		}

		return 0;
	}
	
	/**
	 * Muestra todos los movimientos que puede hacer el jugador.
	 */
	private void showHintAll() {
		List<RadikalChessAction> actions = game.getActions(current);

		for (Iterator<RadikalChessAction> iterator = actions.iterator(); iterator.hasNext();) {
			RadikalChessAction action = (RadikalChessAction) iterator.next();
			System.out.println("\t- "+RadikalChessUtil.actionToNatural(current, action));
		}	
	}

	/**
	 * Deshace un movimiento.
	 */
	private void undo() {
		if (history.size() >= 2) {
			history.remove(history.size()-1);
			current = history.get(history.size()-1);
		}
	}

	/**
	 * Imprime la historia de movimientos.
	 */
	private void printHistory() {
		for (RadikalChessState item: history) {
			System.out.println(item);
		}
		
		System.out.println("\nEnd of history");
	}

	/**
	 * Setter del estado actual.
	 * 
	 * @param current estado actual.
	 */
	public void updateCurrent(RadikalChessState current) {
		history.add(current);
		this.current = current;
	}
	
	/**
	 * Getter del estado actual.
	 * 
	 * @return estado actual.
	 */
	public RadikalChessState getCurrent() {
		return current;
	}
	
	/**
	 * Imprime una ayuda al usuario.
	 */
	private void printHelp() {
		System.out.println("\nCommands: [quit, exit, end], [help, ?], hint <Letter><Number>, hintall, "+
				"save <filename>, load <filename>, <Letter><Number> <Letter><Number>, history, undo, dundo");		
	}
	
	/**
	 * Analiza las coordenadas naturales e informa de los posibles movimientos que tiene.
	 * Solo se dará los movimientos de las fichas del propio jugador.
	 * 
	 * @param s Coordenadas a tratar.
	 */
	private void showHint(String s) {
		if (s.split("\\s+").length != 2 || s.split("\\s+")[1].length() != 2) {
			System.out.println("\nUnknown, use hint <Letter><Number>");
			return;
		}
		System.out.println("\nList of available actions for " + current.getPlayer() + 
				" (" + s.split("\\s+")[1] + "):\n");

		int x = (int) (s.split("\\s+")[1].charAt(0) - 65);
		int y = current.getBoard()[0].length - Integer.parseInt(s.split("\\s+")[1].charAt(1) + "");

		List<RadikalChessAction> actions = game.getActions(current);

		for (Iterator<RadikalChessAction> iterator = actions.iterator(); iterator.hasNext();) {
			RadikalChessAction action = (RadikalChessAction) iterator.next();
			if (action.getX0() == x && action.getY0() == y) {
				System.out.println("\t- "+RadikalChessUtil.actionToNatural(current, action));
			}
		}
	}
	
	/**
	 * Guarda en el fichero indicado el estado.
	 * 
	 * @param s Comando a tratar.
	 */
	private void saveState(String s) {
		if (s.split("\\s+").length != 2 || s.split("\\s+")[1].length() < 1) {
			System.out.println("\nFilename is missing");
			return;
		}

		RadikalChessUtil.saveToFile(current, s.split("\\s+")[1].toLowerCase());
	}
	
	/**
	 * Carga el fichero indicado por el comando.
	 * 
	 * @param s Comando a tratar.
	 */
	private void loadState(String s) {
		if (s.split("\\s+").length != 2 || s.split("\\s+")[1].length() < 1) {
			System.out.println("\nFilename is missing");
			return;
		}

		RadikalChessState buffer = RadikalChessUtil.loadFromFile(s.split("\\s+")[1].toLowerCase());

		if (buffer != null) {
			current = buffer;
		}
	}
	
	/**
	 * Función inicial para realizar el movimiento de una ficha.
	 * 
	 * @param s Comando a tratar.
	 * @return Si el movimiento se ha llevado a cabo.
	 */
	private boolean validMove(String s) {
		int[] pos = new int[4];

		String[] split = s.split("\\s+");

		if (split.length < 2 || split[0].length() != 2 || split[1].length() != 2)
			return false;

		try {
			pos[0] = (int) (split[0].charAt(0) - 65);
			pos[1] = current.getBoard()[0].length - Integer.parseInt(split[0].charAt(1) + "");
			pos[2] = (int) (split[1].charAt(0) - 65);
			pos[3] = current.getBoard()[0].length - Integer.parseInt(split[1].charAt(1) + "");
		} catch (NumberFormatException nfe) {
			return false;
		}

		return validPos(pos);
	}
	
	/**
	 * Segunda fase del movimiento de una ficha.
	 * 
	 * @param pos Posición de inicio y fin.
	 * @return Si el movimiento se ha llevado a cabo.
	 */
	private boolean validPos(int[] pos) {
		if (current.getBoard()[pos[0]][pos[1]] != null && 
				current.getBoard()[pos[0]][pos[1]].getOwner() != current.getPlayer()) {
			return false;
		}

		List<RadikalChessAction> actions = game.getActions(current);

		for (Iterator<RadikalChessAction> iterator = actions.iterator(); iterator.hasNext();) {
			RadikalChessAction action = (RadikalChessAction) iterator.next();

			if (action.getX0() == pos[0] && action.getY0() == pos[1] &&
					action.getX1() == pos[2] && action.getY1() == pos[3]) {
				current = game.getResult(current, new RadikalChessAction(pos[0], pos[1], pos[2], pos[3], false));
				history.add(current);
				return true;
			}
		}

		return false;
	}
	
	/**
	 * @param string String a comprobar si es double
	 * @return Resultado de la utilidad, si es double o no.
	 */
	private boolean validDouble(String string) {
		try {
			Double.parseDouble(string);
		} catch (NumberFormatException nfe) {
			return false;
		}

		return true;
	}

	/**
	 * Despedida de la partida.
	 */
	public void bye() {
		System.out.println(current + "\n\n" + RadikalChessUtil.otherPlayer(game.getPlayer(current)) + 
				" has won. Gratz!");
		System.out.println("\nWell, that was fun :)");		
	}

	/**
	 * Impresión en pantalla de las métricas de la búsqueda.
	 * 
	 * @param search Búsqueda
	 */
	public void printMetrics(ABSearch search) {
		System.out.println("\t- Expanded nodes: "+ search.getMetrics().get("expandedNodes"));
		System.out.println("\t- Depth: "+ search.getMetrics().get("maxDepth"));
	}

}