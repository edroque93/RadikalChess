package Model;

import java.io.Serializable;
import java.util.List;

/**
 * Clase abstracta para establecer las normas para cada ficha del juego.
 * Implementa la interfaz Serializable para tener la posibilidad de grabar
 * el tablero en un fichero.
 * 
 * @author Quique
 */
public abstract class ChessPiece implements Serializable {
	
	private static final long serialVersionUID = -3494692929338584031L;
	private RadikalChessPlayer owner;
	
	/**
	 * La ficha lo único que sabe es a quién pertenece.
	 * En ningún momento la ficha sabe donde está.
	 * Una ficha cualquiera podrá ser interrogada para que devuelva que
	 * acciones puede hacer dado una posición y un tablero (véase getAction).
	 * 
	 * @param owner Propietario de esta ficha
	 */
	public ChessPiece(RadikalChessPlayer owner) {
		this.owner = owner;
	}

	/**
	 * @return Devuelve el nombre inglés de la ficha.
	 */
	public abstract String getName();
	
	
	/**
	 * @returnn Devuelve el símbolo que representa su tipo.
	 */
	public abstract String unicodeSymbol();
	
	
	/**
	 * @param state Estado del tablero.
	 * @param x Coord.
	 * @param y Coord.
	 * @return Lista de los movimientos posible dado un estado y si estuviera en (x,y).
	 */
	public abstract List<RadikalChessAction> getAction(RadikalChessState state, int x, int y);
	
	/**
	 * Getter del propietario.
	 */
	public RadikalChessPlayer getOwner() {
		return owner;
	}
	
	/*
	 * Forzamos a que el toString sea elsímbolo de la ficha en unicode.
	 */
	public String toString() {
		return unicodeSymbol();
	}
}