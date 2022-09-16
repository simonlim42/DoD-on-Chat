import java.util.ArrayList;

/**
 * Contains the main logic part of the game, as it processes.
 *
 */
public class GameLogic {
	private int gold;
	private Map map;
	ArrayList<Player> players = new ArrayList<>();


	/**
	 * Default constructor
	 */
	public GameLogic() {
		map = new Map();
	}

	/**
	 * Handles set up: map choice and player populating.
	 */
	private void startGame() {
		boolean chosen = false;

		//adding new player and botplayer
		Player playerReal = new Player('P');
		BotPlayer playerBot = new BotPlayer('B');
		//adding players to an array list for turn based system
		players.add(playerReal);
		players.add(playerBot);

		//if the mapFile array is empty then a default map will be played on
		if(map.getMapFileLen()==0) {
			System.out.println("No map files detected. Default map is set.");
			new Map();
		}else {
			//print all the maps available to be played on
			map.printMaps();
			System.out.println("Choose your map!");
			//loop continues until a map has been chosen
			while(chosen==false){
				String mapChoice = playerReal.getMove();
				try {//test if input can be converted to an int
					Integer.parseInt(mapChoice);
					//the int must be within the number of maps available so a map can be chosen
					if(Integer.parseInt(mapChoice)-1 < map.getMapFileLen()){
						map = new Map(Integer.parseInt(mapChoice)-1);
						chosen = true;
					}else{
						System.out.println(mapChoice + " is an invalid input. Please enter a valid number.");
					}
				}
				catch(NumberFormatException e){
					System.out.println(mapChoice + " is an invalid input. Please enter a valid number.");
				}catch(ArrayIndexOutOfBoundsException e) {
					System.out.println(mapChoice + " is an invalid input. Please enter a valid number.");
				}
			}
		}
		System.out.println("You have chosen " + map.getMapName());
		System.out.println("You may begin!");
	}

	/**
	 * Handles turn base system and players input processing.
	 */
	public void playGame(){
		startGame();
		//variable to check which player it is currently
		int currentPlayer = 0;

		//turn base system
		while(true){
			//determines which player turn it is
			Player current = players.get(currentPlayer%2);
			String input = current.getMove();

			//checks or prints a 5x5 grid centered in the current player
			if(input.equals("LOOK")){
				if(current instanceof BotPlayer){
					//checks the 5x5 grid for character 'P'
					if(look(current.getToken()).contains("P")){
						BotPlayer.playerSeen = true;
						//if human player seen then coordinate of player is obtained
						locationOfBoth();
					}else{
						BotPlayer.playerSeen = false;
					}
				}else{
					System.out.println(look(current.getToken()));
				}

				//prints how much gold required to win on the current map
			}else if(input.equals("HELLO")){
				System.out.println("Gold to win: "+ hello());

				//moves current player's token on the map and prints "SUCCESS" or "FAIL"
			}else if (input.startsWith("MOVE ") && input.length()<7&& input.length()>5){
				String direction = input.substring(5);
				if(direction.equals("N") || direction.equals("S")||direction.equals("E")|| direction.equals("W")){
					if(current instanceof BotPlayer){
						move(direction,current.getToken()); 
						//keep track of bot coordinate after every move
						locationOfB();
					}else{
						System.out.println(move(direction,current.getToken()));
					}
				}else{
					System.out.println("Invalid move. Turn missed.");
				}
				//prints SUCCESS or FAIL depending if human player is standing on gold
			}else if (input.equals("PICKUP")){
				System.out.println(pickup());
				//prints current amount of gold owned by human player
			}else if(input.equals("GOLD")){
				System.out.println(gold());
				//exits the program and prints if WIN or LOSE on exit
			}else if(input.equals("QUIT")){
				if(map.standOnExit == true && hello() == gold){
					System.out.println("WIN");
					quitGame();
				}else{
					System.out.println("LOSE");
					quitGame();
				}
			}else{
				System.out.println("Sorry "+ input + " was not a valid input so you missed a turn.");
			}
			currentPlayer++;
		}
	}
	/**
	 * Returns the gold required to win.
	 *
	 * @return  Gold required to win.
	 */
	protected int hello() {
		return map.getGoldRequired();
	}

	/**
	 * Returns the gold currently owned by the player.
	 *
	 * @return  Gold currently owned.
	 */
	protected String gold() {

		return ("Gold owned: " + gold);
	}

	/**
	 * Checks if movement is legal and updates player's location on the map.
	 *
	 * @param direction  
	 * 					The direction of the movement.
	 * @param token  
	 * 				Token of player.
	 * @return  Protocol if success or not.
	 */
	protected String move(String direction,char token) {
		if(map.movement(direction,token)==true){
			return "SUCCESS";
		}else{
			if(token =='B'){
				//when bot chases a player and it runs into a wall it will stop chasing the player
				BotPlayer.playerSeen = false;
			}
			return "FAIL";
		}
	}

	/**
	 * Converts the map from a 2D char array to a single string.
	 * @param token 
	 * 				The token player;
	 * @return A String representation of the game map.
	 */
	protected String look(char token) {
		return map.miniMap(token);
	}

	/**
	 * Processes the player's pickup command, updating the map and the player's gold amount.
	 *
	 * @return If the player successfully picked-up gold or not.
	 */
	protected String pickup() {
		if(map.standOnGold==true){
			map.standOnGold = false;
			gold++;
			return ("SUCCESS. Gold owned: " + gold);
		}
		return ("FAIL. Gold owned: " + gold);
	}

	/**
	 * Quits the game, shutting down the application.
	 */
	protected void quitGame() {
		System.exit(0);
	}
	/**
	 * Finds and passes coordinates of both player tokens to BotPlayer.
	 */
	protected void locationOfBoth(){
		map.findPosition('B');
		map.findPosition('P');
		BotPlayer.receivePositionBot(map.getPosiBotY(),map.getPosiBotX());
		BotPlayer.receivePositionPlayer(map.getPosiHumanY(), map.getPosiHumanX());
	}
	/**
	 * Finds and passes coordinates of bot player token to BotPlayer.
	 */
	protected void locationOfB(){
		map.findPosition('B');
		BotPlayer.receivePositionBot(map.getPosiBotY(),map.getPosiBotX());

	}

}