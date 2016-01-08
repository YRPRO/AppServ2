package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import reservation.OutilSerialisationPrereservation;
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
		//donnée de test derialisées du fichier prereservationSerialiser.ser
		try {
			this.listePrereservation = OutilSerialisationPrereservation.serialisationBinToList();
		} catch (ClassNotFoundException e) {
			System.out.println("Erreur : impossible de deserialiser les prereservations");
		} catch (IOException e) {
			System.out.println("Erreur d'accés à prereservationSerialiser.ser " + e.toString());
		}
	}
	
	
	@Override
	public void traitement() throws IOException {
		//Streams pour le serveur
		BufferedReader sin = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
		PrintWriter sout = new PrintWriter(this.client.getOutputStream(), true);
		
		sout.println("Nouvelle session bienvenue sur le service de validation des préreservation");
		sout.flush();
		//recuperation du numéro de reservation
		int numeroReservation = dialogueDemandeNumPrereservation(sin, sout);
		//message au client selon les differents cas
		PreReservation p = this.prereservationExist(numeroReservation);
		while(p == null){
			sout.println("Votre reservation n'a pas été trouvée veuilez saisir à nouveau ");
			numeroReservation = dialogueDemandeNumPrereservation(sin, sout);
			p = this.prereservationExist(numeroReservation);
		}
		if(prereservationEstValide(p))
			sout.println("Votre préreservation est maintenant validée !");
		else
			sout.println("Votre prereservation n'est plus valide car le delais de 24h est depassé");
		//demande si le client veut faire une nouvelle valider
		dialogueNouvelleValidation(sin, sout);
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
	
	/**
	 * Methode permettant l'envoi d'un message à un client
	 * @param message le message à envoyer
	 * @param sout le Printwriter confirmer
	 * @throws IOException
	 */
	private void envoieMessage(String message, PrintWriter sout) throws IOException{	
		sout.println(message);
		sout.flush();
		sout.println("AttenteReponse");
		sout.flush();
	}
	/**
	 * Methode permettant la demande du numéro de prereservation au client
	 * @param sin de type BufferedReader
	 * @param sout de type PrintWriter
	 * @return le numero de prereservation une fois celui-ci valider
	 * @throws IOException
	 */
	private int dialogueDemandeNumPrereservation(BufferedReader sin,PrintWriter sout) throws IOException{
		envoieMessage("Veuillez saisir un numéro de reservation :", sout);
		//recuperation du numéro de reservation
		String reponseClient;
		reponseClient = sin.readLine();
		int numeroReservation = Integer.parseInt(reponseClient);
		while(!this.numeroEstValide(numeroReservation)){
			//sout.println("Veuillez saisir un numéro valide (>0)");
			envoieMessage("Veuillez saisir un numéro valide (>0)", sout);
			reponseClient = sin.readLine();
			numeroReservation = Integer.parseInt(reponseClient);
		}
		return numeroReservation;
	}
	/**
	 * Methode permettant de demander une nouvelle validation
	 * @param sin de type BufferedReader
	 * @param sout de type PrintWriter
	 * @throws IOException
	 */
	public void dialogueNouvelleValidation(BufferedReader sin,PrintWriter sout) throws IOException{
		envoieMessage("Voulez vous realiser une nouvelle validation ? (oui/non)", sout);
		String reponse = sin.readLine();
		while(!reponse.equalsIgnoreCase("oui") && !reponse.equalsIgnoreCase("non")){
			envoieMessage("Veuillez repondre par oui ou non", sout);
			reponse = sin.readLine();
		}
		if(reponse.equalsIgnoreCase("oui"))
			this.traitement();
		else{
			//fermeture de la connexion
			envoieMessage("stop", sout);
			this.client.close();
			this.terminer();
		}
			
			
	}
}
