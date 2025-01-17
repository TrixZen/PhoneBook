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
    public void addContact(Contact newContact) {
        Optional<Contact> contact = contactRepository.findByNameAndNumber(newContact.getName(), newContact.getNumber());
        if (contact.isPresent()) {
            throw new IllegalArgumentException("Харилцагч аль хэдийн байна!");
        }
        contactRepository.save(newContact);
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
        if (contacts.isEmpty()) {
            throw new IllegalArgumentException("Харилцагч олдсонгүй!");
        }
        return contacts;
    }

    // Update existing contact
    public void updateContact(ContactUpdateRequest contactUpdateRequest) {
        Optional<Contact> existingContact = contactRepository.findByNameAndNumber(
                contactUpdateRequest.oldName(), contactUpdateRequest.oldNumber());
        if (existingContact.isPresent()) {
            // Шинэ мэдээллийг шалгах
            Optional<Contact> existingContactWithNewInfo = contactRepository.findByNameAndNumber(
                    contactUpdateRequest.newName(), contactUpdateRequest.newNumber());

            if (existingContactWithNewInfo.isPresent()) {
                throw new IllegalArgumentException("Зарим харилцагчийн шинэ нэр болон дугаар нь аль хэдийн байна.");
            }

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
