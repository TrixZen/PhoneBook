package phonebook.service;

import phonebook.model.Contact;
import java.util.List;

public interface ContactService {
    List<Contact> getContacts();
    void saveContact(Contact contact);
    void deleteContact(String name, String number);
    Contact findContact(String name, String number);
    void editContact(Contact contact, String oldName, String oldNumber);
    List<Contact> searchName(String searchName);
}