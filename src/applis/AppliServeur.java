package applis;

import java.io.IOException;

import serveur.Serveur;

public class AppliServeur {
	public static void main(String[] args) {
		//creation d'un serveur
		try {
			Serveur serveurReservation = new Serveur(3000);
			//lancement du serveur 
			serveurReservation.lancer();
		} catch (IOException e) {
			System.out.println("Erreur de lancement serveur sur le port choisi");
		}
	}

}
