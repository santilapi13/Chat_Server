package client.controller;

import client.model.Usuario;
import client.view.IVista;
import client.view.VentanaChat;
import client.view.VentanaPrincipal;
import client.view.VentanaRegistro;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;

public class ControladorRegistro  implements ActionListener  {

    private IVista vista;

    private static ControladorRegistro instance;

    private ControladorRegistro() throws UnknownHostException {
        this.vista = new VentanaRegistro();
        this.vista.setActionListener(this);
        ((VentanaPrincipal) this.vista).setTextFieldNombre(Usuario.getInstance().getUsername());
    }

    public static ControladorRegistro getInstance() throws UnknownHostException {
        if (instance == null) {
            instance = new ControladorRegistro();
        }
        return instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if (comando.equals("Registrarse")) {
            String ip = vista.getDireccionIP();
            String puerto = vista.getPuertoIP();
            String usuario = vista.getText();

            //TODO
            //Se deberia asignar los campos donde vaya

            //se cierra VentanaRegistro y se abre VentanaPrincipal
            ((VentanaRegistro) vista).dispose();
            try {
                ControladorPrincipal.getInstance().getVista().abrirVentana();
            } catch (UnknownHostException ex) {
                throw new RuntimeException(ex);
            }

        }

    }
}
