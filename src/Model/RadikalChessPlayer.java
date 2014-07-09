package Model;

/**
 * Enumerado que representa a un jugador.
 * 
 * @author Quique
 */
public enum RadikalChessPlayer {
	WHITE ("White"), BLACK ("Black");
	
	private String name;

	/**
	 * @param name Nombre de la ficha, mejora el toString de serie.
	 */
	private RadikalChessPlayer(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
