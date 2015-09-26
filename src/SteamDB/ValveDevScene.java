package SteamDB;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ValveDevScene extends AdminScene {
	
	
	public ValveDevScene(MyStage s) {
		super(s);
		Menu menuValve = new Menu("Valve");
		MenuItem removeAdmin = new MenuItem("God Powers"); 
		MenuItem addGame = new MenuItem("Add New Game");
		MenuItem banGame = new MenuItem("Ban Games");
		removeAdmin.setOnAction(e -> removeAdminHandler());
		addGame.setOnAction(e -> addNewGameHandler());
		banGame.setOnAction(e -> banGameHandler());
		
		menuValve.getItems().addAll(removeAdmin,addGame,banGame);
		
		menuBar.getMenus().addAll(menuValve);
	}

	private void banGameHandler() {
		MyStage stage = new MyStage("Moderation");
		MyScene scene;
		if (steam.isValveDev) scene = new ValveDevScene(stage);
		else if (steam.isAdmin) scene = new AdminScene(stage);
		else scene = new MyScene(stage);
		
		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(5);
		grid.setHgap(5);
		
		Label revoke = new Label();
		revoke.setText("Ban Game");
		GridPane.setConstraints(revoke, 0 ,0);
		grid.getChildren().add(revoke);
		
		Button ban = new Button("Ban");
		GridPane.setConstraints(ban, 25, 15);
		grid.getChildren().add(ban);
		
		TextField ID = new TextField();
		ID.setPromptText("Enter appID to ban");
		GridPane.setConstraints(ID, 25, 15);
		grid.getChildren().add(ID);
		
		ban.setOnAction(e -> {
			try{
				int temp = Integer.parseInt(ID.getText());
				int max = steam.query.getMaxAppID();
				if(temp> max || temp < 1){
					steam.errorWindow("appID must be a valid number");
				}
				else{
				if (!steam.query.banGame(temp)) steam.errorWindow("Unable to ban the game");
				else steam.errorWindow("this game is banned");
				}
				
			}
			
			catch(Exception message){
				steam.errorWindow("appID must be a valid number");
			}
		});
		
        ((VBox) scene.getRoot()).getChildren().addAll(grid,ban);
		
		stage.setScene(scene);
		stage.show();
		
	}

	private void addNewGameHandler() {
		
		MyStage stage = new MyStage("Moderation");
		MyScene scene;
		if (steam.isValveDev) scene = new ValveDevScene(stage);
		else if (steam.isAdmin) scene = new AdminScene(stage);
		else scene = new MyScene(stage);
		
		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(5);
		grid.setHgap(5);
		
		Label revoke = new Label();
		revoke.setText("Add New Game");
		GridPane.setConstraints(revoke, 0 ,0);
		grid.getChildren().add(revoke);
		
		Button add = new Button("Add");
		GridPane.setConstraints(add, 25, 15);
		grid.getChildren().add(add);
		
		TextField genre = new TextField();
		genre.setPromptText("Enter genre");
		GridPane.setConstraints(genre, 25, 12);
		grid.getChildren().add(genre);
		
		TextField price = new TextField();
		price.setPromptText("Enter price");
		GridPane.setConstraints(price, 25, 12);
		grid.getChildren().add(price);
		
		TextField devName = new TextField();
		devName.setPromptText("Enter developer name");
		GridPane.setConstraints(devName, 25, 12);
		grid.getChildren().add(devName);
		
		TextField title = new TextField();
		title.setPromptText("Enter title");
		GridPane.setConstraints(title, 25, 12);
		grid.getChildren().add(title);
		
		add.setOnAction(e -> {
			
			try{
			String gen = genre.getText();
			Float pri = Float.parseFloat(price.getText());
			String t = title.getText();
			String dev = devName.getText();
			int appID = steam.query.getMaxAppID() + 1;
			System.out.println(appID);
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			long time = cal.getTimeInMillis();
	        Date dateDate = new Date(time);
	        String date = new SimpleDateFormat("dd-MM-yyyy").format(dateDate);
	        
	        if (!steam.query.addGame(appID, gen, pri, t, date, dev)) {steam.errorWindow("Unable to add game");}
	        else {steam.errorWindow("Game added succesfully");stage.close();}
			}
			catch(Exception ex){
				steam.errorWindow("price must be a number");
			}
			
			
			
		});
		
        ((VBox) scene.getRoot()).getChildren().addAll(grid, add, title,devName, price, genre);
		
		stage.setScene(scene);
		stage.show();
		

	}

	private void removeAdminHandler() {
		MyStage stage = new MyStage("Moderation");
		MyScene scene;
		if (steam.isValveDev) scene = new ValveDevScene(stage);
		else if (steam.isAdmin) scene = new AdminScene(stage);
		else scene = new MyScene(stage);
		
		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(5);
		grid.setHgap(5);
		
		Label revoke = new Label();
		revoke.setText("Revoke admin permissions");
		GridPane.setConstraints(revoke, 0 ,0);
		grid.getChildren().add(revoke);
		
		Button enter = new Button("REVOKE");
		GridPane.setConstraints(enter, 25, 15);
		grid.getChildren().add(enter);
		
		TextField adminID = new TextField();
		adminID.setPromptText("Enter playerID");
		GridPane.setConstraints(adminID, 25, 12);
		grid.getChildren().add(adminID);
		
           
		enter.setOnAction(e -> {
			try {
				int temp = Integer.parseInt(adminID.getText());
				if(!steam.query.deleteAdmin(temp)) {steam.errorWindow("adminID must be valid");}
				else steam.errorWindow("REVOKE IS SUCCESS");
			} catch(Exception v)
			{
				steam.errorWindow("Should be a number");
			}
			
		});
		
		((VBox) scene.getRoot()).getChildren().addAll(grid, enter, adminID);
		
		stage.setScene(scene);
		stage.show();
			
	}
}
