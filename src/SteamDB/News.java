package SteamDB;

import java.util.Date;

public class News {

	private int appID;
	private String gameName;
	private String message;
	private Date date;
	
	public News(int appID,String gameName, String message, Date date) {
		this.appID = appID;
		this.gameName = gameName;
		this.message = message;
		this.date = date;
	}
	
	public String getGameName(){
		return gameName;
	}
	
	public void setGameName(String gameName){
		this.gameName = gameName;
	}

	public int getAppID() {
		return appID;
	}

	public void setAppID(int appID) {
		this.appID = appID;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
