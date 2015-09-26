package SteamDB;

public class Player {
	int playerID;
	String name;
	String email;
	int userLvl;
	
	public Player(int playerID, String name, String email, int userLvl){
		this.playerID = playerID;
		this.name = name;
		this.email = email;
		this.userLvl = userLvl;
		
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getUserLvl() {
		return userLvl;
	}

	public void setUserLvl(int userLvl) {
		this.userLvl = userLvl;
	}

}
