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
				String r�ponse = "";
				
				//DEBUT MODIF
				//On demande au client s'il veut faire une r�servation ou valider une pr�reservation
				System.out.println("Voulez vous r�server ('1') ou valider une pr�reservation ('2')");
				
				do {
					System.out.println("Veuillez r�pondre par 1 ou 2");
					r�ponse = sc.nextLine();
				} while (!r�ponse.equals("1") && !r�ponse.equals("2"));
				
				if(r�ponse.equals("1")){
					ClientPrincipale client = new ClientPrincipale("127.0.0.1",3000);
					client.lancer();
				}
				if(r�ponse.equals("2")){
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
