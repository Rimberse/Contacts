package com.rimberse.contacts.datamodel;

import javafx.beans.property.SimpleStringProperty;

public class Contact {
	public String getFirstName() {
		return firstName.getValue();
	}
	
	public SimpleStringProperty firstNameProperty() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName.setValue(firstName);
	}

	public String getLastName() {
		return lastName.getValue();
	}
	
	public SimpleStringProperty lastNameProperty() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName.setValue(lastName);
	}

	public String getPhoneNumber() {
		return phoneNumber.getValue();
	}
	
	public SimpleStringProperty phoneNumberProperty() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber.setValue(phoneNumber);
	}

	public String getNotes() {
		return notes.getValue();
	}
	
	public SimpleStringProperty notesProperty() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes.setValue(notes);
	}

	private SimpleStringProperty firstName;
	private SimpleStringProperty lastName;
	private SimpleStringProperty phoneNumber;
	private SimpleStringProperty notes;
	
	public Contact() {
		this("First Name", "Last Name", "Phone Number", "Notes");
	}
	
	public Contact(String firstName, String lastName, String phoneNumber, String notes) {
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.phoneNumber = new SimpleStringProperty(phoneNumber);
		this.notes = new SimpleStringProperty(notes);
	}

	@Override
	public String toString() {
		return "Contact [firstName=" + firstName + ", lastName=" + lastName + ", phoneNumber=" + phoneNumber
				+ ", notes=" + notes + "]";
	}
}