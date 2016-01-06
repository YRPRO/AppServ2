package vol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LecteurFicherVol {
	
	private static final String CARACTERE_SEPARATEUR = ";";
	private static final int INDICE_NUM_VOL = 0;
	private static final int INDICE_DESINATION = 1;
	private static final int INDICE_DATE_VOL = 2;
	private static final int INDICE_NB_PLACE = 3;
	private static final int INDICE_COMPAGNIE = 4;
	private static final int INDICE_PRIX = 5 ;
	private static final String NOM_FICHIER_BIN_SAUV = "vols.ser";
	
	/**
	 * Methode permettant de récuperer une liste d'objet Vol à partir d'un fichier texte
	 * @param chemin le chemin (String) du fichier
	 * @return une liste de vols
	 * @throws FileNotFoundException
	 */
	public static List<VolsSimple> ficherTextToList(String chemin) throws FileNotFoundException{
		//creation du fichier 
		File ficher = new File(chemin);
		//creation d'un scanner
		Scanner sc = new Scanner(ficher);
		//liste contenant les vol
		ArrayList<VolsSimple> vols = new ArrayList<VolsSimple>(); 
		while(sc.hasNext()){
			String ligne = sc.nextLine();
			//recuperation des parametre dans un tableau
			String parametre[] = ligne.split(CARACTERE_SEPARATEUR);
			//parametre de creation d'un vol
			int numero = Integer.parseInt( parametre[INDICE_NUM_VOL]);
			String destination = parametre[INDICE_DESINATION];
			String date =parametre[INDICE_DATE_VOL];
			int nbPlace =Integer.parseInt( parametre[INDICE_NB_PLACE]);
			String compagnie = parametre[INDICE_COMPAGNIE];
			float prix =Float.parseFloat(parametre[INDICE_PRIX]);
			
			//creation du vol 
			vols.add(new VolsSimple(numero, destination, date, nbPlace, compagnie, prix));
		}
		//fermeture du scanner
		sc.close();
		//retour de la liste complete des vols disponibles
		return vols;
	}
	/**
	 * Methode permettant de recuperer une liste de vol à partir du fichier binaire de sauvegarde
	 * @return une liste de vols
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static List<VolsSimple> serialisationBinToList() throws IOException, ClassNotFoundException{
		FileInputStream fichier = new FileInputStream(NOM_FICHIER_BIN_SAUV);
		ObjectInputStream volSerialiser = new ObjectInputStream(fichier);
		List<VolsSimple> volRecuperer =  (List<VolsSimple>) volSerialiser.readObject();
		volSerialiser.close();
		return volRecuperer;
	}
	/**
	 * Methode permettant de sérialiser une liste de vols dans le fichier binaire de sauvegarde
	 * @param volsAserialiser
	 * @throws FileNotFoundException
	 */
	public static void serialisationListToBin(List<VolsSimple> volsAserialiser) throws FileNotFoundException{
		FileOutputStream fichierVols = new FileOutputStream(NOM_FICHIER_BIN_SAUV);
		try {
			ObjectOutputStream objVols = new ObjectOutputStream(fichierVols);
			objVols.writeObject(volsAserialiser);
			objVols.flush();
			objVols.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
