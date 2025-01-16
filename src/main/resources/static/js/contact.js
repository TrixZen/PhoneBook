document.addEventListener('DOMContentLoaded', () => {
    loadContacts();

    // Event listener for searching
    document.getElementById('searchButton').addEventListener('click', searchContacts);
});

// Add contact
async function addContact(event) {
    event.preventDefault();
    const formData = {
        name: document.getElementById('name').value,
        number: document.getElementById('number').value
    };

    try {
        const response = await fetch('/api/contact/add', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(formData)
        });
        if (response.ok) {
            loadContacts();
            document.getElementById('contactForm').reset();
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

// Load all contacts
async function loadContacts() {
    try {
        const response = await fetch('/api/contact/');
        const contacts = await response.json();
        const tbody = document.querySelector('#contactsTable tbody');
        tbody.innerHTML = '';

        contacts.forEach((contact, index) => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${index + 1}</td>
                <td contenteditable="true">${contact.name}</td>
                <td contenteditable="true">${contact.number}</td>
                <td>
                    <button onclick="saveContact(this, '${contact.name}', '${contact.number}')" class="btn btn-sm btn-success">Save</button>
                    <button onclick="deleteContact('${contact.name}', '${contact.number}')" class="btn btn-sm btn-danger">Delete</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error('Error:', error);
    }
}

// Save edited contact
async function saveContact(button, oldName, oldNumber) {
    const row = button.closest('tr');
    const newName = row.children[1].textContent.trim();
    const newNumber = row.children[2].textContent.trim();

    try {
        const response = await fetch(`/api/contact/save?oldName=${oldName}&oldNumber=${oldNumber}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name: newName, number: newNumber })
        });
        if (response.ok) {
            loadContacts();
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

// Delete contact
async function deleteContact(name, number) {
    if (!confirm('Are you sure you want to delete this contact?')) return;

    try {
        const response = await fetch(`/api/contact/delete?name=${name}&number=${number}`, {
            method: 'DELETE'
        });
        if (response.ok) {
            loadContacts();
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

// Search contacts
async function searchContacts() {
    const searchName = document.getElementById('searchName').value.trim();

    try {
        const response = await fetch(`/api/contact/search?name=${searchName}`);
        const contacts = await response.json();
        const tbody = document.querySelector('#contactsTable tbody');
        tbody.innerHTML = '';

        contacts.forEach((contact, index) => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${index + 1}</td>
                <td>${contact.name}</td>
                <td>${contact.number}</td>
                <td>
                    <button onclick="saveContact(this, '${contact.name}', '${contact.number}')" class="btn btn-sm btn-success">Save</button>
                    <button onclick="deleteContact('${contact.name}', '${contact.number}')" class="btn btn-sm btn-danger">Delete</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error('Error:', error);
    }
}
