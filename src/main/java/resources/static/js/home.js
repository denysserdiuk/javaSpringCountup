// Save scroll position before form submission
document.querySelectorAll('form').forEach(form => {
    form.addEventListener('submit', function () {
        localStorage.setItem('scrollPosition', window.scrollY);
    });
});

// Restore scroll position after page load
window.addEventListener('load', function () {
    const scrollPosition = localStorage.getItem('scrollPosition');
    if (scrollPosition) {
        window.scrollTo(0, parseInt(scrollPosition)); // Scroll to the saved position
        localStorage.removeItem('scrollPosition'); // Remove it after use
    }
});

//budget lines

$(document).ready(function () {
    // Load current month budget lines
    $.ajax({
        url: '/currentMonthBudgets',
        type: 'GET',
        success: function (data) {
            var profitsTableBody = $('#profits-table tbody');
            var lossesTableBody = $('#losses-table tbody');

            // Clear existing table rows
            profitsTableBody.empty();
            lossesTableBody.empty();

            // Loop through the data and append rows to the appropriate table
            data.forEach(function (budget) {
                // Format the date (assuming date is in ISO format like "2023-10-21")
                var dateAdded = new Date(budget.date).toLocaleDateString();

                // Create the options menu HTML
                var optionsMenu = `
                    <div class="dropdown">
                        <button class="btn btn-secondary btn-sm dropdown-toggle" type="button" id="dropdownMenuButton${budget.id}" data-bs-toggle="dropdown" aria-expanded="false">
                            &#x22EE;
                        </button>
                        <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton${budget.id}">
                            <li><a class="dropdown-item edit-budget-item" href="#" data-id="${budget.id}" data-type="${budget.type}">Edit</a></li>
                            <li><a class="dropdown-item delete-budget-item" href="#" data-id="${budget.id}" data-type="${budget.type}">Delete</a></li>
                        </ul>
                    </div>
                `;

                // Create the table row HTML
                var rowHtml = `
                    <tr>
                        <td>${budget.description}</td>
                        <td>$${budget.amount.toFixed(2)}</td>
                        <td>${dateAdded}</td>
                        <td>${optionsMenu}</td>
                    </tr>
                `;

                if (budget.type === 'profit') {
                    profitsTableBody.append(rowHtml);
                } else if (budget.type === 'loss') {
                    lossesTableBody.append(rowHtml);
                }
            });

            // Attach event handlers for edit and delete options
            $('.edit-budget-item').on('click', function (e) {
                e.preventDefault();
                var budgetId = $(this).data('id');
                var budgetType = $(this).data('type');
                // Open edit modal and populate with budget item data
                openEditModal(budgetId, budgetType);
            });

            $('.delete-budget-item').on('click', function (e) {
                e.preventDefault();
                var budgetId = $(this).data('id');
                var budgetType = $(this).data('type');
                // Confirm deletion and delete the budget item
                deleteBudgetItem(budgetId, budgetType);
            });
        },
        error: function (error) {
            console.log("Error fetching budget lines:", error);
        }
    });
});

// Function to open the edit modal (you need to implement this)
function openEditModal(budgetId, budgetType) {
    // Fetch budget item data and populate the edit form
    // Then, show the edit modal
}

// Function to delete a budget item (you need to implement this)
function deleteBudgetItem(budgetId, budgetType) {
    if (confirm('Are you sure you want to delete this item?')) {
        $.ajax({
            url: '/api/deleteBudgetItem',
            type: 'POST',
            data: { id: budgetId },
            success: function () {
                // Reload the budget lines after deletion
                location.reload();
            },
            error: function (error) {
                console.log("Error deleting budget item:", error);
            }
        });
    }
}


// Handle the edit form submission
$('#edit-budget-form').on('submit', function (e) {
    e.preventDefault();
    var formData = $(this).serialize();

    $.ajax({
        url: '/api/updateBudgetItem',
        type: 'POST',
        data: formData,
        success: function () {
            // Close the modal and reload the budget lines
            $('#edit-budget-modal').modal('hide');
            location.reload();
        },
        error: function (error) {
            console.log("Error updating budget item:", error);
        }
    });
});


//Adding new category

function toggleNewCategoryInput(selectElement, formType) {
    var newCategoryGroup = document.getElementById('new-category-group-' + formType);
    var newCategoryInput = document.getElementById('new-category-' + formType);

    if (selectElement.value === 'Other') {
        newCategoryGroup.style.display = 'flex'; // Show the new category input field
    } else {
        newCategoryGroup.style.display = 'none'; // Hide the new category input field
        newCategoryInput.value = ''; // Clear the input when not needed
    }
}

// Handle form submission for both add-profit and add-expense forms
document.getElementById('add-profit').addEventListener('submit', function(e) {
    handleCategorySubmission('profit');
});

document.getElementById('add-loss').addEventListener('submit', function(e) {
    handleCategorySubmission('loss');
});

function handleCategorySubmission(formType) {
    var categorySelect = document.getElementById(formType + '-category');
    var newCategoryInput = document.getElementById('new-category-' + formType);

    // If 'Other' is selected and the new category input is filled
    if (categorySelect.value === 'Other' && newCategoryInput.value.trim() !== '') {
        var newCategoryValue = newCategoryInput.value.trim().replace(/\s+/g, '_'); // Replace spaces with underscores

        // Create a new option element with the user's input
        var newOption = document.createElement('option');
        newOption.value = newCategoryValue;
        newOption.text = newCategoryValue;

        // Add the new option to the select dropdown
        categorySelect.add(newOption);

        // Set the new option as the selected value
        categorySelect.value = newCategoryValue;
    }
}







