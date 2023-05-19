package server;

import client.model.CredencialesUsuario;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
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

    public HashMap<String, SocketUsuario> getUsuarios() {
        return usuarios;
    }

    public void registrarUsuario(Socket socket) throws IOException {
        SocketUsuario socketUsuario = new SocketUsuario(socket);
        if (!this.usuarios.containsKey(socketUsuario.getUsername())) {
            System.out.println("Usuario registrado: " + socketUsuario.getUsername());
            socketUsuario.getSalida().println("200");
            this.usuarios.put(socketUsuario.getUsername(), socketUsuario);
            socketUsuario.start();
        } else
            socketUsuario.getSalida().println("409");
    }

    public void escucharNuevosUsuarios() throws IOException {
        Socket socket = socketServer.accept();
        System.out.println("Usuario con IP " + socket.getInetAddress().getHostAddress() + " intenta registrarse");
        this.registrarUsuario(socket);
    }

    public String procesarSolicitud(String mensaje, String username) throws UnknownHostException {
        String[] partes = mensaje.split(" ");
        String IP = partes[0];
        int puerto = Integer.parseInt(partes[1]);
        System.out.println("Usuario " + username + " solicito iniciar un chat con " + IP + ":" + puerto + ".");
        String usernameInterlocutor = null;

        // Busca el usuario con el que quiere establecer conexion.
        for (Map.Entry<String, SocketUsuario> entry : this.usuarios.entrySet()) {
            SocketUsuario v = entry.getValue();

            // Si el IP y puerto ingresados coinciden con alguno, los vincula (setea el interlocutor).
            if (v.isEscuchando() && v.getSocket().getInetAddress().getHostAddress().equals(IP) && v.getSocket().getPort() == puerto) {
                v.setInterlocutor(username);
                usernameInterlocutor = v.getUsername();
                v.getSalida().println(username);
            }
        }
        return usernameInterlocutor;
    }

    public void cerrarChat(String username) {
        SocketUsuario usuario = this.usuarios.get(username);
        if (usuario.getInterlocutor() != null)
            usuario.getSalida().println("504");
    }

    public void enviarMensaje(String username, String mensaje) {
        SocketUsuario usuario = this.usuarios.get(username);
        usuario.getSalida().println("351");
        usuario.getSalida().println(mensaje);
    }

}
