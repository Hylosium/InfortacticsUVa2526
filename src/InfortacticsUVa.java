import java.io.File;
import java.util.Scanner;
// Importar librerías necesarias para ficheros (File, PrintWriter, etc.)
import java.io.PrintWriter;

public class InfortacticsUVa {

    public static void main(String[] args) {
        // 1. PREPARACIÓN DE DATOS
        // Inicializar Scanner.
        Scanner in = new Scanner(System.in);
        // Cantidad de elixir.
        int elixirRestante = Assets.INITIAL_ELIXIR;
        // Definir vector de Strings para baraja del Jugador (inicializar a "")
        String[] playerDeck = new String[Assets.INITIAL_ELIXIR];
        // Definir vector de Strings para baraja del Enemigo (inicializar a "")
        String[] enemyDeck = new String[Assets.INITIAL_ELIXIR];
        for (int i = 0; i < playerDeck.length; i++){
            playerDeck[i]="";
            enemyDeck[i]="";
        }

        // 2. BUCLE DEL MENÚ PRINCIPAL
        // Crear bucle (while) que se repita hasta que el usuario elija "Salir"
        boolean salir=false;
        // 2.1 MOSTRAR INTERFAZ
        // Mostrar las 5 opciones del menú por pantalla (Imagen 2)
        // Pedir opción al usuario. Validar que sea correcta; si no, mensaje error y repetir.
        do{
            System.out.println("🏰  InforTactics UVa  🏰");
            System.out.println(".______________________.");
            System.out.println("| 1. Nueva Partida     |");
            System.out.println("| 2. Configurar Baraja |");
            System.out.println("| 3. Guardar Baraja    |");
            System.out.println("| 4. Cargar Baraja     |");
            System.out.println("| 5. Salir             |");
            System.out.println(".______________________.");

            System.out.print("Opción: ");
            String entrada = in.nextLine();
            // 2.2 LÓGICA DE OPCIONES
            switch (entrada) {
                // --- OPCIÓN 5: SALIR ---
                // Mostrar mensaje de despedida y romper el bucle principal.
                case "5":
                    System.out.println("Saliendo del programa ...");
                    System.out.println("Gracias por jugar");
                    salir = true;
                    break;

                // --- OPCIÓN 4: CARGAR BARAJA ---
                // Leer fichero "Barajas/BarajaGuardada.txt".
                // Rellenar vector jugador y recalcular el elixir gastado. Checkear errores de fichero.
                case "4":
                    System.out.println("Cargando la baraja ...");
                    try{
                        // Abrir el fichero
                        Scanner leerFichero = new Scanner(new File("Barajas/BarajaGuardada.txt"));
                        // Limpiar el vector
                        Methods.initializeDeck(playerDeck);
                        // Reiniciar el elixir:
                        elixirRestante = Assets.INITIAL_ELIXIR;
                        // Cargar los personajes
                        for ( int i = 0; i< playerDeck.length; i++){
                            // Comprobamos si hay alguna palabra más que leer.
                            if (leerFichero.hasNext()){
                                // Leer palabra por palabra.
                                playerDeck[i] = leerFichero.next();
                                elixirRestante -= Methods.getCharacterElixir(playerDeck[i].charAt(0));

                            }
                        }
                        // Cerramos el fichero
                        leerFichero.close();
                        System.out.println("Baraja cargada correctamente.");
                    } catch (Exception e){
                        System.out.println("Error: "+e.getMessage());
                    }
                    break;

                // --- OPCIÓN 3: GUARDAR BARAJA [Ver Pág. 3] ---
                // Escribir el contenido del vector jugador en el fichero "Barajas/BarajaGuardada.txt".
                // Formato de escritura: Strings separados por espacios (Ej: "V33 D41").
                case "3":
                    System.out.println("Guardando la baraja ...");
                    try{
                        // Abrir el fichero
                        PrintWriter escribirFichero = new PrintWriter("Barajas/BarajaGuardada.txt");
                        for ( int i = 0; i< playerDeck.length; i++){
                            if (playerDeck[i].equals("")){
                                // Esta vacio.
                            } else {
                                // Escribir el personaje y las coordenadas en el fichero:
                                escribirFichero.print(playerDeck[i]+" ");
                            }
                        }

                        // Cerramos el fichero
                        escribirFichero.close();
                    } catch (Exception e){
                        System.out.println("Error: "+e.getMessage());
                    }
                    break;


                // --- OPCIÓN 2: CONFIGURAR BARAJA ---
                case "2":
                    // Llamamos al método y actualizamos el elixir con lo que nos devuelva
                    elixirRestante = configurarBaraja(in, playerDeck, elixirRestante);
                    break;

                // --- OPCIÓN 1: NUEVA PARTIDA---
                case "1":
                    nuevaPartida(in, playerDeck, enemyDeck);
                    break;

                default:
                    System.out.println("No has seleccionado una opción correcta.");
                    System.out.println("Vuelve a intentarlo.");
                    break;
            }

        }while(!salir);

    }

    // 3. MÉTODOS OBLIGATORIOS Y AUXILIARES

    // TODO: Definir procedimiento 'printBoard' para dibujar el tablero 6x6 [Ver Pág. 5].
    //       (Debe leer el vector de Strings y pintar iconos o huecos vacíos).
    public static void printBoard(String[] deck) {
        // Vacio temporalemente para que no de error mientras compilo mi parte.
        System.out.println("********************************");
        System.out.println("(Aquí se dibujará el tablero...)");
        System.out.println("");
        for (int i =0; i < deck.length; i++){
            System.out.println("Posición "+i+":"+deck[i]);

        }
    }

    // Método del CASE 1:
    // Crear método auxiliar para cargar baraja enemiga aleatoria (Opción 1).
    // Comprobar si la baraja del jugador tiene al menos 1 personaje. Si no -> Error.
    // Si tiene personajes -> Cargar baraja enemiga aleatoria desde "Barajas/BarajasEnemigas.txt".
    public static void nuevaPartida(Scanner in, String[] playerDeck, String[] enemyDeck) {
        // 1. Limpiar el vector de enemigos previo
        Methods.initializeDeck(enemyDeck);

        // 2. Comprobar si el jugador tiene cartas (Basta con encontrar UNA no vacía)
        boolean hayPersonajes = false;
        for (String carta : playerDeck) {
            if (!carta.equals("")) {
                hayPersonajes = true;
                break;
            }
        }

        if (!hayPersonajes) {
            System.out.println("¡Error! Tienes que configurar tu baraja antes.");
            System.out.println("Ve a la opción 2 para comprar tropas.");
            return; // Salimos del método sin jugar
        }

        // 3. Si hay personajes, preparamos el enemigo aleatorio
        System.out.println("Empezando nueva partida ...");
        System.out.println("Buscando rival...");

        try {
            // Leemos todas las líneas del fichero de enemigos
            Scanner ficheroEnemigos = new Scanner(new File("Barajas/BarajasEnemigas.txt"));
            String[] listaEnemigos = new String[100]; // Buffer grande para leer líneas
            int totalLineas = 0;

            while (ficheroEnemigos.hasNextLine() && totalLineas < listaEnemigos.length) {
                listaEnemigos[totalLineas] = ficheroEnemigos.nextLine();
                totalLineas++;
            }
            ficheroEnemigos.close();

            if (totalLineas > 0) {
                // Elegimos una línea aleatoria
                int indiceAleatorio = (int) (Math.random() * totalLineas);
                String lineaEnemiga = listaEnemigos[indiceAleatorio];

                // Parseamos esa línea para rellenar el enemyDeck
                Scanner parserLinea = new Scanner(lineaEnemiga);
                int hueco = 0;
                while (parserLinea.hasNext() && hueco < enemyDeck.length) {
                    enemyDeck[hueco] = parserLinea.next();
                    hueco++;
                }
                parserLinea.close();

                // 4. Empezar el juego.
                Methods.startGame(in, playerDeck, enemyDeck);

            } else {
                System.out.println("Error: El fichero de enemigos está vacío.");
            }

        } catch (Exception e) {
            System.out.println("Error al cargar enemigo: " + e.getMessage());
        }
    }


    // Método del CASE 2:
    // Crear método auxiliar para la lógica de configuración (Opción 2) para no llenar el main.
    /**
     * Gestiona el menú de configuración de la baraja permitiendo añadir o borrar tropas.
     * El flujo de entrada está dividido en dos pasos: selección de personaje y selección de coordenadas.
     *
     * @param in            Scanner para leer la entrada del teclado.
     * @param deck          Array de Strings que representa la baraja del jugador.
     * @param currentElixir Cantidad de elixir disponible actualmente.
     * @return El elixir restante después de realizar las modificaciones.
     */
    public static int configurarBaraja(Scanner in, String[] deck, int currentElixir) {
        System.out.println("Configurando la baraja ...");
        boolean seguirEditando = true;

        do {
            // 1. Mostrar estado actual
            printBoard(deck);
            System.out.println("Elixir restante 💧: " + currentElixir);

            // 2. Primer Prompt
            System.out.print("Personaje a añadir (x para borrar; 0 para guardar): ");
            String entradaAccion = in.nextLine();

            // Si introducimos un Intro ya no entra.
            // Aquí, controlar que el char introducido no es mas de un caracter:
            if (entradaAccion.length() == 1) {

                char simbolo = entradaAccion.charAt(0);

                switch (simbolo) {
                    case '0':
                        seguirEditando = false;
                        System.out.println("Guardando configuración...");
                        System.out.println("Mazo guardado correctamente.");
                        break;

                    case 'x':
                        System.out.print("Introduce posición (p.ej. 33): ");
                        String entradaPosBorrar = in.nextLine();

                        if (entradaPosBorrar.length() == 2) {
                            int filaB = entradaPosBorrar.charAt(0) - '0';
                            int colB = entradaPosBorrar.charAt(1) - '0';

                            if (filaB >= 3 && filaB <= 5 && colB >= 0 && colB <= 5) {
                                boolean encontrado = false;
                                for (int i = 0; i < deck.length && !encontrado; i++) {
                                    if (!deck[i].equals("") &&
                                            (deck[i].charAt(1) - '0' == filaB) &&
                                            (deck[i].charAt(2) - '0' == colB)) {

                                        int coste = Methods.getCharacterElixir(deck[i].charAt(0));
                                        currentElixir += coste;

                                        if (currentElixir > Assets.INITIAL_ELIXIR) {
                                            currentElixir = Assets.INITIAL_ELIXIR;
                                        }

                                        deck[i] = "";
                                        System.out.println("Personaje borrado correctamente.");
                                        encontrado = true;
                                    }
                                }
                                if (!encontrado) {
                                    System.out.println("No hay ninguna tropa en esa posición.");
                                }
                            } else {
                                System.out.println("Posición fuera de la zona del jugador (Filas 3-5).");
                            }
                        } else {
                            System.out.println("Formato de posición incorrecto. Debe ser XY (ej: 33).");
                        }
                        break;

                    default:
                        int coste = Methods.getCharacterElixir(simbolo);

                        if (coste > 0) {
                            if (currentElixir - coste >= 0) {
                                System.out.print("Introduce posición (p.ej. 33): ");
                                String entradaPosPoner = in.nextLine();

                                if (entradaPosPoner.length() == 2) {
                                    int fila = entradaPosPoner.charAt(0) - '0';
                                    int col = entradaPosPoner.charAt(1) - '0';

                                    if (fila >= 3 && fila <= 5 && col >= 0 && col <= 5) {

                                        // -------------------------------------------------
                                        // DETECTOR DE OCUPACIÓN (Versión Clásica)
                                        // -------------------------------------------------
                                        boolean ocupado = false;
                                        for (int i = 0; i < deck.length && !ocupado; i++) {
                                            // Usamos deck[i] en vez de la variable 'carta'
                                            if (!deck[i].equals("") &&
                                                    (deck[i].charAt(1) - '0' == fila) &&
                                                    (deck[i].charAt(2) - '0' == col)) {
                                                ocupado = true; // Activamos bandera para salir
                                            }
                                        }
                                        // -------------------------------------------------

                                        if (!ocupado) {
                                            String cartaFinal = simbolo + entradaPosPoner;
                                            boolean guardado = false;
                                            // Bucle clásico para guardar
                                            for (int i = 0; i < deck.length && !guardado; i++) {
                                                if (deck[i].equals("")) {
                                                    deck[i] = cartaFinal;
                                                    currentElixir -= coste;
                                                    guardado = true;
                                                    System.out.println("Tropa añadida con éxito.");
                                                }
                                            }
                                        } else {
                                            System.out.println("¡Esa casilla ya está ocupada!");
                                        }
                                    } else {
                                        System.out.println("Posición no válida (Filas 3-5).");
                                    }
                                } else {
                                    System.out.println("Formato de posición incorrecto (ej: 33).");
                                }
                            } else {
                                System.out.println("¡No tienes suficiente elixir!");
                            }
                        }
                        break;
                } // Fin del Switch
            } else {
                System.out.println("No has introducido un SÍMBOLO correcto. pe (V, K, x, 0 ...) ");
            }

        } while (seguirEditando);

        return currentElixir;
    }
}