package phonebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import phonebook.model.Contact;
import phonebook.model.ContactUpdateRequest;
import phonebook.service.ContactService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    // Add new contact
    @PostMapping("/add")
    public ResponseEntity<?> addContact(@RequestBody Contact contact) {
        try {
            contactService.addContact(contact);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = Map.of("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    // Add all new contacts
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
    public ResponseEntity<?> searchContacts(@RequestParam String searchData) {
        try {
            List<Contact> contacts = contactService.searchContactsByData(searchData);
            return ResponseEntity.ok(contacts);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = Map.of("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    // Update contact
    @PutMapping("/saveSelected")
    public ResponseEntity<?> updateContact(@RequestBody List<ContactUpdateRequest> contacts) {
        for (ContactUpdateRequest contact : contacts) {
            try {
                contactService.updateContact(contact);
            } catch (IllegalArgumentException e) {
                Map<String, String> errorResponse = Map.of("message", e.getMessage());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
            }
        }
        return ResponseEntity.ok().build();
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
