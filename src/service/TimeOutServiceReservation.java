package service;

import java.io.IOException;
import java.util.TimerTask;
/**
 * class permettant caracterisant la tache planifier : interrompre la session du service reservation
 *
 */
public class TimeOutServiceReservation extends TimerTask{
	private ServiceReservation serviceReservation;
	
	public TimeOutServiceReservation(ServiceReservation serviceReservation) {
		this.serviceReservation = serviceReservation;
	}
	@Override
	public synchronized void run() {
		//fermeture de la socket de liaison avec le client
		try {
			this.serviceReservation.getSocket().close();
		} catch (IOException e) {
			System.out.println("Erreur : probleme dans la fermeture de la socket dans la tache planifier TimeOutServiceReservation");
		}
		//interruption du thread 
		this.serviceReservation.getThread().interrupt();
	}

}
