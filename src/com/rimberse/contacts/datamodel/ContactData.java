package com.rimberse.contacts.datamodel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ContactData {
    public ObservableList<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(ObservableList<Contact> contacts) {
		this.contacts = contacts;
	}

	public static ContactData getInstance() {
		return instance;
	}
    
    public void addContact(Contact contact) {
    	contacts.add(contact);
    }
    
    public void deleteContact(Contact contact) {
    	contacts.remove(contact);
    }
    
    public String editContact(Contact contact, String firstName, String lastName, String phoneNumber, String notes) {	
		int index = contacts.indexOf(contact);
		String oldFirstName = contacts.get(index).getFirstName();
		String oldLastName = contacts.get(index).getLastName();
		String oldPhoneNumber = contacts.get(index).getPhoneNumber();
		String oldNotes = contacts.get(index).getNotes();
		StringBuilder sb = new StringBuilder("Edited:\n\t");
		if (!firstName.isEmpty()) {
			contacts.get(index).setFirstName(firstName);
			sb.append("First Name: <" + oldFirstName + "> to <" + contacts.get(index).getFirstName() + ">\n\t");
		}
		if (!lastName.isEmpty()) {
			contacts.get(index).setLastName(lastName);
			sb.append("Last Name: <" + oldLastName + "> to <" + contacts.get(index).getLastName() + ">\n\t");
		}
		if (!phoneNumber.isEmpty()) {
			contacts.get(index).setPhoneNumber(phoneNumber);
			sb.append("Phone Number: <" + oldPhoneNumber + "> to <" + contacts.get(index).getPhoneNumber() + ">\n\t");
		}
		if (!notes.isEmpty()) {
			contacts.get(index).setNotes(notes);
			sb.append("Notes: <" + oldNotes + "> to <" + contacts.get(index).getNotes() + ">");
		}
		return sb.toString();
	}
    
    private ContactData() {
        contacts = FXCollections.observableArrayList();
    }

	private static final String CONTACTS_FILE = "contacts.xml";

    private static final String CONTACT = "contact";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String PHONE_NUMBER = "phone_number";
    private static final String NOTES = "notes";

    private static ContactData instance = new ContactData();
    private ObservableList<Contact> contacts;

    public void loadContacts() {
    	
        try {
            // First, create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader
            InputStream in = new FileInputStream(CONTACTS_FILE);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // read the XML document
            Contact contact = null;

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    // If there is a contact item, we create a new contact
                    if (startElement.getName().getLocalPart().equals(CONTACT)) {
                        contact = new Contact();
                        continue;
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart().equals(FIRST_NAME)) {
                            event = eventReader.nextEvent();
                            contact.setFirstName(event.asCharacters().getData());
                            continue;
                        }
                    }
                    if (event.asStartElement().getName().getLocalPart().equals(LAST_NAME)) {
                        event = eventReader.nextEvent();
                        contact.setLastName(event.asCharacters().getData());
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart().equals(PHONE_NUMBER)) {
                        event = eventReader.nextEvent();
                        contact.setPhoneNumber(event.asCharacters().getData());
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart().equals(NOTES)) {
                        event = eventReader.nextEvent();
                        contact.setNotes(event.asCharacters().getData());
                        continue;
                    }
                }

                // Reached the end of a contact element, we add it to the list
                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equals(CONTACT)) {
                        contacts.add(contact);
                    }
                }
            }
        }
        catch (FileNotFoundException e) {
            //e.printStackTrace();
        }
        catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    public void saveContacts() {

        try {
            // create an XMLOutputFactory
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            // create XMLEventWriter
            XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(CONTACTS_FILE));
            // create an EventFactory
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();
            XMLEvent end = eventFactory.createDTD("\n");
            // create and write Start Tag
            StartDocument startDocument = eventFactory.createStartDocument();
            eventWriter.add(startDocument);
            eventWriter.add(end);

            StartElement contactsStartElement = eventFactory.createStartElement("", "", "contacts");
            eventWriter.add(contactsStartElement);
            eventWriter.add(end);

            for (Contact contact: contacts) {
                saveContact(eventWriter, eventFactory, contact);
            }

            eventWriter.add(eventFactory.createEndElement("", "", "contacts"));
            eventWriter.add(end);
            eventWriter.add(eventFactory.createEndDocument());
            eventWriter.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Problem with Contacts file: " + e.getMessage());
            e.printStackTrace();
        }
        catch (XMLStreamException e) {
            System.out.println("Problem writing contact: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveContact(XMLEventWriter eventWriter, XMLEventFactory eventFactory, Contact contact) throws FileNotFoundException, XMLStreamException {

        XMLEvent end = eventFactory.createDTD("\n");

        // create contact open tag
        StartElement configStartElement = eventFactory.createStartElement("", "", CONTACT);
        eventWriter.add(configStartElement);
        eventWriter.add(end);
        // Write the different nodes
        if (!contact.getFirstName().isEmpty()) 
        	createNode(eventWriter, FIRST_NAME, contact.getFirstName());
        else 
        	createNode(eventWriter, FIRST_NAME, "N/A");							// save string in XML as N/A in case if the field is empty
        if (!contact.getLastName().isEmpty())
        	createNode(eventWriter, LAST_NAME, contact.getLastName());
        else
        	createNode(eventWriter, LAST_NAME,	"N/A");
        if (!contact.getPhoneNumber().isEmpty())
        	createNode(eventWriter, PHONE_NUMBER, contact.getPhoneNumber());
        else
        	createNode(eventWriter, PHONE_NUMBER, "N/A");
        if (!contact.getNotes().isEmpty())
        	createNode(eventWriter, NOTES, contact.getNotes());
        else
        	createNode(eventWriter, NOTES, "N/A");
        
        eventWriter.add(eventFactory.createEndElement("", "", CONTACT));
        eventWriter.add(end);
    }

    private void createNode(XMLEventWriter eventWriter, String name, String value) throws XMLStreamException {

        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");
        // create Start node
        StartElement sElement = eventFactory.createStartElement("", "", name);
        eventWriter.add(tab);
        eventWriter.add(sElement);
        // create Content
        Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);
        // create End node
        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
        eventWriter.add(end);
    }
}