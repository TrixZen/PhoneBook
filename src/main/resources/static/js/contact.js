document.addEventListener('DOMContentLoaded', () => {
    loadContacts();

    // Event listener for searching
    document.getElementById('searchButton').addEventListener('click', searchContacts);
    // Event listener for viewing all contacts
    document.getElementById('viewAllButton').addEventListener('click', loadContacts);
    // Event listener for deleting selected contacts
    document.getElementById('deleteSelectedButton').addEventListener('click', deleteSelectedContacts);
    // Event listener for saving all update contacts
    document.getElementById('saveButton').addEventListener('click', saveContacts);
});

// Add contact
async function addContact(event) {
    event.preventDefault();

    const name = document.getElementById('name').value.trim();
    const number = document.getElementById('number').value.trim();

    // Нэр зөвхөн үсэг агуулж байгааг шалгах
    if (!/^[a-zA-Zа-яА-ЯёЁ]+$/.test(name)) {
        alert("Нэр зөвхөн үсэг агуулсан байх ёстой!");
        return;
    }

    // Дугаар зөвхөн 8 оронтой тоо байхыг шалгах
    if (!/^\d{8}$/.test(number)) {
        alert("Дугаар заавал 8 оронтой байх ёстой!");
        return;
    }

    const formData = { name, number };

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
                <td><input type="checkbox" class="contact-checkbox" data-name="${contact.name}" data-number="${contact.number}"></td>
                <td>${index + 1}</td>
                <td contenteditable="true">${contact.name}</td>
                <td contenteditable="true">${contact.number}</td>
                <td>
                    <button onclick="deleteContact('${contact.name}', '${contact.number}')" class="btn btn-sm btn-danger">Delete</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error('Error:', error);
    }
}

// Save all contacts
async function saveContacts() {
    // Сонгогдсон мөрийг авах
     const selectedCheckboxes = document.querySelectorAll('.contact-checkbox:checked');

    if (selectedCheckboxes.length === 0) {
        alert('Please select at least one row to save.');
        return;
   }
  // Сонгогдсон бүх мөрний шинэ утгуудыг авах
    const contactsToSave = [];
    selectedCheckboxes.forEach(checkbox => {

        const row = checkbox.closest('tr'); // Сонгосон мөрийг авах

        const oldName = checkbox.getAttribute('data-name');
        const oldNumber = checkbox.getAttribute('data-number');
        const newName = row.children[2].textContent.trim();
        const newNumber = row.children[3].textContent.trim();

        contactsToSave.push({ oldName, oldNumber, newName, newNumber });
    });

    try {
        const response = await fetch('/api/contact/saveSelected', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(contactsToSave)
        });

        if (response.ok) {
            alert('Contact saved successfully!');
            loadContacts();
        } else {
            const error = await response.text();
            alert(`Failed to save contact: ${error}`);
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

// Save edited contact
/*
async function saveContact(button, oldName, oldNumber) {
    const row = button.closest('tr');
    const newName = row.children[2].textContent.trim();
    const newNumber = row.children[3].textContent.trim();

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
*/

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
    const searchData = document.getElementById('searchData').value.trim();

    try {
        const response = await fetch(`/api/contact/search?searchData=${searchData}`);
        const contacts = await response.json();
        const tbody = document.querySelector('#contactsTable tbody');
        tbody.innerHTML = '';

        contacts.forEach((contact, index) => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td><input type="checkbox" class="contact-checkbox" data-name="${contact.name}" data-number="${contact.number}"></td>
                <td>${index + 1}</td>
                <td contenteditable="true">${contact.name}</td>
                <td contenteditable="true">${contact.number}</td>
                <td>
                    <button onclick="deleteContact('${contact.name}', '${contact.number}')" class="btn btn-sm btn-danger">Delete</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
        if (response.ok) {
             document.getElementById('searchData').value = '';

        }
    } catch (error) {
        console.error('Error:', error);
    }
}

// Select or deselect all checkboxes
function toggleSelectAll() {
    const selectAllCheckbox = document.getElementById('selectAllCheckbox');
    const checkboxes = document.querySelectorAll('.contact-checkbox');
    checkboxes.forEach(checkbox => {
        checkbox.checked = selectAllCheckbox.checked;
    });
}

// Delete selected contacts
async function deleteSelectedContacts() {
    const selectedCheckboxes = document.querySelectorAll('.contact-checkbox:checked');

    if (selectedCheckboxes.length === 0) {
        alert('Please select contacts to delete.');
        return;
    }

    if (!confirm('Are you sure you want to delete selected contacts?')) return;

    const contactsToDelete = [];
    selectedCheckboxes.forEach(checkbox => {
        contactsToDelete.push({
            name: checkbox.getAttribute('data-name'),
            number: checkbox.getAttribute('data-number')
        });
    });

    try {
        const response = await fetch('/api/contact/deleteSelected', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(contactsToDelete)
        });

        if (response.ok) {
            loadContacts();
        } else {
            const error = await response.text();
            console.error('Server error:', error);
            alert(`Failed to delete contacts: ${error}`);

        }
    } catch (error) {
        console.error('Error:', error);
    }
}