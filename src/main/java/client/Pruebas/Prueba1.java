package client.Pruebas;

import java.net.UnknownHostException;

import client.controller.ControladorPrincipal;
import client.model.Usuario;

public class Prueba1 {

	
	
	public static void main(String[] args) throws UnknownHostException {

		System.out.println("Mi puerto es: " + Usuario.getInstance().getPuerto());
		// TODO: Instanciar ventana de registro

	}

}
