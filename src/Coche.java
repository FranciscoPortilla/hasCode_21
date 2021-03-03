
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
		
		System.out.println(" Recorrido: ");
		for (int i = 0; i < callesRecorrido.length; i++) {
			System.out.println(getName()+" "+callesRecorrido[i]);
		}
		
		for (int i = 1; i < callesRecorrido.length; i++) {
			boolean flag=false;
			for (int j = 0; j < calles.length && !flag; j++) {
				if (callesRecorrido[i].equals(calles[j].getNombre())) {//SI COINCIDE EL NOMBRE DE LA CALLE LO MANDO A ESA CALLE A ESPERAR
					calles[j].espera(getName(),i+"/"+callesRecorrido.length);
					//int interseccion = calles[j].getTermina();
					//Main.getIntersecciones()[interseccion].esperar(callesRecorrido[i],interseccion,getName(),calles[j].getDuracion(),i+"/"+callesRecorrido.length); //LO RECOJO SINCRONIZADO
					flag=true;
				}
			}
		}
		System.out.println("////////////////////////////////// "+getName()+"LLEGA A SU DESTINO /////////////////////////////////////////////");
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
