package client.model;

import client.controller.ControladorChat;
import client.controller.ControladorPrincipal;

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
    private ServerSocket socketServer;
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

    public void registrarseEnServidor(String IP, int puerto) throws IOException {
        this.socket = new Socket(IP, puerto);
        iniciarESSockets();
    }

    /**
     * Se pone, concurrentemente, en modo escucha esperando una solicitud de chat.
     */
    @Override
    public void run() {
        try {
            this.activarModoEscucha();
        } catch (IOException e) {
        }
    }

    /**
     * Inicia los streams de entrada y salida del socket. Envia el username al receptor y recibe el username del usuario remoto.
     * Instancia una nueva sesion, asignandola al atributo sesionActual, con la IP, puerto y username del usuario remoto.
     * En caso de que sea el solicitante de la sesion de chat, invoca la creacion de la ventana de chat.<br>
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

    /**
     * Activa el modo escucha del usuario. Se pone a la espera de una solicitud de chat. Cuando la recibe, crea un nuevo socket con el usuario remoto.<br>
     * <b>Pre:</b> -<br>
     * <b>Post:</b> Se ha creado un nuevo socket con el usuario remoto a partir del socketServer.
     * @throws IOException: Si hay un error al crear el socket.
     */
    private void activarModoEscucha() throws IOException {
        System.out.println("Modo escucha activado.");
        this.socketServer = new ServerSocket(this.getPuerto());
        escuchando = true;
        this.socket = socketServer.accept();
        this.socketServer.close();
        escuchando = false;
        this.socketServer = null;
        this.iniciarESSockets();
    }

    /**
     * Desactiva el modo escucha del usuario. Cierra el socketServer y el socket.<br>
     * @throws IOException: Si hay un error al cerrar el socketServer.
     */
    public void desactivarModoEscucha() throws IOException {
        this.socketServer.close();
        this.socketServer = null;
        this.socket = null;
        escuchando = false;
        System.out.println("Modo escucha desactivado.");
    }

    /**
     * Solicita un chat al usuario remoto. Crea un nuevo socket con el usuario remoto, en caso de que su IP y puerto sean validos.<br>
     * <b>Pre:</b> Los parametros IP y puerto deben ser validos.<br>
     * <b>Post:</b> Se ha creado un nuevo socket con el usuario remoto.
     * @param credencialesUsuarioReceptor: La informacion del usuario remoto.
     * @throws IOException: Si hay un error al crear el socket.
     */
    public void solicitarChat(CredencialesUsuario credencialesUsuarioReceptor) throws IOException {
        // TODO: enviar solicitud al servidor (y cambiar javadoc)
        this.solicitando = true;
        this.iniciarSesionChat();
    }

    private void iniciarSesionChat() throws IOException {
        String usernameRemoto = this.entrada.readLine();
        this.sesionChatActual = new SesionChat(this.credencialesUsuario, new CredencialesUsuario(this.socket.getInetAddress().toString(), this.socket.getPort(), usernameRemoto));

        if (this.solicitando) {
            ControladorChat.getInstance().nuevaVentana();
        }
        else{
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
        this.salida.println(mensaje);
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

    /**
     * Desconecta el socket del usuario remoto. Cierra los streams de entrada y salida del socket.
     * @throws IOException: Si hay un error al cerrar los streams de entrada y salida del socket.
     */
    public void desconectar() throws IOException {
        this.addNuevaSesion(this.sesionChatActual);
        sesionChatActual = null;
        this.socket.close();
        this.socket = null;
        this.salida = null;
        this.entrada = null;
        this.entradaSocket = null;
        this.solicitando = false;
    }

}
