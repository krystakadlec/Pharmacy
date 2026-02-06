document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('button-addMedicine').addEventListener('click', addMedicineClick);
    document.getElementById('button-removeMedicine').addEventListener('click', removeMedicineClick);
    document.getElementById('button-createPrescription').addEventListener('click', createPrescriptionClick);
});


let selectedMedicines = [];
let selectedPrescription = null;

async function initializeMedicineSelection() {
    const medicineSelect = document.getElementById('medicines');

    medicineSelect.innerHTML = '';

    try {
        const response = await fetch('http://localhost:8080/rest/api/medicine');
        if (!response.ok) {
            alert ('Failed to fetch medicines');
        }

        const medicines = await response.json();

        medicines.forEach(medicine => {
            const option = document.createElement('option');
            option.value = medicine.id;
            option.textContent = medicine.name;
            option.dataset.price = medicine.price;
            medicineSelect.appendChild(option);
        });

        document.getElementById('button-addMedicine').disabled = false;
        document.getElementById('button-removeMedicine').disabled = false;

    } catch (error) {
        console.error('Error fetching medicines:', error);
    }
}

async function addMedicineClick() {
    const medicineSelect = document.getElementById('medicines');
    const selectedOption = medicineSelect.options[medicineSelect.selectedIndex];

    if (!selectedOption) {
        alert("Please select a medicine first.");
        return;
    }

    const medicineId = selectedOption.value;
    const medicineName = selectedOption.textContent;
    const medicinePrice = parseFloat(selectedOption.dataset.price);

    if (!selectedMedicines.some(m => m.id === medicineId)) {
        selectedMedicines.push({
            id: medicineId,
            name: medicineName,
            price: medicinePrice
        });
        await updatePrescriptionPreview();
    }
    console.log('Selected Medicines:', selectedMedicines);
}

async function removeMedicineClick() {
    const medicineSelect = document.getElementById('medicines');
    const selectedOption = medicineSelect.options[medicineSelect.selectedIndex];
    if (!selectedOption) {
        alert("Please select a medicine first.");
        return;
    }

    const medicineId = selectedOption.value;

    selectedMedicines = selectedMedicines.filter(m => m.id !== medicineId);

    await updatePrescriptionPreview();
}

let isCreatingPrescription = false;

async function createPrescriptionClick() {

    if (isCreatingPrescription) {
        console.log('Prescription creation already in progress...');
        return; // Prevent duplicate calls
    }
    isCreatingPrescription = true;

    const urlParams = new URLSearchParams(window.location.search);
    const currentPatientId = urlParams.get('CurrentPatientId');
    const currentDate = new Date().toISOString().split('T')[0];

    if (!currentPatientId) {
        alert('Current patient ID is missing from the URL.');
        return;
    }

    if (selectedMedicines.length === 0) {
        alert("No medicines selected for the prescription.");
        return;
    }

    const totalPrice = selectedMedicines.reduce((sum, medicine) => sum + medicine.price, 0);

    const prescription = {
        id: null,
        price: totalPrice,
        date: currentDate,
        discountApply: false,
        prescribedPatientId: parseInt(currentPatientId),
        prescribedMedicineIds: selectedMedicines.map(medicine => medicine.id),
    };

    const response = await fetch('http://localhost:8080/rest/api/prescription', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(prescription)
    });

    if (response.ok) {
        selectedMedicines = [];
        await loadPrescriptions();
        await loadMedicineHistory();
        await updatePrescriptionPreview();
    } else {
        alert ('Failed to create prescription');
    }
    isCreatingPrescription = false;
}

function updatePrescriptionPreview() {
    const urlParams = new URLSearchParams(window.location.search);
    const patientId = urlParams.get('CurrentPatientId');
    const currentDate = new Date().toLocaleDateString();

    document.getElementById('preview-patient-id').textContent = patientId || 'none';
    document.getElementById('preview-date').textContent = currentDate;

    const medicinesList = document.getElementById('preview-medicines');
    medicinesList.innerHTML = '';

    if (selectedMedicines.length === 0) {
        const emptyItem = document.createElement('li');
        emptyItem.textContent = "No medicines added yet.";
        medicinesList.appendChild(emptyItem);
    } else {
        selectedMedicines.forEach(medicine => {
            const listItem = document.createElement('li');
            listItem.textContent = `${medicine.name} - ${medicine.price.toFixed(2)} Kč`;
            medicinesList.appendChild(listItem);
        });
    }

    const totalPrice = selectedMedicines.reduce((sum, medicine) => sum + medicine.price, 0);
    document.getElementById('preview-total-price').textContent = `${totalPrice.toFixed(2)} Kč`;
    console.log('Updating Preview:', selectedMedicines);
}

async function loadMedicineHistory() {
    try {
        const urlParams = new URLSearchParams(window.location.search);
        const currentPatientId = urlParams.get("CurrentPatientId");

        if (!currentPatientId) {
            console.error("Current patient ID is missing.");
            return;
        }

        const response = await fetch(`http://localhost:8080/rest/api/patient/${currentPatientId}/medicines`);

        if (!response.ok) {
            alert ("Failed to fetch medicines used by the patient.");
        }

        const medicines = await response.json();
        const historyList = document.getElementById("history-list");

        historyList.innerHTML = "";

        if (medicines.length === 0) {
            historyList.innerHTML = "<li>No history available.</li>";
        } else {
             medicines.forEach(medicine => {
                const listItem = document.createElement("li");
                listItem.textContent = `Medicine: ${medicine.name} - daily dosage: ${medicine.dailyDosage}`;
                historyList.appendChild(listItem);
            });
        }
    } catch (error) {
        console.error("Error loading prescription history:", error);
        alert("Failed to load prescription history. Please try again later.");
    }
}



async function loadPrescriptions() {
    try {
        const urlParams = new URLSearchParams(window.location.search);
        const currentPatientId = urlParams.get("CurrentPatientId");

        if (!currentPatientId) {
            console.error("Current patient ID is missing.");
            return;
        }

        const response = await fetch(`http://localhost:8080/rest/api/patient/${currentPatientId}/prescriptions`);

        if (!response.ok) {
            alert ("Failed to fetch prescriptions.");
        }

        const prescriptions = await response.json();
        const prescriptionsList = document.getElementById("prescriptions-list");

        prescriptionsList.innerHTML = "";

        if (prescriptions.length === 0) {
            prescriptionsList.innerHTML = "<tr><td colspan='5'>No prescriptions available.</td></tr>";
        } else {
            prescriptions.forEach(prescription => {
                const row = document.createElement("tr");
                row.dataset.prescriptionId = prescription.id;

                const selectCell = document.createElement("td");
                const selectCheckbox = document.createElement("input");
                selectCheckbox.type = "radio";
                selectCheckbox.name = "prescription-select";
                selectCheckbox.addEventListener('change', () => selectPrescription(prescription.id));
                selectCell.appendChild(selectCheckbox);
                row.appendChild(selectCell);

                const idCell = document.createElement("td");
                idCell.textContent = prescription.id;
                row.appendChild(idCell);

                const dateCell = document.createElement("td");
                dateCell.textContent = prescription.date;
                row.appendChild(dateCell);

                const priceCell = document.createElement("td");
                priceCell.textContent = `${prescription.price.toFixed(2)} Kč`;
                row.appendChild(priceCell);

                const actionsCell = document.createElement("td");
                actionsCell.textContent = `${prescription.discountApply}`;
                row.appendChild(actionsCell);

                prescriptionsList.appendChild(row);
            });
            document.getElementById('button-deletePrescription').disabled = true;
            document.getElementById('button-applyDiscount').disabled = true;
        }
    } catch (error) {
        console.error("Error loading prescriptions:", error);
        alert("Failed to load prescriptions. Please try again later.");
    }
}

function selectPrescription(prescriptionId) {
    selectedPrescription = prescriptionId;
    document.getElementById('button-deletePrescription').disabled = false;
    document.getElementById('button-applyDiscount').disabled = false;
}

async function deletePrescriptionClick() {
    if (!selectedPrescription) {
        alert("No prescription selected.");
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/rest/api/prescription/${selectedPrescription}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            alert("Prescription deleted successfully!");
            await loadPrescriptions();
        } else {
            const errorText = await response.text();
            alert (`Failed to delete prescription: ${errorText}`);
        }
    } catch (error) {
        console.error("Error deleting prescription:", error);
        alert("Failed to delete prescription. Please try again later.");
    }

    await loadPrescriptions();
    await loadMedicineHistory();
}

async function deleteMedicineClick() {
    const medicineSelect = document.getElementById('medicines');
    const selectedOption = medicineSelect.options[medicineSelect.selectedIndex];

    if (!selectedOption) {
        alert("Please select a medicine first.");
        return;
    }

    const medicineId = selectedOption.value;

    if (selectedMedicines.some(medicine => medicine.id === medicineId)) {
        alert(`Remove medicine from prescription preview to be able to delete it.`);
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/rest/api/medicine/${medicineId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            alert("Medicine deleted successfully!");
            await initializeMedicineSelection();
            await loadMedicineHistory();
            await loadPrescriptions();
            await updatePrescriptionPreview();
        } else {
            const errorText = await response.text();
            alert(`Failed to delete medicine: ${errorText}`);
        }
    } catch (error) {
        console.error("Error deleting medicine:", error);
        alert("Failed to delete medicine. Please try again later.");
    }
}

async function getPrescriptionDetails(id) {
    const response = await fetch(`http://localhost:8080/rest/api/prescription/${id}`);
    if (!response.ok) {
        alert ('Failed to load prescription details');
        return;
    }
    return await response.json();
}

async function applyDiscountClick() {
    if (!selectedPrescription) {
        alert("No prescription selected.");
        return;
    }

    try {
        const prescriptionDetails = await getPrescriptionDetails(selectedPrescription);
        if(prescriptionDetails){
            const response = await fetch(`http://localhost:8080/rest/api/prescription/${selectedPrescription}/discount`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(prescriptionDetails)
            });

            if (response.ok) {
                alert("Discount applied successfully!");
                await loadPrescriptions();
            } else {
                alert (`Unable to apply discount`);
            }

        }

    } catch (error) {
        console.error("Error applying discount:", error);
        alert("Not eligible for discount");
    }
}

