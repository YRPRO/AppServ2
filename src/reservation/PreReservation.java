package reservation;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

import vol.Vol;

public class PreReservation implements Serializable{
	private static final long serialVersionUID = 1L;
	private static int NbReservation = 0;
	private Vol volPreReserver;
	private int nbPlace;
	private Calendar dateExpirationPreReservation;
	private int numeroReservation;
	
	public PreReservation(Vol volReserver, int nbPlace){
		PreReservation.NbReservation++;
		this.volPreReserver = volReserver;
		this.nbPlace = nbPlace;
		this.dateExpirationPreReservation = new GregorianCalendar();
		this.dateExpirationPreReservation.roll(Calendar.DAY_OF_YEAR, true);
		this.numeroReservation = NbReservation;
	}
	/**
	 * Methode permettant de savoir si la prereservation est encore valide
	 * au niveau de la date (-de 24h)
	 * @return true si la date est encore valide false sinon
	 */
	public boolean preReservationValide(){
		Calendar dateActuel = new GregorianCalendar();
		if(dateActuel.compareTo(this.dateExpirationPreReservation) <= 0)
			return true;
		return false;
	}	
	
	public int getNumeroReservation(){
		return this.numeroReservation;
	}
	
	public String toString(){
		return "Numero de reservation : " + this.getNumeroReservation()+ " vol : " + this.volPreReserver.toString();
	}
	
}