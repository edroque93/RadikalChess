package Model;

/**
 * Clase que representa una acción. Una acción se define como una casilla de partida
 * y una casilla destino. Además, se conoce si este movimiento es una captura de ficha.
 * Getters y Setters de las propiedades definidas anteriormente.
 * 
 * @author Quique
 */
public class RadikalChessAction {

	private final int x0;
	private final int y0;
	private final int x1;
	private final int y1;
	private final boolean isCapture;

	public RadikalChessAction(int x0, int y0, int x1, int y1, boolean isCapture) {
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
		this.isCapture = isCapture;
	}

	public int getX0() {
		return x0;
	}

	public int getY0() {
		return y0;
	}

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public boolean isCapture() {
		return isCapture;
	}
	
	@Override
	public String toString() {
		return x1 + "," + y1 + "; isCapture=" + isCapture;
	}	
}