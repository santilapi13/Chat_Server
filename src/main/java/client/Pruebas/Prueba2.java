package client.Pruebas;

import client.controller.ControladorPrincipal;
import client.controller.ControladorRegistro;
import client.model.Usuario;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Prueba2 {
    public static void main(String[] args) throws UnknownHostException {
        int puerto=1123;
        Usuario.getInstance().setPuerto(puerto);
        System.out.println("Mi puerto es: " + Usuario.getInstance().getPuerto());
        System.out.println("Mi IP es: " + InetAddress.getLocalHost());

        ControladorRegistro.getInstance();

    }

}