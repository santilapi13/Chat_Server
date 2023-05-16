package client.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicTextFieldUI;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.*;

public class VentanaRegistro extends JFrame implements IVista, KeyListener, MouseListener {

    private JTextField campoUsuario;
    private JTextField campoDireccionIP;
    private JTextField campoPuertoIP;

    public VentanaRegistro() {
        // Configurar la ventana
        setTitle("Ventana de Registro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(329, 365);
        getContentPane().setLayout(new GridLayout(1, 1)); // Utilizar GridLayout para reorganizar los componentes

        // Crear panel para agrupar los campos de texto y los labels
        JPanel panel = new JPanel(new GridLayout(7, 2));

        // Crear labels
        JLabel labelUsuario = new JLabel("   Nombre de Usuario:");
        labelUsuario.setFont(new Font("Tahoma", Font.BOLD, 11));
        JLabel labelDireccionIP = new JLabel("   Direcci\u00F3n IP de Servidor:");
        labelDireccionIP.setFont(new Font("Tahoma", Font.BOLD, 11));
        JLabel labelPuertoIP = new JLabel("   Puerto IP de Servidor:");
        labelPuertoIP.setFont(new Font("Tahoma", Font.BOLD, 11));

        // Crear campo de texto para Nombre de Usuario
        campoUsuario = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty()) {
                    g.setColor(Color.LIGHT_GRAY); // Establecer el color de texto gris claro
                    g.drawString("Ingrese nombre de usuario...", getInsets().left, (getHeight() + getFont().getSize()) / 2);
                }
            }
        };
        campoUsuario.setUI(new BasicTextFieldUI());

        // Crear campo de texto para Dirección IP de Servidor
        campoDireccionIP = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty()) {
                    g.setColor(Color.LIGHT_GRAY); // Establecer el color de texto gris claro
                    g.drawString("Ingrese dirección IP de servidor...", getInsets().left, (getHeight() + getFont().getSize()) / 2);
                }
            }
        };
        campoDireccionIP.setUI(new BasicTextFieldUI());

        // Crear campo de texto para Puerto IP de Servidor
        campoPuertoIP = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty()) {
                    g.setColor(Color.LIGHT_GRAY); // Establecer el color de texto gris claro
                    g.drawString("Ingrese puerto IP de servidor...", getInsets().left, (getHeight() + getFont().getSize()) / 2);
                }
            }
        };
        campoPuertoIP.setUI(new BasicTextFieldUI());

        // Agregar los componentes al panel
        panel.add(labelUsuario);
        panel.add(campoUsuario);
        panel.add(labelDireccionIP);
        panel.add(campoDireccionIP);
        panel.add(labelPuertoIP);
        panel.add(campoPuertoIP);

        // Agregar el panel a la ventana
        getContentPane().add(panel);

        // Hacer visible la ventana
        setVisible(true);
    }

    public static void main(String[] args) {
        // Crear una instancia de la ventana y ejecutarla
        VentanaRegistro ventana = new VentanaRegistro();
    }

    @Override
    public void minimizarVentana() {

    }

    @Override
    public void abrirVentana() {

    }

    @Override
    public void setActionListener(ActionListener actionListener) {

    }

    @Override
    public void cerrarse() {

    }

    @Override
    public String getDireccionIP() {
        return null;
    }

    @Override
    public String getPuertoIP() {
        return null;
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public void agregarMensaje(String mensaje) {

    }

    @Override
    public void agregarUsuario(String usuario) {

    }

    @Override
    public void deseleccionar() {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}