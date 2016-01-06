package serveur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import service.IService;
import service.ServiceReservation;

public class Serveur implements Runnable {
	
	private ServerSocket socketServeur;
	private Thread t;
	
	public Serveur( int port) throws IOException{
		this.socketServeur = new ServerSocket(port);
		this.t = new Thread(this);
	}
	
	public ServerSocket getSocketServeur(){
		return this.socketServeur;
	}
	

	@Override
	public void run() {
		//creation d'un service de reservation de vol
		//acceptation des connexions par les clients
			try {
				while(true){
					//creation de la socket du client
					Socket socketClient = this.getSocketServeur().accept();
					System.out.println("connexion avec le client reussi ");
					//creation du service
					IService serviceReservation = new ServiceReservation(socketClient);
					//lancement du service
					serviceReservation.lancer();
				}
				
			} catch (IOException e) {
				System.out.println(e.toString());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
	}
	/**
	 * methode de lancement du thread
	 */
	public void lancer(){
		this.t.start();
		System.out.println("Serveur lancer");
	}
	/**
	 * methode d'interruption du thread
	 */
	public void terminer(){
		this.t.interrupt();
	}
	

}
