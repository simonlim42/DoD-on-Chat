import java.util.Random;
/**
 *Runs the game with a bot player and contains code to determine its input
 *
 */
public class BotPlayer extends Player{
	private Random random;
	//variables of bot and human player coordinates
	private static int botPlayerY;
	private static int botPlayerX;
	private static int humanPlayerY;
	private static int humanPlayerX;
	//boolean to make the bot look at its first turn
	private boolean start = true;
	//boolean to make the bot move then look then move
	private boolean botToggle;
	//booleans that lets the bot know when there is a player arround it
	public static boolean playerSeen;
	private String userInput;

	/**
	 * 
	 * @param token 
	 * 			The token of the BOT that is used in the game.
	 */
	public BotPlayer(char token){
		super(token);
		//for random movement by bot
		random = new Random();
	}
	/**
	 * Gets the position of the bot on the map.
	 * @param y
	 * 			Bot player's y coordinate on 2-D array.
	 * @param x
	 * 			Bot player's x coordinate on 2-D array.
	 */			
	public static void receivePositionBot(int y,int x){
		botPlayerY=y;
		botPlayerX=x;
	}

	/**
	 * Gets the position of the human player on the map.
	 * @param y
	 * 			Human player's y coordinate on 2-D array.
	 * @param x
	 * 			Human player's x coordinate on 2-D array.
	 */
	public static void receivePositionPlayer(int y,int x){
		humanPlayerY=y;
		humanPlayerX=x;
	}

	/**
	 * Contains code that determines the bot's next move and returns the string command.
	 */
	public String getMove(){
		//on the first bot turn it will LOOK
		if (start==true){
			userInput="LOOK";
			start = false;
			//Chasing mechanic
		}else if(playerSeen==true){
			//when the bot has reached the last recorded position of human player it LOOKs again
			if(botPlayerY==humanPlayerY && botPlayerX==humanPlayerX){
				userInput = "LOOK";
			}
			/*the difference in coordinates will determine which direction the bot will move,
			to get closer to the human player*/
			else if(botPlayerY>humanPlayerY){
				userInput ="MOVE N";
			}else if(botPlayerY<humanPlayerY){
				userInput = "MOVE S";
			}else if(botPlayerX>humanPlayerX){
				userInput = "MOVE W";
			}else if(botPlayerX<humanPlayerX){
				userInput = "MOVE E";
			}
		}
		//if the bot does not see human player around it, it will move randomly
		//it will toggle between MOVE and LOOK to continuously move and check for human player
		else if(playerSeen == false){
			int r = random.nextInt(4);
			if(botToggle==false){
				if(r==0){
					userInput="MOVE N";
				}else if(r==1){
					userInput="MOVE W";
				}else if(r==2){
					userInput="MOVE S";
				}else if (r==3){
					userInput="MOVE E";
				}
				botToggle = true;
			}else{
				userInput="LOOK";
				botToggle = false;
			}
		}
		return userInput;
	}
}