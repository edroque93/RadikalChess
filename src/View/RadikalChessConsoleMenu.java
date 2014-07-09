package View;

import java.util.Scanner;

import Control.RadikalChessCC;
import Control.RadikalChessExperiment;
import Control.RadikalChessHC;
import Control.RadikalChessHH;

public class RadikalChessConsoleMenu {

	private static RadikalChessConsoleMenu instance;
	private Scanner in = new Scanner(System.in);
	private String[] menuItems = new String[]{"Human vs Human",
			"Human vs Computer", 
			"Computer vs Computer",
			"Launch experiment"};

	/**
	 * Inicio del menú mediante instancia única de clase.
	 */
	public static void start() {
		if (instance == null) {
			instance = new RadikalChessConsoleMenu();
		}
	}

	/**
	 * Tratamiento de los posible modos de juego.
	 */
	private RadikalChessConsoleMenu() {
		printLogo();
		printMenu();
		
		int option = selectedOption();
		
		while (option == -1) {
			option = selectedOption();
		}
		
		switch (option) {
			case 0:
				RadikalChessHH.start();
				break;
			case 1:
				RadikalChessHC.start();
				break;
			case 2:
				RadikalChessCC.start();
			case 3:
				RadikalChessExperiment.start();
			default:
				break;
		}
		
		System.out.println("\nBye!");
	}

	/**
	 * Logo de la aplicación.
	 */
	public void printLogo() {
		System.out.println("                         Enrique Díaz Roque                    __");
		System.out.println("  ____           _ _ _         _  ____ _                      (  )");
		System.out.println(" |  _ \\ __ _  __| (_) | ____ _| |/ ___| |__   ___  ___ ___     ><");
		System.out.println(" | |_) / _` |/ _` | | |/ / _` | | |   | '_ \\ / _ \\/ __/ __|   |  |");
		System.out.println(" |  _ < (_| | (_| | |   < (_| | | |___| | | |  __/\\__ \\__ \\  / __ \\");
		System.out.println(" |_| \\_\\__,_|\\__,_|_|_|\\_\\__,_|_|\\____|_| |_|\\___||___/___/ |______|\n");
	}

	/**
	 * Imprime el menú con los diferentes modos de juego.
	 */
	private void printMenu() {
		for (int i = 0; i < menuItems.length; i++) {
			System.out.println("\t" + (i + 1) + " - " + menuItems[i]);
		}
		
		System.out.print("\n> ");
	}

	/**
	 * @return Opción de juego seleccionada.
	 */
	private int selectedOption() {
		String sOpt = in.nextLine();
		
		if (!isInt(sOpt)) {
			wrongInput();
			return -1;
		}
		
		int opt = Integer.parseInt(sOpt);
		
		if (opt < 1 || opt > menuItems.length) {
			wrongInput();
			return -1;
		}
		
		return opt - 1;
	}
	
	/**
	 * Comando no encontrado.
	 */
	private void wrongInput() {
		System.out.println("\nWrong input");
		System.out.print("\n> ");
	}

	/**
	 * @param string String a tratar.
	 * @return Si la String es un entero.
	 */
	private boolean isInt(String string) {
		try {
			Integer.parseInt(string);
		} catch (NumberFormatException nfe) {
			return false;
		}
		
		return true;
	}

}