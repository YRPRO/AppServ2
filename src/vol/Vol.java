package vol;

import java.io.Serializable;


public class Vol implements Serializable{
	
	private int numero; 
	private String destination;
	private String date;
	private int nbPlace;
	private String compagnie;
	private float prix;
	
	public Vol(int numero, String destination, String date, int nbPlace,String compagnie, float prix) {
		this.numero = numero;
		this.destination = destination;
		this.date = date;
		this.nbPlace = nbPlace;
		this.compagnie = compagnie;
		this.prix = prix;
	}

	public int getNumero() {
		return numero;
	}

	public String getDestination() {
		return destination;
	}

	public String getDate() {
		return date;
	}

	public int getNbPlace() {
		return nbPlace;
	}

	public String getCompagnie() {
		return compagnie;
	}

	public float getPrix() {
		return prix;
	}
	public void setNbPlace(int nb){
		this.nbPlace = nb;
	}

	@Override
	public String toString() {
		return "[numero=" + numero + "| destination=" + destination
				+ "| date=" + date + "| nbPlace=" + nbPlace + "| compagnie="
				+ compagnie + "| prix=" + prix + "]" + "\n";
	}
}
