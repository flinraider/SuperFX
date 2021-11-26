package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.exceptions.IOFXException;

public class Program extends Application {
	
	@Override
	public void start(Stage stage) throws Exception {
		
		try {
			Parent vc = (AnchorPane)FXMLLoader.load(getClass().getResource("/gui/View.fxml"));
			stage.setScene(new Scene(vc));
			stage.setTitle("View");
			stage.show();
		} catch (IOException e) {
			throw new IOFXException(e.getMessage());
		}
		
	}
	
	public static void main(String[] args) {
	   launch(args);	
	}

}
