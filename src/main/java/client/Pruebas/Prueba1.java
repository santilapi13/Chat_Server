package client.Pruebas;

import java.net.InetAddress;
import java.net.UnknownHostException;

import client.controller.ControladorPrincipal;
import client.controller.ControladorRegistro;
import client.model.Usuario;
import client.view.VentanaRegistro;

public class Prueba1 {

	
	
	public static void main(String[] args) throws UnknownHostException {

		System.out.println("Mi puerto es: " + Usuario.getInstance().getPuerto());
		System.out.println("Mi IP es: " + InetAddress.getLocalHost());
		System.out.println("Mi IP es: " + (InetAddress.getLocalHost()).getHostAddress());
		ControladorRegistro.getInstance();
	}

}
