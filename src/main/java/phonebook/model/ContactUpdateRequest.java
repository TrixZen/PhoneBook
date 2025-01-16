package phonebook.model;

public class ContactUpdateRequest {
    private String oldName;
    private String oldNumber;
    private String newName;
    private String newNumber;

    // Getters and Setters

    public String getOldName() {
        return oldName;
    }

    public String getOldNumber() {
        return oldNumber;
    }

    public String getNewName() {
        return newName;
    }

    public String getNewNumber() {
        return newNumber;
    }
}