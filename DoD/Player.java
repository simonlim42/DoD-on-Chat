import java.io.*;
/**
 * Human player of game and contains code needed to read inputs.
 *
 */
public class Player {
	private BufferedReader in;
	private char token;
	/**
	 * Default constructor.
	 */
	public Player(){
		this.token='P';
	}
	
	/**
	 * Constructor which takes in a parameter
	 * @param token
	 * 				Token in which represents the player
	 */	
	public Player(char token){
		in = new BufferedReader(new InputStreamReader(System.in));
		this.token=token;
	}

	/**
	 * Reads player's input from the console.
	 * <p>
	 * @return  A string containing the input the player entered.
	 */
	protected String getMove() {
		String userInput = "";
		try{
			userInput = in.readLine();
		}
		catch(IOException e){
			System.out.println(e);
		}
		return userInput;
	}

	/**
	 * Accessor used to get player's token.
	 * @return The character token of the player.
	 */
	public char getToken(){
		return token;
	}

}