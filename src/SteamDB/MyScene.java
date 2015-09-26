package SteamDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import SteamDB.SteamMenu;
import SteamDB.LoginScene;

public class MyScene extends Scene {
	
	protected MenuBar menuBar;
	private Menu menuGames;
	private Menu menuPlayer;
	private Menu menuEdit;
	private Menu menuView;
	private MenuItem allGames;
	private MenuItem friendsList;
	private MenuItem logOut;
	private MenuItem allDevelopers;
	private MenuItem changePassword;
	private MenuItem gameChampions;
	private MenuItem addFriend;
	private MenuItem deleteFriend;
	private MenuItem showMyGames;
	private MenuItem showMyItems;
	private MenuItem showMyAchievements;
	private MenuItem showNews;
	private MenuItem view;
	protected SteamMenu steam;
	private int WIDTH;
	private int HEIGHT;
	private MyStage currentStage;
	
	public MyScene(MyStage s, int w, int h) {
		super(new VBox(), w, h);
		
		currentStage = s;
		
		this.setFill(Color.DARKSALMON);
		menuBar = new MenuBar();
		menuGames = new Menu("Games");
		menuPlayer = new Menu("Players");
		menuEdit = new Menu("My Profile");
		menuView = new Menu("View");
		
		allGames = new MenuItem("All Games");
		friendsList = new MenuItem("Friends"); 
		logOut = new MenuItem("Log Out");
		allDevelopers = new MenuItem("Developers");
		changePassword = new MenuItem("Change Password");
		gameChampions = new MenuItem("Game Champions");
		addFriend = new MenuItem("Add Friend");
		deleteFriend = new MenuItem("Delete Friend");
		showMyGames = new MenuItem("My Games");
		showMyItems = new MenuItem("My Items");
		showMyAchievements = new MenuItem("My Achievements");
		showNews = new MenuItem("All News");
		view = new MenuItem("Show Developers Average Price View");
				
		view.setOnAction(e -> getViewHandler());
		allGames.setOnAction(e -> allGamesHandler());		
		friendsList.setOnAction(e -> friendsListHandler());
		allDevelopers.setOnAction(e -> allDevelopersHandler());
		changePassword.setOnAction(e -> updatePasswordHandler());
		gameChampions.setOnAction(e -> getGameChampions());
		logOut.setOnAction(e -> logOutHandler());
		addFriend.setOnAction(e -> addFriendHandler());
		deleteFriend.setOnAction(e -> deleteFriendHandler());
		showMyGames.setOnAction(e -> showMyGamesHandler());
		showMyItems.setOnAction(e -> showMyItemsHandler());
		showMyAchievements.setOnAction(e->showMyAchievementsHandler());
		showNews.setOnAction(e -> showNewsHandler());
		
		
		menuGames.getItems().addAll(allGames, allDevelopers,showMyGames, gameChampions,showNews);
		menuPlayer.getItems().addAll(friendsList, addFriend, deleteFriend);
		menuEdit.getItems().addAll(showMyAchievements, showMyItems,changePassword, logOut);
		menuView.getItems().addAll(view);
		menuBar.getMenus().addAll(menuGames, menuPlayer, menuEdit, menuView);
				
		((VBox) this.getRoot()).getChildren().addAll(menuBar);
	}

	private void showNewsHandler() {
        ObservableList<News> newsData = FXCollections.observableArrayList();
		
		MyStage stage = new MyStage("All News");
		MyScene scene;
		if (steam.isValveDev) scene = new ValveDevScene(stage);
		else if (steam.isAdmin) scene = new AdminScene(stage);
		else scene = new MyScene(stage);
		
		ArrayList<News> newsList = steam.query.getNews();
		
		if (newsList.isEmpty()) {steam.errorWindow("There is no news available");return;}
		for (int i=0; i<newsList.size(); i++) {
			newsData.add(newsList.get(i));
		}
		
        Label newsLabel = new Label("News");
		
		TableView newsTable = new TableView();
		
		newsTable.setEditable(true);
		
		TableColumn gameTitleCol = new TableColumn("appID");
		gameTitleCol.setMinWidth(50);
		gameTitleCol.setCellValueFactory(new PropertyValueFactory<>("appID"));
		
		// GameTable Developer
		TableColumn devNameCol = new TableColumn("gameName");
		devNameCol.setMinWidth(200);
		devNameCol.setCellValueFactory(new PropertyValueFactory<>("gameName"));
		
		// GameTable Release Date
		TableColumn messageCol = new TableColumn("message");
		messageCol.setMinWidth(300);
		messageCol.setCellValueFactory(new PropertyValueFactory<>("message"));
		
		TableColumn DateCol = new TableColumn("date");
		DateCol.setMinWidth(100);
		DateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		
		
		newsTable.setItems(newsData);
		newsTable.getColumns().addAll(gameTitleCol,devNameCol, messageCol, DateCol);
 
		VBox gamesVBox = new VBox();
		gamesVBox.setSpacing(5);
		gamesVBox.setPadding(new Insets(10, 0, 0, 10));
		gamesVBox.getChildren().addAll(newsLabel, newsTable);
		 
		((VBox) scene.getRoot()).getChildren().addAll(gamesVBox);
		
		stage.setScene(scene);
		stage.show();
	}

	public MyScene(MyStage s) {
		this(s, 700, 350);
	}
	
	private void showMyAchievementsHandler() {
		ObservableList<Achievement> gamesData = FXCollections.observableArrayList();
		
		MyStage stage = new MyStage("My Achievements");
		MyScene scene;
		if (steam.isValveDev) scene = new ValveDevScene(stage);
		else if (steam.isAdmin) scene = new AdminScene(stage);
		else scene = new MyScene(stage);
		
		ArrayList<Achievement> gamesList = steam.query.getAchievementsObtained(steam.CurrentUserID);
		
		if (gamesList.isEmpty()) {steam.errorWindow("You don't have achievments");return;}
		for (int i=0; i<gamesList.size(); i++) {
			gamesData.add(gamesList.get(i));
		}
		
		Label gamesLabel = new Label("Achievements");
		
		TableView gamesTable = new TableView();
		
		gamesTable.setEditable(true);
		
		// GameTable Game
		TableColumn gameTitleCol = new TableColumn("Name");
		gameTitleCol.setMinWidth(300);
		gameTitleCol.setCellValueFactory(new PropertyValueFactory<>("achName"));
		
		// GameTable Developer
		TableColumn devNameCol = new TableColumn("Game");
		devNameCol.setMinWidth(200);
		devNameCol.setCellValueFactory(new PropertyValueFactory<>("game"));
		
		// GameTable Release Date
		TableColumn releaseDateCol = new TableColumn("Description");
		releaseDateCol.setMinWidth(100);
		releaseDateCol.setCellValueFactory(new PropertyValueFactory<>("description"));
		
		
		gamesTable.setItems(gamesData);
		gamesTable.getColumns().addAll(gameTitleCol,devNameCol, releaseDateCol);
 
		VBox gamesVBox = new VBox();
		gamesVBox.setSpacing(5);
		gamesVBox.setPadding(new Insets(10, 0, 0, 10));
		gamesVBox.getChildren().addAll(gamesLabel, gamesTable);
		 
		((VBox) scene.getRoot()).getChildren().addAll(gamesVBox);
		
		stage.setScene(scene);
		stage.show();
	
	}
	
	
	private void showMyItemsHandler() {
		ObservableList<Item> gamesData = FXCollections.observableArrayList();
		
		MyStage stage = new MyStage("My Items");
		MyScene scene;
		if (steam.isValveDev) scene = new ValveDevScene(stage);
		else if (steam.isAdmin) scene = new AdminScene(stage);
		else scene = new MyScene(stage);
		
		ArrayList<Item> gamesList = steam.query.getItemsCollected(steam.CurrentUserID);
		
		if (gamesList.isEmpty()) {steam.errorWindow("You don't have any items");return;}
		for (int i=0; i<gamesList.size(); i++) {
			gamesData.add(gamesList.get(i));
		}
		
		Label gamesLabel = new Label("Items");
		
		TableView gamesTable = new TableView();
		
		gamesTable.setEditable(true);
		
		// GameTable Game
		TableColumn gameTitleCol = new TableColumn("Item");
		gameTitleCol.setMinWidth(300);
		gameTitleCol.setCellValueFactory(new PropertyValueFactory<>("itemID"));
		
		// GameTable Developer
		TableColumn devNameCol = new TableColumn("Game");
		devNameCol.setMinWidth(200);
		devNameCol.setCellValueFactory(new PropertyValueFactory<>("game"));
		
		// GameTable Release Date
		TableColumn releaseDateCol = new TableColumn("Level");
		releaseDateCol.setMinWidth(100);
		releaseDateCol.setCellValueFactory(new PropertyValueFactory<>("itemLvl"));
		
		// GameTable appID
		TableColumn appIDCol = new TableColumn("Description");
		appIDCol.setMinWidth(100);
		appIDCol.setCellValueFactory(new PropertyValueFactory<>("description"));
		
		gamesTable.setItems(gamesData);
		gamesTable.getColumns().addAll(gameTitleCol,devNameCol, releaseDateCol, appIDCol);
 
		VBox gamesVBox = new VBox();
		gamesVBox.setSpacing(5);
		gamesVBox.setPadding(new Insets(10, 0, 0, 10));
		gamesVBox.getChildren().addAll(gamesLabel, gamesTable);
		 
		((VBox) scene.getRoot()).getChildren().addAll(gamesVBox);
		
		stage.setScene(scene);
		stage.show();
	
	}
	
	private void showMyGamesHandler() {
		ObservableList<Game> gamesData = FXCollections.observableArrayList();
		
		MyStage stage = new MyStage("My Games");
		MyScene scene;
		if (steam.isValveDev) scene = new ValveDevScene(stage);
		else if (steam.isAdmin) scene = new AdminScene(stage);
		else scene = new MyScene(stage);
		
		ArrayList<Game> gamesList = steam.query.getGamesOwned(steam.CurrentUserID);
		
		if (gamesList.isEmpty()) {steam.errorWindow("You don't have any games");;return;}
		for (int i=0; i<gamesList.size(); i++) {
			gamesData.add(gamesList.get(i));
		}
		
		Label gamesLabel = new Label("Games");
		
		TableView gamesTable = new TableView();
		
		gamesTable.setEditable(true);
		
		// GameTable Game
		TableColumn gameTitleCol = new TableColumn("Game");
		gameTitleCol.setMinWidth(300);
		gameTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
		
		// GameTable Developer
		TableColumn devNameCol = new TableColumn("Developer");
		devNameCol.setMinWidth(200);
		devNameCol.setCellValueFactory(new PropertyValueFactory<>("devName"));
		
		// GameTable Release Date
		TableColumn releaseDateCol = new TableColumn("Own Since");
		releaseDateCol.setMinWidth(100);
		releaseDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		
		// GameTable appID
		TableColumn appIDCol = new TableColumn("Game ID");
		appIDCol.setMinWidth(100);
		appIDCol.setCellValueFactory(new PropertyValueFactory<>("appID"));
		
		gamesTable.setItems(gamesData);
		gamesTable.getColumns().addAll(appIDCol, gameTitleCol, devNameCol, releaseDateCol);
 
		VBox gamesVBox = new VBox();
		gamesVBox.setSpacing(5);
		gamesVBox.setPadding(new Insets(10, 0, 0, 10));
		gamesVBox.getChildren().addAll(gamesLabel, gamesTable);
		 
		((VBox) scene.getRoot()).getChildren().addAll(gamesVBox);
		
		stage.setScene(scene);
		stage.show();
	
	}
	
	private void allGamesHandler() {
		
		ObservableList<Game> gamesData = FXCollections.observableArrayList();
		
		MyStage stage = new MyStage("Games");
		MyScene scene;
		if (steam.isValveDev) scene = new ValveDevScene(stage);
		else if (steam.isAdmin) scene = new AdminScene(stage);
		else scene = new MyScene(stage);
		
		ArrayList<Game> gamesList = steam.query.getAllGamesSQL();
		
		if (gamesList.isEmpty()) {steam.errorWindow("List is empty");return;}
		for (int i=0; i<gamesList.size(); i++) {
			gamesData.add(gamesList.get(i));
		}
		
		Label gamesLabel = new Label("Games");
		
		TableView gamesTable = new TableView();
		
		gamesTable.setEditable(true);
		
		// GameTable Game
		TableColumn gameTitleCol = new TableColumn("Game");
		gameTitleCol.setMinWidth(300);
		gameTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
		
		// GameTable Developer
		TableColumn devNameCol = new TableColumn("Developer");
		devNameCol.setMinWidth(200);
		devNameCol.setCellValueFactory(new PropertyValueFactory<>("devName"));
		
		// GameTable Release Date
		TableColumn releaseDateCol = new TableColumn("Release Date");
		releaseDateCol.setMinWidth(100);
		releaseDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		
		// GameTable appID
		TableColumn appIDCol = new TableColumn("Game ID");
		appIDCol.setMinWidth(100);
		appIDCol.setCellValueFactory(new PropertyValueFactory<>("appID"));
		
		gamesTable.setItems(gamesData);
		gamesTable.getColumns().addAll(appIDCol, gameTitleCol, devNameCol, releaseDateCol);
 
		VBox gamesVBox = new VBox();
		gamesVBox.setSpacing(5);
		gamesVBox.setPadding(new Insets(10, 0, 0, 10));
		gamesVBox.getChildren().addAll(gamesLabel, gamesTable);
		 
		((VBox) scene.getRoot()).getChildren().addAll(gamesVBox);
		
		stage.setScene(scene);
		stage.show();
	}
	
	private void friendsListHandler() {
		
		MyStage stage = new MyStage("Friends");
		
		MyScene scene;
		if (steam.isValveDev) scene = new ValveDevScene(stage);
		else if (steam.isAdmin) scene = new AdminScene(stage);
		else scene = new MyScene(stage);
		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(5);
		grid.setHgap(5);
		
		/*
		TextField userID = new TextField();
		userID.setPromptText("Enter your user ID");
		GridPane.setConstraints(userID, 0, 0);
		grid.getChildren().add(userID);
		
		Button enter = new Button("enter");
		GridPane.setConstraints(enter,1,0);
		grid.getChildren().add(enter);
		*/
		
		Label friendsLabel = new Label("Your Friends");		
		ListView<String> friendsListView = new ListView<String>();
			
		VBox friendsVBox = new VBox();
		friendsVBox.setSpacing(5);
		friendsVBox.setPadding(new Insets(10, 0, 0, 10));
		friendsVBox.getChildren().addAll(friendsLabel, friendsListView);
		((VBox) scene.getRoot()).getChildren().addAll(friendsVBox);
            
            
		//enter.setOnAction(e -> {
			ArrayList<String> friendsList = new ArrayList<String>();
			//int temp = Integer.parseInt(userID.getText());
			int temp = steam.CurrentUserID;
			friendsList = steam.query.getFriends(temp);
			
			if (friendsList.isEmpty()) {
				ObservableList<String> friendless = FXCollections.observableArrayList();
				friendless.add("NO FRIENDS :(");
				friendsListView.setItems(friendless);				
			}
			
			else {	
				ObservableList<String> friendsData = FXCollections.observableArrayList();						
				for (int i=0; i<friendsList.size(); i++) {
					friendsData.add(friendsList.get(i));
				}
				friendsListView.setItems(friendsData);				
				friendsListView.setPrefWidth(100);
				friendsListView.setPrefHeight(500);							 				
			}		
		((VBox) scene.getRoot()).getChildren().addAll(grid);//,userID,enter);
		
		stage.setScene(scene);
		stage.show();
	}

	
	private void allDevelopersHandler() {
		MyStage stage = new MyStage("Developers");
		
		MyScene scene;
		if (steam.isValveDev) scene = new ValveDevScene(stage);
		else if (steam.isAdmin) scene = new AdminScene(stage);
		else scene = new MyScene(stage);
		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(5);
		grid.setHgap(5);
		
		
		
		Button max = new Button("Most Expensive Developer");
		GridPane.setConstraints(max,1,0);
		grid.getChildren().add(max);
					
		
		Button min = new Button("Least Expensive Developer");
		GridPane.setConstraints(max,1,0);
		grid.getChildren().add(min);
		Label devName = new Label("Developer Name  Average Price");	
		Text t = new Text();
		t.setFont(new Font(20));
		t.setWrappingWidth(200);
		
        VBox devVBox = new VBox();
		devVBox.setSpacing(5);
		devVBox.setPadding(new Insets(10, 0, 0, 10));
		devVBox.getChildren().addAll(devName,t);		
		((VBox) scene.getRoot()).getChildren().addAll(devVBox,devName,t);
		
		max.setOnAction(e -> {	
			String expDev = steam.query.getMostExpensiveDev();			
			t.setText(expDev);
			t.setFont(new Font (18));
					
		});
		
		min.setOnAction(e -> {			
			String cptDev = steam.query.getCheapestDev();			
			t.setText(cptDev);
			t.setFont(new Font (18));

		});
		
		((VBox) scene.getRoot()).getChildren().addAll(grid,max,min);
		stage.setScene(scene);
		stage.show();
	}
	
	
	private void updatePasswordHandler() {
		MyStage stage = new MyStage("Update Password");
		
		MyScene scene;
		if (steam.isValveDev) scene = new ValveDevScene(stage);
		else if (steam.isAdmin) scene = new AdminScene(stage);
		else scene = new MyScene(stage);
		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(5);
		grid.setHgap(5);
		
		TextField userID = new TextField();
		userID.setPromptText("Enter your new password");
		GridPane.setConstraints(userID, 0, 0);
		grid.getChildren().add(userID);
		
	
		Button confirm = new Button("Confirm");
		GridPane.setConstraints(confirm,1,0);
		grid.getChildren().add(confirm);
		
		VBox password = new VBox();
		password.setSpacing(5);
		password.setPadding(new Insets(10, 0, 0, 10));
		password.getChildren().addAll(userID);		
		((VBox) scene.getRoot()).getChildren().addAll(password);
		
		confirm.setOnAction(e -> {
			String temp = userID.getText();
			if(temp.equals("")){
				steam.errorWindow("password cannot be null");
			}
			else if(!steam.query.changePassword(steam.CurrentUserID,temp)) {steam.errorWindow("Something is Wrong");}	
			else steam.errorWindow("Password has been changed!");
			});
		
        ((VBox) scene.getRoot()).getChildren().addAll(grid,userID,confirm);
		
		stage.setScene(scene);
		stage.show();
	}
	


	private void getGameChampions() {
		
		
        MyStage stage = new MyStage("Champions");
        MyScene scene;
		if (steam.isValveDev) scene = new ValveDevScene(stage);
		else if (steam.isAdmin) scene = new AdminScene(stage);
		else scene = new MyScene(stage);
		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(5);
		grid.setHgap(5);
		
		Label playerLabel = new Label("Players");
		TableView playersTable = new TableView();
		
		// GameTable Game
		TableColumn playerTitleCol = new TableColumn("Player");
		playerTitleCol.setMinWidth(300);
		playerTitleCol.setCellValueFactory(new PropertyValueFactory<>("name"));
					
	    // GameTable Developer
		TableColumn emailCol = new TableColumn("Email");
		emailCol.setMinWidth(200);
		emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
					
		// GameTable Release Date
		TableColumn lvlCol = new TableColumn("User Level");
		lvlCol.setMinWidth(100);
		lvlCol.setCellValueFactory(new PropertyValueFactory<>("userLvl"));
					
		// GameTable appID
		TableColumn playerIDCol = new TableColumn("Player ID");
		playerIDCol.setMinWidth(100);
		playerIDCol.setCellValueFactory(new PropertyValueFactory<>("playerID"));
					
		playersTable.getItems().clear();
		
					
		playersTable.getColumns().addAll(playerIDCol, playerTitleCol, emailCol, lvlCol); 
					
			//});
				
		VBox playersVBox = new VBox();
		playersVBox.setSpacing(5);
		playersVBox.setPadding(new Insets(10, 0, 0, 10));
		playersVBox.getChildren().addAll(playerLabel, playersTable);
				 
				
		
        playersTable.setEditable(true);
		
        TextField userID = new TextField();
		userID.setPromptText("enter appID here");
		GridPane.setConstraints(userID, 0, 0);
		grid.getChildren().add(userID);
		
	
		Button confirm = new Button("Confirm");
		GridPane.setConstraints(confirm,1,0);
		grid.getChildren().add(confirm);
		
		
		confirm.setOnAction(e -> {
			ObservableList<Player> playersData = FXCollections.observableArrayList();
			int temp = Integer.parseInt(userID.getText());
			ArrayList<Player> playerList = steam.query.getGameChampion(temp);	
			
			for (int i=0; i<playerList.size(); i++) {
				playersData.add(playerList.get(i));
			}
			playerList.clear();
			playersTable.setItems(playersData);
		});
					
			
		((VBox) scene.getRoot()).getChildren().addAll(playersVBox,userID,confirm);
		stage.setScene(scene);
		stage.show();

	}

	public void logOutHandler() {
		steam.isAdmin=false;
		steam.isValveDev=false;
		MyStage stage = new MyStage("Welcome!");
		LoginScene scene = new LoginScene(stage);
		currentStage.close();
		stage.setScene(scene);
		stage.show();
	}
	
	public void addFriendHandler() {
		MyStage stage = new MyStage("Friends");
		MyScene scene;
		if (steam.isValveDev) scene = new ValveDevScene(stage);
		else if (steam.isAdmin) scene = new AdminScene(stage);
		else scene = new MyScene(stage);
		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(5);
		grid.setHgap(5);
		
		TextField userID = new TextField();
		userID.setPromptText("Enter playerID");
		GridPane.setConstraints(userID, 0, 0);
		grid.getChildren().add(userID);
		
	
		Button search = new Button("search");
		GridPane.setConstraints(search,1,0);
		grid.getChildren().add(search);
		
		search.setOnAction(e -> {
			try {
				int temp = Integer.parseInt(userID.getText());
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
				long time = cal.getTimeInMillis();
		        Date dateDate = new Date(time);
		        String date = new SimpleDateFormat("dd-MM-yyyy").format(dateDate);
		        if (!steam.query.addFriend(date, steam.CurrentUserID, Integer.parseInt(userID.getText()))) {steam.errorWindow("Can't add friend"); return;};
		        steam.query.addFriend(date, Integer.parseInt(userID.getText()), steam.CurrentUserID);
		        steam.errorWindow("You are now friends with player "+steam.query.getPlayerName(Integer.parseInt(userID.getText())));
			} catch (Exception v)
			{
				steam.errorWindow("Should be a number");
			}
			
		});
		
        ((VBox) scene.getRoot()).getChildren().addAll(grid);
		
		stage.setScene(scene);
		stage.show();
	}
	
	private void deleteFriendHandler() {
		MyStage stage = new MyStage("Delete Friend");
		MyScene scene;
		if (steam.isValveDev) scene = new ValveDevScene(stage);
		else if (steam.isAdmin) scene = new AdminScene(stage);
		else scene = new MyScene(stage);
		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(5);
		grid.setHgap(5);
		
		TextField userID = new TextField();
		userID.setPromptText("Enter your playerID to delete");
		GridPane.setConstraints(userID, 0, 0);
		grid.getChildren().add(userID);
		
	
		Button confirm = new Button("Confirm");
		GridPane.setConstraints(confirm,1,0);
		grid.getChildren().add(confirm);
		
		VBox friend = new VBox();
		friend.setSpacing(5);
		friend.setPadding(new Insets(10, 0, 0, 10));
		friend.getChildren().addAll(userID);		
		((VBox) scene.getRoot()).getChildren().addAll(friend);
		
		confirm.setOnAction(e -> {	
			try {
				int temp = Integer.parseInt(userID.getText());
				if(!steam.query.deleteFriends(steam.CurrentUserID, temp))
					steam.errorWindow("playerID must be a valid number");
				else steam.errorWindow("this friend has been deleted");
			} catch (Exception v)
			{
				steam.errorWindow("Should be a number");
			}
				
		});
		
		
		((VBox) scene.getRoot()).getChildren().addAll(grid, userID,confirm);
		stage.setScene(scene);
		stage.show();
		
	}
	
	private void getViewHandler() {
		
		MyStage stage = new MyStage("Friends");
		
		MyScene scene;
		if (steam.isValveDev) scene = new ValveDevScene(stage);
		else if (steam.isAdmin) scene = new AdminScene(stage);
		else scene = new MyScene(stage);
		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(5);
		grid.setHgap(5);
		
		
		Label friendsLabel = new Label("Developer, Average Price");		
		ListView<String> friendsListView = new ListView<String>();
			
		VBox friendsVBox = new VBox();
		friendsVBox.setSpacing(5);
		friendsVBox.setPadding(new Insets(10, 0, 0, 10));
		friendsVBox.getChildren().addAll(friendsLabel, friendsListView);
		((VBox) scene.getRoot()).getChildren().addAll(friendsVBox);
            
   
			ArrayList<String> friendsList = new ArrayList<String>();

			
			friendsList = steam.query.getView();
			
			if (friendsList.isEmpty()) {
				ObservableList<String> friendless = FXCollections.observableArrayList();
				friendless.add("NO VIEW :(");
				friendsListView.setItems(friendless);				
			}
			
			else {	
				ObservableList<String> friendsData = FXCollections.observableArrayList();						
				for (int i=0; i<friendsList.size(); i++) {
					friendsData.add(friendsList.get(i));
				}
				friendsListView.setItems(friendsData);				
				friendsListView.setPrefWidth(100);
				friendsListView.setPrefHeight(500);							 				
			}		
		((VBox) scene.getRoot()).getChildren().addAll(grid);
		stage.setScene(scene);
		stage.show();
	}
	
	
}

