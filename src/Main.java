import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Main {
	
	public static int segundos;
	public static int tiempoTotal;
	public static int interseccionComienzo;
	public static int interseccionFinal;
	public static int interseccionesTotales;
	public static int callesTotal;
	public static int cochesTotal;
	public static int puntos;
	public static int tiempoCalle;
	public static String calle;
	public static String[] recorridoCallesCoche;
	public static Coche[] coches;
	public static Calle[] calles;
	public static Interseccion [] intersecciones;
	public static int banderas;
	public static ArrayList<Integer> semaforos = new ArrayList<Integer>();
	public static boolean stop=true;
	
	

	synchronized public static Interseccion[] getIntersecciones() {
		return intersecciones;
	}
	
	
	public static void main(String[] args) throws IOException {
		leerfichero("c");
		cantidadSemaforos();
		creaIntersecciones();
		
		
		//SALEN LOS COCHES
		for (int i = 0; i < coches.length; i++) {//LOS COCHES SE ENCONTRARÁN TODOS LOS SEMAFOROS EL ROJOS HASTA QUE EL CONTROLADOR DE LOS SEMAFOROS COMIENCEN (MENOS LOS QUE ESTAN SIEMPRE EN VERDE)
			coches[i].start();
		}
		
		int cont=0;
		do {
			for (int i = 0; i < intersecciones.length; i++) {
				intersecciones[i].start();//COMIENZAN A FUNCIONAR LOS SEMAFOROS
			}
			
			if(cont==0) {
				ContadorTiempo contador=new ContadorTiempo(tiempoTotal);
				contador.start();
			}
			
			cont++;
		} while (!stop);
		
		
		escribirFichero("submit");
	}



	private static ArrayList<Integer> establecePrioridades(ArrayList<String> callesEntrantes) {

		ArrayList<Integer> prioridades=new ArrayList<Integer>();
		for (int i = 0; i < callesEntrantes.size(); i++) {
			prioridades.add(0);
			for (int j = 0; j < coches.length; j++) {
				for (int j2 = 1; j2 < coches[j].getCallesRecorrido().length; j2++) {//empiezo desde 1 porque el primero no es una calle
					if(callesEntrantes.get(i).equals(coches[j].getCallesRecorrido()[j2])) {
						prioridades.set(i, prioridades.get(i)+1);
					}
				}
			}
		}
		return prioridades;
	}

	private static void creaIntersecciones() {
		
		for (int i = 0; i < intersecciones.length; i++) {
			ArrayList<String> callesEntrantes=new ArrayList<String>();
			for (int j = 0; j < calles.length; j++) {
				if (i == calles[j].getTermina()) {
					callesEntrantes.add(calles[j].getNombre());
				}
			}
			ArrayList<Integer> prioridades=establecePrioridades(callesEntrantes);
			intersecciones[i]=new Interseccion(i,callesEntrantes,prioridades,calles,coches);
		}
	}

	
	public static void leerfichero(String name) throws IOException {
		File f = new File(name + ".txt");

		if (f.exists()) {
			FileReader fr = new FileReader(f);
			BufferedReader BRF = new BufferedReader(fr);

			String linea;
			linea = BRF.readLine();
			int cont = 0;
			int contadorCoche = 0;
			int contadorCalles = 0;

			String[] datos;
			while (linea != null) {

				if (cont == 0) {
					datos = linea.split(" ");
					tiempoTotal = Integer.parseInt(datos[0]);
					interseccionesTotales = Integer.parseInt(datos[1]);
					callesTotal = Integer.parseInt(datos[2]);
					cochesTotal = Integer.parseInt(datos[3]);
					puntos = Integer.parseInt(datos[4]);
					coches = new Coche[cochesTotal];
					calles = new Calle[callesTotal];
					intersecciones=new Interseccion[interseccionesTotales];
				} else if (cont <= callesTotal) {
					datos = linea.split(" ");
					interseccionComienzo = Integer.parseInt(datos[0]);
					interseccionFinal = Integer.parseInt(datos[1]);
					calle = datos[2];
					tiempoCalle = Integer.parseInt(datos[3]);
					calles[contadorCalles] = new Calle(interseccionComienzo, interseccionFinal, tiempoCalle, calle);
					contadorCalles++;
				} else {
					recorridoCallesCoche = linea.split(" ");
					coches[contadorCoche] = new Coche(contadorCoche, recorridoCallesCoche,calles);
					contadorCoche++;
				}

				linea = BRF.readLine();
				cont++;
			}

			BRF.close();
			fr.close();

		}		
	}
	
	
	public static void cantidadSemaforos() {
		for (int i = 0; i < interseccionesTotales; i++) {
			int termina = 1;
			for (int j = 0; j < calles.length; j++) {
				if (i == calles[j].getTermina()) {
					termina++;
				}
			}
			semaforos.add(1);
		}
	}

	
	public static void escribirFichero(String string) throws IOException {

		FileWriter fw = new FileWriter(string + ".txt", false);
		PrintWriter pw = new PrintWriter(fw);

		int tiempoVerde;
		
		
		pw.println(intersecciones.length);//NUERO DE INTERSECCIONES
		for (int i = 0; i < intersecciones.length; i++) {
			pw.println(intersecciones[i].getid());//NUMERO INTERSECCION
			pw.println(intersecciones[i].getCallesEntrantes().size()); // CALLES ENTRANTES
			for (int j= 0; j <intersecciones[i].getCallesEntrantes().size(); j++) {
				for (int j2 = 0; j2 < intersecciones[i].getSemaforos().length; j2++) {//RECORRO TODOS LOS SEMAFOROS HASTA QUE ENCUENTRE EL NOMBRE PARA SABER LOS SEGUNDO EN VERDE
					if(intersecciones[i].getCallesEntrantes().get(j).equals(intersecciones[i].getSemaforos()[j2].nombreCalle)) {
						tiempoVerde=intersecciones[i].getSemaforos()[j2].getTimpoVerde();
						pw.println(intersecciones[i].getCallesEntrantes().get(j) + " " + tiempoVerde);
					}
				}
			}
		}
		pw.close();
		fw.close();

	}

}
