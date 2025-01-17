package phonebook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phonebook.model.Contact;
import phonebook.model.ContactUpdateRequest;
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
    public List<Contact> searchContactsByData(String searchData) {
        List<Contact> contacts;
        contacts = contactRepository.findByNameContainingIgnoreCase(searchData);
        if (contacts != null && !contacts.isEmpty()) {
            return contacts;
        }
        contacts = contactRepository.findByNumberContainingIgnoreCase(searchData);
        return contacts;
    }

    // Update existing contact
    public void updateContact(ContactUpdateRequest contactUpdateRequest) {
        Optional<Contact> existingContact = contactRepository.findByNameAndNumber(contactUpdateRequest.oldName(), contactUpdateRequest.oldNumber());
        if (existingContact.isPresent()) {
            Contact contact = existingContact.get();
            contact.setName(contactUpdateRequest.newName());
            contact.setNumber(contactUpdateRequest.newNumber());
            contactRepository.save(contact);
        }
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
