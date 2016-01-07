package applis;

import java.io.IOException;

import serveur.Serveur;
import service.TypeService;

public class AppliServeur {
	private static final int PORT_RESERVATION = 3000;
	private static final int PORT_VALIDER_PRERESERVATION = 3001;
	public static void main(String[] args) {
		//creation des serveurs
		try {
			Serveur serveurReservation = new Serveur(PORT_RESERVATION,TypeService.SERVICE_RESERVATION);
			Serveur serveurValiderPrereservation = new Serveur(PORT_VALIDER_PRERESERVATION, TypeService.SERVICE_VALIDER_PRERESERVATION);
			//lancement des serveurs
			serveurReservation.lancer();
			serveurValiderPrereservation.lancer();
		} catch (IOException e) {
			System.out.println("Erreur de lancement serveur sur le port choisi");
		}
	}

}
