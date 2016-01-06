package applis;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

import client.ClientPrincipale;

public class AppliClient {
	
	public static void main(String[] args)  {
		//creation d'un client
			try {
				Scanner sc = new Scanner(System.in);
				String réponse = "";
				
				//DEBUT MODIF
				//On demande au client s'il veut faire une réservation ou valider une préreservation
				System.out.println("Voulez vous réserver ('1') ou valider une préreservation ('2')");
				
				do {
					System.out.println("Veuillez répondre par 1 ou 2");
					réponse = sc.nextLine();
				} while (!réponse.equals("1") && !réponse.equals("2"));
				
				if(réponse.equals("1")){
					ClientPrincipale client = new ClientPrincipale("127.0.0.1",3000);
					client.lancer();
				}
				if(réponse.equals("2")){
					ClientPrincipale client = new ClientPrincipale("127.0.0.1",3001);
					client.lancer();
				}
				
				sc.close();
				
				//FIN MODIF
				
			} catch (UnknownHostException e) {
				System.out.println("Erreur : probleme au niveau de la connexion client -> serveur");
			} catch (IOException e) {
				System.out.println("Erreur : probleme au niveau de la connexion client -> serveur");
			}
		
	}

}
