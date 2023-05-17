package server;

import client.model.CredencialesUsuario;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Servidor {
    private HashMap<String, SocketUsuario> usuarios = new HashMap<String, SocketUsuario>();
    private ServerSocket socketServer;
    private static Servidor instance;

    public static Servidor getInstance() throws IOException {
        if (instance == null)
            instance = new Servidor();
        return instance;
    }

    private Servidor() throws IOException {
        this.socketServer = new ServerSocket(2345);
    }

    public void registrarUsuario(Socket socket) throws IOException {
        SocketUsuario socketUsuario = new SocketUsuario(socket);
        if (!this.usuarios.containsKey(socketUsuario.getUsername())) {
            System.out.println("Usuario registrado: " + socketUsuario.getUsername());
            socketUsuario.getSalida().println("200");
            this.usuarios.put(socketUsuario.getUsername(), socketUsuario);
        } else
            socketUsuario.getSalida().println("409");
    }

    public void escucharNuevosUsuarios() throws IOException {
        Socket socket = socketServer.accept();
        System.out.println("Usuario con IP " + socket.getInetAddress() + " intenta registrarse");
        this.registrarUsuario(socket);
    }

    public void escucharSolicitudes() {
        //System.out.println("Escuchando solicitudes..");
        for (Map.Entry<String, SocketUsuario> entry : this.usuarios.entrySet()) {
            try {
                System.out.println("LLEGA AL TRY");
                SocketUsuario usuarioActual = entry.getValue();
                //System.out.println("Escuchando solicitudes de " + usuarioActual.getUsername());
                // TODO: checkear que este en modo escucha el interlocutor.
                if (usuarioActual.getInterlocutor() == null) {
                    System.out.println("LLEGA ANTES DEL READLINE");
                    String mensaje = usuarioActual.getEntrada().readLine();
                    System.out.println("LLEGA DESPUES DEL READLINE");
                    if (Integer.parseInt(mensaje) == 503) { // Si el usuario se desconecto, lo elimina de la lista.
                        System.out.println("Usuario " + usuarioActual.getUsername() + " se desconecto.");
                        this.usuarios.remove(usuarioActual.getUsername());
                    } else
                        usuarioActual.setInterlocutor(procesarSolicitud(mensaje, usuarioActual.getUsername()));
                }
            } catch (IOException e) {
            }
        }
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
