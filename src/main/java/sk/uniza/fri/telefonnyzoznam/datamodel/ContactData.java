package sk.uniza.fri.telefonnyzoznam.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Objects;

/**
 * Prevzatá trieda z kurzu na Udemy <a href=”https://www.udemy.com/course/java-the-complete-java-developer-course/“>
 * Slúži na vytvorenie a spracovávanie databázy, kt. ukladá jednotlivé kontakty do .xml súboru
 * @author timbuchalka, created on 2/11/16.
 */
public class ContactData {
    private static final String CONTACTS_FILE = "db_kontakty.xml";

    private static final String KONTAKT = "kontakt";
    private static final String KRSTNE_MENO = "krstne_meno";
    private static final String PRIEZVISKO = "priezvisko";
    private static final String TEL_CISLO = "tel_cislo";
    private static final String POZNAMKA = "poznamka";

    private final ObservableList<Kontakt> kontakts;

    public ContactData() {
        kontakts = FXCollections.observableArrayList();
    }

    public ObservableList<Kontakt> getContacts() {
        return kontakts;
    }

    public void addContact(Kontakt item) {
        kontakts.add(item);
    }

    public void deleteContact(Kontakt item) {
        kontakts.remove(item);
    }

    public void loadContacts() {
        try {
            // First, create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader
            InputStream in = new FileInputStream(CONTACTS_FILE);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // read the XML document
            sk.uniza.fri.telefonnyzoznam.datamodel.Kontakt kontakt = null;

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    // If we have a kontakt item, we create a new kontakt
                    if (startElement.getName().getLocalPart().equals(KONTAKT)) {
                        kontakt = new Kontakt();
                        continue;
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(KRSTNE_MENO)) {
                            event = eventReader.nextEvent();
                            Objects.requireNonNull(kontakt).setKrstneMeno(event.asCharacters().getData());
                            continue;
                        }
                    }
                    if (event.asStartElement().getName().getLocalPart()
                            .equals(PRIEZVISKO)) {
                        event = eventReader.nextEvent();
                        Objects.requireNonNull(kontakt).setPriezvisko(event.asCharacters().getData());
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart()
                            .equals(TEL_CISLO)) {
                        event = eventReader.nextEvent();
                        Objects.requireNonNull(kontakt).setTelCislo(event.asCharacters().getData());
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart()
                            .equals(POZNAMKA)) {
                        event = eventReader.nextEvent();
                        Objects.requireNonNull(kontakt).setPoznamka(event.asCharacters().getData());
                        continue;
                    }
                }

                // If we reach the end of a kontakt element, we add it to the list
                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equals(KONTAKT)) {
                        kontakts.add(kontakt);
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
            XMLEventWriter eventWriter = outputFactory
                    .createXMLEventWriter(new FileOutputStream(CONTACTS_FILE));
            // create an EventFactory
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();
            XMLEvent end = eventFactory.createDTD("\n");
            // create and write Start Tag
            StartDocument startDocument = eventFactory.createStartDocument();
            eventWriter.add(startDocument);
            eventWriter.add(end);

            StartElement contactsStartElement = eventFactory.createStartElement("",
                    "", "kontakts");
            eventWriter.add(contactsStartElement);
            eventWriter.add(end);

            for (sk.uniza.fri.telefonnyzoznam.datamodel.Kontakt kontakt : kontakts) {
                saveContact(eventWriter, eventFactory, kontakt);
            }

            eventWriter.add(eventFactory.createEndElement("", "", "kontakts"));
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

    private void saveContact(XMLEventWriter eventWriter, XMLEventFactory eventFactory, sk.uniza.fri.telefonnyzoznam.datamodel.Kontakt kontakt)
            throws FileNotFoundException, XMLStreamException {

        XMLEvent end = eventFactory.createDTD("\n");

        // create kontakt open tag
        StartElement configStartElement = eventFactory.createStartElement("",
                "", KONTAKT);
        eventWriter.add(configStartElement);
        eventWriter.add(end);
        // Write the different nodes
        createNode(eventWriter, KRSTNE_MENO, kontakt.getKrstneMeno());
        createNode(eventWriter, PRIEZVISKO, kontakt.getPriezvisko());
        createNode(eventWriter, TEL_CISLO, kontakt.getTelCislo());
        createNode(eventWriter, POZNAMKA, kontakt.getPoznamka());

        eventWriter.add(eventFactory.createEndElement("", "", KONTAKT));
        eventWriter.add(end);
    }

    private void createNode(XMLEventWriter eventWriter, String name,
                            String value) throws XMLStreamException {

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
