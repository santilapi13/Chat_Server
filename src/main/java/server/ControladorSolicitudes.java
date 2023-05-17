package server;

import java.io.IOException;

public class ControladorSolicitudes extends Thread {

    //singleton
    private static ControladorSolicitudes instance;

    public static ControladorSolicitudes getInstance() {
        if (instance == null) {
            instance = new ControladorSolicitudes();
        }
        return instance;
    }

    private ControladorSolicitudes() {
    }

    @Override
    public void run() {
        while (true) {
            try {
                Servidor.getInstance().escucharSolicitudes();
            } catch (IOException e) {
            }
        }
    }

}
