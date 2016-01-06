package service;

import java.io.IOException;

public interface IService {

	/**
	 * Méthode contenant le traitement du service 
	 * @throws IOException
	 */
	public abstract void traitement() throws IOException;
	public void run();
	/**
	 * methode de lancement du thread
	 */
	public void lancer();
	/**
	 * methode d'interruption du thread
	 */
	public void terminer();

}