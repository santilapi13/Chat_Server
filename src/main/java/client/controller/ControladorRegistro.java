package client.controller;

import client.model.Usuario;
import client.view.IVista;
import client.view.VentanaChat;
import client.view.VentanaPrincipal;
import client.view.VentanaRegistro;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

public class ControladorRegistro  implements ActionListener  {

    private IVista vista;

    private static ControladorRegistro instance;

    private ControladorRegistro()  {
        this.vista = new VentanaRegistro();
        this.vista.setActionListener(this);
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
        try {
            if (comando.equalsIgnoreCase("Registrarse")) {
                String ip = vista.getDireccionIP();
                int puerto = Integer.parseInt(vista.getPuertoIP());
                String usuario = vista.getText();

                //TODO
                Usuario.getInstance().registrarseEnServidor(ip, puerto, usuario);

                int msg = -1;
                while (msg == -1) {
                    try {
                        msg = Integer.parseInt(Usuario.getInstance().getEntrada().readLine());
                    } catch (IOException ex) {
                    }
                }


                if (msg == 409)
                    JOptionPane.showMessageDialog(null, "Username ya registrado. Elija uno nuevo.");
                else if (msg == 200) {
                    ((VentanaRegistro) vista).dispose();
                    ControladorPrincipal.getInstance().getVista().abrirVentana();
                }
            }
        } catch (UnknownHostException ex) {
            throw new RuntimeException(ex);
        } catch (IOException e1) {
            System.out.println("Error al conectarse al servidor");
        }

    }
}
