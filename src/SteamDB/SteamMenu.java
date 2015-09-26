package SteamDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;



import java.util.HashMap;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;import javafx.util.Callback;



public class SteamMenu extends Application {
	
	private MyStage FirstStage;
	public static Connection con;
	public static Queries query = new Queries();
	protected static int CurrentUserID;
	protected static boolean isValveDev=false;
	protected static boolean isAdmin=false;
	
	
	private static void openDriver() throws SQLException {
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
	}
	
	public static void connect(String userid, String password) throws SQLException {
		con = DriverManager.getConnection("jdbc:oracle:thin:@dbhost.ugrad.cs.ubc.ca:1522:ug", "ora_"+userid, 'a'+password);

	}
	
	
	public void disconnect() throws SQLException
	{
		con.close();
	}
	
	@Override
	public void start(Stage stage) {

		LoginScene scene = new LoginScene(stage);
		if (!scene.getLogged()) stage.show();
		else stage.close();
        stage.setScene(scene);
       
	}
	
	protected static final void errorWindow(String message) {
		Stage stage = new Stage();
		Scene scene = new Scene(new VBox(), 300, 300);
		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(5);
		grid.setHgap(5);
		
		scene.setFill(Color.DARKSALMON);
		Label error = new Label(message);
		
		GridPane.setConstraints(error, 6 ,6);
		grid.getChildren().add(error);
		((VBox) scene.getRoot()).getChildren().addAll(grid);
		stage.setScene(scene);
		stage.show();
	}
	

	
	

	public void launch() {
		FirstStage = new MyStage("Welcome!");
		this.start(FirstStage);
	}
	
	public static void main(String[] args) {
		try {
			openDriver();
			connect("0000","000000"); /* ubc cs id, ubc student id */
		} catch(SQLException e){
			System.out.println("Can't connect to the server");
		}
		launch(args);

	}
}