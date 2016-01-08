package reservation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class OutilSerialisationPrereservation {
	private final static String NOM_FICHIER_SAUVEGARDE = "prereservationSerialiser.ser";
	/**
	 * Methode permettant de serialiser une liste de prereservation
	 * @param listeASeriliser la liste à serialiser
	 * @throws FileNotFoundException
	 */
	public static void serialisationListToBin(List<PreReservation> listeASeriliser) throws FileNotFoundException{
		FileOutputStream fichierPrereservation = new FileOutputStream(NOM_FICHIER_SAUVEGARDE);
		try {
			ObjectOutputStream objPrereservation = new ObjectOutputStream(fichierPrereservation);
			objPrereservation.writeObject(listeASeriliser);
			objPrereservation.flush();
			objPrereservation.close();
		} catch (IOException e) {
			System.out.println("Erreur : probleme lors de la serialisation des prereservations");
		}
	}
	
	/**
	 * Methode permettant de déserialiser une liste de prereservations depuis un fichier binaire de sauvegarde
	 * @return la liste de prereservation 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static List<PreReservation> serialisationBinToList() throws IOException, ClassNotFoundException{
		FileInputStream fichierPrereservation = new FileInputStream(NOM_FICHIER_SAUVEGARDE);
		ObjectInputStream objPrereservation;
		objPrereservation = new ObjectInputStream(fichierPrereservation);
		List<PreReservation> prereservationRecuperer =  (List<PreReservation>) objPrereservation.readObject();
		objPrereservation.close();
		return prereservationRecuperer;
	}
}
