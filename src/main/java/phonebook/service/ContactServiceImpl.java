package phonebook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phonebook.model.Contact;
import phonebook.repository.ContactRepository;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public List<Contact> getContacts() {
        return contactRepository.findAll();
    }

    @Override
    public void saveContact(Contact contact) {
        contactRepository.save(contact);
    }

    @Override
    public void deleteContact(String name, String number) {
        Contact contact = contactRepository.findByNameAndNumber(name, number)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found"));
        contactRepository.delete(contact);
    }

    @Override
    public Contact findContact(String name, String number) {
        return contactRepository.findByNameAndNumber(name, number)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found"));
    }

    @Override
    public void editContact(Contact newContact, String oldName, String oldNumber) {
        Contact contact = contactRepository.findByNameAndNumber(oldName, oldNumber)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found"));

        contact.setName(newContact.getName());
        contact.setNumber(newContact.getNumber());

        contactRepository.save(contact);
    }

    @Override
    public List<Contact> searchName(String searchName) {
        return contactRepository.findByName(searchName)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found"));
    }

}