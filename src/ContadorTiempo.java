
public class ContadorTiempo extends Thread{

	
	private int segundos=0;
	private int totalSegundos;
	
	
	ContadorTiempo(int tiempoTotal){
		this.totalSegundos=tiempoTotal;
	}
	
	
	public void run() {
		try {
			sleep(500);
		} catch (InterruptedException e) {e.printStackTrace();}
		do {
			segundos++;
		}while(segundos<=this.totalSegundos);
		Main.stop=true;
		
	}
	
}
