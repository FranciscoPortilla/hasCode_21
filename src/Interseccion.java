import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Interseccion extends Thread{

	private int id;
	public ArrayList<String> callesEntrantes;
	public Semaforo[] semaforos;
	public ArrayList<Integer> prioridades;
	public int tiempoTotal = Main.tiempoTotal;

	// PASAMOS EL FLAG PARA INICIALIZAR EL SEMAFORO DEL COLOR QUE NOS INTERESE
	public Interseccion(int id, ArrayList<String> callesEntrantes, ArrayList<Integer> prioridades) {
		System.out.println("INTERSECCION: " + id);
		this.callesEntrantes = callesEntrantes;
		this.id = id;
		this.prioridades = prioridades;
		crearSemaforos();
	}
	
	public void run() {
		comienzaControladorSemaforos();
	}

	private void crearSemaforos() {

		int cuantos = cuantosRepartir();
		semaforos = new Semaforo[callesEntrantes.size()];

		if (callesEntrantes.size() <= 1) {// SI EN LA INTERSECCION SOLO HAY UNA CALLE ENTRANTE LO PONEMOS VERDE
			// SIEMPRE

			semaforos[0] = new Semaforo(true, callesEntrantes.get(0), tiempoTotal, true,false);
			System.out.println("Semaforo creado en calle: " + callesEntrantes.get(0) + "\n");

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

				if(max!=0) {
					semaforos[i] = new Semaforo(false, callesEntrantes.get(i), calculaTiempo(max, cuantos), false,false);
				}else {
					semaforos[i] = new Semaforo(false, callesEntrantes.get(i), 0, false,true);
				}
				System.out.println("calle: " + callesEntrantes.get(i) + " con prioridad: " + max);
				System.out.println("Semaforo creado en calle: " + callesEntrantes.get(i) + "\n");

				prioridades.remove(i);
			}
		}

	}

	private int cuantosRepartir() {
		int cuantos = 0;
		for (int i = 0; i < prioridades.size(); i++) {
			if (prioridades.get(i) >= 1) {
				cuantos += prioridades.get(i);
			}
		}
		return cuantos;
	}

	private int calculaTiempo(int prioridad, int cuantos) {
		int segundos = 0;
		// HAGO UN REGLA DE TRES
		System.out.println("entra: " + prioridad + " cuantos: " + cuantos);
		segundos = (this.tiempoTotal * prioridad) / cuantos;
		return segundos;
	}

	synchronized public void comienzaControladorSemaforos() {

		do {
			for (int i = 0; i < semaforos.length; i++) {
				if (!semaforos[i].isSiempreVerde() && semaforos[i].isSiempreRojo()) {// SI NO ES DE LOS SEMAFOROS QUE SIEMPRE TIENEN QUE ESTAR EN VERDE
					semaforos[i].switchLight();// LO PONGO EN VERDE
					//notify();// NOTIFICO EN ORDEN
					notifica();
					System.out.println(
							"SEMAFORO CALLE: " + semaforos[i].nombreCalle + " color: " + semaforos[i].isFlag());
					
					try {
						Thread.sleep(semaforos[i].getTimpoVerde());
						//Thread.sleep(2000);
					} catch (InterruptedException e) {e.printStackTrace();}
					
					semaforos[i].switchLight();// LO PONGO EN ROJO
					System.out.println(
							"SEMAFORO CALLE: " + semaforos[i].nombreCalle + " color: " + semaforos[i].isFlag());
				}

			}
		} while (!Main.stop);

		System.out.println("Se acabo el tiempo");
		//todosVerde();
		//notifica();
	
	}
	
	private void notifica() {notify();}

	private void todosVerde() {
		for (int i = 0; i < semaforos.length; i++) {
			if (!semaforos[i].isSiempreVerde()) {// SI NO ES DE LOS SEMAFOROS QUE SIEMPRE TIENEN QUE ESTAR EN VERDE
				semaforos[i].switchLight();// LO PONGO EN VERDE
				System.out.println("TODOS EN VERDE Y NOTIFICA");
			}

		}
	}

	synchronized public void esperar(String calleActual, int interseccion, String nombre,int tiempoCalle) {

		for (int k = 0; k < getSemaforos().length; k++) {// RECORRO LOS SEMAFOROS DE ESA INTERSECCION HASTA ENCONTRAR EN
															// EL
			if (getSemaforos()[k].getNombreCalle().equals(calleActual)) {
				while (!getSemaforos()[k].isFlag()) {// MIENTRAS EL SEMAFORO ESTE EN ROJO
					try {
						System.out.println("[--] ESPERA!  Interseccion del coche: " + nombre + " por la calle: "
								+ calleActual + " en interseccion: " + interseccion+ " verde: "+getSemaforos()[k].isFlag()+" nombrecallesemaforo: "+getSemaforos()[k].getNombreCalle());
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("-->[**] ARRANCA!  Interseccion del coche: " + nombre + " por la calle: "
							+ calleActual + " en interseccion: " + interseccion+ " verde: "+getSemaforos()[k].isFlag()+" nombrecallesemaforo: "+getSemaforos()[k].getNombreCalle());
					
					
					try {
						Thread.sleep(tiempoCalle);
					} catch (InterruptedException e) {e.printStackTrace();}
				}
			}
		}
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
