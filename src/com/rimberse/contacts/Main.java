package com.rimberse.contacts;

import com.rimberse.contacts.datamodel.ContactData;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	@Override
	public void init() throws Exception {
		ContactData.getInstance().loadContacts();			// load contacts from XML here or in an initialization() method of a Controller
	}

	@Override
	public void stop() throws Exception {
		ContactData.getInstance().saveContacts();			// same here
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("MainWindowUI.fxml"));
			Scene scene = new Scene(root,900,500);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("My Contacts");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
