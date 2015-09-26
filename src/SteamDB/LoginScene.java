package SteamDB;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginScene extends Scene {

	private SteamMenu steam;
	private int WIDTH;
	private int HEIGHT;
	private Stage currentStage;
	private boolean logged;
	
	public LoginScene(Stage s, int w, int h) {
		super(new VBox(), w, h);
		this.setFill(Color.DARKSALMON);
		
		currentStage = s;
		logged = false;
		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(5);
		grid.setHgap(5);
		
		// WE CAN USE PLAYERID INSTEAD OF EMAIL IF EMAIL WONT WORK OUT
		Label log = new Label("Login");
		GridPane.setConstraints(log, 3, 2);
		grid.getChildren().add(log);
		
		TextField email = new TextField();
		email.setPromptText("Enter your email");
		GridPane.setConstraints(email, 3, 3);
		grid.getChildren().add(email);
		
		TextField pass = new TextField();
		pass.setPromptText("Enter your password");
		GridPane.setConstraints(pass, 3, 4);
		grid.getChildren().add(pass);
		
		Button login = new Button("login");
		GridPane.setConstraints(login,3,5);
		grid.getChildren().add(login);
		
		//
		Label reg = new Label("Sign up");
		GridPane.setConstraints(reg, 8, 2);
		grid.getChildren().add(reg);
		
		TextField name = new TextField();
		name.setPromptText("Enter your name");
		GridPane.setConstraints(name, 8, 3);
		grid.getChildren().add(name);
		
		TextField emailSign = new TextField();
		emailSign.setPromptText("Enter your email");
		GridPane.setConstraints(emailSign, 8, 4);
		grid.getChildren().add(emailSign);
		
		TextField passSign = new TextField();
		passSign.setPromptText("Enter your password");
		GridPane.setConstraints(passSign, 8,5);
		grid.getChildren().add(passSign);
		
		Button register = new Button("register");
		GridPane.setConstraints(register, 8, 6);
		grid.getChildren().add(register);
		
		login.setOnAction(e -> {loginHandler(email, pass); e.consume();});
		register.setOnAction(e -> {registerHandler(name, emailSign, passSign); e.consume();});
		
		((VBox) this.getRoot()).getChildren().addAll(grid);
 
	}
	
	public LoginScene(Stage s) {
		this(s, 700,350);
	}
	
	public void registerHandler(TextField nameField, TextField emailField, TextField passField) {
		String name = nameField.getText();
		
		String email = emailField.getText();
		if (name.isEmpty()) steam.errorWindow("Password field is empty!");
		if (email.isEmpty()) steam.errorWindow("Email field is empty!");
		Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		Matcher matcher = pattern.matcher(email);
		if (!matcher.matches()) {steam.errorWindow("Enter valid email"); return;}
		if (email.isEmpty()) steam.errorWindow("Password field is empty!");
		
		String pass = passField.getText();
		int maxPlayerID = steam.query.getMaxPlayerID();
		if (maxPlayerID == -1) {steam.errorWindow("Can't Register");return;}
		if (!steam.query.addPlayer(maxPlayerID+1, name, email, pass)){steam.errorWindow("Unable to register");}
		else steam.errorWindow("Thanks for Sign up!");
	}
		
	public void loginHandler(TextField email, TextField pass) {
		String tempPass = pass.getText();
		String tempEmail = email.getText();
		if (tempPass.isEmpty()) {steam.errorWindow("Password field is empty!");return;}
		if (tempEmail.isEmpty()) {steam.errorWindow("Email field is empty!");return;}
		HashMap<String, String> comb = steam.query.getCombinations();
		if (comb.isEmpty())	steam.errorWindow("Can't acquire user credentials");
		for (Player p: steam.query.getBannedPlayer()) {
			if (tempEmail.equals(p.getEmail())) {
				steam.errorWindow("We are sorry but you are banned from Steam");
				return;
			}
		}
		
		if (comb.containsKey(email.getText()) && comb.containsValue(pass.getText()) && comb.get(email.getText()).equals(pass.getText())) {
			if  (steam.query.chckValveDev(tempEmail, tempPass) > 0) { steam.errorWindow("WELCOME DEV"); valveDevHandler(); steam.isValveDev=true;}
			else if (steam.query.chckAdmin(tempEmail, tempPass) > 0) { steam.errorWindow("WELCOME ADMIN"); adminHandler(); steam.isAdmin=true;}
			else {steam.errorWindow("WELCOME"); loginHandler();}
			steam.CurrentUserID = steam.query.chckPlayer(tempEmail, tempPass);
			((VBox) this.getRoot()).getChildren().clear();
			currentStage.close();
		} else steam.errorWindow("Login not found");
	}
	
	private void valveDevHandler() {
		MyStage stage = new MyStage("Steam: Valve");
		MyScene scene = new ValveDevScene(stage);
		
		Image steam = new Image("http://i.kinja-img.com/gawker-media/image/upload/s--yI_V5S0Q--/c_fit,fl_progressive,q_80,w_636/aqmpcu0fslz0n2qly5ny.jpg");
		ImageView steamView = new ImageView();
		steamView.setImage(steam);
		
		((VBox) scene.getRoot()).getChildren().addAll(steamView);
		stage.setScene(scene);
		stage.show();
	}
	
	private void adminHandler() {
		MyStage stage = new MyStage("Steam: Admin");
		MyScene scene = new AdminScene(stage);
		
		Image steam = new Image("http://cdn.wegotthiscovered.com/wp-content/uploads/Steam-Logo.jpg");
		ImageView steamView = new ImageView();
		steamView.setImage(steam);
		
		((VBox) scene.getRoot()).getChildren().addAll(steamView);
		stage.setScene(scene);
		stage.show();
	}
	
	private void loginHandler() {
		MyStage stage = new MyStage("Steam");
		Scene scene = new MyScene(stage);
		
		Image steam = new Image("https://i.ytimg.com/vi/PQQbu10cU_4/hqdefault.jpg");
		ImageView steamView = new ImageView();
		steamView.setImage(steam);
		
		((VBox) scene.getRoot()).getChildren().addAll(steamView);
		logged = true;
		currentStage.close();
		stage.setScene(scene);
		stage.show();
	}
	
	public boolean getLogged() {
		return logged;
	}

	
	
}
