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
    private boolean escuchando;

    @Override
    public void run() {
        while (true) {
            try {
                if (this.interlocutor == null) {
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
        this.escuchando = false;
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
    public boolean isEscuchando() {
        return escuchando;
    }
    public String getInterlocutor() {
        return interlocutor;
    }
    public void setInterlocutor(String interlocutor) {
        this.interlocutor = interlocutor;
    }

    private void escucharSolicitudes() throws IOException {
        String mensaje = this.entrada.readLine();

        // Si el usuario se desconecto, lo elimina de la lista.
        if (mensaje.equals("503")) {
            System.out.println("Usuario " + this.username + " se desconecto.");
            Servidor.getInstance().getUsuarios().remove(this.username);

        // Si el usuario activo el modo escucha.
        } else if (mensaje.equals("300")) {
            System.out.println("Usuario " + this.username + " activo el modo escucha.");
            this.escuchando = true;

        // Si el usuario desactivo el modo escucha.
        } else if (mensaje.equals("301")) {
            System.out.println("Usuario " + this.username + " desactivo el modo escucha.");
            this.escuchando = false;

        } else {
            String usernameInterlocutor = Servidor.getInstance().procesarSolicitud(mensaje, this.username);
            if (usernameInterlocutor != null) {
                this.interlocutor = usernameInterlocutor;
                this.salida.println("200");
                System.out.println("Usuario " + username + " se conecto con " + this.interlocutor + ".");
                this.salida.println(this.interlocutor);
            } else
                this.salida.println("404");
        }
    }

    private void escucharMensajes() {

    }

}
