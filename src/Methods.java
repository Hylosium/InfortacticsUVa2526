import java.util.Scanner;

public class Methods {

	/**
	 * Procedimiento que muestra por pantalla los diferentes turnos de un combate.
	 * Primero, se crea un vector alternando los jugadores propios y del enemigo.
	 * Segundo, se muestran todos los personajes de ese vector con el procedimiento printBoard().
	 * Tercero, se recorre ese vector para ir atacando de acuerdo a las propiedades de cara personaje.
	 * @author Alejandro Ortega
	 * @param in		 Instancia de la clase Scanner para leer datos de teclado
	 * @param playerDeck Referencia a un vector de Strings que contiene los personajes del jugador y sus posiciones en el formato "A12" 
	 * @param enemyDeck  Referencia a un vector de Strings que contiene los personajes del enemigo y sus posiciones en el formato "A12"
	 */
	public static void startGame(Scanner in, String[] playerDeck, String[] enemyDeck) {
		String[] gameDeck = new String[Assets.INITIAL_ELIXIR];
		int winner = -1;
		int i, j;
		String readEnter;
		
		//Creamos la baraja del juego
		createGameDeck(playerDeck, enemyDeck, gameDeck);
		
		//Mostramos los personajes del juego
		InfortacticsUVa.printBoard(gameDeck);
		
		System.out.println("\n\nComienza la Partida.......\n");
		
		readEnter = in.nextLine();
		readEnter = in.nextLine();
		System.out.print(readEnter);
		flushScreen();
		
		i=1;
		while(i<=Assets.MAX_TURNS && winner==-1) {
			System.out.println("Turno " + i);
			
			j=0;
			while(j<gameDeck.length && winner==-1) {
				
				if(gameDeck[j].length()>0) {
					switch(gameDeck[j].charAt(0)) {
					case(Assets.ARCHER_SYMBOL):
						archerAttack(gameDeck, j);
						break;
					case(Assets.DRAGON_SYMBOL):
						dragonAttack(gameDeck, j);
						break;
					case(Assets.PRINCESS_SYMBOL):
						archerAttack(gameDeck, j);
						archerAttack(gameDeck, j);
						break;
					case(Assets.VALKYRIE_SYMBOL):
						valkyrieAttack(gameDeck, j, true);
						break;
					case(Assets.GOBLIN_SYMBOL):
						valkyrieAttack(gameDeck, j, true);
						break;
					case(Assets.PK_SYMBOL):
						valkyrieAttack(gameDeck, j, false);
						valkyrieAttack(gameDeck, j, true);
						break;
					}
					
					winner = checkWinner(gameDeck, j);
				}
				j++;
			}
			
			InfortacticsUVa.printBoard(gameDeck);

			readEnter = in.nextLine();
			
			flushScreen();
			i++;
		}
		
		System.out.println("\n\n");
		if(winner==1) {
			System.out.println("¡Enhorabuena, Has ganado!");
		}else if(winner==0){
			System.out.println("Vaya.. ¡Perdiste!");
		}else {
			System.out.println("La partida ha terminado en empate.");
		}
		System.out.println("\n\n");
	}
	
	/**
	 * Procedimiento que rellena el vector de juego en función de los vectores propios y del enemigo
	 * Primero, inicializamos el vector de juego a todas sus posiciones con cadena vacía "".
	 * Segundo, alternamos los diferentes personajes en el vector: pares->jugador, impares->enemigo
	 * @author Alejandro Ortega
	 * @param playerDeck Referencia a un vector de Strings que contiene los personajes del jugador y sus posiciones en el formato "A12"
	 * @param enemyDeck  Referencia a un vector de Strings que contiene los personajes del enemigo y sus posiciones en el formato "A12"
	 * @param gameDeck   Referencia a un vector de Strings que contiene los personajes intercalados
	 */
	public static void createGameDeck(String[] playerDeck, String[] enemyDeck, String[] gameDeck) {
		int indexGame;
		
		initializeDeck(gameDeck);
		
		indexGame=0;
		for(int i=0; i<playerDeck.length; i++) {
			if(playerDeck[i].length()>0) {
				gameDeck[indexGame] = playerDeck[i];
				indexGame+=2;
			}
		}
		
		indexGame=1;
		for(int i=0; i<enemyDeck.length; i++) {
			if(enemyDeck[i].length()>0) {
				gameDeck[indexGame] = enemyDeck[i];
				indexGame+=2;
			}
		}
	}
	
	/**
	 * Procedimiento que simula limpiar la consola al añadir 10 saltos de línea
	 * (no se puede hacer de otra manera con la consola de Eclipse)
	 * @author Alejandro Ortega
	 */
	public static void flushScreen() {
		for(int i = 0; i < 10; i++) {
		    System.out.println("\n");
		}
	}
	
	/**
	 * Función que comprueba si hay un ganador, comprobando los personajes vivos en el vector de juego 
	 * @author Alejandro Ortega
	 * @param gameDeck Referencia al vector de juego con los personajes intercalados
	 * @param j		   Posición en el vector del turno actual: pares->jugador impares->enemigo
	 * @return		   1 Ganador Jugador; 0 Ganador Enemigo; -1 No hay ganador todavía
	 */
	public static int checkWinner(String[] gameDeck, int j) {
		int resultado;
		int i;
		
		if(j%2==0) {
			resultado = 1;
			i=1;
		}else {
			resultado = 0;
			i=0;
		}
		
		for(; i<gameDeck.length; i+=2) {
			if(gameDeck[i].length()!=0)
				return -1;
		}
		
		return resultado;	
	}
	
	/**
	 * Procedimiento que realiza un ataque a las tres posiciones en frente del personaje: Valkiria, Goblin &amp; PEKKA
	 * En caso de que no haya personajes, avanza si el valor de advance es verdadero.
	 * @author Alejandro Ortega
	 * @param gameDeck Referencia al vector de juego
	 * @param j		   Posición del vector que representa el turno: pares->jugador, impares->enemigo
	 * @param advance  Valor que indica si el personaje debe avanzar si no hay ningún jugador al que golpear
	 */
	public static void valkyrieAttack(String[] gameDeck, int j, boolean advance) {
		String turn, noTurn;
		boolean hitted = false;
		boolean front = false;
		int i;
		
		if(j%2==0) {
			turn = "Player";
			noTurn = "Enemy";
			i=1;
		}else {
			turn = "Enemy";
			noTurn = "Player";
			i=0;
		}
		
		for(; i<gameDeck.length; i+=2) {
			if(gameDeck[i].length()>0) {
				if(turn.equals("Player")){					
					if((gameDeck[j].charAt(1)-'0'>0) && (gameDeck[i].charAt(1)-'0')==(gameDeck[j].charAt(1)-'0'-1)) {
						if(gameDeck[i].charAt(2)==gameDeck[j].charAt(2)) {
							hitted = true;
							if(fightCharacters(gameDeck[j].charAt(0), gameDeck[i].charAt(0))) {
								System.out.println(getCharacterName(gameDeck[j].charAt(0)) + " ["+ gameDeck[j].charAt(1) +"]["+ gameDeck[j].charAt(2) +"] (" + turn + ") ha matado a " + getCharacterName(gameDeck[i].charAt(0)) + " ["+ gameDeck[i].charAt(1) +"]["+ gameDeck[i].charAt(2) +"] (" + noTurn + ")");
								gameDeck[i]="";
							}else {
								System.out.println(getCharacterName(gameDeck[j].charAt(0)) + " ["+ gameDeck[j].charAt(1) +"]["+ gameDeck[j].charAt(2) +"] (" + turn + ") NO ha matado a " + getCharacterName(gameDeck[i].charAt(0)) + " ["+ gameDeck[i].charAt(1) +"]["+ gameDeck[i].charAt(2) +"] (" + noTurn + ")");
								front = true;
							}
						}
						if((gameDeck[i].length()>0) && (gameDeck[j].charAt(2)-'0' > 0) && (gameDeck[i].charAt(2)-'0')==(gameDeck[j].charAt(2)-'0'-1)) {
							hitted = true;
							if(fightCharacters(gameDeck[j].charAt(0), gameDeck[i].charAt(0))) {
								System.out.println(getCharacterName(gameDeck[j].charAt(0)) + " ["+ gameDeck[j].charAt(1) +"]["+ gameDeck[j].charAt(2) +"] (" + turn + ") ha matado a " + getCharacterName(gameDeck[i].charAt(0)) + " ["+ gameDeck[i].charAt(1) +"]["+ gameDeck[i].charAt(2) +"] (" + noTurn + ")");
								gameDeck[i]="";
							}else {
								System.out.println(getCharacterName(gameDeck[j].charAt(0)) + " ["+ gameDeck[j].charAt(1) +"]["+ gameDeck[j].charAt(2) +"] (" + turn + ") NO ha matado a " + getCharacterName(gameDeck[i].charAt(0)) + " ["+ gameDeck[i].charAt(1) +"]["+ gameDeck[i].charAt(2) +"] (" + noTurn + ")");
							}
						}
						
						if((gameDeck[i].length()>0) && (gameDeck[j].charAt(2)-'0' < (Assets.BOARD_COLUMNS-1)) && (gameDeck[i].charAt(2)-'0')==(gameDeck[j].charAt(2)-'0'+1)) {
							hitted = true;
							if(fightCharacters(gameDeck[j].charAt(0), gameDeck[i].charAt(0))) {
								System.out.println(getCharacterName(gameDeck[j].charAt(0)) + " ["+ gameDeck[j].charAt(1) +"]["+ gameDeck[j].charAt(2) +"] (" + turn + ") ha matado a " + getCharacterName(gameDeck[i].charAt(0)) + " ["+ gameDeck[i].charAt(1) +"]["+ gameDeck[i].charAt(2) +"] (" + noTurn + ")");
								gameDeck[i]="";
							}else {
								System.out.println(getCharacterName(gameDeck[j].charAt(0)) + " ["+ gameDeck[j].charAt(1) +"]["+ gameDeck[j].charAt(2) +"] (" + turn + ") NO ha matado a " + getCharacterName(gameDeck[i].charAt(0)) + " ["+ gameDeck[i].charAt(1) +"]["+ gameDeck[i].charAt(2) +"] (" + noTurn + ")");
							}
						}
					}else if(gameDeck[j].charAt(1)-'0'==0) {
						hitted = true;
						if(fightCharacters(gameDeck[j].charAt(0), gameDeck[i].charAt(0))) {
							System.out.println(getCharacterName(gameDeck[j].charAt(0)) + " ["+ gameDeck[j].charAt(1) +"]["+ gameDeck[j].charAt(2) +"] (" + turn + ") ha matado a " + getCharacterName(gameDeck[i].charAt(0)) + " ["+ gameDeck[i].charAt(1) +"]["+ gameDeck[i].charAt(2) +"] (" + noTurn + ")");
							gameDeck[i]="";
						}else {
							System.out.println(getCharacterName(gameDeck[j].charAt(0)) + " ["+ gameDeck[j].charAt(1) +"]["+ gameDeck[j].charAt(2) +"] (" + turn + ") NO ha matado a " + getCharacterName(gameDeck[i].charAt(0)) + " ["+ gameDeck[i].charAt(1) +"]["+ gameDeck[i].charAt(2) +"] (" + noTurn + ")");
						}
					}
					
				}else if(turn.equals("Enemy")) {
					if((gameDeck[j].charAt(1)-'0'<(Assets.BOARD_ROWS-1)) && (gameDeck[i].charAt(1)-'0')==(gameDeck[j].charAt(1)-'0'+1)) {
						if(gameDeck[i].charAt(2)==gameDeck[j].charAt(2)) {
							hitted = true;
							if(fightCharacters(gameDeck[j].charAt(0), gameDeck[i].charAt(0))) {
								System.out.println(getCharacterName(gameDeck[j].charAt(0)) + " ["+ gameDeck[j].charAt(1) +"]["+ gameDeck[j].charAt(2) +"] (" + turn + ") ha matado a " + getCharacterName(gameDeck[i].charAt(0)) + " ["+ gameDeck[i].charAt(1) +"]["+ gameDeck[i].charAt(2) +"] (" + noTurn + ")");
								gameDeck[i]="";
							}else {
								System.out.println(getCharacterName(gameDeck[j].charAt(0)) + " ["+ gameDeck[j].charAt(1) +"]["+ gameDeck[j].charAt(2) +"] (" + turn + ") NO ha matado a " + getCharacterName(gameDeck[i].charAt(0)) + " ["+ gameDeck[i].charAt(1) +"]["+ gameDeck[i].charAt(2) +"] (" + noTurn + ")");
								front = true;
							}
						}
						if((gameDeck[i].length()>0) && (gameDeck[j].charAt(2)-'0' > 0) && (gameDeck[i].charAt(2)-'0')==(gameDeck[j].charAt(2)-'0'-1)) {
							hitted = true;
							if(fightCharacters(gameDeck[j].charAt(0), gameDeck[i].charAt(0))) {
								System.out.println(getCharacterName(gameDeck[j].charAt(0)) + " ["+ gameDeck[j].charAt(1) +"]["+ gameDeck[j].charAt(2) +"] (" + turn + ") ha matado a " + getCharacterName(gameDeck[i].charAt(0)) + " ["+ gameDeck[i].charAt(1) +"]["+ gameDeck[i].charAt(2) +"] (" + noTurn + ")");
								gameDeck[i]="";
							}else {
								System.out.println(getCharacterName(gameDeck[j].charAt(0)) + " ["+ gameDeck[j].charAt(1) +"]["+ gameDeck[j].charAt(2) +"] (" + turn + ") NO ha matado a " + getCharacterName(gameDeck[i].charAt(0)) + " ["+ gameDeck[i].charAt(1) +"]["+ gameDeck[i].charAt(2) +"] (" + noTurn + ")");
							}
						}
						
						if((gameDeck[i].length()>0) && (gameDeck[j].charAt(2)-'0' < (Assets.BOARD_COLUMNS-1)) && (gameDeck[i].charAt(2)-'0')==(gameDeck[j].charAt(2)-'0'+1)) {
							hitted = true;
							if(fightCharacters(gameDeck[j].charAt(0), gameDeck[i].charAt(0))) {
								System.out.println(getCharacterName(gameDeck[j].charAt(0)) + " ["+ gameDeck[j].charAt(1) +"]["+ gameDeck[j].charAt(2) +"] (" + turn + ") ha matado a " + getCharacterName(gameDeck[i].charAt(0)) + " ["+ gameDeck[i].charAt(1) +"]["+ gameDeck[i].charAt(2) +"] (" + noTurn + ")");
								gameDeck[i]="";
							}else {
								System.out.println(getCharacterName(gameDeck[j].charAt(0)) + " ["+ gameDeck[j].charAt(1) +"]["+ gameDeck[j].charAt(2) +"] (" + turn + ") NO ha matado a " + getCharacterName(gameDeck[i].charAt(0)) + " ["+ gameDeck[i].charAt(1) +"]["+ gameDeck[i].charAt(2) +"] (" + noTurn + ")");
							}
						}
					}else if(gameDeck[j].charAt(1)-'0'== (Assets.BOARD_ROWS-1)) {
						hitted = true;
						if(fightCharacters(gameDeck[j].charAt(0), gameDeck[i].charAt(0))) {
							System.out.println(getCharacterName(gameDeck[j].charAt(0)) + " ["+ gameDeck[j].charAt(1) +"]["+ gameDeck[j].charAt(2) +"] (" + turn + ") ha matado a " + getCharacterName(gameDeck[i].charAt(0)) + " ["+ gameDeck[i].charAt(1) +"]["+ gameDeck[i].charAt(2) +"] (" + noTurn + ")");
							gameDeck[i]="";
						}else {
							System.out.println(getCharacterName(gameDeck[j].charAt(0)) + " ["+ gameDeck[j].charAt(1) +"]["+ gameDeck[j].charAt(2) +"] (" + turn + ") NO ha matado a " + getCharacterName(gameDeck[i].charAt(0)) + " ["+ gameDeck[i].charAt(1) +"]["+ gameDeck[i].charAt(2) +"] (" + noTurn + ")");
						}
					}
				}
			}
		}
				
		if(!hitted && !front && turn.equals("Player") && (gameDeck[j].charAt(1)-'0' > 0) && advance) {
			System.out.println(getCharacterName(gameDeck[j].charAt(0)) + " ["+ gameDeck[j].charAt(1) +"]["+ gameDeck[j].charAt(2) +"] (" + turn + ") avanza a ["+ (char)(gameDeck[j].charAt(1)-1) +"]["+ gameDeck[j].charAt(2) +"]");
			gameDeck[j] = "" + gameDeck[j].charAt(0) + ((char)(gameDeck[j].charAt(1)-1)) + gameDeck[j].charAt(2);
		}else if(!hitted && !front && turn.equals("Enemy") && (gameDeck[j].charAt(1)-'0' < (Assets.BOARD_ROWS-1)) && advance) {
			System.out.println(getCharacterName(gameDeck[j].charAt(0)) + " ["+ gameDeck[j].charAt(1) +"]["+ gameDeck[j].charAt(2) +"] (" + turn + ") avanza a ["+ (char)(gameDeck[j].charAt(1)+1) +"]["+ gameDeck[j].charAt(2) +"]");
			gameDeck[j] = "" + gameDeck[j].charAt(0) + ((char)(gameDeck[j].charAt(1)+1)) + gameDeck[j].charAt(2);
		}
	}
	
	/**
	 * Procedimiento que realiza un ataque a los personajes de la siguiente fila: Dragon
	 * En caso de que no haya personajes, avanza una fila.
	 * @author Alejandro Ortega
	 * @param gameDeck Referencia al vector de juego
	 * @param j		   Posición del vector que representa el turno: pares->jugador, impares->enemigo
	 */
	public static void dragonAttack(String[] gameDeck, int j) {
		String turn, noTurn;
		boolean hitted = false;
		boolean front = false;
		int i;
		
		if(j%2==0) {
			turn = "Player";
			noTurn = "Enemy";
			i=1;
		}else {
			turn = "Enemy";
			noTurn = "Player";
			i=0;
		}
		
		for(; i<gameDeck.length; i+=2) {
			if(gameDeck[i].length()>0) {
				if(turn.equals("Player") && (gameDeck[j].charAt(1)-'0'>0) && (gameDeck[i].charAt(1)-'0')==(gameDeck[j].charAt(1)-'0'-1)) {
					hitted = true;
					if(gameDeck[i].charAt(2)==gameDeck[j].charAt(2)) {
						front = true;
					}
					if(fightCharacters(gameDeck[j].charAt(0), gameDeck[i].charAt(0))) {
						if(gameDeck[i].charAt(2)==gameDeck[j].charAt(2)) {
							front = false;
						}
						System.out.println(getCharacterName(gameDeck[j].charAt(0)) + " ["+ gameDeck[j].charAt(1) +"]["+ gameDeck[j].charAt(2) +"] (" + turn + ") ha matado a " + getCharacterName(gameDeck[i].charAt(0)) + " ["+ gameDeck[i].charAt(1) +"]["+ gameDeck[i].charAt(2) +"] (" + noTurn + ")");
						gameDeck[i]="";
					}else {
						System.out.println(getCharacterName(gameDeck[j].charAt(0)) + " ["+ gameDeck[j].charAt(1) +"]["+ gameDeck[j].charAt(2) +"] (" + turn + ") NO ha matado a " + getCharacterName(gameDeck[i].charAt(0)) + " ["+ gameDeck[i].charAt(1) +"]["+ gameDeck[i].charAt(2) +"] (" + noTurn + ")");
					}
					
				}else if(turn.equals("Enemy") && (gameDeck[j].charAt(1)-'0'<(Assets.BOARD_ROWS-1)) && (gameDeck[i].charAt(1)-'0')==(gameDeck[j].charAt(1)-'0'+1)) {
					hitted = true;
					if(gameDeck[i].charAt(2)==gameDeck[j].charAt(2)) {
						front = true;
					}
					if(fightCharacters(gameDeck[j].charAt(0), gameDeck[i].charAt(0))) {
						if(gameDeck[i].charAt(2)==gameDeck[j].charAt(2)) {
							front = false;
						}
						System.out.println(getCharacterName(gameDeck[j].charAt(0)) + " ["+ gameDeck[j].charAt(1) +"]["+ gameDeck[j].charAt(2) +"] (" + turn + ") ha matado a " + getCharacterName(gameDeck[i].charAt(0)) + " ["+ gameDeck[i].charAt(1) +"]["+ gameDeck[i].charAt(2) +"] (" + noTurn + ")");
						gameDeck[i]="";
					}else {
						System.out.println(getCharacterName(gameDeck[j].charAt(0)) + " ["+ gameDeck[j].charAt(1) +"]["+ gameDeck[j].charAt(2) +"] (" + turn + ") NO ha matado a " + getCharacterName(gameDeck[i].charAt(0)) + " ["+ gameDeck[i].charAt(1) +"]["+ gameDeck[i].charAt(2) +"] (" + noTurn + ")");
					}
				}
			}
		}
				
		if(!hitted && !front && turn.equals("Player") && (gameDeck[j].charAt(1)-'0'>0)) {
			System.out.println(getCharacterName(gameDeck[j].charAt(0)) + " ["+ gameDeck[j].charAt(1) +"]["+ gameDeck[j].charAt(2) +"] (" + turn + ") avanza a ["+ (char)(gameDeck[j].charAt(1)-1) +"]["+ gameDeck[j].charAt(2) +"]");
			gameDeck[j] = "" + gameDeck[j].charAt(0) + ((char)(gameDeck[j].charAt(1)-1)) + gameDeck[j].charAt(2);
		}else if(!hitted && !front && turn.equals("Enemy") && (gameDeck[j].charAt(1)-'0'<Assets.BOARD_ROWS-1)) {
			System.out.println(getCharacterName(gameDeck[j].charAt(0)) + " ["+ gameDeck[j].charAt(1) +"]["+ gameDeck[j].charAt(2) +"] (" + turn + ") avanza a ["+ (char)(gameDeck[j].charAt(1)+1) +"]["+ gameDeck[j].charAt(2) +"]");
			gameDeck[j] = "" + gameDeck[j].charAt(0) + ((char)(gameDeck[j].charAt(1)+1)) + gameDeck[j].charAt(2);
		}
	}
	
	/**
	 * Procedimiento que realiza un ataque al personaje más lejano utilizando la distancia euclídea: Arquera &amp; Princesa
	 * @author Alejandro Ortega
	 * @param gameDeck Referencia al vector de juego
	 * @param j		   Posición del vector que representa el turno: pares->jugador, impares->enemigo
	 */
	public static void archerAttack(String[] gameDeck, int j) {
		String turn, noTurn;
		int i;
		double maxDistance = 0;
		int indexDistance = -1;
		
		if(j%2==0) {
			turn = "Player";
			noTurn = "Enemy";
			i=1;
		}else {
			turn = "Enemy";
			noTurn = "Player";
			i=0;
		}
		
		for(; i<gameDeck.length; i+=2) {
			if(gameDeck[i].length()>0) {
				int x1 = gameDeck[j].charAt(1)-'0';
				int y1 = gameDeck[j].charAt(2)-'0';
				int x2 = gameDeck[i].charAt(1)-'0';
				int y2 = gameDeck[i].charAt(2)-'0';
				if(Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2)) > maxDistance) {
					maxDistance = Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
					indexDistance = i;
				}
			}
		}
		if(indexDistance != -1) {
			if(fightCharacters(gameDeck[j].charAt(0), gameDeck[indexDistance].charAt(0))) {
				System.out.println(getCharacterName(gameDeck[j].charAt(0)) + " ["+ gameDeck[j].charAt(1) +"]["+ gameDeck[j].charAt(2) +"] (" + turn + ") ha matado a " + getCharacterName(gameDeck[indexDistance].charAt(0)) + " ["+ gameDeck[indexDistance].charAt(1) +"]["+ gameDeck[indexDistance].charAt(2) +"] (" + noTurn + ")");
				gameDeck[indexDistance]="";
			}else {
				System.out.println(getCharacterName(gameDeck[j].charAt(0)) + " ["+ gameDeck[j].charAt(1) +"]["+ gameDeck[j].charAt(2) +"] (" + turn + ") NO ha matado a " + getCharacterName(gameDeck[indexDistance].charAt(0)) + " ["+ gameDeck[indexDistance].charAt(1) +"]["+ gameDeck[indexDistance].charAt(2) +"] (" + noTurn + ")");
			}
		}
		
	}
	
	/**
	 * Procedimiento que escribe en un vector de Strings todas las posiciones a cadenas vacías
	 * @author Alejandro Ortega
	 * @param deck Referencia del vector a inicializar
	 */
	public static void initializeDeck(String[]deck) {
		for(int i=0; i<deck.length; i++) {
			deck[i] = "";
		}
	}
	
	/**
	 * Función que ejecuta los porcentajes de lucha entre dos personajes
	 * @author Alejandro Ortega
	 * @param attackerSymbol Carácter del símbolo que ataca
	 * @param defenderSymbol Carácter del símbolo que defiende
	 * @return				 V: Atacante ha matado a defensor, F: Atacante NO ha matado a defensor
	 */
	public static boolean fightCharacters(char attackerSymbol, char defenderSymbol) {
		int attackerProb, defenderProb;
		
		switch(attackerSymbol) {
			case(Assets.ARCHER_SYMBOL):
				attackerProb = Assets.ARCHER_ATTACK;
			break;
			case(Assets.DRAGON_SYMBOL):
				attackerProb = Assets.DRAGON_ATTACK;
			break;
			case(Assets.PRINCESS_SYMBOL):
				attackerProb = Assets.PRINCESS_ATTACK;
			break;
			case(Assets.VALKYRIE_SYMBOL):
				attackerProb = Assets.VALKYRIE_ATTACK;
			break;
			case(Assets.GOBLIN_SYMBOL):
				attackerProb = Assets.GOBLIN_ATTACK;
			break;
			case(Assets.PK_SYMBOL):
				attackerProb = Assets.PK_ATTACK;
			break;	
			default:
				System.out.println("Símbolo erróneo");
				return false;
		}
		
		switch(defenderSymbol) {
			case(Assets.ARCHER_SYMBOL):
				defenderProb = Assets.ARCHER_DEFENSE;
			break;
			case(Assets.DRAGON_SYMBOL):
				defenderProb = Assets.DRAGON_DEFENSE;
			break;
			case(Assets.PRINCESS_SYMBOL):
				defenderProb = Assets.PRINCESS_DEFENSE;
			break;
			case(Assets.VALKYRIE_SYMBOL):
				defenderProb = Assets.VALKYRIE_DEFENSE;
			break;
			case(Assets.GOBLIN_SYMBOL):
				defenderProb = Assets.GOBLIN_DEFENSE;
			break;
			case(Assets.PK_SYMBOL):
				defenderProb = Assets.PK_DEFENSE;
			break;	
			default:
				System.out.println("Símbolo erróneo");
				return false;
		}
		
		return ((Math.random() < (attackerProb/100.0)) && (Math.random() > (defenderProb/100.0)));
	}
	
	/**
	 * Función que devuelve el nombre de un personaje dado su carácter
	 * @author Alejandro Ortega
	 * @param  symbol Carácter del personaje
	 * @return Nombre del personaje o "Símbolo erróneo" si no existe
	 */
	public static String getCharacterName(char symbol) {
		String name = "";
		
		switch(symbol) {
			case(Assets.ARCHER_SYMBOL):
				name = Assets.ARCHER_NAME;
			break;
			case(Assets.DRAGON_SYMBOL):
				name = Assets.DRAGON_NAME;
			break;
			case(Assets.PRINCESS_SYMBOL):
				name = Assets.PRINCESS_NAME;
			break;
			case(Assets.VALKYRIE_SYMBOL):
				name = Assets.VALKYRIE_NAME;
			break;
			case(Assets.GOBLIN_SYMBOL):
				name = Assets.GOBLIN_NAME;
			break;
			case(Assets.PK_SYMBOL):
				name = Assets.PK_NAME;
			break;	
			default:
				System.out.println("Símbolo erróneo");
		}
		
		return name;
	}
	
	/**
	 * Función que devuelve el icono de un personaje dado su carácter
	 * @author Alejandro Ortega
	 * @param  symbol Carácter del personaje
	 * @return Icono del personaje en formato String o "Símbolo erróneo" si no existe
	 */
	public static String getCharacterImage(char symbol) {
		String image = "";
		
		switch(symbol) {
			case(Assets.ARCHER_SYMBOL):
				image = Assets.ARCHER_IMAGE;
			break;
			case(Assets.DRAGON_SYMBOL):
				image  = Assets.DRAGON_IMAGE;
			break;
			case(Assets.PRINCESS_SYMBOL):
				image = Assets.PRINCESS_IMAGE;
			break;
			case(Assets.VALKYRIE_SYMBOL):
				image = Assets.VALKYRIE_IMAGE;
			break;
			case(Assets.GOBLIN_SYMBOL):
				image = Assets.GOBLIN_IMAGE;
			break;
			case(Assets.PK_SYMBOL):
				image = Assets.PK_IMAGE;
			break;	
			default:
				System.out.println("Símbolo erróneo");
		}		
		return image;
	}
	
	/**
	 * Función que devuelve el coste de elixir de un personaje dado su carácter
	 * @author Alejandro Ortega
	 * @param  symbol Carácter del personaje
	 * @return Coste de elixir o "Símbolo erróneo" si no existe
	 */
	public static int getCharacterElixir(char symbol) {
		int elixir = 0;
		
		switch(symbol) {
			case(Assets.ARCHER_SYMBOL):
				elixir = Assets.ARCHER_ELIXIR;
			break;
			case(Assets.DRAGON_SYMBOL):
				elixir  = Assets.DRAGON_ELIXIR;
			break;
			case(Assets.PRINCESS_SYMBOL):
				elixir = Assets.PRINCESS_ELIXIR;
			break;
			case(Assets.VALKYRIE_SYMBOL):
				elixir = Assets.VALKYRIE_ELIXIR;
			break;
			case(Assets.GOBLIN_SYMBOL):
				elixir = Assets.GOBLIN_ELIXIR;
			break;
			case(Assets.PK_SYMBOL):
				elixir = Assets.PK_ELIXIR;
			break;	
			default:
				System.out.println("Símbolo erróneo");
		}		
		return elixir;
	}
}
