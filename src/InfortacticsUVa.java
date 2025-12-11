import java.io.File;
import java.io.IOException;
import java.io.PrintWriter; // Usamos PrintWriter
import java.util.Scanner;

/**
 * Clase principal del juego InforTactics UVa.
 * Gestiona el menú principal, la persistencia de datos y el flujo general de la
 * aplicación.
 */
public class InfortacticsUVa {

    /**
     * Método principal de la aplicación.
     */
    public static void main(String[] args) {
        // 1. PREPARACIÓN DE DATOS
        // Inicializar Scanner.
        Scanner in = new Scanner(System.in);
        // Cantidad de elixir.
        int elixirRestante = Assets.INITIAL_ELIXIR;
        // Se definen los strings con la cantidad de "INITIAL_ELIXIR" por escalabilidad
        // en el futuro.
        // Si cambias la constante en Assets.java se replica aquí también.

        // Definir vector de Strings para baraja del Jugador (inicializar a
        // INITIAL_ELIXIR)
        String[] playerDeck = new String[Assets.INITIAL_ELIXIR];
        // Definir vector de Strings para baraja del Enemigo (inicializar a
        // INITIAL_ELIXIR)
        String[] enemyDeck = new String[Assets.INITIAL_ELIXIR];

        for (int i = 0; i < playerDeck.length; i++) {
            playerDeck[i] = "";
            enemyDeck[i] = "";
        }

        // 2. BUCLE DEL MENÚ PRINCIPAL
        // Crear bucle (while) que se repita hasta que el usuario elija "Salir"
        boolean salir = false;

        do {
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
                case "5":
                    System.out.println("Saliendo del programa ...");
                    System.out.println("Gracias por jugar <3<3<3<3");
                    salir = true;
                    break;

                // --- OPCIÓN 4: CARGAR BARAJA ---
                case "4":
                    // Llamamos a la función y actualizamos el elixir
                    elixirRestante = cargarBaraja(playerDeck, elixirRestante);
                    break;

                // --- OPCIÓN 3: GUARDAR BARAJA ---
                case "3":
                    guardarBaraja(playerDeck);
                    break;

                // --- OPCIÓN 2: CONFIGURAR BARAJA ---
                case "2":
                    elixirRestante = configurarBaraja(in, playerDeck, elixirRestante);
                    break;

                // --- OPCIÓN 1: NUEVA PARTIDA ---
                case "1":
                    nuevaPartida(in, playerDeck, enemyDeck);
                    break;

                default:
                    System.out.println("No has seleccionado una opción correcta.");
                    System.out.println("Vuelve a intentarlo.");
                    break;
            }

            // Methods.flushScreen();

        } while (!salir);
    }

    // 3. MÉTODOS OBLIGATORIOS Y AUXILIARES

    /**
     * Dibuja por pantalla el estado actual del tablero.
     * 
     * @param deck Vector de Strings que contiene las cartas a mostrar.
     */
    public static void printBoard(String[] deck) {
        System.out.println("TABLERO");
        System.out.println("    0   1   2   3   4   5");
        System.out.println("  -------------------------");

        for (int i = 0; i < Assets.BOARD_ROWS; i++) {

            // Fila de contenido
            System.out.print(i + " |");

            for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
                String contenidoCelda = " ";

                // Zona enemiga (Sombreado)
                if (i < 3) {
                    // Concatenamos comillas vacías para convertir char a String
                    contenidoCelda = Assets.NO_POSITION + "";
                }

                // Buscar tropas en deck
                for (int k = 0; k < deck.length; k++) {
                    if (!deck[k].equals("")) {
                        int fila = deck[k].charAt(1) - '0';
                        int col = deck[k].charAt(2) - '0';

                        if (fila == i && col == j) {
                            char letra = deck[k].charAt(0);

                            // Traductor de Letra a Emoji
                            switch (letra) {
                                case Assets.ARCHER_SYMBOL:
                                    contenidoCelda = Assets.ARCHER_IMAGE;
                                    break;
                                case Assets.DRAGON_SYMBOL:
                                    contenidoCelda = Assets.DRAGON_IMAGE;
                                    break;
                                case Assets.PRINCESS_SYMBOL:
                                    contenidoCelda = Assets.PRINCESS_IMAGE;
                                    break;
                                case Assets.VALKYRIE_SYMBOL:
                                    contenidoCelda = Assets.VALKYRIE_IMAGE;
                                    break;
                                case Assets.GOBLIN_SYMBOL:
                                    contenidoCelda = Assets.GOBLIN_IMAGE;
                                    break;
                                case Assets.PK_SYMBOL:
                                    contenidoCelda = Assets.PK_IMAGE;
                                    break;
                                default:
                                    contenidoCelda = letra + "";
                            }
                        }
                    }
                }

                // PRINTF con Emojis
                if (contenidoCelda.length() > 1) {
                    // Formato compacto para emojis
                    System.out.printf(" %s|", contenidoCelda);
                } else {
                    // Formato normal para letras
                    System.out.printf(" %s |", contenidoCelda);
                }
            }

            System.out.println();
            System.out.println("  -------------------------");
        }
    }

    /**
     * Muestra la tabla de estadísticas de los personajes debajo del tablero.
     */
    public static void printStats() {
        System.out.println();
        // Cabecera alineada
        System.out.printf("%-13s %-7s %-7s %-9s %-9s%n", "Personaje", "Símb.", "Elixir", "%Ataque", "%Defensa");
        System.out.println("------------------------------------------------");

        // Filas de datos usando las constantes de Assets
        printStatRow(Assets.ARCHER_IMAGE + " " + Assets.ARCHER_NAME, Assets.ARCHER_SYMBOL, Assets.ARCHER_ELIXIR,
                Assets.ARCHER_ATTACK, Assets.ARCHER_DEFENSE);
        printStatRow(Assets.DRAGON_IMAGE + " " + Assets.DRAGON_NAME, Assets.DRAGON_SYMBOL, Assets.DRAGON_ELIXIR,
                Assets.DRAGON_ATTACK, Assets.DRAGON_DEFENSE);
        printStatRow(Assets.PRINCESS_IMAGE + " " + Assets.PRINCESS_NAME, Assets.PRINCESS_SYMBOL, Assets.PRINCESS_ELIXIR,
                Assets.PRINCESS_ATTACK, Assets.PRINCESS_DEFENSE);
        printStatRow(Assets.VALKYRIE_IMAGE + " " + Assets.VALKYRIE_NAME, Assets.VALKYRIE_SYMBOL, Assets.VALKYRIE_ELIXIR,
                Assets.VALKYRIE_ATTACK, Assets.VALKYRIE_DEFENSE);
        printStatRow(Assets.GOBLIN_IMAGE + " " + Assets.GOBLIN_NAME, Assets.GOBLIN_SYMBOL, Assets.GOBLIN_ELIXIR,
                Assets.GOBLIN_ATTACK, Assets.GOBLIN_DEFENSE);
        printStatRow(Assets.PK_IMAGE + " " + Assets.PK_NAME, Assets.PK_SYMBOL, Assets.PK_ELIXIR, Assets.PK_ATTACK,
                Assets.PK_DEFENSE);

        System.out.println("------------------------------------------------");
    }

    /**
     * Método auxiliar para imprimir una fila de estadísticas con formato.
     */
    public static void printStatRow(String name, char symbol, int elixir, int attack, int defense) {
        System.out.printf("%-13s %-7c %-7d %-9d %-9d%n", name, symbol, elixir, attack, defense);
    }

    // --- MÉTODOS DE GESTIÓN DE BARAJAS (CARGAR/GUARDAR) ---

    // Método del CASE 4:
    /**
     * Carga una baraja desde un fichero y actualiza el elixir restante.
     * 
     * @param playerDeck    Vector donde se cargarán los datos.
     * @param currentElixir Elixir actual (se reinicia al cargar).
     * @return El nuevo valor del elixir restante tras cargar las cartas.
     */
    public static int cargarBaraja(String[] playerDeck, int currentElixir) {
        System.out.println("Cargando la baraja ...");
        File carpetaBarajas = new File("Barajas");
        File archivoBaraja = new File("Barajas/BarajaGuardada.txt");

        try {
            if (!carpetaBarajas.exists()) {
                carpetaBarajas.mkdir();
            }

            // Comprobamos si existe antes de leer
            if (archivoBaraja.exists()) {
                Scanner leerFichero = new Scanner(archivoBaraja);
                Methods.initializeDeck(playerDeck);
                currentElixir = Assets.INITIAL_ELIXIR;

                for (int i = 0; i < playerDeck.length; i++) {
                    if (leerFichero.hasNext()) {
                        playerDeck[i] = leerFichero.next();
                        currentElixir -= Methods.getCharacterElixir(playerDeck[i].charAt(0));
                    }
                }
                leerFichero.close();
                System.out.println("Baraja cargada correctamente.");
            } else {
                System.out.println("No existe ninguna baraja guardada previamente.");
                System.out.println("Usa la opción 3 para guardar una primero.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("No ha sido posible cargar la baraja correctamente.");
        }
        return currentElixir;
    }

    // Método del CASE 3:
    /**
     * Guarda la baraja actual en un fichero de texto.
     * 
     * @param playerDeck Vector con las cartas del jugador.
     */
    public static void guardarBaraja(String[] playerDeck) {
        System.out.println("Guardando la baraja ...");
        try {
            // Crear carpeta si no existe (Control de errores)
            File carpeta = new File("Barajas");
            if (!carpeta.exists())
                carpeta.mkdir();

            // Usamos PrintWriter como solicitado (más fácil para escribir strings)
            PrintWriter escribirFichero = new PrintWriter("Barajas/BarajaGuardada.txt");

            for (int i = 0; i < playerDeck.length; i++) {
                if (!playerDeck[i].equals("")) {
                    // Escribir el personaje y las coordenadas en el fichero:
                    escribirFichero.print(playerDeck[i] + " ");
                }
            }
            escribirFichero.close();
            System.out.println("Mazo guardado correctamente.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("No ha sido posible guardar la baraja.");
        }
    }

    // --- LÓGICA DEL JUEGO ---
    // Método del CASE 1:
    // Crear método auxiliar para cargar baraja enemiga aleatoria (Opción 1).
    /**
     * Gestiona el inicio de una nueva partida (Opción 1).
     * Comprueba si el jugador tiene cartas. Si es así, carga un enemigo aleatorio
     * desde el fichero "Barajas/BarajasEnemigas.txt" y lanza la lógica del juego.
     *
     * @param in         Scanner para la lectura de datos.
     * @param playerDeck Baraja actual del jugador.
     * @param enemyDeck  Baraja del enemigo donde se cargarán los datos.
     */
    public static void nuevaPartida(Scanner in, String[] playerDeck, String[] enemyDeck) {
        Methods.initializeDeck(enemyDeck);
        boolean hayPersonajes = false;

        // Bucle While
        int cont = 0;
        while (cont < playerDeck.length && !hayPersonajes) {
            if (!playerDeck[cont].equals("")) {
                hayPersonajes = true;
            }
            cont++;
        }

        if (hayPersonajes) {
            System.out.println("Empezando nueva partida ...");
            System.out.println("Buscando rival...");

            try {
                Scanner ficheroEnemigos = new Scanner(new File("Barajas/BarajasEnemigas.txt"));
                String[] listaEnemigos = new String[100];
                int totalLineas = 0;

                // Leemos todas las líneas del fichero BarajasEnemigas.txt
                // Calculamos el total de líneas del fichero.
                while (ficheroEnemigos.hasNextLine() && totalLineas < listaEnemigos.length) {
                    listaEnemigos[totalLineas] = ficheroEnemigos.nextLine();
                    totalLineas++;
                }
                ficheroEnemigos.close();

                if (totalLineas > 0) {
                    // Número aleatorio entre el 0 y el total de líneas.
                    int indiceAleatorio = (int) (Math.random() * totalLineas);
                    // Elegimos una linea del fichero
                    String lineaEnemiga = listaEnemigos[indiceAleatorio];

                    // Metemos en un Scanner la línea elegida. No metemos un fichero esta vez!
                    Scanner parserLinea = new Scanner(lineaEnemiga);
                    int hueco = 0;
                    // Rellenamos con la línea el vector de enemyDeck.
                    while (parserLinea.hasNext() && hueco < enemyDeck.length) {
                        enemyDeck[hueco] = parserLinea.next();
                        hueco++;
                    }
                    parserLinea.close();

                    // --- INICIO DE LA PARTIDA ---
                    Methods.startGame(in, playerDeck, enemyDeck);

                } else {
                    System.out.println("Error: El fichero de enemigos está vacío.");
                }

            } catch (Exception e) {
                System.out.println("Error al cargar enemigo: " + e.getMessage());
            }

        } else {
            System.out.println("¡Error! Tienes que configurar tu baraja antes.");
            System.out.println("Ve a la opción 2 para comprar tropas.");
        }
    }

    // Método del CASE 2:
    /**
     * Gestiona el menú de configuración de la baraja permitiendo añadir o borrar
     * tropas.
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
            printBoard(deck);
            printStats();
            System.out.println("Elixir restante 💧: " + currentElixir);

            System.out.print("Personaje a añadir (x para borrar; 0 para guardar): ");
            String entradaAccion = in.nextLine();

            // Si introducimos un Intro ya no entra.
            if (entradaAccion.length() == 1) {
                char simbolo = entradaAccion.charAt(0);

                switch (simbolo) {
                    case '0':
                        seguirEditando = false;
                        System.out.println("Guardando configuración...");
                        System.out.println("Mazo guardado en memoria virtual, pendiente de escribir la partida.");
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

                                        boolean ocupado = false;
                                        for (int i = 0; i < deck.length && !ocupado; i++) {
                                            if (!deck[i].equals("") &&
                                                    (deck[i].charAt(1) - '0' == fila) &&
                                                    (deck[i].charAt(2) - '0' == col)) {
                                                ocupado = true;
                                            }
                                        }

                                        if (!ocupado) {
                                            String cartaFinal = simbolo + entradaPosPoner;
                                            boolean guardado = false;
                                            for (int i = 0; i < deck.length && !guardado; i++) {
                                                if (deck[i].equals("")) {
                                                    deck[i] = simbolo + entradaPosPoner;
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
                }
            } else {
                System.out.println("No has introducido un SÍMBOLO correcto. pe (V, K, x, 0 ...) ");
            }

        } while (seguirEditando);

        return currentElixir;
    }

    /**
     * Actualiza y guarda las estadísticas de victorias/derrotas.
     * Crea la carpeta y el fichero si no existen.
     * Utiliza SCANNER en lugar de split/trim para cumplir con las restricciones
     * académicas.
     * 
     * @param haGanado true si el jugador ganó, false si perdió.
     */
    public static void actualizarEstadisticas(boolean haGanado) {
        int victorias = 0;
        int derrotas = 0;
        boolean lecturaCorrecta = true;

        // Ruta relativa
        File carpeta = new File("Estadisticas");
        File archivo = new File("Estadisticas/EstadisticasGuardadas.txt");

        try {
            // 1. Crear directorio si no existe
            if (!carpeta.exists()) {
                carpeta.mkdir();
            }

            // 2. Crear archivo si no existe
            if (!archivo.exists()) {
                if (archivo.createNewFile()) {
                    System.out.println("Se ha creado el archivo de estadísticas nuevo.");
                }
            } else {
                // 3. Leer estadísticas previas USANDO SCANNER
                Scanner lector = new Scanner(archivo);

                try {
                    // Leemos token a token esperando el formato "Victorias: X"
                    if (lector.hasNext()) {
                        lector.next(); // Ignoramos "Victorias:"
                        if (lector.hasNextInt()) {
                            victorias = lector.nextInt();
                        } else {
                            lecturaCorrecta = false;
                        }

                        // Lee "Derrotas:" y lo ignoramos
                        if (lector.hasNext()) {
                            lector.next(); // Ignoramos "Derrotas:"
                        }
                        // Lee el número si existe
                        if (lector.hasNextInt()) {
                            derrotas = lector.nextInt();
                        } else {
                            lecturaCorrecta = false;
                        }
                    }
                } catch (Exception e) {
                    lecturaCorrecta = false;
                    System.out.println("Error al leer el formato del archivo.");
                }
                lector.close();
            }

            // 4. Actualizar contadores SOLO si la lectura fue correcta
            if (lecturaCorrecta) {
                if (haGanado) {
                    victorias++;
                    System.out.println("🎉 ¡Victoria registrada en las estadísticas! 🎉");
                } else {
                    derrotas++;
                    System.out.println("💀 Derrota registrada en las estadísticas. 💀");
                }

                // 5. Escribir nuevos datos
                // Usamos PrintWriter (es más cómodo que FileWriter)
                PrintWriter escritor = new PrintWriter(archivo);
                escritor.println("Victorias: " + victorias);
                escritor.println("Derrotas: " + derrotas);
                escritor.close();

                // Mostrar resumen
                System.out.println("--- ESTADÍSTICAS TOTALES ---");
                System.out.println("🏆 Victorias: " + victorias);
                System.out.println("❌ Derrotas: " + derrotas);
                System.out.println("----------------------------");
            } else {
                System.out
                        .println("⚠️  AVISO: El archivo de estadísticas parece dañado o tiene un formato incorrecto.");
                System.out.println("⚠️  No se han guardado los nuevos datos para evitar perder el progreso anterior.");
            }

        } catch (IOException e) {
            System.out.println("Error al guardar estadísticas: " + e.getMessage());
        }
    }
}