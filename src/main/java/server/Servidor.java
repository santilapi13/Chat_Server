package server;

import client.model.CredencialesUsuario;
import server.exceptions.UsernameRepetidoException;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Servidor implements Runnable {
    private HashMap<String, SocketUsuario> usuarios = new HashMap<String, SocketUsuario>();
    private ServerSocket socketServer;

    public Servidor() throws IOException {
        this.socketServer = new ServerSocket(2345);
    }

    public void registrarUsuario(Socket socket) throws IOException, UsernameRepetidoException {
        SocketUsuario socketUsuario = new SocketUsuario(socket);
        if (!this.usuarios.containsKey(socketUsuario.getUsername()))
            this.usuarios.put(socketUsuario.getUsername(), socketUsuario);
        else
            throw new UsernameRepetidoException();
    }

    @Override
    public void run() {
        try {
            while (true)
                this.escucharNuevosUsuarios();
        } catch (IOException e1) {
        } catch (UsernameRepetidoException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void escucharNuevosUsuarios() throws IOException, UsernameRepetidoException {
        Socket socket = socketServer.accept();
        this.registrarUsuario(socket);
    }

    public void escucharSolicitudes() throws IOException {
        this.usuarios.forEach((k, v) -> {
            try {
                // Itera sobre todos los usuarios que no tienen interlocutor para ver si alguno realizo una solicitud.
                // TODO: checkear que este en modo escucha el interlocutor.
                if (v.getInterlocutor() == null) {
                    String mensaje = v.getEntrada().readLine();
                    v.setInterlocutor(procesarSolicitud(mensaje, v.getUsername()));
                }
            } catch (IOException e) {
            }
        });
    }

    private String procesarSolicitud(String mensaje, String username) {
        String[] partes = mensaje.split(" ");
        String IP = partes[0];
        int puerto = Integer.parseInt(partes[1]);
        String usernameInterlocutor = null;

        // Busca el usuario con el que quiere establecer conexion.
        for (Map.Entry<String, SocketUsuario> entry : this.usuarios.entrySet()) {
            SocketUsuario v = entry.getValue();
            // Si el IP y puerto ingresados coinciden con alguno, los vincula (setea el interlocutor).
            if (v.getSocket().getInetAddress().toString().equals(IP) && v.getSocket().getPort() == puerto) {
                v.setInterlocutor(username);
                usernameInterlocutor =  v.getUsername();
            }
        }
        return usernameInterlocutor;
    }

    public void escucharMensajes() throws IOException {

    }

}
