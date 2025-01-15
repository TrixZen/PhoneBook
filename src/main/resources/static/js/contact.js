// static/js/contact.js
async function addContact(event) {
    event.preventDefault();
    const formData = {
        name: document.getElementById('name').value,
        number: document.getElementById('number').value
    };

    try {
        const response = await fetch('/api/contact/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
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

async function loadContacts() {
    try {
        const response = await fetch('/api/contact/');
        const contacts = await response.json();
        const tbody = document.querySelector('#contactsTable tbody');
        tbody.innerHTML = '/';

        contacts.forEach((contact, index) => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${index + 1}</td>
                <td>${contact.name}</td>
                <td>${contact.number}</td>
                <td>
                    <button onclick="editContact('${contact.name}', '${contact.number}')" class="btn btn-sm btn-primary">Edit</button>
                    <button onclick="deleteContact('${contact.name}', '${contact.number}')" class="btn btn-sm btn-danger">Delete</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error('Error:', error);
    }
}

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

async function editContact(name, number) {
    try {
        const response = await fetch(`/api/contact/edit?name=${name}&number=${number}`);
        const contact = await response.json();

        document.getElementById('name').value = contact.name;
        document.getElementById('number').value = contact.number;

        // Store old values for update
        document.getElementById('oldName').value = name;
        document.getElementById('oldNumber').value = number;

        // Change form submit handler
        document.getElementById('contactForm').onsubmit = updateContact;
        document.querySelector('button[type="submit"]').textContent = 'Update Contact';
    } catch (error) {
        console.error('Error:', error);
    }
}

async function updateContact(event) {
    event.preventDefault();
    const formData = {
        name: document.getElementById('name').value,
        number: document.getElementById('number').value
    };

    const oldName = document.getElementById('oldName').value;
    const oldNumber = document.getElementById('oldNumber').value;

    try {
        const response = await fetch(`/api/contact/save?oldName=${oldName}&oldNumber=${oldNumber}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });

        if (response.ok) {
            loadContacts();
            document.getElementById('contactForm').reset();
            // Reset form to add mode
            document.getElementById('contactForm').onsubmit = addContact;
            document.querySelector('button[type="submit"]').textContent = 'Add Contact';
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

// Load contacts when page loads
document.addEventListener('DOMContentLoaded', loadContacts);