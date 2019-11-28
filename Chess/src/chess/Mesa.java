package chess;

import chess.Tablero.Espacio;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * @author Wilmar Alexander Rodriguez Bueno
 * @author Miguel Angel Chacon
 */
// Clase que genera la interfaz visual del programa 
public class Mesa {

    //Variables y constantes principales 
    private static final Dimension DIMENSION_EXTERIOR_VENTANA = new Dimension(600, 600);
    private static final Dimension DIMENSION_PANEL_TABLERO = new Dimension(400, 350);
    private static final Dimension DIMENSION_PANEL_ESPACIO = new Dimension(10, 10);
    private static final Color COLOR_CLARO = Color.decode("#66FFCC");
    private static final Color COLOR_OSCURO = Color.decode("#FFFF99");
    private static final Color COLOR_FONDO = Color.decode("#F08080");
    private static final Color COLOR_BORDE = Color.decode("#000000");
    private static final Color COLOR_SELECCION = Color.decode("#00FF00");
    private final JFrame mesaFrame;
    private final PanelTablero tableroPanel;
    private static Tablero tableroActual;
    private Espacio EspacioFuente;
    private Espacio EspacioFinal;
    
    public static final int NUM_ESPACIOS = 64;

    //Constructor de la interfaz
    private Mesa() {
        //Generacion del Frame principal
        this.mesaFrame = new JFrame("AJEDREZ NEAT V.0.0.1");
        this.mesaFrame.setSize(DIMENSION_EXTERIOR_VENTANA);
        this.mesaFrame.setLayout(new BorderLayout());
        //Generacion del menu principal
        final JMenuBar tableMenuBar = new JMenuBar();
        LlenarMenu(tableMenuBar);
        this.mesaFrame.setJMenuBar(tableMenuBar);
        //Generacion del Panel de juego
        Mesa.tableroActual = new Tablero();
        this.tableroPanel = new PanelTablero();
        this.mesaFrame.add(this.tableroPanel, BorderLayout.CENTER);
        this.mesaFrame.setVisible(true);
    }

    //Constructor public de la mesa de juego
    public static Mesa CrearMesa() {
        final Mesa NuevaMesa = new Mesa();
        return NuevaMesa;
    }

    //Metodo que permite llenar la barra de menu
    private void LlenarMenu(JMenuBar MesaMenu) {
        MesaMenu.add(CrearMenuArchivo());
    }

    //Metodo que crea el menu Archivo y sus items correspondientes
    private JMenu CrearMenuArchivo() {
        final JMenu MenuArchivo = new JMenu("Archivo");
        final JMenuItem MenuItemSalir = new JMenuItem("Salir", KeyEvent.VK_X);
        MenuItemSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                System.exit(0);
            }
        });
        MenuArchivo.add(MenuItemSalir);
        return MenuArchivo;
    }

    //Clase que genera el Panel principal del tablero de juego
    private class PanelTablero extends JPanel {

        //Lista con todos los paneles que representan los espacios del el tablero
        final List<PanelEspacio> tableroEspacios;

        //Constructor del Panel 
        PanelTablero() {
            //Seleccion del Layout
            super(new GridLayout(Tablero.TAMAÑO_NUM_ESPACIO_POR_FILA, Tablero.TAMAÑO_NUM_ESPACIO_POR_FILA));
            //Se crean todos los sub-paneles del tablero
            this.tableroEspacios = new ArrayList<>();
            for (int i = 0; i < NUM_ESPACIOS; i++) {
                final PanelEspacio tilePanel = new PanelEspacio(this, i);
                this.tableroEspacios.add(tilePanel);
                add(tilePanel);
            }
            //Se seleccionan tamaño y colores del panel
            setPreferredSize(DIMENSION_PANEL_TABLERO);
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            setBackground(COLOR_FONDO);
            validate();
        }

        //Metodo que permite dibujar o actualizar el tablero visualmente
        void DibujarPanelTablero(final Tablero TableroActual) {
            removeAll();
            for (final PanelEspacio EspacioActual : tableroEspacios) {
                EspacioActual.DibujarEspacio(TableroActual);
                add(EspacioActual);
            }
            validate();
            repaint();
        }

        //Metodo que elimina visualmente la selecion de un espacio en el tablero
        void EliminarSeleccion() {
            for (final PanelEspacio EspacioActual : tableroEspacios) {
                EspacioActual.ReiniciarSeleccion(COLOR_BORDE);
            }
        }
    }

    //Clase que genera el Panel que representa los espacios que ocupan las fichas en el tablero
    private class PanelEspacio extends JPanel {

        //Numero que representa la posicion del espacio en el tablero
        private final int posicion;

        //Constructor del Panel
        PanelEspacio(final PanelTablero boardPanel, final int valorPosicion) {
            //Seleccion del Layout y inicializacion del panel
            super(new GridBagLayout());
            this.posicion = valorPosicion;
            //Se le asigna el tamaño, color e imagen del respectivo espacio
            setPreferredSize(DIMENSION_PANEL_ESPACIO);
            AsignarColorEspacio();
            AsignarImagenEspacio(tableroActual);
            setBorder(BorderFactory.createLineBorder(COLOR_BORDE));
            //Add de un metodo que permite interactuar con el espacio al darle click
            addMouseListener(new MouseListener() {
                @Override
                //Metodo del click, primer clic izquierdo seleciona la pieza 
                //el segundo clic izquierdo selecciona el destino
                //el clic derecho elimina cualquier seleccion realizada
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        EspacioFuente = null;
                        EspacioFinal = null;
                        boardPanel.EliminarSeleccion();
                    } else if (SwingUtilities.isLeftMouseButton(e)) {
                        if (EspacioFuente == null) {
                            EspacioFuente = tableroActual.getEspacio(valorPosicion);
                            SelecionarEspacio(COLOR_SELECCION);
                        } else {
                            EspacioFinal = tableroActual.getEspacio(valorPosicion);
                            tableroActual.Mover(EspacioFuente.X, EspacioFuente.Y, EspacioFinal.X, EspacioFinal.Y);
                            EspacioFuente = null;
                            EspacioFinal = null;
                            boardPanel.EliminarSeleccion();
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                boardPanel.DibujarPanelTablero(tableroActual);
                            }
                        });
                    }
                }

                //Otros metedos no utilizados pero nesesarios para generar el MouseListener
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
            });
            validate();
        }

        //Metodo que me permite reiniciar visualmente la seleccion de un espacio/pieza en el tablero
        private void ReiniciarSeleccion(final Color c) {
            setBorder(BorderFactory.createLineBorder(c));
        }

        //Metodo que me permite generar visualmente la seleccion de un espacio/pieza en el tablero
        private void SelecionarEspacio(final Color c) {
            setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, c));
        }

        //Metodo que permite dibujar o actualizar el espacio visualmente
        void DibujarEspacio(final Tablero tableroActual) {
            AsignarColorEspacio();
            AsignarImagenEspacio(tableroActual);
            validate();
            repaint();
        }

        //Metodo que le asigna a un espacio una imagen correspondiente a la ficha que la ocupa
        private void AsignarImagenEspacio(final Tablero TableroActual) {
            this.removeAll();
            if (TableroActual.getPieza(this.posicion) != null) {
                String pieceIconPath = "art/pieces/";
                try {
                    final BufferedImage image = ImageIO.read(new File(pieceIconPath + TableroActual.getPieza(this.posicion).getReino().toString()
                            + TableroActual.getPieza(this.posicion).getTipo_Pieza().toString()
                            + ".png"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //Metodo que le asigna los colores a los espacios (permite que luzca como un ajedrez)
        private void AsignarColorEspacio() {
            if ((this.posicion / 8) % 2 == 0) {
                setBackground(this.posicion % 2 == 0 ? COLOR_CLARO : COLOR_OSCURO);
            } else {
                setBackground(this.posicion % 2 != 0 ? COLOR_CLARO : COLOR_OSCURO);
            }
        }
    }
}
