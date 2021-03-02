
public class Semaforo {

	public boolean flag;
	public boolean siempreVerde;
	public String nombreCalle;
	public int timpoVerde;
	public boolean siempreRojo;
	
	Semaforo(boolean flag, String nombreCalle, int tiempoVerde, boolean siempreVerde,boolean siempreRojo) {
		this.flag = flag;
		this.nombreCalle = nombreCalle;
		this.timpoVerde = tiempoVerde;
		this.siempreVerde = siempreVerde;
		this.siempreRojo=siempreRojo;
		System.out.println("-->Semaforo calle: " + nombreCalle + " color:" + flag + " timepoVerde: " + tiempoVerde
				+ " siempre verde: " + siempreVerde);
	}



	// CAMBIAMOS LA LUZ DEL SEMÁFORO
	public void switchLight() {
		if (flag) {
			flag = false;
		} else {
			flag = true;
		}
	}

	public boolean isSiempreRojo() {
		return siempreRojo;
	}
	
	public boolean isSiempreVerde() {
		return siempreVerde;
	}

	public void setSiempreVerde(boolean siempreVerde) {
		this.siempreVerde = siempreVerde;
	}

	synchronized public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	synchronized public String getNombreCalle() {
		return nombreCalle;
	}

	public void setNombreCalle(String nombreCalle) {
		this.nombreCalle = nombreCalle;
	}

	public int getTimpoVerde() {
		return timpoVerde;
	}

	public void setTimpoVerde(int timpoVerde) {
		this.timpoVerde = timpoVerde;
	}

}
