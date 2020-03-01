package com.rimberse.contacts;

import java.io.IOException;
import java.util.Optional;

import com.rimberse.contacts.datamodel.Contact;
import com.rimberse.contacts.datamodel.ContactData;

import javafx.application.Platform;
//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

public class Controller {
	@FXML
	private TableView<Contact> contactsTable;
	@FXML
	private BorderPane mainBorderPane;
	@FXML
	private TableColumn<Contact, String> firstNameColumn;
	@FXML
	private TableColumn<Contact, String> lastNameColumn;
	@FXML
	private TableColumn<Contact, String> phoneNumberColumn;
	@FXML
	private TableColumn<Contact, String> notesColumn;
	
	public void initialize() {
		contactsTable.setItems(ContactData.getInstance().getContacts());
		contactsTable.getSelectionModel().selectFirst();
	}
	
	@FXML
	public void showAddContactDialog() {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.initOwner(mainBorderPane.getScene().getWindow());
		dialog.setTitle("Add New Contact");
		dialog.setHeaderText("Use this dialog to add a new Contact");
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getClassLoader().getResource("ContactDialogUI.fxml"));
		
		try {
			dialog.getDialogPane().setContent(fxmlLoader.load());
		} catch(IOException exception) {
			System.out.println("Couldn't load the dialog");
			exception.printStackTrace();
			return;
		}
		dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
		
		Optional<ButtonType> result = dialog.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			DialogController controller = fxmlLoader.getController();
			Contact newContact = controller.getNewContact();
			contactsTable.getSelectionModel().select(newContact);
		}
	}
	
	@FXML
	public void showEditContactDialog() {
		Contact existingContact = contactsTable.getSelectionModel().getSelectedItem();
		if (existingContact == null) {
			Alert selectionAlert = new Alert(AlertType.INFORMATION);
			selectionAlert.setTitle("No Contact Selected");
			selectionAlert.setHeaderText(null);
			selectionAlert.setContentText("Please select the contact you want to edit.");
			selectionAlert.showAndWait();
			return;
		}
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.initOwner(mainBorderPane.getScene().getWindow());
		dialog.setTitle("Edit existing Contact");
		dialog.setHeaderText("Use this dialog to edit existing Contact");
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getClassLoader().getResource("ContactDialogUI.fxml"));
		
		try {
			dialog.getDialogPane().setContent(fxmlLoader.load());
		} catch(IOException exception) {
			System.out.println("Couldn't load the dialog");
			exception.printStackTrace();
			return;
		}
		dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
		
		DialogController controller = fxmlLoader.getController();
		controller.getCurrentValues(existingContact);
		
		Optional<ButtonType> result = dialog.showAndWait();
		String information = null;
		if (result.isPresent() && result.get() == ButtonType.OK) {
			information = controller.getExistingContact(existingContact);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Edit Contact");
			alert.setHeaderText("Contact was succesfully updated");
			alert.setContentText(information);
			Optional<ButtonType> confirmation = alert.showAndWait();
			if (confirmation.isPresent() && confirmation.get() == ButtonType.OK) {
				contactsTable.refresh();
				contactsTable.getSelectionModel().select(existingContact);
			}
		}
	}
	
	@FXML
	public void deleteContact() {
		Contact selectedContact = contactsTable.getSelectionModel().getSelectedItem();
		if (selectedContact == null) {
			Alert selectionAlert = new Alert(AlertType.INFORMATION);
			selectionAlert.setTitle("No Contact Selected");
			selectionAlert.setHeaderText(null);
			selectionAlert.setContentText("Please select the contact you want to delete.");
			selectionAlert.showAndWait();
			return;
		}
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Contact");
		alert.setHeaderText("Detele contact: " + selectedContact.getFirstName() + " " + selectedContact.getLastName());
		alert.setContentText("Are you sure? Press OK to confirm, or Cancel to back out.");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			ContactData.getInstance().deleteContact(selectedContact);
		}
	}
	
	@FXML
	public void exit() {
		Platform.exit();
	}
}