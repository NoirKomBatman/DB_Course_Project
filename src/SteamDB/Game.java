package SteamDB;

import java.lang.String;
import java.util.Date;

public class Game {

	private int appID;
	private String title;
	private String genre;
	private Date date;
	private String devName;
	
	public Game(int appID, String title, String genre, Date date, String devName)
	{
		this.appID = appID;
		this.title = title;
		this.genre = genre;
		this.date = date;
		this.devName = devName;
	}

	public int getAppID() {
		return appID;
	}

	public void setAppID(int appID) {
		this.appID = appID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}
	
	
	
	
}
