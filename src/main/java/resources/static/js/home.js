document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('sidebarToggle').addEventListener('click', function () {
        const sidebar = document.querySelector('.sidebar');
        sidebar.classList.toggle('sidebar--collapsed');
    });
});

// Earnings Overview Chart
$(document).ready(function () {
    $.ajax({
        url: '/api/userBalance',
        type: 'GET',
        success: function (data) {
            const earningsCtx = document.getElementById('earningsChart').getContext('2d');

            const earningsChart = new Chart(earningsCtx, {
                type: 'line',
                data: {
                    labels: Object.keys(data), // Month names from the returned map
                    datasets: [{
                        label: 'Balance',
                        data: Object.values(data), // Balance values
                        borderColor: 'rgba(78, 115, 223, 1)', // SB Admin 2's primary color
                        backgroundColor: 'rgba(78, 115, 223, 0.05)', // Light fill for the line
                        borderWidth: 3,
                        pointBackgroundColor: 'rgba(78, 115, 223, 1)', // Color for the points
                        tension: 0.4, // Smooth the line a little bit
                        fill: true
                    }]
                },
                options: {
                    maintainAspectRatio: false,
                    responsive: true,
                    scales: {
                        x: {
                            grid: {
                                display: false
                            }
                        },
                        y: {
                            beginAtZero: true,
                            ticks: {
                                callback: function (value) {
                                    return '$' + value.toLocaleString(); // Format y-axis values as currency
                                }
                            }
                        }
                    },
                    plugins: {
                        legend: {
                            display: true,
                            position: 'top',
                            labels: {
                                color: '#858796', // Match SB Admin 2's gray text color
                            }
                        }
                    }
                }
            });
        },
        error: function (error) {
            console.log("Error fetching user balance data:", error);
        }
    });
});

// Revenue Sources Doughnut Chart
const revenueCtx = document.getElementById('revenueChart').getContext('2d');
const revenueChart = new Chart(revenueCtx, {
    type: 'doughnut',
    data: {
        labels: ['Direct', 'Social', 'Referral'],
        datasets: [{
            data: [50, 30, 20],
            backgroundColor: ['#4e73df', '#1cc88a', '#36b9cc'], // Use SB Admin 2's colors
            hoverBackgroundColor: ['#2e59d9', '#17a673', '#2c9faf'],
            hoverBorderColor: 'rgba(234, 236, 244, 1)',
        }]
    },
    options: {
        maintainAspectRatio: false,
        responsive: true,
        plugins: {
            legend: {
                display: true,
                position: 'right',
                labels: {
                    color: '#858796'
                }
            },
        },
        cutout: '75%', // Make it look like a "donut"
    }
});

$(document).ready(function () {
    // Load current month budget lines
    $.ajax({
        url: '/currentMonthBudgets',
        type: 'GET',
        success: function (data) {
            var profitsList = $('#profits-list');
            var expensesList = $('#losses-list');

            // Clear existing list items
            profitsList.empty();
            expensesList.empty();

            // Loop through the data and append items to the appropriate list
            data.forEach(function (budget) {
                if (budget.type === 'profit') {
                    profitsList.append('<li>' + budget.description + ' - $' + budget.amount + '</li>');
                } else if (budget.type === 'loss') {
                    expensesList.append('<li>' + budget.description + ' - $' + budget.amount + '</li>');
                }
            });
        },
        error: function (error) {
            console.log("Error fetching budget lines:", error);
        }
    });
});

