package SteamDB;

import javafx.stage.Stage;

public class MyStage extends Stage {

	private static final double MINIMUM_WINDOW_WIDTH = 390.0;
	private static final double MINIMUM_WINDOW_HEIGHT = 500.0;
	
	public MyStage(String title) {
		super();
		this.setTitle(title);
		this.setMinWidth(MINIMUM_WINDOW_WIDTH);
        this.setMinHeight(MINIMUM_WINDOW_HEIGHT);
	}
	
}
