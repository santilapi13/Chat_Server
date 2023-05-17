package server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {

            Servidor.getInstance();
            ControladorNuevos.getInstance().start();
            ControladorSolicitudes.getInstance().start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
