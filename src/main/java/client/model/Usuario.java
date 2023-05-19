package client.model;

import client.controller.ControladorChat;
import client.controller.ControladorPrincipal;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * @author : Grupo 4 - Avalos, Lapiana y Sosa
 */

public class Usuario implements Runnable, GestorSesiones, EnvioMensajes, GestorConexion {
    private CredencialesUsuario credencialesUsuario;
    private Socket socket;
    private InputStreamReader entradaSocket;
    private PrintWriter salida;
    private BufferedReader entrada;
    private boolean escuchando;
    private ArrayList<SesionChat> sesionesAnteriores;
    private SesionChat sesionChatActual;
    private boolean solicitando = false;

     //PATRON SINGLETON
    private static Usuario instance;
    private Usuario() throws UnknownHostException {
        this.credencialesUsuario = new CredencialesUsuario(InetAddress.getLocalHost().getHostAddress(), 1234, "");
        this.escuchando = false;
        this.sesionesAnteriores = new ArrayList<>();
        this.sesionChatActual = null;
    }
    public static Usuario getInstance() throws UnknownHostException {
        if (instance == null)
            instance = new Usuario();
        return instance;
    }

     //GETTERS Y SETTERS
    public CredencialesUsuario getInformacion() {
        return credencialesUsuario;
    }
    public String getIP() {
        return this.credencialesUsuario.getIP();
    }
    public int getPuerto() {
        return this.credencialesUsuario.getPuerto();
    }
    public String getUsername() {
        return this.credencialesUsuario.getUsername();
    }
    public void setPuerto(int puerto) {
        this.credencialesUsuario.setPuerto(puerto);
    }
    public void setUsername(String username) {
        this.credencialesUsuario.setUsername(username);
    }
    public Socket getSocket() {
        return this.socket;
    }
    public SesionChat getSesionActual() {
        return sesionChatActual;
    }
    @Override
    public void setSesionActual(SesionChat sesionChatActual) {
        this.sesionChatActual = sesionChatActual;
    }
    @Override
    public void addNuevaSesion(SesionChat sesionChat) {
        this.sesionesAnteriores.add(sesionChat);
    }

    public boolean isEscuchando() {
        return escuchando;
    }

    public BufferedReader getEntrada() {
        return entrada;
    }

    public PrintWriter getSalida() {
        return salida;
    }

    public void registrarseEnServidor(String IP, int puerto, String usuario) throws IOException {
        this.socket = new Socket(IP, puerto, null, this.credencialesUsuario.getPuerto());
            //TODO: LEER! tal vez en vez de guardar null deberia guardar la del dispositvo, pq localAddr sera siempre 127 meparece.
        this.credencialesUsuario.setUsername(usuario);
        iniciarESSockets();
    }

    /**
     * Inicia los streams de entrada y salida del socket. Envia el username al receptor y recibe el username del usuario remoto.
     * <b>Pre:</b> El socket debe estar conectado al usuario remoto.<br>
     * <b>Post:</b> Se ha iniciado los streams de entrada y salida del socket. Se ha instanciado una nueva sesion con la IP, puerto y username del usuario remoto.
     *
     * @throws IOException: Si hay un error al iniciar los streams de entrada y salida del socket.
     */
    private void iniciarESSockets() throws IOException {
        this.entradaSocket = new InputStreamReader(socket.getInputStream());
        this.entrada = new BufferedReader(entradaSocket);
        this.salida = new PrintWriter(socket.getOutputStream(), true);
        this.salida.println(this.credencialesUsuario.getUsername());
    }

     @Override
     public void run() {
        try {
            this.activarModoEscucha();
        } catch (IOException e) {}
     }

    private void activarModoEscucha() throws IOException {
        this.salida.println("300");
        escuchando = true;
        System.out.println("Modo escucha activado.");
        this.iniciarSesionChat();
    }

    public void desactivarModoEscucha() throws IOException {
        this.salida.println("301");
        escuchando = false;
        System.out.println("Modo escucha desactivado.");
    }

    public void solicitarChat(CredencialesUsuario credencialesUsuarioReceptor) throws IOException {
        String IP = credencialesUsuarioReceptor.getIP();

        if (IP.equals("localhost")){
            InetAddress localAddress = socket.getLocalAddress();
            IP = localAddress.getHostAddress();
        }

        this.salida.println(IP + " " + credencialesUsuarioReceptor.getPuerto());
        this.solicitando = true;
    }

    public void iniciarSesionChat() throws IOException {
        String usernameRemoto = this.entrada.readLine();
        System.out.println("Usuario remoto: " + usernameRemoto);
        if (this.escuchando || this.solicitando) {
            this.sesionChatActual = new SesionChat(this.credencialesUsuario, new CredencialesUsuario(this.socket.getInetAddress().toString(), this.socket.getPort(), usernameRemoto));
            if (this.solicitando)
                ControladorChat.getInstance().nuevaVentana();
            else
                ControladorPrincipal.getInstance().agregarUsuario(usernameRemoto);
        }
    }

    /**
     * Envia un mensaje al usuario remoto.<br>
     * @param mensaje: El mensaje a enviar.
     * @throws IOException: Si hay un error al enviar el mensaje.
     */
    public void enviarMensaje(String mensaje) throws IOException {
        this.sesionChatActual.addMensaje(mensaje, true);
        this.salida.println("msg: " + mensaje);
    }

    /**
     * Recibe un mensaje del usuario remoto.<br>
     * @return El mensaje recibido.
     * @throws IOException: Si hay un error al leer el mensaje.
     */
    public String recibirMensaje() throws IOException {
        String mensaje = this.entrada.readLine();
        this.sesionChatActual.addMensaje(mensaje, false);
        return mensaje;
    }

    public void desconectar() throws IOException {
        this.addNuevaSesion(this.sesionChatActual);
        sesionChatActual = null;
        this.escuchando = false;
        this.solicitando = false;
    }

}
