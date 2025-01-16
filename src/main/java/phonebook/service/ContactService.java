package phonebook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phonebook.model.Contact;
import phonebook.repository.ContactRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    // Add new contact
    public Contact addContact(Contact contact) {
        return contactRepository.save(contact);
    }

    // Get all contacts
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    // Search contacts by name
    public List<Contact> searchContactsByName(String name) {
        return contactRepository.findByNameContainingIgnoreCase(name);
    }

    // Update existing contact
    public Contact updateContact(String oldName, String oldNumber, Contact newContact) {
        Optional<Contact> existingContact = contactRepository.findByNameAndNumber(oldName, oldNumber);
        if (existingContact.isPresent()) {
            Contact contact = existingContact.get();
            contact.setName(newContact.getName());
            contact.setNumber(newContact.getNumber());
            return contactRepository.save(contact);
        }
        return null;
    }

    // Delete contact by name and number
    public void deleteContact(String name, String number) {
        Contact contact = this.contactRepository.findByNameAndNumber(name, number).orElseThrow(() -> new IllegalArgumentException("Contact not found"));
        this.contactRepository.delete(contact);
    }

    // Delete multiple contacts
    public void deleteContacts(List<Contact> contacts) {
        for (Contact contact : contacts) {
            deleteContact(contact.getName(), contact.getNumber());
        }
    }
    // test method
    public List<Contact> addAllContact(List<Contact> contacts) {
        return contactRepository.saveAll(contacts);
    }
}
