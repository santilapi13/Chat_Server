package server;

import java.io.IOException;

public class ControladorNuevos extends Thread {

    //singleton
    private static ControladorNuevos instance;

    public static ControladorNuevos getInstance() {
        if (instance == null) {
            instance = new ControladorNuevos();
        }
        return instance;
    }

    private ControladorNuevos() {
    }

    @Override
    public void run() {
        try {
            while (true)
                Servidor.getInstance().escucharNuevosUsuarios();
        } catch (IOException e1) {
        }
    }

}
