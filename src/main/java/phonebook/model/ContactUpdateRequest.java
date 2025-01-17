package phonebook.model;

public record ContactUpdateRequest(String oldName, String oldNumber, String newName, String newNumber) {

    // Getters and Setters
}