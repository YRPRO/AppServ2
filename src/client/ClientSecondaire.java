package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSecondaire {
	
	private final static int PORT = 3000;
	private final static String HOST = "localhost"; 
	private static String SEPARATEUR = ";";

	public static void main(String[] args) {
		Socket s = null;
		
		try{
			
			s = new Socket(HOST, PORT);
			
			//Streams pour le serveur
			BufferedReader sin = new BufferedReader(new InputStreamReader(s.getInputStream()));
			PrintWriter sout = new PrintWriter(s.getOutputStream(), true);
			
			//Stream pour saisies claviers
			BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.println("Connecté au serveur " + s.getInetAddress() + ":"+ s.getPort());
			
			while(true){
				String laSaisie;
				String volsDisponibles;
				laSaisie = demandeDeSaisie(clavier);
				if(laSaisie == null){
					continue;
				}
				else{
					sout.println(laSaisie);
					volsDisponibles = sin.readLine();
					volsDisponibles = interpreteurDeResultat(volsDisponibles);
					System.out.println(volsDisponibles);
					choixVol(clavier, sout, sin);
					
				}
				
				
			}
		} catch (IOException e) {
			System.err.println(e); 
		}

	}
	
	
	/*
	 * Méthode static de demande de saisie
	 * Donne les indications a l'utilisateur
	 * Param in : le Stream de saisie clavier
	 * Return : String correspondant aux saisies clavier
	 */
	private static String demandeDeSaisie(BufferedReader clavier) throws IOException{
		String laSaisie;
		System.out.println("Quelle est la destination ?");
		laSaisie = clavier.readLine();
		laSaisie += ";";
		System.out.println("Quelle est la date de départ ? (au format JJMMAAAA)");
		laSaisie = clavier.readLine();
		laSaisie += ";";
		System.out.println("Nombre de place à réserver ?");
		laSaisie = clavier.readLine();
		laSaisie += ";";

		return laSaisie;
	}
	
	
	/*
	 * Méthode static, interprète le String reçu du serveur
	 * param in : String, la liste des vols disponnible reçu du serveur
	 * return : la liste des vols disponnibles mieux présenté
	 */
	private static String interpreteurDeResultat(String leRésultat){
		String leRésultatInterpreté = new String();
		String[] tmp = leRésultat.split(SEPARATEUR);
		for (int i=0; i< tmp.length; i++){
			leRésultatInterpreté = tmp[i] + "\n";
		}
		return leRésultatInterpreté;
	}
	
	
	/*
	 * Méthode static traitant le choix du numéro de vol à réserver
	 * param in : BufferedReader clavier, récupère les saisies clavier; PrintWriter sout, BufferedReader sin, 
	 																	Stream de communication avec le serveur
	 */
	private static void choixVol(BufferedReader clavier, PrintWriter sout, BufferedReader sin) throws IOException{
		String choixVol;
		String choixValide;
		System.out.println("Choisissez un numéro de vol");
		choixVol = clavier.readLine();
		sout.println(choixVol);
		choixValide = sin.readLine();
		while(choixValide != "Bon"){
			System.out.println("Choix de vol erroné.");
			System.out.println("Veuillez choisir un numéro de vol valide.");
			choixVol = clavier.readLine();
			sout.println(choixVol);
			choixValide = sin.readLine();
		}
	}
	
	
	private static void confirmationReservation(BufferedReader clavier, PrintWriter sout, BufferedReader sin) throws IOException{
		String confirmation;
		confirmation = sin.readLine();
		
	}
	
	
//	private static void reservation(BufferedReader clavier, PrintWriter sout, BufferedReader sin) throws NumberFormatException, IOException{
//		int nbPlaceLibre;
//		int nbPlaceChoisis;
//		nbPlaceLibre = Integer.parseInt(sin.readLine());
//		System.out.println("Il y a " + nbPlaceLibre + " places libres");
//		System.out.println("Combien de places voulez-vous réserver");
//		nbPlaceChoisis = Integer.parseInt(clavier.readLine());
//		
//		while(nbPlaceChoisis > nbPlaceLibre){
//			System.out.println("Il n'y a pas autant de places disponibles");
//			System.out.println("Le nombre de places réservés ne doit pas dépasser " + nbPlaceLibre);
//			nbPlaceChoisis = Integer.parseInt(clavier.readLine());
//		}			
//	}

}
