
public class Coche extends Thread {
	private int idCoche;
	private String[] callesRecorrido;
	private Calle[] calles;

	public Coche(int id, String[] callesRecorrido, Calle[] calles) {
		this.idCoche = id;
		this.callesRecorrido = callesRecorrido;
		this.calles = calles;
	}

	public void run() {
		for (int i = 1; i < callesRecorrido.length; i++) {
			for (int j = 0; j < calles.length; j++) {
				if (callesRecorrido[i].equals(calles[j].getNombre())) {
					int interseccion = calles[j].getTermina();
					Main.getIntersecciones()[interseccion].esperar(callesRecorrido[i],interseccion,getName(),calles[j].getDuracion()); //LO RECOJO SINCRONIZADO
				}
			}
		}
	}

	public int getIdCoche() {
		return idCoche;
	}

	public void setIdCoche(int idCoche) {
		this.idCoche = idCoche;
	}

	public String[] getCallesRecorrido() {
		return callesRecorrido;
	}

	public void setCallesRecorrido(String[] callesRecorrido) {
		this.callesRecorrido = callesRecorrido;
	}

	public Calle[] getCalles() {
		return calles;
	}

	public void setCalles(Calle[] calles) {
		this.calles = calles;
	}

}
