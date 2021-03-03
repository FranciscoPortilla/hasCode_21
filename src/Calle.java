import java.util.*;

public class Calle {

	// EMPIEZA TERMINA NOMBRE DURACION COCHES
	private int interseccionComienza, interseccionAcaba, tiempoCalle;
	private String nombreCalle;
	private Semaforo semaforo;

	public Calle(int interseccionComienza, int interseccionAcaba, int tiempoCalle, String nombreCalle) {

		this.interseccionComienza = interseccionComienza;
		this.interseccionAcaba = interseccionAcaba;
		this.tiempoCalle = tiempoCalle;
		this.nombreCalle = nombreCalle;
	}

	synchronized public void espera(String nombreHilo, String recorrido) {

		while (!semaforo.isFlag()) {// MIENTRAS EL SEMAFORO ESTE EN ROJO
			try {
				System.out.println("[--] ESPERA!  Interseccion del coche: " + nombreHilo + " en interseccion: "
						+ interseccionAcaba + " verde: " + semaforo.isFlag() + " nombrecallesemaforo: " + nombreCalle
						+ " RECORRIDO: " + recorrido);
				wait();
			} catch (InterruptedException e) {e.printStackTrace();}

			try {
				Thread.sleep(tiempoCalle);//TIEMPO DE CALLE
			} catch (InterruptedException e) {e.printStackTrace();}
			
			System.out.println("[**] SALE DE LA CALLE!  Interseccion del coche: " + nombreHilo + " en interseccion: "
					+ interseccionAcaba + " verde: " + semaforo.isFlag() + " nombrecallesemaforo: " + nombreCalle
					+ " RECORRIDO: " + recorrido);
		}
	}

	synchronized public void cambiSemaforo() {
		semaforo.switchLight();
		notify();
	}
	
	public Semaforo getSemaforo() {
		return semaforo;
	}

	public void setSemaforo(Semaforo semaforo) {
		this.semaforo = semaforo;
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
