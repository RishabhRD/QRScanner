package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;



public class Main extends Application {
	@Override
	public void start(Stage stage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("Home.fxml"));
			Scene scene = new Scene(root,root.getMaxWidth(),root.getMaxHeight());
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.setTitle("QR Code Reader/Writer");	
			stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.jpg")));
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
