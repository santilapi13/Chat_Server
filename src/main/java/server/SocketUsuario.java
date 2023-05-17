package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class SocketUsuario extends Thread {
    private String username;
    private String interlocutor;
    private Socket socket;
    private InputStreamReader entradaSocket;
    private PrintWriter salida;
    private BufferedReader entrada;

    @Override
    public void run() {
        while (true) {
            try {
                if (this.interlocutor == null) { //TODO: && modo escucha activado
                    this.escucharSolicitudes();
                } else
                    this.escucharMensajes();
            } catch (IOException e) {}
        }
    }

    public SocketUsuario(Socket socket) throws IOException {
        this.interlocutor = null;
        this.socket = socket;
        this.entradaSocket = new InputStreamReader(socket.getInputStream());
        this.entrada = new BufferedReader(entradaSocket);
        this.salida = new PrintWriter(socket.getOutputStream(), true);
        this.username = this.entrada.readLine();
    }

    public String getUsername() {
        return username;
    }

    public Socket getSocket() {
        return socket;
    }

    public InputStreamReader getEntradaSocket() {
        return entradaSocket;
    }

    public PrintWriter getSalida() {
        return salida;
    }

    public BufferedReader getEntrada() {
        return entrada;
    }

    public String getInterlocutor() {
        return interlocutor;
    }
    public void setInterlocutor(String interlocutor) {
        this.interlocutor = interlocutor;
    }

    private void escucharSolicitudes() throws IOException {
        String mensaje = this.entrada.readLine();
        if (mensaje.equals("503")) { // Si el usuario se desconecto, lo elimina de la lista.
            System.out.println("Usuario " + this.username + " se desconecto.");
            Servidor.getInstance().getUsuarios().remove(this.username);
        } else {
            String usernameInterlocutor = Servidor.getInstance().procesarSolicitud(mensaje, this.username);
            if (usernameInterlocutor != null) {
                this.interlocutor = usernameInterlocutor;
                this.salida.println("200");
            } else
                this.salida.println("404");
        }
    }

    private void escucharMensajes() {

    }

}
