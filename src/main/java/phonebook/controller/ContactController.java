package phonebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import phonebook.model.Contact;
import phonebook.service.ContactService;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    // Add new contact
    @PostMapping("/add")
    public ResponseEntity<Contact> addContact(@RequestBody Contact contact) {
        return ResponseEntity.ok(contactService.addContact(contact));
    }
    @PostMapping("/addAll")
    public ResponseEntity<List<Contact>> addAllContact(@RequestBody List<Contact> contacts) {
        return ResponseEntity.ok(contactService.addAllContact(contacts));
    }
    // Get all contacts
    @GetMapping("/")
    public ResponseEntity<List<Contact>> getAllContacts() {
        return ResponseEntity.ok(contactService.getAllContacts());
    }
    // Search contacts by name
    @GetMapping("/search")
    public ResponseEntity<List<Contact>> searchContacts(@RequestParam String searchName) {
        return ResponseEntity.ok(contactService.searchContactsByName(searchName));
    }
    // Update contact
    @PutMapping("/save")
    public ResponseEntity<Contact> updateContact(@RequestParam String oldName, @RequestParam String oldNumber, @RequestBody Contact newContact) {
        Contact updatedContact = contactService.updateContact(oldName, oldNumber, newContact);
        return updatedContact != null ? ResponseEntity.ok(updatedContact) : ResponseEntity.notFound().build();
    }

    // Delete contact
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteContact(@RequestParam String name, @RequestParam String number) {
        contactService.deleteContact(name, number);
        return ResponseEntity.ok().build();
    }

    // Delete multiple contacts
    @PostMapping("/deleteSelected")
    public ResponseEntity<Void> deleteSelectedContacts(@RequestBody List<Contact> contacts) {
        if (contacts == null || contacts.isEmpty()) {
            return ResponseEntity.badRequest().build(); // Зөв өгөгдөл ирээгүй бол алдаа буцаана.
        }
        contactService.deleteContacts(contacts);
        return ResponseEntity.ok().build();
    }
}
