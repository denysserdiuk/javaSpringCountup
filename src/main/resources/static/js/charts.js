// Earnings Overview Chart
$(document).ready(function () {
    $.ajax({
        url: '/api/userBalance',
        type: 'GET',
        success: function (data) {
            const earningsCtx = document.getElementById('earningsChart').getContext('2d');

            // Use your palette colors
            const borderColor = '#1A3636'; // --primary-dark
            const backgroundColor = '#D6BD98'; // --primary-cream

            // Create gradient for the line
            const gradientStroke = earningsCtx.createLinearGradient(0, 0, 0, 400);
            gradientStroke.addColorStop(0, borderColor);
            gradientStroke.addColorStop(1, backgroundColor);

            // Create gradient for the fill
            const gradientFill = earningsCtx.createLinearGradient(0, 0, 0, 400);
            gradientFill.addColorStop(0, 'rgba(214, 189, 152, 0.5)'); // --primary-cream with opacity
            gradientFill.addColorStop(1, 'rgba(214, 189, 152, 0)');

            const earningsChart = new Chart(earningsCtx, {
                type: 'line',
                data: {
                    labels: Object.keys(data),
                    datasets: [{
                        label: 'Balance',
                        data: Object.values(data),
                        borderColor: gradientStroke,
                        backgroundColor: gradientFill,
                        borderWidth: 3,
                        pointBackgroundColor: '#FFFFFF',
                        pointBorderColor: borderColor,
                        pointHoverBackgroundColor: borderColor,
                        pointHoverBorderColor: '#FFFFFF',
                        pointRadius: 6,
                        pointHoverRadius: 8,
                        tension: 0.4,
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
                            },
                            ticks: {
                                font: {
                                    family: 'var(--bs-body-font-family)',
                                    size: 12,
                                    weight: '500'
                                },
                                color: '#6c757d'
                            }
                        },
                        y: {
                            beginAtZero: true,
                            ticks: {
                                font: {
                                    family: 'var(--bs-body-font-family)',
                                    size: 12,
                                    weight: '500'
                                },
                                color: '#6c757d',
                                callback: function (value) {
                                    return '$' + value.toLocaleString();
                                }
                            },
                            grid: {
                                color: 'rgba(0, 0, 0, 0.05)'
                            }
                        }
                    },
                    plugins: {
                        legend: {
                            display: true,
                            position: 'top',
                            labels: {
                                color: '#343a40',
                                font: {
                                    family: 'var(--bs-body-font-family)',
                                    size: 14,
                                    weight: '600'
                                }
                            }
                        },
                        tooltip: {
                            backgroundColor: '#FFFFFF',
                            titleColor: '#343a40',
                            bodyColor: '#343a40',
                            borderColor: borderColor,
                            borderWidth: 1,
                            titleFont: {
                                family: 'var(--bs-body-font-family)',
                                size: 14,
                                weight: '600'
                            },
                            bodyFont: {
                                family: 'var(--bs-body-font-family)',
                                size: 12,
                                weight: '500'
                            },
                            callbacks: {
                                label: function (context) {
                                    let value = context.parsed.y;
                                    return '$' + value.toLocaleString();
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

// Donut Chart
$(document).ready(function () {
    $.ajax({
        url: 'api/CurrentMonthLossesByCategory',
        type: 'GET',
        success: function (data) {
            var categories = Object.keys(data);
            var percentages = Object.values(data);

            var colorPalette = [
                '#1A3636',          // --primary-dark
                '#40534C',          // --primary-darker-green
                '#677D6A',          // --primary-light-green
                '#D6BD98',          // --primary-cream
                '#495057',          // --bs-gray-secondary
                '#5eb535',          // --filler-color
            ];

            // Extend the palette if necessary
            while (colorPalette.length < categories.length) {
                colorPalette = colorPalette.concat(colorPalette);
            }

            var colors = categories.map(function (_, index) {
                return colorPalette[index % colorPalette.length];
            });

            const revenueCtx = document.getElementById('revenueChart').getContext('2d');
            const revenueChart = new Chart(revenueCtx, {
                type: 'doughnut',
                data: {
                    labels: categories,
                    datasets: [{
                        data: percentages,
                        backgroundColor: colors,
                        hoverBackgroundColor: colors.map(color => shadeColor(color, -10)),
                        hoverBorderColor: '#FFFFFF',
                        borderWidth: 2,
                    }]
                },
                options: {
                    maintainAspectRatio: false,
                    responsive: true,
                    aspectRatio: 1,
                    plugins: {
                        legend: {
                            display: true,
                            position: 'right',
                            labels: {
                                color: '#343a40',
                                font: {
                                    family: 'var(--bs-body-font-family)',
                                    size: 14,
                                    weight: '500'
                                },
                                usePointStyle: true,
                                pointStyle: 'circle',
                                generateLabels: function (chart) {
                                    return chart.data.labels.map(function (label, i) {
                                        let value = chart.data.datasets[0].data[i];
                                        return {
                                            text: label + ': ' + Math.round(value) + '%',
                                            fillStyle: chart.data.datasets[0].backgroundColor[i],
                                            strokeStyle: chart.data.datasets[0].backgroundColor[i],
                                            lineWidth: 1,
                                        };
                                    });
                                }
                            }
                        },
                        tooltip: {
                            backgroundColor: '#FFFFFF',
                            titleColor: '#343a40',
                            bodyColor: '#343a40',
                            borderColor: '#dddddd',
                            borderWidth: 1,
                            titleFont: {
                                family: 'var(--bs-body-font-family)',
                                size: 14,
                                weight: '600'
                            },
                            bodyFont: {
                                family: 'var(--bs-body-font-family)',
                                size: 12,
                                weight: '500'
                            },
                            callbacks: {
                                label: function (tooltipItem) {
                                    let value = tooltipItem.raw;
                                    return tooltipItem.label + ': ' + Math.round(value) + '%';
                                }
                            }
                        }
                    },
                    cutout: '70%',
                }
            });
        },
        error: function (error) {
            console.log("Error fetching loss category data:", error);
        }
    });

    // Helper function to shade a color
    function shadeColor(color, percent) {
        let R = parseInt(color.substring(1,3),16);
        let G = parseInt(color.substring(3,5),16);
        let B = parseInt(color.substring(5,7),16);

        R = parseInt(R * (100 + percent) / 100);
        G = parseInt(G * (100 + percent) / 100);
        B = parseInt(B * (100 + percent) / 100);

        R = (R<255)?R:255;
        G = (G<255)?G:255;
        B = (B<255)?B:255;

        let RR = ((R.toString(16).length==1)?"0"+R.toString(16):R.toString(16));
        let GG = ((G.toString(16).length==1)?"0"+G.toString(16):G.toString(16));
        let BB = ((B.toString(16).length==1)?"0"+B.toString(16):B.toString(16));

        return "#"+RR+GG+BB;
    }
});
