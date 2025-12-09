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
                // --- OPCIÓN 5: SALIR [Ver Pág. 2] ---
                // Mostrar mensaje despedida y romper el bucle principal.
                case "5":
                    System.out.println("Saliendo del programa ...");
                    salir = true;
                    break;

                // --- OPCIÓN 4: CARGAR BARAJA [Ver Pág. 3] ---
                // Leer fichero "Barajas/BarajaGuardada.txt".
                // Rellenar vector jugador y recalcular el elixir gastado. Checkear errores de fichero.
                case "4":
                    System.out.println("Cargando la baraja ...");
                    try{
                        // Abrir el fichero
                        Scanner leerFichero = new Scanner(new File("Barajas/BarajaGuardada.txt"));
                        // Limpiar el vector
                        Methods.initializeDeck(playerDeck);
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


                // --- OPCIÓN 2: CONFIGURAR BARAJA [Ver Pág. 2 y Pág. 3] ---
                // TODO: Mostrar tablero actual (usando printBoard), stats de personajes y elixir restante.
                // TODO: Bucle de configuración:
                //      - Si introduce '0': Guardar cambios en memoria y volver al menú.
                //      - Si introduce 'x': Pedir posición y borrar figura (devolver elixir).
                //      - Si introduce Personaje (A, V...):
                //          1. Validar elixir suficiente.
                //          2. Pedir posición (XY). Validar que es zona jugador (filas 3-5) y está vacía [Ver Pág. 3].
                //          3. Guardar en el vector.
                case "2":
                    System.out.println("Configurando la baraja ...");
                    // 1. Bandera para controlar el bucle
                    boolean seguirEditando = true;
                    do{
                        // 2. Mostrar estado actual
                        printBoard(playerDeck);
                        System.out.println("Elixir restante: " + elixirRestante);
                        System.out.println("Escribe posición y tropa (ej: V33), 'x' para borrar, o '0' para salir.");

                        // 3. Pedir entrada
                        System.out.print("Opción: ");
                        String subEntrada = in.nextLine();

                        // 4. Evaluar entrada
                        switch (subEntrada) {
                            case "0":
                                seguirEditando=false;
                                System.out.println("Guardando configuración...");
                                break;

                            case "x":
                                System.out.println("Entrando en el modo BORRAR ...");
                                System.out.println("Introduzca las posiciones para borrar un PJ: ");
                                // Para las filas
                                int posFilaB = in.nextInt();
                                // Para las columnas
                                int posColB = in.nextInt();
                                // Para quitarnos de problemas con "\n":
                                in.nextLine();
                                // Banderas para detener el bucle
                                boolean cartaEliminada = false;
                                // Validamos las coordenadas introducidas.
                                if ((posFilaB <= 5 && posFilaB >=3)&&(posColB <= 5 && posColB >=0)){
                                    // Recorrer la baraja buscando las coordenadas.
                                    for (int i = 0; i < playerDeck.length && !cartaEliminada; i++){
                                        // Comprobar que la posición no está vacía:
                                        if (!playerDeck[i].equals("")){
                                            // Comprobar que los datos introducidos existen en la baraja
                                            // Antes de borrar, validamos:
                                            if (playerDeck[i].charAt(1) - '0' == posFilaB &&
                                                playerDeck[i].charAt(2) - '0' == posColB){
                                                // Devolvemos el valor del PJ a nuestra cantidad de elixir.
                                                int coste = Methods.getCharacterElixir(playerDeck[i].charAt(0));
                                                // Comprobamos que realmente ese PJ tiene un valor:
                                                if (coste > 0){
                                                    elixirRestante += coste;
                                                    // Validamos que, si nos pasamos del elixir máximo, no guardamos mas del TOPE.
                                                    if (elixirRestante > Assets.INITIAL_ELIXIR){
                                                        elixirRestante = Assets.INITIAL_ELIXIR;
                                                    }
                                                    //Limpiamos esa posición del vector:
                                                    playerDeck[i] = "";
                                                    System.out.println("Ha borrado usted correctamente su personaje:");
                                                    cartaEliminada = true;

                                                }
                                            }
                                        };

                                    }
                                } else {
                                    System.out.println("Las posiciones introducidas no son válidas");
                                }

                                break;

                            default:
                                System.out.println("Intentando añadir tropa...");
                                // Solo se aceptan entradas que tengan 3 caracteres, pe A33.
                                if(subEntrada.length() == 3){
                                    // Obtenemos el coste del PJ introducido.
                                    int coste = Methods.getCharacterElixir(subEntrada.charAt(0));
                                    // Convertimos los caracteres de la entrada en enteros. Restamos el '0'
                                    // para evitar que sean en valores ASCII.
                                    int pjFil = subEntrada.charAt(1) - '0';
                                    int pjCol = subEntrada.charAt(2) - '0';

                                    // Comprobamos que el PJ existe comprobando su valor de elixir:
                                    if (coste >0){
                                        // Comprobamos que tenemos suficiente elixir para comprar PJs.
                                        if ((elixirRestante - coste >= 0)){
                                            // Validamos la posición.
                                            // Filas jugables: 3, 4, 5. Columnas: 0 a 5.
                                            if ((pjFil >=3 && pjFil <=5)&&(pjCol >=0 && pjCol <=5)){

                                                // Banderas de control para los siguientes bucles.
                                                // (Comprobar cartas guardades y evitar sobreescribir)
                                                boolean cartaGuardada = false;
                                                boolean posicionOcupada = false;
                                                int contadorHuecos = 0;

                                                // Detector de ocupación (posicionOcupada):
                                                // Recorremos la baraja para asegurar que NO haya nadie en esas coordenadas.
                                                for (int i = 0; i < playerDeck.length && !posicionOcupada; i++){
                                                    // 1. Que el hueco NO esté vacío (!equals).
                                                    // 2. Que coincida fila Y columna.
                                                    if((!playerDeck[i].equals("")) &&
                                                            (playerDeck[i].charAt(1) - '0' ==pjFil) &&
                                                            (playerDeck[i].charAt(2) - '0' ==pjCol)){
                                                        posicionOcupada = true;
                                                    }
                                                }

                                                // Solo si el detector dice que está libre (!posicionOcupada).
                                                if (!posicionOcupada){
                                                    // Buscamos el primer hueco libre ("") para guardar.
                                                    while(!cartaGuardada && contadorHuecos<playerDeck.length){
                                                        if (playerDeck[contadorHuecos].equals("")){
                                                            // Restamos el valor del PJ al elixir total.
                                                            elixirRestante -= coste;
                                                            // Guardamos en la baraja.
                                                            playerDeck[contadorHuecos] = subEntrada;
                                                            // Salimos del bucle.
                                                            cartaGuardada=true;
                                                            System.out.println("PJ Configurado con éxito.");
                                                        }
                                                        contadorHuecos++;
                                                    }
                                                } else {
                                                    // Si posicionOcupada es true, entramos aquí.
                                                    System.out.println("¡Error! Esa posición ya está ocupada.");
                                                }

                                            } else {
                                                System.out.println("Posiciones no válidas (Filas 3-5).");
                                            }
                                        } else {
                                            System.out.println("No tienes suficiente dinero para jugar con ese PJ.");
                                        }
                                    } else {
                                        System.out.println("El personaje introducido no es valido.");
                                    }
                                }
                                break;
                        }

                    }while(seguirEditando);

                    break;

                // --- OPCIÓN 1: NUEVA PARTIDA---
                // Comprobar si la baraja del jugador tiene al menos 1 personaje. Si no -> Error.
                // Si tiene personajes -> Cargar baraja enemiga aleatoria desde "Barajas/BarajasEnemigas.txt".
                case "1":
                    // Limpiar el vector de enemigos.
                    Methods.initializeDeck(enemyDeck);
                    // Comprobar si hay personajes o no.
                    boolean hayPersonajes=false;
                    for (int i = 0; i<playerDeck.length; i++){
                        if (!(playerDeck[i].equals(""))){
                            hayPersonajes=true;
                            break;
                        } else {
                            if (i==playerDeck.length-1){
                                System.out.println("¡Tienes que configurar tu baraja antes!.");
                                System.out.println("Configura tu baraja en la opción 2.");
                                break;
                            }
                        }
                    }

                    // Empezar la partida
                    if (hayPersonajes){
                        System.out.println("Empezando nueva partida ...");
                        // Vector de enemigos, con un número muy grande para permitir guardar suficientes.
                        String[] barajaEnemiga = new String[100];
                        // Baraja aleatoria.
                        try{
                            Scanner ficheroEnemigos = new Scanner(new File("Barajas/BarajasEnemigas.txt"));
                            // Indice de líneas.
                            int nFilBarajaEnemiga=0;
                            while(ficheroEnemigos.hasNextLine()){
                                barajaEnemiga[nFilBarajaEnemiga] = ficheroEnemigos.nextLine();
                                nFilBarajaEnemiga++;
                            }
                            // Número aleatorio para elegir la línea.
                            int nAleatorioBaraja = (int) (Math.random() * (nFilBarajaEnemiga));

                            // Guardar la nueva línea en enemyDeck.
                            Scanner siguienteEnemigo = new Scanner(barajaEnemiga[nAleatorioBaraja]);
                            if (siguienteEnemigo.hasNext()){
                                // Contador para guardar en el vector enemyDeck
                                int contador=0;
                                while (siguienteEnemigo.hasNext()){
                                    enemyDeck[contador] = siguienteEnemigo.next();
                                    contador++;
                                }
                            }

                            // Llamar al juego
                            // Llamar a Methods.startGame(in, playerDeck, enemyDeck).
                            Methods.startGame(in, playerDeck, enemyDeck);
                        }
                        catch (Exception e){
                            System.out.println("Error: "+e.getMessage());
                        }
                    }
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
    // TODO: Crear método auxiliar para cargar baraja enemiga aleatoria (Opción 1).
    // TODO: Crear método auxiliar para la lógica de configuración (Opción 2) para no llenar el main.

}