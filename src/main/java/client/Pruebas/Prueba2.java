package client.Pruebas;

import client.controller.ControladorPrincipal;
import client.model.Usuario;

import java.net.UnknownHostException;

public class Prueba2 {
    public static void main(String[] args) throws UnknownHostException {
        int puerto=1123;
        Usuario.getInstance().setPuerto(puerto);
        System.out.println("Mi puerto es: " + Usuario.getInstance().getPuerto());

        ControladorPrincipal.getInstance();

    }

}