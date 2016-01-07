package service;

import java.io.IOException;
import java.net.Socket;


public class ServiceFactory {

	public  IService  creer(TypeService service,Socket client) throws ClassNotFoundException, IOException{
		if(service == TypeService.SERVICE_RESERVATION)
			return new ServiceReservation(client);
		else
			return new ServiceValiderPrereservation(client);
	}
}
