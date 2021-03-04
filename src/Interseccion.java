import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class Interseccion extends Thread {

	private int id;
	public ArrayList<String> callesEntrantes;
	public Semaforo[] semaforos;
	public ArrayList<Integer> prioridades;
	public int tiempoTotal = Main.tiempoTotal;
	public Calle[] calles;
	public Coche[] coches;
	public int indexCalleEmpieza = -1;
	public int indexSemaforoEmpieza;

	// PASAMOS EL FLAG PARA INICIALIZAR EL SEMAFORO DEL COLOR QUE NOS INTERESE
	public Interseccion(int id, ArrayList<String> callesEntrantes, ArrayList<Integer> prioridades, Calle[] calles,
			Coche[] coches) {
		// System.out.println("INTERSECCION: " + id);
		this.callesEntrantes = callesEntrantes;
		this.id = id;
		this.prioridades = prioridades;
		this.calles = calles;
		this.coches = coches;
		crearSemaforos();

	}

	public void run() {

		boolean ok = false;
		do {

			// EL PRIMER SEMAFORO
			if (indexCalleEmpieza != -1) {// CAMBIO A ROJO EL PRIMER SEMAFORO QUE PUSE EN VERDE EN CASO DE HABERLO
											// CAMBIADO
				calles[indexCalleEmpieza].cambiSemaforo();// SE PONE EN VERDE


				try {
					sleep(semaforos[indexSemaforoEmpieza].timpoVerde);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				calles[indexCalleEmpieza].cambiSemaforo();// SE PONE EN VERDE

				ok = true;
			}

			// YA EMPIEZA POR PRIORIDADES
			for (int i = 0; i < semaforos.length; i++) {
				if (!semaforos[i].isSiempreVerde() || semaforos[i].isSiempreRojo()) {// SI NO ES DE LOS SEMAFOROS QUE
																						// SIEMPRE TIENEN QUE ESTAR EN
					boolean flag = false;
					for (int j = 0; j < calles.length && !flag; j++) {
						if (calles[j].getNombre().equals(semaforos[i].nombreCalle)) {
							
							if (ok && i!= indexCalleEmpieza) {// COMO EL 1º SEMAFORO YA HA ESTADO EL VERDE CAMBIAMOS
																// TODOS MENOS ESE QUE YA A ESTADO EN VERDE
								calles[j].cambiSemaforo();// SE PONE EN VERDE
							}else {
								calles[j].cambiSemaforo();
							}

							try {
								sleep(semaforos[i].timpoVerde);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

							if(ok && i!=indexCalleEmpieza ) {
									calles[j].cambiSemaforo();
							}else {
								calles[j].cambiSemaforo();
							}
							
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
		boolean verde;
		int cont = 0;

		if (callesEntrantes.size() <= 1) {// SI EN LA INTERSECCION SOLO HAY 1 CALLE ENTRANTE LO PONEMOS VERDE

			boolean flag = false;
			for (int k = 0; k < calles.length && !flag; k++) {
				if (calles[k].getNombre().equals(callesEntrantes.get(0))) {
					semaforos[0] = new Semaforo(true, callesEntrantes.get(0), 1, true, false);// CREO EL
																										// SEMAFORO
					calles[k].setSemaforo(semaforos[0]);// LE PASO EL SEMAFORO A LA CALLE
					flag = true;

				}
			}
		} else {
			for (int i = prioridades.size() - 1; i >= 0; i--) {// LO TENGO QUE HACER AL REVES PORQUE AL ELIMINAR EL
				//
				int max = 0;
				int indexMax = 0;

				if (i == 0) {
					max = prioridades.get(i);
					indexMax = i;
				} else if (prioridades.get(i) > max) {
					max = prioridades.get(i);
					indexMax = i;
				}

				if (max != 0) {// SI VAN A PASAR COCHES
					boolean flag = false;

					for (int k = 0; k < calles.length && !flag; k++) {// RECORRO LAS CALLES PARA ENCONTRAR LA QUE TENGO
																		// DE ENTRANTES
						if (calles[k].getNombre().equals(callesEntrantes.get(i))) {// CUANDO ENCUENTRA LA CALLE CREO EL
																					// SAMAFORO Y PARO EL BUCLE
							semaforos[i] = new Semaforo(false, callesEntrantes.get(i), calculaTiempo(max, cuantos),
									false, false);// CREO EL SEMAFORO
							calles[k].setSemaforo(semaforos[i]);// LE PASO EL SEMAFORO A LA CALLE
							flag = true;

						}
					}
				} else {// SI NO VAN A PASAR COCHES
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
		semaforoComienzaVerde();// COJO EL SEMAFORO PARA QUE EMPIEZE EN VERDE SI UN COCHE ESTA PARADO AHI
	}

	private void semaforoComienzaVerde() {

		boolean flag = false;
		if (callesEntrantes.size() >= 2) {
			for (int i = 0; i < callesEntrantes.size() && !flag; i++) {
				for (int j = 0; j < coches.length; j++) {
					if (coches[j].getCallesRecorrido()[1].equals(callesEntrantes.get(i))) {
						for (int k = 0; k < calles.length; k++) {
							if (calles[k].getNombre().equals(callesEntrantes.get(i))) {
								indexCalleEmpieza = k;
								for (int k2 = 0; k2 < semaforos.length; k2++) {
									if (semaforos[k2].getNombreCalle().equals(calles[k].getNombre())) {
										indexSemaforoEmpieza = k2;
									}
								}

								flag = true;
							}
						}
					}
				}
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
		//return segundos;
		return 1;
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
