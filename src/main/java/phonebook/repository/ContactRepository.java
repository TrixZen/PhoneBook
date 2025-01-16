package phonebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phonebook.model.Contact;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findByNameContainingIgnoreCase(String name);

    Optional<Contact> findByNameAndNumber(String name, String number);

    void deleteByNameAndNumber(String name, String number);
}