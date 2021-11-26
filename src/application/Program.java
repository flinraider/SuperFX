package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.exceptions.IOFXException;

public class Program extends Application {
	
	private static Scene mainScene;
	
	@Override
	public void start(Stage stage) throws Exception {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			ScrollPane scrollPane = loader.load(); 
			
			scrollPane.setFitToHeight(true);
			scrollPane.setFitToWidth(true);
			mainScene = new Scene(scrollPane);
			stage.setScene(mainScene);
			stage.setTitle("SuperFX");
			stage.show();
		} catch (IOException e) {
			throw new IOFXException(e.getMessage());
		}
		
	}
	
	public static Scene getMainScene() {
		return mainScene;
	}
	
	public static void main(String[] args) {
	   launch(args);	
	}

}
