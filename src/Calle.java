import java.util.*;

public class Calle {
	
	// EMPIEZA TERMINA NOMBRE DURACION COCHES
	private int interseccionComienza,interseccionAcaba,tiempoCalle;
	private String nombreCalle;
	
	

	public Calle(int interseccionComienza, int interseccionAcaba, int tiempoCalle, String nombreCalle) {
		
		this.interseccionComienza = interseccionComienza;
		this.interseccionAcaba = interseccionAcaba;
		this.tiempoCalle = tiempoCalle;
		this.nombreCalle = nombreCalle;
	}
	
	
	
	public int getEmpieza() {
		return interseccionComienza;
	}

	public void setEmpieza(int empieza) {
		this.interseccionComienza = empieza;
	}

	public int getTermina() {
		return interseccionAcaba;
	}

	public void setTermina(int termina) {
		this.interseccionAcaba = termina;
	}

	public int getDuracion() {
		return tiempoCalle;
	}

	public void setDuracion(int duracion) {
		this.tiempoCalle = duracion;
	}

	public String getNombre() {
		return nombreCalle;
	}

	public void setNombre(String nombre) {
		this.nombreCalle = nombre;
	}

	

}


								

