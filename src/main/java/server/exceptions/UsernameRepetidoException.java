package server.exceptions;

public class UsernameRepetidoException extends Throwable {

    public UsernameRepetidoException() {
        super("Nombre de usuario repetido. Elija otro.");
    }
}
