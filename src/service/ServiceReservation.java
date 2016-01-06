package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import vol.LecteurFicherVol;
import vol.Vol;
import vol.VolsSimple;

public class ServiceReservation implements Runnable, IService{
	private static final long TEMPS_TIMEOUT = 600000; // 10 minutes
	// timeout 30000; //30seconde 
	private Socket client;
	private Thread t;
	private List<VolsSimple> vols;
	private Timer tempsEcouler;
	
	public  ServiceReservation(Socket s) throws ClassNotFoundException, IOException {
		this.client = s;
		this.t = new Thread(this);
		this.vols = LecteurFicherVol.serialisationBinToList();
		this.tempsEcouler = new Timer();
		//lancement de la tache planifier
		tempsEcouler.schedule(new TimeOutServiceReservation(this), TEMPS_TIMEOUT);
	}
	
	public Socket getSocket(){
		return this.client;
	}
	
	public Thread getThread(){
		return this.t;
	}

	@Override
	public void run() {
		try {
			this.traitement();
		} catch (IOException e) {
			System.out.println("Erreur : Probleme au niveau des stream dans le service reservation");
		}
	}
	/**
	 * methode de lancement du thread
	 */
	public void lancer(){
		this.t.start();
	}
	/**
	 * methode d'interruption du thread
	 */
	public void terminer(){
		this.t.interrupt();
	}
	/* (non-Javadoc)
	 * @see service.IService#traitement()
	 */
	@Override
	public void traitement() throws IOException{
		//Streams pour le serveur
		BufferedReader sin = new BufferedReader(new InputStreamReader(client.getInputStream()));
		PrintWriter sout = new PrintWriter(client.getOutputStream(), true);
		
		sout.println("Nouvelle session ");
		sout.flush();
		
		String destination = "";
		String date = "";
		int nbPersonne = 0;
		List<VolsSimple> volsTrouver = new ArrayList<VolsSimple>();
		
		do {
			//envoi des vols dispo
			sout.println("vols dispo \n" + this.vols.toString());
			sout.flush();
			destination = dialogueDemandeDestination(sin, sout);
			date = dialogueDemandeDate(sin, sout);
			nbPersonne = dialogueDemandeNbPlace(sin, sout);
			volsTrouver = rechercheVol(destination, date, nbPersonne);
			
			if(!volsTrouver.isEmpty()){
				sout.println(volsTrouver.toString());
				dialogueChoixDuVol(sin, sout, volsTrouver,nbPersonne);
			}
			else
				sout.println("Aucun vol n'est disponible pour vos critères ");
			
		} while(dialogueDemandeNouvelleRecherche(sin, sout));
		synchronized (this.vols) {
			LecteurFicherVol.serialisationListToBin(this.vols);
		}
		sout.println("Stop");
		//FERMETURE DE LA SOCKET ET TERMINAISON DU THREAD
		this.client.close();
		sout.close();
		sin.close();
		this.terminer();
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
	 * Methode permetant la demande de la destination au client
	 * @param sin le PrintWriter attribué à la socket 
	 * @param sout le BufferedReader attribué à la socket 
	 * @return la destination (String) une fois saisie et verifiée
	 * @throws IOException
	 */
	private String dialogueDemandeDestination(BufferedReader sin, PrintWriter sout) throws IOException{
		String destination;
		envoieMessage("Quel est votre déstination ?", sout);
		destination = sin.readLine();
		while(!verifDestination(destination)){
			envoieMessage("Veuillez entrer une destination valide", sout);
			destination = sin.readLine();
		}
		return destination;
	}
	/**
	 * Methode permetant la demande de la date de depart au client
	 * @param sin le PrintWriter attribué à la socket 
	 * @param sout le BufferedReader attribué à la socket 
	 * @return la date (String) une fois saisie et verifiée
	 * @throws IOException
	 */
	private String dialogueDemandeDate(BufferedReader sin, PrintWriter sout) throws IOException{
		String date;
		envoieMessage("Quel est la date de départ ?", sout);
		date = sin.readLine();
		while(!verifDate(date)){
			envoieMessage("Veuillez entrer une date valide au format jj/mm/aaaa", sout);
			date = sin.readLine();
		}
		return date;
	}
	/**
	 * Methode permetant la demande du nombre de place à reserver au client
	 * @param sin le PrintWriter attribué à la socket 
	 * @param sout le BufferedReader attribué à la socket 
	 * @return le nombre de place (int) une fois saisie et verifiée
	 * @throws IOException
	 */
	private int dialogueDemandeNbPlace(BufferedReader sin, PrintWriter sout) throws IOException{
		int nbPersonne ;
		envoieMessage("Combien de place(s) souhaitez vous réserver", sout);
		nbPersonne = Integer.parseInt(sin.readLine());
		
		while(!verifNbPlace(nbPersonne)){
			envoieMessage("Veuillez entrer un nombre de place valide", sout);
			nbPersonne = Integer.parseInt(sin.readLine());
		}
		return nbPersonne;
	}
	/**
	 * Methode permetant la demande d'arret au client
	 * @param sin le PrintWriter attribué à la socket 
	 * @param sout le BufferedReader attribué à la socket 
	 * @return boolean true si le client veut une nouvelle recherche false sinon
	 * @throws IOException
	 */
	private boolean dialogueDemandeNouvelleRecherche(BufferedReader sin, PrintWriter sout) throws IOException {
		String reponse;
		boolean bonneReponse = false;
		envoieMessage("Voulez-vous faire une nouvelle recherche (oui ou non) ?", sout);
		reponse = sin.readLine();
		//verification de la reponse
		while(!bonneReponse){
			if(reponse.equalsIgnoreCase("oui") || reponse.equalsIgnoreCase("non"))
				bonneReponse = true;
			else 
				reponse = sin.readLine();
		}
		
		if(reponse.equalsIgnoreCase("oui"))
			return true;
		return false;
	}
	/**
	 * Methode permettant au client de choisir son vol parmi ceux validant ses criteres 
	 * @param sin le PrintWriter attribué à la socket 
	 * @param sout le BufferedReader attribué à la socket 
	 * @param volsSelectionner une liste de vol validant les criteres
	 * @param nbPersonne 
	 * @throws IOException
	 */
	private void dialogueChoixDuVol(BufferedReader sin, PrintWriter sout,List<VolsSimple> volsSelectionner, int nbPersonne) throws IOException{
		int reponse;
		boolean volTrouver = false;
		boolean volConfirmer = false;
		envoieMessage("Veuillez entrer le numéro du vol que vous souhaitez reserver", sout);
		reponse =Integer.parseInt(sin.readLine());
		//verification du vol parmi les vols possible
		for(Vol v : volsSelectionner){
			if(v.getNumero() == reponse){
				volTrouver = true;
				break;
			}
		}
		//verification : le numero de vol doit correspondre à un vols selectionner
		if(volTrouver)
			volConfirmer = dialogueDemandeconfirmation(sin, sout);
		else
			envoieMessage("Le numéro de vol saisi ne correspond pas une nouvelle saisie est necessaire ", sout);
		//si le numero de vol correspond et le vol est confirmé alors la reservation est effectuée
		if(volTrouver && volConfirmer){
			envoieMessage("Votre vol est maintenant reserver (appuyer sur entrer)", sout);
			//suppression des place
			supprimerPlace(this.vols, reponse, nbPersonne);
		}
			
		
	}
	
	/**
	 * Methode de demande au client la confirmation du choix de son vol
	 * @param sin le PrintWriter attribué à la socket 
	 * @param sout le BufferedReader attribué à la socket 
	 * @return boolean true si le client confirme le vol false sinon
	 * @throws IOException
	 */
	private boolean dialogueDemandeconfirmation(BufferedReader sin, PrintWriter sout) throws IOException {
		String reponse;
		envoieMessage("Confirmez vous le choix de ce vol (oui ou non) ?", sout);
		reponse = sin.readLine();
		while((!reponse.equalsIgnoreCase("oui")) && (!reponse.equalsIgnoreCase("non"))){
			envoieMessage("Vous devez répondre par oui ou non", sout);
			reponse = sin.readLine();
		}
		if(reponse.equalsIgnoreCase("oui")){
			return true;
		}
		return false;
	}
	/**
	 * Methode permettan de supprimer des place dans un vol et donc de valider la réservation
	 * @param vols liste de vols disponible
	 * @param numeroVol le numéro du vol concerné
	 * @param nbPlaceAreserver le nombre de place à reserver
	 */
	synchronized private void supprimerPlace(List<VolsSimple> vols, int numeroVol, int nbPlaceAreserver){
		for(Vol v : vols){
			if(v.getNumero() == numeroVol)
				v.setNbPlace(v.getNbPlace() - nbPlaceAreserver);
		}
	}
	
	/**
	 * Permet la verification (au niveau syntaxique) de la destination entrer par le client
	 * @param destination la chaine à verifier 
	 * @return true si la syntaxe est correcte false sinon
	 */
	private  boolean verifDestination(String destination){
		return (destination.length() != 0);
	}
	/**
	 * Permet la verification (au niveau syntaxique) avec une éxpression régulière de la date entrer par le client
	 * @param date la chaine à verifier
	 * @return true si la syntaxe est correcte false sinon
	 */
	private  boolean verifDate(String date){	
		String dateRegex = "^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$";
		return (date.length()!=0 && date.matches(dateRegex) );
	}
	/**
	 * Permet la verification (au niveau syntaxique) du nombre de place(s) entrer par le client
	 * @param nbPlace la valeur à verifier
	 * @return true si la syntaxe est correcte false sinon
	 */
	private  boolean verifNbPlace(int nbPlace){
		return nbPlace !=0;
	}
	/**
	 * méthode permetant de recherche les vols correspondant aux critères du client
	 * @param destination (String)
	 * @param date (String)
	 * @param nbPlace (int)
	 * @return une liste de vol contenant l'ensemble des vols correspondant aux critères du client
	 */
	private List<VolsSimple> rechercheVol(String destination,String date,int nbPlace){
		List<VolsSimple> volSelectionner = new ArrayList<VolsSimple>(); 
		for(VolsSimple v : this.vols){
			if(v.getDestination().equalsIgnoreCase(destination) && v.getDate().equalsIgnoreCase(date) && v.getNbPlace() >= nbPlace)
				volSelectionner.add(v);
		}
		return volSelectionner;
	}
}
