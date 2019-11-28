package chess;

/**
 * @author Wilmar Alexander Rodriguez Bueno
 * @author Miguel Angel Chacon
 */
// Clase que genera la informacion de una ficha del ajedrez
public class Pieza {

    //Variables principales 

    final Tipo_Pieza tipo;
    private final Reino reino;
    private boolean PrimerMover;

    public static final int[][] TORRE = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
    public static final int[][] REINA = {{1, 1}, {1, -1}, {-1, -1}, {-1, 1}, {0, 1}, {0, -1}, {1, 0}, {-1, 0}};
    public static final int[][] ALFIL = {{1, 1}, {1, -1}, {-1, -1}, {-1, 1}};
    public static final int[][] REY = {{1, 1}, {1, -1}, {-1, -1}, {-1, 1}, {0, 1}, {0, -1}, {1, 0}, {-1, 0}};
    public static final int[][] CABALLERO = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {-1, 2}, {-1, -2}, {1, -2}};
    public static final int[][] SOLDADO = {{1, 0}};

    //Constructor de la pieza
    public Pieza(final Tipo_Pieza t, final Reino r) {
        this.tipo = t;
        this.reino = r;
        PrimerMover = true;
    }

    //Metodo que retorna si la ficha es blanca

    public boolean EsFichaBlanca() {
        return "B".equals(this.reino.toString());
    }

    //Metodo que retorna si la pieza ha sido movida al menos una vez

    public boolean getPrimerMove() {
        return PrimerMover;
    }

    //Metodo que informa a la pieza que esta ya se ha movido al menos una vez

    public void realizarPrimerMove() {
        PrimerMover = false;
    }

    //Metodo que retorna el nombre de la pieza (primer letra del reino mas primer letra del tipo de pieza)

    public String getName() {
        return (this.reino.toString() + this.tipo.toString());
    }

    //Metodo que retorna el reino de la pieza

    public Reino getReino() {
        return this.reino;
    }

    //Metodo que retorna el tipo de la pieza

    public Tipo_Pieza getTipo_Pieza() {
        return this.tipo;
    }

    //Enum que representa los posibles reinos de la pieza

    public enum Reino {

        //Tipos de reino

        Blanco("B"),
        Negro("N");
        //Variable del reino
        private final String NombreReino;

        //Constructor del Reino

        Reino(final String r) {
            this.NombreReino = r;
        }

        //Metodo que retorna el reino de la pieza como un string de una letra

        @Override
        public String toString() {
            return this.NombreReino;
        }
    }

    //Enum que representa los posibles tipos de la pieza

    public enum Tipo_Pieza {

        //Tipos de pieza

        Soldado("P", SOLDADO),
        Caballero("C", CABALLERO),
        Alfil("A", ALFIL),
        Torre("T", TORRE),
        Reina("Q", REINA),
        Rey("R", REY);
        //Variables del tipo de pieza
        private final String NombrePieza;
        private final int[][] moveLegal;
        //Constructor del Tipo Pieza

        Tipo_Pieza(final String n, final int[][] m) {
            this.NombrePieza = n;
            this.moveLegal = m;
        }

        //Metodo que retorna los vectores unitarios de los movimientos legales del tipo de pieza especificado

        public int[][] getMoveLegal() {
            return moveLegal;
        }

        //Metodo que retorna el tipo de la pieza como un string de una letra

        @Override
        public String toString() {
            return this.NombrePieza;
        }
    }
}
