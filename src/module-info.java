/**
 * 
 */
/**
 * @author Kerim
 *
 */
module Contacts {
	exports com.rimberse.contacts;
	exports com.rimberse.contacts.datamodel;

	requires javafx.base;
	requires javafx.fxml;
	requires javafx.controls;
	requires java.xml;
	requires transitive javafx.graphics;
	
	opens com.rimberse.contacts;
	opens com.rimberse.contacts.datamodel;
}