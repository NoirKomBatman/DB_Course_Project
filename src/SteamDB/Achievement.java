package SteamDB;

public class Achievement {

	private String game;
	private String achName;
	private String description;
	
	public Achievement(String achName, String description,String game) {
		this.game = game;
		this.achName = achName;
		this.description = description;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public String getAchName() {
		return achName;
	}

	public void setAchName(String achName) {
		this.achName = achName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}
