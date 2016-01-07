package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import reservation.PreReservation;
import vol.VolsSimple;

public class ServiceValiderPrereservation implements Runnable,IService {
	
	private Socket client;
	private Thread t;
	private List<PreReservation> listePrereservation ;
	
	public ServiceValiderPrereservation(Socket client) {
		this.client = client;
		this.t = new Thread(this);
		this.listePrereservation = new ArrayList<PreReservation>();
		//donnée de test
		VolsSimple vol = new VolsSimple(50, "Londres", "07/01/2016", 500, "compagnie", 150);
		for(int i = 0 ;i<10;i++);
			this.listePrereservation.add(new PreReservation(vol, 5));
	}
	
	
	@Override
	public void traitement() throws IOException {
		//Streams pour le serveur
		BufferedReader sin = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
		PrintWriter sout = new PrintWriter(this.client.getOutputStream(), true);
		
		sout.println("Nouvelle session bienvenue sur le service de validation des préreservation");
		sout.flush();
		String reponseClient ="";
		sout.println("Veuillez saisir un numéro de reservation :");
		//recuperation du numéro de reservation
		reponseClient = sin.readLine();
		int numeroReservation = Integer.parseInt(reponseClient);
		
		while(!this.numeroEstValide(numeroReservation)){
			sout.println("Veuillez saisir un numéro valide (>0)");
			reponseClient = sin.readLine();
			numeroReservation = Integer.parseInt(reponseClient);
		}
		//message au client selon les differents cas
		PreReservation p = this.prereservationExist(numeroReservation);
		while(p == null){
			sout.println("Votre reservation n'a pas été trouvée veuilez saisir à nouveau ");
			reponseClient = sin.readLine();
			numeroReservation = Integer.parseInt(reponseClient);
			p = this.prereservationExist(numeroReservation);
		}
		if(prereservationEstValide(p))
			sout.println("Votre préreservation est maintenant validée !");
		else
			sout.println("Votre prereservation n'est plus valide car le delais de 24h est depassé");
		
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
	 * @param p la prereservation
	 * @return true si elle valide (date < 24h et prereservation existe) false sinon
	 */
	private boolean prereservationEstValide(PreReservation p){
		return p!=null && p.preReservationValide();
	}
	
}
