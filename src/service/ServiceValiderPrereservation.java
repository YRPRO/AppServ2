package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import reservation.PreReservation;

public class ServiceValiderPrereservation implements IService {
	
	private Socket client;
	private Thread t;
	private List<PreReservation> listePrereservation ;
	
	public ServiceValiderPrereservation(Socket client) {
		this.client = client;
		this.t = new Thread();
		//liste a remplir plus tard
		this.listePrereservation = new ArrayList<PreReservation>();
	}
	
	
	@Override
	public void traitement() throws IOException {
		//Streams pour le serveur
		BufferedReader sin = new BufferedReader(new InputStreamReader(client.getInputStream()));
		PrintWriter sout = new PrintWriter(client.getOutputStream(), true);
				
		sout.println("Nouvelle session bienvenu sur le service de validation des préreservation");
		sout.flush();
		String reponseClient ="";
		sout.println("Veuillez saisir un numéro de reservation :");
		//recuperation du numéro de reservation
		reponseClient = sin.readLine();
		int numeroReservation = Integer.parseInt(reponseClient);
		
		while(this.numeroEstValide(numeroReservation)){
			sout.println("Veuillez saisir un numéro valide (>0)");
			reponseClient = sin.readLine();
			numeroReservation = Integer.parseInt(reponseClient);
		}
		
	}
	
	
	@Override
	public void run() {
		try {
			this.traitement();
		} catch (IOException e) {
			System.out.println("Erreur : probleme lancement service prereservation " + e.toString());
		}
	}

	@Override
	public void lancer() {
		this.t.start();
	}

	@Override
	public void terminer() {
		this.t.interrupt();
	} 	
	/**
	 * methode de validation du numero saisie (il est doit superieur à 0)
	 * @param numero à traiter
	 * @return true si le numéro est valide false sinon
	 */
	private boolean numeroEstValide(int numero){
		return numero>0;
	}
	/**
	 * Methode permettant de retrouver la prereservation grace a son numero
	 * @param numero de reservation
	 * @return la prereservation si elle existe null sinon
	 */
	private PreReservation prereservationExist(int numero){
		for(PreReservation p : this.listePrereservation){
			if(p.getNumeroReservation() == numero)
				return p;
		}
		return null;
	}
	/**
	 * Methode permettant de valider une prereservation grace à son numero
	 * @param numero de la reservation
	 * @return true si elle valide (date < 24h et prereservation existe) false sinon
	 */
	private boolean prereservationEstValide(int numero){
		PreReservation p = prereservationExist(numero);
		return p!=null && p.preReservationValide();
	}
}
