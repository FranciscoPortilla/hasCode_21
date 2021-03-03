import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Interseccion extends Thread {

	private int id;
	public ArrayList<String> callesEntrantes;
	public Semaforo[] semaforos;
	public ArrayList<Integer> prioridades;
	public int tiempoTotal = Main.tiempoTotal;
	public Calle[] calles;

	// PASAMOS EL FLAG PARA INICIALIZAR EL SEMAFORO DEL COLOR QUE NOS INTERESE
	public Interseccion(int id, ArrayList<String> callesEntrantes, ArrayList<Integer> prioridades, Calle[] calles) {
		// System.out.println("INTERSECCION: " + id);
		this.callesEntrantes = callesEntrantes;
		this.id = id;
		this.prioridades = prioridades;
		this.calles = calles;
		crearSemaforos();
	}

	public void run() {

		do {
			for (int i = 0; i < semaforos.length; i++) {
				if (!semaforos[i].isSiempreVerde() || semaforos[i].isSiempreRojo()) {// SI NO ES DE LOS SEMAFOROS QUE
																						// SIEMPRE TIENEN QUE ESTAR EN
					boolean flag = false;
					for (int j = 0; j < calles.length && !flag; j++) {
						if (calles[j].getNombre().equals(semaforos[i].nombreCalle)) {
							if (i != 0) {// EL PRIMER SEMAFORO LO PONGO A ROJO DORECTAMANTE PORQUE YA LO PUSE A VERDE AL PRICIPIO
								calles[j].cambiSemaforo();// SE PONE EN VERDE
								System.out.println("SEMAFORO CALLE: " + semaforos[i].nombreCalle + " color: "
										+ semaforos[i].isFlag());
							}

							try {
								sleep(semaforos[i].timpoVerde);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							calles[j].cambiSemaforo();
							System.out.println(
									"SEMAFORO CALLE: " + semaforos[i].nombreCalle + " color: " + semaforos[i].isFlag());
							flag = true;
						}
					}
				}

			}
		} while (!Main.stop);
	}

	private void crearSemaforos() {

		int cuantos = cuantosRepartir();
		semaforos = new Semaforo[callesEntrantes.size()];

		if (callesEntrantes.size() <= 1) {// SI EN LA INTERSECCION SOLO HAY UNA CALLE ENTRANTE LO PONEMOS VERDE
			// SIEMPRE
			boolean flag = false;
			for (int k = 0; k < calles.length && !flag; k++) {
				if (calles[k].getNombre().equals(callesEntrantes.get(0))) {
					semaforos[0] = new Semaforo(true, callesEntrantes.get(0), tiempoTotal, true, false);// CREO EL
																										// SEMAFORO
					calles[k].setSemaforo(semaforos[0]);// LE PASO EL SEMAFORO A LA CALLE
					flag = true;
				}
			}
		} else {
			for (int i = prioridades.size() - 1; i >= 0; i--) {// LO TENGO QUE HACER AL REVER PORQUE AL ELIMINAR EL
																// INDEX SE REPOSICIONAN HACIA DELANTE Y EL FOR FALLA

				int max = 0;
				int indexMax = 0;

				if (i == 0) {
					max = prioridades.get(i);
					indexMax = i;
				} else if (prioridades.get(i) > max) {
					max = prioridades.get(i);
					indexMax = i;
				}

				if (max != 0) {
					boolean flag = false;
					boolean verde;

					if (i == prioridades.size() - 1) {// EL PRIMERO A VERDE LOS DEMAS EN ROJO AL EMPEZAR
						verde = true;
					} else {
						verde = false;
					}

					for (int k = 0; k < calles.length && !flag; k++) {
						if (calles[k].getNombre().equals(callesEntrantes.get(i))) {
							semaforos[i] = new Semaforo(verde, callesEntrantes.get(i), calculaTiempo(max, cuantos),
									false, false);// CREO EL SEMAFORO
							calles[k].setSemaforo(semaforos[i]);// LE PASO EL SEMAFORO A LA CALLE
							flag = true;
						}
					}
				} else {
					boolean flag = false;
					for (int j = 0; j < calles.length && !flag; j++) {
						if (calles[j].getNombre().equals(callesEntrantes.get(i))) {
							semaforos[i] = new Semaforo(false, callesEntrantes.get(i), 1, false, true);// CREO EL
							calles[j].setSemaforo(semaforos[i]);// LE PASO EL SEMAFORO A LA CALLE
							flag = true;
						}

					}
				}

				prioridades.remove(i);
			}
		}

	}

	private int cuantosRepartir() {
		int cuantos = 0;
		for (int i = 0; i < prioridades.size(); i++) {
			if (prioridades.get(i) >= 1) {
				cuantos += prioridades.get(i);
			} else {
				cuantos += 1;
			}
		}
		return cuantos;
	}

	private int calculaTiempo(int prioridad, int cuantos) {
		int segundos = 0;
		// HAGO UN REGLA DE TRES
		segundos = (this.tiempoTotal * prioridad) / cuantos;
		return segundos;
		// return 2;
	}

	public int getid() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<String> getCallesEntrantes() {
		return callesEntrantes;
	}

	public void setCallesEntrantes(ArrayList<String> callesEntrantes) {
		this.callesEntrantes = callesEntrantes;
	}

	synchronized public Semaforo[] getSemaforos() {
		return semaforos;
	}

	public void setSemaforos(Semaforo[] semaforos) {
		this.semaforos = semaforos;
	}

	public ArrayList<Integer> getPrioridades() {
		return prioridades;
	}

	public void setPrioridades(ArrayList<Integer> prioridades) {
		this.prioridades = prioridades;
	}

	public int getTiempoTotal() {
		return tiempoTotal;
	}

	public void setTiempoTotal(int tiempoTotal) {
		this.tiempoTotal = tiempoTotal;
	}

}
