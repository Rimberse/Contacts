package com.rimberse.contacts;

import com.rimberse.contacts.datamodel.Contact;
import com.rimberse.contacts.datamodel.ContactData;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class DialogController {
	@FXML
	private TextField firstNameField;
	@FXML
	private TextField lastNameField;
	@FXML
	private TextField phoneNumberField;
	@FXML
	private TextField notesField;
	
	public Contact getNewContact() {
		String firstName = firstNameField.getText().trim();
		String lastName = lastNameField.getText().trim();
		String phoneNumber = phoneNumberField.getText().trim();
		String notes = notesField.getText().trim();
		
		Contact newContact = new Contact(firstName, lastName, phoneNumber, notes);
		ContactData.getInstance().addContact(newContact);
		return newContact;
	}
	
	public String getExistingContact(Contact contact) {
		String firstName = "";														// Basic check to prevent information alert from showing all the values,
		String lastName = "";														// which we display using getCurrentValues() below. Nothing
		String phoneNumber = "";													// important, just came up with this to make it look nice.
		String notes = "";															// Without it, alert shows wrong information like this:
		if (!contact.getFirstName().equals(firstNameField.getText().trim()))		// Edited: <something> to <something>, which of course isn't true
			firstName = firstNameField.getText().trim();							// we only want it to show: Edited: <something> to <anotherThing>.
		if (!contact.getLastName().equals(lastNameField.getText().trim()))
			lastName = lastNameField.getText().trim();
		if (!contact.getPhoneNumber().equals(phoneNumberField.getText().trim()))
			phoneNumber = phoneNumberField.getText().trim();
		if (!contact.getNotes().equals(notesField.getText().trim()))
			notes = notesField.getText().trim();
		
		return ContactData.getInstance().editContact(contact, firstName, lastName, phoneNumber, notes);
	}
	
	public void getCurrentValues(Contact contact) {
		firstNameField.setText(contact.getFirstName());
		lastNameField.setText(contact.getLastName());
		phoneNumberField.setText(contact.getPhoneNumber());
		notesField.setText(contact.getNotes());
	}
}