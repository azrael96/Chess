package chess;

import chess.Pieza.Reino;
import chess.Pieza.Tipo_Pieza;

/**
 * @author Wilmar Alexander Rodriguez Bueno
 * @author Miguel Angel Chacon
 */
// Clase que genera la informacion de un tablero de ajedrez
public class Tablero {

    //Variables y constantes principales

    public static final int TAMAÑO_NUM_ESPACIO_POR_FILA = 8;
    private static final Espacio[][] TableroActual = TableroInicial();
    public boolean TurnoBlanco = true;

    //Metodo que genera el constructor de la clase

    public Tablero() {
    }

    //Metodo que genera la forma inicial del tablero de juego

    public static final Espacio[][] TableroInicial() {
        //Genera un tablero vacio
        Espacio[][] Constructor = new Espacio[TAMAÑO_NUM_ESPACIO_POR_FILA][TAMAÑO_NUM_ESPACIO_POR_FILA];
        for (int i = 0; i < TAMAÑO_NUM_ESPACIO_POR_FILA; i++) {
            for (int j = 0; j < TAMAÑO_NUM_ESPACIO_POR_FILA; j++) {
                Constructor[i][j] = new Espacio(i, j);
            }
        }
        //se retorna un tablero con la configuracion estandar
        return TableroEstandar(Constructor);
    }

    //Metodo que permite organizar un tablero en su forma estandar inicial 

    public static Espacio[][] TableroEstandar(Espacio[][] Constructor) {
        //generando y ubicando piezas negras
        Constructor[0][0].setPieza(new Pieza(Tipo_Pieza.Torre, Reino.Negro));
        Constructor[0][7].setPieza(new Pieza(Tipo_Pieza.Torre, Reino.Negro));
        Constructor[0][1].setPieza(new Pieza(Tipo_Pieza.Caballero, Reino.Negro));
        Constructor[0][6].setPieza(new Pieza(Tipo_Pieza.Caballero, Reino.Negro));
        Constructor[0][2].setPieza(new Pieza(Tipo_Pieza.Alfil, Reino.Negro));
        Constructor[0][5].setPieza(new Pieza(Tipo_Pieza.Alfil, Reino.Negro));
        Constructor[0][3].setPieza(new Pieza(Tipo_Pieza.Reina, Reino.Negro));
        Constructor[0][4].setPieza(new Pieza(Tipo_Pieza.Rey, Reino.Negro));
        //generando y ubicando piezas blancas
        Constructor[7][0].setPieza(new Pieza(Tipo_Pieza.Torre, Reino.Blanco));
        Constructor[7][7].setPieza(new Pieza(Tipo_Pieza.Torre, Reino.Blanco));
        Constructor[7][1].setPieza(new Pieza(Tipo_Pieza.Caballero, Reino.Blanco));
        Constructor[7][6].setPieza(new Pieza(Tipo_Pieza.Caballero, Reino.Blanco));
        Constructor[7][2].setPieza(new Pieza(Tipo_Pieza.Alfil, Reino.Blanco));
        Constructor[7][5].setPieza(new Pieza(Tipo_Pieza.Alfil, Reino.Blanco));
        Constructor[7][3].setPieza(new Pieza(Tipo_Pieza.Reina, Reino.Blanco));
        Constructor[7][4].setPieza(new Pieza(Tipo_Pieza.Rey, Reino.Blanco));
        //generando y ubicando los peones blancos y negros
        int c = 0;
        while (c < TAMAÑO_NUM_ESPACIO_POR_FILA) {
            Constructor[1][c].setPieza(new Pieza(Tipo_Pieza.Soldado, Reino.Negro));
            Constructor[6][c].setPieza(new Pieza(Tipo_Pieza.Soldado, Reino.Blanco));
            c++;
        }
        //retorna la configuracion estandar
        return Constructor;
    }

    //Metodo que permite obtener el espacio(espacio) en una posicion especifica del tablero

    public Espacio getEspacio(final int Posicion) {
        int x = Posicion / TAMAÑO_NUM_ESPACIO_POR_FILA;
        int y = Posicion % TAMAÑO_NUM_ESPACIO_POR_FILA;
        return TableroActual[x][y];
    }

    //Metodo que permite obtener la pieza en una posicion especifica del tablero

    public static Pieza getPieza(int Posicion) {
        int X = Posicion / 8;
        int Y = Posicion % 8;
        return TableroActual[X][Y].Pieza;
    }

    //Metodo que le permite al tablero mover una pieza de un espacio a otro siguiendo las reglas del ajedrez

    public void Mover(final int ix, final int iy, final int fx, final int fy) {
        int TamañoMover;
        if (!(ix == fx && iy == fy)) {
            if (TableroActual[ix][iy].Pieza != null) {
                if ((TableroActual[ix][iy].Pieza.EsFichaBlanca() && TurnoBlanco)
                        || (!TableroActual[ix][iy].Pieza.EsFichaBlanca() && !TurnoBlanco)) {
                    switch (TableroActual[ix][iy].Pieza.tipo.toString()) {
                        case "T":
                        case "A":
                        case "Q":
                            TamañoMover = TAMAÑO_NUM_ESPACIO_POR_FILA;
                            MoverEnRayo(ix, iy, fx, fy, TamañoMover);
                            break;
                        case "R":
                        case "C":
                            TamañoMover = 2;
                            MoverEnRayo(ix, iy, fx, fy, TamañoMover);
                            break;
                        case "P":
                            MoverPeon(ix, iy, fx, fy);
                            break;
                        default:
                            System.out.println("No es una ficha posible");
                            break;
                    }
                } else {
                    System.out.println("No es su turno");
                }
            } else {
                System.out.println("No seleciono una espacio con una pieza");
            }
        } else {
            System.out.println("La posicion incicial y final son iguales");
        }
    }

    //Metodo que permite generar los movimientos normales y especiales de un peon

    public void MoverPeon(final int ix, final int iy, final int fx, final int fy) {
        final Pieza PiezaInicio = TableroActual[ix][iy].Pieza;
        boolean MoverRealizado = false;
        String msj = "";
        int tx, ty, dir;
        //Direccion del peon
        if ("B".equals(PiezaInicio.getReino().toString())) {
            dir = -1;
        } else {
            dir = 1;
        }
        //Movimiento especial inicial
        if (TableroActual[ix][iy].Pieza.getPrimerMove()) {
            tx = ix + (2 * dir);
            ty = iy;
            if (tx == fx && ty == fy) {
                if (TableroActual[tx][ty].EstaVacio()) {
                    msj = "REALIZADO: Movimiento Especial Peon ";
                    TableroActual[fx][fy].Pieza = PiezaInicio;
                    TableroActual[ix][iy].Pieza = null;
                    MoverRealizado = true;
                }
            }
        }
        //Movimiento simple y de ataque del peon
        if (!MoverRealizado) {
            tx = ix + (1 * dir);
            ty = iy;
            if ((tx < TAMAÑO_NUM_ESPACIO_POR_FILA && tx >= 0 && ty < TAMAÑO_NUM_ESPACIO_POR_FILA && ty >= 0)) {
                if (tx == fx && ty == fy) {
                    if (TableroActual[tx][ty].EstaVacio()) {
                        msj = "REALIZADO: Movimiento Simple";
                        TableroActual[fx][fy].Pieza = PiezaInicio;
                        TableroActual[ix][iy].Pieza = null;
                        MoverRealizado = true;
                    } else {
                        msj = "NO PERMITIDO: Camino Bloqueado";
                    }
                } else {
                    if ((ty + 1) == fy && (ty + 1) < TAMAÑO_NUM_ESPACIO_POR_FILA && (ty + 1) >= 0) {
                        ty = (ty + 1);
                    } else if ((ty - 1) == fy && (ty - 1) < TAMAÑO_NUM_ESPACIO_POR_FILA && (ty - 1) >= 0) {
                        ty = (ty - 1);
                    }
                    if (tx == fx && ty == fy) {
                        if (!TableroActual[tx][ty].EstaVacio()) {
                            if (!(TableroActual[ix][iy].Pieza.getReino() == TableroActual[fx][fy].Pieza.getReino())) {
                                msj = "REALIZADO: Movimiento Ataque";
                                TableroActual[fx][fy].Pieza = PiezaInicio;
                                TableroActual[ix][iy].Pieza = null;
                                MoverRealizado = true;
                            } else {
                                msj = "NO PERMITIDO: Movimiento Ataque a un Aliado";
                            }
                        } else {
                            msj = "NO PERMITIDO: Movimiento Ataque a un Espacio vacio";
                        }
                    } else {
                        msj = "NO PERMITIDO: Movimiento simple no permitido";
                    }
                }
            }
        }
        //Movimiento que permite que un peon corone
        if (MoverRealizado) {
            TableroActual[fx][fy].Pieza.realizarPrimerMove();
            CambiarTurno();
            if (fx == 0 || fx == (TAMAÑO_NUM_ESPACIO_POR_FILA - 1)) {
                TableroActual[fx][fy].Pieza = new Pieza(Pieza.Tipo_Pieza.Reina, PiezaInicio.getReino());
                msj = "PEON CORONADO";
            }
        }
        //mensaje que permite saber que tipo de movimiento se realizo o porque el movimiento no se realizo
        System.out.println(msj);
    }

    //Metodo que permite genera los movimientos de una pieza suando sus vectores unitarios

    public void MoverEnRayo(final int ix, final int iy, final int fx, final int fy, final int l) {
        final Pieza PiezaInicio = TableroActual[ix][iy].Pieza;
        final int[][] MovimientosLegales = PiezaInicio.tipo.getMoveLegal();
        boolean MoverRealizado = false;
        String msj = "";
        int tx, ty;
        //analiza los movimientos legales del tipo de pieza y genera el movimiento si este es legal
        for (int[] MovimientoActual : MovimientosLegales) {
            if (!MoverRealizado) {
                for (int i = 1; i < l; i++) {
                    tx = ix + (MovimientoActual[0] * i);
                    ty = iy + (MovimientoActual[1] * i);
                    if ((tx < 8 && tx >= 0 && ty < TAMAÑO_NUM_ESPACIO_POR_FILA && ty >= 0)) {
                        if (TableroActual[tx][ty].EstaVacio()) {
                            if (tx == fx && ty == fy) {
                                msj = "REALIZADO: Movimiento Simple";
                                TableroActual[fx][fy].Pieza = PiezaInicio;
                                TableroActual[ix][iy].Pieza = null;
                                MoverRealizado = true;
                                break;
                            }
                        } else {
                            if (tx == fx && ty == fy) {
                                if (TableroActual[tx][ty].Pieza.getReino() == TableroActual[ix][iy].Pieza.getReino()) {
                                    msj = "NO PERMITIDO: Movimiento Ataque a un Aliado";
                                    break;
                                } else {
                                    msj = "REALIZADO: Movimiento Ataque";
                                    TableroActual[fx][fy].Pieza = PiezaInicio;
                                    TableroActual[ix][iy].Pieza = null;
                                    MoverRealizado = true;
                                    break;
                                }
                            } else {
                                msj = "NO PERMITIDO: Camino Bloqueado o Movimiento simple no permitido";
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        if (MoverRealizado) {
            TableroActual[fx][fy].Pieza.realizarPrimerMove();
            CambiarTurno();
        }

        //mensaje que permite saber que tipo de movimiento se realizo o porque el movimiento no se realizo
        System.out.println(msj);
    }

    //Metodo que cambia el turno del jugador actual por el siguiente (negras a blancas, blancas a negras)

    public void CambiarTurno() {
        if (TurnoBlanco == true) {
            TurnoBlanco = false;
        } else {
            TurnoBlanco = true;
        }
    }

    //Clase que almacena la informacion de cada espacio del tablero

    public static class Espacio {

        //variables y constatantes principales

        final int X, Y;
        Pieza Pieza;

        //Constructor de la clase

        private Espacio(final int x, final int y) {
            this.X = x;
            this.Y = y;
            this.Pieza = null;
        }

        //Metodo que permite ubicar una pieza en en espacio

        public void setPieza(final Pieza p) {
            this.Pieza = p;
        }

        //Metodo que retorna si el espacio contiene una pieza

        public boolean EstaVacio() {
            boolean Estado = false;
            if (this.Pieza == null) {
                Estado = true;
            }
            return Estado;
        }
    }
}
