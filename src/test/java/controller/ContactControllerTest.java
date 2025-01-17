package controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import phonebook.controller.ContactController;
import phonebook.model.Contact;
import phonebook.model.ContactUpdateRequest;
import phonebook.service.ContactService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ContactControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ContactService contactService;

    @InjectMocks
    private ContactController contactController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(contactController).build();
    }

    @Test
    void testAddContact() throws Exception {
        Contact contact = new Contact("John Doe", "1234567890");

        //when(contactService.addContact(any(Contact.class)));

        mockMvc.perform(post("/api/contact/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(contact)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.number").value("1234567890"));
    }

    @Test
    void testGetAllContacts() throws Exception {
        Contact contact1 = new Contact("John Doe", "1234567890");
        Contact contact2 = new Contact("Jane Doe", "0987654321");
        List<Contact> contactList = Arrays.asList(contact1, contact2);

        when(contactService.getAllContacts()).thenReturn(contactList);

        mockMvc.perform(get("/api/contact/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"));
    }

    @Test
    void testSearchContacts() throws Exception {
        Contact contact = new Contact("John Doe", "1234567890");
        List<Contact> contactList = List.of(contact);

        when(contactService.searchContactsByData("John")).thenReturn(contactList);

        mockMvc.perform(get("/api/contact/search")
                        .param("searchName", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    void testSaveSelectedContacts() throws Exception {
        ContactUpdateRequest contactUpdateRequest = new ContactUpdateRequest("John Doe", "1234567890", "John Smith", "9876543210");

        doNothing().when(contactService).updateContact(any(ContactUpdateRequest.class));

        mockMvc.perform(put("/api/contact/saveSelected")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(List.of(contactUpdateRequest))))
                .andExpect(status().isOk());

        verify(contactService, times(1)).updateContact(any(ContactUpdateRequest.class));
    }

    @Test
    void testDeleteContact() throws Exception {
        doNothing().when(contactService).deleteContact(anyString(), anyString());

        mockMvc.perform(delete("/api/contact/delete")
                        .param("name", "John Doe")
                        .param("number", "1234567890"))
                .andExpect(status().isOk());

        verify(contactService, times(1)).deleteContact("John Doe", "1234567890");
    }

    @Test
    void testDeleteSelectedContacts() throws Exception {
        Contact contact = new Contact("John Doe", "1234567890");
        List<Contact> contactsToDelete = List.of(contact);

        doNothing().when(contactService).deleteContacts(anyList());

        mockMvc.perform(post("/api/contact/deleteSelected")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(contactsToDelete)))
                .andExpect(status().isOk());

        verify(contactService, times(1)).deleteContacts(anyList());
    }
}

