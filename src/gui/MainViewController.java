package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Program;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;

/**
 * @author leonardo
 *
 */
public class MainViewController implements Initializable{

	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("Seller");
	}
	
	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml");
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml");
	}
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * O syncronized garante que este processamento 
	 * ira funcionar sem parar ocorrendo um erro inesperado
	 * as view trabalho em mult thread 
	 */	
	private synchronized void loadView(String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVbox = loader.load();
			
			Scene mainScene = Program.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVbox.getChildren());
			
		} catch (IOException e) {
			Alerts.showAlerts("IOExeception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

}
