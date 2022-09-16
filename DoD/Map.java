import java.io.*;
import java.util.Random;
import java.util.ArrayList;
/**
 * Reads and contains in memory the map of the game.
 *
 */
public class Map {
	//ADD NAME OF .txt FILE INTO THIS ARRAY AND INCLUDE .txt FILE IN FOLDER FOR MAP TO BE AVAILABLE
	//ARRAY THAT CONTAINS THE NAMES OF txt FILES
	String mapFile[] = {"large_example_map.txt", "medium_example_map.txt", "small_example_map.txt","test_example_map.txt"};

	//Representation of the map
	private char[][] map;

	//width and height of arraylist that gets converted to 2d array
	private int width=0;
	private int height=0;

	//Map name 
	private String mapName;


	//Gold required for the human player to win
	private int goldRequired;
	//Coordinates used to change player's token position on board
	private int moveVerti;
	private int moveHori;
	//Coordinates of token before changing positions on board
	private int initialPositionY;
	private int initialPositionX;
	//Coordinates of both tokens after movement on board
	private int humanPositionY;
	private int humanPositionX;
	private int botPositionY;
	private int botPositionX;

	public boolean standOnGold = false;
	public boolean standOnGoldBot = false;
	public boolean standOnExit = false;
	public boolean standOnExitBot = false;
	public boolean goldPickUp = false;

	private Random random;


	/**
	 * Default constructor, creates the default map "Very small Labyrinth of doom".
	 */
	public Map() {
		random = new Random();
		mapName = "Very small Labyrinth of Doom";
		goldRequired = 2;
		map = new char[][]{
			{'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'},
			{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
			{'#','.','.','.','.','.','.','G','.','.','.','.','.','.','.','.','.','E','.','#'},
			{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
			{'#','.','.','E','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
			{'#','.','.','.','.','.','.','.','.','.','.','.','G','.','.','.','.','.','.','#'},
			{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
			{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
			{'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'}
		};
		randomSpawn('P');
		randomSpawn('B');
	}

	/**
	 * Constructor that accepts a map to read in from.
	 *
	 * @param fileIndex : The integer index of the map file array.
	 */
	public Map(int fileIndex) {
		readMap(fileIndex);
	}

	/**
	 * @return Gold required to exit the current map.
	 */
	protected int getGoldRequired() {
		return goldRequired;
	}

	/**
	 * @return The length of the mapFile array
	 */
	protected int getMapFileLen() {
		return mapFile.length;
	}


	/**
	 * @return The name of the current map.
	 */
	protected String getMapName() {
		return mapName;
	}

	/**
	 * Prints out all the maps that are available to be read in the Array mapFile[].
	 */
	public void printMaps(){
		try {
			for(int i=0;i<mapFile.length;i++) {
				FileReader fileReader = new FileReader(mapFile[i]);
				String line;
				BufferedReader br = new BufferedReader(fileReader);
				//printing out index of maps
				System.out.println(i+1 + ")");
				while ((line = br.readLine()) != null) {
					//removed gold required when printing for increased difficulty
					if(line.startsWith("win")){
						continue;
					}else{
						System.out.println(line);
					}
				}
				System.out.println();//to make print look cleaner
				br.close();
			}
		}catch (FileNotFoundException e) {
			// Catch this before the IOException
			System.err.println("This file does not exist.");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Reads the map from file.
	 *
	 * @param file 
	 * 			Player's choice of map in terms of an integer.
	 */
	protected void readMap(Integer file) {
		if(mapFile.length==0) {
			new Map();
		}
		random = new Random();
		ArrayList<char[]> mapWrite = new ArrayList<char[]>();
		FileReader fileReader;
		try {
			fileReader = new FileReader(mapFile[file]);
			String line = null;
			BufferedReader mapLine = new BufferedReader(fileReader);
			//continues until there is no more column of lines to be read
			while((line = mapLine.readLine())!= null){

				if(line.startsWith("name")) {
					mapName=line.substring(5);

				}else if (line.startsWith("win")) {
					goldRequired=Integer.parseInt(line.substring(4));

				}else {
					if(line.length() > width){
						this.width = line.length();
					}
					line = line.trim();
					mapWrite.add(line.toCharArray());
				}
			}
			mapLine.close();
			this.height = mapWrite.size();
			//converting ArrayList to 2-D Array
			map = new char[height][width];
			for(int i = 0; i < height; i++){
				char[] row = mapWrite.get(i);
				for(int j = 0; j < row.length; j++){
					map[i][j] = row[j];
				}
			}
		}catch (FileNotFoundException e) {
			e.printStackTrace();// Catch this before the IOException
		}catch (IOException e) {
			e.printStackTrace();
		}
		randomSpawn('P');
		randomSpawn('B');
	}

	/**
	 * Places the token at a random position in the map.
	 * @param t
	 * 			Token of player.
	 * 		
	 */
	protected void randomSpawn(char t){
		//chooses a random row
		int rVerti = random.nextInt(map.length);
		//based on the random row a random column
		int rHori = random.nextInt(map[rVerti].length);
		//player not allowed to spawn on gold. walls and enemies
		if(map[rVerti][rHori]!='#' && map[rVerti][rHori]!='B' && map[rVerti][rHori]!='G' && map[rVerti][rHori]!='P'){
			//checking if players spawns on an exit
			if(t=='P') {
				if(map[rVerti][rHori]=='E') {
					standOnExit=true;
				}
			}
			if(t=='B') {
				if(map[rVerti][rHori]=='E') {
					standOnExitBot=true;
				}
			}
			map[rVerti][rHori] = t;
		}else{
			randomSpawn(t);
		}
	}

	/**
	 * Saves the coordinates of the player as variables.
	 * @param token
	 * 				Token of player.
	 */			
	public void findPosition(char token){
		for(int i=0; i<map.length; i++){
			for(int j=0; j<map[i].length; j++){
				if(map[i][j]==token){
					//keeps current player token position
					initialPositionY = i;
					initialPositionX = j;
					if(token=='P'){
						//keeps human player token position
						humanPositionY=i; 
						humanPositionX=j;

					}else{//keeps bot player token position
						botPositionY=i; 
						botPositionX=j;
					}
				}
			}
		}
	}

	/**
	 * @return Y coordinate of bot token.
	 */
	public int getPosiBotY(){
		return botPositionY;
	}
	/**
	 * @return Y coordinate of bot token.
	 */
	public int getPosiBotX(){
		return botPositionX;
	}
	/**
	 * @return Y coordinate of human player token.
	 */
	public int getPosiHumanY(){
		return humanPositionY;
	}
	/**
	 * @return X coordinate of human player token.
	 */
	public int getPosiHumanX(){
		return humanPositionX;
	}

	/**
	 * Handles the formation of string for 5x5 grid of LOOK command.
	 * @param token
	 * 				Token of player.
	 * @return A 5x5 sized string with the token of the player at the center.
	 */			
	public String miniMap(char token){
		findPosition(token);
		String s = "";
		for(int vertical = initialPositionY-2;vertical<initialPositionY+3;vertical++){
			for(int horizontal = initialPositionX-2; horizontal<initialPositionX+3;horizontal++){
				if(vertical<0 || vertical>=map.length){
					s += '#';
				}else if(horizontal<0 || horizontal>=map[initialPositionY].length){
					s += '#';
				}else{
					s += map[vertical][horizontal];
				}
			}
			s += "\n";
		}
		return s;
	}

	/**
	 * Handles the changing of positions of tokens on the 2D-Array map.
	 * 
	 * @param d
	 * 			Direction in which the token is moving.
	 * @param token
	 * 			The token of the player which is moving.
	 * 
	 * @return True if change of position is a success. False if change of position can not occur.
	 */
	public boolean movement(String d,char token){
		findPosition(token);
		//move up
		if(d.equals("N")){
			moveVerti = initialPositionY-1;
			moveHori = initialPositionX;
		}
		//move down
		else if(d.equals("S")){
			moveVerti = initialPositionY+1;
			moveHori = initialPositionX;
		}
		//move right
		else if(d.equals("E")){
			moveVerti = initialPositionY;
			moveHori = initialPositionX+1;
		}
		//move left
		else if(d.equals("W")){
			moveVerti = initialPositionY;
			moveHori = initialPositionX-1;
		}
		//cannot move through walls
		if(map[moveVerti][moveHori]=='#'){
			return false;

			//when human and bot token meets human loses and game ends
		}else if(map[moveVerti][moveHori]=='P'||map[moveVerti][moveHori]=='B'){
			System.out.println("LOSE");
			System.exit(0);
			return false;
			//when human player is standing on gold token
		}else if(standOnGold == true && token!='B'){
			if(map[moveVerti][moveHori]=='G'){
				standOnGold = true;
			}else if(map[moveVerti][moveHori]=='E'){
				standOnExit = true;
				standOnGold = false;
			}else {
				standOnExit = false;
			}
			map[moveVerti][moveHori]=token;
			map[initialPositionY][initialPositionX]='G';
			return true;
			//when bot player is standing on gold token
		}else if(standOnGoldBot == true && token == 'B'){
			if(map[moveVerti][moveHori]=='G'){
				standOnGoldBot = true;
			}else if(map[moveVerti][moveHori]=='E'){
				standOnExitBot = true;
				standOnGoldBot = false;
			}else {
				standOnExitBot=false;
			}
			map[moveVerti][moveHori]=token;
			map[initialPositionY][initialPositionX]='G';
			return true;
			//when human player is standing on exit token
		}else if(standOnExit==true && token!='B'){
			if(map[moveVerti][moveHori]=='G'){
				standOnGold = true;
			}else if(map[moveVerti][moveHori]=='E'){
				standOnExit=true;
				standOnGold = false;
			}else{
				standOnExit = false;
			}
			map[moveVerti][moveHori]=token;
			map[initialPositionY][initialPositionX]='E';
			return true;
			//when bot player is standing on exit token
		}else if(standOnExitBot==true && token=='B'){
			if(map[moveVerti][moveHori]=='G'){
				standOnGoldBot = true;
			}else if(map[moveVerti][moveHori]=='E'){
				standOnExitBot=true;
				standOnGoldBot=false;
			}else{
				standOnExitBot = false;
			}
			map[moveVerti][moveHori]=token;
			map[initialPositionY][initialPositionX]='E';
			return true;
		}
		//when player move from empty space on to a gold token
		else if(map[moveVerti][moveHori]=='G'){
			if(token!='B'){
				standOnGold = true;
			}
			else{
				standOnGoldBot = true;
			}
			map[moveVerti][moveHori]=token;
			map[initialPositionY][initialPositionX]='.';
			return true;
			//when player move from empty space on to a exit token
		}else if(map[moveVerti][moveHori]=='E'){
			if(token != 'B'){
				standOnExit = true;
			}else{
				standOnExitBot=true;
			}
			map[moveVerti][moveHori]=token;
			map[initialPositionY][initialPositionX]='.';
			return true;
			//when player moves from one empty space to another
		}else{
			map[moveVerti][moveHori]=token;
			map[initialPositionY][initialPositionX]='.';
			return true;
		}
	}
}


