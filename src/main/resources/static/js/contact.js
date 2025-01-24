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

    if(!validateForm(name, number)) {
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
        } else {
            errorMessage(response);
        }
    } catch (error) {
        console.error(error)
    }
}

// Load all contacts
async function loadContacts() {
    try {
        const response = await fetch('/api/contact/');

        renderContacts(response);

    } catch (error) {
        console.error('Алдаа:', error);
    }
}

// Save all contacts
async function saveContacts() {
    // Сонгогдсон мөрийг авах
     const selectedCheckboxes = document.querySelectorAll('.contact-checkbox:checked');

    if (selectedCheckboxes.length === 0) {
        alert('Хадгалахын тулд дор хаяж нэг мөр сонгоно уу.');
        return;
   }
  // Сонгогдсон бүх мөрний шинэ утгуудыг авах
    const contactsToSave = [];

    // Бүх чекбоксуудыг шалгах
    for (let i = 0; i < selectedCheckboxes.length; i++) {
        const checkbox = selectedCheckboxes[i];
        const row = checkbox.closest('tr'); // Сонгосон мөрийг авах

        const oldName = checkbox.getAttribute('data-name');
        const oldNumber = checkbox.getAttribute('data-number');
        const newName = row.children[2].textContent.trim();
        const newNumber = row.children[3].textContent.trim();

        // Нэр, дугаарын шалгалтыг хийх
        if (!validateForm(newName, newNumber)) {
            return; // Алдаа гарсан бол процесс зогсоно
        }

        // Шалгалт амжилттай бол шинэ мэдээллийг нэмнэ
        contactsToSave.push({ oldName, oldNumber, newName, newNumber });
    }

    try {
        const response = await fetch('/api/contact/saveSelected', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(contactsToSave)
        });

        if (response.ok) {
            alert('Харилцагчийг амжилттай хадгаллаа!');
        } else {
            errorMessage(response);
        }
        loadContacts();
    } catch (error) {
        console.error('Алдаа:', error);
    }
}

// Delete contact
async function deleteContact(name, number) {
    if (!confirm('Та энэ харилцагчийг устгахдаа итгэлтэй байна уу?')) return;

    try {
        const response = await fetch(`/api/contact/delete?name=${name}&number=${number}`, {
            method: 'DELETE'
        });
        if (response.ok) {
            loadContacts();
        }
    } catch (error) {
        console.error('Алдаа:', error);
    }
}

// Search contacts
async function searchContacts() {
    const searchData = document.getElementById('searchData').value.trim();

    // Хайлт хоосон байвал үйлдлийг зогсооно
    if (!searchData) {
        alert("Нэр эсвэл дугаар оруулна уу!");
        return;
    }

    if (!validateSearch(searchData)) {
        return
    }

    try {
        const response = await fetch(`/api/contact/search?searchData=${searchData}`);

        if (response.ok) {
            document.getElementById('searchData').value = '';
            renderContacts(response);
        } else {
            errorMessage(response);
        }

    } catch (error) {
        console.error('Алдаа:', error);
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
        alert('Устгах харилцагчдыг сонгоно уу!');
        return;
    }

    if (!confirm('Та сонгосон харилцагчдыг устгахдаа итгэлтэй байна уу?')) return;

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
            console.error('Серверийн Алдаа:', error);
            alert(`Failed to delete contacts: ${error}`);

        }
    } catch (error) {
        console.error('Алдаа:', error);
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
