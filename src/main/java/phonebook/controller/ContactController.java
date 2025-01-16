package phonebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import phonebook.model.Contact;
import phonebook.service.ContactServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    ContactServiceImpl contactServiceImpl;

    @PostMapping("/add")
    public String add(@RequestBody Contact contact) {
        contactServiceImpl.saveContact(contact);
        return "Contact added successfully";
    }

    @GetMapping("/")
    public List<Contact> getContacts() {
        return contactServiceImpl.getContacts();
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam String name,
                         @RequestParam String number) {
        contactServiceImpl.deleteContact(name, number);
        return "Contact deleted successfully";
    }

    @GetMapping("/edit")
    public Contact edit(@RequestParam String name,
                        @RequestParam String number) {
        return contactServiceImpl.findContact(name, number);
    }

    @PutMapping("/save")
    public String saveOrUpdate(
            @RequestBody Contact oldContact,
            @RequestParam("oldName") String oldName,
            @RequestParam("oldNumber") String oldNumber) {
        contactServiceImpl.editContact(oldContact, oldName, oldNumber);
        return "Contact updated successfully";
    }
    @GetMapping("/search")
    public List<Contact> searchName(@RequestParam String searchName) {
        return contactServiceImpl.searchName(searchName);
    }
}
