function validateName(name) {
    if (!/^[a-zA-Zа-яА-ЯёЁ\s]+$/.test(name)) {
        alert("Нэр зөвхөн үсэг болон хоосон зай агуулсан байх ёстой!");
        return false;
    }
    return true;
}

function validateNumber(number) {
    if (!/^\d{8}$/.test(number)) {
        alert("Дугаар заавал 8 оронтой байх ёстой!");
        return false;
    }
    return true;
}

function validateForm(name, number) {
    if (!validateName(name) || !validateNumber(number)) {
        return false;
    }
    return true;
}

function validateSearch(searchData) {
    if (!/^[a-zA-Zа-яА-ЯёЁ\s]+$/.test(searchData)) {
        if (!/^\d{1,8}$/.test(searchData)) {
            alert("Нэр эсвэл Дугаараа зөв оруулна уу!");
            return false;
        }
    }
    return true;
}

async function renderContacts(response) {
    const contacts = await response.json();
    const tbody = document.querySelector('#contactsTable tbody');

    tbody.innerHTML = ''; // Хуучин контент устгаж шинэчлэх

    contacts.forEach((contact, index) => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td><input type="checkbox" class="contact-checkbox" data-id="${contact.id}"></td>
            <td>${index + 1}</td>
            <td contenteditable="true">${contact.name}</td>
            <td contenteditable="true">${contact.number}</td>
            <td>
                <button onclick="deleteContact('${contact.id}')" class="btn btn-sm btn-danger">Устгах</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

async function errorMessage(response) {
    const errorData = await response.json();
    alert(errorData.message);
}

