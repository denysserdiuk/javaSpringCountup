document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('sidebarToggle').addEventListener('click', function() {
        const sidebar = document.querySelector('.sidebar');
        sidebar.classList.toggle('sidebar--collapsed');
    });
});