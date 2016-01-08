package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientPrincipale implements Runnable{
	
	private Socket socketClient;
	private Thread t;
	
	public ClientPrincipale(String adr,int port) throws UnknownHostException, IOException{
		this.socketClient = new Socket(adr,port);
		this.t = new Thread(this);
	}

	@Override
	public void run() {
		try {
			this.traitement();
		} catch (IOException e) {
			System.out.println("Erreur : probleme dans le stream du client ");
		}
	}
	
	public void lancer(){
		this.t.start();
	}
	
	public void terminer(){
		this.t.interrupt();
	}
	
	public void traitement() throws IOException{
		//creation de stream d'acces au service
		BufferedReader sin = new BufferedReader(new InputStreamReader(this.socketClient.getInputStream()));
		PrintWriter sout = new PrintWriter(this.socketClient.getOutputStream(), true);
		Scanner sc = new Scanner(System.in);
		String message = "";
		boolean connecter = true;
		//REPONSE VERS LE SERVEUR
		dialogue(sc, sin, sout);
		//fermerture de l'application
		this.socketClient.close();
		sin.close();
		sout.close();
		sc.close();
		System.out.println("l'application se ferme");
		this.terminer();
		
	}
	
	private void dialogue(Scanner sc, BufferedReader sin, PrintWriter sout) throws IOException{
		String message = "";
		String reponse = "";
		boolean connexionEnCours = true;
		while(connexionEnCours){
			///R�ception du message
			message = sin.readLine();
			//V�rification si le service s'est arr�t�
			if(message.equalsIgnoreCase("stop"))
				connexionEnCours = false;
			//V�rification si le serveur attend une r�ponse
			else if(message.equals("AttenteReponse")){
				reponse = sc.nextLine();
				sout.println(reponse);
				sout.flush();
			}
			
			//Affichage du message s'il doit �tre affich�
			else
				System.out.println("Serveur : " +message);
		}
	}
}
