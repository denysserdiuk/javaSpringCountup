$(document).ready(function() {
    // Fetch shares on page load
    function loadShares() {
        $.ajax({
            url: '/user-shares',
            method: 'GET',
            success: function(data) {
                let sharesList = $('#shares-list');
                sharesList.empty();
                data.forEach(function(share) {
                    let row = `
                        <tr>
                            <td>${share.ticker}</td>
                            <td>${share.amount}</td>
                            <td>${share.purchaseDate}</td>
                            <td>${share.price}</td>
                            <td id="price-${share.id}">Loading...</td>
                            <td>${share.profit}</td>
                        </tr>
                    `;
                    sharesList.append(row);
                    loadStockPrice(share.ticker, share.id); // Fetch stock price for each ticker
                });
            }
        });
    }

    // Fetch stock price from external API using the cached data
    function loadStockPrice(ticker, shareId) {
        $.ajax({
            url: `/api/stock-price?ticker=${ticker}`,
            method: 'GET',
            success: function(data) {
                if (data && data.price) {
                    $(`#price-${shareId}`).text(data.price);
                } else {
                    $(`#price-${shareId}`).text('Price unavailable');
                }
            },
            error: function(err) {
                $(`#price-${shareId}`).text('Error loading price');
            }
        });
    }

    loadShares(); // Load shares and their prices on page load
});
