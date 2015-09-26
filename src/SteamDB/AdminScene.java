package SteamDB;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;




import java.util.Calendar;
import java.util.TimeZone;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminScene extends MyScene {
	
	public AdminScene(MyStage s) {
		super(s);
		Menu menuAdmin = new Menu("Admin");
		MenuItem showGoodPlayers = new MenuItem("Good Players"); 
		MenuItem banPlayer = new MenuItem("Ban Player");
		banPlayer.setOnAction(e -> banPlayerHandler());
		showGoodPlayers.setOnAction(e -> showGoodPlayersHandler());
		menuAdmin.getItems().addAll(showGoodPlayers,banPlayer);
		
		menuBar.getMenus().addAll(menuAdmin);
	}
	
	private void showGoodPlayersHandler() {
		ObservableList<Player> playersData = FXCollections.observableArrayList();
		
		MyStage stage = new MyStage("Good Players");
		MyScene scene;
		if (steam.isValveDev) scene = new ValveDevScene(stage);
		else if (steam.isAdmin) scene = new AdminScene(stage);
		else scene = new MyScene(stage);
		
		ArrayList<Player> playersList = steam.query.getNotBannedPlayer();
		
		if (playersList.isEmpty()) return;
		for (int i=0; i<playersList.size(); i++) {
			playersData.add(playersList.get(i));
		}
		
		Label playersLabel = new Label("Players");
		
		TableView playersTable = new TableView();
		
		playersTable.setEditable(true);
		
		TableColumn playerIDCol = new TableColumn("PlayerID");
		playerIDCol.setMinWidth(300);
		playerIDCol.setCellValueFactory(new PropertyValueFactory<>("playerID"));
		
		TableColumn nameCol = new TableColumn("Name");
		nameCol.setMinWidth(200);
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		TableColumn emailCol = new TableColumn("Email");
		emailCol.setMinWidth(100);
		emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
		
		TableColumn levelCol = new TableColumn("Level");
		levelCol.setMinWidth(100);
		levelCol.setCellValueFactory(new PropertyValueFactory<>("userLvl"));
		
		playersTable.setItems(playersData);
		playersTable.getColumns().addAll(playerIDCol, nameCol, emailCol, levelCol);
 
		VBox gamesVBox = new VBox();
		gamesVBox.setSpacing(5);
		gamesVBox.setPadding(new Insets(10, 0, 0, 10));
		gamesVBox.getChildren().addAll(playersLabel, playersTable);
		 
		((VBox) scene.getRoot()).getChildren().addAll(gamesVBox);
		
		stage.setScene(scene);
		stage.show();
	}
	
	private void banPlayerHandler() {
		MyStage stage = new MyStage("Ban Editor");
		MyScene scene;
		if (steam.isValveDev) scene = new ValveDevScene(stage);
		else if (steam.isAdmin) scene = new AdminScene(stage);
		else scene = new MyScene(stage);
		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(5);
		grid.setHgap(5);
		
		Label ban = new Label();
		ban.setText("Ban Player");
		GridPane.setConstraints(ban, 0 ,0);
		grid.getChildren().add(ban);
		
		Button enter = new Button("BAN");
		GridPane.setConstraints(enter, 25, 30);
		grid.getChildren().add(enter);
		
		TextField userID = new TextField();
		userID.setPromptText("Enter playerID");
		GridPane.setConstraints(userID, 25, 25);
		grid.getChildren().add(userID);
		
		TextField reason = new TextField();
		reason.setPromptText("Enter reason");
		GridPane.setConstraints(reason, 25, 27);
		grid.getChildren().add(reason);
		
           
		enter.setOnAction(e -> {
			try {
			int temp = Integer.parseInt(userID.getText());
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			long time = cal.getTimeInMillis();
	        Date dateDate = new Date(time);
	        String date = new SimpleDateFormat("dd-MM-yyyy").format(dateDate);
	        if (steam.isAdmin=true) {
				for (int i: steam.query.getAllValveID())
					if (temp == i) {
						steam.errorWindow("Not enough permissions");
						return;
					}
			}
			if (!steam.query.banPlayer(temp, steam.CurrentUserID, date, reason.getText())) {steam.errorWindow("Unable to ban");}
			else steam.errorWindow("Ban is successful");
			}catch(Exception v)
			{
				steam.errorWindow("Should be a number");
			}
		});
		
		((VBox) scene.getRoot()).getChildren().addAll(grid);//,userID,enter);
		
		stage.setScene(scene);
		stage.show();
		
	}


}
