import java.util.Scanner;
// TODO: Importar librerías necesarias para ficheros (File, PrintWriter, etc.)

public class InfortacticsUVa {

    public static void main(String[] args) {
        // 1. PREPARACIÓN DE DATOS
        // TODO: Inicializar Scanner.
        // TODO: Definir vector de Strings para baraja del Jugador (inicializar a "") [Ver Pág. 5, "Información de juego"].
        // TODO: Definir vector de Strings para baraja del Enemigo (inicializar a "") [Ver Pág. 5].

        // 2. BUCLE DEL MENÚ PRINCIPAL
        // TODO: Crear bucle (while) que se repita hasta que el usuario elija "Salir" [Ver Pág. 2].

        // 2.1 MOSTRAR INTERFAZ
        // TODO: Mostrar las 5 opciones del menú por pantalla (Imagen 2) [Ver Pág. 2].
        // TODO: Pedir opción al usuario. Validar que sea correcta; si no, mensaje error y repetir [Ver Pág. 2].

        // 2.2 LÓGICA DE OPCIONES (Switch)

        // --- OPCIÓN 1: NUEVA PARTIDA [Ver Pág. 3 (final) y Pág. 4] ---
        // TODO: Comprobar si la baraja del jugador tiene al menos 1 personaje. Si no -> Error.
        // TODO: Si tiene personajes -> Cargar baraja enemiga aleatoria desde "Barajas/BarajasEnemigas.txt".
        // TODO: Llamar a Methods.startGame(in, playerDeck, enemyDeck).

        // --- OPCIÓN 2: CONFIGURAR BARAJA [Ver Pág. 2 y Pág. 3] ---
        // TODO: Mostrar tablero actual (usando printBoard), stats de personajes y elixir restante.
        // TODO: Bucle de configuración:
        //      - Si introduce '0': Guardar cambios en memoria y volver al menú.
        //      - Si introduce 'x': Pedir posición y borrar figura (devolver elixir).
        //      - Si introduce Personaje (A, V...):
        //          1. Validar elixir suficiente.
        //          2. Pedir posición (XY). Validar que es zona jugador (filas 3-5) y está vacía [Ver Pág. 3].
        //          3. Guardar en el vector.

        // --- OPCIÓN 3: GUARDAR BARAJA [Ver Pág. 3] ---
        // TODO: Escribir el contenido del vector jugador en el fichero "Barajas/BarajaGuardada.txt".
        // TODO: Formato de escritura: Strings separados por espacios (Ej: "V33 D41").

        // --- OPCIÓN 4: CARGAR BARAJA [Ver Pág. 3] ---
        // TODO: Leer fichero "Barajas/BarajaGuardada.txt".
        // TODO: Rellenar vector jugador y recalcular el elixir gastado. Checkear errores de fichero.

        // --- OPCIÓN 5: SALIR [Ver Pág. 2] ---
        // TODO: Mostrar mensaje despedida y romper el bucle principal.

    }

    // 3. MÉTODOS OBLIGATORIOS Y AUXILIARES

    // TODO: Definir procedimiento 'printBoard' para dibujar el tablero 6x6 [Ver Pág. 5].
    //       (Debe leer el vector de Strings y pintar iconos o huecos vacíos).

    // TODO: (Recomendado) Crear método auxiliar para cargar baraja enemiga aleatoria (Opción 1).
    // TODO: (Recomendado) Crear método auxiliar para la lógica de configuración (Opción 2) para no llenar el main.
}