package SteamDB;

public class Item {

	private String itemID;
	private int appID;
	private String game;
	private int itemLvl;
	private String description;
	
	public Item(String itemID, int appID, String game, int itemLvl, String descr) {
		this.itemID = itemID;
		this.appID = appID;
		this.game = game;
		this.itemLvl = itemLvl;
		this.description = descr;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public int getAppID() {
		return appID;
	}

	public void setAppID(int appID) {
		this.appID = appID;
	}
	
	public String getGame() {
		return game;
	}

	public int getItemLvl() {
		return itemLvl;
	}

	public void setItemLvl(int itemLvl) {
		this.itemLvl = itemLvl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
