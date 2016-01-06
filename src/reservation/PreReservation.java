package reservation;

import java.util.Calendar;
import java.util.GregorianCalendar;

import vol.Vol;

public class PreReservation {
	private static int NbReservation = 0;
	private Vol volPreReserver;
	private int nbPlace;
	private Calendar dateExpirationPreReservation;
	
	public PreReservation(Vol volReserver){
		PreReservation.NbReservation++;
		this.volPreReserver = volReserver;
		this.nbPlace = nbPlace;
		this.dateExpirationPreReservation = new GregorianCalendar();
		this.dateExpirationPreReservation.roll(Calendar.DAY_OF_YEAR, true);
	}
	
	public boolean preReservationValide(){
		Calendar dateActuel = new GregorianCalendar();
		if(dateActuel.compareTo(this.dateExpirationPreReservation) <= 0)
			return true;
		return false;
	}	
	
	
}