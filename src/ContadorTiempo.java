
public class ContadorTiempo extends Thread{

	
	private int segundos=0;
	private int totalSegundos;
	
	
	ContadorTiempo(int tiempoTotal){
		this.totalSegundos=tiempoTotal;
	}
	
	
	public void run() {
		System.out.println("***************************************************************************");
		System.out.println("************************** COMIENZA EL TIEMPO *****************************");
		System.out.println("***************************************************************************");

		
	
		do {
			
			segundos++;
		}while(segundos<=totalSegundos);
		
		System.out.println("***************************************************************************");
		System.out.println("************************** SE ACABÓ EL TIEMPO *****************************");
		System.out.println("***************************************************************************");

		Main.stop=true;
		
	}
	
}
