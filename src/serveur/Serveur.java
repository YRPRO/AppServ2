package serveur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import service.IService;
import service.ServiceFactory;
import service.TypeService;

public class Serveur implements Runnable {
	
	private ServerSocket socketServeur;
	private Thread t;
	private ServiceFactory factory;
	private TypeService typreService;
	
	public Serveur( int port,TypeService service) throws IOException{
		this.socketServeur = new ServerSocket(port);
		this.t = new Thread(this);
		this.factory = new ServiceFactory();
		this.typreService = service;
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
					IService service = this.factory.creer(this.typreService, socketClient); 
					//lancement du service
					service.lancer();
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
		System.out.println("Serveur " +typreService.toString() + " lancer");
	}
	/**
	 * methode d'interruption du thread
	 */
	public void terminer(){
		this.t.interrupt();
	}
	

}
