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
                        borderColor: '#495057', // SB Admin 2's primary color
                        backgroundColor: 'rgba(78, 115, 223, 0.05)', // Light fill for the line
                        borderWidth: 3,
                        pointBackgroundColor: '#5eb535', // Color for the points
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
                                color: '#858796',
                                font: {
                                    family: 'Roboto Slab',
                                    size: 14
                                }
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



//DONUT CHART HOME
$(document).ready(function () {
    // Load current month loss category percentages
    $.ajax({
        url: 'api/CurrentMonthLossesByCategory',
        type: 'GET',
        success: function (data) {
            // The returned data is a map of category names to percentages
            var categories = Object.keys(data);  // Get the category names
            var percentages = Object.values(data); // Get the percentages for each category

            // Define a list of colors for the chart
            var colorPalette = [
                '#5eb535', '#495057', '#2d651b', '#212529', '#D6BD98', '#677D6A', '#40534C'
            ];

            // Extend the palette with more colors if needed (up to 115 colors)
            // You can repeat the colors if the number of categories exceeds the color list length
            while (colorPalette.length < 115) {
                colorPalette = colorPalette.concat(colorPalette);
            }

            // Select colors from the predefined palette (one color per category)
            var colors = categories.map(function (_, index) {
                return colorPalette[index % colorPalette.length]; // Cycle through the palette
            });

            // Update the doughnut chart with the dynamic data
            const revenueCtx = document.getElementById('revenueChart').getContext('2d');
            const revenueChart = new Chart(revenueCtx, {
                type: 'doughnut',
                data: {
                    labels: categories, // The category names
                    datasets: [{
                        data: percentages, // The percentages of losses per category
                        backgroundColor: colors, // Use colors from the predefined palette
                        hoverBackgroundColor: colors, // Use the same colors for hover
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
                                color: '#858796',
                                font: {
                                    family: 'Roboto Slab',  // Reference the custom font from global.css
                                    size: 14,
                                },
                                generateLabels: function (chart) {
                                    return chart.data.labels.map(function (label, i) {
                                        let value = chart.data.datasets[0].data[i];
                                        return {
                                            text: label + ': ' + Math.round(value) + '%',
                                            fillStyle: chart.data.datasets[0].backgroundColor[i],
                                        };
                                    });
                                }
                            }
                        },
                        tooltip: {
                            callbacks: {
                                label: function (tooltipItem) {
                                    let value = tooltipItem.raw;
                                    return tooltipItem.label + ': ' + Math.round(value) + '%';
                                }
                            }
                        }
                    },
                    cutout: '75%',
                }
            });
        },
        error: function (error) {
            console.log("Error fetching loss category data:", error);
        }
    });
});
